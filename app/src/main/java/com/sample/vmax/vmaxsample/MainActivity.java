package com.sample.vmax.vmaxsample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

    private Button btnCacheAds, btnOpenActivity;
    private TextView tvCache;
    private EditText editAdspotId, editHeight, editWidth;
    private RelativeLayout nativeAdLayout;
    private int sizeWidth = 320, sizeHeight = 80;

    private VMaxManager vMaxManager;

    private String adspotId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setProgressBar(findViewById(R.id.progressBar));

        adspotId = getString(R.string.defaulID);

        initUI();
        initListener();
    }

    private void initUI() {
        btnCacheAds = (Button) findViewById(R.id.btnCacheAds);
        btnOpenActivity = (Button) findViewById(R.id.btnGoToOtherActivity);
        tvCache = (TextView) findViewById(R.id.tvCacheAds);
        editAdspotId = (EditText) findViewById(R.id.editAdspotId);
        editHeight = (EditText) findViewById(R.id.editHeight);
        editWidth = (EditText) findViewById(R.id.editWidth);
        nativeAdLayout = (RelativeLayout) findViewById(R.id.nativeAdLayout);
    }

    private void initListener() {
        btnCacheAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initAds();
                vMaxManager.onCacheAds(tvCache);
            }
        });

        btnOpenActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OtherActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("KEY", adspotId.trim());
                bundle.putInt("KEY_HEIGHT", sizeHeight);
                bundle.putInt("KEY_WIDTH", sizeWidth);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        editAdspotId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString()))
                    adspotId = "";
                else adspotId = s.toString();
            }
        });

        editWidth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()))
                    sizeWidth = Integer.parseInt(s.toString());
            }
        });

        editHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()))
                    sizeHeight = Integer.parseInt(s.toString());
            }
        });
    }

    private void initAds() {
        if (!TextUtils.isEmpty(adspotId)) {
            vMaxManager = new VMaxManager(this, nativeAdLayout, adspotId, sizeWidth, sizeHeight);
            vMaxManager.initFirst();
        } else Toast.makeText(this, R.string.input_adspot_id, Toast.LENGTH_LONG).show();
    }
}
