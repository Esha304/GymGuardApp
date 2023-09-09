package com.example.gymguardapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MembershipPlansAdapter extends RecyclerView.Adapter<MembershipPlansAdapter.ViewHolder> {

    private Context context;
    private List<MembershipPlan> membershipPlans;
    private int selectedPosition = -1;

    public MembershipPlansAdapter(Context context, List<MembershipPlan> membershipPlans) {
        this.context = context;
        this.membershipPlans = membershipPlans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_membership_plan, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MembershipPlan plan = membershipPlans.get(position);
        holder.radioButton.setChecked(position == selectedPosition);
        holder.radioButton.setText(plan.getName());
        holder.textMonthly.setText(plan.getMonthly());
        holder.textYearly.setText(plan.getYearly());
        holder.textDetails.setText(plan.getDetails());
        holder.textExpiry.setText(plan.getExpiry());

        holder.radioButton.setOnClickListener(v -> {
            selectedPosition = position;
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return membershipPlans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;
        TextView textMonthly;
        TextView textYearly;
        TextView textDetails;
        TextView textExpiry;

        public ViewHolder(View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radioButtonPlan);
            textMonthly = itemView.findViewById(R.id.textMonthly);
            textYearly = itemView.findViewById(R.id.textYearly);
            textDetails = itemView.findViewById(R.id.textDetails);
            textExpiry = itemView.findViewById(R.id.textExpiry);
        }
    }
}
