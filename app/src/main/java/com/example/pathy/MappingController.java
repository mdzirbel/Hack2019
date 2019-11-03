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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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
     * @param end node to end mapping at
     * @return lis of nodes to get from start to end including start and end
     */
    public static List<Node> getPathBetween(Node start, Node end){
        if(!hasInit) throw new RuntimeException("Attempted to close a non-initialized map");
        return map.getPathBetween(start, end, 0);
    }

    /**
     * function to get a node relative to the building latitude and longitude
     * @param lon
     * @param lat
     * @return
     */
    public Node coordToNode(double lon, double lat) {
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
        if(!hasInit) throw new RuntimeException("Attempted to close a non-initialized map");
        return getSafeMeta("name");
    }

    public static List<Node> getRoomNodes(String roomName){
        if(!roomToEntryNodes.containsKey(roomName)) throw new RuntimeException(roomName + " not found in metadata");
        return roomToEntryNodes.get(roomName);
    }

    public static List<String> getRoomNames(){
        return new LinkedList<>(roomToEntryNodes.keySet());
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
