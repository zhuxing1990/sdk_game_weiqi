package com.vunke.sdk_game_weiqi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vunke.sdk_game_weiqi.javascriptInterface.JavaScriptObject;
import com.vunke.sdk_game_weiqi.modle.StartAppsBean;
import com.vunke.sdk_game_weiqi.util.Utils;

import org.json.JSONObject;

public class WeiQiWebActivity extends AppCompatActivity {
    private static final String TAG = "WeiQiWebActivity";
    private WebView web;
    private ProgressDialog dialog = null;
    private long exitTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weiqiweb);
        initWebView();
        getAppIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getAppIntent(intent);
    }
//    private String LoadUrl = "http://124.232.136.236:8099/GoGame/indexNoResponse.html";
    private String LoadUrl = "http://120.76.235.37:9091/GoGame/indexNoResponse.html";
    private void getAppIntent(Intent intent) {
        Log.i(TAG, "getAppIntent: ");
        if (null == intent){
            return;
        }
        if (intent.hasExtra("startApp")){
            String jsonData = intent.getStringExtra("startApp");
            Log.i(TAG, "getAppIntent: jsonData:"+jsonData);
            try {
                StartAppsBean startAppBean = new Gson().fromJson(jsonData, StartAppsBean.class);
                StartAppsBean.StartAppBean startApp= startAppBean.getStartApp();
                String s = "";
                if (startApp.getJsonData()!=null){
                    s = new Gson().toJson(startApp.getJsonData());
                }
                Log.i(TAG, "getAppIntent: jsonData:"+s);
                Utils.StartAPP(startApp.getPackageName(),startApp.getClassName(),s,getApplicationContext());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if (intent.hasExtra("LoadUrl")){
            LoadUrl = intent.getStringExtra("LoadUrl");
            Log.i(TAG, "getAppIntent: LoadUrl:"+LoadUrl);
            web.loadUrl(LoadUrl);
        }
        if(intent.hasExtra("mangoJson")){
            String manggoJson = intent.getStringExtra("mangoJson");
            Log.i(TAG, "getAppIntent: get manggoJson:"+manggoJson);
            try {
                if(!TextUtils.isEmpty(manggoJson)){
                    JSONObject json = new JSONObject(manggoJson);
                    if(json.has("loadURL")){
                        LoadUrl = json.getString("loadURL");
                        Log.i(TAG, "getAppIntent: LoadUrl:"+LoadUrl);
                        web.loadUrl(LoadUrl);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        if(intent.hasExtra("jsonData")){
            String jsonData = intent.getStringExtra("jsonData");
            Log.i(TAG, "getAppIntent: get jsonData:"+jsonData);
            try {
                JSONObject json = new JSONObject(jsonData);
                if(json.has("loadURL")){
                    LoadUrl = json.getString("loadURL");
                    Log.i(TAG, "getAppIntent: LoadUrl:"+LoadUrl);
                    web.loadUrl(LoadUrl);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
//		Bundle bundle = intent.getExtras();
//		if(bundle!=null){
//			Set<String> set = bundle.keySet();
//			for (Iterator iterator = set.iterator(); iterator.hasNext();) {
//				String key = (String) iterator.next();
//				Object value = bundle.get(key);
//				Log.i("getAppIntent", "key:" + key + " value:" + value);
//			}
//		}
    }

    private void initWebView() {
        dialog = ProgressDialog.show(this, null, "页面加载中，请稍后..");
        web = findViewById(R.id.webordey);
        web.getSettings().setJavaScriptEnabled(true);

        web.addJavascriptInterface(new JavaScriptObject(this,web), "AppFunction");
        // 设置android与web的js交互的桥梁
        // 客户端回调 覆盖浏览器
        web.setWebChromeClient(new WebChromeClient());
        WebViewClient client = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.i(TAG, "shouldOverrideUrlLoading: url:"+url);
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dialog.dismiss();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        };
        web.setWebViewClient(client);
        web.getSettings().setNeedInitialFocus(false);
        web.getSettings().setBuiltInZoomControls(true);
        web.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        web.getSettings().setPluginState(WebSettings.PluginState.ON);
//        web.getSettings().setMediaPlaybackRequiresUserGesture(false);
        Log.i(TAG, "initWebView: LoadUrl:"+LoadUrl);
        web.loadUrl(LoadUrl);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode ==KeyEvent.KEYCODE_BACK){
            goBack();
            if (web.getUrl().contains("NoResponse")){
                Log.i(TAG, "onKeyDown:  NoResponse goback");
                return true;
            }
            gotoBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void gotoBack() {

        if ( web.canGoBack()) {
            Log.i(TAG, "onKeyDown: web.getUrl:"+web.getUrl());
            if (web.getUrl().contains(LoadUrl)) {//正式地址
                return ;
            }
            web.goBack();
        }else if (!web.canGoBack()){
            exit();
        }
    }

    public void exit() {
        if (System.currentTimeMillis() - exitTime > 2000L) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
            return;
        }
        finish();
//        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    public void goBack(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            web.evaluateJavascript("javascript:goBack()", new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    //此处为 js 返回的结果
                }
            });
        }else{
            web.loadUrl("javascript:goBack()");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
