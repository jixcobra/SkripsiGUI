package com.example.deteksi;

import java.util.List;

public class dataSet {
    List<List<Integer>> atribut;
    List<Integer> kelas;

    public dataSet(List<List<Integer>> atribut, List<Integer> kelas){
        this.atribut = atribut;
        this.kelas = kelas;
    }
}
