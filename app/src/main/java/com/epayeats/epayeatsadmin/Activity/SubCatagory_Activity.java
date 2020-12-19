package com.epayeats.epayeatsadmin.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.epayeats.epayeatsadmin.Adapter.SubCatagoryAdapter;
import com.epayeats.epayeatsadmin.Model.SubCatagoryModel;
import com.epayeats.epayeatsadmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SubCatagory_Activity extends AppCompatActivity
{
    ListView subcatagory_listview;
    DatabaseReference reference;
    List<SubCatagoryModel> subCatagoryModel;
    SubCatagoryAdapter mSubCatagoryAdapter;

    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_catagory_);

        try {
            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");

            subcatagory_listview = findViewById(R.id.subcatagory_listview);
            reference = FirebaseDatabase.getInstance().getReference("sub_category");

            load();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void load()
    {
        try {
            progressDialog.show();
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    subCatagoryModel.clear();
                    progressDialog.dismiss();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        SubCatagoryModel model = snapshot1.getValue(SubCatagoryModel.class);
                        subCatagoryModel.add(model);
                    }
                    mSubCatagoryAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressDialog.dismiss();
                    Toast.makeText(SubCatagory_Activity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            subCatagoryModel = new ArrayList<>();
            mSubCatagoryAdapter = new SubCatagoryAdapter(this, subCatagoryModel);
            subcatagory_listview.setAdapter(mSubCatagoryAdapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}