package agh.ics.oop;

import java.util.ArrayList;

public class Animal {
    private Vector2d position;
    private MapDirection orientation = MapDirection.NORTH;
    private int energy;
    private Map map;
    private ArrayList<IPositionObserver> positionObservers = new ArrayList<>();

    public Animal(int baseEnergy, Vector2d position,Map map) {
        energy = baseEnergy;
        this.position = position;
        this.map = map;
        map.place(this);
        positionObservers.add(map);
    }

    public Vector2d getPosition() {
        return position;
    }
    public MapDirection getOrientation() {
        return orientation;
    }
    public boolean isAt(Vector2d position){
        return position.equals(this.position);
    }

    //tu mozesz wywoływac zamieniajac dane na MoveDirection
    public void move(MoveDirection direction){
        switch (direction) {
            case FORWARD -> {
                moveForward();
            }
            case BACKWARD -> {
                //obrót i ruch
                orientation = MapDirection.values()[(orientation.ordinal()+direction.ordinal())%8];
                moveForward();
            }
            default -> {
                //rotate by enum value
                orientation = MapDirection.values()[(orientation.ordinal()+direction.ordinal())%8];
            }
        }
    }

    //method that contacts map and returns actual pos to move
    private Vector2d validateDestination(Vector2d destination) {
        if (map.isWrapped() && map.posOutOfBounds(destination)){
            //nie spradzamy chyba czy slot jest zajety czy cos
            return map.wrapPosition(destination);
        }
        if (!map.isWrapped() && map.posOutOfBounds(destination)){
            return position;//nie ruszamy sie (traci kolejke)
        }
        return destination;
    }

    //moves forward one tile and notifies observers
    private void moveForward(){
        Vector2d destination = position.add(orientation.toVector());
        destination = validateDestination(destination);
        if (!position.equals(destination))
        {
            Vector2d oldPos = position;
//            System.out.println("oldPos"+oldPos);
            position = destination;
//            System.out.println("oldPos"+oldPos);

            for (IPositionObserver observer : positionObservers) {
                observer.positionChanged(oldPos,destination,this);
            }
        }

    }

    public int getEnergy() {
        return energy;
    }

    public void addEnergy(int energy) {
        System.out.println("dodaje energii: "+energy);
        this.energy += energy;
    }
}
