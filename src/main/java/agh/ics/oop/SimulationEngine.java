package agh.ics.oop;

import static java.lang.Math.floor;

public class SimulationEngine {
    Map map = new Map(4,5,true);
    Animal animal1 = new Animal(50,new Vector2d(0,0),map);
    Animal animal2 = new Animal(40,new Vector2d(1,1),map);

    public SimulationEngine() {
        System.out.println("start");

        map.placePlant(new Vector2d(1,2));
        System.out.println("sim====================sim");
//        feedClusters();
        animal2.move(MoveDirection.FORWARD);
        animal1.move(MoveDirection.FORWARD);
        animal1.move(MoveDirection.ROT45);
        animal1.move(MoveDirection.FORWARD);
//        System.out.println(animal2.getPosition());
        feedClusters();
    }

    public void feedClusters(){
        for (AnimalCluster cluster : map.getHungryClusters()) {
            int numOfStrongest = cluster.getStrongest().size();
            for (Animal animal : cluster.getStrongest()) {
                animal.addEnergy((int)floor(SimulationData.plantEnergy/numOfStrongest));
            }
        }
    }
}
