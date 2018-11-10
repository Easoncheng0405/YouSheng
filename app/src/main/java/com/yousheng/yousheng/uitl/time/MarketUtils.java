package com.yousheng.yousheng.uitl.time;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.yousheng.yousheng.model.Market;

import org.litepal.LitePal;

import java.util.Collections;
import java.util.List;

public class MarketUtils {

    /***
     *
     * @return 是否能找到应用商店
     * */
    public static boolean navigationToAppStore(Activity activity) {
        List<Market> markets = LitePal.findAll(Market.class);
        Collections.sort(markets);

        boolean findMarket = false;
        for (Market market : markets) {
            if (toMarket(activity, market.getPackageName())) {
                findMarket = true;
                break;
            }
        }
        return findMarket;
    }

    /**
     * 跳转应用商店.
     *
     * @param marketPkg 应用商店包名
     * @return {@code true} 跳转成功 <br> {@code false} 跳转失败
     */
    public static boolean toMarket(Activity activity, String marketPkg) {
        Uri uri = Uri.parse("market://details?id=com.yousheng.yousheng");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (marketPkg != null) {
            intent.setPackage(marketPkg);
        }
        try {
            activity.startActivity(intent);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
