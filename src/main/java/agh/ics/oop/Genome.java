package agh.ics.oop;

import java.util.Arrays;

public class Genome {
    public int[] genes = {0,0,0,0,1,1,1,1,2,2,2,2,3,3,3,3,4,4,4,4,5,5,5,5,6,6,6,6,7,7,7,7};//32

    public Genome() {
        for (int i = 0; i < 32; i++) {
            genes[i] = Random.getInt(0,7);
        }
        Arrays.sort(genes);
    }

    public void setGenes(int[] genes) throws Exception {
        if (genes.length != 32){
            throw new Exception("Wrong number of genes, expected 32");
        }
        this.genes = genes.clone();
    }

    //percentage of genes to keep from this genome
    public Genome mixGenes(Genome otherGenome, int keepPercentage){
        Genome result = new Genome();
        if (Random.getInt(0,1)==0){//left
            int endIndex = (int) Math.ceil(32*(double)keepPercentage/100)-1;
            if (endIndex + 1 >= 0) System.arraycopy(this.genes, 0, result.genes, 0, endIndex + 1);
            if (32 - (endIndex + 1) >= 0)
                System.arraycopy(otherGenome.genes, endIndex + 1, result.genes, endIndex + 1, 32 - (endIndex + 1));
        }
        else {//right
            int startIndex = (int) Math.floor(32*(double)(100-keepPercentage)/100);
            if (startIndex + 1 >= 0) System.arraycopy(otherGenome.genes, 0, result.genes, 0, startIndex + 1);
            if (32 - (startIndex + 1) >= 0)
                System.arraycopy(this.genes, startIndex + 1, result.genes, startIndex + 1, 32 - (startIndex + 1));
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
        StringBuilder resultStr = new StringBuilder();
        for (Integer i : genes) {
            resultStr.append(i);
        }
        return resultStr.toString();
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

    public Genome cloneGenome(){
        int[] copiedArray = new int[32];
        System.arraycopy(genes, 0, copiedArray, 0, 32);
        Genome result = new Genome();
        try {
            result.setGenes(copiedArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
