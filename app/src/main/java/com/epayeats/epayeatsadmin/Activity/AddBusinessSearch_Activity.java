package com.epayeats.epayeatsadmin.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.epayeats.epayeatsadmin.Maps.Business_MapsActivity;
import com.epayeats.epayeatsadmin.Model.BusinessModel;
import com.epayeats.epayeatsadmin.PlacesApi.PlaceAutoSuggestAdapter;
import com.epayeats.epayeatsadmin.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddBusinessSearch_Activity extends AppCompatActivity
{
    AutoCompleteTextView addline1;
    EditText addline2;
    EditText addcity;
    EditText addstate;
    EditText addcountry;
    EditText addpincode;

    double latitude;
    double longitude;
    Geocoder mGeocoder;

    Button AddBusiness_save_btn;

    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_business_search_);

        addline1 = findViewById(R.id.newaddline1);
        addline2 = findViewById(R.id.newaddline2);
        addcity = findViewById(R.id.newaddcity);
        addstate = findViewById(R.id.newaddstate);
        addcountry = findViewById(R.id.newaddcountry);
        addpincode = findViewById(R.id.newaddpincode);
        AddBusiness_save_btn = findViewById(R.id.AddBusiness_save_btn);


        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");


        addline1.setAdapter(new PlaceAutoSuggestAdapter(this,android.R.layout.simple_list_item_1));

        addline1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                getLatLng(addline1.getText().toString());
            }
        });

        AddBusiness_save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String temp = addline2.getText().toString();
                if(temp.isEmpty())
                {
                    Toast.makeText(AddBusinessSearch_Activity.this, "Please add an area", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    addBusiness();
                }
            }
        });

    }

    private void addBusiness()
    {
        progressDialog.show();

        String loc1, loc2, loc3,loc4, loc5, loc6;

        loc1 = addline1.getText().toString();
        loc2 = addline2.getText().toString();
        loc3 = addcity.getText().toString();
        loc4 = addstate.getText().toString();
        loc5 = addcountry.getText().toString();
        loc6 = addpincode.getText().toString();

        String temp = loc1 +","+ loc2+"," + loc3+"," + loc4+"," +loc5+"," + loc6;

        BusinessModel businessModel;
        businessModel = new BusinessModel();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("business");
        String pushKey = reference.push().getKey();

        businessModel.setBusinessId(pushKey);
        businessModel.setLocation(temp);
        businessModel.setLatitude(String.valueOf(latitude));
        businessModel.setLongitute(String.valueOf(longitude));

        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        businessModel.setDate(date);

        businessModel.setTemp1("");
        businessModel.setTemp2("");

        reference.child(pushKey).setValue(businessModel);

        addline1.setText("");
        addline2.setText("");
        addcity.setText("");
        addstate.setText("");
        addcountry.setText("");
        addpincode.setText("");

        progressDialog.dismiss();
        Toast.makeText(AddBusinessSearch_Activity.this, "New Business Location Added", Toast.LENGTH_SHORT).show();

    }

    public void getLatLng(String location) {
        if (Geocoder.isPresent()) {
            try {
                Geocoder gc = new Geocoder(this);
                List<Address> addresses = gc.getFromLocationName(location, 5); // get the found Address Objects
                List<LatLng> ll = new ArrayList<LatLng>(addresses.size()); // A list to save the coordinates if they are available
                for (Address a : addresses) {
                    if (a.hasLatitude() && a.hasLongitude()) {
                        ll.add(new LatLng(a.getLatitude(), a.getLongitude()));
                        System.out.println("HAHAHA  loc lat: " + a.getLatitude() + " " + a.getLongitude());
                        latitude = a.getLatitude();
                        longitude = a.getLongitude();

                        try {
                            getCityNameByCoordinates(a.getLatitude(), a.getLongitude());
                        } catch (Exception e) {
                            e.printStackTrace();
                            getLatLng(location);
                        }
                    }
                }
                if (addresses.size() < 1) {
                    getLatLng(location);
                }
            } catch (Exception e) {
                getLatLng(location);
            }
        }
    }

    private String getCityNameByCoordinates(double lat, double lon) throws IOException {
        mGeocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = mGeocoder.getFromLocation(lat, lon, 1);
        if (addresses != null && addresses.size() > 0) {
            System.out.println("HAHAHA place details: country " + addresses.get(0).getCountryName() + " PostalCode " + addresses.get(0).getPostalCode() + " getLocality " + addresses.get(0).getLocality() + " getLocale " + addresses.get(0).getLocale() + " getSubLocality " + addresses.get(0).getAdminArea());
            try {
                addline2.setText(addline1.getText().toString().split(", ")[1]);
                addline1.setText(addline1.getText().toString().split(", ")[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            addcity.setText(addresses.get(0).getLocality());
            addcity.setSelection(addcity.getText().toString().length());
            addstate.setText(addresses.get(0).getAdminArea());
            addstate.setSelection(addstate.getText().toString().length());
            addpincode.setText(addresses.get(0).getPostalCode());
            addpincode.setSelection(addpincode.getText().toString().length());
            addcountry.setText(addresses.get(0).getCountryName());
            addcountry.setSelection(addcountry.getText().toString().length());
            addcity.setError(null);
            addstate.setError(null);
            addpincode.setError(null);
            addcountry.setError(null);

            return addresses.get(0).getLocality();
        }
        return null;
    }
}