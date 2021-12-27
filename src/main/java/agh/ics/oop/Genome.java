package agh.ics.oop;

import java.util.ArrayList;
import java.util.Arrays;

public class Genome {
    public int[] genes = {0,0,0,0,1,1,1,1,2,2,2,2,3,3,3,3,4,4,4,4,5,5,5,5,6,6,6,6,7,7,7,7};//32

    public Genome() {
        for (int i = 0; i < 32; i++) {
            genes[i] = Random.getInt(0,7);
        }
        Arrays.sort(genes);
    }

    public void setGenes(int[] genes){
        if (genes.length != 32){
            System.out.println("Zla liczba genow! | Genome");
            return;
        }
        this.genes = genes.clone();
    }

    //percentage of genes to keep from this genome (should be bigger than 50 I guess)
    public Genome mixGenes(Genome otherGenome, int keepPercentage){
        Genome result = new Genome();
        if (Random.getInt(0,1)==0){//left
//            System.out.println("left");
            int endIndex = (int) Math.ceil(32*(double)keepPercentage/100)-1;
            for (int i = 0; i <= endIndex; i++) {
                result.genes[i] = this.genes[i];
            }
            for (int i = endIndex+1; i < 32; i++) {
                result.genes[i] = otherGenome.genes[i];
            }
        }
        else {//right
            int startIndex = (int) Math.floor(32*(double)(100-keepPercentage)/100);
//            System.out.println("right "+startIndex);
            for (int i = 0; i <= startIndex; i++) {
                result.genes[i] = otherGenome.genes[i];
            }
            for (int i = startIndex+1; i < 32; i++) {
                result.genes[i] = this.genes[i];
            }
        }
        return result;
    }

    //randomly picks a gene
    public int pickGene(){
        return genes[Random.getInt(0,31)];
    }

    public MoveDirection pickDirection(){
        return MoveDirection.values()[pickGene()];
    }

    @Override
    public String toString() {
        String resultStr = "";
        for (Integer i :
                genes) {
            resultStr+=i;
        }
        return resultStr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genome genome = (Genome) o;
        return Arrays.equals(genes, genome.genes);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(genes);
    }

    public Genome clone(){
        int[] copiedArray = new int[32];
        System.arraycopy(genes, 0, copiedArray, 0, 32);
        Genome result = new Genome();
        result.setGenes(copiedArray);
        return result;
    }
}
