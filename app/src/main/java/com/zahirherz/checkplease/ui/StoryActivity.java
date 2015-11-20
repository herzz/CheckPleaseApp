package com.zahirherz.checkplease.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.zahirherz.checkplease.taxinfo.Forecast;
import com.zahirherz.checkplease.R;
import com.zahirherz.checkplease.taxinfo.Tax;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class StoryActivity extends Activity {

    public static final String TAG = StoryActivity.class.getSimpleName();
    private Forecast mForecast;
    private String mZipcode;
    private String mBill;
    private String mTipPercent;

    @InjectView(R.id.initial_bill) TextView mInitialBill;
    @InjectView(R.id.tip) TextView mTip;
    @InjectView(R.id.total) TextView mTotal;
    @InjectView(R.id.progressBar) ProgressBar mProgressBar;
    @InjectView(R.id.startOverButton) Button mStartOverButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        Intent intent = getIntent();

        ButterKnife.inject(this);

        mStartOverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mProgressBar.setVisibility(View.INVISIBLE);
        mZipcode = intent.getStringExtra("zipcode");
        mBill = intent.getStringExtra("bill");
        mTipPercent = intent.getStringExtra("tipPercent");

        getTaxRate();

    }

    private void toggleRefresh() {
        Log.d(TAG, mProgressBar.getVisibility() + "//");
        if(mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void getTaxRate() {
        String api = "kgWY7oD2axrhKPR4kIRuPwQy0UnTyuyj/62QLdWMcPczR0CNbtVSUqGLgdXwgxrXimr0fwbU7Pg" +
                "lKWRwqiu1wQ==";
        String taxRateURL = "https://taxrates.api.avalara.com:443/postal?country=usa&postal="+ mZipcode +
                "&apikey=kgWY7oD2axrhKPR4kIRuPwQy0UnTyuyj%2F62QLdWMcPczR0CNbtVSUqGLgdXwgxrXimr0fwb" +
                "U7PglKWRwqiu1wQ%3D%3D\n";
        if(isNetworkAvailable()) {
            toggleRefresh();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(taxRateURL)
                    .build();

            Call call = client.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });
                    alertUser();
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            toggleRefresh();
                        }
                    });

                    try {
                        String jsonData = response.body().string();
                        Log.v(TAG, "onResponse "+ jsonData);
                        if(response.isSuccessful()) {
                            Log.v(TAG, "// " + jsonData.toString());

                            mForecast = parseForecaseDetails(jsonData);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updateDisplay();
                                }
                            });
                        } else {
                            alertUser();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Exception caught: " + e);
                    } catch (JSONException e) {
                        Log.e(TAG, "JSONException caught:" + e);
                    }
                }
            });
        }
    }

    private Forecast parseForecaseDetails(String jsonData) throws JSONException {
        Forecast forecast = new Forecast();
        forecast.setTax(getTaxDetails(jsonData));
        return forecast;
    }

    private Tax getTaxDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);
        double taxrate = forecast.getDouble("totalRate");

        Tax tax = new Tax();
        tax.setRate(taxrate);

        return tax;
    }

    private void updateDisplay() {
        Tax tax = mForecast.getTax();
        String mTaxRate = tax.getRate() + "";

        double bill = Double.parseDouble(mBill);
        double taxRate = Double.parseDouble(mTaxRate);
        double tipPercent = Double.parseDouble(mTipPercent) /100 ;
        double beforetax;
        double tip;
        double total;

        String purchaseLabel;
        String tipLabel;
        String totalLabel;

        purchaseLabel = "$" + Double.toString(bill);
        beforetax = bill * taxRate;

        tip = (bill - beforetax) * tipPercent;
        tip = roundTwoDecimals(tip);
        tipLabel = "$" + Double.toString(tip);

        total = bill + tip;
        total = roundTwoDecimals(total);
        totalLabel = "$" + Double.toString(total);

        mInitialBill.setText(purchaseLabel);
        mTip.setText(tipLabel);
        mTotal.setText(totalLabel);

    }

    private double roundTwoDecimals(double d) {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if(networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }

    private void alertUser() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

}
