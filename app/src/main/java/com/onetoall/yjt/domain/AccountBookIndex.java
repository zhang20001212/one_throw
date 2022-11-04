package com.onetoall.yjt.domain;

import java.util.ArrayList;

/**
 * Created by qinwei on 2016/10/22 18:39
 * email:qinwei_it@163.com
 */

public class AccountBookIndex {
    private PayTotal statistics;
    private ArrayList<PayOrder> list;

    public PayTotal getStatistics() {
        return statistics;
    }

    public void setStatistics(PayTotal statistics) {
        this.statistics = statistics;
    }

    public ArrayList<PayOrder> getList() {
        return list;
    }

    public void setList(ArrayList<PayOrder> list) {
        this.list = list;
    }
}
