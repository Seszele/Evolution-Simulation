package agh.ics.oop.gui;

import agh.ics.oop.Animal;
import agh.ics.oop.Map;
import agh.ics.oop.SimulationData;
import agh.ics.oop.Vector2d;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class App extends Application {
    private MapGui wrappedMapGui;
    @Override
    public void init() throws Exception {
        super.init();
        System.out.println("init");
        Map wrappedMap = new Map(SimulationData.width,SimulationData.height,true);
        Animal animal1 = new Animal(50,new Vector2d(0,0),wrappedMap);
        wrappedMapGui = new MapGui(wrappedMap);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox root = new VBox(5);
        root.getChildren().add(wrappedMapGui.getRoot());
        wrappedMapGui.redraw();

        Scene scene = new Scene(root,400,400);
        primaryStage.setScene(scene);
        primaryStage.show();
        //TODO thready, zatrzymywanie i wznawianie (DLA KAZDEJ MAPY OSOBNO)
    }
}
