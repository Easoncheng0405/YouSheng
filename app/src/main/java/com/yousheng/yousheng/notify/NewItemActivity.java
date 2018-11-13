package com.yousheng.yousheng.notify;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;

import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;

import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.yousheng.yousheng.Constants;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.model.NewItem;
import com.yousheng.yousheng.receiver.AlarmHelper;
import com.yousheng.yousheng.timepickerlib.CustomDatePicker;
import com.yousheng.yousheng.uitl.CalendarUtils;
import com.yousheng.yousheng.uitl.ToastUtil;


import org.litepal.LitePal;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.yousheng.yousheng.uitl.time.Api;

import static com.yousheng.yousheng.uitl.TitleBarUtils.dip2px;


public class NewItemActivity extends AppCompatActivity implements View.OnClickListener {

    private Calendar calendar = Calendar.getInstance(Locale.CHINA);
    private CustomDatePicker mDatePicker;


    private SuperTextView notify;
    private SuperTextView time;
    private EditText content;

    private Context context;
    private boolean legal = true;
    private long id = -1;

    private boolean isNotify = false;
    private Api api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_itme);
        context = this;

        ((TextView) findViewById(R.id.title)).setCompoundDrawablePadding(dip2px(context, 25));
        try {
            AssetManager manager = getAssets();
            InputStream inputStream = manager.open("TimeExp.m");
            api = new Api(inputStream);
        } catch (Exception e) {
            Log.e("NewItemActivity", "onCreate:  exception ", e);
        }


        time = findViewById(R.id.time);
        notify = findViewById(R.id.notify);
        content = findViewById(R.id.content);
        if (api != null)
            content.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    Log.d("NewItemActivity", "text change");
                    Date[] dates = api.getDate(s.toString());

                    if (dates.length > 0) {
                        isNotify = true;
                        calendar.setTime(dates[0]);
                        notify.setSwitchIsChecked(true);
                        time.setLeftString(DateFormat.format("yyyy/MM/dd HH:mm", calendar.getTime()));
                        legal = calendar.getTimeInMillis() > System.currentTimeMillis();

                    }
                }
            });


        findViewById(R.id.ok).setOnClickListener(this);
        TextView tv = findViewById(R.id.t2);
        time.setOnClickListener(this);
        notify.setOnClickListener(this);
        int px = dip2px(this, 10);
        ((SuperTextView) findViewById(R.id.notify)).getLeftTextView().setPadding(px, 0, 0, 0);
        ((SuperTextView) findViewById(R.id.time)).getLeftTextView().setPadding(px, 0, 0, 0);
        //默认提醒时间是明天早上8点
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);

        //要更新在start activity时传入要更新的id
        Intent intent = getIntent();
        id = intent.getLongExtra("id", -1);
        //初始化数据
        if (id != -1) {
            findViewById(R.id.t1).setVisibility(View.VISIBLE);
            NewItem item = LitePal.find(NewItem.class, id);
            if (item == null) {
                ToastUtil.showMsg(context, "发生了一些错误，请联系客服！");
                finish();
                return;
            }
            isNotify = item.isNotify();
            content.setText(item.getContent());
            if (item.isNotify()) {
                time.setVisibility(View.VISIBLE);
                notify.setSwitchIsChecked(true);
            }
            long l = item.getTime();
            if (l > 0) {
                calendar.setTimeInMillis(l);
            }
            tv.setText("保存");
            ((TextView) findViewById(R.id.title)).setText("待办事项修改");
        } else {
            findViewById(R.id.t1).setVisibility(View.GONE);
            tv.setText("添加");
        }
        time.setLeftString(DateFormat.format("yyyy/MM/dd HH:mm", calendar.getTime()));


        notify.setSwitchCheckedChangeListener(new SuperTextView.OnSwitchCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                isNotify = b;
                if (b) {
                    time.setVisibility(View.VISIBLE);
                } else {
                    time.setVisibility(View.GONE);
                }
            }
        });

        mDatePicker =
                new CustomDatePicker
                        .Builder()
                        .setContext(this)
                        .setStartDate(CalendarUtils.formatDateString(System.currentTimeMillis(),
                                "yyyy-MM-dd HH:mm"))
                        .setEndDate("2100-01-01 23:59")
                        .setTitle("选择一个时间")
                        .setResultHandler(new CustomDatePicker.ResultHandler() {
                            @Override
                            public void handle(String t, long l) {
                                calendar.setTimeInMillis(l);
                                legal = calendar.getTimeInMillis() > System.currentTimeMillis();
                                time.setLeftString(DateFormat.format("yyyy/MM/dd HH:mm", calendar.getTime()));
                            }
                        })
                        .create();
        mDatePicker.showSpecificTime(true);
    }

    private void addNewItem() {
        String str = content.getText().toString();
        if (TextUtils.isEmpty(str) || str.trim().length() == 0) {
            ToastUtil.showMsg(context, "请填写提醒内容");
            return;
        }

        if (str.length() > 150) {
            ToastUtil.showMsg(context, "内容不能超过150字符哦");
            return;
        }

        if (!legal) {
            ToastUtil.showMsg(context, "请选择一个将来的时间");
            return;
        }


        long time = isNotify ? calendar.getTimeInMillis() : 0;
        NewItem item;
        if (id == -1) {
            item = new NewItem(str, time);
            item.setNotify(isNotify);
            item.save();
        } else {
            item = LitePal.find(NewItem.class, id);
            item.setContent(str);
            item.setTime(time);
            item.setNotify(isNotify);
            item.save();
        }

        AlarmHelper.notifyNewItem(context, item);
        ToastUtil.showMsg(context, "成功添加提醒事项！");
        Intent intent = new Intent();
        intent.putExtra("timeRange", NewItemHelper.getRange(System.currentTimeMillis(), item).value);
        setResult(Constants.RESULT_CODE_MAIN_TO_NEWITEM, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title:
                finish();
                break;
            case R.id.t1:
                new AlertDialog.Builder(context).setTitle("注意")
                        .setMessage("确定要删除此提醒事项吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NewItem item = LitePal.find(NewItem.class, id);
                                if (item != null)
                                    item.delete();
                                ToastUtil.showMsg(context, "成功删除提醒事项");
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
                break;
            case R.id.t2:
            case R.id.ok:
                addNewItem();
                break;
            case R.id.notify:
                notify.setSwitchIsChecked(!notify.getSwitchIsChecked());
                break;
            case R.id.time:
                mDatePicker.show(CalendarUtils.formatDateString(System.currentTimeMillis(),
                        "yyyy-MM-dd HH:mm"));
                break;
        }
    }
}
