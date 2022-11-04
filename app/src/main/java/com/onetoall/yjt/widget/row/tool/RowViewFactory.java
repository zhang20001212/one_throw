package com.onetoall.yjt.widget.row.tool;


import android.content.Context;

import com.onetoall.yjt.widget.row.BaseRowView;
import com.onetoall.yjt.widget.row.GeneralRowView;
import com.onetoall.yjt.widget.row.SimpleInfoRowView;
import com.onetoall.yjt.widget.row.expand.IOSRowView;


public class RowViewFactory {

    public static BaseRowView produceRowView(Context context, RowClassEnum clazz) {
        BaseRowView rowView = null;
        switch (clazz) {
            case GeneralRowView:
                rowView = new GeneralRowView(context);
                break;
            case SimpleInfoRowView:
                rowView = new SimpleInfoRowView(context);
                break;
            case IOSRowView:
                rowView = new IOSRowView(context);
                break;
            default:
                break;
        }
        return rowView;
    }
}
