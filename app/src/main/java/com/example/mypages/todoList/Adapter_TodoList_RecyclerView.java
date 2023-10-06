package com.example.mypages.todoList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypages.ColorPickClass;

import com.example.mypages.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Adapter_TodoList_RecyclerView extends RecyclerView.Adapter<Adapter_TodoList_RecyclerView.ViewHolder> {
    private List<Model_todoList> itemList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference databaseReference;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    Context context;
    String path;

    public Adapter_TodoList_RecyclerView(List<Model_todoList> itemList, Context context, String path) {
        this.itemList = itemList;
        this.context = context;
        this.path = path;
        databaseReference = database.getReference("Users").child(auth.getUid()).child("todo_list_folder");
    }

    @NonNull
    @Override
    public Adapter_TodoList_RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_list_card, parent, false);
        return new Adapter_TodoList_RecyclerView.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_TodoList_RecyclerView.ViewHolder holder, int position) {
        Model_todoList item = itemList.get(position);

        holder.title.setText(item.getTitle());
        holder.description.setText(item.getDescription());
        if (!item.isCompressed()) {
            holder.attachmentCard.setVisibility(View.VISIBLE);
            holder.subtaskCard.setVisibility(View.VISIBLE);
            holder.description.setVisibility(View.VISIBLE);
            holder.expandButton.setRotation(90);
        } else {
            holder.attachmentCard.setVisibility(View.GONE);
            holder.subtaskCard.setVisibility(View.GONE);
            holder.description.setVisibility(View.GONE);
            holder.expandButton.setRotation(-90);
        }
        if (item.isHasDeadline()) {
            holder.deadline.setText("Deadline: " + dateFormat.format(item.getDeadLine()));
            Date currDate = new Date();
            if (currDate.compareTo(item.getDeadLine()) > 0 && !item.isCompleted() && !path.equals("pending")) {
                databaseReference.child("active").child(item.getKey()).setValue(null);
                databaseReference.child("pending").child(item.getKey()).setValue(item);
            }
        } else {
            holder.deadline.setText("Deadline: none");
        }
        if (item.isCompleted()) {
            holder.checkBox.setCardBackgroundColor(ContextCompat.getColor(context, R.color.coral));
        } else {
            holder.checkBox.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }
        ColorStateList colorStateList1;
        if (item.isNotificationEnabled()) {
            colorStateList1 = ColorStateList.valueOf(context.getResources().getColor(R.color.coral));
        } else {
            colorStateList1 = ColorStateList.valueOf(context.getResources().getColor(R.color.black));
        }
        holder.notificationIcon.setBackgroundTintList(colorStateList1);
        if (item.isRepeatEnabled()) {
            colorStateList1 = ColorStateList.valueOf(context.getResources().getColor(R.color.coral));
        } else {
            colorStateList1 = ColorStateList.valueOf(context.getResources().getColor(R.color.black));
        }
        holder.repeatIcon.setBackgroundTintList(colorStateList1);

        ArrayList<Model_todoList.SubTask> subTaskList = item.getSubTaskList();
        ArrayList attachmentList = item.getAttachments();
        if (attachmentList != null && attachmentList.size() > 0) {
            holder.attachmentRecyclerView.setVisibility(View.VISIBLE);
        } else {
            holder.attachmentRecyclerView.setVisibility(View.GONE);
        }

        if (subTaskList != null && subTaskList.size() > 0){
            holder.subtaskRecyclerView.setVisibility(View.VISIBLE);
            int progress = 0;
            for (int i = 0; i< subTaskList.size(); i++) {
                progress += subTaskList.get(i).isCompleted()?1:0;
            }
            holder.progressBar.setProgress(progress*100/subTaskList.size());
            holder.subtaskRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            Adapter_SubTask adapterSubTask = new Adapter_SubTask(item, context, path);
            holder.subtaskRecyclerView.setAdapter(adapterSubTask);
        } else {
            holder.progressBar.setProgress(0);
            holder.subtaskRecyclerView.setVisibility(View.GONE);
        }

        holder.checkBox.setOnClickListener(view -> {
            if (item.isCompleted()) {
                holder.checkBox.setCardBackgroundColor(ContextCompat.getColor(context, R.color.white));
                item.setCompleted(false);
                item.setDateCompleted(null);
                if (item.isHasDeadline() && new Date().compareTo(item.getDeadLine()) > 0) {
                    databaseReference.child("pending").child(item.getKey()).setValue(item);
                } else {
                    databaseReference.child("active").child(item.getKey()).setValue(item);
                }
                databaseReference.child("completed").child(item.getKey()).setValue(null);
            } else {
                holder.checkBox.setCardBackgroundColor(ContextCompat.getColor(context, R.color.coral));
                item.setCompleted(true);
                item.setDateCompleted(new Date());
                if (subTaskList != null && subTaskList.size() > 0){
                    for (int i = 0; i< subTaskList.size(); i++) {
                        if (!subTaskList.get(i).isCompleted()) {
                            subTaskList.get(i).setCompletionDate(new Date());
                            subTaskList.get(i).setCompleted(true);
                        }
                    }
                }
                databaseReference.child("completed").child(item.getKey()).setValue(item);
                databaseReference.child("active").child(item.getKey()).setValue(null);
                databaseReference.child("pending").child(item.getKey()).setValue(null);
            }
        });

        holder.expandButton.setOnClickListener(v -> {
            if (item.isCompressed()) {
                item.setCompressed(false);
                databaseReference.child(path).child(item.getKey()).setValue(item);
            } else {
                item.setCompressed(true);
                databaseReference.child(path).child(item.getKey()).setValue(item);
            }
        });

        holder.addSubtask.setOnClickListener(view -> {
            createSubtaskDialogue(holder.addSubtask, item);
        });

        holder.infoButton.setOnClickListener(view -> {
            showInfoDialogue(item, holder.infoButton);
        });

        holder.menuButton.setOnClickListener(view -> showEditDialog(item, holder.menuButton));

        holder.mainBody.setCardBackgroundColor(item.getPrimaryColor());
        holder.title.setTextColor(item.getSecondaryColor());
        holder.infoButton.setCardBackgroundColor(item.getSecondaryColor());
        holder.menuButton.setCardBackgroundColor(item.getSecondaryColor());
        holder.expandButton.setCardBackgroundColor(item.getSecondaryColor());
        holder.addAttachment.setCardBackgroundColor(item.getSecondaryColor());
        holder.addSubtask.setCardBackgroundColor(item.getSecondaryColor());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView mainBody, infoButton, menuButton, expandButton, addSubtask, addAttachment, checkBox, subtaskCard, attachmentCard;
        TextView title, deadline, description;
        RecyclerView attachmentRecyclerView, subtaskRecyclerView;
        ProgressBar progressBar;
        ConstraintLayout notificationIcon, repeatIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mainBody = itemView.findViewById(R.id.todoListCard_mainBody);
            infoButton = itemView.findViewById(R.id.todoListCard_itemInfo);
            menuButton = itemView.findViewById(R.id.todoListCard_menu);
            expandButton = itemView.findViewById(R.id.todoListCard_expandButton);
            addSubtask = itemView.findViewById(R.id.todoListCard_addSubtaskButton);
            addAttachment = itemView.findViewById(R.id.todoListCard_addAttachmentsButton);
            checkBox = itemView.findViewById(R.id.todoListCard_mainCheckBox);
            subtaskCard = itemView.findViewById(R.id.todoListCard_subtaskCard);
            attachmentCard = itemView.findViewById(R.id.todoListCard_attachmentsCard);

            progressBar = itemView.findViewById(R.id.todoListCard_progressBar);

            title = itemView.findViewById(R.id.todoListCard_itemName);
            deadline = itemView.findViewById(R.id.todoListCard_deadline);
            description = itemView.findViewById(R.id.todoListCard_description);

            attachmentRecyclerView = itemView.findViewById(R.id.todoListCard_attachmentsRecyclerView);
            subtaskRecyclerView = itemView.findViewById(R.id.todoListCard_subtaskRecyclerView);

            repeatIcon = itemView.findViewById(R.id.todoListCard_repeatIcon);
            notificationIcon = itemView.findViewById(R.id.todoListCard_notificationIcon);
        }
    }

    private void createSubtaskDialogue(CardView card, Model_todoList item) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(card.getContext());
        LayoutInflater inflater = LayoutInflater.from(card.getContext());
        View dialogView = inflater.inflate(R.layout.text_input, null);

        EditText editText = dialogView.findViewById(R.id.dialogueTextInput_editText);
        Button save = dialogView.findViewById(R.id.dialogueTextInput_buttonOk);
        Button cancel = dialogView.findViewById(R.id.dialogueTextInput_buttonCancel);

        editText.setInputType(InputType.TYPE_CLASS_TEXT);
        editText.setHint("Subtask Name");
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        save.setOnClickListener(v -> {
            String editedText = editText.getText().toString();
            Model_todoList.SubTask subTask = new Model_todoList.SubTask();
            subTask.setSubTaskName(editedText);
            item.setCompleted(false);
            item.getSubTaskList().add(subTask);
            if (item.isHasDeadline() && new Date().compareTo(item.getDeadLine()) > 0) {
                databaseReference.child("pending").child(item.getKey()).setValue(item);
            } else {
                databaseReference.child("active").child(item.getKey()).setValue(item);
            }
            databaseReference.child("completed").child(item.getKey()).setValue(null);
            alertDialog.dismiss();
        });

        cancel.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    private void showInfoDialogue(Model_todoList item, CardView card) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(card.getContext());
        LayoutInflater inflater = LayoutInflater.from(card.getContext());
        View dialogView = inflater.inflate(R.layout.todo_list_info_card, null);

        TextView itemTitle, completedDate, timeTaken,notification, notificationBefore, repeat, repeatType, createdDate;
        Button cancel = dialogView.findViewById(R.id.todoListItemInfo_close);
        RecyclerView recyclerView;
        LinearLayout box = dialogView.findViewById(R.id.todoListItemInfo_subtaskBox);

        itemTitle = dialogView.findViewById(R.id.todoListItemInfo_title);
        createdDate = dialogView.findViewById(R.id.todoListItemInfo_creationDate);
        completedDate = dialogView.findViewById(R.id.todoListItemInfo_completionDate);
        timeTaken = dialogView.findViewById(R.id.todoListItemInfo_completionTime);
        notification = dialogView.findViewById(R.id.todoListItemInfo_notification);
        notificationBefore = dialogView.findViewById(R.id.todoListItemInfo_notificationBefore);
        recyclerView = dialogView.findViewById(R.id.todoListInfoCard_recyclerView);
        repeat = dialogView.findViewById(R.id.todoListItemInfo_repeat);
        repeatType = dialogView.findViewById(R.id.todoListItemInfo_repeatType);


        itemTitle.setText(item.getTitle());
        createdDate.setText(dateFormat.format(item.getDateCreated()));
        if (item.isCompleted()) {
            completedDate.setText(dateFormat.format(item.getDateCompleted()));
            long timeDifferenceMillis = item.getDateCompleted().getTime() - item.getDateCreated().getTime();
            long millisecondsInADay = 24 * 60 * 60 * 1000; // Number of milliseconds in a day
            long daysDifference = timeDifferenceMillis / millisecondsInADay;
            long hoursDifference = (timeDifferenceMillis % millisecondsInADay) / (60 * 60 * 1000);
            timeTaken.setText(Long.toString(daysDifference) + " days " + Long.toString(hoursDifference) + " hrs");
        } else {
            completedDate.setText("in progress");
            timeTaken.setText("in progress");
        }

        if (item.isNotificationEnabled()) {
            notification.setText("Enabled");
            String s = Integer.toString(item.getNotificationBefore()) + " ";
            s +=  (item.getNotificationBeforeType()==0)?"Minutes":"Hours";
            notificationBefore.setText(s);
        } else {
            notification.setText("Disabled");
            notificationBefore.setText("none");
        }

        if (item.isRepeatEnabled()) {
            repeat.setText("Enabled");
            String type;
            switch (item.getRepeatType()) {
                case 0:
                    type = "daily"; break;
                case 1:
                    type = "weekly"; break;
                case 3:
                    type = "monthly"; break;
                default:
                    type = "yearly"; break;
            }
            repeatType.setText(type);
        } else {
            repeat.setText("Disabled");
            repeatType.setText("none");
        }

        if (!(item.getSubTaskList() == null) && item.getSubTaskList().size() > 0) {
            recyclerView.setLayoutManager(new LinearLayoutManager(card.getContext()));
            recyclerView.setAdapter(new Adapter_subtaskInfo(item.getSubTaskList(), item.getDateCreated().getTime()));
        } else {
            box.setVisibility(View.GONE);
        }

        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        cancel.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    private void showEditDialog(Model_todoList newItem, CardView card) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(card.getContext());
        LayoutInflater inflater = LayoutInflater.from(card.getContext());
        View dialogView = inflater.inflate(R.layout.create_todo_list_dialogue, null);

        EditText name, description, deadlineDate, deadlineTime, notifyBefore;
        CardView primaryColor, secondaryColor, notifyExpand, repeatExpand;
        CheckBox notification, repeat;
        RadioGroup notifyRG, repeatRG;
        Button save, discard, editSubtask, close;
        RadioButton minute, hour, daily, weekly, monthly, yearly;
        ConstraintLayout constraintLayout;
        ArrayList<Model_todoList.SubTask> subTasks = new ArrayList<>(newItem.getSubTaskList());

        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat dateFormat3 = new SimpleDateFormat("HH:mm");

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
        yearly = dialogView.findViewById(R.id.todoListCreateDialogue_repeatRG_yearly);
        constraintLayout = dialogView.findViewById(R.id.todoListCreateDialogue_editBox);
        editSubtask = dialogView.findViewById(R.id.todoListCreateDialogue_editSubtask);
        close = dialogView.findViewById(R.id.todoListCreateDialogue_close);

        constraintLayout.setVisibility(View.VISIBLE);
        close.setVisibility(View.VISIBLE);
        name.setText(newItem.getTitle());
        description.setText(newItem.getDescription());
        if (newItem.isHasDeadline()) {
            deadlineDate.setText(dateFormat2.format(newItem.getDeadLine()));
            deadlineTime.setText(dateFormat3.format(newItem.getDeadLine()));
        }

        if (newItem.isNotificationEnabled()) {
            notification.setChecked(true);
            notifyBefore.setVisibility(View.VISIBLE);
            notifyBefore.setText(newItem.getNotificationBefore() + "");
            if (newItem.getNotificationBeforeType() == 0) {
                minute.setChecked(true);
            } else {
                hour.setChecked(true);
                notifyBefore.setHint("__ hrs before");
            }
            notifyExpand.setVisibility(View.VISIBLE);
            notifyExpand.setRotation(-90);
        }

        if (newItem.isRepeatEnabled()) {
            repeat.setChecked(true);
            repeatExpand.setRotation(-90);
            repeatExpand.setVisibility(View.VISIBLE);
            switch (newItem.getRepeatType()) {
                case 0:
                    daily.setChecked(true); break;
                case 1:
                    weekly.setChecked(true); break;
                case 2:
                    monthly.setChecked(true); break;
                case 3:
                    yearly.setChecked(true); break;
            }
        }

        primaryColor.setCardBackgroundColor(newItem.getPrimaryColor());
        secondaryColor.setCardBackgroundColor(newItem.getSecondaryColor());

        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        discard.setOnClickListener(view -> {
            databaseReference.child(path).child(newItem.getKey()).setValue(null);
            alertDialog.dismiss();
        });

        close.setOnClickListener(view -> alertDialog.dismiss());

        save.setOnClickListener(view -> {
            if (name.getText().toString().equals("")) {
                Toast.makeText(card.getContext(), "Please enter task name", Toast.LENGTH_SHORT).show();
            } else if (notification.isChecked() && deadlineTime.getText().toString().equals("")) {
                Toast.makeText(card.getContext(), "Enter time to receive notification", Toast.LENGTH_SHORT).show();
            } else {
                String dateInput = deadlineDate.getText().toString();
                String timeInput = deadlineTime.getText().toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Date selectDate, selectTime, deadline;

                if (dateInput.equals("")) {
                    dateInput = dateFormat2.format(new Date());
                }
                if (timeInput.equals("")) {
                    timeInput = "23:59";
                }
                try {
                    selectDate = dateFormat2.parse(dateInput);
                } catch (Exception e) {
                    Toast.makeText(card.getContext(), "Enter date in proper dd/mm/yyyy format", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    selectTime = dateFormat3.parse(timeInput);
                } catch (Exception e) {
                    Toast.makeText(card.getContext(), "Enter time in proper HH:mm format", Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    deadline = dateFormat.parse(dateFormat2.format(selectDate) + " " + dateFormat3.format(selectTime));
                } catch (Exception e) {
                    Toast.makeText(card.getContext(), "exception occurred", Toast.LENGTH_SHORT).show();
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

                newItem.setSubTaskList(subTasks);
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
                databaseReference.child(path).child(newItem.getKey()).setValue(newItem);
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

        editSubtask.setOnClickListener(view -> {
            editSubtaskDialogue(card, subTasks,newItem);
        });

        primaryColor.setOnClickListener(view -> {
            ColorPickClass colorPickClass = new ColorPickClass(
                    card.getContext(), ContextCompat.getColor(card.getContext(), R.color.deepBlue3), newItem.getPrimaryColor(),
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
                    card.getContext(), ContextCompat.getColor(card.getContext(), R.color.ice), newItem.getSecondaryColor(),
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

    private void editSubtaskDialogue(CardView card, ArrayList<Model_todoList.SubTask> list, Model_todoList item) {
        if (list != null && list.size() > 0) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
            LayoutInflater inflater = LayoutInflater.from(card.getContext());
            View dialogView = inflater.inflate(R.layout.todo_list_subtask_edit_dialogue, null);

            RecyclerView recyclerView = dialogView.findViewById(R.id.todoListSubtaskEditDialog_recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            Adapter_SubTaskEdit adapterSubTaskEdit = new Adapter_SubTaskEdit(list);
            recyclerView.setAdapter(adapterSubTaskEdit);

            Button cancel = dialogView.findViewById(R.id.todoListSubtaskEditDialog_cancle);
            Button save = dialogView.findViewById(R.id.todoListSubtaskEditDialog_save);

            dialogBuilder.setView(dialogView);
            AlertDialog alertDialog = dialogBuilder.create();

            cancel.setOnClickListener(view -> {
                setValue(list, item.getSubTaskList());
                alertDialog.dismiss();
            });

            save.setOnClickListener(view -> {
                setValue(item.getSubTaskList(), list);
                alertDialog.dismiss();
            });

            alertDialog.show();
        } else {
            Toast.makeText(context, "No subtasks available", Toast.LENGTH_SHORT).show();
        }
    }

    private void setValue(ArrayList<Model_todoList.SubTask> l1, ArrayList<Model_todoList.SubTask> l2) {
        l1.clear();
        l1.addAll(l2);
    }
}
