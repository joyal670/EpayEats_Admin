package com.epayeats.epayeatsadmin.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.epayeats.epayeatsadmin.R;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class deliveryBoyReportdetails_Activity extends AppCompatActivity
{
    String id, name, charge, phone;
    int charg;

    TextView boy_report_details_name, boy_report_details_totalsale, boy_report_details_deliverd, boy_report_details_pending, boy_report_details_totalcash_delivery_charge;

    DatabaseReference reference;

    int count0 = 0;
    int count1 = 0;
    int count2 = 0;
    int countdeli = 0;
    int cnt = 0;

    int total = 0;
    int total2 = 0;

    String status0 = "0";
    String status1 = "1";
    String status2 = "2";
    String status3 = "3";

    SwipeRefreshLayout refresh_deliveryboy_details;

    public ProgressDialog progressDialog;

    TextView selectedDate_From_admn, selectedDate_To_admn;

    Button search_generate_coadmin_report;



    TextView coadmin_report_name, coadmin_report_fromdate, coadmin_report_todate, coadmin_report_phone;
    TextView coadmin_report_km, coadmin_report_totalsale;
    TextView coadmin_report_totaldeliverd, coadmin_report_totalpending, coadmin_report_totalearned;

    int report_totalsale = 0;
    int report_totaldeliverd = 0;
    int report_totalcancelled = 0;
    int report_totalpending = 0;
    int report_totalearned = 0;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date fromD = new Date();
    Date toD = new Date();

    int delivCharge;
    int delivtotal = 0;

    Button deliveryboy_ShareReport;
    Button deliveryboy_report_from_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_boy_reportdetails_);

        try {

            coadmin_report_name = findViewById(R.id.deliveryboy_report_name);
            coadmin_report_fromdate = findViewById(R.id.deliveryboy_report_fromdate);
            coadmin_report_todate = findViewById(R.id.deliveryboy_report_todate);
            coadmin_report_phone = findViewById(R.id.deliveryboy_report_phone);


            coadmin_report_km = findViewById(R.id.deliveryboy_report_km);
            coadmin_report_totalsale = findViewById(R.id.deliveryboy_report_totalsale);

            coadmin_report_totaldeliverd = findViewById(R.id.deliveryboy_report_totaldeliverd);
            coadmin_report_totalpending = findViewById(R.id.deliveryboy_report_totalpending);
            coadmin_report_totalearned = findViewById(R.id.deliveryboy_report_totalearned);

            selectedDate_From_admn = findViewById(R.id.selectedDate_From_deliveryboy);
            selectedDate_To_admn = findViewById(R.id.selectedDate_To_deliveryboy);
            search_generate_coadmin_report = findViewById(R.id.search_generate_deliveryboy_report);
            deliveryboy_ShareReport = findViewById(R.id.deliveryboy_ShareReport);
            deliveryboy_report_from_date = findViewById(R.id.deliveryboy_report_from_date);

            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");

            id = getIntent().getExtras().getString("id");
            name = getIntent().getExtras().getString("name");
            charge = getIntent().getExtras().getString("charge");
            phone = getIntent().getExtras().getString("phone");

            boy_report_details_name = findViewById(R.id.boy_report_details_name);
            boy_report_details_totalsale = findViewById(R.id.boy_report_details_totalsale);
            boy_report_details_deliverd = findViewById(R.id.boy_report_details_deliverd);
            boy_report_details_pending = findViewById(R.id.boy_report_details_pending);
            boy_report_details_totalcash_delivery_charge = findViewById(R.id.boy_report_details_totalcash_delivery_charge);
            refresh_deliveryboy_details = findViewById(R.id.refresh_deliveryboy_details);

            refresh_deliveryboy_details.setRefreshing(true);
            refresh_deliveryboy_details.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refresh_deliveryboy_details.setRefreshing(false);
                }
            });

            boy_report_details_name.setText(name);

            reference = FirebaseDatabase.getInstance().getReference("order_data");

            load();


            search_generate_coadmin_report.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    String a1 = selectedDate_From_admn.getText().toString();
                    String a2 = selectedDate_To_admn.getText().toString();
                    if(a1.isEmpty() || a2.isEmpty())
                    {
                        Toast.makeText(deliveryBoyReportdetails_Activity.this, "Select Date", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        generateReport(a1, a2);
                    }
                }
            });

            deliveryboy_ShareReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShareReport();
                }
            });

            CalendarConstraints.Builder conBuilder = new CalendarConstraints.Builder();
            MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
            builder.setTitleText("Select Date");
            builder.setCalendarConstraints(conBuilder.build());
            final MaterialDatePicker materialDatePicker = builder.build();

            deliveryboy_report_from_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
                }
            });
            materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                @Override
                public void onPositiveButtonClick(Object selection)
                {
                    Pair selectedDates = (Pair) materialDatePicker.getSelection();

                    final Pair<Date, Date> rangeDate = new Pair<>(new Date((Long) selectedDates.first), new Date((Long) selectedDates.second));

                    Date startDate = rangeDate.first;
                    Date endDate = rangeDate.second;

                    SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd");
                    selectedDate_From_admn.setText(simpleFormat.format(startDate));
                    selectedDate_To_admn.setText(simpleFormat.format(endDate));
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void ShareReport()
    {
        String totoSale = String.valueOf(report_totalsale);
        String deliveryChr = String.valueOf(delivCharge);
        String totDeli = String.valueOf(report_totaldeliverd);
        String totPend = String.valueOf(report_totalpending);
        String totEarned = String.valueOf(delivtotal);

        String temp = coadmin_report_totaldeliverd.getText().toString();
        if(temp.equals("0"))
        {
            Toast.makeText(this, "No records", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Report");
            String sAux = name+ " Report, From  " +selectedDate_From_admn.getText().toString() + "  To  "+selectedDate_To_admn.getText().toString()+ "\n\n";
            sAux = sAux + "------------------------\n";
            sAux = sAux + "Details\n";
            sAux = sAux + "Name : "+name + "\n";
            sAux = sAux + "Delivery Charge : "+deliveryChr + "\n";
            sAux = sAux + "------------------------\n";
            sAux = sAux + "Sale Details\n";
            sAux = sAux + "Total Sale : "+totoSale + "\n";
            sAux = sAux + "Total Deliverd orders : " +totDeli + "\n";
            sAux = sAux + "Total Pending orders : " +totPend + "\n";
            sAux = sAux + "Total Cash Earned : "+totEarned + "\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        }

    }

    private void generateReport(String a1, String a2)
    {
        try {
            String tem1 = a1;
            String tem2 = a2;

            try {
                fromD = dateFormat.parse(a1);
                toD = dateFormat.parse(a2);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            report_totalsale = 0;
            report_totaldeliverd = 0;
            report_totalcancelled = 0;
            report_totalpending = 0;
            report_totalearned = 0;
            coadmin_report_totalsale.setText("0");
            coadmin_report_totaldeliverd.setText("0");
            coadmin_report_totalpending.setText("0");
            coadmin_report_totalearned.setText("0");


            coadmin_report_name.setText(name);
            coadmin_report_fromdate.setText(a1);
            coadmin_report_todate.setText(a2);
            coadmin_report_phone.setText(phone);
            coadmin_report_km.setText("");
            delivtotal = 0;
            delivCharge = 0;

            Query query = FirebaseDatabase.getInstance().getReference("order_data");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren())
                    {
                        if (id.equals(snapshot1.child("deliveryBodID").getValue().toString())) {
                            String d1 = snapshot1.child("orderDate").getValue().toString();

                            try {
                                Date cd = dateFormat.parse(d1);

                                if (fromD.before(cd) && cd.before(toD))
                                {
                                    report_totalsale = report_totalsale + 1;
                                    coadmin_report_totalsale.setText(report_totalsale + "");

                                    if (status2.equals(snapshot1.child("orderStatus").getValue().toString())) {
                                        delivCharge = Integer.parseInt(charge);
                                        int cg = delivCharge;

                                        delivtotal = delivtotal + cg;

                                        coadmin_report_km.setText(delivtotal + "");

                                        report_totaldeliverd = report_totaldeliverd + 1;
                                        coadmin_report_totaldeliverd.setText(report_totaldeliverd + "");
                                    }

                                    if (status1.equals(snapshot1.child("orderStatus").getValue().toString())) {
                                        report_totalpending = report_totalpending + 1;
                                        coadmin_report_totalpending.setText(report_totalpending + "");
                                    }
                                    if (status2.equals(snapshot1.child("orderStatus").getValue().toString())) {
                                        int qty = 0;
                                        int price = 0;
                                        qty = qty + Integer.parseInt(snapshot1.child("qty").getValue().toString());
                                        price = price + Integer.parseInt(snapshot1.child("offerPrice").getValue().toString());
                                        int temp = qty * price;
                                        report_totalearned = report_totalearned + temp;
                                        coadmin_report_totalearned.setText(report_totalearned + "");

                                    }
                                }


                            } catch (ParseException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(deliveryBoyReportdetails_Activity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void load()
    {
        refresh_deliveryboy_details.setRefreshing(false);
        loadAllOrders();
        loadDeliverdOrders();
        loadPendingOrders();
        loadTotalCharge();
    }


    private void loadTotalCharge()
    {
        try {
            charg = Integer.parseInt(charge);
            progressDialog.show();
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progressDialog.dismiss();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if (status2.equals(dataSnapshot1.child("orderStatus").getValue().toString())) {
                            if (id.equals(dataSnapshot1.child("deliveryBodID").getValue().toString())) {
                                try {

                                    int cg = charg;

                                    total = total + cg;

                                    boy_report_details_totalcash_delivery_charge.setText(total + "");

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressDialog.dismiss();
                    Toast.makeText(deliveryBoyReportdetails_Activity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }

    private void loadAllOrders()
    {
        try {
            progressDialog.show();
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progressDialog.dismiss();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if (id.equals(dataSnapshot1.child("deliveryBodID").getValue().toString())) {
                            try {
                                count0 = count0 + 1;
                                boy_report_details_totalsale.setText(count0 + "");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressDialog.dismiss();
                    Toast.makeText(deliveryBoyReportdetails_Activity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadDeliverdOrders()
    {
        try {
            progressDialog.show();
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progressDialog.dismiss();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if (status2.equals(dataSnapshot1.child("orderStatus").getValue().toString())) {
                            if (id.equals(dataSnapshot1.child("deliveryBodID").getValue().toString())) {
                                try {
                                    count2 = count2 + 1;
                                    boy_report_details_deliverd.setText(count2 + "");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressDialog.dismiss();
                    Toast.makeText(deliveryBoyReportdetails_Activity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPendingOrders()
    {
        try {
            progressDialog.show();
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progressDialog.dismiss();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if (status1.equals(dataSnapshot1.child("orderStatus").getValue().toString())) {
                            if (id.equals(dataSnapshot1.child("deliveryBodID").getValue().toString())) {
                                try {
                                    count1 = count1 + 1;
                                    boy_report_details_pending.setText(count1 + "");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressDialog.dismiss();
                    Toast.makeText(deliveryBoyReportdetails_Activity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}