package com.initydev.coronatracker.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
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
    public SwipeRefreshLayout swipeRefreshLayout;
    CatLoadingView LoadScreen;
    TextView cases, deaths, recovered;
    TextView infected_patient, inMildCondition, inCriticalCondition;
    TextView closed_case_outcome, closed_case_recovered, closed_case_death;
    TextView mildcondition_percentage, critical_percentage, closed_case_recovered_percentage, closed_case_death_percentage;
    TextView _cases, _cases_today, _cases_active, _death, _death_today, _recovered, _critical, _country;
    String UserCountry;
    JSONObject countryData;
    RequestQueue queue;

    public homeFragment() {
        // Required empty public constructor
    }

    public static String formatNumber(int number) {

        return NumberFormat.getNumberInstance(Locale.getDefault()).format(number);
    }

    public static String formatNumber(String number) {
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(Integer.parseInt(number));
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

        //Country Card Hook
        _country = view.findViewById(R.id.country);
        _cases = view.findViewById(R.id.tv_cases);
        _cases_today = view.findViewById(R.id.tv_cases_today);
        _cases_active = view.findViewById(R.id.tv_cases_active);
        _death = view.findViewById(R.id.tv_deaths);
        _death_today = view.findViewById(R.id.tv_deaths_today);
        _recovered = view.findViewById(R.id.tv_recovered);
        _critical = view.findViewById(R.id.tv_critical);


        InternetCheck check = new InternetCheck();
        if (check.isInternetOn(getActivity()) == false) {
            Toast.makeText(getContext(), "Please Enable Your Internet!", Toast.LENGTH_SHORT).show();


        } else {
            queue = Volley.newRequestQueue(getActivity());
            GetAllCardData();
            ScreenSwipeDown();

            TelephonyManager telephoneManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
            String countryCode = telephoneManager.getNetworkCountryIso();
            Locale loc = new Locale("", countryCode);
            UserCountry = loc.getDisplayCountry();
            //UserCountry = "China";
        }
        return view;
    }

    private void ScreenSwipeDown() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetAllCardData();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void GetAllCardData() {
        LoadScreen.show(getFragmentManager(), "");
        String url = "https://covid19-server.chrismichael.now.sh/api/v1/AllReports";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray report = response.getJSONArray("reports");

                    //Set Global Card
                    cases.setText(formatNumber(report.getJSONObject(0).getInt("cases")));
                    deaths.setText(formatNumber(report.getJSONObject(0).getInt("deaths")));
                    recovered.setText(formatNumber(report.getJSONObject(0).getInt("recovered")));

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
                    closed_case_outcome.setText(formatNumber(report.getJSONObject(0).getJSONArray("closed_cases").getJSONObject(0).getString("cases_which_had_an_outcome").toString()));
                    closed_case_recovered.setText(formatNumber(report.getJSONObject(0).getJSONArray("closed_cases").getJSONObject(0).getString("recovered").toString()));
                    closed_case_death.setText(formatNumber(report.getJSONObject(0).getJSONArray("closed_cases").getJSONObject(0).getString("deaths").toString()));
                    LoadScreen.dismiss();

                    JSONArray countries = report.getJSONObject(0).getJSONArray("table").getJSONArray(0);
                    for (int i = 0; i < countries.length(); i++) {
                        String countr = countries.getJSONObject(i).getString("Country");
                        if (countr.equals(UserCountry)) {
                            countryData = countries.getJSONObject(i);
                        }
                    }
                    //Set Data to Textview
                    _country.setText("My Country " + countryData.getString("Country"));
                    _cases.setText(countryData.getString("TotalCases"));
                    _cases_today.setText(countryData.getString("NewCases"));
                    _cases_active.setText(countryData.getString("ActiveCases"));
                    _death.setText(countryData.getString("TotalDeaths"));
                    _recovered.setText(countryData.getString("TotalRecovered"));
                    //Add Condition if data is there or not
                    if (TextUtils.isEmpty(countryData.getString("NewDeaths"))) {
                        _death_today.setText("0");
                    } else _death_today.setText(formatNumber(countryData.getString("NewDeaths")));
                    if (TextUtils.isEmpty(countryData.getString("Serious_Critical"))) {
                        _critical.setText("0");
                    } else _critical.setText(countryData.getString("Serious_Critical"));

                } catch (JSONException e) {
                    Toast.makeText(getActivity(), "Something Error Inform to Developers", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Something Error in API..!!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }


}
