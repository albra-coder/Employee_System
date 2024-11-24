package com.example.employee_system;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class BookLeaveActivity extends AppCompatActivity {

    private EditText etFullName, etWorkId;
    private TextView tvStartDate, tvEndDate;
    private String startDate = "", endDate = "";
    private Spinner spinnerReason;
    private DatabaseReference databaseReference;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private String requestId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_leave);

        // Initialize views
        etFullName = findViewById(R.id.etFullName);
        etWorkId = findViewById(R.id.etWorkId);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        spinnerReason = findViewById(R.id.spinnerReason);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("leaveRequests");

        // Set up listeners for date selection
        tvStartDate.setOnClickListener(v -> showDatePicker(true));
        tvEndDate.setOnClickListener(v -> showDatePicker(false));

        // Set up the submit button listener
        btnSubmit.setOnClickListener(v -> submitLeaveRequest());

    }

    private void showDatePicker(boolean isStartDate) {
        // Initialize Calendar for the current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Show DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            String selectedDate = year1 + "-" + (month1 + 1) + "-" + dayOfMonth;

            // Update the appropriate field based on isStartDate
            if (isStartDate) {
                startDate = selectedDate;
                tvStartDate.setText("Start Date: " + selectedDate);
            } else {
                endDate = selectedDate;
                tvEndDate.setText("End Date: " + selectedDate);
            }
        }, year, month, day);

        // Restrict past dates
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void submitLeaveRequest() {
        // Retrieve input values
        String fullName = etFullName.getText().toString().trim();
        String workId = etWorkId.getText().toString().trim();
        String reason = spinnerReason.getSelectedItem().toString();  // Get the selected reason

        // Validate fields
        if (fullName.isEmpty() || workId.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || reason.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate date order
        if (startDate.compareTo(endDate) > 0) {
            Toast.makeText(this, "Start Date cannot be after End Date", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if leave duration exceeds 30 days
        if (isLeaveDurationExceedsLimit(startDate, endDate)) {
            Toast.makeText(this, "Leave duration cannot exceed 30 days", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a unique ID for the request
        String requestId = databaseReference.push().getKey();

        // Create a HashMap to represent the leave request
        HashMap<String, Object> leaveRequest = new HashMap<>();
        leaveRequest.put("fullName", fullName);
        leaveRequest.put("workId", workId);
        leaveRequest.put("startDate", startDate);
        leaveRequest.put("endDate", endDate);
        leaveRequest.put("reason", reason);
        leaveRequest.put("status", "Pending");

        // Submit the request to Firebase
        if (requestId != null) {
            databaseReference.child(requestId).setValue(leaveRequest)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Leave Request Submitted!", Toast.LENGTH_LONG).show();
                        clearFields();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private boolean isLeaveDurationExceedsLimit(String start, String end) {
        try {
            Date startDate = dateFormat.parse(start);
            Date endDate = dateFormat.parse(end);

            if (startDate != null && endDate != null) {
                long differenceInMillis = endDate.getTime() - startDate.getTime();
                long days = differenceInMillis / (1000 * 60 * 60 * 24);
                return days > 30; // Returns true if duration exceeds 30 days
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
    private void clearFields() {
        // Clear the input fields
        etFullName.setText("");
        etWorkId.setText("");
        tvStartDate.setText("Select Start Date");
        tvEndDate.setText("Select End Date");
        startDate = "";
        endDate = "";
    }
}
