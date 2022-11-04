package com.onetoall.yjt.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shaomengjie on 2016/11/21.
 */

public class PersonnelManagementBean implements Serializable {

        /**
         * user_name : 15294635507
         * gender : 1
         * user_id : 48
         * mobile : 15294635507
         * crdate : 1476236464
         * email :
         * nickname : ??
         * user_lev :
         */

        private UserInfoBean user_info;
        private String token;
        /**
         * sub_account : {"store_id":"20161010MDchy12","user_name":"15670632850","gender":"0","user_id":57,"mobile":"15670632850","crdate":1476249857,"email":"","nickname":"?","user_lev":""}
         */

        private List<AccountArrBean> account_arr;
        /**
         * store_id : 147601487248262
         * store_number : 20161010MDchy02
         * store_name : 上海3二坦金融信息有限公司
         * client : 13980887363
         * merchant_id : 147601159537883
         * lev : 1
         */

        private List<StoreArrBean> store_arr;

        public UserInfoBean getUser_info() {
            return user_info;
        }

        public void setUser_info(UserInfoBean user_info) {
            this.user_info = user_info;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public List<AccountArrBean> getAccount_arr() {
            return account_arr;
        }

        public void setAccount_arr(List<AccountArrBean> account_arr) {
            this.account_arr = account_arr;
        }

        public List<StoreArrBean> getStore_arr() {
            return store_arr;
        }

        public void setStore_arr(List<StoreArrBean> store_arr) {
            this.store_arr = store_arr;
        }

        public static class UserInfoBean {
            private String user_name;
            private String gender;
            private int user_id;
            private String mobile;
            private int crdate;
            private String email;
            private String nickname;
            private String user_lev;
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUser_name() {
                return user_name;
            }

            public void setUser_name(String user_name) {
                this.user_name = user_name;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public int getCrdate() {
                return crdate;
            }

            public void setCrdate(int crdate) {
                this.crdate = crdate;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getUser_lev() {
                return user_lev;
            }

            public void setUser_lev(String user_lev) {
                this.user_lev = user_lev;
            }
        }

        public static class AccountArrBean {
            /**
             * store_id : 20161010MDchy12
             * user_name : 15670632850
             * gender : 0
             * user_id : 57
             * mobile : 15670632850
             * crdate : 1476249857
             * email :
             * nickname : ?
             * user_lev :
             */

            private SubAccountBean sub_account;

            public SubAccountBean getSub_account() {
                return sub_account;
            }

            public void setSub_account(SubAccountBean sub_account) {
                this.sub_account = sub_account;
            }

            public static class SubAccountBean {
                private String store_id;
                private String user_name;
                private String gender;
                private int user_id;
                private String mobile;
                private int crdate;
                private String email;
                private String nickname;
                private String user_lev;
                private String name;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getStore_id() {
                    return store_id;
                }

                public void setStore_id(String store_id) {
                    this.store_id = store_id;
                }

                public String getUser_name() {
                    return user_name;
                }

                public void setUser_name(String user_name) {
                    this.user_name = user_name;
                }

                public String getGender() {
                    return gender;
                }

                public void setGender(String gender) {
                    this.gender = gender;
                }

                public int getUser_id() {
                    return user_id;
                }

                public void setUser_id(int user_id) {
                    this.user_id = user_id;
                }

                public String getMobile() {
                    return mobile;
                }

                public void setMobile(String mobile) {
                    this.mobile = mobile;
                }

                public int getCrdate() {
                    return crdate;
                }

                public void setCrdate(int crdate) {
                    this.crdate = crdate;
                }

                public String getEmail() {
                    return email;
                }

                public void setEmail(String email) {
                    this.email = email;
                }

                public String getNickname() {
                    return nickname;
                }

                public void setNickname(String nickname) {
                    this.nickname = nickname;
                }

                public String getUser_lev() {
                    return user_lev;
                }

                public void setUser_lev(String user_lev) {
                    this.user_lev = user_lev;
                }
            }
        }

        public static class StoreArrBean {
            private long store_id;
            private String store_number;
            private String store_name;
            private String client;
            private long merchant_id;
            private String lev;

            public long getStore_id() {
                return store_id;
            }

            public void setStore_id(long store_id) {
                this.store_id = store_id;
            }

            public String getStore_number() {
                return store_number;
            }

            public void setStore_number(String store_number) {
                this.store_number = store_number;
            }

            public String getStore_name() {
                return store_name;
            }

            public void setStore_name(String store_name) {
                this.store_name = store_name;
            }

            public String getClient() {
                return client;
            }

            public void setClient(String client) {
                this.client = client;
            }

            public long getMerchant_id() {
                return merchant_id;
            }

            public void setMerchant_id(long merchant_id) {
                this.merchant_id = merchant_id;
            }

            public String getLev() {
                return lev;
            }

            public void setLev(String lev) {
                this.lev = lev;
            }
        }

}
