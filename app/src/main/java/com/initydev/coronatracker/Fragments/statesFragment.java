package com.initydev.coronatracker.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.initydev.coronatracker.Adapters.stateAdapter;
import com.initydev.coronatracker.Models.modelState;
import com.initydev.coronatracker.R;
import com.initydev.coronatracker.stateClickInterface;
import com.roger.catloadinglibrary.CatLoadingView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class statesFragment extends Fragment implements stateClickInterface {
    CatLoadingView LoadScreen;
    String UserCountry;
    RecyclerView recyclerView;
    public SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager linearLayoutManager;
    List<modelState> stateList;
    //RecyclerView.Adapter stateAdapter;
    stateAdapter stateAdapter;
    private RequestQueue queue;
    ImageView notfoundImage;
    modelState modelState;

    public statesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LoadScreen = new CatLoadingView();
        LoadScreen.setCanceledOnTouchOutside(false);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_states, container, false);
        swipeRefreshLayout = view.findViewById(R.id.StateFragment);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        TelephonyManager telephoneManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String countryCode = telephoneManager.getNetworkCountryIso();
        Locale loc = new Locale("", countryCode);
        UserCountry = loc.getDisplayCountry();
        queue = Volley.newRequestQueue(getActivity());
        getActivity().setTitle("Corona Tracker - States");

        //Hooks
        recyclerView = view.findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        stateList = new ArrayList<>();
        stateAdapter = new stateAdapter(getContext(), stateList, this);
        recyclerView.setAdapter(stateAdapter);
        if (UserCountry.equalsIgnoreCase("india")) {
            getStateData();
            ScreenSwipeDown();
        } else {
            recyclerView.setVisibility(View.INVISIBLE);
            CFAlertDialog.Builder builder = new CFAlertDialog.Builder(view.getContext());
            builder.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
            builder.setTitle("Sorry..!! States Data Not Available for " + UserCountry);
            builder.setCancelable(false);
            builder.setMessage("Currently States Data Available only for India.\n Please Check Rest of the Statistics\n" +
                    "\n This Screen Will Close Now \n Thank You");
            builder.setAutoDismissAfter(6000);
            builder.show();
            Toast.makeText(getContext(), "States Data Not Available for " + UserCountry, Toast.LENGTH_SHORT).show();
        }

        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                stateAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }


    private void ScreenSwipeDown() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getStateData();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }


    public void getStateData() {
        LoadScreen.show(getFragmentManager(), "");

        String url = "https://api.covid19india.org/data.json";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray states = response.getJSONArray("statewise");
                    //JSONObject s = st.getJSONObject(0);
                    for (int i = 1; i < states.length(); i++) {

                        modelState = new modelState(
                                states.getJSONObject(i).getString("active"),
                                states.getJSONObject(i).getString("confirmed"),
                                states.getJSONObject(i).getString("deaths"),
                                states.getJSONObject(i).getString("recovered"),
                                states.getJSONObject(i).getString("state")
                        );
                        stateList.add(modelState);
                        stateAdapter.notifyDataSetChanged();
                    }
                    LoadScreen.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    getStateData();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });

        queue.add(request);

    }

    @Override
    public void onItemClick(int position) {
        String stateName = stateList.get(position).getState();
        swapFragment(stateName);

    }

    private void swapFragment(String stateName) {
        Bundle bundle = new Bundle();
        bundle.putString("state", stateName); // set your parameteres

        districtFragment nextFragment = new districtFragment();
        nextFragment.setArguments(bundle);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_layout, nextFragment).commit();
    }
}