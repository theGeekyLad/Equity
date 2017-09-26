package io.mapwize.mapwize;


import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Account manager will store account informations
 */
public class MWZAccountManager {

    private String mApiKey;
    private Context mContext;

    private static MWZAccountManager sInstance = null;

    public static MWZAccountManager start(@NonNull Context ctx, @NonNull String apiKey) {
        if (sInstance == null) {
            sInstance = new MWZAccountManager(ctx, apiKey);
        }
        return sInstance;
    }

    private MWZAccountManager(Context ctx, String apiKey) {
        this.mContext = ctx;
        this.mApiKey = apiKey;
    }

    static MWZAccountManager getInstance() {
        return sInstance;
    }

    String getApiKey() {
        return mApiKey;
    }

    Context getContext() {
        return mContext;
    }

}
