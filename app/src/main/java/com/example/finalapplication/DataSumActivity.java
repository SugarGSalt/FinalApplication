package com.example.finalapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DataSumActivity extends AppCompatActivity {

    private List<String> dateList=new ArrayList<>();
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_data_sum);

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Calendar calendar=Calendar.getInstance(); // 获取当前日期

        for (int i=0; i<14; i++) {
            String dateStr=sdf.format(calendar.getTime());
            dateList.add(dateStr);
            calendar.add(Calendar.DAY_OF_YEAR, 1); //日期加1
        }

        ArrayAdapter<String> adpter=new ArrayAdapter<String>
                (DataSumActivity.this, android.R.layout.simple_list_item_1,dateList);

        ListView listView=(ListView) findViewById(R.id.data_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDate=dateList.get(position);
                Intent intent=new Intent(DataSumActivity.this, SingleActivity.class);
                intent.putExtra("date", selectedDate);
                startActivity(intent);
            }
        });

        listView.setAdapter(adpter);



    }
}