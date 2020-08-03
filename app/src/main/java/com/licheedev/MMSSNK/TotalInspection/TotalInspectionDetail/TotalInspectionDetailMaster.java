package com.licheedev.MMSSNK.TotalInspection.TotalInspectionDetail;

public class TotalInspectionDetailMaster {
    private String pno;
    private String prd_lcd;
    private String prd_dt;
    private String sts;
    private String prd_qty;
    private String gr_qty;
    private String think_def_qty;
    private String visual_def_qty;
    private String total_def_qty;
    private String total_def_rate;

    public TotalInspectionDetailMaster(String pno, String prd_lcd, String prd_dt, String sts, String prd_qty, String gr_qty, String think_def_qty, String visual_def_qty, String total_def_qty, String total_def_rate) {
        this.pno = pno;
        this.prd_lcd = prd_lcd;
        this.prd_dt = prd_dt;
        this.sts = sts;
        this.prd_qty = prd_qty;
        this.gr_qty = gr_qty;
        this.think_def_qty = think_def_qty;
        this.visual_def_qty = visual_def_qty;
        this.total_def_qty = total_def_qty;
        this.total_def_rate = total_def_rate;
    }

    public String getPno() {
        return pno;
    }

    public void setPno(String pno) {
        this.pno = pno;
    }

    public String getPrd_lcd() {
        return prd_lcd;
    }

    public void setPrd_lcd(String prd_lcd) {
        this.prd_lcd = prd_lcd;
    }

    public String getPrd_dt() {
        return prd_dt;
    }

    public void setPrd_dt(String prd_dt) {
        this.prd_dt = prd_dt;
    }

    public String getSts() {
        return sts;
    }

    public void setSts(String sts) {
        this.sts = sts;
    }

    public String getPrd_qty() {
        return prd_qty;
    }

    public void setPrd_qty(String prd_qty) {
        this.prd_qty = prd_qty;
    }

    public String getGr_qty() {
        return gr_qty;
    }

    public void setGr_qty(String gr_qty) {
        this.gr_qty = gr_qty;
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
}
