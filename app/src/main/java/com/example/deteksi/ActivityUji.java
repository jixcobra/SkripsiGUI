package com.example.deteksi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;
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

        /*LOG*/
        Log.e("atributdata",""+atributData.get(0).atribut);

        /*K-FOLD CROSS VALIDATION*/
        int n = atributData.size();
        Log.w("length", String.valueOf(n));
        int fold = 10;
        dataTrain[][] dTr = new dataTrain[fold][];
        dataTest[][] dTs = new dataTest[fold][];
        ArrayList<ArrayList<dataTrain>> dTrain = new ArrayList<>(fold);
        ArrayList<ArrayList<dataTest>> dTest = new ArrayList<>(fold);

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
                    }
                    dTs[i][p++] = new dataTest(Collections.singletonList(testAtribut.get(j)), Collections.<Integer>singletonList(Integer.valueOf(String.valueOf(atributData.get(j).kelas.get(0)))));
                    dTest.get(i).add(dTs[i][p++]);
                } else {
                    if (trainAtribut.get(j).isEmpty()) {
                        for (int a = 0; a < 5; a++) {
                            trainAtribut.get(j).add(Integer.parseInt(String.valueOf(atributData.get(j).atribut.get(0).get(a))));
                        }
                    }
                    dTr[i][q++] = new dataTrain(Collections.singletonList(trainAtribut.get(j)), Collections.<Integer>singletonList(Integer.valueOf(String.valueOf(atributData.get(j).kelas.get(0)))));
                    dTrain.get(i).add(dTr[i][q++]);
                }
            }
        }
    }
}
