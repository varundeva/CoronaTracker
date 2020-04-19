package com.initydev.coronatracker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.initydev.coronatracker.Models.modelDistrict;
import com.initydev.coronatracker.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class districtAdapter extends RecyclerView.Adapter<districtAdapter.ViewHolder> {
    private Context mContext;
    private List<modelDistrict> list;

    public districtAdapter(Context mContext, List<modelDistrict> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.districtdatacard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.districtName.setText(list.get(position).getDistrictName());

        String cases = formatNumber(list.get(position).getConfirmed());
        holder.confirmedCase.setText(cases);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView confirmedCase, districtName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            districtName = itemView.findViewById(R.id.districtName);
            confirmedCase = itemView.findViewById(R.id.confirmedCase);
        }
    }

    public static String formatNumber(int number) {
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(number);
    }

    public static String formatNumber(String number) {
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(Integer.parseInt(number));
    }
}
