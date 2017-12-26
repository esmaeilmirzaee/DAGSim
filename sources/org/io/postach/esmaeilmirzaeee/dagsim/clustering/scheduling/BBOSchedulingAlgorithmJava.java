package org.io.postach.esmaeilmirzaeee.dagsim.clustering.scheduling;

import org.cloudbus.cloudsim.Cloudlet;
import org.io.postach.esmaeilmirzaeee.dagsim.clustering.Parser;
import org.io.postach.esmaeilmirzaeee.dagsim.clustering.utils.*;
import org.workflowsim.CondorVM;
import org.workflowsim.WorkflowSimTags;
import org.workflowsim.scheduling.BaseSchedulingAlgorithm;

import java.text.DecimalFormat;
import java.util.*;


/**
 * Created by esmaeil on 7/20/17.
 */
public class BBOSchedulingAlgorithmJava extends BaseSchedulingAlgorithm {
    private int POINT = 1000;
    private DecimalFormat df = new DecimalFormat("#.###");
    private List<Cloudlet> tasks = getCloudletList();
    private List<CondorVM> vms = getVmList();
    private List<VmTypes> vmTypes = new ArrayList<>();
    private int NUMBER_OF_TYPES = 5;
    private Map<Integer, Integer> localTasks = new HashMap();
    private Map<Integer, List<Integer>> mu = new HashMap<>();
    private int REPETITION = 100;
    private double[][][] TEC;// task execution cost [REPETITION][task id][vm type]
    private double[][][] ET; // execution time [REPETITION][task id][vm type]
    private double[][][] TT; // transfer time [REPETITION][task id][vm type]
    private double[] deadlines;
    private static int[] latestAssigned;

    @Override
    public void run() throws Exception {
        System.out.println("START");
        vms = getVmList();
        System.out.println("Number of virtual machine is: " + vms.size());
        preRun();
        CondorVM candidateVm;
        List<Cloudlet> onlineTasks = getCloudletList();
//        for (int i = 0; i < latestAssigned.length; i++) System.out.println("i: " + i + " latestAssigned " + latestAssigned[i]);
        for (Cloudlet cloudlet: onlineTasks) {
//            System.out.println("Online task length is: " + latestAssigned.length + " ID: " + cloudlet.getCloudletId());
//            System.out.println("Online task id is: " + latestAssigned[cloudlet.getCloudletId() - 1]);
            for (int j = 0; j < vms.size(); j++) {
                candidateVm = vms.get(j);
                System.out.println( "Candidate VM is: " + candidateVm.getId());
//                System.out.println("Candidate vm status is: " + candidateVm.getState());
                if (candidateVm.getState() == WorkflowSimTags.VM_STATUS_IDLE) {
                    cloudlet.setVmId(vms.get(j).getId());
                    candidateVm.setState(WorkflowSimTags.VM_STATUS_BUSY);
                    getScheduledList().add(cloudlet);
                    break;
                }
//                if (candidateVm.getState() == WorkflowSimTags.VM_STATUS_READY) {
//                    candidateVm.setState(WorkflowSimTags.VM_STATUS_IDLE);
//                }
            }
        }

        System.out.println("STOP");
    }

    /**
     * This is part of the code which is working offline. In other words, at first tasks will be evaluated then after
     * running the algorithm and finding the best possible solution via Bio-geography Algorithm. At the end, tasks vmType
     * will be saved in latestAssigned so when system is online and going to assign tasks to virtual machines it will be
     * used.
     */

    private void preRun() {
        Parser parser = new Parser();
        tasks = parser.getTasks();
        System.out.println("Tasks size is: " + tasks.size());
        deadlines = new double[tasks.size()];
        latestAssigned = new int[tasks.size()];
        TEC = new double[REPETITION][tasks.size()][NUMBER_OF_TYPES];
        ET = new double[REPETITION][tasks.size()][NUMBER_OF_TYPES];
        TT = new double[REPETITION][tasks.size()][NUMBER_OF_TYPES];
        setVmTypes(5, 1000, 512, 1000, 12, 2);
        init();
        deadline();
        for (int i = 0; i < REPETITION; i++) {
            mu.put(i, calculateMu());
            calculation(i);
            mutation();
        }
    }

    private void setVmTypes(int numberOfTypes, double baseCpu, double baseRam, double baseBw, double baseCost, int alpha) {
        int first = 1;
        for (int i = 0; i < numberOfTypes; i++) {
            vmTypes.add(new VmTypes(first * baseCpu, first * baseRam, first * baseBw, first * baseCost));
//            System.out.println(vmTypes.get(i).getCpu());
//            System.out.println(vmTypes.get(i).getRam());
//            System.out.println(vmTypes.get(i).getBw());
//            System.out.println(vmTypes.get(i).getCost());
//            System.out.println("--------------------");
            first *= alpha;
        }
    }

    private void init() {
        int x = tasks.size() / NUMBER_OF_TYPES;
        for (int i = 1; i < tasks.size(); i++) {
            if (i == x) ++x;
            localTasks.put(i, x);
        }
    }

    private void deadline() {
        Random random = new Random(NUMBER_OF_TYPES);
        for (int i = 0; i < tasks.size(); i++) {
            deadlines[i] = tasks.get(i).getCloudletLength() / vmTypes.get(random.nextInt(5)).getCpu();
        }
    }

    private List<Integer> calculateMu() {
        List<Integer> mus = new ArrayList<>();
        int[] vmTypeSize = new int[NUMBER_OF_TYPES];
        for (int i = 0; i < tasks.size(); i++) {
            vmTypeSize[latestAssigned[i]] += 1;
        }
        for (int i = 0; i < tasks.size(); i++) {
            mus.add((vmTypeSize[latestAssigned[i]] + 1 - i) + (tasks.size() + 1));
        }
        return mus;
    }

    private void calculation(int repetition) {
        boolean minusOne = false;
        for (int taskId = 0; taskId < tasks.size(); taskId++) {
            double minTEC = Double.MAX_VALUE;
            for (int vmType = 0; vmType < vmTypes.size(); vmType++) {
                ET[repetition][taskId][vmType] = tasks.get(taskId).getCloudletLength() / vmTypes.get(vmType).getCpu() * 1.2;
                TEC[repetition][taskId][vmType] = Double.parseDouble(df.format(ET[repetition][taskId][vmType])) * vmTypes.get(vmType).getCost();
                if (TEC[repetition][taskId][vmType] <= minTEC) {
                    minTEC = TEC[repetition][taskId][vmType];
//                    System.out.println("Execution time is: " + ET[repetition][taskId][vmType] +
//                    " and deadline is: " + deadlines[taskId]);
                    if (ET[repetition][taskId][vmType] <= deadlines[taskId]) {
                        latestAssigned[taskId] = vmType;
//                        System.out.println("REPETITION is: " + repetition);
//                        System.out.println("taskId is: " + taskId);
//                        System.out.println("vmType is: " + vmType);
                        break;
                    }
                }
                TT[repetition][taskId][vmType] = (tasks.get(taskId).getCloudletOutputSize() * 10.0 / vmTypes.get(vmType).getBw()) * 1.00;
            }
        }
    }

    private void mutation() {
        Random random = new Random();
        int firstMagicNumber = (int) (Math.random() * (tasks.size() - 0) + 0);
        int secondMagicNumber = (int) (Math.random() * (tasks.size() - 0) + 0);
        while (firstMagicNumber == secondMagicNumber) secondMagicNumber = (int) (Math.random() * (tasks.size() - 0) + 0);
        System.out.println("firstMagicNumber: " + firstMagicNumber);
        System.out.println("secondMagicNumber: " + secondMagicNumber);

        if (latestAssigned[firstMagicNumber] <= 0) latestAssigned[firstMagicNumber] = 0;
        if (latestAssigned[secondMagicNumber] <= 0) latestAssigned[secondMagicNumber] = 0;
        int temp = latestAssigned[firstMagicNumber];
        latestAssigned[firstMagicNumber] = latestAssigned[secondMagicNumber];
        latestAssigned[secondMagicNumber] = temp;
    }
}
