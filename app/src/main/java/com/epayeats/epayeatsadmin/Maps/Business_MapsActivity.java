package com.epayeats.epayeatsadmin.Maps;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.epayeats.epayeatsadmin.Model.BusinessModel;
import com.epayeats.epayeatsadmin.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Business_MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    BottomSheetBehavior behavior;
    TextView bussinesmap_selectedloc;
    Button bussinesmap_save;
    Double lat, lon;

    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business__maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.businessmap);
        mapFragment.getMapAsync(this);

        bussinesmap_selectedloc = findViewById(R.id.bussinesmap_selectedloc);
        bussinesmap_save = findViewById(R.id.bussinesmap_save);

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");

        View bootom = findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(bootom);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState)
            {
                switch (newState)
                {
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;

                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset)
            {}
        });

        bussinesmap_save.setOnClickListener(v -> addBusiness());


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLongClickListener(latLng -> {

            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(latLng).title("New Business Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            String s = latLng.toString();
            String s2 = s.replaceAll("[()lat/lng:]","");
            String[] s3 = s2.split(",");
            Double d1 = Double.valueOf(s3[0]);
            Double d2 = Double.valueOf(s3[1]);

            lat = d1;
            lon = d2;

            try {
                Geocoder geocoder = new Geocoder(Business_MapsActivity.this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(d1, d2, 1);
                String address = addresses.get(0).getAddressLine(0);
                bussinesmap_selectedloc.setText(address);

            } catch (IOException e) {
                e.printStackTrace();
            }

            setBootom();
        });
    }

    private void addBusiness()
    {
        String txt = bussinesmap_selectedloc.getText().toString();
        if(txt.isEmpty())
        {
            Toast.makeText(this, "Please Select a Location", Toast.LENGTH_SHORT).show();
        }
        else
        {
            progressDialog.show();
            BusinessModel businessModel;
            businessModel = new BusinessModel();

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("business");
            String pushKey = reference.push().getKey();

            businessModel.setBusinessId(pushKey);
            businessModel.setLocation(txt);
            businessModel.setLatitude(String.valueOf(lat));
            businessModel.setLongitute(String.valueOf(lon));

            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
            businessModel.setDate(date);

            businessModel.setTemp1("");
            businessModel.setTemp2("");

            reference.child(pushKey).setValue(businessModel);

            bussinesmap_selectedloc.setText("");

            progressDialog.dismiss();
            Toast.makeText(Business_MapsActivity.this, "New Business Location Added", Toast.LENGTH_SHORT).show();
        }
    }

    private void setBootom()
    {
        if(behavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
        {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        else
        {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

}