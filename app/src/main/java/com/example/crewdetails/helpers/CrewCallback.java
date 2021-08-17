package com.example.crewdetails.helpers;

import android.widget.ImageView;
import android.widget.TextView;

public interface CrewCallback {

    void onItemClick(int pos,
                     TextView name,
                     TextView agency,
                     TextView wiki,
                     TextView status);

}
