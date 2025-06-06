package com.example.finalapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class SingleActivity extends AppCompatActivity {

    //控件变量
    private LinearLayout goalContainer; //checkbox
    private EditText inputGoal; //文本输入
    private Button saveGoal, backButton; //保存返回钮
    private TextView welcomeText; //欢迎光临
    private ProgressBar progressBar; //进度条
    private String currentDate = ""; //当前日期
    private int totalGoals = 0; //目标数目
    private GoalDatabaseHelper dbHelper; //数据库
    private static boolean isDbCleared = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);

        Intent intent = getIntent();
        String dateFromIntent = intent.getStringExtra("date");
        if (dateFromIntent != null && !dateFromIntent.isEmpty()) {
            currentDate=dateFromIntent;
        } else {
            currentDate=new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        }

        //获取控件
        inputGoal=findViewById(R.id.input_goal);
        Button saveButton=(Button) findViewById(R.id.save_goal);
        Button backButton=(Button) findViewById(R.id.button_back);
        welcomeText=findViewById(R.id.text_welcome);
        progressBar=findViewById(R.id.progressBar);

        goalContainer=findViewById(R.id.goal_container);


        //数据库
        dbHelper=new GoalDatabaseHelper(this);

        dbHelper = new GoalDatabaseHelper(this);
        


        //返回按钮
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //保存按钮
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String goalText = inputGoal.getText().toString().trim();
                if (!goalText.isEmpty()) {
                    addGoal(goalText);
                    inputGoal.setText("");
                    saveGoals();
                }
            }
        });

        //读取目标
        loadGoals();
    }

    //添加目标
    private void addGoal(String text) {
        addGoal(text, false);//默认不打勾
    }

    //设置打勾
    private void addGoal(String text, boolean isChecked) {
        CheckBox checkBox = new CheckBox(this); //创建新框
        checkBox.setText(text); //设置文字
        checkBox.setTextSize(16);
        checkBox.setChecked(isChecked); //是否打勾
        goalContainer.addView(checkBox); //添加到页面上


        totalGoals++;
        progressBar.setMax(totalGoals); //进度条最大值

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateProgress();
                saveGoals();
            }
        });

        updateProgress(); //添加目标时更新一次进度条
    }

    //更新进度条
    private void updateProgress() {
        int checkedCount=0;
        for (int i=0; i<goalContainer.getChildCount(); i++) {
            View child=goalContainer.getChildAt(i);
            if (child instanceof CheckBox && ((CheckBox) child).isChecked()) {
                checkedCount++;
            }
        }
        progressBar.setProgress(checkedCount); //设置进度条为当前值
    }

    //保存到数据库
    private void saveGoals() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(GoalDatabaseHelper.TB_NAME, GoalDatabaseHelper.DATE + "=?", new String[]{currentDate});

        for (int i = 0; i < goalContainer.getChildCount(); i++) {
            View child = goalContainer.getChildAt(i);
            if (child instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) child;
                String text = checkBox.getText().toString();
                boolean done = checkBox.isChecked();

                ContentValues values = new ContentValues();
                values.put(GoalDatabaseHelper.DATE, currentDate);
                values.put(GoalDatabaseHelper.TEXT, text);
                values.put(GoalDatabaseHelper.DONE, done ? 1 : 0);

                db.insert(GoalDatabaseHelper.TB_NAME, null, values);
            }
        }

        db.close();
    }


    //读取保存目标
    private void loadGoals() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                GoalDatabaseHelper.TB_NAME,
                null,
                GoalDatabaseHelper.DATE + "=?",
                new String[]{currentDate},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            do {
                String text = cursor.getString(cursor.getColumnIndexOrThrow(GoalDatabaseHelper.TEXT));
                int done = cursor.getInt(cursor.getColumnIndexOrThrow(GoalDatabaseHelper.DONE));
                addGoal(text, done == 1);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }



}
