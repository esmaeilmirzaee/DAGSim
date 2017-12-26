package org.io.postach.esmaeilmirzaeee.dagsim.clustering.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by esmaeil on 8/7/17.
 */
public class Result {
    private int taskNumber;  // number of tasks
    private int iteration;  // number of iteration
    private double cost;    // cost
    private double ET;      // execution time
    private int vmType;     // type of vm

    public Result(int iteration, int taskNumber, double cost, double ET, int vmType) {
        this.taskNumber = taskNumber;
        this.iteration = iteration;
        this.cost = cost;
        this.ET = ET;
        this.vmType = vmType;
    }

    public void sampleTest(String[] args) {
        List<Result> results = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            for (int t = 0; t < 20; t++) {
                Result result = new Result(i, t, 201.123, 0.02, 1);
                results.add(result);
            }
        }
        results.get(0).getIteration();
    }

    public int getTaskNumber() {
        return taskNumber;
    }

    public void setTaskNumber(int taskNumber) {
        this.taskNumber = taskNumber;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getET() {
        return ET;
    }

    public void setET(double ET) {
        this.ET = ET;
    }

    public int getVmType() {
        return vmType;
    }

    public void setVmType(int vmType) {
        this.vmType = vmType;
    }

    public int getIteration() {
        return iteration;
    }
}
