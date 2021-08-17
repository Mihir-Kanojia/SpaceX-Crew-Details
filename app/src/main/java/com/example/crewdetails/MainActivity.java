package com.example.crewdetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class

MainActivity extends AppCompatActivity {


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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvCrewDetails.setLayoutManager(linearLayoutManager);
        adapter = new MainAdapter(MainActivity.this, dataList);
        rvCrewDetails.setAdapter(adapter);

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
                Log.d("mihir", "Something went wrong: "+error);
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
}