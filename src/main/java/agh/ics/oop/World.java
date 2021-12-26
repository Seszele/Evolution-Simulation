package agh.ics.oop;

import agh.ics.oop.gui.App;
import javafx.application.Application;

import javax.lang.model.util.SimpleAnnotationValueVisitor6;
import java.util.*;

public class World {
    public static void main(String[] args){
        System.out.println("================");
        Genome g1 = new Genome();
        Genome g2 = new Genome();
        Genome g3 = new Genome();

        int[] tmp ={0,0,0,0,0,0,0,0,0,0,0,0,0,3,3,3,4,4,4,4,5,5,5,5,6,6,6,6,7,7,7,7};
        g1.setGenes(tmp);
        g3.setGenes(tmp);
        ArrayList<Genome> array = new ArrayList<>();
        array.add(g1);
        array.add(g2);
        array.add(g3);
//
//        List asList = Arrays.asList(array);
        Set<Genome> mySet = new HashSet<Genome>(array);
////
        for(Genome s: mySet){
            System.out.println(s + " " + Collections.frequency(array,s));
        }
//        System.out.println(Collections.frequency(array,g1));



//        Application.launch(App.class, args);
        Application.launch(App.class);
    }

}
