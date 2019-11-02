import aStar.MapRegion;
import aStar.MapV2;
import aStar.Node;

import java.io.File;
import java.util.Scanner;

public class MainV2 {

    public static void main(String[] args){
        System.out.println("creating map");
        MapV2 inst = new MapV2(, 1000,1000, new MapRegion(0,0,1,1));
        System.out.println("map created");
        Scanner input = new Scanner(System.in);
        Node start = new Node(input.nextInt(),input.nextInt());
        Node end = new Node(input.nextInt(),input.nextInt());
        System.out.println("path from " + start + " to " + end);
        System.out.println(inst.getPathBetween(start, end, 10));
        input.close();

    }

}
