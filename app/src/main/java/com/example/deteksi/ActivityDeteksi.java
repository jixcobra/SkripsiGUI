package com.example.deteksi;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityDeteksi extends AppCompatActivity {

    private final String TAG = this.getClass().getSimpleName();
    private static final int READ_EXTERNAL_DATA = 24;
    private static final int PERMISSION_REQUEST_CODE = 100;
    private Button btAdd;
    private Button btProses;
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
        btProses = findViewById(R.id.btSends);
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
        intent.setType("*/*");

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
//                    klasifikasi(simplePath);
                    proses(simplePath, uri);
                }
            }
        }
    }

    private void proses(String simplePath, Uri uri) {
        final String path = simplePath;
        final Uri uri1 = uri;

        btProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!path.isEmpty()) {
                    klasifikasi(path,uri1);
                    Log.i("a", "" + path);
                }
            }
        });

    }

    private void klasifikasi(String input, Uri uri1){
        final ProgressDialog dialog = new ProgressDialog(ActivityDeteksi.this);
        File file = new File(input);
        Uri uri = uri1;

        dialog.setMessage("Please Wait...");
        dialog.show();

        //creating request body for file
        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(uri)), file);
        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), "Test");

        //The gson builder
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        //creating retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //creating our api
        Api api = retrofit.create(Api.class);

        //creating a call and calling the upload image method
        Call<MyResponse> call = api.uploadImage(requestFile, descBody);

        //finally performing the call
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                Log.i("a", response.body().message);
                Log.i("a", response.body().data_string);

                Toast.makeText(getApplicationContext(), "" + response.body().message, Toast.LENGTH_SHORT).show();
                dialog.dismiss();

                List<String> returnFromServer = response.body().data_array;
                List<String> returnAtribut = returnFromServer.subList(0,20);
                List<String> returnPackageName = returnFromServer.subList(20,21);
                List<Integer> ratrList = new ArrayList<>();
                for(String s : returnAtribut) ratrList.add(Integer.valueOf(s));

                Log.e("returnAtribut",""+returnAtribut);
                Log.e("returnPackageName",""+returnPackageName);

                fileTest[] fTest = new fileTest[1];
                ArrayList<fileTest> atrFile = new ArrayList<>();
                fTest[0] = new fileTest(Collections.singletonList(ratrList), Collections.singletonList(returnPackageName.get(0)));
                atrFile.add(fTest[0]);
                Log.e("atrFile",""+atrFile.get(0).atribut);

                InputStream inputData = getResources().openRawResource(R.raw.dataset1);
                readData readData = new readData(inputData);
                ArrayList<dataSet> atributData = readData.readAtribut();

                Log.e("atributData",""+atributData.get(50).atribut);

                knn(atrFile, atributData);
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void knn(ArrayList<fileTest> atrFile, ArrayList<dataSet> atributData) {
        /*DISTANCE*/
        List<Double> distance = new ArrayList<>();
//        Log.e("KNN","atrFile"+atrFile.get(0).atribut);
//        Log.e("KNN","atributData"+atributData.get(50).atribut);
        int nilaiK = 3;
        for (int k=0; k < 1; k++) {
            for (int i = 0; i < 200; i++) {
                double hasilJD = jaccard2.getDistance(atributData.get(i), atrFile.get(k));
                distance.add(hasilJD);

                atributData.get(i).distance = distance.get(i);
//                Log.e(TAG, "atribut Distance" + atributData.get(i).distance);
            }

            /*COMPARE*/
            Collections.sort(atributData, dataSet.dCompare);
            for (int i = 0; i < 200; i++) {
                int kelasbaik = 0;
                int kelasburuk = 0;
                Log.e(TAG, "atribut Distance" + atributData.get(i).distance);
                Log.e(TAG,"atribut Kelas"+atributData.get(i).kelas);
                for (int j = 0; j < nilaiK; j++) {
                    if (atributData.get(i).kelas.get(0) == 0) {
                        kelasbaik++;
                    } else if (atributData.get(i).kelas.get(0) == 1) {
                        kelasburuk++;
                    }
                }
                Log.e(TAG,"Kelas Baik"+kelasbaik);
                Log.e(TAG,"Kelas Buruk"+kelasburuk);
                /*PREDIKSI*/
                if (kelasbaik < kelasburuk) {
//                    Log.d(TAG,"KELAS BURUK");
                    tvNamaData.setText("TERDETEKSI MALWARE");
                }
                else if (kelasbaik > kelasburuk) {
//                    Log.d(TAG,"KELAS BAIK");
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
