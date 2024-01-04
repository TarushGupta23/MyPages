package com.example.mypages.notes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mypages.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NotesEditActivity extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    private RecyclerView imageRecyclerView;
    private EditText noteHeading, noteBody;
    Model_Note noteItem;
    boolean changesSaved = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_edit);

        auth = FirebaseAuth.getInstance();
        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("note")) {
            noteItem = (Model_Note) intent.getSerializableExtra("note");
        } else {
            Toast.makeText(this, "Unable to fetch note", Toast.LENGTH_SHORT).show();
            finish();
        }
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid()).child("notes_folder").child(noteItem.getKey());
        noteHeading = findViewById(R.id.editText_noteHeading);
        noteBody = findViewById(R.id.editText_noteBody);

        noteHeading.setText(noteItem.getNoteHeading());
        if (noteItem.getNoteBody().equals(Model_Note.EMPTY_STRING)) {
            noteBody.setText("");
        } else {
            noteBody.setText(noteItem.getNoteBody());
        }

        noteBody.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                changesSaved = false;
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        noteHeading.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                changesSaved = false;
            }
            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    @Override
    public void onBackPressed() {
        if (noteHeading.getText().toString().equals("New Note") && noteBody.getText().toString().equals("")) {
            databaseReference.setValue(null);
            super.onBackPressed();
            return;
        }
        if (noteItem.isNew()) {
            showLockDialog();
        } else if (!changesSaved) {
            showSaveChangesDialog();
        } else {
            super.onBackPressed();
        }
    }

    private void showLockDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(this).inflate(R.layout.text_input, null);
        builder.setView(v);
        AlertDialog dialog = builder.create();

        Button ok, cancel;
        EditText password = v.findViewById(R.id.dialogueTextInput_editText);
        ok = v.findViewById(R.id.dialogueTextInput_buttonOk);
        cancel = v.findViewById(R.id.dialogueTextInput_buttonCancel);

        password.setHint("Set Password?");
        ok.setOnClickListener(view -> {
            String pwd = password.getText().toString();
            if (!pwd.equals("")) {
                databaseReference.child("password").setValue(pwd);
                databaseReference.child("isLocked").setValue(true);
                noteItem.setLocked(true);
                dialog.dismiss();
                if (!changesSaved) {
                    showSaveChangesDialog();
                } else {
                    super.onBackPressed();
                }
            } else {
                Toast.makeText(this, "Enter the password", Toast.LENGTH_SHORT).show();
            }
        });
        cancel.setOnClickListener(view -> {
            dialog.dismiss();
            if (!changesSaved) {
                showSaveChangesDialog();
            } else {
                super.onBackPressed();
            }
        });
        dialog.show();
    }

    private void showSaveChangesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Save Changes??")
                .setPositiveButton("Save", (dialog, id) -> {
                    if (noteHeading.getText().toString().equals("")) {
                        noteItem.setNoteHeading("New Note");
                    } else {
                        noteItem.setNoteHeading(noteHeading.getText().toString());
                    }
                    if (noteBody.getText().toString().equals("")) {
                        noteItem.setNoteBody(Model_Note.EMPTY_STRING);
                    } else {
                        noteItem.setNoteBody(noteBody.getText().toString());
                    }

                    if (noteItem.isNew()) {
                        databaseReference.setValue(null);
                    } else {
                        databaseReference.setValue(noteItem);
                    }
                    dialog.dismiss();
                    super.onBackPressed();
                }).setNegativeButton("Cancel", (dialog, id) -> {
                    if (noteItem.isNew()) {databaseReference.setValue(null);}
                    dialog.dismiss();
                    super.onBackPressed();
                }).create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_action_saveNote) {
            String heading = noteHeading.getText().toString();
            String body = noteBody.getText().toString();
            if (heading.equals("New Note") && body.equals("")) {
                Toast.makeText(this, "Nothing to save", Toast.LENGTH_SHORT).show();
            } else {
                databaseReference.child("noteHeading").setValue(heading);
                databaseReference.child("noteBody").setValue( body.equals("") ? Model_Note.EMPTY_STRING : body );
                changesSaved = true;
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}