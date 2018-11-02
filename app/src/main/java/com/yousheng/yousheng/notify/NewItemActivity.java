package com.yousheng.yousheng.notify;

import android.app.AlertDialog;
import android.app.DatePickerDialog;

import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.allen.library.SuperTextView;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.Constants;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.model.Habit;
import com.yousheng.yousheng.model.NewItem;
import com.yousheng.yousheng.receiver.AlarmHelper;
import com.yousheng.yousheng.uitl.ToastUtil;


import org.litepal.LitePal;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import com.yousheng.yousheng.uitl.time.Api;

import static com.yousheng.yousheng.uitl.TitleBarUtils.changeTitleImageLeftMargin;


public class NewItemActivity extends AppCompatActivity implements View.OnClickListener {

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Calendar calendar = Calendar.getInstance(Locale.CHINA);


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
        AssetManager manager = getAssets();
        try {
            InputStream inputStream = manager.open("TimeExp.m");
            api = new Api(inputStream);
        } catch (Exception e) {
            Log.e("NewItemActivity", "onCreate:  exception ", e);
        }

        CommonTitleBar titleBar = findViewById(R.id.title);
        changeTitleImageLeftMargin(this, titleBar);
        titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                switch (action) {
                    case 1:
                        finish();
                        break;
                    case 3:
                        if (id != -1L)
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
                        else
                            addNewItem();
                        break;
                }
            }
        });

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
                    }
                }
            });


        findViewById(R.id.ok).setOnClickListener(this);
        time.setOnClickListener(this);
        notify.setOnClickListener(this);

        //默认提醒时间是明天早上8点
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);

        //要更新在start activity时传入要更新的id
        Intent intent = getIntent();
        id = intent.getLongExtra("id", -1);
        //初始化数据
        if (id != -1) {
            titleBar.getRightTextView().setText("删除");
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
        } else
            titleBar.getRightTextView().setText("添加");

        time.setLeftString(DateFormat.format("yyyy/MM/dd HH:mm", calendar.getTime()));

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                timePickerDialog.show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                legal = calendar.getTimeInMillis() > System.currentTimeMillis();
                time.setLeftString(DateFormat.format("yyyy/MM/dd HH:mm", calendar.getTime()));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

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

        //用于测试
        HashMap<NewItemHelper.TimeRange, ArrayList<NewItem>> map = NewItemHelper.getAllNewItemInRange();
        for (NewItemHelper.TimeRange range : map.keySet()) {
            Log.d("NewItemActivity", range + "|" + map.get(range));
        }
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
            case R.id.ok:
                addNewItem();
                break;
            case R.id.notify:
                notify.setSwitchIsChecked(!notify.getSwitchIsChecked());
                break;
            case R.id.time:
                datePickerDialog.show();
                break;
        }
    }


}
