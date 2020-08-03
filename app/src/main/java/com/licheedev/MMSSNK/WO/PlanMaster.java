package com.licheedev.MMSSNK.WO;

public class PlanMaster {
    private String p_date;
    private String p_process;
    private String p_prod_qty;

    public PlanMaster(String p_date, String p_process, String p_prod_qty) {
        this.p_date = p_date;
        this.p_process = p_process;
        this.p_prod_qty = p_prod_qty;
    }

    public String getP_date() {
        return p_date;
    }

    public void setP_date(String p_date) {
        this.p_date = p_date;
    }

    public String getP_process() {
        return p_process;
    }

    public void setP_process(String p_process) {
        this.p_process = p_process;
    }

    public String getP_prod_qty() {
        return p_prod_qty;
    }

    public void setP_prod_qty(String p_prod_qty) {
        this.p_prod_qty = p_prod_qty;
    }

    @Override
    public String toString() {
        return  "{\"p_date\":\"" + p_date +
                "\",\"p_process\":\"" + p_process +
                "\",\"p_prod_qty\":\"" + p_prod_qty +
                "\"}";
    }
}
