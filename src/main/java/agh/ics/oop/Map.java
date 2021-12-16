package agh.ics.oop;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Map {//nazwa taka jak Map z javy, uwaga

    public Map(int x,int y,boolean wrapped) {
        this.wrapped = wrapped;
        this.dimension = new Vector2d(x,y);
    }

    private boolean wrapped;
    private Vector2d dimension;//od (0,0) jest mapa
    //array z dead animals?
    private final ArrayList<Animal> animals = new ArrayList<>();
    private final java.util.Map<Vector2d,Plant> plants = new LinkedHashMap<>();
    private final java.util.Map<Vector2d,AnimalCluster> animalCluster = new LinkedHashMap<>();

    public boolean isWrapped(){
        return wrapped;
    }
    //returns outside bound position wrapped to the other side
    public Vector2d wrapPosition(Vector2d position){
        if (!isWrapped())//debug
            System.out.println("Nie jest wrappująca!! ZLE");
        if (!posOutOfBounds(position)) {
            System.out.println("nie jest poza granicami!! ZLE");
            return position;
        }

        int x = position.x;
        int y = position.y;
        if (position.x < 0)
            x = position.x + dimension.x;
        if (position.y<0)
            y = position.y + dimension.y;
        if (position.x>= dimension.x)
            x = position.x - dimension.x;
        if (position.y >= dimension.y)
            y = position.y - dimension.y;

        return new Vector2d(x,y);

    }

    public boolean posOutOfBounds(Vector2d position){
        return (position.x < 0 || position.y<0 || position.x >= dimension.x || position.y >= dimension.y);
    }

    //metoda poschanged(oldpos,animal)
}
