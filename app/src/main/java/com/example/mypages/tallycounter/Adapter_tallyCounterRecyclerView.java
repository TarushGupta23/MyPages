package com.example.mypages.tallycounter;


import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypages.ColorPickClass;
import com.example.mypages.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class Adapter_tallyCounterRecyclerView extends RecyclerView.Adapter<Adapter_tallyCounterRecyclerView.ViewHolder> {
    private List<Model_tallyCounter> itemList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();;
    DatabaseReference databaseReference = database.getReference("Users").child(auth.getUid()).child("tally_counter_folder");

    public Adapter_tallyCounterRecyclerView(List<Model_tallyCounter> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tally_counter_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Model_tallyCounter item = itemList.get(position);
        holder.counterNameTextView.setText(item.getCounterName());
        holder.countTextView.setText(String.valueOf(item.getCount()));

        holder.plusOneButton.setOnClickListener(v -> {
            if (!item.isLocked()) {
                item.setCount(item.getCount() + 1);
                databaseReference.child(item.getKey()).setValue(item);
            } else {
                Toast.makeText(v.getContext(), "Counter is locked", Toast.LENGTH_SHORT).show();
            }
        });

        holder.plusTenButton.setOnClickListener(v -> {
            if (!item.isLocked()) {
                item.setCount(item.getCount() + 10);
                databaseReference.child(item.getKey()).setValue(item);
            } else {
                Toast.makeText(v.getContext(), "Counter is locked", Toast.LENGTH_SHORT).show();
            }
        });

        holder.minusOneButton.setOnClickListener(v -> {
            if (!item.isLocked()) {
                item.setCount(item.getCount() - 1);
                databaseReference.child(item.getKey()).setValue(item);
            } else {
                Toast.makeText(v.getContext(), "Counter is locked", Toast.LENGTH_SHORT).show();
            }
        });

        holder.minusTenButton.setOnClickListener(v -> {
            if (!item.isLocked()) {
                item.setCount(item.getCount() - 10);
                databaseReference.child(item.getKey()).setValue(item);
            } else {
                Toast.makeText(v.getContext(), "Counter is locked", Toast.LENGTH_SHORT).show();
            }
        });

        holder.counterNameTextView.setOnClickListener(v -> {
            showEditDialog(holder.counterNameTextView, item, InputType.TYPE_CLASS_TEXT, "counterName");
        });

        holder.countTextView.setOnClickListener(v -> {
            if (item.isLocked()) {
                Toast.makeText(v.getContext(), "Counter is locked", Toast.LENGTH_SHORT).show();
            } else {
                showEditDialog(holder.countTextView, item, InputType.TYPE_CLASS_NUMBER, "count");
            }
        });

        holder.menuButton.setOnClickListener(v->{
            showEditMenuDialog(item, v.getContext());
        });

        if (item.getPrimaryColor() != 0) {
            holder.cardBody.setCardBackgroundColor(item.getPrimaryColor());
            holder.minusTenText.setTextColor(item.getPrimaryColor());
            holder.plusTenText.setTextColor(item.getPrimaryColor());
            holder.minusOneText.setTextColor(item.getPrimaryColor());
            holder.plusOneText.setTextColor(item.getPrimaryColor());
            holder.menuText.setTextColor(item.getPrimaryColor());
        }
        if (item.getSecondaryColor() != 0) {
            holder.counterNameTextView.setTextColor(item.getSecondaryColor());
            holder.countTextView.setTextColor(item.getSecondaryColor());
            holder.plusTenButton.setCardBackgroundColor(item.getSecondaryColor());
            holder.minusTenButton.setCardBackgroundColor(item.getSecondaryColor());
            holder.plusOneButton.setCardBackgroundColor(item.getSecondaryColor());
            holder.minusOneButton.setCardBackgroundColor(item.getSecondaryColor());
            holder.menuButton.setCardBackgroundColor(item.getSecondaryColor());
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView counterNameTextView, countTextView,
                plusOneText, plusTenText, minusOneText, minusTenText, menuText;
        CardView plusOneButton, plusTenButton, minusOneButton, minusTenButton, menuButton,cardBody;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            counterNameTextView = itemView.findViewById(R.id.tallyCounterCard_itemName);
            countTextView = itemView.findViewById(R.id.tallyCounterCard_itemCount);
            plusOneButton = itemView.findViewById(R.id.tallyCounterCard_increase);
            plusTenButton = itemView.findViewById(R.id.tallyCounterCard_increase10);
            minusOneButton = itemView.findViewById(R.id.tallyCounterCard_decrease);
            minusTenButton = itemView.findViewById(R.id.tallyCounterCard_decrease10);
            cardBody = itemView.findViewById(R.id.tallyCounterCard_body);
            menuButton = itemView.findViewById(R.id.tallyCounterCard_more);
            plusTenText = itemView.findViewById(R.id.textView8);
            plusOneText = itemView.findViewById(R.id.textView9);
            minusOneText = itemView.findViewById(R.id.textView10);
            minusTenText = itemView.findViewById(R.id.textView13);
            menuText = itemView.findViewById(R.id.textView12);
        }
    }

    private void showEditDialog(TextView textView, Model_tallyCounter item, int inputType, String editField) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(textView.getContext());
        LayoutInflater inflater = LayoutInflater.from(textView.getContext());
        View dialogView = inflater.inflate(R.layout.text_input, null);

        EditText editText = dialogView.findViewById(R.id.dialogueTextInput_editText);
        Button save = dialogView.findViewById(R.id.dialogueTextInput_buttonOk);
        Button cancel = dialogView.findViewById(R.id.dialogueTextInput_buttonCancel);

        editText.setInputType(inputType);
        editText.setText(textView.getText());
        dialogBuilder.setView(dialogView);
        AlertDialog alertDialog = dialogBuilder.create();

        save.setOnClickListener(v -> {
            String editedText = editText.getText().toString();
            textView.setText(editedText);
            if (editField.equals("count")) {
                databaseReference.child(item.getKey()).child(editField).setValue(Integer.parseInt(editedText));
            } else {
                databaseReference.child(item.getKey()).child(editField).setValue(editedText);
            }
            alertDialog.dismiss();
        });

        cancel.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        alertDialog.show();
    }

    void showEditMenuDialog(Model_tallyCounter counter, Context context) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.edit_tally_counter_dialogue, null);
        EditText name, value;
        Button lock, save, discard;
        CardView colorPrim, colorSecond;
        DatabaseReference currRef = databaseReference.child(counter.getKey());
        Button close = dialogView.findViewById(R.id.tallyCounterDialogue_closeButton);
        close.setVisibility(View.VISIBLE);

        name = dialogView.findViewById(R.id.tallyCounterDialogue_name);
        value = dialogView.findViewById(R.id.tallyCounterDialogue_count);
        lock = dialogView.findViewById(R.id.tallyCounterDialogue_lockButton);
        save = dialogView.findViewById(R.id.tallyCounterDialogue_saveButton);
        discard = dialogView.findViewById(R.id.tallyCounterDialogue_discardButton);
        colorPrim = dialogView.findViewById(R.id.todoListCreateDialogue_colorPrimary);
        colorSecond = dialogView.findViewById(R.id.todoListCreateDialogue_colorSecondary);

        dialogBuilder.setView(dialogView);
        name.setText(counter.getCounterName());
        colorPrim.setCardBackgroundColor(counter.getPrimaryColor());
        colorSecond.setCardBackgroundColor(counter.getSecondaryColor());
        value.setText(counter.getCount() + "");
        AlertDialog alertDialog = dialogBuilder.create();

        close.setOnClickListener(view -> alertDialog.dismiss());

        save.setOnClickListener(v -> {
            if (name.getText().toString().equals("")) {
                Toast.makeText(context, "Please Enter Name", Toast.LENGTH_SHORT).show();
            } else if (colorPrim.getCardBackgroundColor().equals(colorSecond.getCardBackgroundColor())) {
                Toast.makeText(context, "Primary and Secondary colors should not be same", Toast.LENGTH_SHORT).show();
            } else {
                counter.setCounterName(name.getText().toString());
                try {
                    counter.setCount(Integer.parseInt(value.getText().toString()));
                } catch (Exception e) {
                    counter.setCount(0);
                }
                counter.setPrimaryColor(colorPrim.getCardBackgroundColor().getDefaultColor());
                counter.setSecondaryColor(colorSecond.getCardBackgroundColor().getDefaultColor());
                alertDialog.dismiss();
                currRef.setValue(counter);
            }
        });

        discard.setOnClickListener(view -> {
            currRef.setValue(null);
            alertDialog.dismiss();
        });

        lock.setOnClickListener(view -> {
            if (counter.isLocked()) {
                Toast.makeText(context, "Counter unlocked", Toast.LENGTH_SHORT).show();
                counter.setLocked(false);
            } else {
                Toast.makeText(context, "Counter locked", Toast.LENGTH_SHORT).show();
                counter.setLocked(true);
            }
        });

        colorPrim.setOnClickListener(view -> {
            ColorPickClass colorPickClass = new ColorPickClass(
                    context, ContextCompat.getColor(context, R.color.deepBlue3), counter.getPrimaryColor(),
                    new ColorPickClass.OnSaveClickListener() {
                        @Override
                        public void onSaveClicked(int newColor) {
                            counter.setPrimaryColor(newColor);
                            colorPrim.setCardBackgroundColor(newColor);
                        }
                    },
                    new ColorPickClass.OnDefaultClickListener() {
                        @Override
                        public void onDefaultClicked(int color) {
                            counter.setPrimaryColor(color);
                            colorPrim.setCardBackgroundColor(color);
                        }
                    },
                    new ColorPickClass.OnCancelClickListener() {
                        @Override
                        public void onCancelClicked(int color) {
                            counter.setPrimaryColor(color);
                            colorPrim.setCardBackgroundColor(color);
                        }
                    }
            );
            colorPickClass.show();
        });

        colorSecond.setOnClickListener(view -> {
            ColorPickClass colorPickClass = new ColorPickClass(
                    context, ContextCompat.getColor(context, R.color.ice), counter.getSecondaryColor(),
                    new ColorPickClass.OnSaveClickListener() {
                        @Override
                        public void onSaveClicked(int newColor) {
                            counter.setSecondaryColor(newColor);
                            colorSecond.setCardBackgroundColor(newColor);
                        }
                    },
                    new ColorPickClass.OnDefaultClickListener() {
                        @Override
                        public void onDefaultClicked(int color) {
                            counter.setSecondaryColor(color);
                            colorSecond.setCardBackgroundColor(color);
                        }
                    },
                    new ColorPickClass.OnCancelClickListener() {
                        @Override
                        public void onCancelClicked(int color) {
                            counter.setSecondaryColor(color);
                            colorSecond.setCardBackgroundColor(color);
                        }
                    }
            );
            colorPickClass.show();
        });

        alertDialog.show();
    }
}
