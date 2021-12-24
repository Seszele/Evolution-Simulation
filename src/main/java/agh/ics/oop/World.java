package agh.ics.oop;

import javax.lang.model.util.SimpleAnnotationValueVisitor6;

public class World {
    public static void main(String[] args){
        SimulationEngine engine = new SimulationEngine();
        System.out.println("================");
        Genome g1 = new Genome();
        Genome g2 = new Genome();
//        int[] tmp = {9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9,9};
        int[] tmp ={0,0,0,0,0,0,0,0,0,0,0,0,0,3,3,3,4,4,4,4,5,5,5,5,6,6,6,6,7,7,7,7};
        g1.setGenes(tmp);
        Genome g3 = g1.mixGenes(g2,71);
        System.out.println(g3);
        System.out.println((double)50/60);
        System.out.println(Random.getOrientation());
        System.out.println(Random.getOrientation());
        System.out.println(Random.getOrientation());
        System.out.println(Random.getOrientation());
        System.out.println(SimulationData.hmm());


//        Application.launch(App.class, args);
    }

}
