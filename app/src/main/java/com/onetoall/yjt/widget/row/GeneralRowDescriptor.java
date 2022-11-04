package com.onetoall.yjt.widget.row;

import com.onetoall.yjt.widget.row.tool.RowActionEnum;
import com.onetoall.yjt.widget.row.tool.RowClassEnum;

public class GeneralRowDescriptor extends BaseRowDescriptor {
    public String num;
    public int iconResId;
    public String label;

    public GeneralRowDescriptor(int iconResId, String label, RowActionEnum action) {
        this.iconResId = iconResId;
        this.label = label;
        this.action = action;
        this.clazz = RowClassEnum.GeneralRowView;
    }

    public GeneralRowDescriptor(int iconResId, String label, String num, RowActionEnum action) {
        this.iconResId = iconResId;
        this.label = label;
        this.num = num;
        this.action = action;
        this.clazz = RowClassEnum.GeneralRowView;
    }
}
