package com.sample.vmax.vmaxsample;

import android.content.Context;
import android.content.MutableContextWrapper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vmax.android.ads.api.AdContainer;
import com.vmax.android.ads.api.VmaxAdSettings;
import com.vmax.android.ads.api.VmaxAdView;
import com.vmax.android.ads.common.VmaxAdListener;
import com.vmax.android.ads.exception.VmaxAdError;
import com.vmax.android.ads.nativeHelper.Infeed.VmaxNativeInfeed;
import com.vmax.android.ads.nativeads.NativeViewListener;

import java.util.HashMap;

/**
 * Created by SilverWolf on 23/03/2017.
 */
public class VMaxManager extends VmaxAdListener {
    private BaseActivity activity;
    private Context context;
    private VmaxAdView vmaxAdView;
    private RelativeLayout nativeAdLayout;
    private String adspotId;
    private VmaxAdSettings vmaxAdSettings;
    private int sizeWidth, sizeHeight;

    private boolean isOnlyCache = false, cacheAdClicked = false, loadAdClicked = false;

    public VMaxManager(BaseActivity activity, RelativeLayout nativeAdLayout, String adspotId, int sizeWidth, int sizeHeight) {
        this.activity = activity;
        this.context = activity;
        this.nativeAdLayout = nativeAdLayout;
        this.adspotId = adspotId;
        this.sizeWidth = sizeWidth;
        this.sizeHeight = sizeHeight;
    }

    public void initFirst() {
        Log.d("VMaxManager", "init first");

        vmaxAdView = new VmaxAdView(context, adspotId.trim(), VmaxAdView.UX_NATIVE);
        vmaxAdSettings = new VmaxAdSettings();
        vmaxAdSettings.setAdmobNativeExpressAdSize(sizeWidth, sizeHeight);
        vmaxAdView.setAdSettings(vmaxAdSettings);
        vmaxAdView.setAdListener(this);
        vmaxAdView.cacheAd();
        activity.showProgressBar();
    }

    public void onCacheAds(TextView textView) {
        initializeAdViewObjects();
        isOnlyCache = true;
        cacheAdClicked = true;
        loadAdClicked = false;

        vmaxAdView.setUxType(VmaxAdView.UX_NATIVE);
        vmaxAdView.setAdSpotId(adspotId.trim());
        vmaxAdView.cacheAd();
        vmaxAdView.setAdListener(this);
    }

    public void loadCached() {
        nativeAdLayout.removeAllViews();
        nativeAdLayout.setVisibility(View.GONE);
        if (vmaxAdView == null) {
            vmaxAdView = VmaxAdView.getMutableInstance(context, adspotId.trim(), VmaxAdView.UX_NATIVE);
        } else {
            vmaxAdView.setAdSpotId(adspotId);
        }
        if (vmaxAdView != null) {
            vmaxAdView.setUxType(VmaxAdView.UX_NATIVE);

            vmaxAdSettings = new VmaxAdSettings();
            vmaxAdSettings.setAdmobNativeExpressAdSize(sizeWidth, sizeHeight);
            vmaxAdView.setAdSettings(vmaxAdSettings);

            setListenerToAdview();
            vmaxAdView.cacheAd();
            activity.showProgressBar();
        }
    }

    public void initCached() {
        Log.d("VMaxManager", "init cached");
        AdContainer singleton = AdContainer.getInstance();
        vmaxAdView = singleton.getAdView(adspotId.trim());

        try {
            if (vmaxAdView != null) {
                setListenerToAdview();
                if (vmaxAdView.getContext() != null) {
                    Log.d("VMaxManager", "activity instance: " + (vmaxAdView.getContext() instanceof MutableContextWrapper));
                    ((MutableContextWrapper) vmaxAdView.getContext()).setBaseContext(context);
                }
                Log.d("VMaxManager", "Act2 Native In-feed");
                VmaxNativeInfeed vmaxNativeInfeed = new VmaxNativeInfeed(vmaxAdView);
                vmaxNativeInfeed.setNativeViewListener(new NativeViewListener() {
                    @Override
                    public void onAttachSuccess(ViewGroup viewGroup) {
                        nativeAdLayout.removeAllViews();
                        nativeAdLayout.setVisibility(View.VISIBLE);
                        nativeAdLayout.addView(viewGroup);
                    }

                    @Override
                    public void onAttachFailed(String errormsg) {
                        Log.i("VMaxManager", "onAttachFailed : " + errormsg);
                        nativeAdLayout.removeAllViews();
                        nativeAdLayout.setVisibility(View.GONE);
                    }
                });
                vmaxNativeInfeed.setNativeAd();
            } else {
                Toast.makeText(context, activity.getString(R.string.load__cache_beforce), Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setListenerToAdview(){
        vmaxAdView.setAdListener(new VmaxAdListener() {
            @Override
            public void onAdReady(VmaxAdView vmaxAdView) {
                activity.hideProgressBar();
                Log.d("vmax", "onAdReady Other activity");
                Toast.makeText(context, "onAdReady Other activity", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdError(VmaxAdError vmaxAdError) {
                Toast.makeText(context, vmaxAdError.getErrorDescription(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAdDismissed(VmaxAdView vmaxAdView) {
            }

            @Override
            public void onAdEnd(boolean b, long l) {
            }

            @Override
            public void onAdStarted(VmaxAdView vmaxAdView) {
                super.onAdStarted(vmaxAdView);
                activity.hideProgressBar();
            }
        });
    }

    @Override
    public void onAdReady(VmaxAdView adView) {
        Toast.makeText(context, "onAdReady in Main activity", Toast.LENGTH_LONG).show();
        activity.hideProgressBar();
        VmaxNativeInfeed vmaxNativeInfeed = new VmaxNativeInfeed(adView);
        vmaxNativeInfeed.setNativeViewListener(new NativeViewListener() {
            @Override
            public void onAttachSuccess(ViewGroup viewGroup) {
                nativeAdLayout.removeAllViews();
                nativeAdLayout.setVisibility(View.VISIBLE);
                nativeAdLayout.addView(viewGroup);
            }

            @Override
            public void onAttachFailed(String errormsg) {
                Log.i("VMaxManager", "onAttachFailed : " + errormsg);
                nativeAdLayout.removeAllViews();
                nativeAdLayout.setVisibility(View.GONE);
                activity.hideProgressBar();
            }
        });
        vmaxNativeInfeed.setNativeAd();
    }

    @Override
    public void onAdError(VmaxAdError vmaxAdError) {
        Toast.makeText(context, vmaxAdError.getErrorDescription(), Toast.LENGTH_LONG).show();
        Log.d("VMaxManager", vmaxAdError.getErrorDescription());
        activity.hideProgressBar();
    }

    @Override
    public void onAdDismissed(VmaxAdView adView) {

    }

    @Override
    public void onAdEnd(boolean b, long l) {

    }

    @Override
    public void onAdStarted(VmaxAdView vmaxAdView) {
        super.onAdStarted(vmaxAdView);
    }

    public void initializeAdViewObjects() {
        if (vmaxAdView == null) {
            vmaxAdView = VmaxAdView.getMutableInstance(context, adspotId.trim(), VmaxAdView.UX_INTERSTITIAL);
        } else {
            vmaxAdView.setAdSpotId(adspotId.trim());
        }
        HashMap<String, String> customData = new HashMap<>();
        customData.put("key1", "value1");
        customData.put("key2", "value2");
        vmaxAdView.setCustomData(customData);
        vmaxAdSettings = new VmaxAdSettings();
        vmaxAdView.setAdListener(this);
    }
}
