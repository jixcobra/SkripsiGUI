package com.example.deteksi;

import java.util.Comparator;
import java.util.List;

public class dataSet {
    List<List<Integer>> atribut;
    List<Integer> kelas;
    List<String> packageName;

    double distance;

    public dataSet(List<List<Integer>> atribut, List<Integer> kelas, List<String> packageName){
        this.atribut = atribut;
        this.kelas = kelas;
        this.packageName = packageName;
    }

    public double getDistance() {
        return distance;
    }

    public static Comparator<dataSet> dCompare
            = new Comparator<dataSet>() {

        public int compare(dataSet d1, dataSet d2) {

            Double dis1 = d1.getDistance();
            Double dis2 = d2.getDistance();

            //ascending order
            //return (int)(dis1-dis2);
            return dis1.compareTo(dis2);

        }
    };
}
