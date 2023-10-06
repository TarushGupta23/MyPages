package com.example.mypages;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.skydoves.colorpickerview.ColorEnvelope;
import com.skydoves.colorpickerview.ColorPickerView;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;
import com.skydoves.colorpickerview.sliders.BrightnessSlideBar;

public class ColorPickClass extends Dialog{
    Button cancel, save, defaultBtn;
    ColorPickerView colorPickerView;
    BrightnessSlideBar brightnessSlideBar;

    CardView pallet;
    OnSaveClickListener onSaveClickListener;
    OnDefaultClickListener onDefaultClickListener;
    OnCancelClickListener onCancelClickListener;
    int defaultColor, initialColor;

    public ColorPickClass(@NonNull Context context, int defaultColor, int initialColor, OnSaveClickListener onSaveClickListener, OnDefaultClickListener onDefaultClickListener, OnCancelClickListener onCancelClickListener)
    {
        super(context);
        this.onSaveClickListener = onSaveClickListener;
        this.onDefaultClickListener = onDefaultClickListener;
        this.onCancelClickListener = onCancelClickListener;
        this.defaultColor = defaultColor;
        this.initialColor = initialColor;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_pick_dialogue);

        colorPickerView = findViewById(R.id.colorPickerView);
        brightnessSlideBar = findViewById(R.id.brightnessSlideBar);
        pallet = findViewById(R.id.selectedColorCard);

        cancel = findViewById(R.id.colorPickDialogue_buttonCancel);
        save = findViewById(R.id.colorPickDialogue_buttonSave);
        defaultBtn = findViewById(R.id.colorPickerDialogue_resetButton);

        colorPickerView.setColorListener(new ColorEnvelopeListener() {
            @Override
            public void onColorSelected(ColorEnvelope envelope, boolean fromUser) {
                pallet.setCardBackgroundColor(envelope.getColor());
            }
        });
        colorPickerView.attachBrightnessSlider(brightnessSlideBar);

        save.setOnClickListener(view -> {
            if (onSaveClickListener != null) {
                onSaveClickListener.onSaveClicked(pallet.getCardBackgroundColor().getDefaultColor());
            }
            dismiss();
        });
        cancel.setOnClickListener(view -> {
            if (onCancelClickListener != null) {
                onCancelClickListener.onCancelClicked(initialColor);
            }
            dismiss();
        });
        defaultBtn.setOnClickListener(view -> {
            if (onDefaultClickListener != null) {
                onDefaultClickListener.onDefaultClicked(defaultColor);
            }
            dismiss();
        });
    }

    public interface OnSaveClickListener {
        void onSaveClicked(int color);
    }
    public interface OnCancelClickListener {
        void onCancelClicked(int color);
    }
    public interface OnDefaultClickListener {
        void onDefaultClicked(int color);
    }
}
