package com.yousheng.yousheng;

/***
 * 定义一些常量在这里
 * */
public class Constants {
    /****UMeng**/
    public final static boolean DEBUG = true;
    public final static String UMENG_APP_KEY = "5b97b864f29d985254000027";


    /**** request code**/
    public final static int REQUEST_CODE_MAIN_TO_MENSE = 0;
    public final static int REQUEST_CODE_OVULATION_TO_RECORD = 1;
    public final static int REQUEST_CODE_SETTING_TO_MENSE_MANAGEMENT = 3;
    public final static int REQUEST_CODE_MAIN_TO_NEWITEM = 4;
    public final static int REQUEST_CODE_MAIN_TO_COMMENT = 5;

    public final static int RESULT_CODE_NO_CHANGE = 100 - 1;
    public final static int RESULT_CODE_HAS_CHANGE = 100 - 2;
    public final static int RESULT_CODE_MENSE_START_DAY_CHANGED = 100 - 3;
    public final static int RESULT_CODE_MAIN_TO_NEWITEM = 100 - 4;
    public final static int RESULT_CODE_MAIN_TO_COMMENT = 100 - 5;

    /****经期管理界面常量*/
    public final static String DEFAULT_MENSE_GAP = "28";
    public final static String DEFAULT_MENSE_DURAION = "6";

    /****试纸结果界面*/


    /****时间常量**/
    public final static long ONE_DAY_IN_TS = 24 * 3600 * 1000L;
    public final static String DATE_FORMAT = "yyyy-MM-dd";
    public final static String DATE_FORMAT_MONTH_OF_YEAR = "yyyy-MM";

}
