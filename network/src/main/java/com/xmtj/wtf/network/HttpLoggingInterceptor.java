package com.xmtj.wtf.network;

import android.os.Build;
import android.util.Log;


import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;

/**
 * OkHttp的{@link Interceptor}, 通过设置
 * {@link Configuration#enableLoggingNetworkParams()}打印网络请求参数与响应结果
 * <br/>
 * Created by Ryan on 2015/12/31.
 */
public class HttpLoggingInterceptor implements Interceptor {
    private static final String TAG = "HttpLogging";

    @Override
    public Response intercept(Chain chain) throws IOException {
        //========添加公共参数============
        Request original = chain.request();
        Request request;

        if ("GET".equals(original.method())) {
            //get参数
            HttpUrl modifiedUrl = original.url().newBuilder()
                    .addEncodedQueryParameter("device_udid", Build.SERIAL)
                    .addEncodedQueryParameter("device_name", Build.MODEL)
                    .addEncodedQueryParameter("device_version", Build.VERSION.SDK_INT + "")
                    .build();
            request = original.newBuilder().url(modifiedUrl).build();
        } else {
            //post参数
            Request.Builder requestBuilder = original.newBuilder();
//                        .header("APIKEY", Constant.API_KEY);
            //请求体定制：统一添加token参数
            if (original.body() instanceof FormBody) {


                FormBody.Builder newFormBody = new FormBody.Builder();
                FormBody oidFormBody = (FormBody) original.body();
                for (int i = 0; i < oidFormBody.size(); i++) {
                    newFormBody.addEncoded(oidFormBody.encodedName(i), oidFormBody.encodedValue(i));
                }
                newFormBody.add("device_udid", Build.SERIAL);
                newFormBody.add("device_name", Build.DEVICE);
                newFormBody.add("device_version", Build.VERSION.SDK_INT + "");
                requestBuilder.method(original.method(), newFormBody.build());
            }

            request = requestBuilder.build();
        }


        //===========log===========
        long t1 = System.nanoTime();

        Buffer buffer = new Buffer();
        if (request.body() != null) {
            request.body().writeTo(buffer);
            Log.e(TAG, String.format("Sending request %s on %s%n%sRequest Params: %s",
                    request.url(), chain.connection(), request.headers(), buffer.clone().readUtf8()));
            buffer.close();
        }

        Response response = chain.proceed(request);
        long t2 = System.nanoTime();

        BufferedSource source = response.body().source();
        source.request(Long.MAX_VALUE);
        buffer = source.buffer().clone();


//        Log.e(TAG, "==============================");
//        while (buffer.size() > 0) {
//            Log.e(TAG, "" + buffer.readUtf8Line());
//        }
//        Log.e(TAG, "==============================");

        Log.e(TAG, String.format("Received response for %s in %.1fms%n%sResponse Json: %s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers(),
                buffer.readUtf8()));


        return response;
    }
}
