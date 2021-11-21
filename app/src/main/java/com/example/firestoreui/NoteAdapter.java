package com.example.firestoreui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NoteAdapter extends FirestoreRecyclerAdapter<NoteModel, NoteAdapter.NoteHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<NoteModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder holder, int position, @NonNull NoteModel model) {
        /** 2 **/
        holder.tvTitle.setText(model.getTitle());
        holder.tvDescription.setText(model.getDescription());
        holder.tvPriority.setText(String.valueOf(model.getPriority()));
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /** 3 **/
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_note, parent, false);
        return new NoteHolder(view);
    }

    /** 4 Ajout du swipe to Delete **/
    public void deleteNote(int position){
        // Pour supprimer une entrée de la base nous avons d'une DocumentReference
        getSnapshots().getSnapshot(position).getReference().delete();
    } // puis dans main activity

    class NoteHolder extends RecyclerView.ViewHolder{
        /** 1 **/
        TextView tvTitle, tvDescription, tvPriority;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            tvPriority = itemView.findViewById(R.id.tvPriority);
        }
    }

}
