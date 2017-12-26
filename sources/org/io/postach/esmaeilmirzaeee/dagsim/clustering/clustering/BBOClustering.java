package org.io.postach.esmaeilmirzaeee.dagsim.clustering.clustering;

import org.io.postach.esmaeilmirzaeee.dagsim.clustering.utils.EvalDataModel;
import org.io.postach.esmaeilmirzaeee.dagsim.clustering.utils.Linspace;
import org.io.postach.esmaeilmirzaeee.dagsim.clustering.utils.MapList;
import org.workflowsim.FileItem;
import org.workflowsim.Task;
import org.workflowsim.clustering.BasicClustering;
import org.workflowsim.scheduling.StaticSchedulingAlgorithm;

import java.util.*;

/**
 * Created by esmaeil on 7/13/17.
 */
public class BBOClustering extends BasicClustering {
    private final int maxIt;
    private final int nPop;
    private final double keepRate;
    private final int nKeep;
    private final int nNew;
    private final double[] mu;
    private final double[] lambda;
    private final float alpha;
    private final float pMutation;
    private List<Integer> taskId;
    private List<MapList> myData;
    private Map<List<Integer>, Double> resource;
    private double computeResource = 0.0D;
    private double storageResource = 0.0D;
    private Map<Integer, List<Integer>> outerArray;

    private List<Task> tasks;
    private List<Task> cluster;


    /**
     * New data model
     */
    EvalDataModel evalDataModel = new EvalDataModel(1, 1, 10.0, 10.1, 154);
    List<EvalDataModel> evalDataModelList = new ArrayList<>();
    Map<Set<Integer>, List<EvalDataModel>> calc = new HashMap<>();


    public BBOClustering() {
        super();
        maxIt = 100;            // Number of iteration
        // This is number of population which it should be equal to
        // the number of virtual machines. Now we don't have access
        // to the number of vms and it should be accessible
        nPop = 20;
        keepRate = 0.2;      // rate of old population should be renewed
        nKeep = (int) (keepRate * nPop); // prevent to trap in local minimum
        nNew = nPop - nKeep;    // Number of new habitats
        /*
        migration rates
         */
        mu = Linspace.linspace(1, 0, nPop);      // Emigration rates
        lambda = Linspace.linspace(0, 1, nPop);  // Immigration rates, 1.0 - mu
        alpha = 0.9F;         // It is used for immigration calculation
        pMutation = 0.1F;     // mutation percentage

    }

    public void run() {
        tasks = getTaskList();
        initialisation();

        System.out.print("");
        updateDependencies();
        addClustDelay();
    }

    private void initialisation() {
        int count = 0;
        int preDepth = tasks.get(0).getDepth();
        taskId = new ArrayList<>();
        outerArray = new HashMap<>();
        if (cluster == null) cluster = new ArrayList<>();
        myData = new ArrayList<>();
        MapList data;
        for (Task task : tasks) {
            ++count;
            if (preDepth == task.getDepth()) {
                cluster.add(task);
                taskId.add(task.getCloudletId());
                List<FileItem> files = task.getFileList();
                for (FileItem file : files) {
                    storageResource += file.getSize();
                }
                computeResource += task.getCloudletLength();
            } else {
                outerArray.put(count, taskId);
//                addTasks2Job(cluster);
                data = new MapList(task.getCloudletId(),computeResource, storageResource, taskId);
                taskId = new ArrayList<>();
//                cluster = new ArrayList<>();
                preDepth = task.getDepth();
//                cluster.add(task);
                storageResource = 0.0;
                computeResource = 0.0;
                taskId.add(task.getCloudletId());
            }
            if (count == tasks.size()) {
            }

        }
        System.out.println(outerArray.size());
        int m = 0;
        Set<Integer> arrayIndex = outerArray.keySet();
        int index[] = new int[arrayIndex.size()];
        for (int z : arrayIndex) {
            index[m] = z;
            ++m;
        }
        int calc = 0;
        for (int i = 0; i < outerArray.size(); i++) {
            List<Integer> tempArray = outerArray.get(index[i]);
            System.out.println(tempArray.size());
            calc += tempArray.size();
        }
        problemDefinition();


    }

    // Problem definition - check how much resource we do have and how much is used
    private void problemDefinition() {
        final int imageSize = 10000;
        final int ramAndMips = 512 * 1000 * 1;
        double computationResource = ramAndMips;
        double storageResource = imageSize;
        for (int i = 0; i < 20; i++) {

        }

        StaticSchedulingAlgorithm sca = new StaticSchedulingAlgorithm();
        List vms = sca.getVmList();
        System.out.println("vms: " + vms);

    }

    /*
    
     */
    public void mutation(String[] args) {
        int minimum = 0, maximum = tasks.size();
        double rand;
        int count = 0;
        for (int k = 0; k <= 50; k++) {
            for (int parameterIndex = 1; parameterIndex < 20; parameterIndex++) {
                ++count;
                rand = minimum + (Math.random() * maximum);
                System.out.println(count + ": " + ((int) rand));
            }
        }
    }

    public static void main(String[] args) {
        BBOClustering bbo = new BBOClustering();
        bbo.initialisation();
    }
}
