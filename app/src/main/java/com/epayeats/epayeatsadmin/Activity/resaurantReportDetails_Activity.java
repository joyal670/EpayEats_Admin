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

public class resaurantReportDetails_Activity extends AppCompatActivity
{
    String id, name, localadmin, phone, location;

    TextView rest_report_details_name, rest_report_details_totalsale, rest_report_details_deliverd, rest_report_details_pending, rest_report_details_cancelled, rest_report_details_totalcash_offer;

    DatabaseReference reference;

    int count0 = 0;
    int count1 = 0;
    int count2 = 0;
    int count3 = 0;


    int total = 0;
    int total2 = 0;

    String status0 = "0";
    String status1 = "1";
    String status2 = "2";
    String status3 = "3";

    SwipeRefreshLayout refres_rest_activity_detailed_report;

    public ProgressDialog progressDialog;

    TextView selectedDate_From_admn, selectedDate_To_admn;

    Button search_generate_coadmin_report;


    TextView coadmin_report_name, coadmin_report_fromdate, coadmin_report_todate, coadmin_report_phone, coadmin_report_place;
    TextView coadmin_report_km, coadmin_report_totalsale;
    TextView coadmin_report_totaldeliverd, coadmin_report_totalcancelled, coadmin_report_totalpending, coadmin_report_totalearned, rest_report_totalneworders;

    int report_totalsale = 0;
    int report_totaldeliverd = 0;
    int report_totalcancelled = 0;
    int report_totalpending = 0;
    int report_totalearned = 0;
    int report_totalnew = 0;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date fromD = new Date();
    Date toD = new Date();

    Button rest_ShareReport;
    Button rest_report_from_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resaurant_report_details_);
        try {

            coadmin_report_name = findViewById(R.id.rest_report_name);
            coadmin_report_fromdate = findViewById(R.id.rest_report_fromdate);
            coadmin_report_todate = findViewById(R.id.rest_report_todate);
            coadmin_report_phone = findViewById(R.id.rest_report_phone);
            coadmin_report_place = findViewById(R.id.rest_report_place);

            coadmin_report_km = findViewById(R.id.rest_report_km);
            coadmin_report_totalsale = findViewById(R.id.rest_report_totalsale);

            coadmin_report_totaldeliverd = findViewById(R.id.rest_report_totaldeliverd);
            coadmin_report_totalcancelled = findViewById(R.id.rest_report_totalcancelled);
            coadmin_report_totalpending = findViewById(R.id.rest_report_totalpending);
            coadmin_report_totalearned = findViewById(R.id.rest_report_totalearned);
            rest_report_totalneworders = findViewById(R.id.rest_report_totalneworders);

            selectedDate_From_admn = findViewById(R.id.selectedDate_From_rest);
            selectedDate_To_admn = findViewById(R.id.selectedDate_To_rest);
            search_generate_coadmin_report = findViewById(R.id.search_generate_rest_report);
            rest_ShareReport = findViewById(R.id.rest_ShareReport);
            rest_report_from_date = findViewById(R.id.rest_report_from_date);

            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");

            id = getIntent().getExtras().getString("id");
            name = getIntent().getExtras().getString("name");
            localadmin = getIntent().getExtras().getString("localadmin");
            phone = getIntent().getExtras().getString("phone");
            location = getIntent().getExtras().getString("location");

            rest_report_details_name = findViewById(R.id.rest_report_details_name);
            rest_report_details_totalsale = findViewById(R.id.rest_report_details_totalsale);
            rest_report_details_deliverd = findViewById(R.id.rest_report_details_deliverd);
            rest_report_details_pending = findViewById(R.id.rest_report_details_pending);
            rest_report_details_cancelled = findViewById(R.id.rest_report_details_cancelled);
            rest_report_details_totalcash_offer = findViewById(R.id.rest_report_details_totalcash_offer);

            refres_rest_activity_detailed_report = findViewById(R.id.refres_rest_activity_detailed_report);
            refres_rest_activity_detailed_report.setRefreshing(true);
            refres_rest_activity_detailed_report.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refres_rest_activity_detailed_report.setRefreshing(false);
                }
            });

            rest_report_details_name.setText(name);

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
                        Toast.makeText(resaurantReportDetails_Activity.this, "Select Date", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        generateReport(a1, a2);
                    }
                }
            });

            rest_ShareReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareReport();
                }
            });


            CalendarConstraints.Builder conBuilder = new CalendarConstraints.Builder();
            MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
            builder.setTitleText("Select Date");
            builder.setCalendarConstraints(conBuilder.build());
            final MaterialDatePicker materialDatePicker = builder.build();
            rest_report_from_date.setOnClickListener(new View.OnClickListener() {
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

    private void shareReport()
    {
        String totDeli = String.valueOf(report_totaldeliverd);
        String totSale = String.valueOf(report_totalsale);
        String totCancel = String.valueOf(report_totalcancelled);
        String totPend = String.valueOf(report_totalpending);
        String totEarn = String.valueOf(report_totalearned);
        String totNew = String.valueOf(report_totalnew);

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
            String sAux = name+ " Report, From  " +selectedDate_From_admn.getText().toString() + "  To  "+selectedDate_To_admn.getText().toString()+ "\n";
            sAux = sAux + "------------------------\n";
            sAux = sAux + "Details\n";
            sAux = sAux + "Name : "+name + "\n";
            sAux = sAux + "Phone : " +phone + "\n";
            sAux = sAux + "Location : " +location + "\n";
            sAux = sAux + "Local Admin : " +localadmin + "\n\n";
            sAux = sAux + "------------------------\n";
            sAux = sAux + "Sale Details\n";
            sAux = sAux + "Total Sale : " +totSale + "\n";
            sAux = sAux + "New orders : " +totNew + "\n";
            sAux = sAux + "Total Deliverd orders : " +totDeli + "\n";
            sAux = sAux + "Total Pending orders : " +totPend + "\n";
            sAux = sAux + "Total Cancelled orders : " +totCancel + "\n";
            sAux = sAux + "Total Cash Earned : "+totEarn + "\n";
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
            report_totalnew = 0;
            coadmin_report_totalsale.setText("0");
            coadmin_report_totaldeliverd.setText("0");
            coadmin_report_totalcancelled.setText("0");
            coadmin_report_totalpending.setText("0");
            coadmin_report_totalearned.setText("0");
            rest_report_totalneworders.setText("0");


            coadmin_report_name.setText(name);
            coadmin_report_fromdate.setText(a1);
            coadmin_report_todate.setText(a2);
            coadmin_report_phone.setText(phone);
            coadmin_report_place.setText(location);
            coadmin_report_km.setText(localadmin);

            Query query = FirebaseDatabase.getInstance().getReference("order_data");
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        if (id.equals(snapshot1.child("restID").getValue().toString())) {
                            String d1 = snapshot1.child("orderDate").getValue().toString();

                            try {
                                Date cd = dateFormat.parse(d1);

                                if (fromD.before(cd) && cd.before(toD)) {
                                    report_totalsale = report_totalsale + 1;
                                    coadmin_report_totalsale.setText(report_totalsale + "");

                                    if (status2.equals(snapshot1.child("orderStatus").getValue().toString())) {
                                        report_totaldeliverd = report_totaldeliverd + 1;
                                        coadmin_report_totaldeliverd.setText(report_totaldeliverd + "");
                                    }
                                    if (status0.equals(snapshot1.child("orderStatus").getValue().toString())) {
                                        report_totalnew = report_totalnew + 1;
                                        rest_report_totalneworders.setText(report_totalnew + "");
                                    }

                                    if (status3.equals(snapshot1.child("orderStatus").getValue().toString())) {
                                        report_totalcancelled = report_totalcancelled + 1;
                                        coadmin_report_totalcancelled.setText(report_totalcancelled + "");
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
                    Toast.makeText(resaurantReportDetails_Activity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void load()
    {
        refres_rest_activity_detailed_report.setRefreshing(false);
        loadAllOrders();
        loadDeliverdOrders();
        loadPendingOrders();
        loadCancelledOrders();
        loadToatalCoast();
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
                        if (id.equals(dataSnapshot1.child("restID").getValue().toString())) {
                            try {
                                count0 = count0 + 1;
                                rest_report_details_totalsale.setText(count0 + "");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressDialog.dismiss();
                    Toast.makeText(resaurantReportDetails_Activity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if (status2.equals(dataSnapshot1.child("orderStatus").getValue().toString())) {
                            if (id.equals(dataSnapshot1.child("restID").getValue().toString())) {
                                try {
                                    count2 = count2 + 1;
                                    rest_report_details_deliverd.setText(count2 + "");
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
                    Toast.makeText(resaurantReportDetails_Activity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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
                            if (id.equals(dataSnapshot1.child("restID").getValue().toString())) {
                                try {
                                    count1 = count1 + 1;
                                    rest_report_details_pending.setText(count1 + "");
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
                    Toast.makeText(resaurantReportDetails_Activity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCancelledOrders()
    {
        try {
            progressDialog.show();
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progressDialog.dismiss();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if (status3.equals(dataSnapshot1.child("orderStatus").getValue().toString())) {
                            if (id.equals(dataSnapshot1.child("restID").getValue().toString())) {
                                try {
                                    count3 = count3 + 1;
                                    rest_report_details_cancelled.setText(count3 + "");
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
                    Toast.makeText(resaurantReportDetails_Activity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadToatalCoast()
    {
        try {
            progressDialog.show();
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    progressDialog.dismiss();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if (id.equals(dataSnapshot1.child("restID").getValue().toString())) {
                            if (status2.equals(dataSnapshot1.child("orderStatus").getValue().toString())) {
                                try {

                                    int qty = 0;
                                    int price = 0;

                                    qty = qty + Integer.parseInt(dataSnapshot1.child("qty").getValue().toString());
                                    price = price + Integer.parseInt(dataSnapshot1.child("offerPrice").getValue().toString());
                                    int temp = qty * price;
                                    total = total + temp;

                                    rest_report_details_totalcash_offer.setText(total + "");

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
                    Toast.makeText(resaurantReportDetails_Activity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}