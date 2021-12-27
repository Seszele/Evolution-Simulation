package agh.ics.oop;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Map implements IPositionObserver {
    private final boolean wrapped;
    private final boolean magical;
    //map begins at (0,0)
    private final Vector2d dimension;

    private final ArrayList<Animal> animals = new ArrayList<>();
    private final LinkedHashMap<Vector2d,Plant> plants = new LinkedHashMap<>();
    private final LinkedHashMap<Vector2d,AnimalCluster> animalClusters = new LinkedHashMap<>();

    public Map(int x, int y, boolean wrapped, boolean magical) {
        this.wrapped = wrapped;
        this.magical =magical;
        this.dimension = new Vector2d(x,y);
        placePlants();
    }

    //the borders can be bigger then specified
    public Vector2d jungleUpperRight(){
        float centerX = (float)dimension.x/2;
        float centerY = (float)dimension.y/2;
        int rightX = (int) Math.ceil(centerX+ (float)dimension.x*SimulationData.jungleRatio/100/2);
        int rightY = (int) Math.ceil(centerY+ (float)dimension.y*SimulationData.jungleRatio/100/2);
        return new Vector2d(rightX,rightY);
    }
    public Vector2d jungleLowerLeft(){
        float centerX = (float)dimension.x/2;
        float centerY = (float)dimension.y/2;
        int leftX = (int) Math.floor(centerX- (float)dimension.x*SimulationData.jungleRatio/100/2);
        int leftY = (int) Math.floor(centerY- (float)dimension.y*SimulationData.jungleRatio/100/2);
        return new Vector2d(leftX,leftY);

    }
    public boolean isJungle(Vector2d position){
        return position.follows(jungleLowerLeft()) && position.precedes(jungleUpperRight());
    }

    //go through all tiles and place plants based on the chance (it's higher for a jungle)
    private void placePlants(){
        for (int y = 0; y <= dimension.y; y++) {
            for (int x = 0; x <= dimension.x; x++) {
                if (isJungle(new Vector2d(x,y))){
                    if (Random.getInt(0,100)<=SimulationData.plantsGrowth*SimulationData.jungleMultiplier){
                        plants.put(new Vector2d(x,y),new Plant(new Vector2d(x,y)));
                    }
                }
                else if(Random.getInt(0,100)<=SimulationData.plantsGrowth){
                    plants.put(new Vector2d(x,y),new Plant(new Vector2d(x,y)));
                }
            }
        }
    }

    public boolean isWrapped(){
        return wrapped;
    }
    public boolean isOccupied(Vector2d position){
        return plants.containsKey(position) || animalClusters.containsKey(position);
    }

    //returns outside bound position wrapped to the other side
    public Vector2d wrapPosition(Vector2d position) throws Exception {
        if (!isWrapped())
            throw new Exception("Tried to call method wrapPosition on map that isn't wrapping");
        if (!posOutOfBounds(position))
            throw new Exception("Tried to wrap position that is not out of bounds and doesn't need wrapping");

        int x = position.x;
        int y = position.y;
        if (position.x < 0)
            x = position.x + dimension.x+1;
        if (position.y<0)
            y = position.y + dimension.y+1;
        if (position.x> dimension.x)
            x = 0;
        if (position.y > dimension.y)
            y =0;

        return new Vector2d(x,y);
    }

    public boolean posOutOfBounds(Vector2d position){
        return (position.x < 0 || position.y<0 || position.x > dimension.x || position.y > dimension.y);
    }

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal movedAnimal) throws Exception {
        if (oldPosition.equals(newPosition))
            throw new Exception("positionChanged() was called with the same positions");
        //there is no cluster, make a new one
        if(!animalClusters.containsKey(newPosition)){
            animalClusters.put(newPosition,new AnimalCluster(movedAnimal,this));
        }
        else{
            animalClusters.get(newPosition).addAnimal(movedAnimal);
        }
        //delete animal from old cluster
        animalClusters.get(oldPosition).removeAnimal(movedAnimal);
        if (animalClusters.get(oldPosition).isEmpty()){
            animalClusters.remove(oldPosition);
        }
    }

    public void placePlant(Vector2d pos){
        if (!plants.containsKey(pos)){
            plants.put(pos,new Plant(pos));
        }
    }
    public ArrayList<AnimalCluster> getClusters(){
        return new ArrayList<>(animalClusters.values());
    }

    //Returns animal clusters where there is a plant growing
    public ArrayList<AnimalCluster> getHungryClusters(){
        ArrayList<AnimalCluster> result = new ArrayList<>();
        for (Vector2d plantPos : plants.keySet()) {
            if (animalClusters.get(plantPos) != null){
                result.add(animalClusters.get(plantPos));
            }
        }
        return result;
    }

    public void place(Animal animal) {
        animals.add(animal);
        if(!animalClusters.containsKey(animal.getPosition())){
            animalClusters.put(animal.getPosition(),new AnimalCluster(animal,this));
        }
        else{
            try {
                animalClusters.get(animal.getPosition()).addAnimal(animal);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void removePlant(Vector2d position) {
        plants.remove(position);
    }

    public void removeCluster(AnimalCluster cluster) {
        animalClusters.remove(cluster.getPosition(),cluster);
    }

    //returns null if not occupied
    public Object getObjectAt(Vector2d position) {
        if (animalClusters.containsKey(position)){
            try {
                return animalClusters.get(position).getStrongest().get(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return plants.get(position);
    }

    public ArrayList<Plant> getPlants() {
        return new ArrayList<>(plants.values());
    }
    public boolean isMagical() {
        return magical;
    }
    public Vector2d getDimension() {
        return dimension;
    }
    public ArrayList<Animal> getAnimals() {
        return animals;
    }

}
