package org.io.postach.esmaeilmirzaeee.dagsim.clustering.utils;

/**
 * Created by esmaeil on 8/2/17.
 */
public class SortIndex implements Comparable<SortIndex> {
    public final int index;
    public final double value;

    public SortIndex(int index, double value) {
        this.index = index;
        this.value = value;
    }

    @Override
    public int compareTo(SortIndex other) {
        //multiplied to -1 as the author need descending sort order
        return +1 * Double.valueOf(this.value).compareTo(other.value);
    }

}
