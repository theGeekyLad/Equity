package io.mapwize.mapwize;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

class MWZRetrofitBuilder {
    static final String BASE_URL = "https://www.mapwize.io/api/v1/";

    static MWZApiInterface getComplexClient(Context ctx) {
        OkHttpClient client = getOkHttpClient(ctx);

        Retrofit raCustom = new Retrofit.Builder()
                .client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        return raCustom.create(MWZApiInterface.class);
    }

    @NonNull
    private static OkHttpClient getOkHttpClient(Context ctx) {
        File myCacheDir=new File(ctx.getCacheDir(),"MWZOkHttpCache");
        int cacheSize=1024*1024;
        Cache cacheDir=new Cache(myCacheDir,cacheSize);
        return new OkHttpClient.Builder()
                .cookieJar(new MWZWebViewCookieHandler())
                .cache(cacheDir)
                .build();
    }

}