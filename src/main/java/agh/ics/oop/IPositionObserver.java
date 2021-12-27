package agh.ics.oop;

public interface IPositionObserver {
    void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal movedAnimal) throws Exception;
}
