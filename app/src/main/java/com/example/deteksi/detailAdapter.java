package com.example.deteksi;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class detailAdapter extends RecyclerView.Adapter<detailAdapter.detailViewHolder> {

    private ArrayList<detail> dataList;

    public detailAdapter(ArrayList<detail> dataList) {
        this.dataList = dataList;
    }

    @Override
    public detailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_detail, parent, false);
        return new detailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(detailViewHolder holder, int position) {
        holder.tvPkgName.setText(dataList.get(position).getPackageName());
        holder.tvRealCls.setText("Kelas Asli : "+dataList.get(position).getRealClass());
        holder.tvDetectCls.setText("Kelas Prediksi : "+dataList.get(position).getDetectClass());
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class detailViewHolder extends RecyclerView.ViewHolder{
        private TextView tvPkgName, tvRealCls, tvDetectCls;

        public detailViewHolder(View itemView) {
            super(itemView);
            tvPkgName = (TextView) itemView.findViewById(R.id.pkgName);
            tvRealCls = (TextView) itemView.findViewById(R.id.realCls);
            tvDetectCls = (TextView) itemView.findViewById(R.id.detectCls);
        }
    }
}
