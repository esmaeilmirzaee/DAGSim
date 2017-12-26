package org.io.postach.esmaeilmirzaeee.dagsim.clustering.utils;

import org.workflowsim.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by esmaeil on 8/1/17.
 */
public class Data {
    static List<Task> tasks;

    public static List<Task> getTasks() {
        return tasks;
    }

    public static void setTasks(List<Task> task) {
        tasks = new ArrayList<>(task);
    }
}
