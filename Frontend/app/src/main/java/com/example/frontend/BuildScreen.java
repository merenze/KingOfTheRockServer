package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.frontend.Entities.IUser;
import com.example.frontend.Logic.BuildLogic;
import com.example.frontend.Network.ServerRequest;
import com.example.frontend.SupportingClasses.AppController;
import com.example.frontend.SupportingClasses.IView;

import org.json.JSONException;

/**
 * Class for the logic of the screen to build a structure
 *
 * @author Noah Cordova
 */
public class BuildScreen extends AppCompatActivity implements IView {

    private String TAG = BuildScreen.class.getSimpleName();
    private static IUser currentUser;
    private BuildLogic logic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AppController();
        setContentView(R.layout.activity_build_screen);

        currentUser = LoginScreen.getCurrentUser();
        ServerRequest serverRequest = new ServerRequest();
        logic = new BuildLogic(this, serverRequest, getCurrentUser());

        Bundle bundle = getIntent().getExtras();
        String gameObjectString = bundle.getString("game-object-string");

        Button townButton = (Button) findViewById(R.id.activity_build_screen_button_structure_town);
        townButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    logic.buildStructure("town", gameObjectString);
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            }
        });

        Button houseButton = (Button) findViewById(R.id.activity_build_screen_button_structure_house);
        houseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    logic.buildStructure("house", gameObjectString);
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            }
        });

        Button mineButton = (Button) findViewById(R.id.activity_build_screen_button_structure_mine);
        mineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    logic.buildStructure("mine", gameObjectString);
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            }
        });

        Button lumberyardButton = (Button) findViewById(R.id.activity_build_screen_button_structure_lumberyard);
        lumberyardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    logic.buildStructure("lumberyard", gameObjectString);
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            }
        });

        Button gardenButton = (Button) findViewById(R.id.activity_build_screen_button_structure_garden);
        gardenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    logic.buildStructure("garden", gameObjectString);
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            }
        });

        Button wellButton = (Button) findViewById(R.id.activity_build_screen_button_structure_well);
        wellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    logic.buildStructure("well", gameObjectString);
                } catch (JSONException exception) {
                    exception.printStackTrace();
                }
            }
        });

        Button cancelButton = (Button) findViewById(R.id.activity_build_screen_button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(view.getContext(), GameViewScreen.class));
            }
        });

        Button chatButton = (Button) findViewById(R.id.activity_build_screen_button_chat);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logic to open chat pop-up
            }
        });

        Button menuButton = (Button) findViewById(R.id.activity_build_screen_button_menu);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //logic to open menu pop-up
            }
        });
    }

    public static IUser getCurrentUser() {
        return currentUser;
    }

    @Override
    public void logText(String s) {
        Log.d("LoginScreen", s);
    }

    @Override
    public void makeToast(String message) {
        Log.d(TAG, "making Toast...");
        Toast.makeText(BuildScreen.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void switchActivity() {
    }

}