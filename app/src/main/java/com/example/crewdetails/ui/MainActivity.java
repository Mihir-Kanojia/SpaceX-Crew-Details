package com.example.crewdetails.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.crewdetails.helpers.CrewCallback;
import com.example.crewdetails.helpers.MainAdapter;
import com.example.crewdetails.database.MainData;
import com.example.crewdetails.R;
import com.example.crewdetails.database.RoomDB;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class

MainActivity extends AppCompatActivity implements CrewCallback {


    List<MainData> dataList = new ArrayList<>();
    RoomDB database;
    MainAdapter adapter;

    RecyclerView rvCrewDetails;
    MaterialButton refreshBtn;
    MaterialButton deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvCrewDetails = findViewById(R.id.rvCrewDetails);
        refreshBtn = findViewById(R.id.refreshBtn);
        deleteBtn = findViewById(R.id.deleteBtn);

        database = RoomDB.getInstance(this);
        dataList = database.mainDao().getAll();

        rvCrewDetails.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MainAdapter(MainActivity.this, dataList, this);
        rvCrewDetails.setAdapter(adapter);

//        adapter.setOnItemClickListener(new MainAdapter.onItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//
//
//            }
//        });

        //Add data
        refreshBtn.setOnClickListener(v -> {

            RequestQueue requestQueue;
            requestQueue = Volley.newRequestQueue(this);

            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET
                    , "https://api.spacexdata.com/v4/crew", null
                    , response -> {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);
                        String name = obj.getString("name");
                        String agency = obj.getString("agency");
                        String image = obj.getString("image");
                        String wikipedia = obj.getString("wikipedia");
                        String status = obj.getString("status");

                        Log.d("mihir", "onResponse: agency is " + obj.getString("agency"));

                        MainData data = new MainData();
                        data.setName(name);
                        data.setAgency(agency);
                        data.setImage(image);
                        data.setStatus(status);
                        data.setWikipedia(wikipedia);

                        database.mainDao().insert(data);
                        dataList.clear();
                        dataList.addAll(database.mainDao().getAll());
                        adapter.notifyDataSetChanged();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }, error -> {
                Log.d("mihir", "Something went wrong: " + error);
            });


            requestQueue.add(jsonArrayRequest);


//            MainData data = new MainData();
//            data.setName("Test0");
//            data.setAgency("Test1");
//            data.setImage("Test1");
//            data.setStatus("Test3");
//            data.setWikipedia("Test4");

//            database.mainDao().insert(data);


        });

        //Delete all data
        deleteBtn.setOnClickListener(v -> {
            database.mainDao().reset(dataList);
            dataList.clear();
            dataList.addAll(database.mainDao().getAll());
            adapter.notifyDataSetChanged();
        });


    }

    @Override
    public void onItemClick(int pos,
                            TextView name,
                            TextView agency,
                            TextView wiki,
                            TextView status) {

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("bookObject", dataList.get(pos));

        //Shared Anim
        Pair<View, String> pair2 = Pair.create((View) name, "name_transition");
        Pair<View, String> pair3 = Pair.create((View) agency, "agency_transition");
        Pair<View, String> pair4 = Pair.create((View) wiki, "wiki_transition");
        Pair<View, String> pair5 = Pair.create((View) status, "status_transition");

        ActivityOptionsCompat optionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this, pair2, pair3, pair4, pair5);

        startActivity(intent, optionsCompat.toBundle());


    }
}