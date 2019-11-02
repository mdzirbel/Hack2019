package com.example.pathy;

import android.content.Context;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

public class SearchBar {


    public static String userInput;


    public static void registerSearchListeners(SearchView userSearch, final Context con) {
        userSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                userInput = query;
                Log.e("Submit!", "Input: " + query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(con, "Input: " + newText, Toast.LENGTH_LONG).show();
                Log.e("Text change", "Input: " + newText);
                return false;
            }
        });
    }

    public static void dropDownList() {

    }

}
