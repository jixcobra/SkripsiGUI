package com.example.deteksi;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActivityDeteksi extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private static final int READ_EXTERNAL_DATA = 24;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private Button btAdd;
    private TextView tvNamaData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deteksi);
        setInit();
        setClickEvent();
    }

    private void setInit() {
        btAdd = findViewById(R.id.add);
        tvNamaData = findViewById(R.id.dataName);
    }

    private void setClickEvent() {
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    if (chekPermissions()) {
                        performFileSearch();
                    } else {
                        reqPermissions();
                    }
                }
            }
        });
    }

    private void performFileSearch() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");

        startActivityForResult(intent, READ_EXTERNAL_DATA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == READ_EXTERNAL_DATA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null){
                    String path = uri.getPath();
                    int sub = path.indexOf(":");
                    String simplePath = path.substring(sub + 1);
                    Toast.makeText(this, ""+simplePath, Toast.LENGTH_SHORT).show();
                    klasifikasi(simplePath);
                }
            }
        }
    }

    private void klasifikasi(String input){
        File data = new File(input);
        try {
            FileInputStream fileInputStream = new FileInputStream(data);
            readFile readFile = new readFile(fileInputStream);
            ArrayList<fileTest> atrFile = readFile.readAtribut();
            Log.e("atrFile",""+atrFile.get(0).atribut);

            InputStream inputData = getResources().openRawResource(R.raw.dataset1);
            readData readData = new readData(inputData);
            ArrayList<dataSet> atributData = readData.readAtribut();

            knn(atrFile, atributData);

        } catch (Exception e){
            Log.e(TAG, "Error Klasifikasi: " + e.getMessage());
        }
    }

    private void knn(ArrayList<fileTest> atrFile, ArrayList<dataSet> atributData) {
        /*DISTANCE*/
        ArrayList<dataTrain> dTrains = new ArrayList<dataTrain>(200);
        List<Double> distance = new ArrayList<>();
        Integer[] klsPrediksi = new Integer[1];
        int nilaiK = 19;
        for (int k=0; k < 1; k++) {
            int kelasbaik = 0;
            int kelasburuk = 0;
            for (int i = 0; i < 200; i++) {
                double hasilJD = jaccard2.getDistance(atributData.get(i), atrFile.get(k));
                distance.add(hasilJD);

                atributData.get(i).distance = distance.get(i);
                Log.e(TAG,"atribut Distance"+atributData.get(i).distance);

                /*COMPARE*/
                Collections.sort(atributData, dataSet.dCompare);

                for (int j = 0; j < nilaiK; j++) {
                    if (atributData.get(i).kelas.get(0) == 0) {
                        kelasbaik++;
                    } else if (atributData.get(i).kelas.get(0) == 1) {
                        kelasburuk++;
                    }
                }
                /*PREDIKSI*/
                if (kelasbaik < kelasburuk) {
                    Log.d(TAG,"KELAS BURUK");
                    tvNamaData.setText("TERDETEKSI MALWARE");
                }
                else if (kelasbaik > kelasburuk) {
                    Log.d(TAG,"KELAS BAIK");
                    tvNamaData.setText("TERDETEKSI AMAN");
                }
                atributData.remove(distance);
                distance.clear();
            }
        }

    }

    private boolean chekPermissions() {
        int result = ContextCompat.checkSelfPermission(
                ActivityDeteksi.this,
                Manifest.permission.READ_EXTERNAL_STORAGE
        );
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void reqPermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                ActivityDeteksi.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        ){
            Toast.makeText(ActivityDeteksi.this, "Read External Storage permission allows us to read files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(ActivityDeteksi.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.e("value", "Permission Granted !");
            } else {
                Log.e("value", "Permission Denied !");
            }
        }
    }
}
