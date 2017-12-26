package org.io.postach.esmaeilmirzaeee.dagsim.clustering.scheduling;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Log;
import org.workflowsim.CondorVM;
import org.workflowsim.WorkflowSimTags;
import org.workflowsim.scheduling.BaseSchedulingAlgorithm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FTBWS extends BaseSchedulingAlgorithm {
    private List<Cloudlet> tasks;
    private Set<Integer> hasAdded;

    public FTBWS() {
        super();
    }

    @Override
    public void run() throws Exception {
        tasks = getCloudletList();
        hasAdded = new HashSet<>();
        int latestTaskID = 0;
        int notPrimaryVM = 0;
        Log.printLine("Tasks size is " + tasks.size());
        for (int i = 0; i < tasks.size(); i++) {
            Cloudlet primaryTask = tasks.get(i);
            Cloudlet backupTask = tasks.get(i);
            List<CondorVM> vms = getVmList();
            Log.printLine("VM size is " + vms.size());
            for (int j = 0; j < vms.size(); j++) {
                for (int c = 0; c < latestTaskID; c++) {
                    if (tasks.get(c).getVmId() == WorkflowSimTags.VM_STATUS_READY) {
                        vms.get(tasks.get(c).getVmId()).setState(WorkflowSimTags.VM_STATUS_IDLE);
                    }
                }
                CondorVM primaryCandidateVM = vms.get(j);
                if (primaryCandidateVM.getState() == WorkflowSimTags.VM_STATUS_IDLE) {
                    primaryTask.setVmId(primaryCandidateVM.getId());
                    getScheduledList().add(primaryTask);
                    primaryCandidateVM.setState(WorkflowSimTags.VM_STATUS_BUSY);
                    hasAdded.add(primaryTask.getVmId());
                    latestTaskID++;
                    notPrimaryVM = primaryCandidateVM.getId();
//                    break;
                }
                if (notPrimaryVM == j && j < vms.size() - 1) ++j;
                CondorVM backupCandidateVM = vms.get(j);
                if (backupCandidateVM.getState() == WorkflowSimTags.VM_STATUS_IDLE) {
                    backupTask.setVmId(backupCandidateVM.getId());
                    getScheduledList().add(backupTask);
                    backupCandidateVM.setState(WorkflowSimTags.VM_STATUS_BUSY);
                    hasAdded.add(backupTask.getVmId());
                    latestTaskID++;
                    break;
                }
            }
            Log.printLine("Primary task " + primaryTask.getCloudletId() + " is added to VM#" +
                    primaryTask.getVmId());
        }
    }
}
