package com.example.pathy;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SearchBar {

    SearchView search;

    public SearchBar(SearchView searchView) {
        search = searchView;
    }

    private static final String[] SUGGESTLIST = {"Archie M. Griffin Grand Ballroom",
            "Senate Chambers", "Student-Alumni Council Room", "Sphinx Centennial Leadership Suite",
            "Ohio Staters, Inc. Founders Room", "Ohio Staters, Inc. Traditions Room",
            "Glass Art Lounge", "Keith B. Key Center for Student Leadership and Service",
            "Administrative Office Suite", "Danny Price Student Lounge"};

    private SimpleCursorAdapter adapter;

    void registerSearchListeners(final SearchView userSearch, final LinearLayout suggestion, final Context con) {
        userSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("QUERY SUBMIT", "Input: " + query);
                submitQuery(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                Toast.makeText(con, "Input: " + newText, Toast.LENGTH_LONG).show();
                Log.e("Text change", "Input: " + newText);

                dropDownList(newText, suggestion, con, userSearch);

                return false;
            }
        });
    }


    public void dropDownList(String text, final LinearLayout suggestion, Context context, final SearchView userSearch) {
        List<String> rooms = new ArrayList<String>();
        rooms = MappingController.getRoomNames();
        Collections.sort(rooms);
        suggestion.removeAllViews();
        int length = text.length();
        suggestion.getLayoutParams().height = 0;

        userSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                suggestion.removeAllViews();
                userSearch.clearFocus();
                return false;
            }
        });


        if (text.length() == 0) {
            suggestion.getLayoutParams().height = 0;
            suggestion.removeAllViews();
        } else {
            suggestion.getLayoutParams().height = 600;

            for (int i = 0; i < rooms.size(); i++) {
                if (text.compareToIgnoreCase(rooms.get(i).substring(0, length)) == 0) {
                    Button button = new Button(context);
                    button.setText(rooms.get(i));
                    button.setBackgroundResource(R.drawable.border);
                    button.setBackgroundColor(Color.TRANSPARENT);
                    button.setOnClickListener(onSuggestionClickListener);
                    suggestion.addView(button);
                }
            }
        }


    }

    // For clicking the suggestions
    private View.OnClickListener onSuggestionClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // Get the text of the button you selected
            String buttonText = (String) ((Button) v).getText();

            // Fill in the whole text in the search box and send the query
            search.setQuery(buttonText, true);
        }
    };

    private static void submitQuery(String query) {



        Log.d("SUMBIT QUERY", query);
    }

//getRoomNodes(name)
}
