package agh.ics.oop;

import javax.swing.plaf.IconUIResource;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Map implements IPositionObserver {//nazwa taka jak Map z javy, uwaga
    private boolean wrapped;

    public Vector2d getDimension() {
        return dimension;
    }

    private Vector2d dimension;//od (0,0) jest mapa

    public Map(int x,int y,boolean wrapped) {
        this.wrapped = wrapped;
        this.dimension = new Vector2d(x,y);
        //tmp
        findJungleBorders();
        placePlants();
    }

    //array z dead animals?
    private final ArrayList<Animal> animals = new ArrayList<>();
    private final LinkedHashMap<Vector2d,Plant> plants = new LinkedHashMap<>();
    private final LinkedHashMap<Vector2d,AnimalCluster> animalClusters = new LinkedHashMap<>();

    //if not nice borders it's actually bigger
    private void findJungleBorders(){
        float centerX = (float)dimension.x/2;
        float centerY = (float)dimension.y/2;
        int rightX = (int) Math.ceil(centerX+ (float)dimension.x*SimulationData.jungleRatio/100/2);
        int rightY = (int) Math.ceil(centerY+ (float)dimension.y*SimulationData.jungleRatio/100/2);
        System.out.println(rightX+" "+rightY);
        int leftX = (int) Math.floor(centerX- (float)dimension.x*SimulationData.jungleRatio/100/2);
        int leftY = (int) Math.floor(centerY- (float)dimension.y*SimulationData.jungleRatio/100/2);
        System.out.println(leftX+" "+leftY);
    }
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

    public ArrayList<Animal> getAnimals() {
        return animals;
    }

    private void placePlants(){
        //go through all tiles and place plants based on the chance (it's higher for a jungle)
        for (int y = 0; y <= dimension.y; y++) {
            for (int x = 0; x <= dimension.x; x++) {
                if (isJungle(new Vector2d(x,y))){
//                    System.out.println("rosne w jungli "+x+" "+y);
                    if (Random.getInt(0,100)<=SimulationData.plantsGrowth*SimulationData.jungleMultiplier){
                        plants.put(new Vector2d(x,y),new Plant(new Vector2d(x,y)));
                    }
                }
                else if(Random.getInt(0,100)<=SimulationData.plantsGrowth){
                    plants.put(new Vector2d(x,y),new Plant(new Vector2d(x,y)));
                }
//                System.out.println("rosne dziko "+x+" "+y);
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

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal movedAnimal) {
        if (oldPosition.equals(newPosition)){
//            throw new Exception("");
            System.out.println("pozycje sa takie same! | Map");
            return;
        }
        //nie ma clustera, tworze nowy
        if(!animalClusters.containsKey(newPosition)){
            animalClusters.put(newPosition,new AnimalCluster(movedAnimal,this));
        }
        else{
            animalClusters.get(newPosition).addAnimal(movedAnimal);
        }
        //usuwam animala ze starego clustera
        animalClusters.get(oldPosition).removeAnimal(movedAnimal);
        //jesli jest pusty to usuwam cluster
        if (animalClusters.get(oldPosition).isEmpty()){
            animalClusters.remove(oldPosition);
        }
        //aktualizacja animals hashmapy jesli bedzie potrzebna

        System.out.println("ruszyl sie z "+oldPosition+" do "+newPosition);
    }


    //public-temporary for debugging
    public void placePlant(Vector2d pos){
        if (!plants.containsKey(pos)){
            plants.put(pos,new Plant(pos));
        }
    }
    public ArrayList<AnimalCluster> getClusters(){
        return new ArrayList<>(animalClusters.values());
    }
    public ArrayList<Animal> getAnimalsAt(Vector2d position){
        return animalClusters.get(position).getAnimals();
    }

    //Return animal clusters where there is a plant growing
    public ArrayList<AnimalCluster> getHungryClusters(){
        ArrayList<AnimalCluster> result = new ArrayList<AnimalCluster>();
//        if (plants.size() == 0){
//            System.out.println("nie ma roslin lol | Map");
//            return result;
//        }
//        System.out.println("wtf: "+animalClusters.get(new Vector2d(1,2)));
        for (Vector2d plantPos : plants.keySet()) {
            if (animalClusters.get(plantPos) != null){
                result.add(animalClusters.get(plantPos));
            }
        }
//        System.out.println("the result: "+result);

        return result;
    }

    public void place(Animal animal) {
        animals.add(animal);
        if(!animalClusters.containsKey(animal.getPosition())){
            animalClusters.put(animal.getPosition(),new AnimalCluster(animal,this));
        }
        else{
            animalClusters.get(animal.getPosition()).addAnimal(animal);
        }
        System.out.println("wkladam zwierzaka "+animal.getPosition());
    }

    public void removePlant(Vector2d position) {
        plants.remove(position);
    }

    public void removeCluster(AnimalCluster cluster) {
        animalClusters.remove(cluster.getPosition(),cluster);
    }

    //metoda poschanged(oldpos,animal)
}
