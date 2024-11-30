package com.example.employee_system;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;

import io.paperdb.Paper;

public class EmployeeEditProfile extends AppCompatActivity {
    private Button updateProfile, goBackProfile, saveUpdatedProfile, cancelUpdating;
    private TextView showProfileName, nameText, specialityText, dateText, salaryText, emailText, passText;
    private EditText name, speciality, date, salary, email, password;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_edit_profile);
        Paper.init(this);

        updateProfile = findViewById(R.id.updateProfile);
        goBackProfile = findViewById(R.id.goBackProfile);
        saveUpdatedProfile = findViewById(R.id.saveUpdatedProfile);
        cancelUpdating = findViewById(R.id.cancelUpdating);

        showProfileName = findViewById(R.id.showProfileName);
        nameText = findViewById(R.id.nameText);
        specialityText = findViewById(R.id.specialityText);
        dateText = findViewById(R.id.dateText);
        emailText = findViewById(R.id.emailText);
        passText = findViewById(R.id.passText);


        name = findViewById(R.id.name);
        speciality = findViewById(R.id.speciality);
        date = findViewById(R.id.date);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeAllVisible();

            }
        });

        goBackProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        cancelUpdating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeAllGone();
            }
        });


        searchAcc();
        saveUpdatedProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ID = Paper.book().read(Prevalent.UserIdKey);
                String Pass = Paper.book().read(Prevalent.UserPasswordKey);
                String ParentDB = Paper.book().read(Prevalent.ParentDB);

                if (ID != "id" && Pass != "pass" && ParentDB != "user") {
                    if (!TextUtils.isEmpty(ID) && !TextUtils.isEmpty(Pass) && !TextUtils.isEmpty(ParentDB)) {
                        final DatabaseReference myRef;
                        myRef = FirebaseDatabase.getInstance().getReference().child("Employees").child(ID);

                        HashMap<String, Object> updatedValues = new HashMap<>();

                        updatedValues.put("name", name.getText().toString());
                        updatedValues.put("speciality", speciality.getText().toString());
                        updatedValues.put("email", email.getText().toString());
                        updatedValues.put("password", password.getText().toString());

                        myRef.updateChildren(updatedValues);
                        Toast.makeText(EmployeeEditProfile.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                        makeAllGone();
                        finish();
                        startActivity(getIntent());
                    }
                }

            }
        });

        if (Prevalent.ParentDB.isEmpty() || Prevalent.UserIdKey.isEmpty() || Prevalent.UserPasswordKey.isEmpty()) {
            Paper.book().write(Prevalent.UserIdKey, "id");
            Paper.book().write(Prevalent.UserPasswordKey, "pass");
            Paper.book().write(Prevalent.ParentDB, "user");
        }
    }

    public void searchAcc() {
        String ID = Paper.book().read(Prevalent.UserIdKey);
        String Pass = Paper.book().read(Prevalent.UserPasswordKey);
        String ParentDB = Paper.book().read(Prevalent.ParentDB);

        if (ID != "id" && Pass != "pass" && ParentDB != "user") {
            if (!TextUtils.isEmpty(ID) && !TextUtils.isEmpty(Pass) && !TextUtils.isEmpty(ParentDB)) {
                final DatabaseReference RootRef;
                RootRef = FirebaseDatabase.getInstance().getReference().child("Employees").child(ID);
                RootRef.addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            showProfileName.setText(getSafeValue(snapshot, "name"));
                            nameText.setText(getSafeValue(snapshot, "name"));
                            specialityText.setText(getSafeValue(snapshot, "speciality"));
                            emailText.setText(getSafeValue(snapshot, "email"));
                            passText.setText(getSafeValue(snapshot, "password"));

                            name.setText(getSafeValue(snapshot, "name"));
                            speciality.setText(getSafeValue(snapshot, "speciality"));

                            email.setText(getSafeValue(snapshot, "email"));
                            password.setText(getSafeValue(snapshot, "password"));
                        } else {
                            Toast.makeText(EmployeeEditProfile.this, "No profile data found.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    // Helper method to safely get a String value
                    private String getSafeValue(DataSnapshot snapshot, String key) {
                        return snapshot.child(key).getValue() != null ? snapshot.child(key).getValue().toString() : "";
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
    }

    public void makeAllVisible() {
        nameText.setVisibility(View.GONE);
        specialityText.setVisibility(View.GONE);
        emailText.setVisibility(View.GONE);
        passText.setVisibility(View.GONE);

        updateProfile.setVisibility(View.GONE);
        goBackProfile.setVisibility(View.GONE);


        name.setVisibility(View.VISIBLE);
        speciality.setVisibility(View.VISIBLE);

        email.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);

        saveUpdatedProfile.setVisibility(View.VISIBLE);
        cancelUpdating.setVisibility(View.VISIBLE);
    }

    public void makeAllGone() {
        nameText.setVisibility(View.VISIBLE);
        specialityText.setVisibility(View.VISIBLE);
        emailText.setVisibility(View.VISIBLE);
        passText.setVisibility(View.VISIBLE);

        updateProfile.setVisibility(View.VISIBLE);
        goBackProfile.setVisibility(View.VISIBLE);

        name.setVisibility(View.GONE);
        speciality.setVisibility(View.GONE);
        email.setVisibility(View.GONE);
        password.setVisibility(View.GONE);

        saveUpdatedProfile.setVisibility(View.GONE);
        cancelUpdating.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Finish the activity only when the user clicks "Yes"
                        EmployeeEditProfile.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Dismiss the dialog without finishing the activity
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
