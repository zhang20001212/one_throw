package com.onetoall.yjt.widget.row.expand;


import com.onetoall.yjt.widget.row.BaseRowDescriptor;
import com.onetoall.yjt.widget.row.tool.RowActionEnum;
import com.onetoall.yjt.widget.row.tool.RowClassEnum;

public class IOSRowDescriptor extends BaseRowDescriptor {
    public int iconResId;
    public String label;
    public String value;

    public IOSRowDescriptor(int iconResId, String label, RowActionEnum action) {
        this.iconResId = iconResId;
        this.label = label;
        this.action = action;
        this.clazz = RowClassEnum.IOSRowView;
    }

    public IOSRowDescriptor(int iconResId, String label, String value, RowActionEnum action) {
        this.value = value;
        this.iconResId = iconResId;
        this.label = label;
        this.action = action;
        this.clazz = RowClassEnum.IOSRowView;
    }

    public IOSRowDescriptor(String label, String value, RowActionEnum action) {
        this.value = value;
        this.label = label;
        this.action = action;
        this.clazz = RowClassEnum.IOSRowView;
    }

    public IOSRowDescriptor(String label, RowActionEnum action) {
        this.label = label;
        this.action = action;
        this.clazz = RowClassEnum.IOSRowView;
    }
}
