package com.example.pathy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.SearchView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public SearchView userSearch;
    public SearchBar userSearchClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Networking.startDownloadTask(getApplicationContext(), "union");
        userSearch = findViewById(R.id.search);
        userSearch.setSubmitButtonEnabled(true);
        userSearchClass.registerSearchListeners(userSearch, getApplicationContext());
        MapPanning mp = findViewById(R.id.im_move_zoom_rotate);
        mp.setImageDrawable(DrawOnImage.drawOnImage(getApplicationContext()));
    }

}