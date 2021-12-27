package agh.ics.oop;

import java.util.*;

public class SimulationEngine implements Runnable {
    private volatile boolean running = true;
    private volatile boolean paused = false;
    private final Object pauseLock = new Object();
    private final Map map;

    private final EpochHistorian epochHistorian;
    private final ArrayList<IEpochObserver> epochObservers = new ArrayList<>();
    private Integer epochCount = 0;
    //statistics of dead animals from the simulation
    private LifeStatistics lifeStatistics = new LifeStatistics();
    //how many times was magic map ability used
    private int usedMana = 0;

    public SimulationEngine(Map map,IEpochObserver observer) {
        this.map = map;
        epochHistorian = new EpochHistorian(isWrapped()?"wrappedMap.csv":"solidMap.csv");
        epochObservers.add(observer);
        epochObservers.add(epochHistorian);
        for (int i = 0; i < SimulationData.startingAnimals; i++) {
            new Animal(SimulationData.startEnergy,Random.getVector(map.getDimension().x,map.getDimension().y),map);
        }
    }

    @Override
    public void run(){
        while(true){
            synchronized (pauseLock) {
                if (!running) {
                    break;
                }
                if (paused) {
                    try {
                        synchronized (pauseLock) {
                            pauseLock.wait();
                        }
                    } catch (InterruptedException ex) {
                        break;
                    }
                    if (!running) {
                        break;
                    }
                }
            }
            try {
                Thread.sleep(SimulationData.epochInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            removeDeadAnimals();
            //there is no more animals to move, end simulation
            if (getAnimalCount()==0){
                stop();
                break;
            }
            moveAnimals();
            feedClusters();
            reproduce();
            growPlants();
            epochCount++;
            notifyObservers();
            if (map.isMagical() && map.getAnimals().size()==5 && usedMana <=3){
                usedMana++;
                spawnMagicalBeings();
            }
        }

    }

    private void spawnMagicalBeings() {
        ArrayList<Animal> copyLiving = new ArrayList<>(map.getAnimals());
        for (Animal animal : copyLiving) {
            new Animal(SimulationData.startEnergy,Random.getVector(map.getDimension().x,map.getDimension().y),map,animal.getGenome().cloneGenome());
        }
    }

    public void feedClusters(){
        for (AnimalCluster cluster : map.getHungryClusters()) {
            try {
                cluster.feed();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void reproduce(){
        for (AnimalCluster cluster : map.getClusters()) {
            if (cluster.size() >= 2){
                try {
                    cluster.reproduce();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void moveAnimals(){
        for (Animal animal : map.getAnimals()) {
            animal.geneticMove();
        }
    }

    public void growPlants(){
        growPlant(false);
        growPlant(true);

    }

    public void addEpochObserver(IEpochObserver observer){
        epochObservers.add(observer);
    }

    private void notifyObservers(){
        for (IEpochObserver epochObserver : epochObservers) {
            epochObserver.epochConcluded(this);
        }
    }

    //grows 1 plant in jungle or on normal terrain
    private void growPlant(boolean isJunglePlant){
        Set<Vector2d> alreadyDrawn = new HashSet<>();
        for (int y = 0; y <= map.getDimension().y; y++) {
            for (int x = 0; x <= map.getDimension().x; x++) {
                Vector2d draw = Random.getVector(map.getDimension().x,map.getDimension().y);
                while(alreadyDrawn.contains(draw)){
                    draw = Random.getVector(map.getDimension().x,map.getDimension().y);
                }
                alreadyDrawn.add(draw);
                if (isJunglePlant){
                    if (map.isJungle(draw) && !map.isOccupied(draw)){
                        //place
                        map.placePlant(draw);
                        return;
                    }
                }
                else{
                    if (!map.isJungle(draw) && !map.isOccupied(draw)){
                        //place
                        map.placePlant(draw);
                        return;
                    }
                }
            }
        }
    }

    public void stop() {
        running = false;
        resume();
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }

    public void removeDeadAnimals(){
        for (AnimalCluster cluster : map.getClusters()) {
            LifeStatistics clusterStats = cluster.cullTheWeaklings();
            lifeStatistics = lifeStatistics.add(clusterStats);
            if (cluster.size() == 0){
                map.removeCluster(cluster);
            }
        }
    }
    public boolean isWrapped() {
        return map.isWrapped();
    }

    public Map getMap() {
        return map;
    }
    public boolean isPaused() {
        return paused;
    }

    public Integer getAnimalCount() {
        return map.getAnimals().size();
    }
    public Integer getPlantCount(){
        return map.getPlants().size();
    }
    public Double getAverageChildCount(){
        int sum = 0;
        for (Animal animal : map.getAnimals()) {
            sum+=animal.getChildCount();
        }
        return (double)sum/map.getAnimals().size();
    }
    public Double getAverageEnergy(){
        int sum = 0;
        for (Animal animal : map.getAnimals()) {
            sum+=animal.getEnergy();
        }
        return (double)sum/map.getAnimals().size();
    }
    public Double getAverageLifespan(){
        return  lifeStatistics.getAverage();
    }
    public Integer getEpochCount() {
        return epochCount;
    }
    public Genome getGenomeDominant(){
        ArrayList<Genome> genomes = new ArrayList<>();
        for (Animal animal : map.getAnimals()) {
            genomes.add(animal.getGenome());
        }
        Set<Genome> mySet = new HashSet<>(genomes);
        LinkedHashMap<Genome,Integer> genomeHashMap = new LinkedHashMap<>();

        for(Genome genome: mySet){
            genomeHashMap.put(genome,Collections.frequency(genomes,genome));
        }

        return genomeHashMap.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
    }
    public int getUsedMana() {
        return usedMana;
    }
    public ArrayList<Vector2d> getPosOfDominants(){
        Genome dominant = getGenomeDominant();
        ArrayList<Vector2d> result = new ArrayList<>();
        for (Animal animal : map.getAnimals()) {
            if (animal.getGenome().equals(dominant)){
                result.add(animal.getPosition());
            }
        }
        return result;
    }

    public void saveToCSV() {
        epochHistorian.saveToCSV();
    }
}
