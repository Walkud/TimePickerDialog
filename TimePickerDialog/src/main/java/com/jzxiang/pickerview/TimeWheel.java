package com.jzxiang.pickerview;

import android.content.Context;
import android.view.View;

import com.jzxiang.pickerview.adapters.NumericWheelAdapter;
import com.jzxiang.pickerview.config.PickerConfig;
import com.jzxiang.pickerview.data.source.TimeRepository;
import com.jzxiang.pickerview.utils.Utils;
import com.jzxiang.pickerview.wheel.OnWheelChangedListener;
import com.jzxiang.pickerview.wheel.WheelView;

import java.util.Calendar;

/**
 * Created by jzxiang on 16/4/20.
 */
public class TimeWheel {
    Context mContext;

    WheelView year, month, day, hour, minute;
    NumericWheelAdapter mYearAdapter, mMonthAdapter, mDayAdapter, mHourAdapter, mMinuteAdapter;

    PickerConfig mPickerConfig;
    TimeRepository mRepository;
    OnWheelChangedListener yearListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            updateMonths();
        }
    };
    OnWheelChangedListener monthListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            updateDays();
        }
    };
    OnWheelChangedListener dayListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            updateHours();
        }
    };
    OnWheelChangedListener minuteListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            updateMinutes();
        }
    };

    public TimeWheel(View view, PickerConfig pickerConfig) {
        mPickerConfig = pickerConfig;

        mRepository = new TimeRepository(pickerConfig);
        mContext = view.getContext();
        initialize(view);
    }

    public void initialize(View view) {
        initView(view);
        initYear();
        initMonth();
        initDay();
        initHour();
        initMinute();
    }


    void initView(View view) {
        year = (WheelView) view.findViewById(R.id.year);
        month = (WheelView) view.findViewById(R.id.month);
        day = (WheelView) view.findViewById(R.id.day);
        hour = (WheelView) view.findViewById(R.id.hour);
        minute = (WheelView) view.findViewById(R.id.minute);

        View startSplitView = view.findViewById(R.id.start_split_view);
        View yearMonthSplitView = view.findViewById(R.id.year_month_split_view);
        View monthDaySplitView = view.findViewById(R.id.month_day_split_view);
        View dayHourSplitView = view.findViewById(R.id.day_hour_split_view);
        View hourMinuteSplitView = view.findViewById(R.id.hour_minute_split_view);
        View endSplitView = view.findViewById(R.id.end_split_view);

        switch (mPickerConfig.mType) {
            case ALL:
                break;
            case YEAR_MONTH_DAY:
                Utils.hideViews(hour, minute);
                Utils.showViews(startSplitView, yearMonthSplitView, monthDaySplitView, endSplitView);
                break;
            case YEAR_MONTH:
                Utils.hideViews(day, hour, minute);
                Utils.showViews(startSplitView, yearMonthSplitView, endSplitView);
                break;
            case MONTH_DAY_HOUR_MIN:
                Utils.hideViews(year);
                Utils.showViews(startSplitView, monthDaySplitView, dayHourSplitView, hourMinuteSplitView, endSplitView);
                break;
            case HOURS_MINS:
                Utils.hideViews(year, month, day);
                Utils.showViews(startSplitView, hourMinuteSplitView, endSplitView);
                break;
            case YEAR:
                Utils.hideViews(month, day, hour, minute);
                Utils.showViews(startSplitView, endSplitView);
                break;
        }

        year.addChangingListener(yearListener);
        year.addChangingListener(monthListener);
        year.addChangingListener(dayListener);
        year.addChangingListener(minuteListener);
        month.addChangingListener(monthListener);
        month.addChangingListener(dayListener);
        month.addChangingListener(minuteListener);
        day.addChangingListener(dayListener);
        day.addChangingListener(minuteListener);
        hour.addChangingListener(minuteListener);
    }

    void initYear() {
        int minYear = mRepository.getMinYear();
        int maxYear = mRepository.getMaxYear();

        mYearAdapter = getNumericWheelAdapter(minYear, maxYear, mPickerConfig.mYear);
        mYearAdapter.setConfig(mPickerConfig);
        mYearAdapter.setCustomLastStr(mPickerConfig.mCustomLastYearStr);
        year.setViewAdapter(mYearAdapter);
        year.setCurrentItem(mRepository.getDefaultCalendar().year - minYear);
        year.setUnit(mPickerConfig.mYear);
    }

    void initMonth() {
        updateMonths();
        int curYear = getCurrentYear();
        int minMonth = mRepository.getMinMonth(curYear);
        month.setCurrentItem(mRepository.getDefaultCalendar().month - minMonth);
        month.setCyclic(mPickerConfig.cyclic);
        month.setUnit(mPickerConfig.mMonth);
    }

    void initDay() {
        updateDays();
        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();

        int minDay = mRepository.getMinDay(curYear, curMonth);
        day.setCurrentItem(mRepository.getDefaultCalendar().day - minDay);
        day.setCyclic(mPickerConfig.cyclic);
        day.setUnit(mPickerConfig.mDay);
    }

    void initHour() {
        updateHours();
        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();
        int curDay = getCurrentDay();

        int minHour = mRepository.getMinHour(curYear, curMonth, curDay);
        hour.setCurrentItem(mRepository.getDefaultCalendar().hour - minHour);
        hour.setCyclic(mPickerConfig.cyclic);
        hour.setUnit(mPickerConfig.mHour);
    }

    void initMinute() {
        updateMinutes();
        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();
        int curDay = getCurrentDay();
        int curHour = getCurrentHour();
        int minMinute = mRepository.getMinMinute(curYear, curMonth, curDay, curHour);

        minute.setCurrentItem(mRepository.getDefaultCalendar().minute - minMinute);
        minute.setCyclic(mPickerConfig.cyclic);
        minute.setUnit(mPickerConfig.mMinute);
    }

    void updateMonths() {
        if (month.getVisibility() == View.GONE)
            return;

        int curYear = getCurrentYear();
        int minMonth = mRepository.getMinMonth(curYear);
        int maxMonth = mRepository.getMaxMonth(curYear);
        mMonthAdapter = getNumericWheelAdapter(minMonth, maxMonth, mPickerConfig.mMonth);
        mMonthAdapter.setConfig(mPickerConfig);
        month.setViewAdapter(mMonthAdapter);

        if (mRepository.isMinYear(curYear)) {
            month.setCurrentItem(0, false);
        }
    }

    void updateDays() {
        if (day.getVisibility() == View.GONE)
            return;

        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
        calendar.set(Calendar.MONTH, curMonth);

        int maxDay = mRepository.getMaxDay(curYear, curMonth);
        int minDay = mRepository.getMinDay(curYear, curMonth);
        mDayAdapter = getNumericWheelAdapter(minDay, maxDay, mPickerConfig.mDay);
        mDayAdapter.setConfig(mPickerConfig);
        day.setViewAdapter(mDayAdapter);

        if (mRepository.isMinMonth(curYear, curMonth)) {
            day.setCurrentItem(0, true);
        }

        int dayCount = mDayAdapter.getItemsCount();
        if (day.getCurrentItem() >= dayCount) {
            day.setCurrentItem(dayCount - 1, true);
        }
    }

    void updateHours() {
        if (hour.getVisibility() == View.GONE)
            return;

        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();
        int curDay = getCurrentDay();

        int minHour = mRepository.getMinHour(curYear, curMonth, curDay);
        int maxHour = mRepository.getMaxHour(curYear, curMonth, curDay);

        mHourAdapter = getNumericWheelAdapter(minHour, maxHour, mPickerConfig.mHour);
        mHourAdapter.setConfig(mPickerConfig);
        hour.setViewAdapter(mHourAdapter);

        if (mRepository.isMinDay(curYear, curMonth, curDay))
            hour.setCurrentItem(0, false);
    }

    void updateMinutes() {
        if (minute.getVisibility() == View.GONE)
            return;

        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();
        int curDay = getCurrentDay();
        int curHour = getCurrentHour();

        int minMinute = mRepository.getMinMinute(curYear, curMonth, curDay, curHour);
        int maxMinute = mRepository.getMaxMinute(curYear, curMonth, curDay, curHour);

        mMinuteAdapter = getNumericWheelAdapter(minMinute, maxMinute, mPickerConfig.mMinute);
        mMinuteAdapter.setConfig(mPickerConfig);
        minute.setViewAdapter(mMinuteAdapter);

        if (mRepository.isMinHour(curYear, curMonth, curDay, curHour))
            minute.setCurrentItem(0, false);
    }

    /**
     * 是否选择自定义年份文本
     *
     * @return
     */
    public boolean hasChooseCustomYearStr() {
        return mYearAdapter.hasCustomLastItem() && year.getCurrentItem() == mYearAdapter.getItemsCount() - 1;
    }

    public int getCurrentYear() {
        return year.getCurrentItem() + mRepository.getMinYear();
    }

    public int getCurrentMonth() {
        int curYear = getCurrentYear();
        return month.getCurrentItem() + +mRepository.getMinMonth(curYear);
    }

    public int getCurrentDay() {
        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();
        return day.getCurrentItem() + mRepository.getMinDay(curYear, curMonth);
    }

    public int getCurrentHour() {
        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();
        int curDay = getCurrentDay();
        return hour.getCurrentItem() + mRepository.getMinHour(curYear, curMonth, curDay);
    }

    public int getCurrentMinute() {
        int curYear = getCurrentYear();
        int curMonth = getCurrentMonth();
        int curDay = getCurrentDay();
        int curHour = getCurrentHour();

        return minute.getCurrentItem() + mRepository.getMinMinute(curYear, curMonth, curDay, curHour);
    }

    public NumericWheelAdapter getNumericWheelAdapter(int minValue, int maxValue,
                                                      String unit) {
        //是否显示单位
        unit = mPickerConfig.mIsShowItemUnit ? unit : "";

        if (mPickerConfig.mItemResource != 0 && mPickerConfig.mItemTextResource != 0) {
            return new NumericWheelAdapter(mContext, minValue, maxValue, mPickerConfig.mItemFormat, unit, mPickerConfig.mItemResource, mPickerConfig.mItemTextResource);
        }

        if (mPickerConfig.mItemResource != 0) {
            return new NumericWheelAdapter(mContext, minValue, maxValue, mPickerConfig.mItemFormat, unit, mPickerConfig.mItemResource);
        }

        return new NumericWheelAdapter(mContext, minValue, maxValue, mPickerConfig.mItemFormat, unit);
    }


}
