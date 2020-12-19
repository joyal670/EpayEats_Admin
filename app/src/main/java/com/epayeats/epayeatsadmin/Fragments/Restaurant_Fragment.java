package com.epayeats.epayeatsadmin.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.epayeats.epayeatsadmin.Activity.RestaurantReport_Activity;
import com.epayeats.epayeatsadmin.Adapter.BusinessAdapter;
import com.epayeats.epayeatsadmin.Adapter.DeliveryBoyAdapter;
import com.epayeats.epayeatsadmin.Adapter.RestaurantAdapter;
import com.epayeats.epayeatsadmin.Model.BusinessModel;
import com.epayeats.epayeatsadmin.Model.DeliveryBoyModel;
import com.epayeats.epayeatsadmin.Model.RestaurantModel;
import com.epayeats.epayeatsadmin.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.ArrayList;
import java.util.List;

public class Restaurant_Fragment extends Fragment {

    ListView listView;
    DatabaseReference reference;
    List<RestaurantModel> restaurantModel;
    RestaurantAdapter restaurantAdapter;

    SearchView fragment_restaurant_searchView;
    SwipeRefreshLayout refresh_restaurants;

    public ProgressDialog progressDialog;


    public Restaurant_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_, container, false);

        try {
            listView = view.findViewById(R.id.fragment_restaurant_listview);
            fragment_restaurant_searchView = view.findViewById(R.id.fragment_restaurant_searchView);
            refresh_restaurants = view.findViewById(R.id.refresh_restaurants);

            reference = FirebaseDatabase.getInstance().getReference("restaurants");

            loadData();

            refresh_restaurants.setRefreshing(true);
            refresh_restaurants.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadData();
                }
            });

            fragment_restaurant_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchRest(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    searchRest(newText);
                    return false;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void searchRest(String query)
    {
        try {
            ArrayList<RestaurantModel> myList = new ArrayList<>();
            for (RestaurantModel obj : restaurantModel) {
                if (obj.getResName().toLowerCase().contains(query.toLowerCase())) {
                    myList.add(obj);
                }
            }
            RestaurantAdapter deliveryBoyAdapter = new RestaurantAdapter(getContext(), myList);
            listView.setAdapter(deliveryBoyAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData()
    {
        try {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    refresh_restaurants.setRefreshing(false);
                    restaurantModel.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        RestaurantModel model = snapshot1.getValue(RestaurantModel.class);
                        restaurantModel.add(model);
                    }
                    restaurantAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            restaurantModel = new ArrayList<>();
            restaurantAdapter = new RestaurantAdapter(getContext(), restaurantModel);
            listView.setAdapter(restaurantAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    callRestaurant(position);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callRestaurant(int position)
    {
        try {
            String[] items = {"Call"};
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle("Select Options");
            dialog.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        Permissions.check(getContext(), Manifest.permission.CALL_PHONE, null, new PermissionHandler() {
                            @Override
                            public void onGranted() {
                                String number = restaurantModel.get(position).getResPhone();

                                Intent intent = new Intent(Intent.ACTION_CALL);
                                if (number.isEmpty()) {
                                    Toast.makeText(getContext(), "Unable To make Call", Toast.LENGTH_SHORT).show();
                                } else {
                                    intent.setData(Uri.parse("tel:" + number));
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                                super.onDenied(context, deniedPermissions);
                            }

                            @Override
                            public boolean onBlocked(Context context, ArrayList<String> blockedList) {
                                Toast.makeText(getContext(), "Permission blocked", Toast.LENGTH_SHORT).show();
                                return super.onBlocked(context, blockedList);
                            }
                        });
                    }

                }
            });
            dialog.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}