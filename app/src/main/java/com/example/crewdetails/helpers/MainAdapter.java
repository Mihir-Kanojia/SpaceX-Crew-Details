package com.example.crewdetails.helpers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.crewdetails.database.MainData;
import com.example.crewdetails.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private onItemClickListener listener;
    private final List<MainData> dataList;
    private final Activity context;

    CrewCallback callback;

    public MainAdapter(Activity context, List<MainData> dataList, CrewCallback callback) {
        this.context = context;
        this.dataList = dataList;
        this.callback = callback;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //Initialize view
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_crew_details, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MainData data = dataList.get(position);
//        RoomDB database = RoomDB.getInstance(context);

        String status = data.getStatus().substring(0, 1).toUpperCase() + data.getStatus().substring(1);
        String wikiLink = data.getWikipedia();

        //Trim wiki link
        if (wikiLink.length() > 36) wikiLink = wikiLink.substring(0, 36) + "...";

        holder.cName.setText(data.getName());
        holder.cStatus.setText(status);
        holder.cAgency.setText(data.getAgency());
        holder.wikipediaLink.setText(wikiLink);

        //Image processing
        Picasso.get()
                .load(data.getImage())
                .centerCrop()
                .fit()
                .into(holder.ivCrew);

        //Opened Wiki link
        holder.wikipediaLink.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(data.getWikipedia()));
            v.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cName, cStatus, cAgency, wikipediaLink;
        ImageView ivCrew;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cName = itemView.findViewById(R.id.tvName);
            cStatus = itemView.findViewById(R.id.tvStatus);
            cAgency = itemView.findViewById(R.id.tvAgency);
            wikipediaLink = itemView.findViewById(R.id.tvWiki);
            ivCrew = itemView.findViewById(R.id.crewImage);

            itemView.setOnClickListener(v -> {
                callback.onItemClick(getAdapterPosition(),
                        cName,
                        cAgency,
                        wikipediaLink,
                        cStatus);
            });

        }
    }

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }


}

