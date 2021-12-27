package agh.ics.oop;

import java.util.ArrayList;

public class SimulationData {
    public static int width = 7;
    public static int height = 5;
    public static int startEnergy = 102;
    public static int moveEnergy = 3;
    public static int plantEnergy = 100;
    public static int jungleRatio = 25;
    public static int plantsGrowth = 3;
    public static int jungleMultiplier = 10;
    public static int epochInterval = 300;
    public static int startingAnimals = 3;

    public static boolean isWrappedMagical = false;
    public static boolean isSolidMagical = false;

    public static void setInitialValues(ArrayList<Integer> initialValues){
        width = initialValues.get(0);
        height = initialValues.get(1);
        startEnergy = initialValues.get(2);
        moveEnergy = initialValues.get(3);
        plantEnergy = initialValues.get(4);
        jungleRatio = initialValues.get(5);
        plantsGrowth = initialValues.get(6);
        jungleMultiplier = initialValues.get(7);
        epochInterval = initialValues.get(8);
        startingAnimals = initialValues.get(9);
    }
}
