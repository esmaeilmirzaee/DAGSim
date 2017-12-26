package org.io.postach.esmaeilmirzaeee.dagsim.clustering;

import org.workflowsim.FileItem;
import org.workflowsim.Task;
import org.workflowsim.clustering.BasicClustering;

import java.util.List;

/**
 * Resource Aware clustering merges tasks based on the current available resources.
 *
 * Created by sam on 5/4/2017.
 * @author Esmaeil Mirzaee
 * @since WorkflowSim Toolkit 1.0
 * @date 4/May/2017
 */
public class ResourceAwareClustering extends BasicClustering {
    private List<Task> tasks;

    public ResourceAwareClustering() {
        super();
        tasks = getTaskList();

    }

    /**
     * The main function
     */
    public void run() {
        tasks = getTaskList();
        for (Task each : tasks) {

        }
    }
}
