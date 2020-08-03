package com.licheedev.MMSSNK.WO;

public class MaterialMaster {
    private String process_type;
    private String mt_nm;
    private String width;
    private String need_qty;
    private String feed_size;
    private String feed_unit;
    private String div_cd;

    public MaterialMaster(String process_type, String mt_nm, String width, String need_qty, String feed_size, String feed_unit, String div_cd) {
        this.process_type = process_type;
        this.mt_nm = mt_nm;
        this.width = width;
        this.need_qty = need_qty;
        this.feed_size = feed_size;
        this.feed_unit = feed_unit;
        this.div_cd = div_cd;
    }

    public String getProcess_type() {
        return process_type;
    }

    public void setProcess_type(String process_type) {
        this.process_type = process_type;
    }

    public String getMt_nm() {
        return mt_nm;
    }

    public void setMt_nm(String mt_nm) {
        this.mt_nm = mt_nm;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getNeed_qty() {
        return need_qty;
    }

    public void setNeed_qty(String need_qty) {
        this.need_qty = need_qty;
    }

    public String getFeed_size() {
        return feed_size;
    }

    public void setFeed_size(String feed_size) {
        this.feed_size = feed_size;
    }

    public String getFeed_unit() {
        return feed_unit;
    }

    public void setFeed_unit(String feed_unit) {
        this.feed_unit = feed_unit;
    }

    public String getDiv_cd() {
        return div_cd;
    }

    public void setDiv_cd(String div_cd) {
        this.div_cd = div_cd;
    }

    @Override
    public String toString() {
        return  "{\"process_type\":\"" + process_type +
                "\",\"mt_nm\":\"" + mt_nm +
                "\",\"width\":\"" + width +
                "\",\"need_qty\":\""+ need_qty +
                "\",\"feed_size\":\"" + feed_size +
                "\",\"feed_unit\":\"" + feed_unit +
                "\",\"div_cd\":\"" + div_cd +
                "\"}";
    }
}
