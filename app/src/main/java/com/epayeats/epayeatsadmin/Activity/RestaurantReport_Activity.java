package com.epayeats.epayeatsadmin.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.epayeats.epayeatsadmin.Adapter.RestaurantAdapter;
import com.epayeats.epayeatsadmin.Adapter.loaclAdminListViewAdapter;
import com.epayeats.epayeatsadmin.Model.AdminModel;
import com.epayeats.epayeatsadmin.Model.RestaurantModel;
import com.epayeats.epayeatsadmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RestaurantReport_Activity extends AppCompatActivity
{
    ListView restaurant_report_listview;
    List<RestaurantModel> restaurantModel;
    RestaurantAdapter restaurantAdapter;
    DatabaseReference reference;

    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_report_);

        try {

            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");

            restaurant_report_listview = findViewById(R.id.restaurant_report_listview);

            progressDialog.show();
            reference = FirebaseDatabase.getInstance().getReference("restaurants");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    restaurantModel.clear();
                    progressDialog.dismiss();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        RestaurantModel model = snapshot1.getValue(RestaurantModel.class);
                        restaurantModel.add(model);
                    }
                    restaurantAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressDialog.dismiss();
                    Toast.makeText(RestaurantReport_Activity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            restaurantModel = new ArrayList<>();
            restaurantAdapter = new RestaurantAdapter(this, restaurantModel);
            restaurant_report_listview.setAdapter(restaurantAdapter);
            restaurant_report_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(RestaurantReport_Activity.this, resaurantReportDetails_Activity.class);
                    intent.putExtra("id", restaurantModel.get(position).getResID());
                    intent.putExtra("name", restaurantModel.get(position).getResName());
                    intent.putExtra("phone", restaurantModel.get(position).getResPhone());
                    intent.putExtra("localadmin", restaurantModel.get(position).getResLocalAdminName());
                    intent.putExtra("location", restaurantModel.get(position).getResLocation());
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}