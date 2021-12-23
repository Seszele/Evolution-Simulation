package agh.ics.oop;

public interface IPositionObserver {
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition,Animal movedAnimal);
}
