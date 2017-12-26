package org.io.postach.esmaeilmirzaeee.dagsim.clustering.utils;

/**
 * Created by esmaeil on 8/1/17.
 */
public class EvalDataModel {
    private int taskId;
    private int vmType;
    private double executionTime;
    private double transferTime;
    private double cost;

    public EvalDataModel(int taskId, int vmType, double executionTime, double transferTime, double cost) {
        this.taskId = taskId;
        this.vmType = vmType;
        this.executionTime = executionTime;
        this.transferTime = transferTime;
        this.cost = cost;
    }

    void calcCost() {
        setCost(getExecutionTime() + getTransferTime());
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getVmType() {
        return vmType;
    }

    public void setVmType(int vmType) {
        this.vmType = vmType;
    }

    public double getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(double executionTime) {
        this.executionTime = executionTime;
    }

    public double getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(double transferTime) {
        this.transferTime = transferTime;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
