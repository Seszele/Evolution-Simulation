package agh.ics.oop;

import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.floor;

public class SimulationEngine {
    Map map = new Map(7,5,true);
    Animal animal1 = new Animal(15,new Vector2d(0,0),map);
    Animal animal2 = new Animal(10,new Vector2d(1,1),map);
    Animal animal3 = new Animal(44,new Vector2d(1,1),map);

    public SimulationEngine() {
        System.out.println("start");

//        map.placePlant(new Vector2d(1,2));
        System.out.println("sim====================sim");
        run();
        run();
        run();
        run();
        run();
        run();
        run();
        run();
        run();
        run();

        //TODO LATER Gui, dzialanie symulacji na threadah w gui (pamietaj o 2 mapach)

    }

    public void run(){
        removeDeadAnimals();
        moveAnimals();
        feedClusters();
        reproduce();
        growPlants();

        //dla testu
        System.out.println("=======Posdumowanie po ruchach================");
        for (Animal animal :
                map.getAnimals()) {
            System.out.println(animal+" ma "+animal.getEnergy()+" energii na "+animal.getPosition());
        }
        System.out.println("===============================================");
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

    //UWAGA nie mozesz ruszac zwierzetami z clusterow, potrzebujesz to robic z listy zwierzakow np
    public void moveAnimals(){
        for (Animal animal : map.getAnimals()) {
            animal.geneticMove();
        }
    }

    public void growPlants(){
        //normal plant
        growPlant(false);
        growPlant(true);

    }
    private boolean growPlant(boolean isJunglePlant){
        Set<Vector2d> alreadyDrawn = new HashSet<>();
        for (int y = 0; y <= map.getDimension().y; y++) {
            for (int x = 0; x <= map.getDimension().x; x++) {
                //musze zdrwawowac ktory nie byl zdrawowany
                Vector2d draw = Random.getVector(map.getDimension().x,map.getDimension().y);
                while(alreadyDrawn.contains(draw)){
                    draw = Random.getVector(map.getDimension().x,map.getDimension().y);
                }
                alreadyDrawn.add(draw);
                if (isJunglePlant){
                    if (map.isJungle(draw) && !map.isOccupied(draw)){
                        //place
                        map.placePlant(draw);
                        System.out.println("klade trawsko w dzungli");
                        return true;
                    }
                }
                else{
                    if (!map.isJungle(draw) && !map.isOccupied(draw)){
                        //place
                        map.placePlant(draw);
                        System.out.println("klade trawsko normalne");
                        return true;
                    }
                }
            }
        }
        System.out.println("nie udalo sie postwic trawy!(pewnie jest pelna mapa) | SimulationEngine");
        return false;
    }

    public void removeDeadAnimals(){
        for (AnimalCluster cluster : map.getClusters()) {
            cluster.cullTheWeaklings();
            if (cluster.size() == 0){
                map.removeCluster(cluster);
            }
        }
    }
}
