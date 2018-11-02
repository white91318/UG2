package lab.hwang.ug;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class next_one_activity1 extends Activity {
    private SQLite dbHelper;
    Button session1_but_distance, session1_but_hight, session1_but_width, session1_but_distance2, session1_but_hight2, session1_but_slash;
    Button previous_but;
    Button rectangle_but, triangle_but, parallel_but, diamond_but, trapezoidal_but, session2_but_see_record;
    TextView person_hight_tv, version;


    public static String user_id = Login.user;
    public static String person_hight = "", new_version = "";
    public static String person_select_measure_type = "";
    public static String switch_practice_homework = "";
    public static Boolean session_one = false;
    public static String measure_shap = "";
    private DatabaseReference mDatabase;


    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basic_session1_main);
        findview();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //開啟SQLite  取得使用者身高
        GetSqlite_person_ghiht();
        GetSqlite_version();
        confirm_date_but();

        previous_but.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(next_one_activity1.this, distance_MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        session1_but_distance.setOnClickListener(new Button.OnClickListener() //量測距離
        {
            @Override
            public void onClick(View v) {
                close_all_but();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        open_all_but();
                    }
                }, 3000);
                session_one = true;
                dbHelper = new SQLite(next_one_activity1.this, "record_session_one_practice");
                dbHelper.Insert_session_one_practice("distance");
                dbHelper.close();//關閉資料庫
                mDatabase.child(user_id).child("PreviewLearning").child("distance_measure").push().setValue("times");
                switch_practice_homework = "practice"; //??
                person_select_measure_type = "distance"; //??
                Intent intent = new Intent();
                intent.setClass(next_one_activity1.this, measure_session3.class);
                startActivity(intent);
            }
        });
        session1_but_hight.setOnClickListener(new Button.OnClickListener() //量測高度
        {
            @Override
            public void onClick(View v) {
                close_all_but();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        open_all_but();
                    }
                }, 3000);
                session_one = true;
                dbHelper = new SQLite(next_one_activity1.this, "record_session_one_practice");
                dbHelper.Insert_session_one_practice("hight");
                dbHelper.close();//關閉資料庫
                mDatabase.child(user_id).child("PreviewLearning").child("hight_measure").push().setValue("times");

	  				/*
                    dbHelper = new SQLite(next_one_activity1.this, "record_action");
	  				dbHelper.Insert_Action(login.MainActivity_page,"To_session_1_hight");
	  				dbHelper.close();//關閉資料庫
	  				*/
                switch_practice_homework = "practice";
                person_select_measure_type = "hight";
                Intent intent = new Intent();
                intent.setClass(next_one_activity1.this, measure_session3.class);
                startActivity(intent);
            }
        });
        session1_but_width.setOnClickListener(new Button.OnClickListener() //量測寬度
        {
            @Override
            public void onClick(View v) {
                close_all_but();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        open_all_but();
                    }
                }, 3000);
                session_one = true;
                dbHelper = new SQLite(next_one_activity1.this, "record_session_one_practice");
                dbHelper.Insert_session_one_practice("width");
                dbHelper.close();//關閉資料庫
                mDatabase.child(user_id).child("PreviewLearning").child("width_measure").push().setValue("times");

	        		/*
	        		dbHelper = new SQLite(next_one_activity1.this, "record_action");
	  				dbHelper.Insert_Action(login.MainActivity_page,"To_session_1_width");
	  				dbHelper.close();//關閉資料庫
	  				*/
                switch_practice_homework = "practice";
                person_select_measure_type = "width";
                Intent intent = new Intent();
                intent.setClass(next_one_activity1.this, measure_session3.class);
                startActivity(intent);
            }
        });
        session1_but_distance2.setOnClickListener(new Button.OnClickListener() //量測距離間距
        {
            @Override
            public void onClick(View v) {
                close_all_but();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        open_all_but();
                    }
                }, 3000);
                session_one = true;
                dbHelper = new SQLite(next_one_activity1.this, "record_session_one_practice");
                dbHelper.Insert_session_one_practice("distance2");
                dbHelper.close();//關閉資料庫
                mDatabase.child(user_id).child("PreviewLearning").child("spacing_distance_measure").push().setValue("times");

 				/*
       		dbHelper = new SQLite(next_one_activity1.this, "record_action");
 				dbHelper.Insert_Action(login.MainActivity_page,"To_session_1_distance2");
 				dbHelper.close();//關閉資料庫
 				*/
                switch_practice_homework = "practice";
                person_select_measure_type = "distance_sky";
                Intent intent = new Intent();
                intent.setClass(next_one_activity1.this, measure_session3.class);
                startActivity(intent);
            }
        });
        session1_but_hight2.setOnClickListener(new Button.OnClickListener() //量測高度間距
        {
            @Override
            public void onClick(View v) {
                close_all_but();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        open_all_but();
                    }
                }, 3000);
                session_one = true;
                dbHelper = new SQLite(next_one_activity1.this, "record_session_one_practice");
                dbHelper.Insert_session_one_practice("hight2");
                dbHelper.close();//關閉資料庫
                mDatabase.child(user_id).child("PreviewLearning").child("spacing_hight_measure").push().setValue("times");
                switch_practice_homework = "practice";
                person_select_measure_type = "hight_sky";
                Intent intent = new Intent();
                intent.setClass(next_one_activity1.this, measure_session3.class);
                startActivity(intent);
            }
        });

        session1_but_slash.setOnClickListener(new Button.OnClickListener() //斜邊
        {
            @Override
            public void onClick(View v) {
                close_all_but();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        open_all_but();
                    }
                }, 3000);
                session_one = true;
                dbHelper = new SQLite(next_one_activity1.this, "record_session_one_practice");
                dbHelper.Insert_session_one_practice("slash");
                dbHelper.close();//關閉資料庫
                mDatabase.child(user_id).child("PreviewLearning").child("slash_measure").push().setValue("times");
                switch_practice_homework = "practice";
                person_select_measure_type = "slash";
                Intent intent = new Intent();
                intent.setClass(next_one_activity1.this, measure_session3.class);
                startActivity(intent);
            }
        });


    }

    private void findview() {
        // TODO Auto-generated method stub
        session1_but_distance = (Button) findViewById(R.id.session1_but_distance1);
        session1_but_hight = (Button) findViewById(R.id.session1_but_hight1);
        session1_but_width = (Button) findViewById(R.id.session1_but_width1);
        session1_but_distance2 = (Button) findViewById(R.id.session1_but_distance3);
        session1_but_hight2 = (Button) findViewById(R.id.session1_but_hight3);
        session1_but_slash = (Button) findViewById(R.id.session1_but_slash);
        previous_but = (Button) findViewById(R.id.previous_but_one);
        person_hight_tv = (TextView) findViewById(R.id.person_hight);
        version = (TextView) findViewById(R.id.version2);

    }

    //判斷AlertDialog input 值
    public static boolean validate_input(String s) {
        return ((50 < Integer.valueOf(s) && 201 > Integer.valueOf(s)) || (1 == Integer.valueOf(s)));
    }

    private void GetSqlite_person_ghiht() {
        // TODO Auto-generated method stub
        dbHelper = new SQLite(next_one_activity1.this, "personhight");
        Cursor cursor = dbHelper.getpersonhight("personhight");

        cursor.moveToNext();
        person_hight = cursor.getString(1);
        person_hight_tv.setText("你的身高:  " + person_hight + "(cm)");
        cursor.close();
        dbHelper.close();
    }

    private void GetSqlite_version() {
        // TODO Auto-generated method stub
        dbHelper = new SQLite(next_one_activity1.this, "user_permission");
        Cursor cursor = dbHelper.getversion("user_permission");
        cursor.moveToNext();
        new_version = cursor.getString(4);
        version.setText("Version: " + new_version + ".1");
        cursor.close();
        dbHelper.close();
    }

    private void get_online_data() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tablename", "download_rank_allstu"));
        String TAG_PRODUCTS = "download_content";
        JSONArray d_table = null;
        // getting JSON string from URL
        JSONObject json = jsonParser.makeHttpRequest(Myservice.url_download, "GET", params);
        try {
            // Checking for SUCCESS TAG
            int success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // products found
                // Getting Array of Products
                d_table = json.getJSONArray(TAG_PRODUCTS);

                // looping through All Products
                for (int i = 0; i < d_table.length(); i++) {
                    JSONObject c = d_table.getJSONObject(i);
                    // Storing each json item in variable
                    String user_id = c.getString("user_id");
                    String total_num = c.getString("total_num");
                    String right_num = c.getString("right_num");
                    String right_rate = c.getString("right_rate");
                    String Date = c.getString("Date");
                    if (!(user_id.equals(next_one_activity1.user_id))) {
                        SQLite db = new SQLite(this, "rank_allstu");
                        db.update_all_stu_rank(user_id, total_num, right_num, right_rate, Date);
                        db.close();
                    }
                }
            } else {
                // no products found
                // Launch Add New product Activity
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void confirm_date_but() {
        Integer verion = Integer.valueOf(new_version);
        //version 1>> session_1 on
        //version 2>> session_2 on
        //version 3>> homework and map on (students can't add poi)
        //version 4>> map (students can add poi)
        //version 5>> competition on
        if (verion > 0) {

            session1_but_distance.setVisibility(View.VISIBLE);
            session1_but_hight.setVisibility(View.VISIBLE);
            session1_but_width.setVisibility(View.VISIBLE);
            session1_but_distance2.setVisibility(View.VISIBLE);
            session1_but_hight2.setVisibility(View.VISIBLE);
            //upgrade_but.setClickable(true);
        }

    }

    private boolean isInternetConnected() {

        //get ConnectivityManager instance
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        //get network info
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        //check network is connecting
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }

    }

    private void close_all_but() {
        session1_but_distance.setVisibility(View.INVISIBLE);
        session1_but_hight.setVisibility(View.INVISIBLE);
        session1_but_width.setVisibility(View.INVISIBLE);
        session1_but_distance2.setVisibility(View.INVISIBLE);
        session1_but_hight2.setVisibility(View.INVISIBLE);

    }

    private void open_all_but() {
        confirm_date_but();
    }

    private void get_new_permission() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tablename", "download_stu_permission"));
        String TAG_PRODUCTS = "download_content";
        JSONArray d_table = null;
        // getting JSON string from URL
        JSONObject json = jsonParser.makeHttpRequest(Myservice.url_download, "GET", params);
        try {
            // Checking for SUCCESS TAG
            int success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // products found
                // Getting Array of Products
                d_table = json.getJSONArray(TAG_PRODUCTS);

                // looping through All Products
                for (int i = 0; i < d_table.length(); i++) {
                    JSONObject c = d_table.getJSONObject(i);
                    // Storing each json item in variable
                    String permission = c.getString("permission");


                    SQLite db = new SQLite(this, "user_permission");
                    db.update_version(permission);
                    db.close();

                }
            } else {
                // no products found
                // Launch Add New product Activity
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(next_one_activity1.this)
                    .setTitle("警告")
                    .setMessage("確定要登出嗎?")
                    .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
		   	            	 /*
		   	            	dbHelper = new SQLite(distance_MainActivity.this, "record_action");
			  				dbHelper.Insert_Action(login.MainActivity_page,"login_out");
			  				dbHelper.close();//關閉資料庫
							*/

                            Intent intent = new Intent();
                            intent.setClass(next_one_activity1.this, Login.class);
                            startActivity(intent);
                            next_one_activity1.this.finish();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
            return true;
        }
        return false;
    }

}

