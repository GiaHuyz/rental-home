package com.example.rentalhome.service;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.annotation.Nullable;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class StripeService {
    private String PulishableKey;
    private String SecretKey;
    private String CustomerId;
    private String EphemeralKey;
    private String ClientSecret;
    private PaymentSheet paymentSheet;
    private final Context context;
    private RequestQueue requestQueue;
    private String amount;

    public interface PaymentSheetResultListener {
        void onPaymentSuccess();
    }

    private PaymentSheetResultListener paymentSheetResultListener;


    public StripeService(Context context, long amount) {
        this.context = context;
        this.PulishableKey = "pk_test_51OIlIMJheuL6RU4wcP5xniZZBjfHMqS0YNxL4nVebQm4xKnMxXxPQOwSr46M6Lfkovfu5S5LsmKzulA0wHmQbTV200nPET5nVh";
        this.SecretKey = "sk_test_51OIlIMJheuL6RU4wLP31nN1gfzP6lZAW4cjzwN4eW3YGu8eEcrIX6C1Y8cXnOZWl86zgzpSNR8tCWeswzrJTMeOO00hhNeiG1m";
        this.requestQueue = Volley.newRequestQueue(context);
        this.amount = String.valueOf(amount);

        PaymentConfiguration.init(context, PulishableKey);
        paymentSheet = new PaymentSheet((ComponentActivity) context, paymentSheetResult -> {
            onPaymentSheetResult(paymentSheetResult);
        });
    }

    public void startTransactionProcess(PaymentSheetResultListener listener) {
        this.paymentSheetResultListener = listener;
        createCustomer();
    }

    private void createCustomer() {
        String url = "https://api.stripe.com/v1/customers";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            CustomerId = jsonResponse.getString("id");
                            createEphemeralKey(CustomerId);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SecretKey);
                return headers;
            }
        };

        requestQueue.add(request);
    }

    private void createEphemeralKey(String CustomerId) {
        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/ephemeral_keys",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject object = new JSONObject(response);
                            EphemeralKey = object.getString("id");
                            createPaymentIntent(CustomerId, EphemeralKey);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SecretKey);
                header.put("Stripe-Version", "2023-10-16");
                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", CustomerId);
                return params;
            }
        };

        requestQueue.add(request);
    }

    private void createPaymentIntent(String CustomerId, String ephemeralKey) {
        this.CustomerId = CustomerId;
        this.EphemeralKey = ephemeralKey;

        String url = "https://api.stripe.com/v1/payment_intents";

        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            ClientSecret = jsonResponse.getString("client_secret");
                            preparePaymentSheet(ClientSecret);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + SecretKey);
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("customer", CustomerId);
                params.put("amount", amount);
                params.put("currency", "vnd");
                params.put("payment_method_types[]", "card");
                return params;
            }
        };

        requestQueue.add(request);
    }

    private void preparePaymentSheet(String ClientSecret) {
        PaymentSheet.CustomerConfiguration customerConfig = new PaymentSheet.CustomerConfiguration(CustomerId, EphemeralKey);

        PaymentSheet.Configuration configuration = new PaymentSheet.Configuration.Builder("Rental-Home")
                .customer(customerConfig)
                .build();

        paymentSheet.presentWithPaymentIntent(ClientSecret, configuration);
    }

    private void onPaymentSheetResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            if (paymentSheetResultListener != null) {
                paymentSheetResultListener.onPaymentSuccess();
            }
        } else if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            Toast.makeText(context, "Payment Canceled", Toast.LENGTH_LONG).show();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            Toast.makeText(context, "Payment Failed", Toast.LENGTH_LONG).show();
        }
    }
}
