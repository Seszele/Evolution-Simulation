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

    public boolean addAnimal(Animal animal){
        if (!animal.isAt(position)) {
            System.out.println("To nie jest to samo pole");
            return false;
        }
        animals.add(animal);
        return true;
    }

    public boolean removeAnimal(Animal animal){
        if (!animal.isAt(position)) {
            System.out.println("To nie jest to samo pole");
            return false;
        }
        animals.remove(animal);
        return true;
    }
}
