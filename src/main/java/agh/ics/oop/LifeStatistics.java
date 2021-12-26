package agh.ics.oop;

public class LifeStatistics {
    public int getDaysLived() {
        return daysLived;
    }

    public int getDied() {
        return died;
    }

    private int daysLived;
    private int died;

    public LifeStatistics() {
        this.daysLived = 0;
        this.died = 0;
    }

    public LifeStatistics(int daysLived, int died) {
        this.daysLived = daysLived;
        this.died = died;
    }

    public Double getAverage() {
        if (died == 0){
            return 0d;
        }
        return (double)daysLived/died;
    }
    public LifeStatistics add(LifeStatistics other){
        return new LifeStatistics(this.daysLived+other.daysLived,this.died+ other.died);
    }
}
