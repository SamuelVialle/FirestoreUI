package com.example.firestoreui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class NoteActivity extends AppCompatActivity {

    /** 0 Connection à Firebase via tools **/
    /** 1 Ajout des dépendances dans le gradle **/
    /** 2 Ajout des icones **/
    /** 3 Ajout du menu dans lequel on affiche le + et le save **/
    /** 4 On ajoute les rules dans Firestore en mode test **/
    /** 5 activity_main avec un coordianator layout **/
    /** 6 Ajout du layout item row **/
    /** 7 Création du Model **/
    /** 8 Ajout de l'adapter **/


    /** 9 Définition des VAR Globales **/
    // RecyclerView
    private RecyclerView recyclerView;
    // Firebase
    private FirebaseFirestore db;
    private CollectionReference notebookRef;
    // Adapter
    private NoteAdapter adapter;
    // Le FAB
    FloatingActionButton btnAddNote;
    // Le context
    Context context;

    /** 10 initialisation des VARS **/
    private void init(){
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        /** 17.1 **/
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManagerWrapper(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);

        db = FirebaseFirestore.getInstance();
        notebookRef = db.collection("Notebook");

        btnAddNote = findViewById(R.id.btnAddNote);

    }

    /** 12 Méthode pour la query dans la db et set de l'adapter **/
    private void setUpRecyclerView() {
        Query query = notebookRef.orderBy("priority", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions options = new FirestoreRecyclerOptions.Builder<NoteModel>()
                .setQuery(query, NoteModel.class)
                .build();

        adapter = new NoteAdapter(options);

        recyclerView.setAdapter(adapter);

        /** 18 Ajout du swipe toDelete en commencant par son ajout dans l'adpater **/
        /** 18.1 Ajout de la méthode ItemTouchHelper pour pour nous aider dans le swipe
         * cette méthode peut aussi être utilisée le drag and drop
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) { // Choix de la direction
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
                // Pour le drag and drop
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Pour le swipe
                adapter.deleteNote(viewHolder.getBindingAdapterPosition());
            }
        }).attachToRecyclerView(recyclerView);
    }

    /**####### CYLCES DE VIE #######**/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        /** 10.1 **/
        init();
        /** 12.1 ***/
        setUpRecyclerView();

        /** 11 Ajout du listener sur le clic du FAB **/
        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NoteActivity.this, AddNewNoteActivity.class));
            }
        });
    }

    /** 13 Lancement du listener de l'adpater pour voir si des choses ont changé dans la base**/
    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    /** 13.1 Arrêt du listener de l'adapter **/
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    /** 14 pour vérifier que tout fonctionne on peut ajouter une donnée à la main dans la console **/
    /** 15 Ajout de l'activité addNewNote */
    /** Part 2 **/
    /** 16 Ajout dans le manifest du retour depuis AddNewNote vers NoteActivity pour avoir la flèche de retour dans l'actionBar dans AddNote **/
    /** 17 Correction du bug du crash **/
    public class LinearLayoutManagerWrapper extends LinearLayoutManager {

        public LinearLayoutManagerWrapper(Context context) {
            super(context);
        }

        public LinearLayoutManagerWrapper(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public LinearLayoutManagerWrapper(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        @Override
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }
    }


}