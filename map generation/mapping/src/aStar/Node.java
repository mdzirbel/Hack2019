package aStar;

import static aStar.Node.TraversalState.*;

public class Node implements Comparable<Node>{

    public enum TraversalState{
        IMPASSABLE(9, "Impassable"),  //cannot pass through
        UNKNOWN(6, "Unknown"),     //this node is unknown need more data
        DEFAULT(6, "Default map creation"),
        UNCERTAIN(4, "Uncertain"),   //data inconclusive but suggests passable
        PASSABLE(2, "Passable"),    //passable node
        TRAVERSED(1, "Traversed");   //node that has already been traversed

        private int weight;
        private String name;

        TraversalState(int weight, String name){
            this.weight = weight;
            this.name = name;
        }

        public int getWeight() {
            return weight;
        }

        @Override
        public String toString() {
            return name;
        }

        public String serialize(){
            return "" + weight;
        }

    }

    public static TraversalState getStateFromInt(int stateCode){
        switch (stateCode){
            case 9: return IMPASSABLE;
            case 4: return UNCERTAIN;
            case 2: return PASSABLE;
            case 1: return TRAVERSED;
            default: return UNKNOWN;
            //ignore map default b/c it should result in unknown
        }
    }

    //needed at load time
    protected int pos_x, pos_y;
    protected TraversalState traversalState;

    //needed at mapping time
    protected boolean hasSearched = false;
    protected Node cameFrom = null, goal = null;
    protected int cost;

    /**
     * creates new instance of node from x,y position and a traversal state
     * @param pos_x x position in a map
     * @param pos_y y position in a map
     * @param traversalState traversal state of the node
     */
    public Node(int pos_x, int pos_y, TraversalState traversalState ){
        this.pos_x = pos_x;
        this.pos_y = pos_y;
        this.traversalState = traversalState;
        cost = 0;
    }

    /**
     * default constructor that initializes to unknown traversal state
     * @param pos_x x position in a map
     * @param pos_y y position in a map
     */
    public Node(int pos_x, int pos_y){
        this(pos_x, pos_y, TraversalState.UNKNOWN);
    }

    public int getPos_x(){
        return pos_x;
    }

    public int getPos_y() {
        return pos_y;
    }

    public Node getCameFrom() {
        return cameFrom;
    }

    public void setCameFrom(Node cameFrom) {
        hasSearched = true;
        cost = cameFrom.cost + 1;
        this.cameFrom = cameFrom;
    }

    public static int distanceTo(Node node1, Node node2){
        return (int) Math.sqrt(Math.pow(node1.getPos_x() - node2.getPos_x(), 2) + Math.pow(node1.getPos_y() - node2.getPos_y(), 2));
    }

    public int getF() {
        return traversalState.getWeight() * cost + distanceTo(this, goal);
    }

    public boolean getSearched() {
        return hasSearched;
    }

    public boolean canTraverse(){
        return traversalState != IMPASSABLE;
    }

    public boolean hasTraversed(){
        return traversalState == TRAVERSED;
    }

    /**
     * updates the traversal state of a node
     * @param traversalState
     */
    public void setTraversalState(TraversalState traversalState) {
        this.traversalState = traversalState;
    }

    public void reset() {
        hasSearched = false;
        cameFrom = null;
        goal = null;
    }

    public void setGoal(Node goal) {
        this.goal = goal;
    }

    @Override
    public int compareTo(Node o) {
        if(getF() < o.getF()) return -1;
        if(getF() > o.getF()) return 1;
        return 0;
    }

    @Override
    public String toString() {
        return "node X: " + pos_x + " Y: " + pos_y;
    }

    public String debug(){
        return "node X: " + pos_x + " Y: " + pos_y + traversalState + " goal: " + goal + " cost: " + cost
                + " F: " + getF() + "came from: " + cameFrom;
    }

    public String serialize(){
        return traversalState.serialize();
    }
}