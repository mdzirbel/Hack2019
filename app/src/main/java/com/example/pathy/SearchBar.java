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

    public SearchBar(Context context) {

    }


    public static String userInput;
//    public static final String[] SUGGESTLIST = {"Archie M. Griffin Grand Ballroom",
//            "Senate Chambers", "Student-Alumni Council Room", "Sphinx Centennial Leadership Suite",
//            "Ohio Staters, Inc. Founders Room", "Ohio Staters, Inc. Traditions Room",
//            "Glass Art Lounge", "Keith B. Key Center for Student Leadership and Service",
//            "Administrative Office Suite", "Danny Price Student Lounge"};


    public static void registerSearchListeners(final SearchView userSearch, final LinearLayout suggestion, final Context con) {
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

                dropDownList(newText, suggestion, con, userSearch);

                return false;
            }
        });


    }


    public static void dropDownList(String text, final LinearLayout suggestion, Context context, final SearchView userSearch) {
        List<String> rooms = new ArrayList<String>();
        rooms = MappingController.getRoomNames();
        Collections.sort(rooms);
        suggestion.removeAllViews();
        int length = text.length();
        suggestion.getLayoutParams().height = 0;

        if (!userSearch.isIconified()) {
            userSearch.setIconified(true);
        } else {
            userSearch.setIconified(false);
        }


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
                    suggestion.addView(button);
                }
            }
        }


    }

}
