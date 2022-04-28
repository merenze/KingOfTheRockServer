package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.Entities.IUser;

/**
 * Class for the logic of the dashboard screen of a non-admin type user
 *
 * @author Dan Rosenhamer
 */
public class UserDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        Button buttonToFindUser = findViewById(R.id.user_dashboard_find_user_button);

        buttonToFindUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), SearchForUserScreen.class));
            }
        });
    }
}
