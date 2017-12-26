package org.io.postach.esmaeilmirzaeee.dagsim.clustering.utils;

/**
 * Created by esmaeil on 8/4/17.
 */
public class SPEC {
    long size; //image size (MB)
    int ram; //vm memory (MB)
    int mips;
    long bw;
    int pesNumber; //number of cpus

    public SPEC(long size, int ram, int mips, long bw, int pesNumber) {
        this.size = size;
        this.ram = ram;
        this.mips = mips;
        this.bw = bw;
        this.pesNumber = pesNumber;
    }

    public long getSize() {
        return size;
    }

    public int getRam() {
        return ram;
    }

    public int getMips() {
        return mips;
    }

    public long getBw() {
        return bw;
    }

    public int getPesNumber() {
        return pesNumber;
    }
}
