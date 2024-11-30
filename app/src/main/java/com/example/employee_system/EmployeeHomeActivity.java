package com.example.employee_system;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.paperdb.Paper;

public class EmployeeHomeActivity extends AppCompatActivity {

    private ImageView empProfileButton, empEditButton, empHoliday, doctorLogout,empNotification;
    private TextView doctorNameShow,tvCalculatedSalary;
    private DatabaseReference myRef;
    private String DocNameShow;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home);

        // Initialize PaperDB at the very start
        Paper.init(this);

        // Fetch and calculate salary increment
        fetchAndCalculateSalary();

        final String Docid = Paper.book().read(Prevalent.UserIdKey);

        doctorNameShow = findViewById(R.id.employerNameShow); // Move this line up

        tvCalculatedSalary = findViewById(R.id.tvCalculatedSalary);



        myRef = FirebaseDatabase.getInstance().getReference("Employees").child(Docid);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DocNameShow = snapshot.child("name").getValue(String.class);
                if (DocNameShow != null) {
                    doctorNameShow.setText(DocNameShow);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        empProfileButton = findViewById(R.id.profileButton);
        empHoliday = findViewById(R.id.empHoliday);
        empEditButton = findViewById(R.id.empEditButton);
        doctorLogout = findViewById(R.id.doctorLogout);
        empNotification = findViewById(R.id.empNotification);

        empEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmployeeHomeActivity.this, EmployeeEditProfile.class);
                startActivity(intent);
            }
        });

        empProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmployeeHomeActivity.this, EmployeeProfileActivity.class);
                startActivity(intent);
            }
        });
        empHoliday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmployeeHomeActivity.this, BookLeaveActivity.class);
                startActivity(intent);
            }
        });

        empNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EmployeeHomeActivity.this, Reviews.class);
                startActivity(intent);
            }
        });

        doctorLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EmployeeHomeActivity.this);
                builder.setMessage("Are you sure you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Paper.book().write(Prevalent.UserIdKey, "id");
                                Paper.book().write(Prevalent.UserPasswordKey, "pass");
                                Paper.book().write(Prevalent.ParentDB, "user");
                                finish();
                                //startActivity(new Intent(DoctorHomeActivity.this, LoginActivity.class));
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    private void fetchAndCalculateSalary() {
        final String userId = Paper.book().read(Prevalent.UserIdKey); // Logged-in user ID
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Employees").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String entryDate = snapshot.child("date").getValue(String.class);
                    Object currentSalaryObj = snapshot.child("salary").getValue();

                    long currentSalary;
                    try {
                        if (currentSalaryObj instanceof Number) {
                            // If already a number, cast it
                            currentSalary = ((Number) currentSalaryObj).longValue();
                        } else if (currentSalaryObj instanceof String) {
                            // If it's a string, parse it
                            currentSalary = Long.parseLong((String) currentSalaryObj);
                        } else {
                            // Invalid data type
                            tvCalculatedSalary.setText("Salary: Current salary format invalid");
                            return;
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        tvCalculatedSalary.setText("Salary: Invalid salary format");
                        return;
                    }

                    // Perform your salary calculation
                    long incrementedSalary = calculateIncrement(entryDate, currentSalary);
                    tvCalculatedSalary.setText("Salary: " + incrementedSalary);
                } else {
                    tvCalculatedSalary.setText("Salary: Employee not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                tvCalculatedSalary.setText("Salary: Error fetching data");
            }
        });
    }


    private long calculateIncrement(String entryDate, long currentSalary) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = sdf.parse(entryDate);

            if (date != null) {
                Calendar entryCalendar = Calendar.getInstance();
                entryCalendar.setTime(date);

                Calendar currentCalendar = Calendar.getInstance();
                int yearsOfService = currentCalendar.get(Calendar.YEAR) - entryCalendar.get(Calendar.YEAR);
                if (currentCalendar.get(Calendar.DAY_OF_YEAR) < entryCalendar.get(Calendar.DAY_OF_YEAR)) {
                    yearsOfService--;
                }

                // Calculate incremented salary (e.g., 5% per year)
                double incrementRate = 0.05; // 5%
                return (long) (currentSalary * Math.pow(1 + incrementRate, yearsOfService));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return currentSalary; // Return current salary if calculation fails
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Paper.book().write(Prevalent.UserIdKey, "id");
                        Paper.book().write(Prevalent.UserPasswordKey, "pass");
                        Paper.book().write(Prevalent.ParentDB, "user");
                        finish();
                        //startActivity(new Intent(DoctorHomeActivity.this, LoginActivity.class));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

        this.moveTaskToBack(true);
    }
}
