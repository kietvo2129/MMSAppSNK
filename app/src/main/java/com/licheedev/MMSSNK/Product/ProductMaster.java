package com.licheedev.MMSSNK.Product;

public class ProductMaster {
    private String wmtid;
    private String gr_qty;
    private String bb_no;
    private String mt_qrcode;

    public ProductMaster(String wmtid, String gr_qty, String bb_no, String mt_qrcode) {
        this.wmtid = wmtid;
        this.gr_qty = gr_qty;
        this.bb_no = bb_no;
        this.mt_qrcode = mt_qrcode;
    }

    public String getWmtid() {
        return wmtid;
    }

    public void setWmtid(String wmtid) {
        this.wmtid = wmtid;
    }

    public String getGr_qty() {
        return gr_qty;
    }

    public void setGr_qty(String gr_qty) {
        this.gr_qty = gr_qty;
    }

    public String getBb_no() {
        return bb_no;
    }

    public void setBb_no(String bb_no) {
        this.bb_no = bb_no;
    }

    public String getMt_qrcode() {
        return mt_qrcode;
    }

    public void setMt_qrcode(String mt_qrcode) {
        this.mt_qrcode = mt_qrcode;
    }
}
