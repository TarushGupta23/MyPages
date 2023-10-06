package com.example.mypages.todoList;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mypages.ColorPickClass;
import com.example.mypages.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class TodoList_ViewPagerFragment extends Fragment {
    RecyclerView recyclerView;
    DatabaseReference databaseReference, addReference;
    FirebaseAuth auth;
    private Adapter_TodoList_RecyclerView adapter;
    private List<Model_todoList> itemList;
    String path;

    public TodoList_ViewPagerFragment(String path) {
        super();
        this.path = path;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo_list_view_pager, container, false);
        setHasOptionsMenu(true);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid()).child("todo_list_folder").child(path);
        addReference = FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid()).child("todo_list_folder").child("active");

        itemList = new ArrayList<>();
        adapter = new Adapter_TodoList_RecyclerView(itemList, getContext(), path);

//        Model_todoList testItem = new Model_todoList();
//        testItem.setTitle("homework");
//        testItem.setCompressed(true);
//        testItem.setDescription("this is ment to make me do homework");
//        testItem.setDeadLine(new Date());
//        ArrayList<Model_todoList.SubTask> testsubtask = new ArrayList<>();
//        Model_todoList.SubTask task2 = new Model_todoList.SubTask(), task3 = new Model_todoList.SubTask();
//        task3.setCompleted(false);
//        task3.setSubTaskName("task3");
//        task2.setCompleted(true);
//        task2.setSubTaskName("task2");
//        testsubtask.add(task3);
//        testsubtask.add(task2);
//        testsubtask.add(task2);
//        testItem.setSubTaskList(testsubtask);
//
//        itemList.add(testItem);

        recyclerView = view.findViewById(R.id.todoListFragment_recyclerView);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String key = dataSnapshot.getKey();
                    Model_todoList item = dataSnapshot.getValue(Model_todoList.class);
                    if (item != null) {
                        item.setKey(key);
                        itemList.add(item);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_action_add) {
            showEditDialog();
            return true;
        }
        else if (id == R.id.menu_action_mic) {
            Intent i = new Intent(getContext(), TodoList_VoiceCreation.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }


    private void showEditDialog() {
        Model_todoList newItem = new Model_todoList(getContext());

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.create_todo_list_dialogue, null);

        EditText name, description, deadlineDate, deadlineTime, notifyBefore;
        CardView primaryColor, secondaryColor, notifyExpand, repeatExpand;
        CheckBox notification, repeat;
        RadioGroup notifyRG, repeatRG;
        Button save, discard;
        RadioButton minute, hour, daily, weekly, monthly;

        name = dialogView.findViewById(R.id.todoListCreateDialogue_taskName);
        description = dialogView.findViewById(R.id.todoListCreateDialogue_taskDescription);
        deadlineDate = dialogView.findViewById(R.id.todoListCreateDialogue_deadlineDate);
        deadlineTime = dialogView.findViewById(R.id.todoListCreateDialogue_deadlineTime);
        notifyBefore = dialogView.findViewById(R.id.todoListCreateDialogue_notifyBefore);
        primaryColor = dialogView.findViewById(R.id.todoListCreateDialogue_colorPrimary);
        secondaryColor = dialogView.findViewById(R.id.todoListCreateDialogue_colorSecondary);
        notifyExpand = dialogView.findViewById(R.id.todoListCreateDialogue_notificationExpand);
        repeatExpand = dialogView.findViewById(R.id.todoListCreateDialogue_repeatExpand);
        notification = dialogView.findViewById(R.id.todoListCreateDialogue_enableNotification);
        repeat = dialogView.findViewById(R.id.todoListCreateDialogue_enableRepeat);
        notifyRG = dialogView.findViewById(R.id.todoListCreateDialogue_notifyRG);
        repeatRG = dialogView.findViewById(R.id.todoListCreateDialogue_repeatRG);
        save = dialogView.findViewById(R.id.todoListCreateDialogue_save);
        discard = dialogView.findViewById(R.id.todoListCreateDialogue_discard);
        minute = dialogView.findViewById(R.id.todoListCreateDialogue_notifyRG_minute);
        hour = dialogView.findViewById(R.id.todoListCreateDialogue_notifyRG_hour);
        daily = dialogView.findViewById(R.id.todoListCreateDialogue_repeatRG_daily);
        weekly = dialogView.findViewById(R.id.todoListCreateDialogue_repeatRG_weekly);
        monthly = dialogView.findViewById(R.id.todoListCreateDialogue_repeatRG_monthly);

        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        discard.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        save.setOnClickListener(view -> {
            if (name.getText().toString().equals("")) {
                Toast.makeText(getContext(), "Please enter task name", Toast.LENGTH_SHORT).show();
            } else if (notification.isChecked() && deadlineTime.getText().toString().equals("")) {
                Toast.makeText(getContext(), "Enter time to receive notification", Toast.LENGTH_SHORT).show();
            } else {
                String dateInput = deadlineDate.getText().toString();
                String timeInput = deadlineTime.getText().toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Date selectDate, selectTime, deadline;
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat dateFormat3 = new SimpleDateFormat("HH:mm");

                if (dateInput.equals("")) {
                    dateInput = dateFormat2.format(new Date());
                }
                if (timeInput.equals("")) {
                    timeInput = "23:59";
                }
                try {
                    selectDate = dateFormat2.parse(dateInput);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Enter date in proper dd/mm/yyyy format", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    selectTime = dateFormat3.parse(timeInput);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Enter time in proper HH:mm format", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    deadline = dateFormat.parse(dateFormat2.format(selectDate) + " " + dateFormat3.format(selectTime));
                } catch (Exception e) {
                    Toast.makeText(getContext(), "exception occurred", Toast.LENGTH_SHORT).show();
                    deadline = new Date();
                }

                newItem.setNotificationEnabled(notification.isChecked());
                if (notification.isChecked()) {
                    int pos = minute.isChecked() ? 0 : 1;
                    newItem.setNotificationBeforeType(pos);
                    if (notifyBefore.getText().toString().equals("")) {
                        newItem.setNotificationBefore(0);
                    } else {
                        newItem.setNotificationBefore(Integer.parseInt(notifyBefore.getText().toString()));
                    }
                }

                newItem.setRepeatEnabled(repeat.isChecked());
                if (repeat.isChecked()) {
                    int pos;
                    if (daily.isChecked()) {
                        pos = 0;
                    } else if (weekly.isChecked()) {
                        pos = 1;
                    } else if (monthly.isChecked()) {
                        pos = 2;
                    } else {
                        pos = 3;
                    }
                    newItem.setRepeatType(pos);
                }

                newItem.setHasDeadline(!deadlineDate.getText().toString().equals("") || !deadlineTime.getText().toString().equals(""));
                newItem.setTitle(name.getText().toString());
                newItem.setDescription(description.getText().toString());
                newItem.setDateCreated(new Date());
                newItem.setDateCompleted(null);
                newItem.setCompressed(false);
                newItem.setDeadLine(deadline);
                newItem.setPrimaryColor(primaryColor.getCardBackgroundColor().getDefaultColor());
                newItem.setSecondaryColor(secondaryColor.getCardBackgroundColor().getDefaultColor());
                alertDialog.dismiss();
                addReference.push().setValue(newItem);
            }
        });

        notification.setOnClickListener(view -> {
            if (notification.isChecked()) {
                notifyBefore.setVisibility(View.VISIBLE);
                notifyExpand.setVisibility(View.VISIBLE);
                notifyExpand.setRotation(-90);
                notifyRG.setVisibility(View.GONE);
            } else {
                notifyBefore.setVisibility(View.INVISIBLE);
                notifyExpand.setVisibility(View.INVISIBLE);
                notifyRG.setVisibility(View.GONE);
            }
        });

        notifyExpand.setOnClickListener(view -> {
            if (notifyExpand.getRotation() == -90) {
                notifyRG.setVisibility(View.VISIBLE);
                notifyExpand.setRotation(90);
            } else {
                notifyRG.setVisibility(View.GONE);
                notifyExpand.setRotation(-90);
            }
        });

        hour.setOnClickListener(view -> {
            notifyBefore.setHint("__ hrs before");
        });

        minute.setOnClickListener(view -> {
            notifyBefore.setHint("__ min before");
        });

        repeat.setOnClickListener(view -> {
            if (repeat.isChecked()) {
                repeatExpand.setVisibility(View.VISIBLE);
                repeatExpand.setRotation(-90);
                repeatRG.setVisibility(View.GONE);
            } else {
                repeatExpand.setVisibility(View.INVISIBLE);
                notifyRG.setVisibility(View.GONE);
            }
        });

        repeatExpand.setOnClickListener(view -> {
            if (repeatExpand.getRotation() == -90) {
                repeatRG.setVisibility(View.VISIBLE);
                repeatExpand.setRotation(90);
            } else {
                repeatRG.setVisibility(View.GONE);
                repeatExpand.setRotation(-90);
            }
        });

        primaryColor.setOnClickListener(view -> {
            ColorPickClass colorPickClass = new ColorPickClass(
                    getContext(), ContextCompat.getColor(getContext(), R.color.deepBlue3), newItem.getPrimaryColor(),
                    new ColorPickClass.OnSaveClickListener() {
                        @Override
                        public void onSaveClicked(int newColor) {
                            newItem.setPrimaryColor(newColor);
                            primaryColor.setCardBackgroundColor(newColor);
                        }
                    },
                    new ColorPickClass.OnDefaultClickListener() {
                        @Override
                        public void onDefaultClicked(int color) {
                            newItem.setPrimaryColor(color);
                            primaryColor.setCardBackgroundColor(color);
                        }
                    },
                    new ColorPickClass.OnCancelClickListener() {
                        @Override
                        public void onCancelClicked(int color) {
                            newItem.setPrimaryColor(color);
                            primaryColor.setCardBackgroundColor(color);
                        }
                    }
            );
            colorPickClass.show();
        });

        secondaryColor.setOnClickListener(view -> {
            ColorPickClass colorPickClass = new ColorPickClass(
                    getContext(), ContextCompat.getColor(getContext(), R.color.ice), newItem.getSecondaryColor(),
                    new ColorPickClass.OnSaveClickListener() {
                        @Override
                        public void onSaveClicked(int newColor) {
                            newItem.setSecondaryColor(newColor);
                            secondaryColor.setCardBackgroundColor(newColor);
                        }
                    },
                    new ColorPickClass.OnDefaultClickListener() {
                        @Override
                        public void onDefaultClicked(int color) {
                            newItem.setSecondaryColor(color);
                            secondaryColor.setCardBackgroundColor(color);
                        }
                    },
                    new ColorPickClass.OnCancelClickListener() {
                        @Override
                        public void onCancelClicked(int color) {
                            newItem.setSecondaryColor(color);
                            secondaryColor.setCardBackgroundColor(color);
                        }
                    }
            );
            colorPickClass.show();
        });

        alertDialog.show();
    }

}