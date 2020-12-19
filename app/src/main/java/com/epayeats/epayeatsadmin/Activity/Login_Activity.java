package com.epayeats.epayeatsadmin.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.epayeats.epayeatsadmin.Activity.MainActivity;
import com.epayeats.epayeatsadmin.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Login_Activity extends AppCompatActivity 
{
    EditText login_activity_email, login_activity_password;
    Button login_activity_btn;

    public ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        try {
            firebaseAuth = FirebaseAuth.getInstance();

            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loging...");

            login_activity_email = findViewById(R.id.login_activity_email);
            login_activity_password = findViewById(R.id.login_activity_password);
            login_activity_btn = findViewById(R.id.login_activity_btn);

            login_activity_btn.setOnClickListener(v -> {
                String email = login_activity_email.getText().toString();
                String password = login_activity_password.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    if (email.isEmpty()) {
                        login_activity_email.setError("Required");
                    }
                    if (password.isEmpty()) {
                        login_activity_password.setError("Required");
                    }
                } else {
                    loginFn(email, password);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // for creating admin account
    private void login()
    {
        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        DatabaseReference reference1;
        reference1 = FirebaseDatabase.getInstance().getReference("user_data");

        firebaseAuth.createUserWithEmailAndPassword("admin@gmail.com", "password").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {

                if (task.isSuccessful())
                {
                    final String key = task.getResult().getUser().getUid();
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("userId", key);
                    hashMap.put("userName", "admin");
                    hashMap.put("userEmail", "admin@gmail.com");
                    hashMap.put("type", "admin");
                    reference1.child(key).setValue(hashMap);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void loginFn(String email, String password)
    {
        try {
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                progressDialog.dismiss();
                if (task.isSuccessful()) {

                    sharedPreferences = getSharedPreferences("data", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("userid", task.getResult().getUser().getUid());
                    editor.putString("useremail", email);
                    editor.putBoolean("login_status", true);
                    editor.apply();

                    Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(Login_Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }).addOnFailureListener(e -> Toast.makeText(Login_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            SharedPreferences sharedPreferences = getSharedPreferences("data", 0);
            boolean logg = sharedPreferences.getBoolean("login_status", false);
            if (logg) {
                Intent intent = new Intent(Login_Activity.this, MainActivity.class);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed()
    {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("Exit");
            builder.setMessage("Are you sure want to Exit?");


            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    moveTaskToBack(true);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.create().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}