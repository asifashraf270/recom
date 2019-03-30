package com.glowingsoft.Recomendados.Seller.ActivitiesSeller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.glowingsoft.Recomendados.Buyer.Adapter.DetailPagerAdaper;
import com.glowingsoft.Recomendados.Buyer.Adapter.ItemRecyclerViewAdapter;
import com.glowingsoft.Recomendados.R;

public class AddProductActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ItemRecyclerViewAdapter recyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        viewBinding();
    }

    private void viewBinding() {
//        recyclerView = findViewById(R.id.recyclerview);
//        recyclerViewAdapter = new ItemRecyclerViewAdapter();
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        recyclerView.setAdapter(recyclerViewAdapter);
//        recyclerView.setHasFixedSize(true);


    }
}
