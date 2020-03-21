package com.initydev.coronatracker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.roger.catloadinglibrary.CatLoadingView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class countryFragment extends Fragment {
    RecyclerView recyclerView;
    countryAdapter countryAdapter;
    List<modelCountry> Countrylist;
    modelCountry modelCountry;
    LinearLayoutManager linearLayoutManager;
    CatLoadingView LoadScreen;

    private RequestQueue queue;

    public countryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_country, container, false);
        //Hooks
        recyclerView = view.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        Countrylist = new ArrayList<>();
        countryAdapter = new countryAdapter(getContext(), Countrylist);
        recyclerView.setAdapter(countryAdapter);

        LoadScreen = new CatLoadingView();
        LoadScreen.setCanceledOnTouchOutside(false);

        InternetCheck check = new InternetCheck();
        if (check.isInternetOn(getActivity()) == false) {
            LoadScreen.show(getFragmentManager(), "");
            Toast.makeText(getContext(), "Please Enable Your Internet!", Toast.LENGTH_SHORT).show();

        } else {
            queue = Volley.newRequestQueue(getActivity());
            getCountryData();
        }
        return view;
    }

    public void getCountryData() {
        String url = "https://coronavirus-19-api.herokuapp.com/countries";
        LoadScreen.show(getFragmentManager(), "");
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    // JSONObject jsonObject = new JSONObject(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String country = jsonObject.getString("cases");
                        Log.d("Case", String.valueOf(Double.valueOf(country)));
                        modelCountry = new modelCountry(
                                jsonObject.getString("country"),
                                jsonObject.getString("cases"),
                                jsonObject.getString("todayCases"),
                                jsonObject.getString("deaths"),
                                jsonObject.getString("todayDeaths"),
                                jsonObject.getString("recovered"),
                                jsonObject.getString("active"),
                                jsonObject.getString("critical")
                        );
                        Countrylist.add(modelCountry);
                        countryAdapter.notifyDataSetChanged();
                    }
                    LoadScreen.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }

}
