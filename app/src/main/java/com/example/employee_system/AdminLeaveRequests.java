package com.example.employee_system;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminLeaveRequests extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LeaveRequestAdapter adapter;
    private ArrayList<LeaveRequest> leaveRequests, filteredRequests;
    private DatabaseReference leaveRequestsRef;
    private ProgressBar progressBar;
    private TextView emptyMessage;
    private EditText searchLeaveRequests;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_leave_requests);

        recyclerView = findViewById(R.id.recyclerViewLeaveRequests);
        progressBar = findViewById(R.id.progressBar);
        emptyMessage = findViewById(R.id.emptyMessage);
        searchLeaveRequests = findViewById(R.id.searchLeaveRequests);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        leaveRequests = new ArrayList<>();
        filteredRequests = new ArrayList<>();
        adapter = new LeaveRequestAdapter(filteredRequests);
        recyclerView.setAdapter(adapter);

        leaveRequestsRef = FirebaseDatabase.getInstance().getReference("leaveRequests");

        fetchLeaveRequests();

        // Search functionality
        searchLeaveRequests.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterLeaveRequests(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void fetchLeaveRequests() {
        progressBar.setVisibility(View.VISIBLE);
        leaveRequestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
                leaveRequests.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    LeaveRequest request = data.getValue(LeaveRequest.class);
                    if (request != null) {
                        request.setRequestId(data.getKey());
                        leaveRequests.add(request);
                    }
                }
                filterLeaveRequests(searchLeaveRequests.getText().toString()); // Apply the current filter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AdminLeaveRequests.this, "Failed to fetch leave requests: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterLeaveRequests(String query) {
        filteredRequests.clear();
        if (query.isEmpty()) {
            filteredRequests.addAll(leaveRequests);
        } else {
            for (LeaveRequest request : leaveRequests) {
                if (request.getFullName().toLowerCase().contains(query.toLowerCase())) {
                    filteredRequests.add(request);
                }
            }
        }

        adapter.notifyDataSetChanged();

        // Toggle visibility of empty message
        if (filteredRequests.isEmpty()) {
            emptyMessage.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyMessage.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}
