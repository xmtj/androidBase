package com.xmtj.wtf.gather.mvp.presenter;


import com.xmtj.wtf.gather.contants.Urls;
import com.xmtj.wtf.gather.mvp.model.BaseData;
import com.xmtj.wtf.gather.mvp.model.Data;
import com.xmtj.wtf.gather.mvp.model.User;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by Administrator on 2016/2/29.
 */
public interface MainManager {


    /**
     * 登陆
     *
     * @return
     */
//    @GET(Urls.LOGIN)
//    Observable<BaseData<User>> login(@QueryMap Map<String, String> paramsMap);
    @FormUrlEncoded
    @POST(Urls.LOGIN)
    Observable<BaseData<Data>> login(@Field("username") String username, @Field("password") String password);
}
