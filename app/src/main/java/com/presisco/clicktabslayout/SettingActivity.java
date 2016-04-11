package com.presisco.clicktabslayout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Switch;

public class SettingActivity extends AppCompatActivity {
    Switch mSwitch;
    EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        mSwitch = (Switch) findViewById(R.id.evenTabsSwitch);
        mEditText = (EditText) findViewById(R.id.tabsCountEdit);
        mEditText.setText(getIntent().getIntExtra("tabs_count", 4) + "");
        mSwitch.setChecked(getIntent().getBooleanExtra("is_even", true));
    }

    @Override
    public void onBackPressed() {
        Intent intent = getIntent();
        intent.putExtra("tabs_count", Integer.parseInt(mEditText.getText().toString().trim()));
        intent.putExtra("is_even", mSwitch.isChecked());
        setResult(0, intent);
        finish();
    }
}
