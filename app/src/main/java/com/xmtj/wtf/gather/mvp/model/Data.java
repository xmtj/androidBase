package com.xmtj.wtf.gather.mvp.model;

/**
 * Created by wanglei on 2017/11/3.
 */

public class Data {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Data{" +
                "user=" + user +
                '}';
    }
}
