package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private static final String DB_NAME = "DBAPPDIARY";
    private static final int DB_VERSION = 1;
    private static String TABLE_NAME = "Root";
    private static SQLiteDatabase db;
    protected static sqlDBHelper DBHelper;
    public DatePickerDialog datePickerDialog;
    private Button dateButton;
    TextView weatherUI,moodUI,diaryUI;
    String _date, todayDate;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getTodayDate(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month+1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        todayDate =makeDateString(day,month,year);
        return makeDateString(day,month,year);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                _date = makeDateString(day,month,year);
                dateButton.setText(_date);
            }

        });
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this,style,dateSetListener,year,month,day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }
    private String makeDateString(int day, int month, int year){
        return getMonthFormat(month)+" "+day+ " "+year;
    }
    private String getMonthFormat(int month){
        if(month==1)
            return"JAN";
        if(month==2)
            return"FEB";
        if(month==3)
            return"MAR";
        if(month==4)
            return"APR";
        if(month==5)
            return"MAY";
        if(month==6)
            return"JUN";
        if(month==7)
            return"JUL";
        if(month==8)
            return"AUG";
        if(month==9)
            return"SEP";
        if(month==10)
            return"OCT";
        if(month==11)
            return"NOV";
        if(month==12)
            return"DEC";
        return "JAN";
    }
    public void openDatePicker(View v){
        datePickerDialog.show();

    }
    private void setData() {
        //初始化資料庫
        DBHelper.checkTable();
        weatherUI = (TextView) findViewById(R.id.textWeather);
        moodUI = (TextView) findViewById(R.id.textMood);
        diaryUI = (TextView) findViewById(R.id.textDiary);
        String SweatherUI = weatherUI.getText().toString();
        String SmoodUI = moodUI.getText().toString();
        String SdiaryUI = diaryUI.getText().toString();
        DBHelper.addData(_date, SweatherUI, SmoodUI, SdiaryUI);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void in(View v) {
        setContentView(R.layout.detail);
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodayDate());
        initDatePicker();
        weatherUI = (TextView) findViewById(R.id.textWeather);
        moodUI = (TextView) findViewById(R.id.textMood);
        diaryUI = (TextView) findViewById(R.id.textDiary);
        DBHelper = new sqlDBHelper(this);
        //DBHelper.checkTable();
    }
    public void back(View v) {
        String SweatherUI = weatherUI.getText().toString();
        String SmoodUI = moodUI.getText().toString();
        String SdiaryUI = diaryUI.getText().toString();
        if (_date==null)
            _date = todayDate;
        Boolean checkinsertdata = DBHelper.addData(_date, SweatherUI, SmoodUI, SdiaryUI);
        if(checkinsertdata)
            Toast.makeText(MainActivity.this,"Successful" , Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(MainActivity.this,"Failed" , Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_main);
    }
}