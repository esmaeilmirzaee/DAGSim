package org.io.postach.esmaeilmirzaeee.dagsim.clustering.scheduling;

import org.cloudbus.cloudsim.Cloudlet;
import org.workflowsim.CondorVM;
import org.workflowsim.scheduling.BaseSchedulingAlgorithm;

import java.util.List;

public class FBBOSchedulingAlgorithm extends BaseSchedulingAlgorithm {

    @Override
    public void run() throws Exception {
        List<CondorVM> vms = getVmList();
        List<Cloudlet> tasks = getCloudletList();
        List<Cloudlet> scheduled = getScheduledList();
        doing(vms, tasks, scheduled);
    }

    private void doing(List<CondorVM> vms, List<Cloudlet> tasks, List<Cloudlet> scheduled) {

    }

    public static void main(String[] args) {
        FBBOSchedulingAlgorithm fbbo = new FBBOSchedulingAlgorithm();
        System.out.println(fbbo.getVmList().size());
    }

}
