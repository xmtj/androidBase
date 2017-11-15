package com.xmtj.wtf.gather.mvp.presenter;


import android.util.Log;

import com.xmtj.wtf.gather.base.BaseRxPresenter;
import com.xmtj.wtf.gather.base.ResponseSubscriber;
import com.xmtj.wtf.gather.mvp.model.BaseData;
import com.xmtj.wtf.gather.mvp.model.Data;
import com.xmtj.wtf.gather.mvp.model.User;
import com.xmtj.wtf.gather.mvp.view.MainView;
import com.xmtj.wtf.network.ManagerFactory;
import com.xmtj.wtf.network.ResponseTransformer;


/**
 * 登录逻辑
 */
public class MainPresenter extends BaseRxPresenter<MainView> {

    private MainManager manager;

    @Override
    protected void onViewAttached() {
        manager = ManagerFactory.getFactory().getManager(MainManager.class);
    }

    /**
     * 获取城市列表
     */
    public void login(String username, String password) {
        manager.login(username, password).compose(new ResponseTransformer(this.<BaseData<Data>>bindLifeCycle())).subscribe(new ResponseSubscriber<BaseData<Data>>(getView()) {
            @Override
            public void success(BaseData<Data> user) {
                Log.e("wanglei", "user: " + user);
            }

            @Override
            public void operationError(BaseData<Data> baseData, int status, String message) {
                if (getView() != null)
                    getView().showToastMessage(message);
            }
        });

    }
}
