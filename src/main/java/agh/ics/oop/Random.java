package agh.ics.oop;

public class Random {
    public static int getInt(int min,int max){
        return min + (int)(Math.random() * ((max - min) + 1));
    }
    public static MapDirection getOrientation(){
        return MapDirection.values()[(getInt(0,7))];
    }
}
