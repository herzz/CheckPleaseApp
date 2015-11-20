package com.zahirherz.checkplease.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.zahirherz.checkplease.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.InjectView;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String PREFS_FILE = "com.zahirherz.checkplease.preferences";
    private static final String KEY_EDITTEXT = "key_edittext";

    private SharedPreferences.Editor mEditor;
    private SharedPreferences mSharedPreferences;

    @InjectView(R.id.zipcodeTxt) TextView mZipCodeTxt;
    @InjectView(R.id.bill) TextView mBill;
    @InjectView(R.id.tipPercent) TextView mTipPercent;
    @InjectView(R.id.startButton) Button mStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);

        mSharedPreferences = getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
        mEditor.apply();

        String editTextString = mSharedPreferences.getString(KEY_EDITTEXT, "");
        mZipCodeTxt.setText(editTextString);

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zipcode = mZipCodeTxt.getText().toString();
                String bill = mBill.getText().toString();
                String tipPercent = mTipPercent.getText().toString();

                String regex = "[0-9]{5}";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(zipcode);

                if(bill.matches("")) {
                    Toast.makeText(MainActivity.this, getString(R.string.bill_error), Toast.LENGTH_SHORT).show();
                } else if(!matcher.matches()) {
                    Toast.makeText(MainActivity.this, getString(R.string.zipcode_error), Toast.LENGTH_SHORT).show();
                } else if(tipPercent.matches("")) {
                    Toast.makeText(MainActivity.this, getString(R.string.tip_error), Toast.LENGTH_SHORT).show();
                } else {
                    startStory(zipcode, bill, tipPercent);
                }
            }
        });

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }


    private void startStory(String zipcode, String bill, String tipPercent) {
        Intent intent = new Intent(MainActivity.this,StoryActivity.class);
        intent.putExtra("zipcode", zipcode);
        intent.putExtra("bill", bill);
        intent.putExtra("tipPercent", tipPercent);

        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mEditor.putString(KEY_EDITTEXT, mZipCodeTxt.getText().toString());
        mEditor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBill.setText("");
        mBill.requestFocus();
        mTipPercent.setText("");
    }
}
