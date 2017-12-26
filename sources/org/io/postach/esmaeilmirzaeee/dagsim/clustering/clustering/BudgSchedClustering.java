package org.io.postach.esmaeilmirzaeee.dagsim.clustering.clustering;

import org.workflowsim.Task;
import org.workflowsim.clustering.BasicClustering;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by esmaeil on 6/10/17.
 */
public class BudgSchedClustering extends BasicClustering {
    public void run() {
        List<Task> tasks = getTaskList();
        List<Task> cluster = new ArrayList<>();
        int curLevel = 1;
        for (Task task : tasks) {
            if (curLevel != task.getDepth()) {
                curLevel = task.getDepth();
                renewClusters(cluster);
                cluster = new ArrayList<>();
            }
            cluster.add(task);
        }
        updateDependencies();
        addClustDelay();
    }

    private void renewClusters (List<Task> cluster) {
        addTasks2Job(cluster);
    }

}
