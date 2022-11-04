package com.onetoall.yjt.widget.time;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by qinwei on 2015/12/1 11:43
 * email:qinwei_it@163.com
 */
public class TimeButton extends Button {
    private TimeTask timeTask;
    private int length;
    private int curTime;
    private OnTimeChangedListener listener;

    public TimeButton(Context context) {
        super(context);
        initializeView();
    }

    public TimeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView();
    }

    public TimeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeView();
    }

    private void initializeView() {

    }

    public void setOnTimeChangedListener(OnTimeChangedListener listener) {
        this.listener = listener;
    }

    public void start() {
        start(60);
    }

    public void start(int length) {
        if (isEnabled()) {
            this.length = length;
            curTime = length;
            if (listener != null) {
                setEnabled(false);
            }
            timeTask = new TimeTask();
            timeTask.execute(length);
        }
    }

    public void reset() {
        setEnabled(true);
        if (timeTask != null) {
            timeTask.cancel(true);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        reset();
    }

    public class TimeTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                for (int i = 0; i < length; i++) {
                    curTime--;
                    publishProgress(curTime);
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (listener != null) {
                setEnabled(true);
                listener.onTimeCompleted();
            }
            super.onPostExecute(integer);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (listener != null) {
                listener.onTimeChanged(values[0]);
            }
            super.onProgressUpdate(values);
        }
    }

    public interface OnTimeChangedListener {
        void onTimeChanged(int ss);

        void onTimeCompleted();
    }
}
