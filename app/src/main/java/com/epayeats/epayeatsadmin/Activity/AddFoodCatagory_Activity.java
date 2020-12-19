package com.epayeats.epayeatsadmin.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.epayeats.epayeatsadmin.Model.FoodCatagoryModel;
import com.epayeats.epayeatsadmin.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddFoodCatagory_Activity extends AppCompatActivity
{
     EditText foodcatagorey;
     Button save;
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_catagory_);

        try {
            foodcatagorey = findViewById(R.id.add_foodcatagorey_editext);
            save = findViewById(R.id.add_foodcatagorey_save);

            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String a1;
                    a1 = foodcatagorey.getText().toString();
                    if (a1.isEmpty() || a1.equals(" ")) {
                        foodcatagorey.setError("Required");
                    } else {
                        progressDialog.show();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("main_catagory");
                        String pushKey = ref.push().getKey();

                        FoodCatagoryModel model;
                        model = new FoodCatagoryModel();

                        model.setFoodCatagoreyID(pushKey);
                        model.setFoodCatagoreyType(a1);

                        ref.child(pushKey).setValue(model);

                        foodcatagorey.setText("");

                        progressDialog.dismiss();

                        Toast.makeText(AddFoodCatagory_Activity.this, "New Catagory Added", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}