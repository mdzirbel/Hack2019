package aStar;

public class MapRegion {

    int x_off, y_off, x_size, y_size;

    public MapRegion(int x_off, int y_off, int x_size, int y_size){
        this.x_off = x_off; this.y_off = y_off; this.x_size = x_size; this.y_size = y_size;
    }


    public String toString(){
        return "region from (" + x_off + ", " + y_off + ") to (" + (x_off + x_size) + ", " + (y_off + y_size) +")";
    }

    public String debug() {
        return "region at X: " + x_off + " Y:" + y_off + " of size [" + x_size + ", " + y_size + "]";
    }
}
