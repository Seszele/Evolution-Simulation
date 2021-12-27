package agh.ics.oop;

import agh.ics.oop.gui.AnimalFollower;

import java.util.ArrayList;

public class Animal {
    private Vector2d position;
    private MapDirection orientation;
    private int energy;
    private Genome genome = new Genome();
    private final Map map;
    private final ArrayList<IPositionObserver> positionObservers = new ArrayList<>();
    private final ArrayList<Animal> childList = new ArrayList<>();
    private AnimalFollower follower;
    private int daysLived =0;
    private boolean isAlive = true;


    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public Animal(int baseEnergy, Vector2d position,Map map) {
        energy = baseEnergy;
        this.position = position;
        this.map = map;
        orientation = Random.getOrientation();
        map.place(this);
        positionObservers.add(map);
    }
    public Animal(int baseEnergy, Vector2d position,Map map,Genome genome){
        this(baseEnergy,position,map);
        this.genome = genome;
    }

    public Vector2d getPosition() {
        return position;
    }
    public Genome getGenome() {
        return genome;
    }
    public ArrayList<Animal> getChildList() {
        return childList;
    }
    public MapDirection getOrientation() {
        return orientation;
    }
    public void setOrientation(MapDirection orientation){
        this.orientation = orientation;
    }
    public boolean isAt(Vector2d position){
        return position.equals(this.position);
    }

    public void move(MoveDirection direction){
        daysLived++;
        energy-= SimulationData.moveEnergy;
        switch (direction) {
            case FORWARD -> {
                moveForward();
            }
            case BACKWARD -> {
                //rotation and movement
                orientation = MapDirection.values()[(orientation.ordinal()+direction.ordinal())%8];
                moveForward();
            }
            default -> {
                //rotate by enum value
                orientation = MapDirection.values()[(orientation.ordinal()+direction.ordinal())%8];
            }
        }
    }

    //method that checks with map and returns actual pos to move
    private Vector2d validateDestination(Vector2d destination) throws Exception {
        if (map.isWrapped() && map.posOutOfBounds(destination)){
            return map.wrapPosition(destination);
        }
        if (!map.isWrapped() && map.posOutOfBounds(destination)){
            return position;//doesn't move, loses turn
        }
        return destination;
    }

    //moves forward one tile and notifies observers
    private void moveForward(){
        Vector2d destination = position.add(orientation.toVector());
        try {
            destination = validateDestination(destination);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!position.equals(destination))
        {
            Vector2d oldPos = position;
            position = destination;

            for (IPositionObserver observer : positionObservers) {
                try {
                    observer.positionChanged(oldPos,destination,this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public int getEnergy() {
        return energy;
    }

    public void addEnergy(int energy) {
        this.energy += energy;
    }

    public Animal reproduce(Animal partner){
        int percentage = (int) ((double)this.getEnergy()/(partner.getEnergy()+this.getEnergy())*100);
        Genome childGenome = genome.mixGenes(partner.getGenome(),percentage);
        Animal child = new Animal((int)(getEnergy()*0.25)+(int)(partner.getEnergy()*0.25),position.copy(),map,childGenome);
        this.addEnergy(-(int)(getEnergy()*0.25));
        partner.addEnergy(-(int) (partner.getEnergy()*0.25));
        this.addChildToList(child);
        partner.addChildToList(child);
        return child;
    }

    public void addChildToList(Animal child) {
        if (follower!=null){
            follower.noticeAChild(child);
        }
        childList.add(child);
    }
    public Integer getChildCount(){
        return childList.size();
    }

    public void geneticMove() {
        move(genome.pickDirection());
    }

    public int getDaysLived() {
        return daysLived;
    }

    public void removeFollower() {
        follower = null;
    }

    public void setFollower(AnimalFollower animalFollower) {
        follower = animalFollower;
    }
}
