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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.epayeats.epayeatsadmin.Activity.AddDeliveryBoy_Activity;
import com.epayeats.epayeatsadmin.Adapter.DeliveryBoyAdapter;
import com.epayeats.epayeatsadmin.Model.DeliveryBoyModel;
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
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class DeliveryBoy_Fragment extends Fragment
{
    ListView fragment_deliveryboy_listview;
    List<DeliveryBoyModel> mDeliveryBoyModel;
    DatabaseReference mDeliveryBoyReference;
    DeliveryBoyAdapter mDeliveryBoyAdapter;

    FloatingActionsMenu deliveryBoyFragmentFloatingMenu;
    FloatingActionButton fab_deliveryBoyFragmentAdd;
    SearchView fragment_deliveryBoy_searchView;
    SwipeRefreshLayout refresh_deliveryboy;

    public ProgressDialog progressDialog;

    public DeliveryBoy_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_delivery_boy_, container, false);

        try {
            fragment_deliveryboy_listview = view.findViewById(R.id.fragment_deliveryboy_listview);
            fab_deliveryBoyFragmentAdd = view.findViewById(R.id.fab_deliveryBoyFragmentAdd);
            deliveryBoyFragmentFloatingMenu = view.findViewById(R.id.deliveryBoyFragmentFloatingMenu);
            fragment_deliveryBoy_searchView = view.findViewById(R.id.fragment_deliveryBoy_searchView);
            refresh_deliveryboy = view.findViewById(R.id.refresh_deliveryboy);

            mDeliveryBoyReference = FirebaseDatabase.getInstance().getReference("delivery_boy");

            progressDialog = new ProgressDialog(getContext());
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");

            loadData();

            refresh_deliveryboy.setRefreshing(true);
            refresh_deliveryboy.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadData();
                }
            });


            fab_deliveryBoyFragmentAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deliveryBoyFragmentFloatingMenu.collapse();
                    addDeliveryBoy();
                }
            });

            fragment_deliveryBoy_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchDeliveryBoy(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    searchDeliveryBoy(newText);
                    return false;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void addDeliveryBoy()
    {
        try {
            Intent intent = new Intent(getContext(), AddDeliveryBoy_Activity.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData()
    {
        try {
            mDeliveryBoyReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    refresh_deliveryboy.setRefreshing(false);
                    mDeliveryBoyModel.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        DeliveryBoyModel model = snapshot1.getValue(DeliveryBoyModel.class);
                        mDeliveryBoyModel.add(model);
                    }
                    mDeliveryBoyAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            mDeliveryBoyModel = new ArrayList<>();
            mDeliveryBoyAdapter = new DeliveryBoyAdapter(getContext(), mDeliveryBoyModel);
            fragment_deliveryboy_listview.setAdapter(mDeliveryBoyAdapter);
            fragment_deliveryboy_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    editDeliveryBoy(position);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchDeliveryBoy(String query)
    {
        try {
            ArrayList<DeliveryBoyModel> myList = new ArrayList<>();
            for (DeliveryBoyModel obj : mDeliveryBoyModel) {
                if (obj.getDeliveryBoyName().toLowerCase().contains(query.toLowerCase())) {
                    myList.add(obj);
                }
            }
            DeliveryBoyAdapter deliveryBoyAdapter = new DeliveryBoyAdapter(getContext(), myList);
            fragment_deliveryboy_listview.setAdapter(deliveryBoyAdapter);
            fragment_deliveryboy_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editDeliveryBoy(int position)
    {
        try {
            String[] items = {"Edit","Call", "Delete"};
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle("Select Options");
            dialog.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0)
                    {
                        try {
                            LayoutInflater li = LayoutInflater.from(getContext());
                            View deliveryboy = li.inflate(R.layout.new_deliveryboy_layout, null);
                            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                            alertBuilder.setView(deliveryboy);

                            DatabaseReference adminReference = FirebaseDatabase.getInstance().getReference("local_admin");
                            ArrayList<String> spinnerDatalist;
                            ArrayAdapter<String> adapter;
                            ValueEventListener listener;
                            String msg = "Select --  ";

                            String[] catogries = {"UnBlocked", "Blocked"};
                            ArrayAdapter<String> catog = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, catogries);

                            final EditText name_editext, licence_editext, vechileno_editext, mobileno_editext;
                            final Spinner localadmin_spiner, new_deliveryboy_block_spinner;
                            final Button save_button;

                            name_editext = deliveryboy.findViewById(R.id.new_deliveryboy_name_editext);
                            localadmin_spiner = deliveryboy.findViewById(R.id.new_deliveryboy_localadmin_editext);
                            new_deliveryboy_block_spinner = deliveryboy.findViewById(R.id.new_deliveryboy_block_spinner);
                            save_button = deliveryboy.findViewById(R.id.new_deliveryboy_save_button);
                            licence_editext = deliveryboy.findViewById(R.id.new_deliveryboy_licence_editext);
                            vechileno_editext = deliveryboy.findViewById(R.id.new_deliveryboy_vechileno_editext);
                            mobileno_editext = deliveryboy.findViewById(R.id.new_deliveryboy_mobileno_editext);

                            name_editext.setText(mDeliveryBoyModel.get(position).getDeliveryBoyName());
                            licence_editext.setText(mDeliveryBoyModel.get(position).getDeliveyBoyLicence());
                            vechileno_editext.setText(mDeliveryBoyModel.get(position).getDeliveyBoyVechileNo());
                            mobileno_editext.setText(mDeliveryBoyModel.get(position).getDeliveyBoyMobileNo());

                            try {
                                spinnerDatalist = new ArrayList<>();
                                adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerDatalist);
                                listener = adminReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        spinnerDatalist.clear();
                                        spinnerDatalist.add(mDeliveryBoyModel.get(position).getDeliveryBoyLocalAdminName());
                                        spinnerDatalist.add(msg);
                                        for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                            spinnerDatalist.add(dataSnapshot1.child("admnName").getValue().toString());
                                            localadmin_spiner.setAdapter(adapter);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            new_deliveryboy_block_spinner.setAdapter(catog);

                            final AlertDialog alertDialog = alertBuilder.create();

                            save_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final String ename, alocaladmin, licence, vechileno, mobile, block;
                                    ename = name_editext.getText().toString();
                                    alocaladmin = localadmin_spiner.getSelectedItem().toString();
                                    block = new_deliveryboy_block_spinner.getSelectedItem().toString();
                                    licence = licence_editext.getText().toString();
                                    vechileno = vechileno_editext.getText().toString();
                                    mobile = mobileno_editext.getText().toString();

                                    if (ename.isEmpty() || alocaladmin.isEmpty() || licence.isEmpty() || vechileno.isEmpty() || mobile.isEmpty() || block.isEmpty()) {
                                        if (ename.isEmpty()) {
                                            name_editext.setError("Required");
                                        }
                                        if (licence.isEmpty()) {
                                            licence_editext.setError("Required");
                                        }
                                        if (vechileno.isEmpty()) {
                                            vechileno_editext.setError("Required");
                                        }
                                        if (mobile.isEmpty()) {
                                            mobileno_editext.setError("Required");
                                        }

                                        if (block.isEmpty()) {
                                            Toast.makeText(getContext(), "Select Status", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        if (alocaladmin.equals(msg)) {
                                            Toast.makeText(getContext(), "Select Local Admin", Toast.LENGTH_SHORT).show();
                                        } else {
                                            progressDialog.show();
                                            try {
                                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("delivery_boy").child(mDeliveryBoyModel.get(position).getDeliveryBoyID());
                                                ref.child("deliveryBoyName").setValue(ename);
                                                ref.child("deliveyBoyLicence").setValue(licence);
                                                ref.child("deliveyBoyVechileNo").setValue(vechileno);
                                                ref.child("deliveyBoyMobileNo").setValue(mobile);
                                                ref.child("deliveryBoyLocalAdminName").setValue(alocaladmin);
                                                ref.child("isBlocked").setValue(block);

                                                Toast.makeText(getContext(), "Updated Sucessfully", Toast.LENGTH_SHORT).show();
                                                alertDialog.cancel();
                                                progressDialog.dismiss();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            });
                            alertDialog.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    if (which == 2)
                    {
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
                                            database.getReference("delivery_boy").child(mDeliveryBoyModel.get(position).getDeliveryBoyID()).removeValue();
                                            database.getReference("user_data").child(mDeliveryBoyModel.get(position).getDeliveryBoyID()).removeValue();
                                        }
                                    });
                            dialog1.show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if(which == 1)
                    {
                        callCoAdmin(mDeliveryBoyModel.get(position).getDeliveyBoyMobileNo());
                    }

                }
            });
            dialog.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callCoAdmin(String deliveyBoyMobileNo)
    {
        Permissions.check(getContext(), Manifest.permission.CALL_PHONE, null, new PermissionHandler() {
            @Override
            public void onGranted()
            {
                String number = deliveyBoyMobileNo;

                Intent intent = new Intent(Intent.ACTION_CALL);
                if (number.isEmpty())
                {
                    Toast.makeText(getContext(), "Unable To make Call", Toast.LENGTH_SHORT).show();
                } else {
                    intent.setData(Uri.parse("tel:" + number));
                    startActivity(intent);
                }
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions)
            {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                super.onDenied(context, deniedPermissions);
            }
            @Override
            public boolean onBlocked(Context context, ArrayList<String> blockedList)
            {
                Toast.makeText(getContext(), "Permission blocked", Toast.LENGTH_SHORT).show();
                return super.onBlocked(context, blockedList);
            }
        });
    }
}