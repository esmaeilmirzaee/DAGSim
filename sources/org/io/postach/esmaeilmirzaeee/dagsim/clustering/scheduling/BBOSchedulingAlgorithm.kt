///*
//package org.io.postach.esmaeilmirzaeee.dagsim.clustering.scheduling
//
//import org.cloudbus.cloudsim.Cloudlet
//import org.workflowsim.CondorVM
//import org.workflowsim.scheduling.BaseSchedulingAlgorithm
//import java.util.*
//
//
//class BBOSchedulingAlgorithm: BaseSchedulingAlgorithm() {
//
//    private fun array2dOfInt(sizeOuter: Int, sizeInner: Int): Array<IntArray>
//            = Array(sizeOuter) { IntArray(sizeInner) }
//    private val repetition = 100
////    private val tasks: Array<Cloudlet> = cloudletList // a list of tasks
//    private val vms = vmList         // a list of vms
//    private var results: Array<Int>? = null
//    private var lamda: Array<Int>? = null
//    private var mu: IntArray? = null
//    private var mus = mutableMapOf<Int, Int>()
//    private var vmTypes: IntArray? = null
//
//    override fun run() {
//
//    }
//
//    fun preRun(): Unit {
////        var mus = array2dOfInt(repetition, tasks.size)
////        var results = array2dOfInt(repetition, tasks.size)
////         1. Initialising the algorithm
////        results[0] = init()
////         2. calculating mus and also lambdas
////        for (i in 1..repetition step 1) {
////            mus[i] = mu!!
////             3. Following steps should be done for all tasks in each vm
////            for (vm in 0..vms.size step 1) {
////                 4. Below steps should be done for all tasks in the specified vm
////                for (t in 1..vm step 1) {
////                    for (taskId in 0..tasks.size step 1)
////                    if (results[repetition][taskId] == t) {
//                        // 5. Calculating cost of tasks in different VMs
////                        calculate()
//                        // 6. Finding the best possible task to emigrate to another vm
//                        bestEmigrant()
//                        // 7. Assigning the task to the candidate VM type
//                        assignTask2Vm()
//                        // 8. Mutating the current results
//                        val (firstCandidate, secondCandidate) = randomGenerate()
////                        val tempVmId = results[repetition][firstCandidate]
////                        results[repetition][firstCandidate] = results[repetition][secondCandidate]
////                        results[repetition][secondCandidate] = tempVmId
//                        // 9. Saving the result for next iteration
//                        save()
//
//                    }
//
//                }
//
//            }
//            // 5.
//            // 6.
//            // 7.
//            // 8.
//            // 9.
//        }
//    }
//
//    */
///**
//     * First step is initialisation so we implement this function to initialise
//     * the data and then start our calculation
//     *//*
//
//    fun init(): IntArray {
//        val type = tasks.size / vms.size
//        var x = 0
//        for (i in 1..tasks.size step 1) {
//            if (type > i) {
//                ++x
//            }
//            vmTypes!![i] = x
//        }
//        return vmTypes!!
//    }
//
//    */
///**
//     * This function swaps a couple results which have been selected randomly.
//     * The major purpose to do so is avoiding of struggle in a local optimum.
//     *//*
//
//    fun swap(taskAId: Int, taskBId: Int): Pair<Int, Int> = Pair(taskBId, taskAId)
//
//
//    */
///**
//     * This function generates two random numbers
//     *//*
//
//    fun randomGenerate(): Pair<Int, Int> {
//        val random = Random()
//        var num1 = random.nextInt(vms.size) + 1
//        var num2 = random.nextInt(vms.size) + 1
//        while (num1 == num2) {
//            num2 = random.nextInt(vms.size) + 1
//        }
//
//        return Pair(num1, num2)
//    }
//
//    */
///**
//     * Calculating mu and lambda
//     * Formula to calculate lambda is ->
//     * Formula to calculate mu is ->
//     *//*
//
//    fun calculateMu(which: String): Unit {
//        for (i in 1..tasks.size step 1) {
//            mu!![i] = (tasks.size + 1 - i) / (tasks.size + 1)
//            println("I: $i, mu:${mu!![i]}")
//        }
//    }
//
//    fun calculate(task: Cloudlet, vm: Array<CondorVM>) {
//        var deadline: Array<Double>
//        // calculate deadline
//        for (i in 0..vm.size step 1) {
//            if (task.cloudletLength / vm[i].mips >= deadline[task.cloudletId]) {
//                // calculate price
//
//            }
//        }
//
//    }
//
//    fun bestEmigrant() {
//
//    }
//
//    fun assignTask2Vm(): Unit {
//
//    }
//
//    fun mutate(repetition: Int): Unit {
//        val (vmNumber1, vmNumber2) = randomGenerate()
////        val temp = results!![repetition][vmNumber1]
//
//
//    }
//
//    fun save(): Unit {
//
//    }
//
//    fun deadline() {
//        tasks[0].getCloudletLength()
//        for (i in 1..tasks.size) {
//            deadline[i] =
//        }
//    }
//}*/
