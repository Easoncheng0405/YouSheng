package com.yousheng.yousheng.manager;

public abstract class AbstractGDTManager {
    /***广告ID编号**/
    public final static long SPLASH_AD_ID1 = 5030643482489904L;
    public final static long SPLASH_AD_ID2 = 2020744452088995L;
    public final static long SPLASH_AD_ID1_HUAWEI = 2060640402079030L;
    public final static long SPLASH_AD_ID2_HUAWEI = 2060640402079030L;
    public final static long NATIVE_AD_ID1 = 7080046422395031L;
    public final static long NATIVE_AD_ID2 = 2080040492294092L;
    public final static long NATIVE_AD_ID1_HUAWEI = 5040347422381977L;
    public final static long NATIVE_AD_ID2_HUAWEI = 1070947452295030L;


    protected long mPosId1;
    protected long mPosId2;
    protected long mAppId;


    /****拉取广告的超时时间 3000~5000*/
    protected int mFetchADTimeOut;
}
