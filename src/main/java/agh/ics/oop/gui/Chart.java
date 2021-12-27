package agh.ics.oop.gui;

import agh.ics.oop.SimulationEngine;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class Chart {
    final int MAX_ENTRIES = 40;
    private final LineChart<String,Number> lineChart;
    private final Label dominantLabel;
    private final VBox resultRoot = new VBox(2);
    private final SimulationEngine simulation;

    private final Series<String, Number> numOfAnimalsSeries = new Series<>();
    private final Series<String, Number> numOfPlantsSeries = new Series<>();
    private final Series<String, Number> averageEnergySeries = new Series<>();
    private final Series<String, Number> averageLifespanSeries = new Series<>();
    private final Series<String, Number> averageChildCountSeries = new Series<>();

    public Chart(SimulationEngine simulation,String title) {
        this.simulation = simulation;

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Epoch");
        xAxis.setAnimated(false);
        yAxis.setLabel("Value");
        yAxis.setAnimated(false);

        lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(title);
        lineChart.setAnimated(false);
        lineChart.setCreateSymbols(false);

        numOfAnimalsSeries.setName("Number of animals");
        numOfPlantsSeries.setName("Number of plants");
        averageEnergySeries.setName("Average energy");
        averageLifespanSeries.setName("Average lifespan");
        averageChildCountSeries.setName("Average number of children");

        dominantLabel = new Label("Dominant genome: "+simulation.getGenomeDominant());

        // add series to chart
        lineChart.getData().addAll(numOfAnimalsSeries,averageEnergySeries,numOfPlantsSeries,averageLifespanSeries,averageChildCountSeries);
        resultRoot.getChildren().addAll(dominantLabel,lineChart);
    }

    public void redraw(){
        try{
            dominantLabel.setText("Dominant genome: "+simulation.getGenomeDominant());
        }
        catch (Exception e){
            return;
        }

        numOfAnimalsSeries.getData().add(new XYChart.Data<>(String.valueOf(simulation.getEpochCount()),simulation.getAnimalCount()));
        numOfPlantsSeries.getData().add(new XYChart.Data<>(String.valueOf(simulation.getEpochCount()),simulation.getPlantCount()));
        averageEnergySeries.getData().add(new XYChart.Data<>(String.valueOf(simulation.getEpochCount()),simulation.getAverageEnergy()));
        averageLifespanSeries.getData().add(new XYChart.Data<>(String.valueOf(simulation.getEpochCount()),simulation.getAverageLifespan()));
        averageChildCountSeries.getData().add(new XYChart.Data<>(String.valueOf(simulation.getEpochCount()),simulation.getAverageChildCount()));
        if (numOfAnimalsSeries.getData().size() > MAX_ENTRIES)
            for (int i = 0; i < lineChart.getData().size(); i++) {
                lineChart.getData().get(i).getData().remove(0);
            }
    }

    public VBox getChart(){
        return resultRoot;
    }


}
