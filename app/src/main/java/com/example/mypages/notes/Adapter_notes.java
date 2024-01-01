package com.example.mypages.notes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypages.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.List;

public class Adapter_notes extends RecyclerView.Adapter<Adapter_notes.ViewHolder> {
    private Context context;
    private List<Model_Note> list;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();;
    DatabaseReference databaseReference = database.getReference("Users").child(auth.getUid()).child("notes_folder");
    public Adapter_notes(List<Model_Note> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView noteCard;
        TextView noteHeading, noteBody, noteDateCreated;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            noteCard = itemView.findViewById(R.id.cardView_noteCard);
            noteHeading = itemView.findViewById(R.id.textView_noteCardHeading);
            noteBody = itemView.findViewById(R.id.textView_noteCardDescription);
            noteDateCreated = itemView.findViewById(R.id.textView_noteCardDateCreated);
        }

        public void bind(Model_Note item) {
            noteHeading.setText(item.getNoteHeading());
            String body = item.getNoteBody().equals(Model_Note.EMPTY_STRING)? "" : item.getNoteBody();
            noteBody.setText(body);
            noteDateCreated.setText(dateFormat.format(item.getDateCreated()));
            noteCard.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    showDeleteDialog(item);
                    return false;
                }
            });
            noteCard.setOnClickListener(view -> {
                Toast.makeText(context, item.getNoteHeading(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    public void showDeleteDialog(Model_Note item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Do you want to delete note").setTitle("Delete Note")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        databaseReference.child(item.getKey()).setValue(null);
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton("No", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                }).create().show();
    }
}
