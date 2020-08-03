package com.licheedev.MMSSNK.PQC;

public class PupopInspectionListQCDetailitem {
    private boolean checkbox;
    private String id_check_dt;
    private String qty;
    private String content;

//            "id_check_dt": 9,
//            "qty": "0",
//            "content": "nặng 40kg"

    public PupopInspectionListQCDetailitem(boolean checkbox, String id_check_dt, String qty, String content) {
        this.checkbox = checkbox;
        this.id_check_dt = id_check_dt;
        this.qty = qty;
        this.content = content;
    }

    public boolean isCheckbox() {
        return checkbox;
    }

    public void setCheckbox(boolean checkbox) {
        this.checkbox = checkbox;
    }

    public String getId_check_dt() {
        return id_check_dt;
    }

    public void setId_check_dt(String id_check_dt) {
        this.id_check_dt = id_check_dt;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

