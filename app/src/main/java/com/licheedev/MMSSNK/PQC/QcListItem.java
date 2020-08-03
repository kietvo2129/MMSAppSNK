package com.licheedev.MMSSNK.PQC;

public class QcListItem {
    private String icno;
    private String item_vcd;
    private String check_type;
    private String check_subject;
    private String check_id;

//        "icno": 6,
//        "item_vcd": "MT00001A",
//        "check_type": "check",
//        "check_subject": "chiều cao",
//        "check_id": "MT00001A002"

    public QcListItem(String icno, String item_vcd, String check_type, String check_subject, String check_id) {
        this.icno = icno;
        this.item_vcd = item_vcd;
        this.check_type = check_type;
        this.check_subject = check_subject;
        this.check_id = check_id;
    }

    public String getCheck_id() {
        return check_id;
    }

    public String getIcno() {
        return icno;
    }

    public String getItem_vcd() {
        return item_vcd;
    }

    public String getCheck_type() {
        return check_type;
    }

    public String getCheck_subject() {
        return check_subject;
    }
}