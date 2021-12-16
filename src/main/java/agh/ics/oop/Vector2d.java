package agh.ics.oop;

import java.util.Objects;

public class Vector2d {
    public final int x,y;
    public Vector2d(int x,int y) {
        this.x = x;
        this.y = y;
    }
    public String toString(){
        return "("+x+","+y+")";
    }
    public boolean precedes(Vector2d other) {
        return x <= other.x && y <= other.y;
    }
    public boolean follows(Vector2d other){
        return x >= other.x && y >= other.y;
    }
    public Vector2d upperRight(Vector2d other){
        int newX = Math.max(x, other.x);
        int newY = Math.max(y, other.y);
        return new Vector2d(newX,newY);
    }
    public Vector2d lowerLeft(Vector2d other){
        int newX = Math.min(x, other.x);
        int newY = Math.min(y, other.y);
        return new Vector2d(newX,newY);
    }
    public Vector2d add(Vector2d other){
        return new Vector2d(x+ other.x,y+other.y);
    }
    public Vector2d subtract(Vector2d other){
        return new Vector2d(x- other.x,y-other.y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2d vector2d = (Vector2d) o;
        return x == vector2d.x && y == vector2d.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public Vector2d opposite(){
        return new Vector2d(-1*x,-1*y);
    }
}
