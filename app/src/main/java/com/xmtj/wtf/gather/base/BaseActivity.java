package com.xmtj.wtf.gather.base;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.ActivityCompat;
import android.widget.EditText;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.xmtj.wtf.gather.util.ToastManager;

import java.util.List;


/**
 * Activity基类, 继承自此类的Activity需要实现{@link #getLayoutId},{@link #init}
 * 以及{@link #createPresenter()}, 不需要覆写onCreate方法.
 * <br/>
 * 实现此类需遵循MVP设计, 第一个泛型V需传入一个继承自{@link BaseView}的MVPView,
 * 第二个泛型需传入继承自{@link BaseRxPresenter}的MVPPresenter.
 * <br/>
 * Presenter的生命周期已交由此类管理, 子类无需管理. 如果子类要使用多个Presenter, 则需要自行管理生命周期.
 * 此类已经实现了BaseView中的抽象方法, 子类无需再实现, 如需自定可覆写对应的方法.
 * <br/>
 * Created by Ryan on 2015/12/28.
 */
public abstract class BaseActivity<V extends BaseView, T extends BaseRxPresenter<V>>
        extends RxAppCompatActivity implements BaseView {

    private static final int PERMISSON_REQUESTCODE = 0;


    protected T presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        AppManager.getAppManager().addActivity(this);
        presenter = createPresenter();
        if (presenter != null) presenter.attachView((V) this);
        init(savedInstanceState);
    }

    /**
     * 指定Activity需加载的布局ID, {@link BaseActivity}BaseActivity
     * 会通过{@link #setContentView}方法来加载布局
     *
     * @return 需加载的布局ID
     */
    protected abstract int getLayoutId();

    /**
     * 初始化方法, 类似OnCreate, 仅在此方法中做初始化操作, findView与事件绑定请使用ButterKnife
     */
    protected abstract void init(Bundle savedInstanceState);

    /**
     * 创建Presenter, 然后通过调用{@link #getPresenter()}来使用生成的Presenter
     *
     * @return Presenter
     */
    protected abstract T createPresenter();

    /**
     * 获取通过{@link #createPresenter()}生成的presenter对象
     *
     * @return Presenter
     */
    public T getPresenter() {
        return presenter;
    }

    /**
     * 获取LoadingDialog, 用来显示加载中
     *
     * @return LoadingDialog
     */
//    public GifLoadingDialog getLoadingDialog() {
//        return gifLoadingDialog;
//    }
    @Override
    protected void onDestroy() {
        if (presenter != null) presenter.detachView();
        presenter = null;
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }

    public void showToast(String s) {
        ToastManager.show(s);
    }

    public void showToast(int r) {
        ToastManager.show(r);
    }

    public String getText(EditText editText) {
        return editText.getText().toString().trim();
    }

    @Override
    public RxAppCompatActivity getRxContext() {
        return this;
    }

    @Override
    public Context getViewContext() {
        return this;
    }

    @Override
    public void showLoadingDialog() {
//        gifLoadingDialog.show();
    }

    @Override
    public void hideLoadingDialog() {
//        gifLoadingDialog.dismiss();
    }

    @Override
    public void showToastMessage(String message) {
        showToast(message);
    }

    public void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);//友盟
    }

    public void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }

}
