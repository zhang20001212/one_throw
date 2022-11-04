package com.onetoall.yjt.widget.row;

import com.onetoall.yjt.widget.row.tool.RowActionEnum;
import com.onetoall.yjt.widget.row.tool.RowClassEnum;

public class SimpleInfoRowDescriptor extends BaseRowDescriptor {
    public int iconResId;
    public String iconUrl;
    public String account;
    public String nick;

    public SimpleInfoRowDescriptor(int iconResId, String account, String nick, RowActionEnum action) {
        this.iconResId = iconResId;
        this.account = account;
        this.nick = nick;
        this.action = action;
        this.clazz = RowClassEnum.SimpleInfoRowView;
    }

    public SimpleInfoRowDescriptor(String iconUrl, String account, String nick, RowActionEnum action) {
        this.iconUrl = iconUrl;
        this.account = account;
        this.nick = nick;
        this.action = action;
        this.clazz = RowClassEnum.SimpleInfoRowView;
    }
}
