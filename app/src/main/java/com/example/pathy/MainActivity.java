package com.example.pathy;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.SearchView;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;

import com.example.pathy.aStar.Node;

public class MainActivity extends AppCompatActivity  {
    public SearchView userSearch;
    public SearchBar userSearchClass;
    public LinearLayout suggestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Networking.startDownloadTask(getApplicationContext(), "union");
        userSearch = findViewById(R.id.search);
        suggestion = findViewById(R.id.linearLayout);
        userSearch.setSubmitButtonEnabled(false);
        userSearch.setQueryHint("Type a room...");
        userSearch.setIconifiedByDefault(false);
        userSearchClass.registerSearchListeners(userSearch, suggestion, getApplicationContext());
        MapPanning mp = findViewById(R.id.im_move_zoom_rotate);
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