package com.example.mypages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    CardView main_createButton, main_folderButton;
    Button main_signOutButton, main_settingsButton, main_helpButton;
    LinearLayout layout1, layout2, layout3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        Configuration configuration = getResources().getConfiguration();
        if ((configuration.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE
                || (configuration.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            // It's a tablet TODO
        } else {
            // It's a mobile TODO
        }*/

        main_createButton = findViewById(R.id.main_createButton);
        main_folderButton = findViewById(R.id.main_folderButton);

        main_helpButton = findViewById(R.id.main_helpButton);
        main_signOutButton = findViewById(R.id.main_signOutButton);
        main_settingsButton = findViewById(R.id.main_settingsButton);

        layout1 = findViewById(R.id.main_linearLayout1);
        layout2 = findViewById(R.id.main_linearLayout2);
        layout3 = findViewById(R.id.main_linearLayout3);

        // changing view depending on orientation
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            layout1.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            );

            layout2.setOrientation(LinearLayout.VERTICAL);
            layout2.setLayoutParams(layoutParams);
            layout3.setLayoutParams(layoutParams);
        }

        main_createButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, selectCreateActivity.class);
            startActivity(intent);
        });

        main_signOutButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

    }

}