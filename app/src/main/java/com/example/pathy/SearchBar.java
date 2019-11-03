package com.example.pathy;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.pathy.aStar.Node;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SearchBar {

    private static SearchView search;

    public SearchBar(SearchView searchView) {
        search = searchView;
    }

//    private static final String[] SUGGESTLIST = {"Archie M. Griffin Grand Ballroom",
//            "Senate Chambers", "Student-Alumni Council Room", "Sphinx Centennial Leadership Suite",
//            "Ohio Staters, Inc. Founders Room", "Ohio Staters, Inc. Traditions Room",
//            "Glass Art Lounge", "Keith B. Key Center for Student Leadership and Service",
//            "Administrative Office Suite", "Danny Price Student Lounge"};


    static void registerSearchListeners(final SearchView userSearch, final LinearLayout suggestion, final Context con) {
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

                dropDownList(newText, suggestion, con);

                return false;
            }
        });
    }


    private static void dropDownList(String text, final LinearLayout suggestion, Context context) {
        List<String> rooms = MappingController.getRoomNames();
        Collections.sort(rooms);
        suggestion.removeAllViews();
        int length = text.length();
        suggestion.getLayoutParams().height = 0;


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
                    button.setOnClickListener(onSuggestionClickListener);

                }
            }
        }


    }

//    // For clicking the suggestions
    static private View.OnClickListener onSuggestionClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            // Get the text of the button you selected
            String buttonText = (String) ((Button) v).getText();

            // Fill in the whole text in the search box and send the query
            search.setQuery(buttonText, false);
            submitQuery(buttonText);
        }
    };

    private static void submitQuery(String query) {

        Log.d("SUBMIT QUERY", query);
        Log.d("SUBMIT QUERY", ""+MainActivity.location.currentNode);

        if (MainActivity.location.currentNode == null) {
            Log.e("SUBMIT QUERY", "Passed null pointer");
        }
        else {
            Log.d("SUBMIT QUERY", "Current node " + MainActivity.location.currentNode);
            Log.d("SUBMIT QUERY", "nearest valid " + MappingController.snapNearestNode(MainActivity.location.currentNode));
            MapPanning.drawPoints = MappingController.getPathBetween(MappingController.snapNearestNode(MainActivity.location.currentNode), query);
        }
    }

//getRoomNodes(name)
}
