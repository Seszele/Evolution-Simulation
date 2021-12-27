package agh.ics.oop.gui;

import agh.ics.oop.*;
import com.sun.scenario.animation.shared.AnimationAccessor;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class MapGui {
    private SimulationEngine simulationEngine;
    private Map map;
    private GridPane grid;
    private AnimalFollower animalFollower;
    //animalfollower ktory sie tu robi, i ustawia cel na animala ktory jest klikniety
    //metoda ktora zwraca z animalfollowera ten gui
    //i w redrawr aktualizacja danych i wyswietlania w followerze

    public MapGui(SimulationEngine simulationEngine) {
        this.simulationEngine = simulationEngine;
        animalFollower = new AnimalFollower(simulationEngine);
        this.map = simulationEngine.getMap();
        grid = setUpGrid();
    }//TODO na posdtawie wielkosci mapy wielkosc gui

    private GridPane setUpGrid(){
        GridPane grid = new GridPane();
        for (int y = 0; y <= map.getDimension().y; y++) {
            for (int x = 0; x <= map.getDimension().x; x++) {
//                Label label = new Label(x+","+y);
                Button button = new Button();
                int finalX = x;
                int finalY = y;
                button.setOnAction(e->animalSelected(new Vector2d(finalX, finalY)));
                button.setMinWidth(30);
                button.setMaxWidth(30);
                button.setMinHeight(30);
                button.setMaxHeight(30);
                button.setStyle("-fx-background-color: #ffffff;-fx-border-color: #000000; -fx-border-width: 1px;");
                grid.add(button,x,map.getDimension().y-y,1,1);
            }
        }

        return grid;
    }

    private void animalSelected(Vector2d position) {
        if (map.getObjectAt(position) instanceof Animal){
            Animal selectedAnimal = (Animal) map.getObjectAt(position);
            animalFollower.follow(selectedAnimal);
        }
    }


    private Button getButtonAt(int x, int y) {
        for (Node node : grid.getChildren()) {
            if (GridPane.getColumnIndex(node) == x && GridPane.getRowIndex(node) == y) {
                return (Button)node;
            }
        }
        return null;
    }
    private Button getButtonAt(Vector2d position) {
        for (Node node : grid.getChildren()) {
            if (GridPane.getColumnIndex(node) == position.x && GridPane.getRowIndex(node) == position.y) {
                return (Button)node;
            }
        }
        return null;
    }

    public void redraw(){
        //wszystkie buttony na defaultowy kolor i bez eventow
        //pokoloruj buttony na dobry kolor zielony czarsny-szary
        //pokoloruj bordery na kolory w zaleznosci czy jest jungla czy nie
        //pamietaj zeby zamienic na: map.getDimension().y-y
        for (int y = 0; y <= map.getDimension().y; y++) {
            for (int x = 0; x <= map.getDimension().x; x++) {
                Button button = getButtonAt(x,map.getDimension().y-y);
                Object mapObject = map.getObjectAt(new Vector2d(x,y));
                Color backgroundColour = Color.HONEYDEW;
                String borderColour = "000000";
                if(mapObject instanceof Animal){
                    backgroundColour = Color.YELLOW;
                    backgroundColour = backgroundColour.interpolate(Color.BROWN, (double)SimulationData.moveEnergy/((Animal) mapObject).getEnergy());
                }
                else if(mapObject instanceof Plant){
                    backgroundColour = Color.LIME;
                }

                if (map.isJungle(new Vector2d(x,y))){
                    borderColour = "FFA500";
                }
                button.setStyle(String.format("-fx-background-color: rgb(%s,%s,%s);-fx-border-color: #%s; -fx-border-width: 1px;", (int)(backgroundColour.getRed()*255),(int)(backgroundColour.getGreen()*255),(int)(backgroundColour.getBlue()*255), borderColour));
            }
        }
        animalFollower.redraw();
    }

    public void highlightPositions(ArrayList<Vector2d> positions){
        for (Vector2d position : positions) {
            if ( getButtonAt(position) != null)
                getButtonAt(position.x,map.getDimension().y-position.y).setStyle("-fx-background-color: #0000FF;-fx-border-color: #000000; -fx-border-width: 1px;");
        }
    }

    public VBox getFollowerGUI(){
        return animalFollower.getGUI();
    }

    public Node getRoot(){
        return grid;
    }
}
