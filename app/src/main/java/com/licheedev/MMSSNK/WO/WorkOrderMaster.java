package com.licheedev.MMSSNK.WO;

public class WorkOrderMaster {
    private String fo_no;
    private String product_code;
    private String product_name;
    private String start_dt;
    private String end_dt;
    private String target_qty;
    private String sked_qty;
    private String po_no;
    private String line_no;
    private String factory;

    public WorkOrderMaster(String fo_no, String product_code, String product_name, String start_dt, String end_dt, String target_qty, String sked_qty, String po_no, String line_no, String factory) {
        this.fo_no = fo_no;
        this.product_code = product_code;
        this.product_name = product_name;
        this.start_dt = start_dt;
        this.end_dt = end_dt;
        this.target_qty = target_qty;
        this.sked_qty = sked_qty;
        this.po_no = po_no;
        this.line_no = line_no;
        this.factory = factory;
    }

    public String getFo_no() {
        return fo_no;
    }

    public void setFo_no(String fo_no) {
        this.fo_no = fo_no;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
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

    public String getTarget_qty() {
        return target_qty;
    }

    public void setTarget_qty(String target_qty) {
        this.target_qty = target_qty;
    }

    public String getSked_qty() {
        return sked_qty;
    }

    public void setSked_qty(String sked_qty) {
        this.sked_qty = sked_qty;
    }

    public String getPo_no() {
        return po_no;
    }

    public void setPo_no(String po_no) {
        this.po_no = po_no;
    }

    public String getLine_no() {
        return line_no;
    }

    public void setLine_no(String line_no) {
        this.line_no = line_no;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    @Override
    public String toString() {
        return  "{\"fo_no\":\"" + fo_no +
                "\",\"product_code\":\"" + product_code +
                "\",\"product_name\":\"" + product_name +
                "\",\"start_dt\":\""+ start_dt +
                "\",\"end_dt\":\"" + end_dt +
                "\",\"target_qty\":\"" + target_qty +
                "\",\"sked_qty\":\"" + sked_qty +
                "\",\"po_no\":\""+ po_no +
                "\",\"line_no\":\"" + line_no +
                "\",\"factory\":\"" + factory +
                "\"}";
    }
}
