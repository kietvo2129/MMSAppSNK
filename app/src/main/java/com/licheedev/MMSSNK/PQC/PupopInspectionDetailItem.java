package com.licheedev.MMSSNK.PQC;

public class PupopInspectionDetailItem {
    private boolean checkbox;
    private String mqhno;
    private String subject;
    private String value;
    private String qty;

    //"mqhno": 58,
    //"check_subject": "cân nặng",
    //"check_value": "nặng 40kg",
    //"check_qty": 10

    public PupopInspectionDetailItem(boolean checkbox, String mqhno, String subject, String value, String qty) {
        this.checkbox = checkbox;
        this.mqhno = mqhno;
        this.subject = subject;
        this.value = value;
        this.qty = qty;
    }

    public String getMqhno() {
        return mqhno;
    }

    public void setMqhno(String mqhno) {
        this.mqhno = mqhno;
    }

    public boolean isCheckbox() {
        return checkbox;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }
}
