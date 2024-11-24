package com.example.employee_system;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LeaveRequestAdapter extends RecyclerView.Adapter<LeaveRequestAdapter.ViewHolder> {

    private final ArrayList<LeaveRequest> leaveRequests;

    public LeaveRequestAdapter(ArrayList<LeaveRequest> leaveRequests) {
        this.leaveRequests = leaveRequests;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leave_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LeaveRequest request = leaveRequests.get(position);
        holder.textFullName.setText("Name: " + request.getFullName());
        holder.textWorkId.setText("Work ID: " + request.getWorkId());
        holder.textDates.setText("Leave: " + request.getStartDate() + " to " + request.getEndDate());
        holder.textStatus.setText("Status: " + request.getStatus());
        holder.textReason.setText("Reason: " + request.getReason());

        holder.buttonApprove.setOnClickListener(view -> {
            if (request.isDurationValid()) {
                updateStatus(request, "Approved", holder);
            } else {
                Toast.makeText(holder.itemView.getContext(), "Leave duration cannot exceed 30 days.", Toast.LENGTH_SHORT).show();
            }
        });

        holder.buttonDecline.setOnClickListener(view -> {
            if (request.isDurationValid()) {
                updateStatus(request, "Declined", holder);
            } else {
                Toast.makeText(holder.itemView.getContext(), "Leave duration cannot exceed 30 days.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateStatus(LeaveRequest request, String status, ViewHolder holder) {
        DatabaseReference leaveRequestsRef = FirebaseDatabase.getInstance().getReference("leaveRequests");
        leaveRequestsRef.child(request.getRequestId()).child("status").setValue(status)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(holder.itemView.getContext(), "Request " + status, Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(holder.itemView.getContext(), "Failed to update status", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getItemCount() {
        return leaveRequests.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textFullName, textWorkId, textDates, textStatus, textReason;
        Button buttonApprove, buttonDecline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textFullName = itemView.findViewById(R.id.textFullName);
            textWorkId = itemView.findViewById(R.id.textWorkId);
            textDates = itemView.findViewById(R.id.textDates);
            textStatus = itemView.findViewById(R.id.textStatus);
            textReason = itemView.findViewById(R.id.textReason);
            buttonApprove = itemView.findViewById(R.id.buttonApprove);
            buttonDecline = itemView.findViewById(R.id.buttonDecline);
        }
    }
}
