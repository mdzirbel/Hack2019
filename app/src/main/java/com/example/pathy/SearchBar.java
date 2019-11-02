package com.example.pathy;


import android.app.ListActivity;
import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SearchRecentSuggestionsProvider;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SearchBar {

    public SearchBar(Context context) {

    }


    public static String userInput;
    public static final String[] SUGGESTLIST = {"Archie M. Griffin Grand Ballroom",
            "Senate Chambers", "Student-Alumni Council Room", "Sphinx Centennial Leadership Suite",
            "Ohio Staters, Inc. Founders Room", "Ohio Staters, Inc. Traditions Room",
            "Glass Art Lounge", "Keith B. Key Center for Student Leadership and Service",
            "Administrative Office Suite", "Danny Price Student Lounge"};
    private SimpleCursorAdapter adapter;

    public static void registerSearchListeners(SearchView userSearch, final LinearLayout suggestion, final Context con) {
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
                dropDownList(newText, suggestion, con);
                return false;
            }
        });


    }

    public static void dropDownList(String text, LinearLayout suggestion, Context context) {
        suggestion.removeAllViews();
        int length = text.length();
        List<String> suggested = new ArrayList<String>();
        for (int i = 0; i < SUGGESTLIST.length; i++) {
            if (text.compareToIgnoreCase(SUGGESTLIST[i].substring(0, length)) == 0) {
                TextView textView = new TextView(context);
                textView.setText(SUGGESTLIST[i]);
                suggestion.addView(textView);
            }
        }


    }

}
