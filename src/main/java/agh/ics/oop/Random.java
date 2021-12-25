package agh.ics.oop;

public class Random {
    public static int getInt(int min,int max){
        return min + (int)(Math.random() * ((max - min) + 1));
    }
    public static MapDirection getOrientation(){
        return MapDirection.values()[(getInt(0,7))];
    }
    public static Vector2d getVector(int x,int y){
        return new Vector2d(Random.getInt(0,x),Random.getInt(0,y));
    }
}
