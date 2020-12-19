package com.epayeats.epayeatsadmin.Fragments;

import android.Manifest;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.epayeats.epayeatsadmin.Activity.AddLocalAdmin_Activity;
import com.epayeats.epayeatsadmin.Adapter.LocaladminAdapter;
import com.epayeats.epayeatsadmin.Model.AdminModel;
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


public class LocalAdmin_Fragment extends Fragment
{
    ListView fragment_localadmin_listview;
    DatabaseReference mAdminDatabaseReference;
    List<AdminModel> mAdminModel;
    LocaladminAdapter mLocalAdminAdapter;

    FloatingActionsMenu localAdminFragmentFloatingMenu;
    FloatingActionButton fab_localAdminFragmentAdd;

    SearchView fragment_local_admin_searchView;
    SwipeRefreshLayout refresh_localadmin;

    public LocalAdmin_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_local_admin_, container, false);

        try {

            fragment_localadmin_listview = view.findViewById(R.id.fragment_localadmin_listview);
            fab_localAdminFragmentAdd = view.findViewById(R.id.fab_localAdminFragmentAdd);
            localAdminFragmentFloatingMenu = view.findViewById(R.id.localAdminFragmentFloatingMenu);
            fragment_local_admin_searchView = view.findViewById(R.id.fragment_local_admin_searchView);
            refresh_localadmin = view.findViewById(R.id.refresh_localadmin);

            mAdminDatabaseReference = FirebaseDatabase.getInstance().getReference("local_admin");

            loadData();

            refresh_localadmin.setRefreshing(true);
            refresh_localadmin.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadData();
                }
            });

            fab_localAdminFragmentAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    localAdminFragmentFloatingMenu.collapse();
                    addLocalAdmin();
                }
            });

            fragment_local_admin_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchLocalAdmin(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    searchLocalAdmin(newText);
                    return false;
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void addLocalAdmin()
    {
        try {
            Intent intent = new Intent(getContext(), AddLocalAdmin_Activity.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadData()
    {
        try {
            mAdminDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    refresh_localadmin.setRefreshing(false);
                    mAdminModel.clear();
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        AdminModel model = snapshot1.getValue(AdminModel.class);
                        mAdminModel.add(model);
                    }
                    mLocalAdminAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            mAdminModel = new ArrayList<>();
            mLocalAdminAdapter = new LocaladminAdapter(getContext(), mAdminModel);
            fragment_localadmin_listview.setAdapter(mLocalAdminAdapter);
            fragment_localadmin_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    editlocalAdmin(position);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchLocalAdmin(String query)
    {
        try {
            ArrayList<AdminModel> myList = new ArrayList<>();
            for (AdminModel obj : mAdminModel) {
                if (obj.getAdmnName().toLowerCase().contains(query.toLowerCase())) {
                    myList.add(obj);
                }
            }
            LocaladminAdapter localadminAdapter = new LocaladminAdapter(getContext(), myList);
            fragment_localadmin_listview.setAdapter(localadminAdapter);
            fragment_localadmin_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void editlocalAdmin(int position)
    {
        try {
            String[] items = {"Edit", "Call", "Delete"};
            AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
            dialog.setTitle("Select Options");
            dialog.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    if (which == 0)
                    {
                        try {

                            LayoutInflater li = LayoutInflater.from(getContext());
                            View admin = li.inflate(R.layout.new_localadmin_layout, null);
                            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                            alertBuilder.setView(admin);

                            DatabaseReference businessReference = FirebaseDatabase.getInstance().getReference("business");
                            ArrayList<String> spinnerDatalist;
                            ArrayAdapter<String> adapter;
                            ValueEventListener listener;
                            String msg = "Select --  ";

                            String[] catogries = {"UnBlocked", "Blocked"};
                            ArrayAdapter<String> catog = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, catogries);

                            final EditText name, address, businessKM, aadharNo, validity, gstNo, pancardNo, phone;
                            final Spinner BusinessArea, blockStatus;
                            final Button new_local_admin_button;

                            name = admin.findViewById(R.id.new_local_admin_name_editext);
                            address = admin.findViewById(R.id.new_local_admin_address_editext);
                            businessKM = admin.findViewById(R.id.new_local_admin_businessKM_editext);
                            aadharNo = admin.findViewById(R.id.new_local_admin_aadharNo_editext);
                            validity = admin.findViewById(R.id.new_local_admin_validaty_editext);
                            gstNo = admin.findViewById(R.id.new_local_admin_gstNo_editext);
                            pancardNo = admin.findViewById(R.id.new_local_admin_pancardNo_editext);
                            BusinessArea = admin.findViewById(R.id.new_local_admin_businessArea_editext);
                            blockStatus = admin.findViewById(R.id.new_local_admin_isBlocked_editext);
                            new_local_admin_button = admin.findViewById(R.id.new_local_admin_button);
                            phone = admin.findViewById(R.id.new_local_admin_phone_editext);

                            final AlertDialog alertDialog = alertBuilder.create();

                            blockStatus.setAdapter(catog);

                            try {
                                // setting business spinner
                                spinnerDatalist = new ArrayList<>();
                                adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerDatalist);
                                listener = businessReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        spinnerDatalist.clear();
                                        spinnerDatalist.add(mAdminModel.get(position).getAdmnBusinessArea());
                                        spinnerDatalist.add(msg);
                                        for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                                            spinnerDatalist.add(dataSnapshot1.child("location").getValue().toString());
                                            BusinessArea.setAdapter(adapter);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            name.setText(mAdminModel.get(position).getAdmnName());
                            address.setText(mAdminModel.get(position).getAdmnAddress());
                            businessKM.setText(mAdminModel.get(position).getAdmnBusinessKM());
                            aadharNo.setText(mAdminModel.get(position).getAdmnaadharNo());
                            validity.setText(mAdminModel.get(position).getAdmnValidaty());
                            gstNo.setText(mAdminModel.get(position).getAdmnGSTNo());
                            pancardNo.setText(mAdminModel.get(position).getAdmnPancardNo());
                            phone.setText(mAdminModel.get(position).getAdmnPhone());
                            new_local_admin_button.setText("Save");


                            new_local_admin_button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    final String a1, a2, a3, a4, a5, a6, a7, a8, a9, a10;

                                    a1 = name.getText().toString();
                                    a2 = address.getText().toString();
                                    a3 = businessKM.getText().toString();
                                    a4 = aadharNo.getText().toString();
                                    a5 = validity.getText().toString();
                                    a6 = gstNo.getText().toString();
                                    a7 = pancardNo.getText().toString();
                                    a8 = BusinessArea.getSelectedItem().toString();
                                    a9 = blockStatus.getSelectedItem().toString();
                                    a10 = phone.getText().toString();

                                    if (a1.isEmpty() || a2.isEmpty() || a3.isEmpty() || a4.isEmpty() || a5.isEmpty() || a6.isEmpty() || a7.isEmpty() || a10.isEmpty()) {
                                        if (a1.isEmpty()) {
                                            name.setError("Required");
                                        }
                                        if (a2.isEmpty()) {
                                            address.setError("Required");
                                        }
                                        if (a3.isEmpty()) {
                                            businessKM.setError("Required");
                                        }
                                        if (a4.isEmpty()) {
                                            aadharNo.setError("Required");
                                        }
                                        if (a5.isEmpty()) {
                                            validity.setError("Required");
                                        }
                                        if (a6.isEmpty()) {
                                            gstNo.setError("Required");
                                        }
                                        if (a7.isEmpty()) {
                                            pancardNo.setError("Required");
                                        }
                                        if (a10.isEmpty()) {
                                            phone.setError("Required");
                                        }
                                    } else {
                                        if (a8.equals(msg)) {
                                            Toast.makeText(getContext(), "Select Business Area", Toast.LENGTH_SHORT).show();
                                        } else {
                                            try {
                                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("local_admin").child(mAdminModel.get(position).getAdmnID());
                                                ref.child("admnName").setValue(a1);
                                                ref.child("admnAddress").setValue(a2);
                                                ref.child("admnBusinessKM").setValue(a3);
                                                ref.child("admnaadharNo").setValue(a4);
                                                ref.child("admnValidaty").setValue(a5);
                                                ref.child("admnGSTNo").setValue(a6);
                                                ref.child("admnPancardNo").setValue(a7);
                                                ref.child("admnBusinessArea").setValue(a8);
                                                ref.child("isBlocked").setValue(a9);
                                                ref.child("admnPhone").setValue(a10);

                                                Toast.makeText(getContext(), "Updated Sucessfully", Toast.LENGTH_SHORT).show();
                                                alertDialog.cancel();
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
                    {try {
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
                                        database.getReference("local_admin").child(mAdminModel.get(position).getAdmnID()).removeValue();
                                        database.getReference("user_data").child(mAdminModel.get(position).getAdmnID()).removeValue();
                                    }
                                });
                        dialog1.show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    }

                    if(which == 1)
                    {
                        callCoAdmin(mAdminModel.get(position).getAdmnPhone());
                    }
                }
            });

            dialog.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callCoAdmin(String admnPhone)
    {
        Permissions.check(getContext(), Manifest.permission.CALL_PHONE, null, new PermissionHandler() {
            @Override
            public void onGranted()
            {
                String number = admnPhone;

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