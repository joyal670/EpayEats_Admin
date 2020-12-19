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

import com.epayeats.epayeatsadmin.Adapter.LocaladminAdapter;
import com.epayeats.epayeatsadmin.Adapter.loaclAdminListViewAdapter;
import com.epayeats.epayeatsadmin.Model.AdminModel;
import com.epayeats.epayeatsadmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LocalAdminReport_Activity extends AppCompatActivity
{
    ListView local_admin_listview;
    DatabaseReference databaseReference;
    List<AdminModel> adminModel;
    loaclAdminListViewAdapter loaclAdminListViewAdapter;
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_admin_report_);

        try {

            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");

            local_admin_listview = findViewById(R.id.local_admin_listview);

            progressDialog.show();
            databaseReference = FirebaseDatabase.getInstance().getReference("local_admin");
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    adminModel.clear();
                    progressDialog.dismiss();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        AdminModel model = snapshot1.getValue(AdminModel.class);
                        adminModel.add(model);
                    }
                    loaclAdminListViewAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressDialog.dismiss();
                    Toast.makeText(LocalAdminReport_Activity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            adminModel = new ArrayList<>();
            loaclAdminListViewAdapter = new loaclAdminListViewAdapter(this, adminModel);
            local_admin_listview.setAdapter(loaclAdminListViewAdapter);
            local_admin_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                {
                    Intent intent = new Intent(LocalAdminReport_Activity.this, coAdminRoprtDetais_Activity.class);
                    intent.putExtra("id", adminModel.get(position).getAdmnID());
                    intent.putExtra("name", adminModel.get(position).getAdmnName());
                    intent.putExtra("area", adminModel.get(position).getAdmnBusinessArea());
                    intent.putExtra("km", adminModel.get(position).getAdmnBusinessKM());
                    intent.putExtra("phone", adminModel.get(position).getAdmnPhone());
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}