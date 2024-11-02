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

import io.paperdb.Paper;

public class EmployeeHomeActivity extends AppCompatActivity {

    private ImageView empProfileButton, empEditButton, empHoliday, doctorLogout,empNotification;
    private TextView doctorNameShow;
    private DatabaseReference myRef;
    private String DocNameShow;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_home);

        doctorNameShow = findViewById(R.id.employerNameShow); // Move this line up

        Paper.init(EmployeeHomeActivity.this);
        final String Docid = Paper.book().read(Prevalent.UserIdKey);

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
                Intent intent = new Intent(EmployeeHomeActivity.this, LeaveManagementActivity.class);
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
