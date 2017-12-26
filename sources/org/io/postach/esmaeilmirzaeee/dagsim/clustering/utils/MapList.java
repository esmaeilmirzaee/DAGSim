package org.io.postach.esmaeilmirzaeee.dagsim.clustering.utils;

import java.util.ArrayList;
import java.util.List;

public class MapList {
    int id = 0;
    double computeResource = 0.0D;
    double storageResource = 0.0D;
    List<Integer> tasksId = new ArrayList<>();

    public MapList(int id, double computeResource, double storageResource, List<Integer> tasksId) {
        this.id = id;
        this.computeResource = computeResource;
        this.storageResource = storageResource;
        this.tasksId = tasksId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getComputeResource() {
        return computeResource;
    }

    public void setComputeResource(double computeResource) {
        this.computeResource = computeResource;
    }

    public double getStorageResource() {
        return storageResource;
    }

    public void setStorageResource(double storageResource) {
        this.storageResource = storageResource;
    }

    public List<Integer> getTasksId() {
        return tasksId;
    }

    public void setTasksId(List<Integer> tasksId) {
        this.tasksId = tasksId;
    }
}
