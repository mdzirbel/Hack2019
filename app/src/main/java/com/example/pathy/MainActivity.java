package com.example.pathy;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.SearchView;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;

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
        userSearch.setSubmitButtonEnabled(true);
        userSearch.setQueryHint("Type a room...");
        userSearch.setIconifiedByDefault(false);
        userSearchClass.registerSearchListeners(userSearch, suggestion, getApplicationContext());
        MapPanning mp = findViewById(R.id.im_move_zoom_rotate);
        mp.setImageDrawable(DrawOnImage.drawOnImage(getApplicationContext()));
    }

}