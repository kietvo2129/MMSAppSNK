package com.licheedev.MMSSNK.Status;

public class StatusMaster {
    String line_no;
    String prod_qty;
    String done_qty;
    String process_no;
    String prounit_cd;
    String product_code;
    String compost_cd;
    String color;
    String process_sts_cd;

    public StatusMaster(String line_no, String prod_qty, String done_qty, String process_no, String prounit_cd, String product_code, String compost_cd, String color, String process_sts_cd) {
        this.line_no = line_no;
        this.prod_qty = prod_qty;
        this.done_qty = done_qty;
        this.process_no = process_no;
        this.prounit_cd = prounit_cd;
        this.product_code = product_code;
        this.compost_cd = compost_cd;
        this.color = color;
        this.process_sts_cd = process_sts_cd;
    }

    public String getLine_no() {
        return line_no;
    }

    public void setLine_no(String line_no) {
        this.line_no = line_no;
    }

    public String getProd_qty() {
        return prod_qty;
    }

    public void setProd_qty(String prod_qty) {
        this.prod_qty = prod_qty;
    }

    public String getDone_qty() {
        return done_qty;
    }

    public void setDone_qty(String done_qty) {
        this.done_qty = done_qty;
    }

    public String getProcess_no() {
        return process_no;
    }

    public void setProcess_no(String process_no) {
        this.process_no = process_no;
    }

    public String getProunit_cd() {
        return prounit_cd;
    }

    public void setProunit_cd(String prounit_cd) {
        this.prounit_cd = prounit_cd;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getCompost_cd() {
        return compost_cd;
    }

    public void setCompost_cd(String compost_cd) {
        this.compost_cd = compost_cd;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getProcess_sts_cd() {
        return process_sts_cd;
    }

    public void setProcess_sts_cd(String process_sts_cd) {
        this.process_sts_cd = process_sts_cd;
    }
}
