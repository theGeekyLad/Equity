package com.theinfiniteloop.thegeekylad.equity;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.io.File;

import io.mapwize.mapwize.LoadURLCallbackInterface;
import io.mapwize.mapwize.MWZAccountManager;
import io.mapwize.mapwize.MWZCoordinate;
import io.mapwize.mapwize.MWZDirection;
import io.mapwize.mapwize.MWZDirectionPointWrapper;
import io.mapwize.mapwize.MWZMapOptions;
import io.mapwize.mapwize.MWZMapView;
import io.mapwize.mapwize.MWZMapViewListener;
import io.mapwize.mapwize.MWZPlace;
import io.mapwize.mapwize.MWZUserPosition;
import io.mapwize.mapwize.MWZVenue;

public class PlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        if (!new File(getFilesDir(), "url.txt").exists()) {
            Toast.makeText(this, "Map not downloaded!", Toast.LENGTH_SHORT).show();
            finish();
        }

        WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(new RW().read(new File(getFilesDir(), "url.txt")));
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }

        });

    }

}
