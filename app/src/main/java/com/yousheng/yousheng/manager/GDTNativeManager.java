package com.yousheng.yousheng.manager;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.comm.util.AdError;
import com.yousheng.yousheng.Constants;
import com.yousheng.yousheng.uitl.UMengUtils;

import java.util.List;

public class GDTNativeManager extends AbstractGDTManager {
    private final static String TAG = "GDTNativeManager";

    private NativeExpressAD mNativeExpress1;
    private NativeExpressAD mNativeExpress2;
    private NativeExpressADView nativeExpressADView;

    private ViewGroup mContainer;
    private Activity mActivity;

    public void init(Activity activity, ViewGroup container) {
        mAppId = Constants.GDT_APP_ID;
        mContainer = container;
        String channel = UMengUtils.getAppMetaData(activity, "UMENG_CHANNEL_VALUE");
        channel = (channel == null ? "default" : channel);

        if (channel.equals("huawei")) {
            mPosId1 = SPLASH_AD_ID1;
            mPosId2 = SPLASH_AD_ID2;
        } else {
            mPosId1 = SPLASH_AD_ID1_HUAWEI;
            mPosId2 = SPLASH_AD_ID2_HUAWEI;
        }


        this.mContainer = container;
        this.mActivity = activity;
        mNativeExpress1 =
                new NativeExpressAD(activity,
                        getMyADSize(),
                        String.valueOf(mAppId),
                        String.valueOf(mPosId1),
                        mListener1); // 这里的Context必须为Activity

    }

    public void show() {
        mNativeExpress1.loadAD(1);
    }

    private ADSize getMyADSize() {
        int w = ADSize.FULL_WIDTH;
        int h = ADSize.AUTO_HEIGHT;
        return new ADSize(w, h);
    }


    /***
     * Activity销毁时调用
     * */
    public void onActivityDestroy() {
        // 使用完了每一个NativeExpressADView之后都要释放掉资源
        if (nativeExpressADView != null) {
            nativeExpressADView.destroy();
        }
    }

    private NativeExpressAD.NativeExpressADListener mListener1 = new NativeExpressAD.NativeExpressADListener() {
        @Override
        public void onADLoaded(List<NativeExpressADView> list) {

            if (list == null || list.size() <= 0) {
                Log.e(TAG, "has no ad");
                return;
            }

            Log.i(TAG, "onADLoaded: " + list.size());

            // 释放前一个展示的NativeExpressADView的资源
            if (nativeExpressADView != null) {
                nativeExpressADView.destroy();
            }

            if (mContainer.getVisibility() != View.VISIBLE) {
                mContainer.setVisibility(View.VISIBLE);
            }

            if (mContainer.getChildCount() > 0) {
                mContainer.removeAllViews();
            }


            nativeExpressADView = list.get(0);

            // 广告可见才会产生曝光，否则将无法产生收益。
            mContainer.addView(nativeExpressADView);
            nativeExpressADView.render();
        }

        @Override
        public void onRenderFail(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onRenderSuccess(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onADExposure(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onADClicked(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onADClosed(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onADClosed");
            // 当广告模板中的关闭按钮被点击时，广告将不再展示。NativeExpressADView也会被Destroy，释放资源，不可以再用来展示。
            if (mContainer != null && mContainer.getChildCount() > 0) {
                mContainer.removeAllViews();
                mContainer.setVisibility(View.GONE);
            }
        }

        @Override
        public void onADLeftApplication(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onNoAD(AdError adError) {
            Log.i(TAG, "onNoAD1");
            mNativeExpress2 =
                    new NativeExpressAD(mActivity,
                            getMyADSize(),
                            String.valueOf(mAppId),
                            String.valueOf(mPosId2),
                            mListener2); // 这里的Context必须为Activity

            mNativeExpress2.loadAD(1);
        }
    };


    //listener2
    private NativeExpressAD.NativeExpressADListener mListener2 = new NativeExpressAD.NativeExpressADListener() {
        @Override
        public void onADLoaded(List<NativeExpressADView> list) {

            if (list == null || list.size() <= 0) {
                Log.e(TAG, "has no ad");
                return;
            }
            Log.i(TAG, "onADLoaded: " + list.size());

            // 释放前一个展示的NativeExpressADView的资源
            if (nativeExpressADView != null) {
                nativeExpressADView.destroy();
            }

            if (mContainer.getVisibility() != View.VISIBLE) {
                mContainer.setVisibility(View.VISIBLE);
            }

            if (mContainer.getChildCount() > 0) {
                mContainer.removeAllViews();
            }


            nativeExpressADView = list.get(0);

            // 广告可见才会产生曝光，否则将无法产生收益。
            mContainer.addView(nativeExpressADView);
            nativeExpressADView.render();
        }

        @Override
        public void onRenderFail(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onRenderSuccess(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onADExposure(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onADClicked(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onADClosed(NativeExpressADView nativeExpressADView) {
            Log.i(TAG, "onADClosed");
            // 当广告模板中的关闭按钮被点击时，广告将不再展示。NativeExpressADView也会被Destroy，释放资源，不可以再用来展示。
            if (mContainer != null && mContainer.getChildCount() > 0) {
                mContainer.removeAllViews();
                mContainer.setVisibility(View.GONE);
            }
        }

        @Override
        public void onADLeftApplication(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onADOpenOverlay(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onADCloseOverlay(NativeExpressADView nativeExpressADView) {

        }

        @Override
        public void onNoAD(AdError adError) {
            Log.i(TAG, "onNoAD2");
            mContainer.setVisibility(View.GONE);
        }
    };


}
