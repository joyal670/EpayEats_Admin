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

import com.epayeats.epayeatsadmin.Adapter.DeliveryBoyAdapter;
import com.epayeats.epayeatsadmin.Adapter.loaclAdminListViewAdapter;
import com.epayeats.epayeatsadmin.Model.AdminModel;
import com.epayeats.epayeatsadmin.Model.DeliveryBoyModel;
import com.epayeats.epayeatsadmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DeliveryboyReport_Activity extends AppCompatActivity
{
    ListView deliveryboy_report_listview;
    List<DeliveryBoyModel> deliveryBoyModel;
    DeliveryBoyAdapter deliveryBoyAdapter;
    DatabaseReference reference;
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deliveryboy_report_);

        try {
            deliveryboy_report_listview = findViewById(R.id.deliveryboy_report_listview);

            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");


            progressDialog.show();
            reference = FirebaseDatabase.getInstance().getReference("delivery_boy");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    deliveryBoyModel.clear();
                    progressDialog.dismiss();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        DeliveryBoyModel model = snapshot1.getValue(DeliveryBoyModel.class);
                        deliveryBoyModel.add(model);
                    }
                    deliveryBoyAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressDialog.dismiss();
                    Toast.makeText(DeliveryboyReport_Activity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            deliveryBoyModel = new ArrayList<>();
            deliveryBoyAdapter = new DeliveryBoyAdapter(this, deliveryBoyModel);
            deliveryboy_report_listview.setAdapter(deliveryBoyAdapter);
            deliveryboy_report_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(DeliveryboyReport_Activity.this, deliveryBoyReportdetails_Activity.class);
                    intent.putExtra("id", deliveryBoyModel.get(position).getDeliveryBoyID());
                    intent.putExtra("name", deliveryBoyModel.get(position).getDeliveryBoyName());
                    intent.putExtra("charge", deliveryBoyModel.get(position).getDeliveryBoyDeliveryCharge());
                    intent.putExtra("phone", deliveryBoyModel.get(position).getDeliveyBoyMobileNo());
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}