package com.example.employee_system;

import android.content.Context;
import android.graphics.Color;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {
    private final Context context;
    private final List<FeedbackItem> items;

    private List<FeedbackItem> reviewList;

    public ReviewsAdapter(Context context, List<FeedbackItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_reviews_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final FeedbackItem item = items.get(position);


        holder.reveiwerName.setText("by_" + item.namefeedback);
        holder.reviewMessage.setText(item.messagefeedback);
        holder.reveiwDoctorName.setText(item.ratingdoctorname);
        holder.reveiwDoctorRating.setText("Rating : " + item.ratingfeedback);

        boolean isExpended = items.get(position).isExpanded();
        holder.expandableView.setVisibility(isExpended ? View.VISIBLE : View.GONE);



        // Set onClickListener for CheckBox
        holder.readCheckbox.setOnClickListener(v -> {
            boolean isChecked = holder.readCheckbox.isChecked();

            // Update the background color based on the new status
            if (isChecked) {
                holder.itemView.setBackgroundColor(Color.parseColor("#E0E0E0"));
            } else {
                holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView reveiwDoctorName, reviewMessage, reveiwDoctorRating, reveiwerName;
        public Button arrowBtn;

        LinearLayout expandableView;

        CardView cardView;
        CheckBox readCheckbox;



        public ViewHolder(View itemView) {
            super(itemView);

            reveiwDoctorName = itemView.findViewById(R.id.reveiwDoctorName);
            reviewMessage = itemView.findViewById(R.id.reviewMessage);
            reveiwDoctorRating = itemView.findViewById(R.id.reveiwDoctorRating);
            reveiwerName = itemView.findViewById(R.id.reveiwerName);

            arrowBtn = itemView.findViewById(R.id.arrowBtn);
            cardView = itemView.findViewById(R.id.cardView);
            expandableView = itemView.findViewById(R.id.expandableView);

            readCheckbox = itemView.findViewById(R.id.readCheckbox);

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
