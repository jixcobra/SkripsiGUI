package com.example.deteksi;

import android.util.Log;
import com.opencsv.CSVReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class readData {
    InputStream inputStream;
    public readData(InputStream inputStream){
        this.inputStream = inputStream;
    }
    String[] data;
    dataSet[] dSet = new dataSet[200];
    ArrayList<dataSet> dSetList = new ArrayList<>();
    List<List<Integer>> listAtribut = new ArrayList<List<Integer>>();
    List<Integer> listKelas = new ArrayList<>();
    List<String> listPackage = new ArrayList<>();

    /*READ DATA*/
    public ArrayList<dataSet> readAtribut(){
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        CSVReader csvReader = new CSVReader(reader);
        try {
            int a = 0;
            while ((data = csvReader.readNext()) != null){
                listAtribut.add(new ArrayList<Integer>());
                for (int i=0; i<20; i++){
                    listAtribut.get(a).add(Integer.parseInt(data[i]));
                }
                listKelas.add(new Integer(data[20]));
                listPackage.add(data[21]);

                dSet[a] = new dataSet(Collections.singletonList(listAtribut.get(a)), Collections.singletonList(listKelas.get(a)), Collections.singletonList(listPackage.get(a)));
                dSetList.add(dSet[a]);
//                Log.w("dset",""+dSet[a].kelas);
                a++;
            }
        }
        catch (IOException e){
            Log.e("Error","Read Data pada class readData error");
        }

        return dSetList;
    }
}
