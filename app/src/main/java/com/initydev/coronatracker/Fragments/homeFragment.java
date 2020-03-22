package com.initydev.coronatracker.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.initydev.coronatracker.Utils.InternetCheck;
import com.initydev.coronatracker.R;
import com.roger.catloadinglibrary.CatLoadingView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

public class homeFragment extends Fragment {
    CatLoadingView LoadScreen;
    TextView cases, deaths, recovered;
    TextView infected_patient, inMildCondition, inCriticalCondition;
    TextView closed_case_outcome, closed_case_recovered, closed_case_death;
    TextView mildcondition_percentage, critical_percentage, closed_case_recovered_percentage, closed_case_death_percentage;
    RequestQueue queue;
    public SwipeRefreshLayout swipeRefreshLayout;

    public homeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        LoadScreen = new CatLoadingView();
        LoadScreen.setCanceledOnTouchOutside(false);
        //LoadScreen.show(getFragmentManager(),"");

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshGlobalList);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        //Global Data Hook
        cases = view.findViewById(R.id.cases);
        deaths = view.findViewById(R.id.deaths);
        recovered = view.findViewById(R.id.recovered);
        //Active Data Hook
        infected_patient = view.findViewById(R.id.active_case_patient);
        inMildCondition = view.findViewById(R.id.active_case_condition);
        inCriticalCondition = view.findViewById(R.id.active_case_critical);
        //Closed Case Data Hook
        closed_case_outcome = view.findViewById(R.id.closed_case_outcome);
        closed_case_recovered = view.findViewById(R.id.closed_case_recovered);
        closed_case_death = view.findViewById(R.id.closed_case_death);
        //Active Case Percentage
        mildcondition_percentage = view.findViewById(R.id.active_case_condition_percentage);
        critical_percentage = view.findViewById(R.id.active_case_critical_percentage);
        //Closed Case Percentage
        closed_case_recovered_percentage = view.findViewById(R.id.closed_case_recovered_percentage);
        closed_case_death_percentage = view.findViewById(R.id.closed_case_death_percentage);

        InternetCheck check = new InternetCheck();
        if (check.isInternetOn(getActivity()) == false) {
            Toast.makeText(getContext(), "Please Enable Your Internet!", Toast.LENGTH_SHORT).show();


        } else {
            queue = Volley.newRequestQueue(getActivity());
            GetGlobalData();
            GetActiveCase();
            onClickListeners();

        }
        return view;
    }

    private void onClickListeners() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetGlobalData();
                GetActiveCase();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void GetGlobalData() {
        String url = "https://corona.lmao.ninja/all";
        LoadScreen.show(getFragmentManager(), "");
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String in = response;
                try {
                    JSONObject reader = new JSONObject(in);
                    cases.setText(formatNumber(reader.getString("cases")));
                    deaths.setText(formatNumber(reader.getString("deaths")));
                    recovered.setText(formatNumber(reader.getString("recovered")));
                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "Something Error..!!", Toast.LENGTH_SHORT).show();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Something Error..!!", Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(request);
    }

    private void GetActiveCase() {
        String url = "https://covid19-server.chrismichael.now.sh/api/v1/AllReports";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray report = response.getJSONArray("reports");
                    //String ActiveCases = report.getJSONObject(0).getJSONArray("active_cases").getJSONObject(0).getString("currently_infected_patients").toString();
                    //Set Text for Active Case Card
                    infected_patient.setText(formatNumber(report.getJSONObject(0).getJSONArray("active_cases").getJSONObject(0).getString("currently_infected_patients").toString()));
                    inMildCondition.setText(formatNumber(report.getJSONObject(0).getJSONArray("active_cases").getJSONObject(0).getString("inMidCondition").toString()));
                    inCriticalCondition.setText(formatNumber(report.getJSONObject(0).getJSONArray("active_cases").getJSONObject(0).getString("criticalStates").toString()));
                    //Set Active Case Percentages
                    double Mild_Cond = Double.parseDouble(report.getJSONObject(0).getJSONArray("active_cases").getJSONObject(0).getString("inMidCondition").toString());
                    double Infected_Patient = Double.parseDouble(report.getJSONObject(0).getJSONArray("active_cases").getJSONObject(0).getString("currently_infected_patients").toString());
                    double critical_pat = Double.parseDouble(report.getJSONObject(0).getJSONArray("active_cases").getJSONObject(0).getString("criticalStates").toString());
                    mildcondition_percentage.setText("  " + Math.round((Mild_Cond * 100) / Infected_Patient) + "%");
                    critical_percentage.setText("  " + Math.round((critical_pat * 100) / Infected_Patient) + "%");

                    //Set Closed Case Percentage
                    double closed_case = Double.parseDouble(report.getJSONObject(0).getJSONArray("closed_cases").getJSONObject(0).getString("cases_which_had_an_outcome").toString());
                    double recoverd_case = Double.parseDouble(report.getJSONObject(0).getJSONArray("closed_cases").getJSONObject(0).getString("recovered").toString());
                    double death_case = Double.parseDouble(report.getJSONObject(0).getJSONArray("closed_cases").getJSONObject(0).getString("deaths").toString());
                    closed_case_recovered_percentage.setText("  " + Math.round((recoverd_case * 100) / closed_case) + "%");
                    closed_case_death_percentage.setText("  " + Math.round((death_case * 100) / closed_case) + "%");

                    //Set text for Closed Cases
                    //String ClosedCases = report.getJSONObject(0).getJSONArray("closed_cases").getJSONObject(0).getString("deaths").toString();
                    closed_case_outcome.setText(formatNumber(report.getJSONObject(0).getJSONArray("closed_cases").getJSONObject(0).getString("cases_which_had_an_outcome").toString()));
                    closed_case_recovered.setText(formatNumber(report.getJSONObject(0).getJSONArray("closed_cases").getJSONObject(0).getString("recovered").toString()));
                    closed_case_death.setText(formatNumber(report.getJSONObject(0).getJSONArray("closed_cases").getJSONObject(0).getString("deaths").toString()));
                    LoadScreen.dismiss();

                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "Something Error..!!", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Something Error..!!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }

    public static String formatNumber(int number) {
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(number);
    }

    public static String formatNumber(String number) {
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(Integer.parseInt(number));
    }

}
