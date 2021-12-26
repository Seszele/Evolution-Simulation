package agh.ics.oop.gui;

import agh.ics.oop.*;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;

public class MapGui {
    private Map map;
    private GridPane grid;

    public MapGui(Map map) {
        this.map = map;
        grid = setUpGrid();
    }//TODO na posdtawie wielkosci mapy wielkosc gui

    private GridPane setUpGrid(){
        GridPane grid = new GridPane();
        for (int y = 0; y <= map.getDimension().y; y++) {
            for (int x = 0; x <= map.getDimension().x; x++) {
//                Label label = new Label(x+","+y);
                Button button = new Button();
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

    private Button getButtonAt(int x, int y) {
        for (Node node : grid.getChildren()) {
            if (GridPane.getColumnIndex(node) == x && GridPane.getRowIndex(node) == y) {
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
//                String backgroundColour = "ffffff";
                Color backgroundColour = Color.HONEYDEW;
                String borderColour = "000000";
                if(mapObject instanceof Animal){
                    backgroundColour = Color.YELLOW;
                    backgroundColour = backgroundColour.interpolate(Color.BROWN, (double)SimulationData.moveEnergy/((Animal) mapObject).getEnergy());
                }
                else if(mapObject instanceof Plant){
//                    backgroundColour = "00ff00";
                    backgroundColour = Color.LIME;
                }

                if (map.isJungle(new Vector2d(x,y))){
                    borderColour = "FFA500";
                }
                button.setStyle(String.format("-fx-background-color: rgb(%s,%s,%s);-fx-border-color: #%s; -fx-border-width: 1px;", (int)(backgroundColour.getRed()*255),(int)(backgroundColour.getGreen()*255),(int)(backgroundColour.getBlue()*255), borderColour));
            }
        }
    }

    public Node getRoot(){
        return grid;
    }
}
