package com.licheedev.MMSSNK.Actual;

public class countrydanhsach {
    private String mdate;
    private String mdateview;
    private String mtaget;
    private String mhour;
    private String mprcunit;

    public countrydanhsach(String dateview,String date, String taget, String hour, String prcunit){
        mdateview =dateview;
        mdate = date;
        mtaget = taget;
        mhour = hour;
        mprcunit = prcunit;
    }

    public String getmdateview() {
        return mdateview;
    }
    public String getdate(){
        return mdate;
    }
    public String gettaget(){
        return mtaget;
    }
    public String gethour(){
        return mhour;
    }
    public String getprcunit(){
        return mprcunit;
    }
}
