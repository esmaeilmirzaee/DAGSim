package org.io.postach.esmaeilmirzaeee.dagsim.clustering.examples;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.io.postach.esmaeilmirzaeee.dagsim.clustering.utils.SPEC;
import org.workflowsim.*;
import org.workflowsim.utils.ClusteringParameters;
import org.workflowsim.utils.OverheadParameters;
import org.workflowsim.utils.Parameters;
import org.workflowsim.utils.ReplicaCatalog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by esmaeil on 6/10/17.
 */
public class DAGSim03 {
    public static void main(String[] args) {
        try {
            // First Step: Initialising the WorkflowSim packages.
            int vmNum = 1;//number of vms;
            String daxPath = "/home/esmaeil/Dropbox/dax/Epigenomics_997.xml";
            java.io.File daxFile = new java.io.File(daxPath);
            if (!daxFile.exists()) {
                Log.printLine("Warning: Please replace daxPath with the physical path in your working environment!");
                return;
            }
            Parameters.SchedulingAlgorithm sch_method = Parameters.SchedulingAlgorithm.BBO;
            Parameters.PlanningAlgorithm pln_method = Parameters.PlanningAlgorithm.INVALID;
            ReplicaCatalog.FileSystem file_system = ReplicaCatalog.FileSystem.SHARED;
            OverheadParameters op = new OverheadParameters(0, null, null, null, null, 0);
            ClusteringParameters.ClusteringMethod method = ClusteringParameters.ClusteringMethod.NONE;
            ClusteringParameters cp = new ClusteringParameters(vmNum, 1000, method, null);
            Parameters.init(vmNum, daxPath, null,
                    null, op, cp, sch_method, pln_method,
                    null, 0);
            ReplicaCatalog.init(file_system);

            // Second Step: Initialise the CloudSim packages.
            int num_user = 1;   // number of grid users
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;  // mean trace events
            CloudSim.init(num_user, calendar, trace_flag);

            CloudSim.startSimulation();
            WorkflowDatacenter datacenter0 = createDatacenter("Datacenter_0", vmNum);
            WorkflowPlanner wfPlanner = new WorkflowPlanner("planner_0", 1);
            WorkflowEngine wfEngine = wfPlanner.getWorkflowEngine();
            List<CondorVM> vmlist0 = createVM(wfEngine.getSchedulerId(0), Parameters.getVmNum());
            wfEngine.submitVmList(vmlist0, 0);

            wfEngine.bindSchedulerDatacenter(datacenter0.getId(), 0);
            List<Job> outputList0 = wfEngine.getJobsReceivedList();
            CloudSim.stopSimulation();
            printJobList(outputList0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static void printJobList(List<Job> list) {
        String indent = "    ";
        Log.printLine();
        Log.printLine("=========== OUTPUT ==========");
        Log.printLine("Job ID" + indent + "Task ID" + indent + "STATUS" + indent
                + "Data center ID" + indent + "VM ID" + indent + indent
                + "Time" + indent + "Start Time" + indent + "Finish Time" + indent + "Depth");
        DecimalFormat dft = new DecimalFormat("###.##");
        for (Job job : list) {
            Log.print(indent + job.getCloudletId() + indent + indent);
            if (job.getClassType() == Parameters.ClassType.STAGE_IN.value) {
                Log.print(" Stage-in");
            }
            for (Task task : job.getTaskList()) {
                Log.print(task.getCloudletId() + ",");
            }
            Log.print(indent);

            if (job.getCloudletStatus() == Cloudlet.SUCCESS) {
                Log.print(" SUCCESS ");
                Log.printLine(indent + indent + job.getResourceId() + indent + indent + indent + job.getVmId()
                        + indent + indent + indent + dft.format(job.getActualCPUTime())
                        + indent + indent + dft.format(job.getExecStartTime()) + indent + indent + indent
                        + dft.format(job.getFinishTime()) + indent + indent + indent + job.getDepth());
            } else if (job.getCloudletStatus() == Cloudlet.FAILED) {
                Log.print(" FAILED ");
                Log.printLine(indent + indent + job.getResourceId() + indent + indent + indent + job.getVmId()
                        + indent + indent + indent + dft.format(job.getActualCPUTime())
                        + indent + indent + dft.format(job.getExecStartTime()) + indent + indent + indent
                        + dft.format(job.getFinishTime()) + indent + indent + indent + job.getDepth());
            }
        }
    }


    protected static List<CondorVM> createVM(int userId, int vms) {
        //Creates a container to store VMs. This list is passed to the broker later
        LinkedList<CondorVM> list = new LinkedList<>();
        //VM Parameters -- Type 1
        SPEC spec_t1 = new SPEC(10000, 512, 4400, 1000, 1);
        SPEC spec_t2 = new SPEC(10000, 512, 8800, 1000, 2);
        SPEC spec_t3 = new SPEC(10000, 512, 17600, 1000, 4);
        SPEC spec_t4 = new SPEC(10000, 512, 35200, 1000, 8);
        SPEC spec_t5 = new SPEC(10000, 512, 57200, 1000, 16);
        SPEC spec_t6 = new SPEC(10000, 512, 114400, 1000, 32);

        String vmm = "Xen"; //VMM name

        //create VMs
        CondorVM[] vm = new CondorVM[7];
        for (int i = 0; i < 7; i++) {
            double ratio = 1.0;
            switch (i) {
                case 0:
                    vm[i] = new CondorVM(i, userId, spec_t1.getMips() * ratio, spec_t1.getPesNumber(), spec_t1.getRam(), spec_t1.getBw(), spec_t1.getSize(), vmm, new CloudletSchedulerSpaceShared());
                    list.add(vm[i]);
                    break;
                case 1:
                    vm[i] = new CondorVM(i, userId, spec_t2.getMips() * ratio, spec_t2.getPesNumber(), spec_t2.getRam(), spec_t2.getBw(), spec_t2.getSize(), vmm, new CloudletSchedulerSpaceShared());
                    list.add(vm[i]);
                    break;
                case 2:
                    vm[i] = new CondorVM(i, userId, spec_t3.getMips() * ratio, spec_t3.getPesNumber(), spec_t3.getRam(), spec_t3.getBw(), spec_t3.getSize(), vmm, new CloudletSchedulerSpaceShared());
                    list.add(vm[i]);
                    break;
                case 3:
                    vm[i] = new CondorVM(i, userId, spec_t4.getMips() * ratio, spec_t4.getPesNumber(), spec_t4.getRam(), spec_t4.getBw(), spec_t4.getSize(), vmm, new CloudletSchedulerSpaceShared());
                    list.add(vm[i]);
                    break;
                case 4:
                    vm[i] = new CondorVM(i, userId, spec_t5.getMips() * ratio, spec_t5.getPesNumber(), spec_t5.getRam(), spec_t5.getBw(), spec_t5.getSize(), vmm, new CloudletSchedulerSpaceShared());
                    list.add(vm[i]);
                    break;
                case 5:
                    vm[i] = new CondorVM(i, userId, spec_t6.getMips() * ratio, spec_t6.getPesNumber(), spec_t6.getRam(), spec_t6.getBw(), spec_t6.getSize(), vmm, new CloudletSchedulerSpaceShared());
                    list.add(vm[i]);
                    break;
            }
        }

        return list;
    }

    protected static WorkflowDatacenter createDatacenter(String name, int vms) {

        // Here are the steps needed to create a PowerDatacenter:
        // 1. We need to create a list to store one or more
        //    Machines
        List<Host> hostList = new ArrayList<>();

        // 2. A Machine contains one or more PEs or CPUs/Cores. Therefore, should
        //    create a list to store these PEs before creating
        //    a Machine.
        System.out.print("");
        for (int i = 1; i <= vms; i++) {
            List<Pe> peList1 = new ArrayList<>();
            int mips = 228800;
            // 3. Create PEs and add these into the list.
            //for a quad-core machine, a list of 4 PEs is required:
            peList1.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
            peList1.add(new Pe(1, new PeProvisionerSimple(mips)));
            peList1.add(new Pe(2, new PeProvisionerSimple(mips)));
            peList1.add(new Pe(3, new PeProvisionerSimple(mips)));
            peList1.add(new Pe(4, new PeProvisionerSimple(mips)));
            peList1.add(new Pe(5, new PeProvisionerSimple(mips)));
            peList1.add(new Pe(6, new PeProvisionerSimple(mips)));
            peList1.add(new Pe(7, new PeProvisionerSimple(mips)));
            peList1.add(new Pe(8, new PeProvisionerSimple(mips)));
            peList1.add(new Pe(9, new PeProvisionerSimple(mips)));
            peList1.add(new Pe(10, new PeProvisionerSimple(mips)));
            peList1.add(new Pe(11, new PeProvisionerSimple(mips)));
            peList1.add(new Pe(12, new PeProvisionerSimple(mips)));
            peList1.add(new Pe(13, new PeProvisionerSimple(mips)));
            peList1.add(new Pe(14, new PeProvisionerSimple(mips)));
            peList1.add(new Pe(15, new PeProvisionerSimple(mips)));
            peList1.add(new Pe(16, new PeProvisionerSimple(mips)));
            peList1.add(new Pe(17, new PeProvisionerSimple(mips)));
            peList1.add(new Pe(18, new PeProvisionerSimple(mips)));
            peList1.add(new Pe(19, new PeProvisionerSimple(mips)));
            peList1.add(new Pe(20, new PeProvisionerSimple(mips)));
            peList1.add(new Pe(21, new PeProvisionerSimple(mips)));
            peList1.add(new Pe(22, new PeProvisionerSimple(mips)));
            peList1.add(new Pe(23, new PeProvisionerSimple(mips)));
            peList1.add(new Pe(24, new PeProvisionerSimple(mips)));
            peList1.add(new Pe(25, new PeProvisionerSimple(mips)));
            peList1.add(new Pe(26, new PeProvisionerSimple(mips)));
            peList1.add(new Pe(28, new PeProvisionerSimple(mips)));
            peList1.add(new Pe(29, new PeProvisionerSimple(mips)));
            peList1.add(new Pe(30, new PeProvisionerSimple(mips)));
            peList1.add(new Pe(31, new PeProvisionerSimple(mips)));

            System.out.print("");
            int hostId = 0;
            int ram = 20480; //host memory (MB)
            long storage = 1000000; //host storage
            int bw = 10000;
            hostList.add(
                    new Host(
                            hostId,
                            new RamProvisionerSimple(ram),
                            new BwProvisionerSimple(bw),
                            storage,
                            peList1,
                            new VmSchedulerTimeShared(peList1))); // This is our first machine
            //hostId++;
        }

        // 4. Create a DatacenterCharacteristics object that stores the
        //    properties of a data center: architecture, OS, list of
        //    Machines, allocation policy: time- or space-shared, time zone
        //    and its price (G$/Pe time unit).
        String arch = "x86";      // system architecture
        String os = "Linux";          // operating system
        String vmm = "Xen";
        double time_zone = 10.0;         // time zone this resource located
        double cost = 3.0;              // the cost of using processing in this resource
        double costPerMem = 0.05;        // the cost of using memory in this resource
        double costPerStorage = 0.1;    // the cost of using storage in this resource
        double costPerBw = 0.1;            // the cost of using bw in this resource
        LinkedList<Storage> storageList = new LinkedList<>();    //we are not adding SAN devices by now
        WorkflowDatacenter datacenter = null;

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);

        // 5. Finally, we need to create a storage object.
        /**
         * The bandwidth within a data center in MB/s.
         */
        int maxTransferRate = 15;// the number comes from the futuregrid site, you can specify your bw

        try {
            // Here we set the bandwidth to be 15MB/s
            HarddriveStorage s1 = new HarddriveStorage(name, 1e12);
            s1.setMaxTransferRate(maxTransferRate);
            storageList.add(s1);
            System.out.print("");
            datacenter = new WorkflowDatacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
        } catch (Exception e) {
            System.out.print("");
            e.printStackTrace();
        }
        return datacenter;
    }
}