package agh.ics.oop;

import java.util.ArrayList;
import java.util.List;
//w mapie wszystkie zwierzaki
//w mapie hashmapa z clusterami
//jak sie zwierze rusza to patrzy czy jest cluster a jak nie to robi go
//na koniec mozna usunąć puste clustery
public class AnimalCluster {
    private Vector2d position;
    private ArrayList<Animal> animals = new ArrayList<>();

    public AnimalCluster(Animal animal) {
        position = animal.getPosition();
        addAnimal(animal);
    }

    public boolean isEmpty(){
        return animals.isEmpty();
    }

    public Vector2d getPosition(){
        return position;
    }

    public boolean addAnimal(Animal animal){
        if (!animal.isAt(position)) {
            System.out.println("Chce dodac"+animal.getPosition()+"To nie jest to samo pole!! | AnimalCluster");
            return false;
        }
        animals.add(animal);
        return true;
    }

    public boolean removeAnimal(Animal animal){
        animals.remove(animal);
        return true;
    }

    public ArrayList<Animal> getStrongest(){
        if (animals.size()==0){
            System.out.println("Tu powinien byc throw | animal cluster");
        }

        ArrayList result = new ArrayList<>();
        int maxEnergy = 0;
        for (Animal animal : animals) {
            maxEnergy = Math.max(maxEnergy,animal.getEnergy());
        }
        for (Animal animal : animals) {
            if (animal.getEnergy() == maxEnergy){
                result.add(animal);
            }
        }
        return result;
    }
}
