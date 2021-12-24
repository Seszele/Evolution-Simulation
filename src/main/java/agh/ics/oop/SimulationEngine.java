package agh.ics.oop;

import static java.lang.Math.floor;

public class SimulationEngine {
    Map map = new Map(4,5,true);
    Animal animal1 = new Animal(50,new Vector2d(0,0),map);
    Animal animal2 = new Animal(20,new Vector2d(1,1),map);
    Animal animal3 = new Animal(44,new Vector2d(3,2),map);

    public SimulationEngine() {
        System.out.println("start");

        map.placePlant(new Vector2d(1,2));
        System.out.println("sim====================sim");
//        feedClusters();
        animal2.move(MoveDirection.FORWARD);
        animal1.move(MoveDirection.FORWARD);
        animal1.move(MoveDirection.ROT45);
        animal1.move(MoveDirection.FORWARD);
        int[] tmp ={0,0,0,0,0,0,0,0,0,0,0,0,6,6,3,3,4,4,4,4,5,5,5,5,6,6,6,6,7,7,7,2};
        animal1.getGenome().setGenes(tmp);
//        System.out.println(animal2.getPosition());
        Animal child = animal1.reproduce(animal2);
        System.out.println(child.getEnergy());
        System.out.println(child.getGenome());
        feedClusters();
        animal2.move(MoveDirection.FORWARD);
        animal1.move(MoveDirection.FORWARD);

        feedClusters();

    }

    public void feedClusters(){
        for (AnimalCluster cluster : map.getHungryClusters()) {
            cluster.feed();
        }
    }

    public void reproduce(){
        for (AnimalCluster cluster : map.getClusters()) {
            if (cluster.size() >= 2){
                cluster.reproduce();
            }
        }
    }
}
