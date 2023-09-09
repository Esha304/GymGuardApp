package com.example.gymguardapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MembershipPlansActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MembershipPlansAdapter adapter;
    private List<MembershipPlan> membershipPlans;
    private Button payBtn;
    String PAYPAL_CLIENT_ID = "ASAnZOubxXNoPUS9riptUPVlBWzD4CKQZTrKmIdYPEq1xF5BzP_3ZbMuCwRBe1nRW7ccsaaDXCpkSner";
    private static final String PAYPAL_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;
    int PAYPAL_REQUEST_CODE = 123;
    public static PayPalConfiguration configu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership_plans);

        recyclerView = findViewById(R.id.recyclerViewPlans);
        membershipPlans = generateMembershipPlans();
        adapter = new MembershipPlansAdapter(this, membershipPlans);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        configu = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX).clientId(PAYPAL_CLIENT_ID);

        payBtn = findViewById(R.id.payButton);
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupPayPal();
                processPayPalPayment();
            }
        });
    }

    private List<MembershipPlan> generateMembershipPlans() {
        List<MembershipPlan> plans = new ArrayList<>();

        // Plan 1
        plans.add(new MembershipPlan(
                "Basic Membership",
                "$29/month",
                "$290/year",
                "Includes access to cardio and weight areas",
                "Offer expires in 30 days"
        ));

        // Plan 2
        plans.add(new MembershipPlan(
                "Premium Membership",
                "$49/month",
                "$490/year",
                "Includes access to all areas, classes, and personal trainer sessions",
                "Offer expires in 15 days"
        ));

        // Plan 3
        plans.add(new MembershipPlan(
                "Elite Membership",
                "$99/month",
                "$999/year",
                "Includes all premium features and spa services",
                "Offer expires in 7 days"
        ));

        return plans;
    }

    private void setupPayPal() {
        configu = new PayPalConfiguration().environment(PAYPAL_ENVIRONMENT).clientId(PAYPAL_CLIENT_ID);

        Intent paypalServiceIntent = new Intent(this, PayPalService.class);
        paypalServiceIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configu);
        startService(paypalServiceIntent);
    }

    private void processPayPalPayment() {
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal("1.99"), "USD",
                " Membership", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, com.paypal.android.sdk.payments.PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, configu);
        intent.putExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PAYPAL_REQUEST_CODE){
            PaymentConfirmation confirmation = data.getParcelableExtra(com.paypal.android.sdk.payments.PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            if(confirmation != null){
                try {
                    String paymentDetails = confirmation.toJSONObject().toString();
                    JSONObject object = new JSONObject(paymentDetails);
                    navigateToMembershipActivity();
                } catch (JSONException e) {
                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    navigateToMembershipActivity();
                }
            } else if (requestCode == Activity.RESULT_CANCELED){
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                navigateToMembershipActivity();
            }
        } else if (requestCode == com.paypal.android.sdk.payments.PaymentActivity.RESULT_EXTRAS_INVALID){
            Toast.makeText(this, "Invalid Payment", Toast.LENGTH_SHORT).show();
            navigateToMembershipActivity();
        }
    }

    private void navigateToMembershipActivity() {
        Intent intent = new Intent(this, MembershipPlansActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        // Handle the back button press here
        // You can perform any desired action, such as returning to the previous activity or showing a confirmation dialog
        // For example, if you want to go back to the previous activity:
        super.onBackPressed(); // This will close the current activity and return to the previous one.
    }

}