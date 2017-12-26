package org.io.postach.esmaeilmirzaeee.dagsim.clustering.utils;

/**
 * Created by esmaeil on 8/7/17.
 */
public class Fitness {
    private int numberOfError;
    private double TEC;
    private double ET;

    public Fitness(int numberOfError, double TEC, double ET) {
        this.numberOfError = numberOfError;
        this.TEC = TEC;
        this.ET = ET;
    }

    public int getNumberOfError() {
        return numberOfError;
    }

    public double getTEC() {
        return TEC;
    }

    public double getET() {
        return ET;
    }
}
