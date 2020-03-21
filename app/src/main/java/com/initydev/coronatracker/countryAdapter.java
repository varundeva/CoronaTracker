package com.initydev.coronatracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import static com.initydev.coronatracker.MyViewHolder.formatNumber;


public class countryAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private Context mContext;
    private List<modelCountry> list;

    public countryAdapter(Context mContext, List<modelCountry> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.countrylayout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //DecimalFormat formatter = new DecimalFormat("###,###");

        holder._country.setText(""+list.get(position).getCountry());

        String cases = formatNumber(list.get(position).getCases());
        holder._cases.setText(cases);

        String cases_today = formatNumber(list.get(position).getTodayCases());
        holder._cases_today.setText(cases_today);

        String cases_active = formatNumber(list.get(position).getActive());
        holder._cases_active.setText(cases_active);

        String total_death = formatNumber(list.get(position).getDeaths());
        holder._death.setText(total_death);

        String today_death = formatNumber(list.get(position).getTodayDeaths());
        holder._death_today.setText(today_death);

        String recovered = formatNumber(list.get(position).getRecovered());
        holder._recovered.setText(recovered);

        String critical = formatNumber(list.get(position).getCritical());
        holder._critical.setText(critical);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}


class MyViewHolder extends RecyclerView.ViewHolder {

    public TextView _cases,_cases_today,_cases_active;
    public TextView _death,_death_today;
    public TextView _recovered,_critical;
    public TextView _country;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        _country = itemView.findViewById(R.id.country);
        _cases = itemView.findViewById(R.id.tv_cases);
        _cases_today = itemView.findViewById(R.id.tv_cases_today);
        _cases_active = itemView.findViewById(R.id.tv_cases_active);
        _death = itemView.findViewById(R.id.tv_deaths);
        _death_today = itemView.findViewById(R.id.tv_deaths_today);
        _recovered = itemView.findViewById(R.id.tv_recovered);
        _critical = itemView.findViewById(R.id.tv_critical);

    }

    public static String formatNumber(int number){
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(number);
    }
    public static String formatNumber(String number){
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(Integer.parseInt(number));
    }
}