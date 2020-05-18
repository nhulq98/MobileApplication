package com.demo.myapplication;

public class HenGio {
    private int gio;
    private int phut;

    public HenGio(int gio, int phut) {
        this.gio = gio;
        this.phut = phut;
    }

    public String getShow(){
        return this.getGio() + ":" + this.getPhut();
    }

    public int getGio() {
        return gio;
    }
    public void setGio(int gio) {
        this.gio = gio;
    }

    public int getPhut() {
        return phut;
    }
    public void setPhut(int phut) {
        this.phut = phut;
    }
}
