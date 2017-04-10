package com.sample.vmax.vmaxsample;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by SilverWolf on 23/03/2017.
 */
public class BaseActivity extends AppCompatActivity {
    private View view;
    protected void setProgressBar(View view){
        this.view = view;
    }

    protected void showProgressBar(){
        if (view !=null) view.setVisibility(View.VISIBLE);
    }

    protected void hideProgressBar(){
        if (view !=null) view.setVisibility(View.GONE);
    }
}
