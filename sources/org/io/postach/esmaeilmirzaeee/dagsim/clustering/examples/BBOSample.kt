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
            val vmNum = 20 // تعریف تعداد ماشین مجازی
            /**
             * Please specify the path to your physical storage where you stored
             * the xml file of a workflow.
             */
            // آدرس محل ذخیره سازی فایل دکس
            val daxPath = "/home/samuel/Downloads/Montage_1000.xml"
            val daxFile = File(daxPath)
            if (!daxFile.exists()) {
                // در صورت بروز مشکل در خواندن فایل
                // مثلا آدرس اشتباه با خطای زیر مواجه خواهید شد
                Log.printLine("Error: Please specify a valid daxPath to your workflow xml file!")
                return
            }
            // در این بخش فایل دکس خوانده می شود
            ///////

            val parser = Parser()
            parser.setPath(daxPath)
            /**
             *
             */
            // در این بخش پارامترهای مربوط به الگوریتم زمان بندی
            // نحوه ایجاد و فایل سیستم بین ماشین های مجازی مشخص می شود
            // مثلا برای زمان بندی جغرافیای زیستی از BBO
            // و برای استفاده از الگوریتم زمان بندی مقاوم در برابر خطا از FTBWS
            // یا برای الگوریتم راند رابین می بایستی از ROUNDROBIN
            // (انواع مختلف این الگوریتم های پیاده سازی شده قابل مشاهده می باشد)
            // استفاده شود
            val schedulingMethod = Parameters.SchedulingAlgorithm.BBO
            val planningMethod = Parameters.PlanningAlgorithm.INVALID
            val fileSystem = ReplicaCatalog.FileSystem.SHARED

            /**
             * Because we did not consider any overhead and clustering in our
             * algorithm. Therefore, we did not add overhead parameters and
             * clustering methods during our implementation.
             */
            // پارامترهای مربوط به نحوه خوشه بندی ریزکارها در این بخش مشخص می شود
            // با توجه به الگوریتم های توسعه داده شده می بایستی مقادیر تغییر نماید
            // یا به عبارت دیگر در صورتی که قصد توسعه کاری مبتنی بر
            // خوشه بندی صورت پذیرد می بایستی
            // این بخش از کد تغییر یابد
            val overheadParameters = OverheadParameters(0, null, null, null, null, 0.0)
            val clusteringMethod = ClusteringParameters.ClusteringMethod.NONE
            val clusteringParameters = ClusteringParameters(vmNum, 1000, clusteringMethod, null)

            /**
             * Initialising static parameters
             */
            // پارامترهای اولیه جهت آماده سازی موتور وورکفلو سیم به آن ارسال می شود
            Parameters.init(vmNum, daxPath, null,
                    null, overheadParameters, clusteringParameters, schedulingMethod,
                    planningMethod,null, 0)
            ReplicaCatalog.init(fileSystem)

            // Befire creating any entities.
            // مشخصات کاربران در این بخش مشخص می شود
            // زمان سیستم، ردیابی رویدادها
            val numUser = 1 // number of users
            val calendar = Calendar.getInstance()
            val traceFlag = false // means trace events

            // Initialising CloudSim trace
            // در این بخش آماده سازی موتور کلادسیم صورت می پذیرد
            CloudSim.init(numUser, calendar, traceFlag)
            // در این بخش مرکز داده ایجاد می شود
            val datacentreArizona = createDatacentre("Arizona-DC")

            /**
             * Create a WorkflowPlanner with one schedulers.
             */
            // نوع و نحوه ایجاد ماشین های مجازی توسط زمان بند
            val workflowPlanner = WorkflowPlanner("FirstPlanner", 1)

            /**
             * Creating a Workflow engine.
             */
            // ایجاد موتور ماشین مجازی
            val workflowEngine = workflowPlanner.workflowEngine

            /**
             * Creating a list of VMs. The UserID of a VM is basically the id of
             * the scheduler that controls this vm.
             */
            // ایجاد ماشین مجازی
            val vmList = createVM(workflowEngine.getSchedulerId(0), Parameters.getVmNum())

            /**
             * Submitting this list of VMs to the WorkflowEngine.
             */
            // ارسال ماشین های مجازی به موتور وورکفلوسیم
            workflowEngine.submitVmList(vmList, 0)

            /**
             * Binding the data centres with the scheduler.
             */
            workflowEngine.bindSchedulerDatacenter(datacentreArizona.id, 0) // مشخص کردن مرکز داده برای هر ماشین مجازی
            CloudSim.startSimulation() // آغاز شبیه سازی
            val outputList = workflowEngine.getJobsReceivedList<Job>() // روند پایان پذیرفتن کارها
            CloudSim.stopSimulation() // پایان شبیه سازی
            printJobList(outputList) // نمایش کارهای صورت پذیرفته

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
        // مشخصات ماشین مجازی
        val size = 10000L // Hard-disk's size
        val ram  = 512 // ram's size
        val mips = 1000 // قدرت پردازنده
        val bw = 1000L // پهنای باند
        val pesNumber = 1 // تعداد هر هسته
        val vmm = "Xen" // نوع مجازی ساز

        // create VMs
        // ایجاد ماشین مجازی
        val vm = arrayOfNulls<CondorVM>(vms)
        for (i in 0 until vms step 1) {
            val ratio = 1.0
            vm[i] = CondorVM(i, userId, mips*ratio, pesNumber, ram, bw, size, vmm, CloudletSchedulerSpaceShared())
            list.add(vm[i]!!)
        }
        return list
    }

    // تابع تعریف مرکز داده
    fun createDatacentre(name: String): WorkflowDatacenter {
        // Herea the steps needed to create a PowerDatecentre
        // 1. we need to create a list to store one or more Machines
        val hostList = ArrayList<Host>()

        // 2. A machine contains one or more Physical Elements (PEs) or CPUs/Cores.
        // Therefore, should create a list to store these PEs before creating a machine.
        // شماره ماشین فیزیکی
        var hostId = 0
        // ایجاد بیست ماشین فیزیکی
        for (i in 1..20 step 1) {
            var peList = ArrayList<Pe>()
            // مشخص کردن قدرت پردازنده
            val mips = 2000.0
            // 3. Create PEs and add these into the list.
            // for a quad-core machine, a list of 4 PEs is required.
            // اضافه کردن پردازنده ها به سیستم
            peList.add(Pe(0, PeProvisionerSimple(mips)))
            peList.add(Pe(1, PeProvisionerSimple(mips)))
            // مشخص کردن رم
            var ram = 2048 // host memory (MB)
            // مشخص کردن اندازه فضای ذخیره ساز
            var storage = 1000000L
            // مشخص کردن پهنای باند
            var bw = 10000L
            //اضافه کردن سیستم های فیزیکی به سیستم
            hostList.add(Host(hostId, RamProvisionerSimple(ram), BwProvisionerSimple(bw), storage, peList, VmSchedulerTimeShared(peList)))
            hostId++
        }
        // 4. Create a DatacentreCharacteristics object that stores the  properties
        // of a data centre: architecture, OS, list of machines, allocation policy:
        // time- or space-shared, time zone and its price (GE/PE time unit).
        // مشخص کردن نوع مجازی ساز
        val arch = "x86"  // نوع پردازنده
        val os = "Linux"  // نوع سیستم عامل
        val vmm = "Xen"  // نوع مجازی ساز
        val time_zone = 10.0 // زمان قرار گیری مرکز داده
        val cost = 3.0   // هزینه
        val costPerMem = 0.05 // هزینه به ازای هر رم
        val costPerStorage = 0.1 // هزینه به ازای هر فضای ذخیره سازی
        val costPerBW = 0.1 // هزینه پهنای باند
        val storageList = LinkedList<Storage>()
        var datacentre: WorkflowDatacenter? = null
        // مشخص کردن ویژگی های مرکز داده
        var characteristics = DatacenterCharacteristics(arch, os, vmm, hostList, time_zone, cost,
                costPerMem, costPerStorage, costPerBW)

        // 5. Finally, we need to create a store object
        // Bandwidth within data centre is in MB/s.
        // میزان نرخ انتقال مرکز داده در این بخش مشخص می شود
        val maxTransferRate = 15

        try {
            // فضاهای ذخیره ساز و نرخ انتقال به سیتسم اضافه می شود
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
