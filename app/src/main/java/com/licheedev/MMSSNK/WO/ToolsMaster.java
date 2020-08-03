package com.licheedev.MMSSNK.WO;

public class ToolsMaster {
    private String prounit_cd;
    private String mc_type;
    private String mc_no;
    private String t_start_dt;
    private String t_end_dt;

    public ToolsMaster(String prounit_cd, String mc_type, String mc_no, String t_start_dt, String t_end_dt) {
        this.prounit_cd = prounit_cd;
        this.mc_type = mc_type;
        this.mc_no = mc_no;
        this.t_start_dt = t_start_dt;
        this.t_end_dt = t_end_dt;
    }

    public String getProunit_cd() {
        return prounit_cd;
    }

    public void setProunit_cd(String prounit_cd) {
        this.prounit_cd = prounit_cd;
    }

    public String getMc_type() {
        return mc_type;
    }

    public void setMc_type(String mc_type) {
        this.mc_type = mc_type;
    }

    public String getMc_no() {
        return mc_no;
    }

    public void setMc_no(String mc_no) {
        this.mc_no = mc_no;
    }

    public String getT_start_dt() {
        return t_start_dt;
    }

    public void setT_start_dt(String t_start_dt) {
        this.t_start_dt = t_start_dt;
    }

    public String getT_end_dt() {
        return t_end_dt;
    }

    public void setT_end_dt(String t_end_dt) {
        this.t_end_dt = t_end_dt;
    }

    @Override
    public String toString() {
        return  "{\"prounit_cd\":\"" + prounit_cd +
                "\",\"mc_type\":\"" + mc_type +
                "\",\"mc_no\":\"" + mc_no +
                "\",\"t_start_dt\":\""+ t_start_dt +
                "\",\"t_end_dt\":\"" + t_end_dt +
                "\"}";
    }
}
