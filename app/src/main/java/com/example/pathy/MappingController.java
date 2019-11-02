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

    public void close(){
        map.close();
        metadata.clear();
    }

    /**
     * function to generate a list of nodes to go from the start node to the end node
     * @param start node to start at
     * @param end node to end mapping at
     * @return lis of nodes to get from start to end including start and end
     */
    public List<Node> getPathBetween(Node start, Node end){
        return map.getPathBetween(start, end, 0);
    }

    /**function to get the name of the current map
     * @returns the name of the map from the metadata file
     */
    public String getMapName(){
        return getSafeMeta("name");
    }

    /**
     * internal function to set up the metadata hashmap
     * @param metaStream input stream of the metadata file
     */
    private void getMetadata(InputStream metaStream){
        metadata = new HashMap<>();
        JsonReader reader = new JsonReader(new InputStreamReader(metaStream));
        try {
            reader.beginObject();
            String currname = "", currval = "";
            while(reader.hasNext()){
                currname = reader.nextName();
                currval = reader.nextString();
                metadata.put(currname, currval);
            }

        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
        finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * internal function to load the graph map into memory for use
     * @param mapStream input stream of the map file
     */
    private void loadmap(InputStream mapStream){
        //pick up the size paramters and load the map
        int size_x = Integer.parseInt(getSafeMeta("size:x"));
        int size_y = Integer.parseInt(getSafeMeta("size:y"));
        map = new MapV2(mapStream, size_x, size_y, new MapRegion(0, 0, size_x, size_y));
    }

    /**
     * function to safely extract a map value from a given key
     * @param key the given key
     * @returns the requested value
     * @throws RuntimeException if the specified key was not found in the metadata file
     */
    private String getSafeMeta(String key){
        if(!metadata.containsKey(key)) throw new RuntimeException(key + " not found in metadata");
        return metadata.get(key);
    }


}
