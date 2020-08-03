package com.licheedev.MMSSNK.Actual;

public class User {
    private String Ten;
    private boolean isChecked;

    public User(String ten, boolean isChecked) {
        Ten = ten;
        this.isChecked = isChecked;
    }

    public String getTen() {
        return Ten;
    }

    public void setTen(String ten) {
        Ten = ten;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
