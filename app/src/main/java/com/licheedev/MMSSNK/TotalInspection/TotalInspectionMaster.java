package com.licheedev.MMSSNK.TotalInspection;

public class TotalInspectionMaster {
    private String pno;
    private String fo_no;
    private String style_no;
    private String start_dt;
    private String end_dt;
    private String gr_qty;
    private String prd_qty;
    private String total_check_qty;
    private String total_def_qty;
    private String total_def_rate;
    private String think_def_qty;
    private String visual_def_qty;
    private String product_cnt;
    private String check_cnt;
    private String stt;

    public TotalInspectionMaster(String pno, String fo_no, String style_no, String start_dt, String end_dt, String gr_qty, String prd_qty, String total_check_qty, String total_def_qty, String total_def_rate, String think_def_qty, String visual_def_qty, String product_cnt, String check_cnt, String stt) {
        this.pno = pno;
        this.fo_no = fo_no;
        this.style_no = style_no;
        this.start_dt = start_dt;
        this.end_dt = end_dt;
        this.gr_qty = gr_qty;
        this.prd_qty = prd_qty;
        this.total_check_qty = total_check_qty;
        this.total_def_qty = total_def_qty;
        this.total_def_rate = total_def_rate;
        this.think_def_qty = think_def_qty;
        this.visual_def_qty = visual_def_qty;
        this.product_cnt = product_cnt;
        this.check_cnt = check_cnt;
        this.stt = stt;
    }

    public String getPno() {
        return pno;
    }

    public void setPno(String pno) {
        this.pno = pno;
    }

    public String getFo_no() {
        return fo_no;
    }

    public void setFo_no(String fo_no) {
        this.fo_no = fo_no;
    }

    public String getStyle_no() {
        return style_no;
    }

    public void setStyle_no(String style_no) {
        this.style_no = style_no;
    }

    public String getStart_dt() {
        return start_dt;
    }

    public void setStart_dt(String start_dt) {
        this.start_dt = start_dt;
    }

    public String getEnd_dt() {
        return end_dt;
    }

    public void setEnd_dt(String end_dt) {
        this.end_dt = end_dt;
    }

    public String getGr_qty() {
        return gr_qty;
    }

    public void setGr_qty(String gr_qty) {
        this.gr_qty = gr_qty;
    }

    public String getPrd_qty() {
        return prd_qty;
    }

    public void setPrd_qty(String prd_qty) {
        this.prd_qty = prd_qty;
    }

    public String getTotal_check_qty() {
        return total_check_qty;
    }

    public void setTotal_check_qty(String total_check_qty) {
        this.total_check_qty = total_check_qty;
    }

    public String getTotal_def_qty() {
        return total_def_qty;
    }

    public void setTotal_def_qty(String total_def_qty) {
        this.total_def_qty = total_def_qty;
    }

    public String getTotal_def_rate() {
        return total_def_rate;
    }

    public void setTotal_def_rate(String total_def_rate) {
        this.total_def_rate = total_def_rate;
    }

    public String getThink_def_qty() {
        return think_def_qty;
    }

    public void setThink_def_qty(String think_def_qty) {
        this.think_def_qty = think_def_qty;
    }

    public String getVisual_def_qty() {
        return visual_def_qty;
    }

    public void setVisual_def_qty(String visual_def_qty) {
        this.visual_def_qty = visual_def_qty;
    }

    public String getProduct_cnt() {
        return product_cnt;
    }

    public void setProduct_cnt(String product_cnt) {
        this.product_cnt = product_cnt;
    }

    public String getCheck_cnt() {
        return check_cnt;
    }

    public void setCheck_cnt(String check_cnt) {
        this.check_cnt = check_cnt;
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }
}
