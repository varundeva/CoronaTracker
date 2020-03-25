package com.initydev.coronatracker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.initydev.coronatracker.R;
import com.initydev.coronatracker.Models.modelCountry;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



public class countryAdapter extends RecyclerView.Adapter<countryAdapter.MyViewHolder> implements Filterable {
    private Context mContext;
    private List<modelCountry> list;
    private List<modelCountry> filteredDataList;


    public countryAdapter(Context mContext, List<modelCountry> list) {
        this.mContext = mContext;
        this.list = list;
        this.filteredDataList = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.countrylayout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder._country.setText("" + filteredDataList.get(position).getCountry());

        String cases = formatNumber(filteredDataList.get(position).getCases());
        holder._cases.setText(cases);

        String cases_today = formatNumber(filteredDataList.get(position).getTodayCases());
        holder._cases_today.setText(cases_today);

        String cases_active = formatNumber(filteredDataList.get(position).getActive());
        holder._cases_active.setText(cases_active);

        String total_death = formatNumber(filteredDataList.get(position).getDeaths());
        holder._death.setText(total_death);

        String today_death = formatNumber(filteredDataList.get(position).getTodayDeaths());
        holder._death_today.setText(today_death);

        String recovered = formatNumber(filteredDataList.get(position).getRecovered());
        holder._recovered.setText(recovered);

        String critical = formatNumber(filteredDataList.get(position).getCritical());
        holder._critical.setText(critical);
    }

    @Override
    public int getItemCount() {
        return filteredDataList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView _cases, _cases_today, _cases_active;
        public TextView _death, _death_today;
        public TextView _recovered, _critical;
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

    }


    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String searchString = charSequence.toString();

                if (searchString.isEmpty()||searchString == null || searchString.length() == 0) {
                    filteredDataList=list;
                } else {
                    ArrayList<modelCountry> tempFilteredList = new ArrayList<>();
                    for (modelCountry country : list) {
                        String filterPattern = searchString.toLowerCase().trim();
                        // search for user title
                        if (country.getCountry().toLowerCase().contains(filterPattern)) {

                            tempFilteredList.add(country);
                        }
                    }
                    filteredDataList = tempFilteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredDataList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredDataList = (ArrayList<modelCountry>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public static String formatNumber(int number) {
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(number);
    }

    public static String formatNumber(String number) {
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(Integer.parseInt(number));
    }
}


