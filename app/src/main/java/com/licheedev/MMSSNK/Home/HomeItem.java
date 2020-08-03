package com.licheedev.MMSSNK.Home;

public class HomeItem implements Comparable{
    private String Hometextview;
    private String Hometitel;
    private String Homemno;
    private String Homecontenner;

    public HomeItem(String Hometextview, String Hometitel, String Homecontenner, String Homemno) {
        this.Hometextview = Hometextview;
        this.Hometitel = Hometitel;
        this.Homecontenner = Homecontenner;
        this.Homemno =  Homemno;
    }

    public String getHomemno() {
        return Homemno;
    }

    public String getImageResource() {
        return Hometextview;
    }

    public String getText1() {
        return Hometitel;
    }

    public String getText2() {
        return Homecontenner;
    }

    @Override
    public int compareTo(Object o) {
        return this.getImageResource().compareTo(((HomeItem) o).getImageResource());
    }

}