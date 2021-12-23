package agh.ics.oop;

import javax.swing.plaf.IconUIResource;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Map implements IPositionObserver {//nazwa taka jak Map z javy, uwaga

    public Map(int x,int y,boolean wrapped) {
        this.wrapped = wrapped;
        this.dimension = new Vector2d(x,y);
    }

    private boolean wrapped;
    private Vector2d dimension;//od (0,0) jest mapa
    //array z dead animals?
    private final ArrayList<Animal> animals = new ArrayList<>();
    private final LinkedHashMap<Vector2d,Plant> plants = new LinkedHashMap<>();
    //na koncu kazdego ruchu zwierzat przejdz przez nich i porozdawaj do clusterów
    private final LinkedHashMap<Vector2d,AnimalCluster> animalClusters = new LinkedHashMap<>();

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

    @Override
    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal movedAnimal) {
        if (oldPosition.equals(newPosition)){
//            throw new Exception("");
            System.out.println("pozycje sa takie same | Map");
            return;
        }
        //nie ma clustera, tworze nowy
        if(!animalClusters.containsKey(newPosition)){
            animalClusters.put(newPosition,new AnimalCluster(movedAnimal));
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


    //createClusters() <- lepiej na zywo chyba
    //feedClusters() <- a to chyba w jakims engine nie?

    //public-temporary for debugging
    public void placePlant(Vector2d pos){
        if (!plants.containsKey(pos)){
            plants.put(pos,new Plant(pos));
        }
    }
    public LinkedHashMap<Vector2d, AnimalCluster> getClusters(){
        return animalClusters;
    }

    //Return animal clusters where there is a plant growing
    public ArrayList<AnimalCluster> getHungryClusters(){
        ArrayList<AnimalCluster> result = new ArrayList<AnimalCluster>();
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
        if(!animalClusters.containsKey(animal.getPosition())){
            animalClusters.put(animal.getPosition(),new AnimalCluster(animal));
        }
        else{
            animalClusters.get(animal.getPosition()).addAnimal(animal);
        }
        System.out.println("wkladam zwierzaka "+animal.getPosition());
    }

    //metoda poschanged(oldpos,animal)
}
