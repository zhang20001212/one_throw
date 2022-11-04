package com.onetoall.yjt.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.onetoall.yjt.R;


/**
 * LoadingView解决了请求网络数据时ui显示的三种状态
 * 分别为加载中，加载失败，无数据
 * email: qinwei_it@163.com
 *
 * @author qinwei create by 2015/10/28
 *         update time 2016/11/16
 */
public class RefreshView extends FrameLayout implements OnClickListener {

    private LinearLayout empty;
    private LinearLayout error;
    private LinearLayout loading;
    private State state;
    private OnRetryListener listener;

    public interface OnRetryListener {
        void onRetry();
    }

    public enum State {
        ing, error, done, empty
    }

    public RefreshView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeView(context);
    }

    public RefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView(context);
    }

    public RefreshView(Context context) {
        super(context);
        initializeView(context);
    }

    private void initializeView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.widget_refresh_view, this);
        empty = (LinearLayout) findViewById(R.id.empty);
        loading = (LinearLayout) findViewById(R.id.loading);
        error = (LinearLayout) findViewById(R.id.error);
        error.setOnClickListener(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
//        TODO check inner view count
        int childCount = getChildCount();
        if (childCount != 4) {
            throw new IllegalArgumentException("you must set one child view in RefreshView");
        }
    }

    public void notifyDataChanged(State state) {
        this.state = state;
        switch (state) {
            case ing:
                loading.setVisibility(View.VISIBLE);
                empty.setVisibility(View.GONE);
                error.setVisibility(View.GONE);
                getContentView().setVisibility(GONE);
                break;
            case empty:
                loading.setVisibility(View.GONE);
                empty.setVisibility(View.VISIBLE);
                error.setVisibility(View.GONE);
                getContentView().setVisibility(GONE);
                break;
            case error:
                loading.setVisibility(View.GONE);
                empty.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
                getContentView().setVisibility(GONE);
                break;
            case done:
                loading.setVisibility(View.GONE);
                empty.setVisibility(View.GONE);
                error.setVisibility(View.GONE);
                getContentView().setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    private View getContentView() {
        return getChildAt(getChildCount() - 1);
    }

    public void setEmptyView(View view) {
        empty.removeAllViews();
        empty.addView(view, getLayoutParams());
    }


    public void setEmptyView(int layoutId) {
        View view = LayoutInflater.from(getContext()).inflate(layoutId, null);
        setEmptyView(view);
    }

    public void setOnRetryListener(OnRetryListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null && state == State.error) {
            listener.onRetry();
        }
    }
}
