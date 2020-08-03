package com.licheedev.MMSSNK.QC_check;

public class QcDetailItem {
    private boolean stCheck;
    private String mqhno;
    private String sub;
    private String check;
    private String qty;


    public QcDetailItem(boolean stCheck, String mqhno, String sub, String check, String qty) {
        this.stCheck = stCheck;
        this.mqhno = mqhno;
        this.sub = sub;
        this.check = check;
        this.qty = qty;
    }

    public void setStCheck(boolean stCheck) {
        this.stCheck = stCheck;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public boolean isStCheck() {
        return stCheck;
    }

    public String getMqhno() {
        return mqhno;
    }

    public String getSub() {
        return sub;
    }

    public String getCheck() {
        return check;
    }

    public String getQty() {
        return qty;
    }
}
