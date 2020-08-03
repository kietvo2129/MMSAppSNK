package com.licheedev.MMSSNK.PQC;

public class PupopInspectionViewItem {
    private String mqno;
    private String mq_no;
    private String workdate;
    private String qty;

    //"mqno": "0",
    //"mq_no": "TOTAL",
    //"work_dt": "",
    //"check_qty": 304

    public PupopInspectionViewItem(String mqno, String mq_no, String workdate, String qty) {
        this.mqno = mqno;
        this.mq_no = mq_no;
        this.workdate = workdate;
        this.qty = qty;
    }

    public String getMqno() {
        return mqno;
    }

    public String getMq_no() {
        return mq_no;
    }

    public String getWorkdate() {
        return workdate;
    }

    public String getQty() {
        return qty;
    }
}
