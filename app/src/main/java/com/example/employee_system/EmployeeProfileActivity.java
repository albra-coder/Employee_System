package com.example.employee_system;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class EmployeeProfileActivity extends AppCompatActivity {
    private TextView showProfileName, nameText, specialityText, degreeText, phoneText, emailText, passText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_profile);
        Paper.init(this);

        // Initialize TextViews
        showProfileName = findViewById(R.id.showProfileName);
        nameText = findViewById(R.id.nameText);
        specialityText = findViewById(R.id.specialityText);
        degreeText = findViewById(R.id.degreeText);
        phoneText = findViewById(R.id.phoneText);
        emailText = findViewById(R.id.emailText);
        passText = findViewById(R.id.passText);

        // Fetch and display account details
        searchAcc();
    }

    public void searchAcc() {
        String ID = Paper.book().read(Prevalent.UserIdKey);
        String Pass = Paper.book().read(Prevalent.UserPasswordKey);
        String ParentDB = Paper.book().read(Prevalent.ParentDB);

        if (!ID.equals("id") && !Pass.equals("pass") && !ParentDB.equals("user")) {
            if (!TextUtils.isEmpty(ID) && !TextUtils.isEmpty(Pass) && !TextUtils.isEmpty(ParentDB)) {
                DatabaseReference RootRef = FirebaseDatabase.getInstance().getReference().child("Employees").child(ID);
                RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            nameText.setText(snapshot.child("name").getValue(String.class));
                            specialityText.setText(snapshot.child("speciality").getValue(String.class));
                            degreeText.setText(snapshot.child("degree").getValue(String.class));
                            phoneText.setText(snapshot.child("phone").getValue(String.class));
                            emailText.setText(snapshot.child("email").getValue(String.class));
                            passText.setText(snapshot.child("password").getValue(String.class));

                            showProfileName.setText(snapshot.child("name").getValue(String.class));
                        } else {
                            Toast.makeText(EmployeeProfileActivity.this, "Profile not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(EmployeeProfileActivity.this, "Error retrieving data", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish(); // Exit the activity when back button is pressed
    }
}
