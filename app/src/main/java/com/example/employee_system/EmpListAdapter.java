package com.example.employee_system;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EmpListAdapter extends RecyclerView.Adapter<EmpListAdapter.ViewHolder> {
    private List<EmployeeListClass> empList;
    private Context context;

    public EmpListAdapter(Context context, List<EmployeeListClass> empList) {
        this.context = context;
        this.empList = empList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_employee_leave, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        EmployeeListClass list = empList.get(position);
        holder.nameTextView.setText(list.getName());
        holder.workIdTextView.setText(list.getId());
        holder.emailTypeTextView.setText(list.getEmail());
        holder.degreeTextView.setText(list.getDegree());
        holder.passwordTextView.setText(list.getPassword());
        holder.phoneTextView.setText(list.getPhone());
        holder.specialityTextView.setText(list.getSpeciality());

        // Handle Edit Button click
        holder.idBtnEditDetails.setOnClickListener(v -> {
            // Show edit dialog or activity
            showEditDialog(list, position);
        });

        // Handle Delete Button click
        holder.idBtnDeleteDetails.setOnClickListener(v -> {
            showDeleteConfirmationDialog(list, position);
        });

    }


    @Override
    public int getItemCount() {
        return empList.size();
    }

    // Show edit dialog or start new activity for editing
    private void showEditDialog(EmployeeListClass empList, int position) {
        // Example using an AlertDialog for editing
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Leave");

        // Add custom layout for editing
        View editView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_leave, null);
        EditText nameEditText = editView.findViewById(R.id.editName);
        EditText workIdEditText = editView.findViewById(R.id.editWorkId);
        EditText EmailEditText = editView.findViewById(R.id.editEmail);
        EditText DegreeEditText = editView.findViewById(R.id.editDegree);
        EditText PasswordEditText = editView.findViewById(R.id.editPassword);
        EditText PhoneEditText = editView.findViewById(R.id.editPhone);
        EditText SpecialityEditText = editView.findViewById(R.id.editSpeciality);
        // Populate with current data
        nameEditText.setText(empList.getName());
        workIdEditText.setText(empList.getId());
        EmailEditText.setText(empList.getEmail());
        DegreeEditText.setText(empList.getEmail());
        PasswordEditText.setText(empList.getPassword());
        PhoneEditText.setText(empList.getPhone());
        SpecialityEditText.setText(empList.getSpeciality());

        builder.setView(editView);

        // Set Save button in dialog
        builder.setPositiveButton("Save", (dialog, which) -> {
            // Update item in list and notify adapter
            empList.setName(nameEditText.getText().toString());
            empList.setId(workIdEditText.getText().toString());
            empList.setEmail(EmailEditText.getText().toString());
            empList.setDegree(DegreeEditText.getText().toString());
            empList.setPassword(PasswordEditText.getText().toString());
            empList.setPhone(PhoneEditText.getText().toString());
            empList.setSpeciality(SpecialityEditText.getText().toString());

            notifyItemChanged(position);
            // Optionally, save changes to Firebase or other database
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.create().show();
    }

    // Show delete confirmation dialog
    private void showDeleteConfirmationDialog(EmployeeListClass list, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete Leave")
                .setMessage("Are you sure you want to delete this employee?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    // Remove the item from the list
                    empList.remove(position);
                    notifyItemRemoved(position);
                    // Optionally, delete from Firebase or other database
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, workIdTextView, emailTypeTextView, degreeTextView, passwordTextView, phoneTextView, specialityTextView;
        Button idBtnEditDetails,idBtnDeleteDetails;
        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            workIdTextView = itemView.findViewById(R.id.workIdTextView);
            emailTypeTextView = itemView.findViewById(R.id.emailTypeTextView);
            degreeTextView = itemView.findViewById(R.id.degreeTextView);
            passwordTextView = itemView.findViewById(R.id.passwordTextView);
            phoneTextView = itemView.findViewById(R.id.phoneTextView);
            specialityTextView = itemView.findViewById(R.id.specialityTextView);
            idBtnEditDetails = itemView.findViewById(R.id.idBtnEditDetails);
            idBtnDeleteDetails = itemView.findViewById(R.id.idBtnDeleteDetails);
        }
    }
}
