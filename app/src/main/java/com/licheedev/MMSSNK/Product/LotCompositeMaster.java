package com.licheedev.MMSSNK.Product;

public class LotCompositeMaster {
    private String wmmid;
    private String mt_no;
    private String mt_lot;
    private String use_yn;
    private String mt_cd;
    private String mt_type;

    public LotCompositeMaster(String wmmid, String mt_no, String mt_lot, String use_yn,String mt_cd,String mt_type) {
        this.wmmid = wmmid;
        this.mt_no = mt_no;
        this.mt_lot = mt_lot;
        this.use_yn = use_yn;
        this.mt_cd = mt_cd;
        this.mt_type = mt_type;
    }



    public String getWmmid() {
        return wmmid;
    }

    public void setWmmid(String wmmid) {
        this.wmmid = wmmid;
    }

    public String getMt_no() {
        return mt_no;
    }

    public void setMt_no(String mt_no) {
        this.mt_no = mt_no;
    }

    public String getMt_lot() {
        return mt_lot;
    }

    public void setMt_lot(String mt_lot) {
        this.mt_lot = mt_lot;
    }

    public String getUse_yn() {
        return use_yn;
    }

    public void setUse_yn(String use_yn) {
        this.use_yn = use_yn;
    }

    public String getMt_cd() {
        return mt_cd;
    }

    public void setMt_cd(String mt_cd) {
        this.mt_cd = mt_cd;
    }

    public String getMt_type() {
        return mt_type;
    }

    public void setMt_type(String mt_type) {
        this.mt_type = mt_type;
    }
}
