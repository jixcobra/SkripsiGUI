package com.example.deteksi;

import java.util.Comparator;
import java.util.List;

public class dataTrain extends dataSet {
    double distance;
    public dataTrain(List<List<Integer>> atribut, List<Integer> kelas) {
        super(atribut, kelas);
    }

    public double getDistance() {
        return distance;
        //Log.e("distance dataTrain", ""+distance);
    }

    /*COMPARATOR*/
    public static Comparator<dataTrain> dCompare
            = new Comparator<dataTrain>() {

        public int compare(dataTrain d1, dataTrain d2) {

            Double dis1 = d1.getDistance();
            Double dis2 = d2.getDistance();

            //ascending order
            //return (int)(dis1-dis2);
            return dis1.compareTo(dis2);

        }
    };
}
