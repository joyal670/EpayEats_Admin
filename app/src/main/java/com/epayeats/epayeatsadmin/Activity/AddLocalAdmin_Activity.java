package com.epayeats.epayeatsadmin.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.epayeats.epayeatsadmin.Model.AdminModel;
import com.epayeats.epayeatsadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AddLocalAdmin_Activity extends AppCompatActivity
{
    DatabaseReference businessReference = FirebaseDatabase.getInstance().getReference("business");
    ArrayList<String> spinnerDatalist;
    ArrayAdapter<String> adapter;
    ValueEventListener listener;
    String msg = "Select --  ";

    public ProgressDialog progressDialog;

    EditText name, email, password, conpassword, address, businessKM, aadharNo, validity, gstNo, pancardNo, phone;
    Spinner BusinessArea, blockStatus, district, state;
    Button new_local_admin_button;

    ArrayList<String> listAll=new ArrayList<String>();
    AutoCompleteTextView act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_local_admin_);

        try {

            name = findViewById(R.id.add_local_admin_name_editext);
            email = findViewById(R.id.add_local_admin_email_editext);
            password = findViewById(R.id.add_local_admin_password_editext);
            conpassword = findViewById(R.id.add_local_admin_conpassword_editext);
            address = findViewById(R.id.add_local_admin_address_editext);
            businessKM = findViewById(R.id.add_local_admin_businessKM_editext);
            aadharNo = findViewById(R.id.add_local_admin_aadharNo_editext);
            validity = findViewById(R.id.add_local_admin_validaty_editext);
            gstNo = findViewById(R.id.add_local_admin_gstNo_editext);
            pancardNo = findViewById(R.id.add_local_admin_pancardNo_editext);
            BusinessArea = findViewById(R.id.add_local_admin_businessArea_editext);
            blockStatus = findViewById(R.id.add_local_admin_isBlocked_editext);
            new_local_admin_button = findViewById(R.id.add_local_admin_button);
            phone = findViewById(R.id.add_local_admin_phone_editext);
            act=(AutoCompleteTextView)findViewById(R.id.actAll);

            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");


            String[] catogries = {"UnBlocked", "Blocked"};
            ArrayAdapter<String> catog = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, catogries);
            blockStatus.setAdapter(catog);

            try {
                progressDialog.show();
                spinnerDatalist = new ArrayList<>();
                adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerDatalist);
                listener = businessReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        spinnerDatalist.clear();
                        spinnerDatalist.add(msg);
                        progressDialog.dismiss();
                        for (DataSnapshot dataSnapshot1 : snapshot.getChildren()) {
                            spinnerDatalist.add(dataSnapshot1.child("location").getValue().toString());
                            BusinessArea.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismiss();
                        Toast.makeText(AddLocalAdmin_Activity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            new_local_admin_button.setOnClickListener(v -> {

                addAdmin();
            });

            inilizeSpinner();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void inilizeSpinner()
    {
        obj_list();
        addToAll();
    }

    private void addAdmin()
    {
        try {
            String a1, a2, a3, a4, a5, a6, a7, a8, a9, aemail, apassword, aconpassword, aphone, aplace;

            a1 = name.getText().toString();
            a2 = address.getText().toString();
            a3 = businessKM.getText().toString();
            a4 = aadharNo.getText().toString();
            a5 = validity.getText().toString();
            a6 = gstNo.getText().toString();
            a7 = pancardNo.getText().toString();
            a8 = BusinessArea.getSelectedItem().toString();
            a9 = blockStatus.getSelectedItem().toString();
            aemail = email.getText().toString();
            apassword = password.getText().toString();
            aconpassword = conpassword.getText().toString();
            aphone = phone.getText().toString();
            aplace = act.getText().toString();

            if (a1.isEmpty() || a2.isEmpty() || a3.isEmpty() || a4.isEmpty() || a5.isEmpty() || a6.isEmpty() || a7.isEmpty() || aemail.isEmpty() || apassword.isEmpty() || aconpassword.isEmpty() || aphone.isEmpty() || a8.isEmpty() || aplace.isEmpty()) {
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
                if (aemail.isEmpty()) {
                    email.setError("Reqired");
                }
                if (apassword.isEmpty()) {
                    password.setError("Requird");
                }
                if (aconpassword.isEmpty()) {
                    conpassword.setError("Required");
                }
                if (aphone.isEmpty()) {
                    phone.setError("Required");
                }

                if(aplace.isEmpty())
                {
                    act.setError("Required");
                }
                if (a8.isEmpty()) {
                    Toast.makeText(AddLocalAdmin_Activity.this, "Select Business Location", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (apassword.equals(aconpassword)) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("local_admin");

                    FirebaseAuth firebaseAuth;
                    firebaseAuth = FirebaseAuth.getInstance();

                    DatabaseReference reference1;
                    reference1 = FirebaseDatabase.getInstance().getReference("user_data");

                    if (a8.equals(msg) || a8.isEmpty()) {
                        Toast.makeText(AddLocalAdmin_Activity.this, "Select Business Area", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.show();
                        try {
                            // finding business ID based on business name
                            Query query = FirebaseDatabase.getInstance().getReference("business");
                            query.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                        if (a8.equals(snapshot1.child("location").getValue().toString())) {
                                            String locID = snapshot1.child("businessId").getValue().toString();

                                            firebaseAuth.createUserWithEmailAndPassword(aemail, aconpassword).addOnCompleteListener(task ->
                                            {
                                                if (task.isSuccessful()) {
                                                    final String key = task.getResult().getUser().getUid();
                                                    HashMap<String, String> hashMap = new HashMap<>();
                                                    hashMap.put("userId", key);
                                                    hashMap.put("userName", a1);
                                                    hashMap.put("userEmail", aemail);
                                                    hashMap.put("type", "local_admin");
                                                    reference1.child(key).setValue(hashMap).addOnCompleteListener(task1 ->
                                                    {
                                                        if (task1.isSuccessful()) {
                                                            AdminModel adminModel;
                                                            adminModel = new AdminModel();

                                                            adminModel.setAdmnID(key);
                                                            adminModel.setAdmnName(a1);
                                                            adminModel.setAdmnEmail(aemail);
                                                            adminModel.setAdmnPassword(aconpassword);
                                                            adminModel.setAdmnAddress(a2);
                                                            adminModel.setAdmnBusinessKM(a3);
                                                            adminModel.setAdmnaadharNo(a4);
                                                            adminModel.setAdmnValidaty(a5);
                                                            adminModel.setAdmnBusinessArea(a8);
                                                            adminModel.setAdmnGSTNo(a6);
                                                            adminModel.setAdmnPancardNo(a7);
                                                            adminModel.setIsBlocked(a9);
                                                            adminModel.setAdmnPhone(aphone);
                                                            adminModel.setBusinesslocationID(locID);

                                                            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                                                            adminModel.setDate(date);

                                                            adminModel.setTemp1(aplace);
                                                            adminModel.setTemp2("");

                                                            ref.child(key).setValue(adminModel);

                                                            name.setText("");
                                                            address.setText("");
                                                            businessKM.setText("");

                                                            aadharNo.setText("");
                                                            validity.setText("");
                                                            gstNo.setText("");
                                                            pancardNo.setText("");

                                                            email.setText("");
                                                            password.setText("");
                                                            conpassword.setText("");
                                                            phone.setText("");
                                                            act.setText("");

                                                            progressDialog.dismiss();

                                                            Toast.makeText(AddLocalAdmin_Activity.this, "New Local Admin Added", Toast.LENGTH_SHORT).show();

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            progressDialog.dismiss();
                                                            Toast.makeText(AddLocalAdmin_Activity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });


                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    progressDialog.dismiss();
                                                    Toast.makeText(AddLocalAdmin_Activity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    progressDialog.dismiss();
                                    Toast.makeText(AddLocalAdmin_Activity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } else {
                    conpassword.setError("Password Not Matched");
                    Toast.makeText(AddLocalAdmin_Activity.this, "Password Not Matched", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getJson()
    {
        String json=null;
        try
        {
            // Opening cities.json file
            InputStream is = getAssets().open("cities.json");
            // is there any content in the file
            int size = is.available();
            byte[] buffer = new byte[size];
            // read values in the byte array
            is.read(buffer);
            // close the stream --- very important
            is.close();
            // convert byte to string
            json = new String(buffer, "UTF-8");
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return json;
        }
        return json;
    }

    // This add all JSON object's data to the respective lists
    void obj_list()
    {
        // Exceptions are returned by JSONObject when the object cannot be created
        try
        {
            // Convert the string returned to a JSON object
            JSONObject jsonObject=new JSONObject(getJson());
            // Get Json array
            JSONArray array=jsonObject.getJSONArray("array");
            // Navigate through an array item one by one
            for(int i=0;i<array.length();i++)
            {
                // select the particular JSON data
                JSONObject object=array.getJSONObject(i);
                String city=object.getString("name");
                String state=object.getString("state");
                // add to the lists in the specified format
                listAll.add(city+" , "+state);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }


    // The first auto complete text view
    void addToAll()
    {

        adapterSetting(listAll);
    }

    // setting adapter for auto complete text views
    void adapterSetting(ArrayList arrayList)
    {
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,arrayList);
        act.setAdapter(adapter);
        hideKeyBoard();
    }

    // hide keyboard on selecting a suggestion
    public void hideKeyBoard()
    {
        act.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
    }
}