package com.yousheng.yousheng.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.widget.ViewStubCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;
import com.yousheng.yousheng.Constants;
import com.yousheng.yousheng.uitl.UMengUtils;

import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

public class GDTSplashManager {
    private final static String TAG = "GDTSplashManager";


    /***广告ID编号**/
    public final static long SPLASH_AD_ID1 = 5030643482489904L;
    public final static long SPLASH_AD_ID2 = 2020744452088995L;
    public final static long SPLASH_AD_ID1_HUAWEI = 2060640402079030L;
    public final static long SPLASH_AD_ID2_HUAWEI = 2060640402079030L;


    private SplashAD mSplashAD1;
    private SplashAD mSplashAD2;

    private Activity mActivity;
    private ViewGroup mAdContainer;
    private View mSkipView;

    private long mPosId1;
    private long mPosId2;
    private long mAppId;

    /****开始拉取广告的时间点*/
    private long mFetchADStartTime;
    /****拉取广告的超时时间 3000~5000*/
    private int mFetchADTimeOut;

    public void setmAdStateListener(SplashADStateListener mAdStateListener) {
        this.mAdStateListener = mAdStateListener;
    }

    private SplashADStateListener mAdStateListener;

    /***
     * @param adContainer 广告位的容器
     * */
    @SuppressLint("CheckResult")
    public void init(final Activity activity,
                     final ViewGroup adContainer,
                     final View skipView,
                     final int fetchTimeOut) {
        String channel = UMengUtils.getAppMetaData(activity, "UMENG_CHANNEL_VALUE");
        channel = channel == null ? "default" : channel;
        Flowable
                .just(channel)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String channel) throws Exception {
                        mActivity = activity;
                        mSkipView = skipView;
                        mAdContainer = adContainer;
                        mFetchADTimeOut = fetchTimeOut;
                        mAppId = Constants.GDT_APP_ID;

                        if (channel.equals("huawei")) {
                            mPosId1 = SPLASH_AD_ID1;
                            mPosId2 = SPLASH_AD_ID2;
                        } else {
                            mPosId1 = SPLASH_AD_ID1_HUAWEI;
                            mPosId2 = SPLASH_AD_ID2_HUAWEI;
                        }
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void fetch() {
        fetchSplashAD(mActivity, mAdContainer, mSkipView, String.valueOf(mAppId), String.valueOf(mPosId1),
                mSplashListener1, mFetchADTimeOut, 1);
    }


    /**
     * 拉取开屏广告，开屏广告的构造方法有3种，详细说明请参考开发者文档。
     *
     * @param activity      展示广告的activity
     * @param adContainer   展示广告的大容器
     * @param skipContainer 自定义的跳过按钮：传入该view给SDK后，SDK会自动给它绑定点击跳过事件。SkipView的样式可以由开发者自由定制，其尺寸限制请参考activity_splash.xml或者接入文档中的说明。
     * @param appId         应用ID
     * @param posId         广告位ID
     * @param adListener    广告状态监听器
     * @param fetchDelay    拉取广告的超时时长：取值范围[3000, 5000]，设为0表示使用广点通SDK默认的超时时长。
     * @param index         拉取第几个广告
     */
    private void fetchSplashAD(Activity activity, ViewGroup adContainer, View skipContainer,
                               String appId, String posId, SplashADListener adListener, int fetchDelay, int index) {
        mFetchADStartTime = System.currentTimeMillis();
        switch (index) {
            case 1:
                mSplashAD1 = new SplashAD(activity, adContainer, skipContainer, appId, posId, adListener, fetchDelay);
                break;
            case 2:
                mSplashAD2 = new SplashAD(activity, adContainer, skipContainer, appId, posId, adListener, fetchDelay);
                break;
        }
    }

    /***
     * 两个广告位有一个展示成功
     * */
    private void onAdShowSuccess() {
        if (mAdStateListener != null) {
            mAdStateListener.onADShow();
        }
    }

    /***
     * 所有的广告位都拉取失败，展示默认广告
     * */
    private void onAllAdShowFailed() {
        if (mAdStateListener != null) {
            mAdStateListener.onADFetchFailed();
        }
    }

    private void onAdShowFinished() {
        if (mAdStateListener != null) {
            mAdStateListener.onADExposure();
        }
    }

    private SplashADListener mSplashListener1 = new SplashADListener() {
        @Override
        public void onADDismissed() {
            Log.d(TAG, "onAD1Dimissed");
            onAdShowFinished();
        }

        @Override
        public void onNoAD(AdError adError) {
            Log.d(TAG, "onNoAD1");

            //如果拉取第一个广告失败，则拉取第二个
            fetchSplashAD(mActivity, mAdContainer, mSkipView, String.valueOf(mAppId), String.valueOf(mPosId2),
                    mSplashListener2, mFetchADTimeOut, 2);
        }

        @Override
        public void onADPresent() {
            Log.d(TAG, "onAD1Present");
            onAdShowSuccess();
        }

        @Override
        public void onADClicked() {
            Log.d(TAG, "onAD1Clicked");

        }

        @Override
        public void onADTick(long l) {
            Log.d(TAG, "onAD1Tick");
            mAdStateListener.onADTick(l);

        }

        @Override
        public void onADExposure() {
            Log.d(TAG, "onAD1Exposure");
        }
    };

    private SplashADListener mSplashListener2 = new SplashADListener() {
        @Override
        public void onADDismissed() {
            Log.d(TAG, "onAD2Dimissed");
            onAdShowFinished();
        }

        @Override
        public void onNoAD(AdError adError) {
            Log.d(TAG, "onNoAD2");
            onAllAdShowFailed();
        }

        @Override
        public void onADPresent() {
            Log.d(TAG, "onAD2Present");
            onAdShowSuccess();
        }

        @Override
        public void onADClicked() {
            Log.d(TAG, "onAD2Clicked");

        }

        @Override
        public void onADTick(long l) {
            Log.d(TAG, "onAD2Tick");
            if (mAdStateListener != null) {
                mAdStateListener.onADTick(l);
            }
        }

        @Override
        public void onADExposure() {
            Log.d(TAG, "onAD2Exposure");
        }
    };

    public interface SplashADStateListener {
        public void onADTick(long timeMillis);

        public void onADShow();

        public void onADFetchFailed();

        public void onADExposure();
    }
}
