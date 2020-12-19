package com.epayeats.epayeatsadmin.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.epayeats.epayeatsadmin.Activity.AddFoodCatagory_Activity;
import com.epayeats.epayeatsadmin.Adapter.FoodCatagoryAdapter;
import com.epayeats.epayeatsadmin.Model.FoodCatagoryModel;
import com.epayeats.epayeatsadmin.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class FoodCatagory_Fragment extends Fragment
{

    SearchView fragment_foodcatagory_searchView;
    SwipeRefreshLayout refresh_foodCatagory;
    ListView fragment_foodcatagory_listview;
    FloatingActionsMenu foodCatagoryFragmentFloatingMenu;
    FloatingActionButton fab_foodCatagoryFragmentAdd;

    DatabaseReference mFoodCatagoryReference;
    List<FoodCatagoryModel> mFoodCatagoryModel;
    FoodCatagoryAdapter mFoodCatagoryAdapter;

    public FoodCatagory_Fragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_catagory_, container, false);

        try {
            fragment_foodcatagory_searchView = view.findViewById(R.id.fragment_foodcatagory_searchView);
            refresh_foodCatagory = view.findViewById(R.id.refresh_foodCatagory);
            fragment_foodcatagory_listview = view.findViewById(R.id.fragment_foodcatagory_listview);
            foodCatagoryFragmentFloatingMenu = view.findViewById(R.id.foodCatagoryFragmentFloatingMenu);
            fab_foodCatagoryFragmentAdd = view.findViewById(R.id.fab_foodCatagoryFragmentAdd);

            mFoodCatagoryReference = FirebaseDatabase.getInstance().getReference("main_catagory");

            loadData();

            refresh_foodCatagory.setRefreshing(true);
            refresh_foodCatagory.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadData();
                }
            });

            fab_foodCatagoryFragmentAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    foodCatagoryFragmentFloatingMenu.collapse();
                    addFoodCatagorey();
                }
            });

            fragment_foodcatagory_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchFoodCatagory(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    searchFoodCatagory(newText);
                    return false;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void addFoodCatagorey()
    {
        Intent intent = new Intent(getContext(), AddFoodCatagory_Activity.class);
        startActivity(intent);
    }

    private void searchFoodCatagory(String query)
    {
        try {
            ArrayList<FoodCatagoryModel> myList = new ArrayList<>();
            for (FoodCatagoryModel obj : mFoodCatagoryModel) {
                if (obj.getFoodCatagoreyType().toLowerCase().contains(query.toLowerCase())) {
                    myList.add(obj);
                }
            }
            FoodCatagoryAdapter foodCatagoryAdapter = new FoodCatagoryAdapter(getContext(), myList);
            fragment_foodcatagory_listview.setAdapter(foodCatagoryAdapter);
            fragment_foodcatagory_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
            mFoodCatagoryReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    refresh_foodCatagory.setRefreshing(false);
                    mFoodCatagoryModel.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        FoodCatagoryModel model = snapshot1.getValue(FoodCatagoryModel.class);
                        mFoodCatagoryModel.add(model);
                    }
                    mFoodCatagoryAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            mFoodCatagoryModel = new ArrayList<>();
            mFoodCatagoryAdapter = new FoodCatagoryAdapter(getContext(), mFoodCatagoryModel);
            fragment_foodcatagory_listview.setAdapter(mFoodCatagoryAdapter);
            fragment_foodcatagory_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    editFoodCatagorey(position);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editFoodCatagorey(int position)
    {
        try {
            String[] items = {"Edit", "Delete"};
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle("Select Options");
            dialog.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        LayoutInflater li = LayoutInflater.from(getContext());
                        View food = li.inflate(R.layout.new_foodcatagorey_layout, null);
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                        alertBuilder.setView(food);

                        final EditText foodcatagorey;
                        final Button save;
                        foodcatagorey = food.findViewById(R.id.new_foodcatagorey_editext);
                        save = food.findViewById(R.id.new_foodcatagorey_save);

                        foodcatagorey.setText(mFoodCatagoryModel.get(position).getFoodCatagoreyType());

                        final AlertDialog alertDialog = alertBuilder.create();

                        save.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String a1;
                                a1 = foodcatagorey.getText().toString();
                                if (a1.isEmpty() || a1.equals(" ")) {
                                    foodcatagorey.setError("Required");
                                } else {
                                    DatabaseReference database = FirebaseDatabase.getInstance().getReference("main_catagory").child(mFoodCatagoryModel.get(position).getFoodCatagoreyID());
                                    database.child("foodCatagoreyType").setValue(a1);

                                    Toast.makeText(getContext(), "Updated Sucessfully", Toast.LENGTH_SHORT).show();
                                    alertDialog.cancel();
                                }
                            }
                        });
                        alertDialog.show();
                    }
                    if (which == 1) {
                        try {
                            SweetAlertDialog dialog1 = new SweetAlertDialog(Objects.requireNonNull(getContext()), SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Are you sure?")
                                    .setContentText("Won't be able to recover!")
                                    .setConfirmText("Yes, delete it!")
                                    .setCancelText("No, cancel please")
                                    .showCancelButton(true)
                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.cancel();
                                        }
                                    })
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            sweetAlertDialog.setTitleText("Deleted")
                                                    .setContentText("Data has been deleted")
                                                    .setConfirmText("OK")
                                                    .setConfirmClickListener(null)
                                                    .showCancelButton(false)
                                                    .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            database.getReference("main_catagory").child(mFoodCatagoryModel.get(position).getFoodCatagoreyID()).removeValue();
                                        }
                                    });
                            dialog1.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            dialog.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}