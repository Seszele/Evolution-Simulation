package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

public class App extends Application implements IEpochObserver {
    private MapGui wrappedMapGui;
    private SimulationEngine wrappedSimulation;
    private Thread wrappedSimThread;
    private Chart leftMapChart;

    private Scene simScene;
    private HBox root = new HBox(50);

    private MapGui solidMapGui;
    private SimulationEngine solidSimulation;
    private Thread solidSimThread;
    private Chart rightMapChart;


    @Override
    public void init() throws Exception {
        super.init();
        System.out.println("init");

    }
//TODO  wybieranie zwierzaka(calosc) | LATER magiczna(guziki w intro i implementacja)|LATER rzucanie wyjatkow zamiast sout
    @Override
    public void start(Stage primaryStage) throws Exception {
        simScene = new Scene(root,1300,400);

        Scene introScene = getIntroScene(primaryStage,simScene);

        primaryStage.setScene(introScene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        solidSimulation.stop();
        wrappedSimulation.stop();
    }

    private void setMaps(){
        wrappedSimulation = new SimulationEngine( new Map(SimulationData.width,SimulationData.height,true),this);
        wrappedMapGui = new MapGui(wrappedSimulation.getMap());
        wrappedSimThread =  new Thread((Runnable) wrappedSimulation);
        leftMapChart = new Chart(wrappedSimulation,"Wrapped Simulation Chart");

        solidSimulation = new SimulationEngine(new Map(SimulationData.width,SimulationData.height,false),this);
        solidMapGui = new MapGui(solidSimulation.getMap());
        solidSimThread =  new Thread((Runnable) solidSimulation);
        rightMapChart = new Chart(solidSimulation,"Solid Simulation Chart");


        Button leftSave = new Button("Save to CSV");
        leftSave.setDisable(true);
        leftSave.setOnAction(e -> wrappedSimulation.saveToCSV());
        Button leftShow = new Button("Show");
        leftShow.setDisable(true);
//        leftShow.setOnAction(e -> toggleSimulation(wrappedSimulation));
        Button leftButton = new Button("Toggle pause");
        leftButton.setOnAction(e -> toggleSimulation(wrappedSimulation,leftSave,leftShow));
        HBox leftMapButtons = new HBox(1);
        leftMapButtons.getChildren().addAll(leftButton,leftSave,leftShow);



        Button rightSave = new Button("Save to CSV");
        rightSave.setDisable(true);
        rightSave.setOnAction(e -> solidSimulation.saveToCSV());
        Button rightShow = new Button("Show");
        rightShow.setDisable(true);
//        leftShow.setOnAction(e -> toggleSimulation(wrappedSimulation));
        Button rightButton = new Button("Toggle pause");
        rightButton.setOnAction(e -> toggleSimulation(solidSimulation,rightSave,rightShow));
        HBox rightMapButtons = new HBox(1);
        rightMapButtons.getChildren().addAll(rightButton,rightSave,rightShow);

        VBox leftMap = new VBox(5);
        leftMap.getChildren().addAll(wrappedMapGui.getRoot(),leftMapButtons);
        VBox rightMap = new VBox(5);
        rightMap.getChildren().addAll(solidMapGui.getRoot(),rightMapButtons);

        root.getChildren().addAll(leftMapChart.getChart(),rightMapChart.getChart(),leftMap,rightMap);
    }

    private Scene getIntroScene(Stage primaryStage,Scene simScene) {
        TextField widthField = new TextField ();
        widthField.setText(String.valueOf(SimulationData.width));
        TextField heightField = new TextField ();
        heightField.setText(String.valueOf(SimulationData.height));
        TextField startEnergy = new TextField ();
        startEnergy.setText(String.valueOf(SimulationData.startEnergy));
        TextField moveEnergy = new TextField ();
        moveEnergy.setText(String.valueOf(SimulationData.moveEnergy));
        TextField plantEnergy = new TextField ();
        plantEnergy.setText(String.valueOf(SimulationData.plantEnergy));
        TextField jungleRatio = new TextField ();
        jungleRatio.setText(String.valueOf(SimulationData.jungleRatio));
        TextField plantsGrowth = new TextField ();
        plantsGrowth.setText(String.valueOf(SimulationData.plantsGrowth));
        TextField jungleMultiplier = new TextField ();
        jungleMultiplier.setText(String.valueOf(SimulationData.jungleMultiplier));
        TextField epochInterval = new TextField ();
        epochInterval.setText(String.valueOf(SimulationData.epochInterval));
        TextField startingAnimals = new TextField ();
        startingAnimals.setText(String.valueOf(SimulationData.startingAnimals));

        GridPane grid = new GridPane();

        Button startButton = new Button("Start");
        startButton.setOnAction(event -> {
            ArrayList<Integer> initialValues= new ArrayList<>();
            for (Node child : grid.getChildren()) {
                if (child instanceof TextField){
                    initialValues.add(Integer.valueOf((((TextField) child).getText())));
                    System.out.println(((TextField) child).getText());
                }
            }
            SimulationData.setInitialValues(initialValues);
            setMaps();


            primaryStage.setScene(simScene);
            wrappedSimThread.start();
            solidSimThread.start();
        });

        grid.setVgap(5);
        grid.setHgap(10);
        grid.setPadding(new Insets(5, 5, 5, 5));
        grid.add(new Label("Width: "), 0, 0);
        grid.add(widthField, 1, 0);
        grid.add(new Label("Height: "), 0, 1);
        grid.add(heightField, 1, 1);
        grid.add(new Label("startEnergy: "), 0, 2);
        grid.add(startEnergy, 1, 2);
        grid.add(new Label("moveEnergy: "), 0, 3);
        grid.add(moveEnergy, 1, 3);
        grid.add(new Label("plantEnergy: "), 0, 4);
        grid.add(plantEnergy, 1, 4);
        grid.add(new Label("jungleRatio: "), 0, 5);
        grid.add(jungleRatio, 1, 5);
        grid.add(new Label("plantsGrowthRatio: "), 0, 6);
        grid.add(plantsGrowth, 1, 6);
        grid.add(new Label("jungleMultiplier: "), 0, 7);
        grid.add(jungleMultiplier, 1, 7);
        grid.add(new Label("epochInterval [ms]: "), 0, 8);
        grid.add(epochInterval, 1, 8);
        grid.add(new Label("startingAnimals: "), 0, 9);
        grid.add(startingAnimals, 1, 9);
        grid.add(startButton, 1, 10);


//        VBox root = new VBox(5);
//        root.getChildren().addAll(grid);
        return new Scene(grid,600,400);
    }

    private void toggleSimulation(SimulationEngine simulationEngine,Button saveBTN,Button selectBTN){
        if (simulationEngine.isPaused()){
            selectBTN.setDisable(true);
            saveBTN.setDisable(true);
            simulationEngine.resume();
        }
        else{
            selectBTN.setDisable(false);
            saveBTN.setDisable(false);
            simulationEngine.pause();
        }
    }

    @Override
    public void epochConcluded(SimulationEngine simulationEngine) {
        if (simulationEngine == wrappedSimulation){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    wrappedMapGui.redraw();
                    leftMapChart.redraw();
                }
            });
        }
        else{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    solidMapGui.redraw();
                    rightMapChart.redraw();
                }
            });
        }
    }
}
