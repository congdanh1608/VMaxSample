package com.sample.vmax.vmaxsample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by SilverWolf on 23/03/2017.
 */
public class OtherActivity extends BaseActivity {

    private RelativeLayout nativeAdLayout;
    private VMaxManager vMaxManager;
    private Button btnShowAds, btnCache;
    private String adspotId;
    private int sizeHeight, sizeWidth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);
        setProgressBar(findViewById(R.id.progressBar));

        initUI();
        initListener();

        adspotId = getIntent().getStringExtra("KEY");
        sizeHeight = getIntent().getIntExtra("KEY_HEIGHT", 80);
        sizeWidth = getIntent().getIntExtra("KEY_WIDTH", 320);
        vMaxManager = new VMaxManager(this, nativeAdLayout, adspotId, sizeWidth, sizeHeight);
    }

    private void initUI() {
        nativeAdLayout = (RelativeLayout) findViewById(R.id.nativeAdLayout);
        btnShowAds = (Button) findViewById(R.id.btnShowAds);
        btnCache = (Button) findViewById(R.id.btnCacheAds);
    }

    private void initListener() {
        btnCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vMaxManager.loadCached();
            }
        });

        btnShowAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initAds();
            }
        });
    }

    private void initAds() {
        if (!TextUtils.isEmpty(adspotId)) {
            vMaxManager.initCached();
        } else Toast.makeText(this, R.string.input_adspot_id, Toast.LENGTH_LONG).show();
    }
}
