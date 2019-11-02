package com.example.pathy;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity  {
    public SearchView userSearch;
    public SearchBar userSearchClass;
    public LinearLayout suggestion;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userSearch = findViewById(R.id.search);
        suggestion = findViewById(R.id.linearLayout);
        userSearch.setSubmitButtonEnabled(true);
        userSearch.setQueryHint("Type a room...");
        userSearch.setIconifiedByDefault(false);
        userSearchClass.registerSearchListeners(userSearch, suggestion, getApplicationContext());
    }
}
