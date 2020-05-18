package com.demo.myapplication;

public class DateCustom {
    private int ngay;
    private int thang;
    private int nam;

    public DateCustom(int ngay, int thang, int nam){
        this.ngay = ngay;
        this.thang = thang;
        this.nam = nam;
    }

    public String getShow(){
        String str = this.getNgay() + "/" + this.getThang() + "/" + this.getNam();
        return str;
    }

    public int getNgay() {
        return ngay;
    }

    public void setNgay(int ngay) {
        this.ngay = ngay;
    }

    public int getThang() {
        return thang;
    }

    public void setThang(int thang) {
        this.thang = thang;
    }

    public int getNam() {
        return nam;
    }

    public void setNam(int nam) {
        this.nam = nam;
    }
}
