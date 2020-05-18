package com.demo.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class QuanLyCongViec extends AppCompatActivity {
    //Database
    Database database;
    //Khai báo view
    EditText editText_TenCongViec,
            editText_NoiDung,
            editText_NgayHoanThanh, editText_HenGio;
    Button btn_Them, btn_Sua;
    ListView lv_CongViec;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    // Khai báo
    ArrayList<CongViec> listCongViec;
    CongViecAdapter congViecAdapter;
    final Calendar calendar = Calendar.getInstance();
    // Cho phép truy cập vào hệ thống báo động của máy
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    Intent intent;

    int vitri = 0;

    // function System
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_cong_viec);
        getControl();
        setEvent();
        // Tạo Database qlcongviec
        database = new Database(QuanLyCongViec.this, "dbcongviec.sqlite", null, 1);

        // Tạo bảng Công việc
        database.queryData("CREATE TABLE IF NOT EXISTS tblCongViec(TenCongViec VARCHAR(250) PRIMARY KEY, NoiDung TEXT, NgayHoanThanh VARCHAR(50), ThoiGianHoanThanh VARCHAR(50) )");

        // insert Data
        //database.queryData("INSERT INTO `CongViec`(`id`, `TenCongViec`, `NoiDung`, `NgayHoanThanh`, `ThoiGianHoanThanh`) VALUES (null,'Bài tập java','trang 22','20/05/2020','12:00')");

        // Select Data
        Cursor dataCongViec =  database.getData("SELECT * FROM tblCongViec");

        listCongViec = new ArrayList<CongViec>();
        while(dataCongViec.moveToNext()){
            String tenCongViec = dataCongViec.getString(0);
            String noiDung = dataCongViec.getString(1);
            String strngayHoanThanh = dataCongViec.getString(2);
            String strhenGio = dataCongViec.getString(3);

            // PROCESS ngayHoanThanh
            String[] arrngayThangNam = strngayHoanThanh.split("/");
            int ngay = Integer.parseInt(arrngayThangNam[0]);
            int thang = Integer.parseInt(arrngayThangNam[1]);
            int nam = Integer.parseInt(arrngayThangNam[2]);
            DateCustom dateCustom = new DateCustom(ngay, thang, nam);
            // PROCESS ALARM
            String[] arrHenGio = strhenGio.split(":");
            int gio = Integer.parseInt(arrHenGio[0]);
            int phut = Integer.parseInt(arrHenGio[1]);
            HenGio henGio = new HenGio(gio, phut);
            // create CongViec
            CongViec congViec = new CongViec(tenCongViec, noiDung, dateCustom, henGio);
            // Add list
            listCongViec.add(congViec);
        }
//        //=========ADD DATA static=======================
//        DateCustom deadline = new DateCustom(12, 3, 2020);
//        HenGio henGio = new HenGio(5,30);
//        CongViec congViec = new CongViec("Làm bài tập java", "làm bài 22 trang 23", deadline, henGio);
//        listCongViec.add(congViec);
//
//        DateCustom deadline2 = new DateCustom(12, 3, 2020);
//        HenGio henGio2 = new HenGio(10,30);
//        CongViec congViec2 = new CongViec("Làm bài tập android", "làm bài 22 trang 23", deadline2, henGio2);
//        listCongViec.add(congViec2);
//        //========================================
        congViecAdapter = new CongViecAdapter(QuanLyCongViec.this, R.layout.dong_cong_viec, listCongViec);
        lv_CongViec.setAdapter(congViecAdapter);

        //ALARM_SERVICE: là thèn tính năng báo thức của hệ thống
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        // khai báo intent để truyền dữ liệu từ QuanLyCongViec sang AlarmReceiver
        intent = new Intent(QuanLyCongViec.this, AlarmReceiver.class);
        // CHECK phát ra cảnh báo DEALINE
        Log.e("Quang Nhu check", "check alarm");
        checkAlarm();
    }
    public void setEvent(){
        // choice alarm
        editText_HenGio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chonGio();
            }
        });
        // choice dealine time
        editText_NgayHoanThanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chonNgay();
            }
        });

        // Delete
        lv_CongViec.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String tenCongViec = listCongViec.get(position).getTenCongViec();
                //update List
                listCongViec.remove(position);
                congViecAdapter.notifyDataSetChanged();
                // Remove database
                database.queryData("DELETE FROM `tblcongviec` WHERE `TenCongViec`='"+tenCongViec+"'");

                Toast.makeText(QuanLyCongViec.this, "Xóa thành công " + tenCongViec, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        //Select
        lv_CongViec.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                vitri = position;
                // đổ dữ liệu vào trong các edit text
                editText_TenCongViec.setText(listCongViec.get(position).getTenCongViec());
                editText_NoiDung.setText(listCongViec.get(position).getNoiDungCongViec());
                editText_NgayHoanThanh.setText(listCongViec.get(position).getNgayHoanThanh().getShow());
                editText_HenGio.setText(listCongViec.get(position).getHenGio().getShow());
            }
        });

        btn_Them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GET DATA
                String tenCongViec = editText_TenCongViec.getText().toString();
                String noiDung = editText_NoiDung.getText().toString();
                //GET DATE
                String[] ngayThangNam = editText_NgayHoanThanh.getText().toString().split("/");
                //GET TIME
                String[] giophut = editText_HenGio.getText().toString().split(":");
                //VALIDATION
                int check = validate(tenCongViec, ngayThangNam, giophut);

                switch (check){
                    case 1: {
                        editText_TenCongViec.setError("Không được trống!");
                        break;
                    }
                    case 2: {
                        editText_NgayHoanThanh.setError("Nhấn đúp để chọn!");
                        break;
                    }
                    case 3:{
                        editText_HenGio.setError("Nhấn đúp để chọn!");
                        break;
                    }
                    default:{// Đã điền đầy đủ ==> ADD vào
                        //delete set Error
                        editText_TenCongViec.setError(null);
                        editText_NgayHoanThanh.setError(null);
                        editText_HenGio.setError(null);
                        // check điều kiện trùng tên(PK) để quyết định xem là sửa hay thêm
                        if(searchByName(tenCongViec, listCongViec) != -1){
                            editText_TenCongViec.setError("Đã trùng tên công việc!");
                        }else{
                            //PROCESS DAY
                            int ngay = Integer.parseInt(ngayThangNam[0]);
                            int thang = Integer.parseInt(ngayThangNam[1]);
                            int nam = Integer.parseInt(ngayThangNam[2]);
                            DateCustom dateCustom = new DateCustom(ngay, thang, nam);
                            // PROCESS ALARM
                            int gio = Integer.parseInt(giophut[0]);
                            int phut = Integer.parseInt(giophut[1]);
                            HenGio henGio = new HenGio(gio, phut);

                            // ADD database
                            database.queryData("INSERT INTO `tblCongViec`(`TenCongViec`, `NoiDung`, `NgayHoanThanh`, `ThoiGianHoanThanh`) VALUES ('"+tenCongViec+"','"+noiDung+"','"+dateCustom.getShow()+"','"+henGio.getShow()+"')");

                            // create new Object
                            CongViec congViec = new CongViec(tenCongViec, noiDung, dateCustom, henGio);
                            //ADD HEAD on LIST
                            listCongViec.add(0, congViec);
                            congViecAdapter.notifyDataSetChanged();
                            Toast.makeText(QuanLyCongViec.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        btn_Sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GET DATA
                String tenCongViec = editText_TenCongViec.getText().toString();
                String noiDung = editText_NoiDung.getText().toString();
                //GET DATE
                String[] ngayThangNam = editText_NgayHoanThanh.getText().toString().split("/");
                // PROCESS ALARM
                String[] giophut = editText_HenGio.getText().toString().split(":");
                //VALIDATION
                int check = validate(tenCongViec, ngayThangNam, giophut);
                switch (check) {
                    case 1: {
                        editText_TenCongViec.setError("Không được trống!");
                        break;
                    }
                    case 2: {
                        editText_NgayHoanThanh.setError("Không được trống!");
                        break;
                    }
                    case 3: {
                        editText_HenGio.setError("Không được trống!");
                        break;
                    }
                    default: {// Đã điền đầy đủ ==> ADD vào
                        //delete set Error
                        editText_TenCongViec.setError(null);
                        editText_NgayHoanThanh.setError(null);
                        editText_HenGio.setError(null);

                        //PROCESS DAY
                        int ngay = Integer.parseInt(ngayThangNam[0]);
                        int thang = Integer.parseInt(ngayThangNam[1]);
                        int nam = Integer.parseInt(ngayThangNam[2]);
                        DateCustom dateCustom = new DateCustom(ngay, thang, nam);
                        //PROCESS TIME
                        int gio = Integer.parseInt(giophut[0]);
                        int phut = Integer.parseInt(giophut[1]);
                        HenGio henGio = new HenGio(gio, phut);
                        // CHECK exits
                        int viTriSua = searchByName(tenCongViec, listCongViec);
                        if(viTriSua == -1){ // not exits ==> ADD
                            // ADD database
                            database.queryData("INSERT INTO `tblCongViec`(`TenCongViec`, `NoiDung`, `NgayHoanThanh`, `ThoiGianHoanThanh`) VALUES ('"+tenCongViec+"','"+noiDung+"','"+dateCustom.getShow()+"','"+henGio.getShow()+"')");

                            // create new Object
                            CongViec congViec = new CongViec(tenCongViec, noiDung, dateCustom, henGio);
                            //ADD LIST
                            listCongViec.add(0, congViec);
                            congViecAdapter.notifyDataSetChanged();
                            Toast.makeText(QuanLyCongViec.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                        }else{// exited
                            //Update on database
                            database.queryData("UPDATE `tblcongviec` SET `TenCongViec`='"+tenCongViec+"',`NoiDung`='"+noiDung+"',`NgayHoanThanh`='"+dateCustom.getShow()+"',`ThoiGianHoanThanh`='"+henGio.getShow()+"' WHERE `TenCongViec`='"+tenCongViec+"'");
                            // create new Object
                            CongViec congViec = new CongViec(tenCongViec, noiDung, dateCustom, henGio);
                            //EDIT element into LIST
                            listCongViec.set(viTriSua, congViec);
                            congViecAdapter.notifyDataSetChanged();
                            Toast.makeText(QuanLyCongViec.this, "Sửa thành công!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }
    public void getControl(){
        editText_TenCongViec = findViewById(R.id.edt_tencongviec);
        editText_NoiDung = findViewById(R.id.edt_noidung);
        editText_NgayHoanThanh = findViewById(R.id.edt_ngayhoanthanh);
        editText_HenGio = findViewById(R.id.edt_hengio);

        btn_Them = findViewById(R.id.btn_them);
        btn_Sua = findViewById(R.id.btn_sua);
        lv_CongViec = findViewById(R.id.lv_congviec);
    }

    // function custom
    public void checkAlarm() {
        //Log.e("Quang Nhu", "check trong hàm alarm");
        for (int i = 0; i < listCongViec.size(); i++) {
            //Log.e("Quang Nhu", "check trong vòng for");
            //B1: GET time current
            //GET DAY
            Calendar calendar = Calendar.getInstance();
            int yearCurrent = calendar.get(Calendar.YEAR);
            int monthCurrent = calendar.get(Calendar.MONTH) + 1;
            int dayCurrent = calendar.get(Calendar.DAY_OF_MONTH);
            //Get time
            int hourCurrent = calendar.get(Calendar.HOUR_OF_DAY);
            int minuteCurrent = calendar.get(Calendar.MINUTE);

            // lấy lịch Hẹn giờ
            // get Date
            String[] ngayThangNam = listCongViec.get(i).getNgayHoanThanh().getShow().split("/");
            //PROCESS DAY
            int ngay = Integer.parseInt(ngayThangNam[0]);
            int thang = Integer.parseInt(ngayThangNam[1]);
            int nam = Integer.parseInt(ngayThangNam[2]);
            //get time
            String[] giophut = listCongViec.get(i).getHenGio().getShow().split(":");
            // PROCESS ALARM
            int gio = Integer.parseInt(giophut[0]);
            int phut = Integer.parseInt(giophut[1]);

            if (nam == yearCurrent && thang == monthCurrent && ngay == dayCurrent) {// tới ngày báo thức
                if (gio == hourCurrent && phut == minuteCurrent) {// PHát nhạc
                    // Lập tức phát âm thanh
                    //Log.e("Quang Nhu", "check phát âm thanh lập tức");
                    phatRaCanhBao();
                }
            }
        }
    }
    private void chonGio(){
        int gio = calendar.get(Calendar.HOUR_OF_DAY);
        int phut = calendar.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                // save data into Calendar
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                // Show result
                editText_HenGio.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
                Toast.makeText(QuanLyCongViec.this, "Ban da dat gio: " + calendar.get(Calendar.HOUR_OF_DAY) + "/ "+ calendar.get(Calendar.MINUTE) +"phut" ,Toast.LENGTH_SHORT).show();
            }
        }, gio, phut, true);
        timePickerDialog.show();
        //delete set Error
        editText_HenGio.setError(null);
    }
    private void chonNgay(){
        int ngay = calendar.get(Calendar.DAY_OF_MONTH);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // save into Calendar
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                editText_NgayHoanThanh.setText(simpleDateFormat.format(calendar.getTime()));

                Toast.makeText(QuanLyCongViec.this, editText_NgayHoanThanh.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        }, nam, thang, ngay);
        datePickerDialog.show();
        //delete set Error
        editText_NgayHoanThanh.setError(null);
    }
    private void phatRaCanhBao(){
        Calendar calendarHienTai = Calendar.getInstance();
        //timePickerDialog.show();
        pendingIntent = PendingIntent.getBroadcast(
                QuanLyCongViec.this,
                0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        //Log.d("calendar time", calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
        // Phát ra âm thanh từ thời điểm hiện tại
        alarmManager.set(AlarmManager.RTC_WAKEUP, 0, pendingIntent);
        //Log.d("Trigger", ""+ calendar.getTimeInMillis());
    }
    public int validate(String tenCongViec, String[] ngayThangNam, String[] time){
        if(tenCongViec.equals("")){
            return 1;
        }
        if(ngayThangNam[0].equals("")){
            return 2;
        }if(time[0].equals("")){
            return 3;
        }
        return 0;
    }
    public int searchByName(String name, ArrayList<CongViec> listCongViec){
        for(int i = 0; i < listCongViec.size(); i++){
            if(listCongViec.get(i).getTenCongViec().equals(name)){
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}

