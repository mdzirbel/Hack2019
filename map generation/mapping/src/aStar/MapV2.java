package aStar;

import java.io.*;
import java.nio.file.Files;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class MapV2 {

    private int size_x, size_y;
    private Node[][] loadedNodes;
    private MapRegion loadedRegion = null;
    private InputStream reader;
    private OutputStream writer;

    /**
     * creates a map of size_x by size_y and writes it to file. a sub-region is then loaded into memory designated by region.
     * @param file file reference to write to
     * @param size_x of map to create
     * @param size_y of map to create
     * @param region map region to load into memory
     */
    public MapV2(InputStream fileReader, OutputStream fileWriter, int size_x, int size_y, MapRegion region){
        this.size_x = size_x; this.size_y = size_y;
        //System.out.println("set class vars");
        //System.out.println(map.isFile() + "");
        if(map.isFile()) {
            try {
                //System.out.println("writing blank map");
                PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
                for (int r = 0; r < size_y; r++) {
                    String toWrite = "";
                    for (int c = 0; c < size_x; c++) {
                        toWrite += Node.TraversalState.DEFAULT.serialize() + "\t"; //TSV
                    }
                    writer.println(toWrite);
                    writer.flush();
                }


            } catch (IOException e) {
                System.out.println("failed to serialize map to file");
            }
            //System.out.println("attempting to load " + region);
            loadRegion(region);
        }
    }

    /**
     * loads a 10 by 10 array starting at 0,0
     * @param file mapping file
     * @param size_x of map to create
     * @param size_y of map to create
     */
    public MapV2(File file, int size_x, int size_y){
        this(file, size_x, size_y, new MapRegion(0,0,10,10));
    }

    public void loadRegion (MapRegion region){
        if(region.x_off + region.x_size > size_x || region.y_off + region.y_size > size_y)
            throw new IndexOutOfBoundsException("Region exceeded map boundaries");
        writeRegion(loadedRegion);
        //System.out.println("called to load " + region);
        try {
            Scanner scanner = new Scanner(map);
            loadedNodes = new Node[region.y_size][region.x_size];
            //System.out.println("created array");
            for(int r = 0; r < region.y_off + region.y_size; r++){
                //System.out.println("row " + r);
                if(r <  region.y_off){
                    scanner.nextLine();
                    continue;
                }
                for(int c = 0; c < region.x_off + region.x_size; c++){
                    if(c < region.x_off){
                        scanner.nextInt();
                        continue;
                    }
                    loadedNodes[r - region.y_off][c - region.x_off] = new Node(c,r,Node.getStateFromInt(scanner.nextInt()));
                }
            }
            loadedRegion = region;
            //System.out.println("loaded " + region);
        } catch (FileNotFoundException e) {
            System.out.println("failed to de-serialize region");
        }

    }

    private void writeRegion(MapRegion region){
        //System.out.println("writing " + loadedRegion);
        if(region == null) return;
        try {
            List<String> lines = Files.readAllLines(map.toPath());
            //System.out.println("Read file contents");
            for(int r = region.y_off; r < region.y_off + region.y_size; r++){
                String out = "";
                for(int c = region.x_off; c < region.x_off + region.x_size; c++){
                    out += loadedNodes[r - region.y_off][c - region.x_off].serialize() + "\t";
                }
                //System.out.println("re-writing line " + r);
                out = ((region.x_off > 0) ? lines.get(r).substring(0, region.x_off * 2) : "") + out +
                        ((lines.get(r).length() > (region.x_off + region.x_size) * 2)? lines.get(r).substring((region.x_off + region.x_size + 1) * 2) : "");
                lines.set(r, out);
            }
        } catch (IOException e) {
            System.out.println("failed to serialize region");
        }
        //System.out.println(region + " written");
    }

    /**
     * method used to retrieve a specific node on a given map
     * <p>can be used to set node properties
     * @param y coordinate
     * @param x coordinate
     * @return Node object at the coordinate pair specified
     */
    public Node getNode(int x, int y){
        if(x > loadedRegion.x_off && x < loadedRegion.x_off + loadedRegion.x_size &&
                y > loadedRegion.y_off && y < loadedRegion.y_off + loadedRegion.y_size)
                    return loadedNodes[y - loadedRegion.y_off][x - loadedRegion.x_off];

        throw new ArrayIndexOutOfBoundsException("outside current loaded region");
    }

    public void close(){
        loadedNodes = null;
        System.gc();
    }

    public LinkedList<Node> adjacentTo(Node node){

        LinkedList<Node> toReturn = new LinkedList<>();

        //left
        if(node.getPos_x() - 1 > loadedRegion.x_off &&
                loadedNodes[node.getPos_y() - loadedRegion.y_off][node.getPos_x() - 1 - loadedRegion.x_off].canTraverse() &&
                !loadedNodes[node.getPos_y() - loadedRegion.y_off][node.getPos_x() - 1 - loadedRegion.x_off].getSearched()){
            loadedNodes[node.getPos_y() - loadedRegion.y_off][node.getPos_x() - 1 - loadedRegion.x_off].setCameFrom(node);
            toReturn.add(loadedNodes[node.getPos_y() - loadedRegion.y_off][node.getPos_x() - 1 - loadedRegion.x_off]);
        }

        //right
        if(node.getPos_x() + 1 < loadedRegion.x_off + loadedRegion.x_size &&
                loadedNodes[node.getPos_y() - loadedRegion.y_off][node.getPos_x() + 1 - loadedRegion.x_off].canTraverse() &&
                !loadedNodes[node.getPos_y() - loadedRegion.y_off][node.getPos_x() + 1 - loadedRegion.x_off].getSearched()) {
            loadedNodes[node.getPos_y() - loadedRegion.y_off][node.getPos_x() + 1 - loadedRegion.x_off].setCameFrom(node);
            toReturn.add(loadedNodes[node.getPos_y() - loadedRegion.y_off][node.getPos_x() + 1 - loadedRegion.x_off]);
        }

        //above
        if(node.getPos_y() - 1 > loadedRegion.y_off &&
                loadedNodes[node.getPos_y() - 1 - loadedRegion.y_off][node.getPos_x() - loadedRegion.x_off].canTraverse() &&
                !loadedNodes[node.getPos_y() - 1 - loadedRegion.y_off][node.getPos_x() - loadedRegion.x_off].getSearched()) {
            loadedNodes[node.getPos_y() - 1 - loadedRegion.y_off][node.getPos_x() - loadedRegion.x_off].setCameFrom(node);
            toReturn.add(loadedNodes[node.getPos_y() - 1 - loadedRegion.y_off][node.getPos_x() - loadedRegion.x_off]);
        }
        //below
        if(node.getPos_y() + 1 < loadedRegion.y_off + loadedRegion.y_size &&
                loadedNodes[node.getPos_y() + 1 - loadedRegion.y_off][node.getPos_x() - loadedRegion.x_off].canTraverse() &&
                !loadedNodes[node.getPos_y() + 1 - loadedRegion.y_off][node.getPos_x() - loadedRegion.x_off].getSearched()) {
            loadedNodes[node.getPos_y() + 1 - loadedRegion.y_off][node.getPos_x() - loadedRegion.x_off].setCameFrom(node);
            toReturn.add(loadedNodes[node.getPos_y() + 1 - loadedRegion.y_off][node.getPos_x() - loadedRegion.x_off]);
        }
        return toReturn;
    }



    /**
     * finds a valid "path" between two nodes and dynamically loads map area needed
     * @param start node to start with (just needs x & y)
     * @param end node to end at (just needs x & y)
     * @return a list of nodes representing the path
     */
    public LinkedList<Node> getPathBetween(Node start, Node end, int buffer){
        //prep for mapping process
        loadRegion(getEncapRegion(start, end, buffer));
        System.out.println("loaded " + loadedRegion);
        //make sure we have the loaded node version of the start and end nodes
        start = getNode(start.pos_x, start.pos_y);
        end = getNode(end.pos_x, end.pos_y);
        setGoal(end); //set new goal node
        LinkedList<Node> toVisit = new LinkedList<>();
        toVisit.add(start); //create new visitation list and add start node
        Node current; //create node instance for manipulation
        //System.out.println("mapping started");
        while(!toVisit.isEmpty()){
            current = toVisit.poll(); //poll the lowest F off the stack
            //System.out.println("searching " + current);
            if(current.equals(end)) break;
            toVisit.addAll(adjacentTo(current));
            Collections.sort(toVisit);// sort by lowest F value
            //System.out.println("sorted list to visit " + toVisit);
        }
        //System.out.println("iteration complete");
        return reconsruct(end, start);

    }

    private void setGoal(Node goal){
        //System.out.println("setting goal");
        for(Node[] nodes : loadedNodes){
            for(Node node : nodes){
                node.setGoal(goal);
            }
        }
    }
    private MapRegion getEncapRegion(Node one, Node two, int buff){
        //load region around the two nodess with a set buffer size (probably 10 by 10)
        int xoff, yoff, xsize, ysize;
        //one is less than two on x
        if(one.pos_x < two.pos_x) {
            xoff = one.pos_x - buff;
            xsize = two.pos_x - xoff + buff;
        }
        else {
            xoff = two.pos_x - buff;
            xsize = one.pos_x - xoff + buff;
        }
        xoff = (xoff < 0)? 0 : xoff;
        //System.out.println("region x off: " + xoff + " x size " + xsize);
        if(one.pos_y < two.pos_y){
            yoff = one.pos_y - buff;
            ysize = two.pos_y - yoff + buff;
        }
        else {
            yoff = two.pos_y - buff;
            ysize = one.pos_y - yoff + buff;
        }
        yoff = (yoff < 0)? 0 : yoff;
        //System.out.println("region y off: " + yoff + " y size " + ysize);

        return new MapRegion(xoff,yoff,xsize,ysize);
    }

    private LinkedList<Node> reconsruct(Node end, Node start){
        LinkedList<Node> path = new LinkedList<>();
        path.addFirst(end); //start from the end and work backwards
        Node prev = end.getCameFrom(); // where did you come from where did you go?
        //System.out.println(prev);
        //System.out.println("reconstructing map");
        while(prev != null && prev != start){
            path.addFirst(prev);
            prev = prev.getCameFrom();
        }
        if(prev == null) return new LinkedList<>();  //short circuit to return empty list
        path.addFirst(start);
        //System.out.println("map reconstructed");
        return path;
    }
}
