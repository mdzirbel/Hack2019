package com.example.pathy;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;

import com.example.pathy.aStar.MapRegion;
import com.example.pathy.aStar.MapV2;
import com.example.pathy.aStar.Node;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MappingController{
    
    private static MapV2 map;
    private static Map<String, String> metadata;

    /**
     * using a given context load the current map into memory
     * @param context
     */
    public void allocateMap(Context context){
        try {
            InputStream mapStream = context.openFileInput("union.map");
            InputStream dataStream = context.openFileInput("union.met");
            if (mapStream != null && dataStream != null) {
                getMetadata(dataStream);
                loadmap(mapStream);
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

    }

    private void getMetadata(InputStream metaStream){
        metadata = new HashMap<>();
        JsonReader reader = new JsonReader(new InputStreamReader(metaStream));


    }

    private void loadmap(InputStream mapStream){
        //find if the size parameters exist in the metadata map
        if(! metadata.containsKey("size:x")) throw new RuntimeException("size:x not found in metadata");
        if(! metadata.containsKey("size:y")) throw new RuntimeException("size:y not found in metadata");

        //pick up the size paramters and load the map
        int size_x = Integer.parseInt(metadata.get("size:x"));
        int size_y = Integer.parseInt(metadata.get("size:y"));
        map = new MapV2(mapStream, size_x, size_y, new MapRegion(0, 0, size_x, size_y));
    }

    public List<Node> getPathBetween(Node start, Node end){
        return map.getPathBetween(start, end, 0);
    }


}
