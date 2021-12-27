package agh.ics.oop.gui;

import agh.ics.oop.Animal;
import agh.ics.oop.SimulationEngine;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.*;


public class AnimalFollower {
    private VBox vBox = new VBox(2);
    private Label genomeLabel;
    private Label childLabel;
    private Label descendantLabel;
    private Label diedLabel;
    private SimulationEngine simulationEngine;
    private Animal animal;
    private ArrayList<Animal> children = new ArrayList<>();
    private boolean died = false;

    public AnimalFollower(SimulationEngine simulationEngine) {
        this.simulationEngine = simulationEngine;
        genomeLabel = new Label("Selected animal: "+getGenome());
        childLabel = new Label("Number of children: "+getChildren());
        descendantLabel = new Label("Number of descendants: "+getDescendants());
        diedLabel = new Label("Died at: "+getDeath());
        vBox.getChildren().addAll(genomeLabel,childLabel,descendantLabel,diedLabel);
    }

    private String getDeath() {
        if (animal == null){
            return "not selected";
        }
        if (!animal.isAlive()){
            //ta ktora jest teraz - fayslived
            died = true;
            return String.valueOf(simulationEngine.getEpochCount());
        }
        return "still alive";
    }

    private String getDescendants() {
        if (animal == null){
            return "not selected";
        }
        int result = 0;
        Set<Animal> visited = new HashSet<>();
        for (Animal child : children) {
            result += bfs(child,visited);
        }
//        return String.valueOf(bfs(animal)-1);
        return String.valueOf(result);
    }

    public int bfs(Animal startNode,Set<Animal> visited) {
        Queue<Animal> queue = new LinkedList<>();
//        Set<Animal> visited = new HashSet<>();

        queue.add(startNode);
        visited.add(startNode);

        while (!queue.isEmpty()) {
            Animal currentNode = queue.remove();

            for (Animal a : currentNode.getChildList()) {
                if (!visited.contains(a)) {
                    queue.add(a);
                    visited.add(a);
                }
            }
        }
        return visited.size();
    }

    private String getChildren() {
        if (animal == null){
            return "not selected";
        }
        return String.valueOf(children.size());
    }

    private String getGenome() {
        if (animal == null){
            return "not selected";
        }
        return animal.getGenome().toString();
    }

    public VBox getGUI(){
        return vBox;
    }
    public void follow(Animal animal){
        if (this.animal!=null){
            this.animal.removeFollower();
        }
        died = false;
        children = new ArrayList<>();
        this.animal = animal;
        this.animal.setFollower(this);
        redraw();
    }
    public void redraw(){
        genomeLabel.setText(("Selected animal: "+getGenome()));
        childLabel.setText(("Number of children: "+getChildren()));
        descendantLabel.setText( ("Number of descendants: "+getDescendants()));
        if (!died){
            diedLabel.setText(("Died at: "+getDeath()));
        }
    }

    public void noticeAChild(Animal child){
        children.add(child);
    }

}
