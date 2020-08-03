package com.licheedev.MMSSNK.WO;

public class StaffMaster {
    private String prounit_cd;
    private String staff_id;
    private String uname;
    private String dt_nm;
    private String s_start_dt;
    private String s_end_dt;

    public StaffMaster(String prounit_cd, String staff_id, String uname, String dt_nm, String s_start_dt, String s_end_dt) {
        this.prounit_cd = prounit_cd;
        this.staff_id = staff_id;
        this.uname = uname;
        this.dt_nm = dt_nm;
        this.s_start_dt = s_start_dt;
        this.s_end_dt = s_end_dt;
    }

    public String getProunit_cd() {
        return prounit_cd;
    }

    public void setProunit_cd(String prounit_cd) {
        this.prounit_cd = prounit_cd;
    }

    public String getStaff_id() {
        return staff_id;
    }

    public void setStaff_id(String staff_id) {
        this.staff_id = staff_id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getDt_nm() {
        return dt_nm;
    }

    public void setDt_nm(String dt_nm) {
        this.dt_nm = dt_nm;
    }

    public String getS_start_dt() {
        return s_start_dt;
    }

    public void setS_start_dt(String s_start_dt) {
        this.s_start_dt = s_start_dt;
    }

    public String getS_end_dt() {
        return s_end_dt;
    }

    public void setS_end_dt(String s_end_dt) {
        this.s_end_dt = s_end_dt;
    }

    @Override
    public String toString() {
        return  "{\"prounit_cd\":\"" + prounit_cd +
                "\",\"staff_id\":\"" + staff_id +
                "\",\"uname\":\"" + uname +
                "\",\"dt_nm\":\""+ dt_nm +
                "\",\"s_start_dt\":\"" + s_start_dt +
                "\",\"s_end_dt\":\"" + s_end_dt +
                "\"}";
    }
}
