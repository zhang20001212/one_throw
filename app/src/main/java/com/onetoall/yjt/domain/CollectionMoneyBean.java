package com.onetoall.yjt.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shaomengjie on 2016/11/20.
 */

public class CollectionMoneyBean implements Serializable {
    private String next_row;
    /**
     * payment_type : 1
     * date_time : 2016-09-11
     * counts : 6
     * sum : 0
     * buyer_id :
     * buyer : 支付宝账户
     */

    private List<ListBean> list;

    public String getNext_row() {
        return next_row;
    }

    public void setNext_row(String next_row) {
        this.next_row = next_row;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String payment_type;
        private String date_time;
        private int counts;
        private String sum;
        private String buyer_id;
        private String buyer;

        public String getPayment_type() {
            return payment_type;
        }

        public void setPayment_type(String payment_type) {
            this.payment_type = payment_type;
        }

        public String getDate_time() {
            return date_time;
        }

        public void setDate_time(String date_time) {
            this.date_time = date_time;
        }

        public int getCounts() {
            return counts;
        }

        public void setCounts(int counts) {
            this.counts = counts;
        }

        public String getSum() {
            return sum;
        }

        public void setSum(String sum) {
            this.sum = sum;
        }

        public String getBuyer_id() {
            return buyer_id;
        }

        public void setBuyer_id(String buyer_id) {
            this.buyer_id = buyer_id;
        }

        public String getBuyer() {
            return buyer;
        }

        public void setBuyer(String buyer) {
            this.buyer = buyer;
        }
    }
}
