package com.example.employee_system;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import io.paperdb.Paper;

public class AdminHomeActivity extends AppCompatActivity {

    private ImageView adddocButton, empCheckButton, editempButton, empDeleteDetails, adminLogout;

    @SuppressLint({"MissingInflatedId", "WrongViewCast"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        Paper.init(this);

        adddocButton = findViewById(R.id.adddocButton);
        empCheckButton = findViewById(R.id.empCheckButton);
        editempButton = findViewById(R.id.editempButton);
        empDeleteDetails = findViewById(R.id.empDeleteDetails);
        adminLogout = findViewById(R.id.adminLogout);


        adddocButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, AddEmployee.class);
                startActivity(intent);
            }
        });

        empCheckButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, Departments.class);
                startActivity(intent);
            }
        });

        editempButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, EmpList.class);
                startActivity(intent);
            }
        });

        empDeleteDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, EmpList
                        .class);
                startActivity(intent);
            }
        });

        adminLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminHomeActivity.this);
                builder.setMessage("Are you sure you want to logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Paper.book().write(Prevalent.UserIdKey, "id");
                                Paper.book().write(Prevalent.UserPasswordKey, "pass");
                                Paper.book().write(Prevalent.ParentDB, "user");
                                finish();
                                startActivity(new Intent(AdminHomeActivity.this, MainActivity.class));
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
                        //startActivity(new Intent(AdminHomeActivity.this, HomeActivity.class));
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
