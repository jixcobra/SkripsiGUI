package com.example.deteksi;

import android.util.Log;

public class jaccardDistance {
    public static double getDistance(dataTrain tr, dataTest ts){
        int p = 0;
        int q = 0;
        int r = 0;
        int s = 0;
        double distance;
        for(int j = 0; j < 5; j ++){
            int dtr = tr.atribut.get(0).get(j);
            int dts = ts.atribut.get(0).get(j);
            if (dtr == 1){
                if (dts == 1){
                    p++;
                }
                else {
                    q++;
                }
            }
            else if (dtr == 0){
                if (dts == 0){
                    s++;
                }
                else {
                    r++;
                }
            }
//            Log.e("atribut ts",""+ts.atribut.get(0).get(j));
//            Log.e("atribut tr",""+tr.atribut.get(0).get(j));
        }
        distance = (double)(q+r)/(p+q+r);
//        Log.e("distance",""+distance);
//        Log.e("p q r s", ""+p +q +r +s );
        return distance;
    }
}
