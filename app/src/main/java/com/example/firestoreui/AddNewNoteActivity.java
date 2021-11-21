package com.example.firestoreui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;

public class AddNewNoteActivity extends AppCompatActivity {

    /** 1 Préparer le layout **/
    /**
     * 2 Définition des VARS
     **/
    private EditText etTitle, etDescription;
    private NumberPicker nbPriority;

    /**
     * 3 Initialisation des VARS
     */
    private void init() {
        etTitle = findViewById(R.id.edTitle);
        etDescription = findViewById(R.id.edDescription);
        nbPriority = findViewById(R.id.nbPriority);

        // Initialisation des min et max des priority
        nbPriority.setMinValue(1);
        nbPriority.setMaxValue(10);
    }

    /**
     * 4 Ajout du menu
     **/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_note_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * 4.1 Ajout du clic sur l'item du menu
     **/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_note:
                // On enregistrera les donnes dans Firestore
                saveNote();
                return true;
            // Ici on peut ajouter d'autre Item clickable
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    /** 5 Ajout de la méthode de sauvegarde **/
    private void saveNote() {
        String title = etTitle.getText().toString();
        String description = etDescription.getText().toString();
        int priority = nbPriority.getValue();

        if(title.trim().isEmpty() || description.isEmpty()){
            Toast.makeText(AddNewNoteActivity.this, "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }

        CollectionReference notebookRef = FirebaseFirestore.getInstance().collection("Notebook");
        notebookRef.add(new NoteModel(title, description, priority));
        Toast.makeText(AddNewNoteActivity.this, "Note added", Toast.LENGTH_SHORT).show();
        finish(); // On ferme cette activité pour retourner sur NoteActivity
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_note);
        /** Part 2 **/
        /** 6Ajout du support de l'actionBar pourla gestion du bouton retour **/
        /** Dans le quel on ajoute le bouton drawable close à la place de la flèche **/
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        // Ainsi que du titre
        setTitle("Add note");

        /** 3.1 initialisation appel */
        init();
    }
}