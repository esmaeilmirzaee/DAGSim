package org.io.postach.esmaeilmirzaeee.dagsim.clustering.utils;

public class VmTypes {
    private double cpu = 0.0;
    private double ram = 0.0;
    private double bw = 0.0;
    private double cost = 0.0;

    public VmTypes(double cpu, double ram, double bw, double cost) {
        this.cpu = cpu;
        this.ram = ram;
        this.bw = bw;
        this.cost = cost;
    }

    public double getCpu() {
        return cpu;
    }

    public double getRam() {
        return ram;
    }

    public double getBw() {
        return bw;
    }

    public double getCost() {
        return cost;
    }
}
