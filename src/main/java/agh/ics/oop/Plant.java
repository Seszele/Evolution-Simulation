package agh.ics.oop;

public class Plant {
    private Vector2d position;
    private final int storedEnergy = 30;

    public Plant(Vector2d position) {
        this.position = position;
    }

    public boolean isAt(Vector2d position) {
        return position.equals(this.position);
    }

    public Vector2d getPosition(){
        return position;
    }
    @Override
    public String toString() {
        return "#";
    }
}
