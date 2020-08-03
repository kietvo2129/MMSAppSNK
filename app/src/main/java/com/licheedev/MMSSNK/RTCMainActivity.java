package com.licheedev.MMSSNK;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RTCMainActivity extends AppCompatActivity {

    WebView mWebView;
    String URL = "http://ssmes.autonsi.com/Display/Camera1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_t_c_main);
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.loadUrl(URL);

        Intent aa = getIntent();
//
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.setWebChromeClient(new WebChromeClient());
//        mWebView.getSettings().setLoadWithOverviewMode(true);


//        String url = URL;
//        try {
//            Intent i = new Intent("android.intent.action.MAIN");
//            i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
//            i.addCategory("android.intent.category.LAUNCHER");
//            i.setData(Uri.parse(url));
//            startActivity(i);
//        }
//        catch(ActivityNotFoundException e) {
//            // Chrome is not installed
//            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//            startActivity(i);
//        }

//        String newUA= "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.45 Safari/535.19";
//        mWebView.getSettings().setUserAgentString(newUA);
////
////        mWebView.getSettings().setJavaScriptEnabled(true);
////        mWebView.getSettings().setLoadWithOverviewMode(true);
////        mWebView.getSettings().setUseWideViewPort(true);
////
////        mWebView.getSettings().setSupportZoom(true);
////        mWebView.getSettings().setBuiltInZoomControls(true);
////        mWebView.getSettings().setDisplayZoomControls(false);
////
////        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
////        mWebView.setScrollbarFadingEnabled(false);
//
//
//        mWebView.setWebChromeClient(new WebChromeClient() {
//            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//            @Override
//            public void onPermissionRequest(final PermissionRequest request) {
//                request.grant(request.getResources());
//            }
//        });
//
//        if (ContextCompat.checkSelfPermission(RTCMainActivity.this,
//                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//
//            ActivityCompat.requestPermissions(RTCMainActivity.this,
//                    new String[]{Manifest.permission.RECORD_AUDIO},1);
//        }
//        mWebView.loadUrl("https://e9ac7eeb.ngrok.io/");
//
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 1) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(RTCMainActivity.this, "permission granted", Toast.LENGTH_SHORT).show();
//                // perform your action here
//
//                mWebView.loadUrl("https://e9ac7eeb.ngrok.io/");
//            } else {
//                Toast.makeText(RTCMainActivity.this, "permission not granted", Toast.LENGTH_SHORT).show();
//            }
//        }
//
    }

}
