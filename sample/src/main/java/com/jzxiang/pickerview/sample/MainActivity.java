package com.jzxiang.pickerview.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnDateSetListener {
    TimePickerDialog mDialogAll;
    TimePickerDialog mDialogYearMonth;
    TimePickerDialog mDialogYearMonthDay;
    TimePickerDialog mDialogMonthDayHourMinute;
    TimePickerDialog mDialogHourMinute;
    TimePickerDialog mDialogHourMinute2;
    TextView mTvTime;
    TextView mTvTime2;

    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        long tenYears = 10L * 365 * 1000 * 60 * 60 * 24L;
        mDialogAll = new TimePickerDialog.Builder()
                .setCallBack(this)
                .setCancelStringId("Cancel")
                .setSureStringId("Sure")
                .setTitleStringId("TimePicker")
                .setYearText("年")
                .setMonthText("月")
                .setDayText("日")
                .setHourText("时")
                .setMinuteText("分")
                .setCyclic(false)
                .setMinMillseconds(System.currentTimeMillis())
                .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                .setCurrentMillseconds(System.currentTimeMillis())
                .setCenterLineColor(getResources().getColor(R.color.timepicker_dialog_bg))
                .setType(Type.ALL)
                .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                .setWheelItemTextSize(12)
                .build();

//        mDialogAll = new TimePickerDialog.Builder()
//                .setMinMillseconds(System.currentTimeMillis())
//                .setThemeColor(R.color.colorPrimary)
//                .setWheelItemTextSize(16)
//                .setCallBack(this)
//                .build();
        mDialogYearMonth = new TimePickerDialog.Builder()
                .setType(Type.YEAR_MONTH)
                .setCenterLineColor(getResources().getColor(R.color.colorPrimary))
                .setCallBack(this)
                .build();
        mDialogYearMonthDay = new TimePickerDialog.Builder()
                .setType(Type.YEAR_MONTH_DAY)
                .setCallBack(this)
                .build();
        mDialogMonthDayHourMinute = new TimePickerDialog.Builder()
                .setType(Type.MONTH_DAY_HOUR_MIN)
                .setShowCenterRect(true)//不显示中间透明选择区
                .setCallBack(this)
                .build();
        mDialogHourMinute = new TimePickerDialog.Builder()
                .setType(Type.HOURS_MINS)
                .setShowCancel(false)//不显示取消按钮
                .setShowCenterRect(false)//不显示中间透明选择区
                .setShowItemUnit(false)//不显示Item单位
                .setCyclic(false)//不循环
                .setMinMillseconds(formatTime("05:05"))
                .setMaxMillseconds(formatTime("20:20"))
                .setCurrentMillseconds(formatTime("10:10"))
                .setItemResource(R.layout.cell_time_picker_item)//自定义Item
                .setCallBack(this)
                .build();

        mDialogHourMinute2 = new TimePickerDialog.Builder()
                .setType(Type.HOURS_MINS)
                .setShowCancel(false)//不显示取消按钮
                .setShowCenterRect(false)//不显示中间透明选择区
                .setShowItemUnit(false)//不显示Item单位
                .setCyclic(false)//不循环
                .setMinMillseconds(formatTime("10:10"))
                .setMaxMillseconds(formatTime("22:22"))
                .setCurrentMillseconds(formatTime("20:20"))
                .setItemResource(R.layout.cell_time_picker_item)//自定义Item
                .setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        String text = getDateToString(millseconds);
                        mTvTime2.setText(text);
                    }
                })
                .build();
    }

    void initView() {
        findViewById(R.id.btn_all).setOnClickListener(this);
        findViewById(R.id.btn_year_month_day).setOnClickListener(this);
        findViewById(R.id.btn_year_month).setOnClickListener(this);
        findViewById(R.id.btn_month_day_hour_minute).setOnClickListener(this);
        findViewById(R.id.btn_hour_minute).setOnClickListener(this);
        findViewById(R.id.btn_hour_minute2).setOnClickListener(this);

        mTvTime = (TextView) findViewById(R.id.tv_time);
        mTvTime2 = (TextView) findViewById(R.id.tv_time2);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_all:
                mDialogAll.show(getSupportFragmentManager(), "all");
                break;
            case R.id.btn_year_month:
                mDialogYearMonth.show(getSupportFragmentManager(), "year_month");
                break;
            case R.id.btn_year_month_day:
                mDialogYearMonthDay.show(getSupportFragmentManager(), "year_month_day");
                break;
            case R.id.btn_month_day_hour_minute:
                mDialogMonthDayHourMinute.show(getSupportFragmentManager(), "month_day_hour_minute");
                break;
            case R.id.btn_hour_minute:
                mDialogHourMinute.show(getSupportFragmentManager(), "hour_minute");
                break;
            case R.id.btn_hour_minute2:
                mDialogHourMinute2.show(getSupportFragmentManager(), "hour_minute2");
                break;
        }
    }


    @Override
    public void onDateSet(TimePickerDialog timePickerDialog, long millseconds) {
        String text = getDateToString(millseconds);
        mTvTime.setText(text);
    }

    public String getDateToString(long time) {
        Date d = new Date(time);
        return sf.format(d);
    }

    /**
     * 将HH:mm格式化为HH时 mm分
     *
     * @param time
     * @return
     */
    public static long formatTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            Date date = sdf.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

}
