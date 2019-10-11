package com.example.deteksi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ActivityUji extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uji);

        InputStream inputData = getResources().openRawResource(R.raw.dataset);
        readData readData = new readData(inputData);
        ArrayList<dataSet> atributData = readData.readAtribut();
        int truePv = 0, trueNv = 0, falsePv = 0, falseNv = 0;
        Integer bahaya=1, aman=0;

        /*LOG*/
        Log.e("atributdata", "" + atributData.get(0).atribut);

        /*K-FOLD CROSS VALIDATION*/
        int n = atributData.size();
        Log.w("length", String.valueOf(n));
        int fold = 10;
        dataTrain[][] dTr = new dataTrain[fold][];
        dataTest[][] dTs = new dataTest[fold][];
        ArrayList<ArrayList<dataTrain>> dTrain = new ArrayList<ArrayList<dataTrain>>(fold);
        ArrayList<ArrayList<dataTest>> dTest = new ArrayList<ArrayList<dataTest>>(fold);

        int chunk = n / fold;
        List<List<Integer>> testAtribut = new ArrayList<List<Integer>>(5);
        List<List<Integer>> trainAtribut = new ArrayList<List<Integer>>(5);
        for (int i = 0; i < fold; i++) {
            int start = chunk * i;
            int end = chunk * (i + 1);
            if (i == fold - 1) end = n;

            dTr[i] = new dataTrain[n - end + start];
            dTs[i] = new dataTest[end - start];

            for (int j = 0, p = 0, q = 0; j < n; j++) {
                testAtribut.add(new ArrayList<Integer>());
                trainAtribut.add(new ArrayList<Integer>());
                if (j >= start && j < end) {
                    for (int a = 0; a < 5; a++) {
                        testAtribut.get(j).add(Integer.parseInt(String.valueOf(atributData.get(j).atribut.get(0).get(a))));
//                        Log.e("testAtribut",""+testAtribut.get(j));
                    }
                    Log.e("testAtribut",""+testAtribut.get(j));
                    dTs[i][p++]= new dataTest(Collections.singletonList(testAtribut.get(j)),Collections.singletonList(atributData.get(j).kelas.get(0)));
                } else {
                    if (trainAtribut.get(j).isEmpty()) {
                        for (int a = 0; a < 5; a++) {
                            trainAtribut.get(j).add(Integer.parseInt(String.valueOf(atributData.get(j).atribut.get(0).get(a))));
                        }
                    }
                    dTr[i][q++] = new dataTrain(Collections.singletonList(trainAtribut.get(j)), Collections.<Integer>singletonList(Integer.valueOf(String.valueOf(atributData.get(j).kelas.get(0)))));
                }
            }
            for(int x=0; x<(end - start); x++){
                dTest.add(new ArrayList<dataTest>());
//                Log.w("dTs",""+dTs[i][x].atribut);
                dTest.get(i).add(dTs[i][x]);
//                Log.w("dTest",""+dTest.get(i).get(x).atribut);
            }
            for(int z=0; z<(n - end + start); z++){
                dTrain.add(new ArrayList<dataTrain>());
//                Log.e("dTr",""+dTr[i][z].atribut);
                dTrain.get(i).add(dTr[i][z]);
//                Log.e("dTrain",""+dTrain.get(i).get(z).atribut);
            }
        }

        /*GET DISTANCE*/
        List<Double> distance = new ArrayList<>();
        Integer[][] klsPrediksi = new Integer[fold][];
        int nilaiK = 3;
        List<Integer> kls = new ArrayList<>();
        for (int a = 0; a < fold; a++) {
            klsPrediksi[a] = new Integer[5];
            for (int k = 0; k < chunk; k++) {
                int kelasbaik = 0;
                int kelasburuk = 0;
                for (int i = 0; i < n - chunk; i++) {
                    double hasilJD = jaccardDistance.getDistance(dTrain.get(a).get(i), dTest.get(a).get(k));
                    distance.add(hasilJD);
                    dTrain.get(a).get(i).distance = distance.get(i);
                }

                /*COMPARE*/
                Collections.sort(dTrain.get(a), dataTrain.dCompare);
//                Log.e("=====","====");
//                for (int i = 0; i < n-chunk; i++) {
//                    Log.w("sorting", "" + dTrain.get(a).get(i).distance);
//                }

                for (int j = 0; j < nilaiK; j++) {
//                    Log.e("kelas", "" + dTrain.get(a).get(i).kelas + dTrain.get(a).get(i).distance);
                    if (dTrain.get(a).get(j).kelas.get(0) == 0) {
                        kelasbaik++;
                    } else if (dTrain.get(a).get(j).kelas.get(0) == 1) {
                        kelasburuk++;
                    }
                }
                /*TP TN FP FN*/
                if (kelasbaik < kelasburuk) {
                    if (dTest.get(a).get(k).kelas.get(0) == 1){
                        trueNv++;
                        klsPrediksi[a][k] = bahaya;
                        dTest.get(a).get(k).kelasPrediksi = klsPrediksi[a][k];
                    }
                    else if(dTest.get(a).get(k).kelas.get(0) == 0) {
                        falsePv++;
                        klsPrediksi[a][k] = bahaya;
                        dTest.get(a).get(k).kelasPrediksi = klsPrediksi[a][k];
                    }

                }
                else if (kelasbaik > kelasburuk) {
                    if (dTest.get(a).get(k).kelas.get(0) == 0){
                        truePv++;
                        klsPrediksi[a][k] = aman;
                        dTest.get(a).get(k).kelasPrediksi = klsPrediksi[a][k];
                    }
                    else if(dTest.get(a).get(k).kelas.get(0) == 1) {
                        falseNv++;
                        klsPrediksi[a][k] = aman;
                        dTest.get(a).get(k).kelasPrediksi = klsPrediksi[a][k];
                    }
                }
                dTrain.remove(distance);
                distance.clear();
                Log.e("===","===");
                Log.w("Kelas Real",""+dTest.get(a).get(k).kelas);
                Log.w("Kelas Prediksi",""+dTest.get(a).get(k).kelasPrediksi);
            }
        }

        /*TAMPILAN*/
        TextView tp = findViewById(R.id.truePositive);
        TextView tn = findViewById(R.id.trueNegative);
        TextView fp = findViewById(R.id.falsePositive);
        TextView fn = findViewById(R.id.falseNegative);

        tp.setText(String.valueOf(truePv));
        tn.setText(String.valueOf(trueNv));
        fp.setText(String.valueOf(falsePv));
        fn.setText(String.valueOf(falseNv));
    }
}
