package com.epayeats.epayeatsadmin.Fragments;

import android.app.ProgressDialog;
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

import com.epayeats.epayeatsadmin.Activity.AddBusinessSearch_Activity;
import com.epayeats.epayeatsadmin.Adapter.BusinessAdapter;
import com.epayeats.epayeatsadmin.Maps.Business_MapsActivity;
import com.epayeats.epayeatsadmin.Model.BusinessModel;
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


public class Business_Fragment extends Fragment
{
    ListView listview_business;
    DatabaseReference mBusinessDatabaseReference;
    List<BusinessModel> mBusinessModel;
    BusinessAdapter mBusinessAdapter;

    FloatingActionsMenu BusinessFragmentFloatingMenu;
    FloatingActionButton fab_BusinessFragmentAdd, fab_BusinessFragmentAddAutoComplete;

    SearchView fragment_business_searchView;
    SwipeRefreshLayout refresh_business;

    public ProgressDialog progressDialog;

    public Business_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_business_, container, false);

        try {
            listview_business = view.findViewById(R.id.fragment_business_listview);
            BusinessFragmentFloatingMenu = view.findViewById(R.id.BusinessFragmentFloatingMenu);
            fab_BusinessFragmentAdd = view.findViewById(R.id.fab_BusinessFragmentAdd);
            fab_BusinessFragmentAddAutoComplete = view.findViewById(R.id.fab_BusinessFragmentAddAutoComplete);
            fragment_business_searchView = view.findViewById(R.id.fragment_business_searchView);
            refresh_business = view.findViewById(R.id.refresh_business);

            mBusinessDatabaseReference = FirebaseDatabase.getInstance().getReference("business");

            progressDialog = new ProgressDialog(getContext());
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");

            loadData();

            refresh_business.setRefreshing(true);

            refresh_business.setOnRefreshListener(() -> loadData());

            fab_BusinessFragmentAdd.setOnClickListener(v -> {
                BusinessFragmentFloatingMenu.collapse();
                addBusiness();
            });

            fragment_business_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchBusiness(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    searchBusiness(newText);
                    return false;
                }
            });

            fab_BusinessFragmentAddAutoComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(getContext(), AddBusinessSearch_Activity.class);
                    startActivity(intent);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void loadData()
    {
        try {
            mBusinessDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    refresh_business.setRefreshing(false);
                    mBusinessModel.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        BusinessModel model = snapshot1.getValue(BusinessModel.class);
                        mBusinessModel.add(model);

                    }
                    mBusinessAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            mBusinessModel = new ArrayList<>();
            mBusinessAdapter = new BusinessAdapter(getContext(), mBusinessModel);
            listview_business.setAdapter(mBusinessAdapter);
            listview_business.setOnItemClickListener((parent, view, position, id) -> editLocation(position));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editLocation(int position)
    {
        try {
            String[] items = {"Rename", "Delete"};
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle("Select Options");
            dialog.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        LayoutInflater li = LayoutInflater.from(getContext());
                        View myBusiness = li.inflate(R.layout.new_business_layout, null);
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                        alertBuilder.setView(myBusiness);

                        final EditText new_business_location_editext;
                        final Button new_business_location_button;

                        new_business_location_editext = myBusiness.findViewById(R.id.new_business_location_editext);
                        new_business_location_button = myBusiness.findViewById(R.id.new_business_location_button);

                        new_business_location_editext.setText(mBusinessModel.get(position).getLocation());

                        final AlertDialog alertDialog = alertBuilder.create();

                        new_business_location_button.setOnClickListener(v -> {
                            String loc = new_business_location_editext.getText().toString();

                            if (loc.isEmpty() || loc.equals(" ")) {
                                new_business_location_editext.setError("Enter Location");
                            } else {
                                progressDialog.show();
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("business").child(mBusinessModel.get(position).getBusinessId());
                                reference.child("location").setValue(loc);
                                Toast.makeText(getContext(), "Location Updated", Toast.LENGTH_SHORT).show();
                                alertDialog.cancel();
                                progressDialog.dismiss();
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
                                    .setCancelClickListener(sweetAlertDialog -> sweetAlertDialog.cancel())
                                    .setConfirmClickListener(sweetAlertDialog -> {
                                        sweetAlertDialog.setTitleText("Deleted")
                                                .setContentText("Data has been deleted")
                                                .setConfirmText("OK")
                                                .setConfirmClickListener(null)
                                                .showCancelButton(false)
                                                .changeAlertType(SweetAlertDialog.SUCCESS_TYPE);

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        database.getReference("business").child(mBusinessModel.get(position).getBusinessId()).removeValue();
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

    private void searchBusiness(String query)
    {
        try {
            ArrayList<BusinessModel> myList = new ArrayList<>();
            for (BusinessModel obj : mBusinessModel) {
                if (obj.getLocation().toLowerCase().contains(query.toLowerCase())) {
                    myList.add(obj);
                }
            }
            BusinessAdapter adapter = new BusinessAdapter(getContext(), myList);
            listview_business.setAdapter(adapter);
            listview_business.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addBusiness()
    {
        Intent intent = new Intent(getContext(), Business_MapsActivity.class);
        startActivity(intent);
    }
}