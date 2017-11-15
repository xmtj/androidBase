package com.xmtj.wtf.gather;

import android.Manifest;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jaeger.library.StatusBarUtil;
import com.meituan.android.walle.WalleChannelReader;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.tencent.sonic.sdk.SonicConfig;
import com.tencent.sonic.sdk.SonicEngine;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.xmtj.wtf.gather.base.BaseActivity;
import com.xmtj.wtf.gather.hybird.BrowserActivity;
import com.xmtj.wtf.gather.hybird.SonicRuntimeImpl;
import com.xmtj.wtf.gather.mvp.presenter.MainPresenter;
import com.xmtj.wtf.gather.mvp.view.MainView;

import rx.functions.Action1;

public class MainActivity extends BaseActivity<MainView, MainPresenter> implements MainView {
    private static final String DEFAULT_URL = "http://mc.vip.qq.com/demo/indexv3";
    Button btn;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.colorPrimary));
        btn = (Button) findViewById(R.id.btn_load_hybird);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrowserActivity.startBrowserActivity(MainActivity.this, BrowserActivity.MODE_SONIC, DEFAULT_URL);
            }
        });
        new RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        Log.e("wanglei", granted + "");
                        if (granted) {
                            initSonic();
                        } else {
                            Log.e("wanglei", "权限不可用");
                        }
                    }
                });
        String channel = WalleChannelReader.getChannel(this.getApplicationContext());
        Log.e("wanglei", channel);
        TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.zip");
        getPresenter().login("15971470520", "dc483e80a7a0bd9ef71d8cf973673924");
    }

    @Override
    protected MainPresenter createPresenter() {
        return new MainPresenter();
    }

    private void initSonic() {
        // init sonic engine
        if (!SonicEngine.isGetInstanceAllowed()) {
            SonicEngine.createInstance(new SonicRuntimeImpl(getApplication()), new SonicConfig.Builder().build());
        }
    }

    @Override
    public void onCityLoaded() {
        Log.e("wanglei", "onCityLoaded: ");

    }
}
