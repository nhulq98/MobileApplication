package com.demo.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CongViecAdapter extends BaseAdapter {

    Context myContext;
    int myLayout;
    ArrayList<CongViec> listCongViec;

    public CongViecAdapter(Context context, int layout, ArrayList<CongViec> listCongViec){
        this.myContext = context;
        this.myLayout = layout;
        this.listCongViec = listCongViec;
    }
    @Override
    public int getCount() {// số lượng dòng của listcongviec
        return listCongViec.size();
    }
    @Override
    public Object getItem(int position) {
        return null;
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Lấy các view từ mylayout và bỏ vào convertView
        convertView = inflater.inflate(myLayout,null);

        // ánh xạ
        TextView edt_TenCongViec = convertView.findViewById(R.id.txt_tencongviec);
        TextView edt_NoiDung = convertView.findViewById(R.id.txt_noidung);
        TextView edt_NgayHoanThanh = convertView.findViewById(R.id.txt_ngayhoanthanh);
        TextView edt_HenGio = convertView.findViewById(R.id.txt_hengio);

        // gán giá trị
        edt_TenCongViec.setText(listCongViec.get(position).getTenCongViec());
        edt_NoiDung.setText(listCongViec.get(position).getNoiDungCongViec());

        DateCustom ngayHoanThanh = listCongViec.get(position).getNgayHoanThanh();
        edt_NgayHoanThanh.setText(ngayHoanThanh.getShow());

        HenGio henGio = listCongViec.get(position).getHenGio();
        //Log.d("hen gio", henGio.getGio() + ": " + henGio.getPhut());
        edt_HenGio.setText(henGio.getShow());

        return convertView;
    }
}
