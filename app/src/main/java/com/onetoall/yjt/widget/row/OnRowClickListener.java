package com.onetoall.yjt.widget.row;

import android.view.View;

import com.onetoall.yjt.widget.row.tool.RowActionEnum;


public interface OnRowClickListener extends BaseRowViewClickListener {
    void onRowClick(View rowView, RowActionEnum action);
}
