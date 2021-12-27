package agh.ics.oop;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static java.lang.Math.floor;

//w mapie wszystkie zwierzaki
//w mapie hashmapa z clusterami
//jak sie zwierze rusza to patrzy czy jest cluster a jak nie to robi go
//na koniec mozna usunąć puste clustery
public class AnimalCluster {
    Map map;
    private Vector2d position;
    private ArrayList<Animal> animals = new ArrayList<>();

    public AnimalCluster(Animal animal,Map map) {
        position = animal.getPosition();
        addAnimal(animal);
        this.map = map;
    }

    public boolean isEmpty(){
        return animals.isEmpty();
    }

    public Vector2d getPosition(){
        return position;
    }

    //to nie powinno istniec w takiej formie publicznie
    public ArrayList<Animal> getAnimals(){
        return animals;
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

    public int size() {
        return animals.size();
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

    public void feed(){
        int numOfStrongest = getStrongest().size();
        for (Animal animal : getStrongest()) {
            animal.addEnergy((int)floor(SimulationData.plantEnergy/numOfStrongest));
        }
        map.removePlant(getPosition());
    }

    public void reproduce(){
        if (animals.size() < 2){
            System.out.println("Za malo zwirzat do rozmnazania!!! | AnimalCluster");
            return;
        }
        animals.sort(Comparator.comparing(Animal::getEnergy));
        Animal a1 = animals.get(animals.size()-1);
        Animal a2 = animals.get(animals.size()-2);
        if (a1.getEnergy() >= (SimulationData.startEnergy/2) && a2.getEnergy()>= (SimulationData.startEnergy/2)){
            a1.reproduce(a2);
        }
    }

//usun martwe, jesli cluster zrobi sie pusty to usuwasz cluster w SimulationEngine juz a nie tu
    public LifeStatistics cullTheWeaklings() {
        int daysLived = 0;
        int died = 0;
        Iterator<Animal> i = animals.iterator();
        while (i.hasNext()) {
            Animal animal = i.next(); // must be called before you can call i.remove()
            if(animal.getEnergy()<SimulationData.moveEnergy){
                daysLived += animal.getDaysLived();
                died++;
                animal.setAlive(false); // :(
                map.getAnimals().remove(animal);
//                System.out.println("usuwam zwierzaka"+animal+" na clusterze "+position);
                i.remove();
            }
        }

        return new LifeStatistics(daysLived,died);
    }

}
