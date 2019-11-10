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

public class readFile {
    InputStream inputStream;
    public readFile(InputStream inputStream){
        this.inputStream = inputStream;
    }
    String[] data;
    fileTest[] fTes = new fileTest[1];
    ArrayList<fileTest> fTestList = new ArrayList<>();
    List<List<Integer>> listAtribut = new ArrayList<List<Integer>>();
    List<String> listPackage = new ArrayList<>();

    /*READ DATA*/
    public ArrayList<fileTest> readAtribut(){
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        CSVReader csvReader = new CSVReader(reader);
        try {
            int a = 0;
            while ((data = csvReader.readNext()) != null){
                listAtribut.add(new ArrayList<Integer>());
                for (int i=0; i<20; i++){
                    listAtribut.get(a).add(Integer.parseInt(data[i]));
                }
                listPackage.add(data[20]);

                fTes[a] = new fileTest(Collections.singletonList(listAtribut.get(a)), Collections.singletonList(listPackage.get(a)));
                fTestList.add(fTes[a]);
//                Log.w("dset",""+fTes[a]);
                a++;
            }
        }
        catch (IOException e){
            Log.e("Error","Read Data pada class readData error");
        }

        return fTestList;
    }
}
