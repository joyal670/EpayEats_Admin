package com.epayeats.epayeatsadmin.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.epayeats.epayeatsadmin.Adapter.OrderAdapter;
import com.epayeats.epayeatsadmin.Model.orderModel;
import com.epayeats.epayeatsadmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;
import java.util.List;

public class PendingOrders_Activity extends AppCompatActivity implements OrderAdapter.OnitemClickListener
{
    DatabaseReference reference;
    List<orderModel> orderModel;
    OrderAdapter orderAdapter;
    RecyclerView AllOrdersRecyclerView;

    public ProgressDialog progressDialog;

    String status = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_orders_);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading Orders...");

        AllOrdersRecyclerView = findViewById(R.id.AllOrdersRecyclerView);
        AllOrdersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        AllOrdersRecyclerView.setHasFixedSize(true);

        loadOrders();


    }

    private void loadOrders()
    {
        progressDialog.show();
        reference = FirebaseDatabase.getInstance().getReference("order_data");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                progressDialog.dismiss();
                orderModel.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    if (status.equals(dataSnapshot1.child("orderStatus").getValue().toString()))
                    {
                        orderModel model = dataSnapshot1.getValue(orderModel.class);
                        orderModel.add(model);
                    }

                }
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(PendingOrders_Activity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        orderModel = new ArrayList<>();
        orderAdapter = new OrderAdapter(this, orderModel);
        AllOrdersRecyclerView.setAdapter(orderAdapter);
        orderAdapter.setOnClickListener(this);
    }

    @Override
    public void onItemClick(int position)
    {
        String[] items = {"Call Local Admin"};
        AlertDialog.Builder dialog = new AlertDialog.Builder(PendingOrders_Activity.this);
        dialog.setTitle("Select Options");
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(which == 0)
                {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("local_admin").child(orderModel.get(position).getLocalAdminID());
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {
                            String number = snapshot.child("admnPhone").getValue().toString();
                            Permissions.check(PendingOrders_Activity.this, Manifest.permission.CALL_PHONE, null, new PermissionHandler() {
                                @Override
                                public void onGranted()
                                {
                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    if (number.isEmpty())
                                    {
                                        Toast.makeText(PendingOrders_Activity.this, "Unable To make Call", Toast.LENGTH_SHORT).show();
                                    } else {
                                        intent.setData(Uri.parse("tel:" + number));
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                                    Toast.makeText(PendingOrders_Activity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                                    super.onDenied(context, deniedPermissions);
                                }

                                @Override
                                public boolean onBlocked(Context context, ArrayList<String> blockedList) {
                                    Toast.makeText(PendingOrders_Activity.this, "Permission Blocked", Toast.LENGTH_SHORT).show();
                                    return super.onBlocked(context, blockedList);
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error)
                        {
                            Toast.makeText(PendingOrders_Activity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        dialog.create().show();
    }
}