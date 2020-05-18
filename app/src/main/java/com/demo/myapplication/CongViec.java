package com.demo.myapplication;

import android.icu.text.CaseMap;
import android.icu.text.DateFormat;

import java.util.Calendar;
import java.util.Date;

public class CongViec {
    private String tenCongViec;
    private String noiDungCongViec;
    private DateCustom ngayHoanThanh = new DateCustom(0,0,0);
    private HenGio henGio = new HenGio(0, 0);

    public CongViec(String tenCongViec, String noiDungCongViec, DateCustom ngayHoanThanh, HenGio henGio) {
        this.tenCongViec = tenCongViec;
        this.noiDungCongViec = noiDungCongViec;
        this.ngayHoanThanh.setNgay(ngayHoanThanh.getNgay());
        this.ngayHoanThanh.setThang(ngayHoanThanh.getThang());
        this.ngayHoanThanh.setNam(ngayHoanThanh.getNam());

        // set bao thuc
        this.henGio.setGio(henGio.getGio());
        this.henGio.setPhut(henGio.getPhut());
    }

    public CongViec(String tenCongViec, String noiDungCongViec, DateCustom ngayHoanThanh) {
        this.tenCongViec = tenCongViec;
        this.noiDungCongViec = noiDungCongViec;
        this.ngayHoanThanh.setNgay(ngayHoanThanh.getNgay());
        this.ngayHoanThanh.setThang(ngayHoanThanh.getThang());
        this.ngayHoanThanh.setNam(ngayHoanThanh.getNam());
    }

    public String getTenCongViec() {
        return tenCongViec;
    }
    public void setTenCongViec(String tenCongViec) {
        this.tenCongViec = tenCongViec;
    }

    public String getNoiDungCongViec() {
        return noiDungCongViec;
    }
    public void setNoiDungCongViec(String noiDungCongViec) {
        this.noiDungCongViec = noiDungCongViec;
    }


    public DateCustom getNgayHoanThanh() {
        return ngayHoanThanh;
    }
    public void setNgayHoanThanh(DateCustom ngayHoanThanh) {
        this.ngayHoanThanh = ngayHoanThanh;
    }

    public HenGio getHenGio() {
        return henGio;
    }
    public void setHenGio(HenGio henGio) {
        this.henGio = henGio;
    }
}
