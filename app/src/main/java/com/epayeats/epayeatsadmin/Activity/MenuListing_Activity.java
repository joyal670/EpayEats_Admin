package com.epayeats.epayeatsadmin.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.epayeats.epayeatsadmin.Adapter.MenuListAdapter;
import com.epayeats.epayeatsadmin.Model.MenuModel;
import com.epayeats.epayeatsadmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MenuListing_Activity extends AppCompatActivity
{
    ListView allmenu_listview;
    List<MenuModel> menuModel;
    MenuListAdapter menuListAdapter;

    public ProgressDialog progressDialog;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_listing_);

        try {
            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");

            allmenu_listview = findViewById(R.id.allmenu_listview);
            reference = FirebaseDatabase.getInstance().getReference("menu");

            load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void load()
    {
        try {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    menuModel.clear();
                    progressDialog.dismiss();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        MenuModel model = snapshot1.getValue(MenuModel.class);
                        menuModel.add(model);
                    }
                    menuListAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressDialog.dismiss();
                    Toast.makeText(MenuListing_Activity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            menuModel = new ArrayList<>();
            menuListAdapter = new MenuListAdapter(this, menuModel);
            allmenu_listview.setAdapter(menuListAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}