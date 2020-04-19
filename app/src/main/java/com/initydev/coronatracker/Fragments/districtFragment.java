package com.initydev.coronatracker.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.initydev.coronatracker.Adapters.districtAdapter;
import com.initydev.coronatracker.Models.modelDistrict;
import com.initydev.coronatracker.R;
import com.roger.catloadinglibrary.CatLoadingView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class districtFragment extends Fragment {
    public String state;
    private RequestQueue queue;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    List<modelDistrict> districtList;
    districtAdapter districtAdapter;
    modelDistrict modelDistrict;
    CatLoadingView LoadScreen;
    public SwipeRefreshLayout swipeRefreshLayout;

    public districtFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_district, container, false);
        //getting state value from the
        state = this.getArguments().getString("state");
        queue = Volley.newRequestQueue(getActivity());

        LoadScreen = new CatLoadingView();
        LoadScreen.setCanceledOnTouchOutside(false);
        getActivity().setTitle("Corona Tracker - " + state + " Districts");

        swipeRefreshLayout = view.findViewById(R.id.districtFragment);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);

        recyclerView = view.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        districtList = new ArrayList<>();
        districtAdapter = new districtAdapter(getContext(), districtList);
        recyclerView.setAdapter(districtAdapter);


        getDistrictData();
        ScreenSwipeDown();

        return view;
    }


    private void ScreenSwipeDown() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDistrictData();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }


    public void getDistrictData() {
        String url = "https://api.covid19india.org/state_district_wise.json";
        LoadScreen.show(getFragmentManager(), "");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    //JSONObject disctricts = response.getJSONObject(state).getJSONObject("districtData");
                    JSONObject disctricts = response.getJSONObject(state);
                    //Log.d("data", String.valueOf(disctricts));
                    String name;
                    String cases;

                    Iterator iterator = disctricts.getJSONObject("districtData").keys();
                    while (iterator.hasNext()) {
                        name = (String) iterator.next();
                        cases = disctricts.getJSONObject("districtData").getJSONObject(name).getString("confirmed");
                        //Log.d("name",name +" " + cases);
                        modelDistrict = new modelDistrict(name, cases);
                        districtList.add(modelDistrict);
                        districtAdapter.notifyDataSetChanged();
                    }

                    LoadScreen.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Something Error", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Something Error", Toast.LENGTH_SHORT).show();
                getDistrictData();
            }
        });
        queue.add(request);
    }


}
