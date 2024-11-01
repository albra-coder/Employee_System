package com.example.employee_system;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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

public class LoginActivity extends AppCompatActivity {

    private EditText userID, userPass;
    private Switch adminSwitch;
    private Button login;
    private TextView loginCancel;
    private String parentDbName = "Doctors";
    private ProgressDialog loadingBar;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userID = findViewById(R.id.userID);
        userPass = findViewById(R.id.userPass);
        adminSwitch = findViewById(R.id.adminSwitch);
        login = findViewById(R.id.login);
        loadingBar = new ProgressDialog(this);
        loginCancel = findViewById(R.id.loginCancel);

        Paper.init(this);

        login.setOnClickListener(view -> {
            LoginAllow();
        });

        loginCancel.setOnClickListener(view -> {
            userID.setText(null);
            userPass.setText(null);
        });
    }

    public void LoginAllow() {
        final String UserID = sanitizeUserID(userID.getText().toString());
        final String UserPass = userPass.getText().toString();

        if (TextUtils.isEmpty(UserID)) {
            Toast.makeText(LoginActivity.this, "Please enter a valid email.", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(UserPass)) {
            Toast.makeText(LoginActivity.this, "Please enter a valid password.", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccount(UserID, UserPass);
        }
    }

    public void AllowAccount(final String UserID, final String UserPass) {

        Paper.book().write(Prevalent.UserIdKey, UserID);
        Paper.book().write(Prevalent.UserPasswordKey, UserPass);

        if (adminSwitch.isChecked()) {
            parentDbName = "Admin";
            Paper.book().write(Prevalent.ParentDB, parentDbName);
        } else {
            parentDbName = "Employees";
            Paper.book().write(Prevalent.ParentDB, parentDbName);
        }

        DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(UserID).exists()) {
                    EmployeesItem usersData = dataSnapshot.child(parentDbName).child(UserID).getValue(EmployeesItem.class);

                    if (usersData != null && usersData.getId().equals(UserID)) {
                        if (usersData.getPassword().equals(UserPass)) {
                            loadingBar.dismiss();
                            Intent intent;
                            if (parentDbName.equals("Admin")) {
                                Toast.makeText(LoginActivity.this, "Welcome Admin, you are logged in successfully.", Toast.LENGTH_SHORT).show();
                                intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                            } else {
                                Toast.makeText(LoginActivity.this, "Logged in successfully.", Toast.LENGTH_SHORT).show();
                                intent = new Intent(LoginActivity.this, EmployeeHomeActivity.class);
                                intent.putExtra("DoctorID", UserID);
                            }
                            startActivity(intent);
                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Password is incorrect.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Account with this ID does not exist.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loadingBar.dismiss();
                Toast.makeText(LoginActivity.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String sanitizeUserID(String userID) {
        return userID.replace(".", "_")
                .replace("#", "_")
                .replace("$", "_")
                .replace("[", "_")
                .replace("]", "_");
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> finish())
                .setNegativeButton("No", (dialog, id) -> dialog.cancel())
                .show();
    }
}
