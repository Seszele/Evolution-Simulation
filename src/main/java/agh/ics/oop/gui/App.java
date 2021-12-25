package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application implements IEpochObserver {
    private MapGui wrappedMapGui;
    private SimulationEngine wrappedSimulation;
    private Thread wrappedSimThread;

    private MapGui solidMapGui;
    private SimulationEngine solidSimulation;
    private Thread solidSimThread;

    @Override
    public void init() throws Exception {
        super.init();
        System.out.println("init");
        wrappedSimulation = new SimulationEngine( new Map(SimulationData.width,SimulationData.height,true),this);
        wrappedMapGui = new MapGui(wrappedSimulation.getMap());
        wrappedSimThread =  new Thread((Runnable) wrappedSimulation);

        solidSimulation = new SimulationEngine(new Map(SimulationData.width,SimulationData.height,false),this);
        solidMapGui = new MapGui(solidSimulation.getMap());
        solidSimThread =  new Thread((Runnable) solidSimulation);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Button leftButton = new Button("Toggle pause");
        leftButton.setOnAction(event -> toggleSimulation(wrappedSimulation));
        Button rightButton = new Button("Toggle pause");
        rightButton.setOnAction(event -> toggleSimulation(solidSimulation));

        VBox leftMap = new VBox(5);
        leftMap.getChildren().addAll(wrappedMapGui.getRoot(),leftButton);
        VBox rightMap = new VBox(5);
        rightMap.getChildren().addAll(solidMapGui.getRoot(),rightButton);

        HBox root = new HBox(50);
        root.getChildren().addAll(leftMap,rightMap);

        Scene scene = new Scene(root,600,400);

        //test
        wrappedSimThread.start();
        solidSimThread.start();


        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void toggleSimulation(SimulationEngine simulationEngine){
        System.out.println("toggle!");
        if (simulationEngine.isPaused()){
            simulationEngine.resume();
        }
        else{
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
                }
            });
        }
        else{
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    solidMapGui.redraw();
                }
            });
        }
    }
}
