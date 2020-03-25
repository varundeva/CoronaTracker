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

import com.initydev.coronatracker.Models.modelCountry;
import com.initydev.coronatracker.Models.modelState;
import com.initydev.coronatracker.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class stateAdapter extends RecyclerView.Adapter<stateAdapter.ViewHolder> implements Filterable {
    private Context mContext;
    private List<modelState> list;
    private List<modelState> filteredDataList;

    public stateAdapter(Context mContext, List<modelState> list) {
        this.mContext = mContext;
        this.list = list;
        this.filteredDataList = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.statedatacard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull stateAdapter.ViewHolder holder, int position) {

        holder.state_name.setText(filteredDataList.get(position).getState());

        String active = formatNumber(filteredDataList.get(position).getActive());
        holder.state_active_case.setText(active);

        String total = formatNumber(filteredDataList.get(position).getConfirmed());
        holder.state_total_case.setText(total);

        String recoverd = formatNumber(filteredDataList.get(position).getRecovered());
        holder.state_recoverd_case.setText(recoverd);

        String death = formatNumber(filteredDataList.get(position).getDeaths());
        holder.state_death_case.setText(death);

    }

    @Override
    public int getItemCount() {
        return filteredDataList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String searchString = charSequence.toString();

                if (searchString.isEmpty() || searchString == null || searchString.length() == 0) {
                    filteredDataList = list;
                } else {
                    ArrayList<modelState> tempFilteredList = new ArrayList<>();
                    for (modelState state : list) {
                        String filterPattern = searchString.toLowerCase().trim();
                        // search for user title
                        if (state.getState().toLowerCase().contains(filterPattern)) {

                            tempFilteredList.add(state);
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
                filteredDataList = (ArrayList<modelState>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView state_total_case, state_active_case, state_recoverd_case, state_death_case, state_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            state_active_case = itemView.findViewById(R.id.state_active_case);
            state_total_case = itemView.findViewById(R.id.state_total_case);
            state_recoverd_case = itemView.findViewById(R.id.state_recoverd_case);
            state_death_case = itemView.findViewById(R.id.state_death_case);
            state_name = itemView.findViewById(R.id.state_name);

        }
    }


    public static String formatNumber(int number) {
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(number);
    }

    public static String formatNumber(String number) {
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(Integer.parseInt(number));
    }
}
