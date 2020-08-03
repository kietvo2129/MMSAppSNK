package com.licheedev.MMSSNK.TotalInspection.TotalInspectionDetail.Thinkness;

public class QcCheckThinknessItem {
    private String pino;
    private String check_qty;
    private String ok_qty;
    private String defect_qty;
    private String defect_rate;

    public QcCheckThinknessItem(String pino, String check_qty, String ok_qty, String defect_qty, String defect_rate) {
        this.pino = pino;
        this.check_qty = check_qty;
        this.ok_qty = ok_qty;
        this.defect_qty = defect_qty;
        this.defect_rate = defect_rate;
    }

    public String getPino() {
        return pino;
    }

    public void setPino(String pino) {
        this.pino = pino;
    }

    public String getCheck_qty() {
        return check_qty;
    }

    public void setCheck_qty(String check_qty) {
        this.check_qty = check_qty;
    }

    public String getOk_qty() {
        return ok_qty;
    }

    public void setOk_qty(String ok_qty) {
        this.ok_qty = ok_qty;
    }

    public String getDefect_qty() {
        return defect_qty;
    }

    public void setDefect_qty(String defect_qty) {
        this.defect_qty = defect_qty;
    }

    public String getDefect_rate() {
        return defect_rate;
    }

    public void setDefect_rate(String defect_rate) {
        this.defect_rate = defect_rate;
    }
}