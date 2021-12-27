package agh.ics.oop;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

import static java.lang.Math.floor;

public class AnimalCluster {
    private final Map map;
    private final Vector2d position;
    private final ArrayList<Animal> animals = new ArrayList<>();

    public AnimalCluster(Animal animal,Map map) {
        position = animal.getPosition();
        try {
            addAnimal(animal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.map = map;
    }

    public boolean isEmpty(){
        return animals.isEmpty();
    }

    public Vector2d getPosition(){
        return position;
    }

    public void addAnimal(Animal animal) throws Exception {
        if (!animal.isAt(position)) {
            throw new Exception("Cannot add an Animal to a cluster, positions are different and are expected to be the same");
        }
        animals.add(animal);
    }

    public void removeAnimal(Animal animal){
        animals.remove(animal);
    }

    public int size() {
        return animals.size();
    }

    public ArrayList<Animal> getStrongest() throws Exception {
        if (animals.size()==0){
            throw new Exception("Cannot get the strongest animal, cluster size is 0 and should be >0");
        }

        ArrayList<Animal> result = new ArrayList<>();
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

    public void feed() throws Exception {
        int numOfStrongest = getStrongest().size();
        for (Animal animal : getStrongest()) {
            animal.addEnergy((int)floor((double)SimulationData.plantEnergy/numOfStrongest));
        }
        map.removePlant(getPosition());
    }

    //select 2 strongest and let them reproduce
    public void reproduce() throws Exception {
        if (animals.size() < 2)
            throw new Exception("Attempted to reproduce less then 2 animals");
        animals.sort(Comparator.comparing(Animal::getEnergy));
        Animal a1 = animals.get(animals.size()-1);
        Animal a2 = animals.get(animals.size()-2);
        if (a1.getEnergy() >= (SimulationData.startEnergy/2) && a2.getEnergy()>= (SimulationData.startEnergy/2)){
            a1.reproduce(a2);
        }
    }

    //delete the weakest Animals and return their life statistics
    public LifeStatistics cullTheWeaklings() {
        int daysLived = 0;
        int died = 0;
        Iterator<Animal> i = animals.iterator();
        while (i.hasNext()) {
            Animal animal = i.next();
            if(animal.getEnergy()<SimulationData.moveEnergy){
                daysLived += animal.getDaysLived();
                died++;
                animal.setAlive(false); // :(
                map.getAnimals().remove(animal);
                i.remove();
            }
        }
        return new LifeStatistics(daysLived,died);
    }

}
