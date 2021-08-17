package com.example.crewdetails.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.crewdetails.R;
import com.example.crewdetails.database.MainData;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {


    ImageView crewImage;
    TextView tvName, tvAgency, tvWiki, tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        MainData data = (MainData) getIntent().getExtras().getSerializable("bookObject");

        crewImage = findViewById(R.id.crewImage);
        tvName = findViewById(R.id.tvName);
        tvAgency = findViewById(R.id.tvAgency);
        tvWiki = findViewById(R.id.tvWiki);
        tvStatus = findViewById(R.id.tvStatus);

        //Setting Values
        String status = "Currently "+data.getStatus();
        String wikiLink = data.getWikipedia();
        if (wikiLink.length() > 36) wikiLink = wikiLink.substring(0, 36) + "...";

        tvName.setText(data.getName());
        tvAgency.setText(data.getAgency());
        tvWiki.setText(data.getWikipedia());
        tvStatus.setText(status);
        tvWiki.setText(wikiLink);
        Picasso.get()
                .load(data.getImage())
                .fit()
                .into(crewImage);

        tvWiki.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(data.getWikipedia()));
            startActivity(intent);
        });
    }

}