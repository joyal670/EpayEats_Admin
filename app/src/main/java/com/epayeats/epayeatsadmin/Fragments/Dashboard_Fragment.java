package com.epayeats.epayeatsadmin.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.epayeats.epayeatsadmin.Activity.DeliveryboyReport_Activity;
import com.epayeats.epayeatsadmin.Activity.LocalAdminReport_Activity;
import com.epayeats.epayeatsadmin.Activity.MenuListing_Activity;
import com.epayeats.epayeatsadmin.Activity.PendingOrders_Activity;
import com.epayeats.epayeatsadmin.Activity.RestaurantReport_Activity;
import com.epayeats.epayeatsadmin.Activity.SubCatagory_Activity;
import com.epayeats.epayeatsadmin.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;


public class Dashboard_Fragment extends Fragment
{
    TextView total_sale, deliverd_items, pending_items, cancelled_items, neworders_items;

    int count0 = 0;
    int count1 = 0;
    int count2 = 0;
    int count3 = 0;
    int countnew = 0;

    String status0 = "0";
    String status1 = "1";
    String status2 = "2";
    String status3 = "3";

    DatabaseReference databaseReference;

    Button local_admin_report_btn, deliveryboy_report_btn, restaurant_report_btn, All_menu_btn, sub_catagory_btn, dashboard_pending_btn;

    SwipeRefreshLayout refresh_dashboard;

    PieChart pieChart;

    public ProgressDialog progressDialog;

    int refcnt = 0;

    public Dashboard_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        try {
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");

            total_sale = view.findViewById(R.id.total_sale);
            deliverd_items = view.findViewById(R.id.deliverd_items);
            pending_items = view.findViewById(R.id.pending_items);
            cancelled_items = view.findViewById(R.id.cancelled_items);

            local_admin_report_btn = view.findViewById(R.id.local_admin_report_btn);
            deliveryboy_report_btn = view.findViewById(R.id.deliveryboy_report_btn);
            restaurant_report_btn = view.findViewById(R.id.restaurant_report_btn);
            All_menu_btn = view.findViewById(R.id.All_menu_btn);
            sub_catagory_btn = view.findViewById(R.id.sub_catagory_btn);
            refresh_dashboard = view.findViewById(R.id.refresh_dashboard);
            pieChart = view.findViewById(R.id.piechart);
            dashboard_pending_btn = view.findViewById(R.id.dashboard_pending_btn);
            neworders_items = view.findViewById(R.id.neworders_items);

            refresh_dashboard.setRefreshing(true);
            refresh_dashboard.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refcnt = refcnt + 1;
                    refresh_dashboard.setRefreshing(false);

                    if (refcnt <= 1) {
                        pieChart.addPieSlice(
                                new PieModel(
                                        "Cancelled",
                                        count3,
                                        Color.parseColor("#EF5350")));
                        pieChart.addPieSlice(
                                new PieModel(
                                        "Delivered",
                                        count2,
                                        Color.parseColor("#66BB6A")));

                        pieChart.addPieSlice(
                                new PieModel(
                                        "Total Sale",
                                        count0,
                                        Color.parseColor("#29B6F6")));
                        pieChart.addPieSlice(
                                new PieModel(
                                        "Pending",
                                        count1,
                                        Color.parseColor("#FFA726")));

                        pieChart.startAnimation();
                    }
                }
            });

            databaseReference = FirebaseDatabase.getInstance().getReference("order_data");

            load();

            local_admin_report_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), LocalAdminReport_Activity.class);
                    startActivity(intent);
                }
            });

            deliveryboy_report_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), DeliveryboyReport_Activity.class);
                    startActivity(intent);
                }
            });

            restaurant_report_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), RestaurantReport_Activity.class);
                    startActivity(intent);
                }
            });

            All_menu_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), MenuListing_Activity.class);
                    startActivity(intent);
                }
            });

            sub_catagory_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), SubCatagory_Activity.class);
                    startActivity(intent);
                }
            });

            dashboard_pending_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), PendingOrders_Activity.class);
                    startActivity(intent);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void load()
    {
        try {
            refresh_dashboard.setRefreshing(false);
            loadAllOrders();
            loadDeliverdOrders();
            loadCancelledOrders();
            loadPendingOrders();
            loadnewOrders();

            pieChart.addPieSlice(
                    new PieModel(
                            "Cancelled",
                            count3,
                            Color.parseColor("#EF5350")));
            pieChart.addPieSlice(
                    new PieModel(
                            "Delivered",
                            count2,
                            Color.parseColor("#66BB6A")));

            pieChart.addPieSlice(
                    new PieModel(
                            "Total Sale",
                            count0,
                            Color.parseColor("#29B6F6")));
            pieChart.addPieSlice(
                    new PieModel(
                            "Pending",
                            count1,
                            Color.parseColor("#FFA726")));

            pieChart.startAnimation();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadnewOrders()
    {
        progressDialog.show();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                {
                    if (status0.equals(dataSnapshot1.child("orderStatus").getValue().toString())) {
                        try {
                            countnew = countnew + 1;
                            neworders_items.setText(countnew + "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadAllOrders()
    {
        try {
            progressDialog.show();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    progressDialog.dismiss();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        try {
                            count0 = count0 + 1;
                            total_sale.setText(count0 + "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDeliverdOrders()
    {
        try {
            progressDialog.show();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progressDialog.dismiss();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if (status2.equals(dataSnapshot1.child("orderStatus").getValue().toString())) {
                            try {
                                count2 = count2 + 1;
                                deliverd_items.setText(count2 + "");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCancelledOrders()
    {
        try {
            progressDialog.show();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progressDialog.dismiss();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        if (status3.equals(dataSnapshot1.child("orderStatus").getValue().toString())) {
                            try {
                                count3 = count3 + 1;
                                cancelled_items.setText(count3 + "");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPendingOrders()
    {
        try {
            progressDialog.show();
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progressDialog.dismiss();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        if (status1.equals(dataSnapshot1.child("orderStatus").getValue().toString())) {
                            try {
                                count1 = count1 + 1;
                                pending_items.setText(count1 + "");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}