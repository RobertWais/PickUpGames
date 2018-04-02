package com.example.robertwais.pickupgames;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import Adapter.EventAdapter;
import Model.ListItem;

public class EventBoardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_board);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewID);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();
        for(int i = 0; i < 15; i++) {
             ListItem item = new ListItem(
                     "Item" + (i+1),
                     "Description"
             );

             listItems.add(item);
        }

        adapter = new EventAdapter(this, listItems);
        recyclerView.setAdapter(adapter);
    }
}
