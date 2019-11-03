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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MappingController{
    
    private static MapV2 map = null;
    private static Map<String, String> metadata;
    private static Map<String, List<Node>> roomToEntryNodes;
    private static boolean hasInit = false;
    private static final String BLDG = "union";

    /**
     * using a given context load the current map into memory
     * should be called after the files are re-downloaded
     * @param context
     */
    public static void allocateMap(Context context){
        try {
            InputStream mapStream = context.openFileInput(BLDG + ".map");
            InputStream dataStream = context.openFileInput(BLDG + ".met");
            if (mapStream != null && dataStream != null) {
                getMetadata(dataStream);
                loadmap(mapStream);
                hasInit = true;
                Log.d("Map loading", roomsAsStr());
                Log.d("Map loading", "Map loaded sucessfully");
            }
        }
        catch (FileNotFoundException e) {
            Log.e("Map loading", "File not found: " + e.toString());
        }

    }

    public static void close(){
        if(!hasInit) throw new RuntimeException("Attempted to close a non-initialized map");
        map.close();
        metadata.clear();
        hasInit = false;
    }

    /**
     * function to generate a list of nodes to go from the start node to the end node
     * @param start node to start at
     * @param ends nodes to end mapping at
     * @return shortest list of nodes to get from start to end including start and end node
     * if the path cannot be made the list will be empty
     */
    public static List<Node> getPathBetween(Node start, List<Node> ends){
        if(!hasInit) throw new RuntimeException("Attempted to close a non-initialized map");
        List<List<Node>> solutions = new LinkedList<>();
        for(Node end : ends) {
            List<Node> solution = map.getPathBetween(start, end, 0);
            if(solution.size() > 0) solutions.add(solution);
        }

        //sort collections based on size
        //largest first
        Collections.sort(solutions, new Comparator<List<Node>>() {
            @Override
            public int compare(List<Node> o1, List<Node> o2) {
                return o1.size() - o2.size();
            }
        });

        return solutions.get(0);
    }

    /**
     * function to generate a list of nodes to go from a start node to the nearest room end node
     * @param start node to start path from
     * @param roomName desired room name to path to
     * @returns a list of nodes that traverse from start to the nearest room end node
     * if the path cannot be made the list will be empty
     */
    public static List<Node> getPathBetween(Node start, String roomName){
        return getPathBetween(start, getRoomNodes(roomName));
    }

    /**
     * function to get a node relative to the building latitude and longitude
     * @param lon
     * @param lat
     * @return
     */
    public static Node coordToNode(double lon, double lat) {
        double y = lon;
        double x = lat;

        // Normalize to 0, 0
        y -= Double.parseDouble(getSafeMeta("gps_zero:y"));
        x -= Double.parseDouble(getSafeMeta("gps_zero:y"));

        // Rotate
        double sin = Double.parseDouble(getSafeMeta("rotation:sin"));
        double cos = Double.parseDouble(getSafeMeta("rotation:cos"));

        y = x * sin + y * cos;
        x = x * cos - y * sin;

        // Scale
        x = x * 0.7871 / 0.00001;
        y = y * 1.1132 / 0.00001;

        return new Node((int) Math.round(x), (int) Math.round(y));
    }

    /**function to get the name of the current map
     * @returns the name of the map from the metadata file
     */
    public static String getMapName(){
        if(!hasInit) throw new RuntimeException("Attempted to get map name before data was loaded");
        return getSafeMeta("name");
    }

    public static List<Node> getRoomNodes(String roomName){
        if(!hasInit) throw new RuntimeException("Attempted to get room nodes before data was loaded");
        if(!roomToEntryNodes.containsKey(roomName)) throw new RuntimeException(roomName + " not found in metadata");
        return roomToEntryNodes.get(roomName);
    }

    public static List<String> getRoomNames(){
        if(!hasInit) throw new RuntimeException("Attempted to get room names before data was loaded");
        if(!hasInit) throw new RuntimeException("Attempted to close a non-initialized map");
        return new LinkedList<>(roomToEntryNodes.keySet());
    }

    public static Node getNode(int x, int y){
        return map.getNode(x,y);
    }

    /**
     * internal function to set up the metadata hashmap
     * @param metaStream input stream of the metadata file
     */
    private static void getMetadata(InputStream metaStream){
        metadata = new HashMap<>();
        roomToEntryNodes = new HashMap<>();
        JsonReader reader = new JsonReader(new InputStreamReader(metaStream));
        try {
            reader.beginObject();
            String currname = "", currval = "";
            while(reader.hasNext()){
                currname = reader.nextName();
                if(!currname.equalsIgnoreCase("rooms")) {
                    currval = reader.nextString();
                    metadata.put(currname, currval);
                }
                else{
                    reader.beginObject();
                    String roomName;
                    while(reader.hasNext()){
                        roomName = reader.nextName();
                        reader.beginArray();
                        List<Node> roomNodes = new LinkedList<>();
                        while (reader.hasNext()){
                            roomNodes.add(new Node(reader.nextInt(), reader.nextInt()));
                        }
                        reader.endArray();
                        roomToEntryNodes.put(roomName, roomNodes);

                    }
                    reader.endObject();
                }
            }
            reader.endObject();

        } catch (IOException e) {
            Log.e("Map loading", "Can not read file: " + e.toString());
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
    private static void loadmap(InputStream mapStream){
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
    private static String getSafeMeta(String key){
        if(!metadata.containsKey(key)) throw new RuntimeException(key + " not found in metadata");
        return metadata.get(key);
    }

    private static String roomsAsStr(){
        String out = "";
        for(String roomName : getRoomNames()){
            out += roomName + " " + nodesAsStr(roomName) + ", ";
        }
        return out;
    }

    private static String nodesAsStr(String roomName){
        String out = "[";
        for(Node entry : getRoomNodes(roomName)){
            out += entry.toString() + ", ";
        }
        return out + "]";
    }


}
