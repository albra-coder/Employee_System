package com.example.employee_system;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.HashMap;
import java.util.Map;

public class LeaveManagementActivity extends AppCompatActivity {

    private EditText fullNameInput, workIdInput;
    private TextView remainingLeaveText, startDateText, endDateText;
    private Button bookLeaveButton;
    private Leave leave;
    private DatabaseReference databaseReference;
    private String startDate, endDate;
    private EditText searchWorkIdInput;
    private Button searchButton;
    private TextView searchResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_management);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("EmployeeLeave");

        // Initialize input fields and buttons
        fullNameInput = findViewById(R.id.fullNameInput);
        workIdInput = findViewById(R.id.workIdInput);
        remainingLeaveText = findViewById(R.id.remainingLeaveText);
        startDateText = findViewById(R.id.startDateText);
        endDateText = findViewById(R.id.endDateText);
        bookLeaveButton = findViewById(R.id.bookLeaveButton);

        // Initialize search input and button
        searchWorkIdInput = findViewById(R.id.searchWorkIdInput);
        searchButton = findViewById(R.id.searchButton);
        searchResults = findViewById(R.id.searchResults);

        // Set onClickListener for search button
        searchButton.setOnClickListener(v -> searchLeaveByWorkId());



        leave = new Leave();
        leave.setRemainingLeaveDays(30);

        // Set date pickers for start and end dates
        startDateText.setOnClickListener(v -> showDatePickerDialog((date) -> {
            startDate = date;
            startDateText.setText(date);
        }));

        endDateText.setOnClickListener(v -> showDatePickerDialog((date) -> {
            endDate = date;
            endDateText.setText(date);
        }));

        bookLeaveButton.setOnClickListener(v -> bookLeave());
    }

    private void searchLeaveByWorkId() {
        String workId = searchWorkIdInput.getText().toString().trim();

        if (workId.isEmpty()) {
            Toast.makeText(this, "Please enter a Work ID", Toast.LENGTH_SHORT).show();
            return;
        }

        Query query = databaseReference.orderByChild("workId").equalTo(workId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StringBuilder results = new StringBuilder();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot leaveSnapshot : dataSnapshot.getChildren()) {
                        Leave leave = leaveSnapshot.getValue(Leave.class);

                        // Append leave details to results StringBuilder
                        results.append("Full Name: ").append(leave.getFullName()).append("\n")
                                .append("Work ID: ").append(leave.getWorkId()).append("\n")
                                .append("Start Date: ").append(leave.getStartDate()).append("\n")
                                .append("End Date: ").append(leave.getEndDate()).append("\n")
                                .append("Days Requested: ").append(leave.getDaysRequested()).append("\n")
                                .append("Remaining Days: ").append(leave.getRemainingLeaveDays()).append("\n\n");
                    }
                } else {
                    results.append("No leave records found for Work ID: ").append(workId);
                }

                searchResults.setText(results.toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LeaveManagementActivity.this, "Failed to retrieve data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void showDatePickerDialog(DateSetListener dateSetListener) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                LeaveManagementActivity.this,
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = year1 + "-" + (month1 + 1) + "-" + dayOfMonth;
                    dateSetListener.onDateSet(selectedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void bookLeave() {
        String fullName = fullNameInput.getText().toString().trim();
        String workId = workIdInput.getText().toString().trim();

        if (fullName.isEmpty() || workId.isEmpty()) {
            Toast.makeText(this, "Please enter full name and work ID", Toast.LENGTH_SHORT).show();
            return;
        }

        if (startDate == null || endDate == null) {
            Toast.makeText(this, "Please select start and end dates", Toast.LENGTH_SHORT).show();
            return;
        }

        int daysRequested = calculateDaysBetween(startDate, endDate);

        if (daysRequested > 0 && leave.getRemainingLeaveDays() >= daysRequested) {
            leave.setFullName(fullName);
            leave.setWorkId(workId);
            leave.setStartDate(startDate);
            leave.setEndDate(endDate);
            leave.setDaysRequested(daysRequested);
            leave.setRemainingLeaveDays(leave.getRemainingLeaveDays() - daysRequested);

            String leaveId = databaseReference.push().getKey();
            leave.setLeaveId(leaveId);
            databaseReference.child(leaveId).setValue(leave);

            updateLeaveBalance();
            Toast.makeText(this, "Leave booked successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Invalid date range or insufficient leave days.", Toast.LENGTH_SHORT).show();
        }
    }

    private int calculateDaysBetween(String start, String end) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(start);
            Date endDate = sdf.parse(end);
            long differenceInMillis = endDate.getTime() - startDate.getTime();
            return (int) (differenceInMillis / (1000 * 60 * 60 * 24)) + 1;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void updateLeaveBalance() {
        remainingLeaveText.setText("Remaining Leave Days: " + leave.getRemainingLeaveDays());
    }

    interface DateSetListener {
        void onDateSet(String date);
    }
}
