package agh.ics.oop;
//TODO epoch historian nie statyczny i w srodku simengine powinien byc (tak lepiej)
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EpochHistorian implements IEpochObserver{
    private String FILE_CSV_NAME = "test.csv";
    private List<String[]> dataEntries = new ArrayList<>();

    private boolean overBound = false;
    private int epoch = 0;
    private int animalSum = 0;
    private int plantSum = 0;
    private int energySum = 0;
    private int lifespanSum = 0;
    private int childNumSum = 0;

    public EpochHistorian(String fileName) {
        FILE_CSV_NAME = fileName;
        dataEntries.add(new String[]{ "Epoch", "Animal count", "Plant count","Average energy","Average lifespan","Average children number"});
    }

    @Override
    public void epochConcluded(SimulationEngine sim) {
        dataEntries.add(new String[]{  sim.getEpochCount().toString(), sim.getAnimalCount().toString(),sim.getPlantCount().toString(),
                sim.getAverageEnergy().toString(),sim.getAverageLifespan().toString(),sim.getAverageChildCount().toString() });
        try{
            epoch = sim.getEpochCount();
            animalSum+=sim.getAnimalCount();
            plantSum+=sim.getPlantCount();
            energySum+=sim.getAverageEnergy();
            lifespanSum+=sim.getAverageLifespan();
            childNumSum+=sim.getAverageChildCount();
        }
        catch (Exception e){
            overBound = true;
        }

    }

    public void saveToCSV(){
        addFinalLine();//podsumowanie
        File csvOutputFile = new File(FILE_CSV_NAME);
        try (PrintWriter pw = new PrintWriter(csvOutputFile)) {
            dataEntries.stream()
                    .map(this::convertToCSV)
                    .forEach(pw::println);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void addFinalLine() {
        if (overBound){
            dataEntries.add(new String[]{"Values are too big to summarize"});
        }
        else{
            dataEntries.add(new String[]{ "Averages", avg(animalSum),avg(plantSum),avg(energySum),avg(lifespanSum),avg(childNumSum)});
        }
    }

    public String convertToCSV(String[] data) {
        return Stream.of(data)
                .collect(Collectors.joining(","));
    }
    private String avg(double val){
        return String.valueOf(val/epoch);
    }
    private String avg(int val){
        return String.valueOf((double)val/epoch);
    }
}
