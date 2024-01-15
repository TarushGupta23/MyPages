package com.example.mypages.simpleList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(auth.getUid()).child("simpleList_folder").child(key);

        linearLayout = findViewById(R.id.simpleList_linearLayout);
        currentLayout = linearLayout;

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object snapshotValue = snapshot.getValue();
                if (snapshotValue instanceof List) {
                    list = (List<Object>) snapshotValue;
                }
                if (list != null) {
                    createList(linearLayout, list);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    void createList(LinearLayout layout, List<Object> list) {
        layout.setOnClickListener(view -> {
            changeEditableLayout(layout);
        });
        for (Object listItem : list) {
            if (listItem instanceof String) {
                String data = (String) listItem;
                TextView textView = new TextView(this);
                textView.setText(data);
                textView.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setTextColor(getResources().getColor(R.color.ice));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                layout.addView(textView);
            } else if (listItem instanceof List) {
                LinearLayout innerLinearLayout = new LinearLayout(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, 10, 0);
                innerLinearLayout.setLayoutParams(layoutParams);
                innerLinearLayout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(innerLinearLayout);
                createList(innerLinearLayout, (List<Object>) listItem);
            }
        }
    }

    void changeEditableLayout(LinearLayout layout) {
        currentLayout.setForeground(null);
        currentLayout = layout;
        currentLayout.setForeground(getResources().getDrawable(R.drawable.simple_list_intend_line));
    }


}