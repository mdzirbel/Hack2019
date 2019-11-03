package com.example.pathy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity  {
    public SearchView userSearch;
    public SearchBar userSearchClass;
    public LinearLayout suggestion;
    public static LocationCoords location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Networking.startDownloadTask(getApplicationContext(), "union");
        userSearchClass = new SearchBar((SearchView) findViewById(R.id.search));
        location = new LocationCoords(getApplicationContext());
        userSearch = findViewById(R.id.search);
        suggestion = findViewById(R.id.linearLayout);
        userSearch.setSubmitButtonEnabled(false);
        userSearch.setQueryHint("Type a room...");
        userSearch.setIconifiedByDefault(false);
        userSearchClass.registerSearchListeners(userSearch, suggestion, getApplicationContext());
        MapPanning mp = findViewById(R.id.im_move_zoom_rotate);

        ImageButton settingsButton = findViewById(R.id.imageButtonSettings);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(startIntent);
            }
        });

        /*for(int x = 0; x < 125; x++)
        {
            for(int y = 0; y < 115; y++)
            {
                MapPanning.drawPoints.add(new Node(x,y));
            }
            MapPanning.drawPoints.add(new Node(x,0));
        }
        for(int y = 0; y < 115; y++)
        {
            for(int x = 0; x < 125; x++)
            {
                MapPanning.drawPoints.add(new Node(x,y));
            }
            MapPanning.drawPoints.add(new Node(0,y));
        }*/
    }
}