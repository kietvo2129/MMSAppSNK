package com.licheedev.MMSSNK.QC_check;

public class QcCheckItem {
    private String idmqno;
    private String mq_no;
    private String workdate;
    private String checkqty;
    private String okqty;
    private String defectqty;


    public QcCheckItem(String idmqno, String mq_no, String workdate, String checkqty, String okqty, String defectqty) {
        this.idmqno = idmqno;
        this.mq_no = mq_no;
        this.workdate = workdate;
        this.checkqty = checkqty;
        this.okqty = okqty;
        this.defectqty = defectqty;
    }

    public String getMq_no() {
        return mq_no;
    }

    public String getIdMqno() {
        return idmqno;
    }

    public String getWorkdate() {
        return workdate;
    }

    public String getCheckqty() {
        return checkqty;
    }

    public String getOkqty() {
        return okqty;
    }

    public String getDefectqty() {
        return defectqty;
    }
}