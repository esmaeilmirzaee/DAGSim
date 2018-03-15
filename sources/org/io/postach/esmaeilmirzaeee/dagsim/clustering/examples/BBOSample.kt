package org.io.postach.esmaeilmirzaeee.dagsim.clustering.examples

import org.cloudbus.cloudsim.*
import org.cloudbus.cloudsim.core.CloudSim
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple
import org.io.postach.esmaeilmirzaeee.dagsim.clustering.Parser
import org.workflowsim.CondorVM
import org.workflowsim.Job
import org.workflowsim.WorkflowDatacenter
import org.workflowsim.WorkflowPlanner
import org.workflowsim.utils.ClusteringParameters
import org.workflowsim.utils.OverheadParameters
import org.workflowsim.utils.Parameters
import org.workflowsim.utils.ReplicaCatalog
import java.io.File
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

    fun main(args: Array<String>) {
        try {
            // First step: initialise the WorkflowSim package.
            /**
             * If enough amount of Hosts' resource is not available, the number of
             * VMs won't be the exact as specified as follows. So take care!
             */
            val vmNum = 20 // Numbrt of virtual machines
            /**
             * Please specify the path to your physical storage where you stored
             * the xml file of a workflow.
             */
            // Uploading daxpath
            val daxPath = "/home/samuel/Downloads/Montage_1000.xml"
            val daxFile = File(daxPath)
            // This is necessary to ensure loading file is safe and won't ended up in disaster
            if (!daxFile.exists()) {
                Log.printLine("Error: Please specify a valid daxPath to your workflow xml file!")
                return
            }
            // Inputing data into the system
            val parser = Parser()
            parser.setPath(daxPath)
            /**
             * Setting scheduling algorithm's variable and also specifying the requried/desired algorithm to schedule
             * virtual machines and tasks.
             */
            val schedulingMethod = Parameters.SchedulingAlgorithm.BBO
            val planningMethod = Parameters.PlanningAlgorithm.INVALID
            val fileSystem = ReplicaCatalog.FileSystem.SHARED

            /**
             * Because we did not consider any overhead and clustering in our
             * algorithm. Therefore, we did not add overhead parameters and
             * clustering methods during our implementation.
             */
            val overheadParameters = OverheadParameters(0, null, null, null, null, 0.0)
            val clusteringMethod = ClusteringParameters.ClusteringMethod.NONE
            val clusteringParameters = ClusteringParameters(vmNum, 1000, clusteringMethod, null)

            /**
             * Initialising static parameters
             * Clousim parameters
             */
            Parameters.init(vmNum, daxPath, null,
                    null, overheadParameters, clusteringParameters, schedulingMethod,
                    planningMethod,null, 0)
            ReplicaCatalog.init(fileSystem)

            // Before creating any entities.
            val numUser = 1 // number of users
            val calendar = Calendar.getInstance()
            val traceFlag = false // means trace events

            // Initialising CloudSim trace
            CloudSim.init(numUser, calendar, traceFlag)
            val datacentreArizona = createDatacentre("Arizona-DC") // creating a datacentre

            /**
             * Create a WorkflowPlanner with one schedulers.
             */
            val workflowPlanner = WorkflowPlanner("FirstPlanner", 1)

            /**
             * Creating a Workflow engine.
             */
            val workflowEngine = workflowPlanner.workflowEngine

            /**
             * Creating a list of VMs. The UserID of a VM is basically the id of
             * the scheduler that controls this vm.
             */
            val vmList = createVM(workflowEngine.getSchedulerId(0), Parameters.getVmNum())

            /**
             * Submitting this list of VMs to the WorkflowEngine.
             */
            workflowEngine.submitVmList(vmList, 0)

            /**
             * Binding the data centres with the scheduler.
             */
            workflowEngine.bindSchedulerDatacenter(datacentreArizona.id, 0) // setting virtual machines to the datacentre
            CloudSim.startSimulation() // starting the simulation
            val outputList = workflowEngine.getJobsReceivedList<Job>() // tracing the tasks
            CloudSim.stopSimulation() // stopping simulation
            printJobList(outputList) // printing the results

        } catch (exception: Exception) {
            println("What the heck!! There is something wrong in this method " +
                " I mean in main method/function in BBOSample class."
            )
            exception.printStackTrace()
        }
    }

    fun printJobList(list: List<Job>): Unit {
        val indent = "      "
        Log.printLine("=========OUTPUT=========")
        Log.printLine("Job ID" + indent + "Task ID" + indent + "STATUS" + indent +
                "Data Centre ID" + indent + "VM ID" + indent + indent + "Time" +
                indent + "Start Time" + indent + "Finish Time" + indent
        )
        val dft = DecimalFormat("###.##")
        list.forEach { job ->
            Log.print(indent + job.cloudletId + indent + indent)
            if (job.classType == Parameters.ClassType.STAGE_IN.value) Log.print("Stage-in")
            job.taskList.forEach { task ->
                Log.print(task.cloudletId.toString() + ", ")
            }
            Log.print(indent)

            if (job.cloudletStatus == Cloudlet.SUCCESS) {
                Log.print("SUCCESS")
                Log.printLine(indent + indent + job.resourceId + indent + indent + indent +
                    job.vmId + indent + indent + indent + dft.format(job.actualCPUTime) + indent +
                        indent + indent + dft.format(job.execStartTime) + indent + indent + indent +
                        indent + indent + dft.format(job.finishTime) + indent + indent + job.depth
                )
            } else if (job.cloudletStatus == Cloudlet.FAILED) {
                Log.print("FAILED")
                Log.printLine(indent + indent + job.resourceId + indent + indent + indent +
                    job.vmId + indent + indent + indent + dft.format(job.actualCPUTime) + indent +
                        indent + dft.format(job.execStartTime) + indent + indent + indent +
                        dft.format(job.finishTime) + indent + indent + indent + job.depth
                )
            }
        }
    }

    // تابع تعریف ماشین مجازی
    fun createVM(userId: Int, vms: Int): List<CondorVM> {
        // Creates a container to store VMs. This list is passed to the broker later
        var list = LinkedList<CondorVM>()

        // VM Parameters
        // Virtual machines specification
        val size = 10000L // Hard-disk's size
        val ram  = 512 // ram's size
        val mips = 1000 // CPU
        val bw = 1000L // bandwidth
        val pesNumber = 1 // Core
        val vmm = "Xen" // VMM

        // create VMs
        val vm = arrayOfNulls<CondorVM>(vms)
        for (i in 0 until vms step 1) {
            val ratio = 1.0
            vm[i] = CondorVM(i, userId, mips*ratio, pesNumber, ram, bw, size, vmm, CloudletSchedulerSpaceShared())
            list.add(vm[i]!!)
        }
        return list
    }

    // Creating datacentre
    fun createDatacentre(name: String): WorkflowDatacenter {
        // Herea the steps needed to create a PowerDatecentre
        // 1. we need to create a list to store one or more Machines
        val hostList = ArrayList<Host>()

        // 2. A machine contains one or more Physical Elements (PEs) or CPUs/Cores.
        // Therefore, should create a list to store these PEs before creating a machine.
        // physical machine's id
        var hostId = 0
        for (i in 1..20 step 1) {
            var peList = ArrayList<Pe>()
            val mips = 2000.0
            // 3. Create PEs and add these into the list.
            // for a quad-core machine, a list of 4 PEs is required.
            peList.add(Pe(0, PeProvisionerSimple(mips)))
            peList.add(Pe(1, PeProvisionerSimple(mips)))
            var ram = 2048 // host memory (MB)
            var storage = 1000000L
            var bw = 10000L // specifying bandwidth
            hostList.add(Host(hostId, RamProvisionerSimple(ram), BwProvisionerSimple(bw), storage, peList, VmSchedulerTimeShared(peList)))
            hostId++
        }
        // 4. Create a DatacentreCharacteristics object that stores the  properties
        // of a data centre: architecture, OS, list of machines, allocation policy:
        // time- or space-shared, time zone and its price (GE/PE time unit).
        val arch = "x86"  // CPU's type
        val os = "Linux"  // Operating system
        val vmm = "Xen"  // VMM
        val time_zone = 5 // Time Zone
        val cost = 3.0   // Cost
        val costPerMem = 0.05 // Memory's cost
        val costPerStorage = 0.1 // Storage's cost
        val costPerBW = 0.1 // Bandwidth's cost
        val storageList = LinkedList<Storage>()
        var datacentre: WorkflowDatacenter? = null
        var characteristics = DatacenterCharacteristics(arch, os, vmm, hostList, time_zone, cost,
                costPerMem, costPerStorage, costPerBW)

        // 5. Finally, we need to create a store object
        // Bandwidth within data centre is in MB/s.
        val maxTransferRate = 15

        try {
            // Here we set the bandwidth to be 15MB/s
            var s1 = HarddriveStorage(name, 1e12)
            s1.setMaxTransferRate(maxTransferRate)
            storageList.add(s1)
            datacentre = WorkflowDatacenter(name, characteristics, VmAllocationPolicySimple(hostList), storageList, 0.0)

        } catch (exception: Exception) {
            println("Creating data centre function has ended up nowhere!")
            exception.printStackTrace()
        }

        return datacentre!!
    }
