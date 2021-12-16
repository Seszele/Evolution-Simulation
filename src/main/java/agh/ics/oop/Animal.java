package agh.ics.oop;

public class Animal {
    private Vector2d position;
    private MapDirection orientation = MapDirection.NORTH;
    private int energy;
    private Map map;

    public Animal(int baseEnergy, Vector2d position) {
        energy = baseEnergy;
        this.position = position;
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
                Vector2d destination = position.add(orientation.toVector());
                destination = validateDestination(destination);
            }
            case BACKWARD -> {
            }
            default -> {
                //rotate by enum value
                System.out.println(orientation);
                orientation = MapDirection.values()[(orientation.ordinal()+direction.ordinal())%8];
                System.out.println(orientation);
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
}
