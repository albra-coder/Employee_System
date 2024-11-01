package com.example.employee_system;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class EmployeeListAdapter extends RecyclerView.Adapter<EmployeeListAdapter.ViewHolder> {
    private final Context context;
    private final List<EmployeeItem> items;

    public EmployeeListAdapter(Context context, List<EmployeeItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_employee_list_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final EmployeeItem item = items.get(position);

        holder.showName.setText(item.name);
        holder.showSpeciality.setText(item.speciality);
        holder.showDegree.setText(item.degree);
        holder.showPhone.setText(item.phone);
        holder.showEmail.setText(item.email);

        StorageReference storageRef =
                FirebaseStorage.getInstance().getReference();

        storageRef.child("Departments/Employees/").child(item.imageName).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(holder.showPicture);
            }
        });


        boolean isExpended = items.get(position).isExpanded();
        holder.expandableView.setVisibility(isExpended ? View.VISIBLE : View.GONE);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView showName, showPhone, showEmail, showSpeciality, showDegree;
        public ImageView showPicture;
        public Button getAppointment, Feedback;

        LinearLayout expandableView;
        Button arrowBtn;
        CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);

            arrowBtn = itemView.findViewById(R.id.arrowBtn);
            cardView = itemView.findViewById(R.id.cardView);
            expandableView = itemView.findViewById(R.id.expandableView);

            showPicture = itemView.findViewById(R.id.showPicture);
            showName = itemView.findViewById(R.id.showName);
            showSpeciality = itemView.findViewById(R.id.showSpeciality);
            showDegree = itemView.findViewById(R.id.showDegree);
            showPhone = itemView.findViewById(R.id.showPhone);
            showEmail = itemView.findViewById(R.id.showEmail);


            arrowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expandableView.getVisibility() == View.GONE) {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        expandableView.setVisibility(View.VISIBLE);
                        arrowBtn.setBackgroundResource(R.drawable.ic_keyboard_arrow_up_black_24dp);
                    } else {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        expandableView.setVisibility(View.GONE);
                        arrowBtn.setBackgroundResource(R.drawable.ic_keyboard_arrow_down_black_24dp);
                    }
                }
            });

        }
    }
}
