package com.example.mypages.simpleList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypages.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SimpleListEditActivity extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference reference;
    LinearLayout linearLayout;
    List<Object> list = new ArrayList<>();
    LinearLayout currentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_list_edit);

        String key = getIntent().getStringExtra("listId");

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid()).child("simpleList_folder").child(key).child("data");

        linearLayout = findViewById(R.id.simpleList_linearLayout);
        currentLayout = linearLayout;
        linearLayout.setOnClickListener(view -> changeEditableLayout(linearLayout));

        changeEditableLayout(linearLayout);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                Object snapshotValue = snapshot.getValue();
                if (snapshotValue instanceof List) {
                    list = (List<Object>) snapshotValue;
                }
                if (list == null) {
                    list = new ArrayList<>();
                }
                createList(linearLayout, list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    void createList(LinearLayout layout, List<Object> list) {
        for (Object listItem : list) {
            if (listItem instanceof String) {
                String data = (String) listItem;
                TextView textView = createTextView(data);
                layout.addView(textView);
                textView.setOnLongClickListener(view -> {
                    showTextEditDialog(textView, layout);
                    return true;
                });
            } else if (listItem instanceof List) {
                LinearLayout innerLinearLayout = createLinearLayout();
                layout.addView(innerLinearLayout);
                createList(innerLinearLayout, (List<Object>) listItem);
            }
        }
    }

    void changeEditableLayout(LinearLayout layout) {
//        currentLayout.setForeground(getResources().getDrawable(R.drawable.simple_list_intend_line_inactive, this.getTheme()));
        currentLayout.setForeground(null);
        currentLayout = layout;
        currentLayout.setForeground(getResources().getDrawable(R.drawable.simple_list_intend_line, this.getTheme()));
    }

    TextView createTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 4, 0, 0);
        textView.setLayoutParams(layoutParams);
        textView.setPaddingRelative(18, 0,0,0);
        textView.setForeground(getResources().getDrawable(R.drawable.simple_list_list_item, this.getTheme()));
        textView.setTextColor(getResources().getColor(R.color.ice, this.getTheme()));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
        return textView;
    }

    LinearLayout createLinearLayout() {
        LinearLayout innerLinearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(55, 0, 0, 0);
        innerLinearLayout.setLayoutParams(layoutParams);
        innerLinearLayout.setOrientation(LinearLayout.VERTICAL);
        innerLinearLayout.setOnClickListener(view -> changeEditableLayout(innerLinearLayout));
//        innerLinearLayout.setForeground(getResources().getDrawable(R.drawable.simple_list_intend_line_inactive, getTheme()));
        return innerLinearLayout;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_action_add) {
            showAddListItemDialog();
            return true;
        } else if (id == R.id.menu_action_addList) {
            LinearLayout innerLinearLayout = createLinearLayout();
            currentLayout.addView(innerLinearLayout);
            Toast.makeText(this, "Sub list created", Toast.LENGTH_SHORT).show();
            changeEditableLayout(innerLinearLayout);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showAddListItemDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.text_input, null);

        EditText input = dialogView.findViewById(R.id.dialogueTextInput_editText);
        Button btnOk = dialogView.findViewById(R.id.dialogueTextInput_buttonOk);
        Button btnCancel = dialogView.findViewById(R.id.dialogueTextInput_buttonCancel);

        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        btnCancel.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        btnOk.setOnClickListener(view -> {
            String value = input.getText().toString();
            if (value.equals("")) {
                Toast.makeText(this, "Please enter list item", Toast.LENGTH_SHORT).show();
            } else {
                TextView textView = createTextView(value);
                currentLayout.addView(textView);
                textView.setOnLongClickListener(v -> {
                    showTextEditDialog(textView, currentLayout);
                    return false;
                });
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        list = generateListFromLayout(linearLayout);
        reference.setValue(list);
        super.onBackPressed();
    }

    List<Object> generateListFromLayout(LinearLayout layout) {
        List<Object> newList = new ArrayList<>();
        for (int i=0; i<layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof TextView) {
                TextView textView = (TextView) child;
                newList.add(textView.getText().toString());
            } else if (child instanceof LinearLayout) {
                newList.add(generateListFromLayout((LinearLayout) child));
            }
        }
        return newList;
    }

    public void showTextEditDialog(TextView textView, LinearLayout parentLayout) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.text_input, null);

        EditText input = dialogView.findViewById(R.id.dialogueTextInput_editText);
        Button btnOk = dialogView.findViewById(R.id.dialogueTextInput_buttonOk);
        Button btnCancel = dialogView.findViewById(R.id.dialogueTextInput_buttonCancel);

        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        input.setText(textView.getText().toString());

        btnCancel.setText("Delete");
        btnOk.setText("Confirm");
        btnCancel.setOnClickListener(view -> {
            parentLayout.removeView(textView);
            alertDialog.dismiss();
        });
        btnOk.setOnClickListener(view -> {
            textView.setText(input.getText().toString());
            alertDialog.dismiss();
        });

        alertDialog.show();
    }
}