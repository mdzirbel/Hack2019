package com.example.pathy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public SearchView userSearch;
    public SearchBar userSearchClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userSearch = findViewById(R.id.search);
        userSearch.setSubmitButtonEnabled(true);
        userSearchClass.registerSearchListeners(userSearch, getApplicationContext());


    }
}
