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
import android.widget.ImageButton;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class next_one_activity2p extends Activity {
    private static final String TAG_SUCCESS = "success";
    public static String user_id = Login.user;
    public static String person_hight = "", new_version = "";
    public static String person_select_measure_type = "";
    public static String switch_practice_homework = "";
    public static Boolean session_one = false;
    public static String measure_shap = "";
    Button previous_but;
    ImageButton triangle_pconcept, rectangle_pconcept, trapezoidal_pconcept, diamond_pconcept, parallel_pconcept;
    ImageButton rectangle_pbut, triangle_pbut, parallel_pbut, diamond_pbut, trapezoidal_pbut;
    ImageButton but_see_record, pmakeup_but1, pmakeup_but_see_record1;
    JSONParser jsonParser = new JSONParser();
    private SQLite dbHelper;

    // 判斷AlertDialog input 值
    public static boolean validate_input(String s) {
        return ((50 < Integer.valueOf(s) && 201 > Integer.valueOf(s)) || (1 == Integer
                .valueOf(s)));
    }

    private void GetSqlite_person_ghiht() {
        // TODO Auto-generated method stub
        dbHelper = new SQLite(next_one_activity2p.this, "personhight");
        Cursor cursor = dbHelper.getpersonhight("personhight");

        cursor.moveToNext();
        person_hight = cursor.getString(1);
        cursor.close();
        dbHelper.close();
    }

    private void GetSqlite_version() {
        // TODO Auto-generated method stub
        dbHelper = new SQLite(next_one_activity2p.this, "user_permission");
        Cursor cursor = dbHelper.getversion("user_permission");
        cursor.moveToNext();
        new_version = cursor.getString(4);
        cursor.close();
        dbHelper.close();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.measure_session1_mainp);
        findview();

        // 開啟SQLite 取得使用者身高
        GetSqlite_person_ghiht();
        GetSqlite_version();
//        confirm_date_but();


        previous_but.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(next_one_activity2p.this,
                        decide.class);
                startActivity(intent);
                finish();
            }
        });

        rectangle_pconcept.setOnClickListener(new Button.OnClickListener() {//長方形
            public void onClick(View v) {
//                close_all_but();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        open_all_but();
                    }
                }, 3000);
                session_one = true;
                dbHelper = new SQLite(next_one_activity2p.this,
                        "record_session_one_practice");
                dbHelper.Insert_session_one_practice("rectangle_c");
                dbHelper.close();// 關閉資料庫


                switch_practice_homework = "practice"; // ??
                measure_shap = "rectangle";
                person_select_measure_type = "get_area_ground";
                // person_select_measure_type = "rectangle"; // ??

                Intent intent = new Intent();

                intent.setClass(next_one_activity2p.this,
                        measure_session4p_concept.class);
                startActivity(intent);
            }
        });

        triangle_pconcept.setOnClickListener(new Button.OnClickListener() {//三角形
            public void onClick(View v) {
//                close_all_but();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        open_all_but();
                    }
                }, 3000);
                session_one = true;
                dbHelper = new SQLite(next_one_activity2p.this,
                        "record_session_one_practice");
                dbHelper.Insert_session_one_practice("triangle_c");
                dbHelper.close();// 關閉資料庫

                switch_practice_homework = "practice"; // ??
                measure_shap = "triangle";
                person_select_measure_type = "get_area_ground";
                // person_select_measure_type = "rectangle"; // ??

                Intent intent = new Intent();

                intent.setClass(next_one_activity2p.this,
                        measure_session4p_concept.class);
                startActivity(intent);
            }
        });

        parallel_pconcept.setOnClickListener(new Button.OnClickListener() {//平行四邊形
            public void onClick(View v) {
//                close_all_but();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        open_all_but();
                    }
                }, 3000);
                session_one = true;
                dbHelper = new SQLite(next_one_activity2p.this,
                        "record_session_one_practice");
                dbHelper.Insert_session_one_practice("parallel_c");
                dbHelper.close();// 關閉資料庫

                switch_practice_homework = "practice"; // ??
                measure_shap = "parallel";
                person_select_measure_type = "get_area_ground";
                // person_select_measure_type = "rectangle"; // ??

                Intent intent = new Intent();

                intent.setClass(next_one_activity2p.this,
                        measure_session4p_concept.class);
                startActivity(intent);
            }
        });

        diamond_pconcept.setOnClickListener(new Button.OnClickListener() { //菱形
            public void onClick(View v) {
//                close_all_but();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        open_all_but();
                    }
                }, 3000);
                session_one = true;
                dbHelper = new SQLite(next_one_activity2p.this,
                        "record_session_one_practice");
                dbHelper.Insert_session_one_practice("diamond_c");
                dbHelper.close();// 關閉資料庫

                switch_practice_homework = "practice"; // ??
                measure_shap = "diamond";
                person_select_measure_type = "get_area_ground";
                // person_select_measure_type = "rectangle"; // ??

                Intent intent = new Intent();

                intent.setClass(next_one_activity2p.this,
                        measure_session4p_concept.class);
                startActivity(intent);
            }
        });

        trapezoidal_pconcept.setOnClickListener(new Button.OnClickListener() { //梯形
            public void onClick(View v) {
//                close_all_but();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        open_all_but();
                    }
                }, 3000);
                session_one = true;
                dbHelper = new SQLite(next_one_activity2p.this,
                        "record_session_one_practice");
                dbHelper.Insert_session_one_practice("trapezoidal_c");
                dbHelper.close();// 關閉資料庫

                switch_practice_homework = "practice"; // ??
                measure_shap = "trapezoidal";
                person_select_measure_type = "get_area_ground";
                // person_select_measure_type = "rectangle"; // ??

                Intent intent = new Intent();

                intent.setClass(next_one_activity2p.this,
                        measure_session4p_concept.class);
                startActivity(intent);
            }
        });


        rectangle_pbut.setOnClickListener(new Button.OnClickListener() {//長方形
            @Override
            public void onClick(View v) {
//                close_all_but();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        open_all_but();
                    }
                }, 1000);
                session_one = false;

                switch_practice_homework = "practice";
                measure_shap = "rectangle";
                show_select_measure_type();
            }
        });
        triangle_pbut.setOnClickListener(new Button.OnClickListener() {//三角形
            @Override
            public void onClick(View v) { //三角形周長按鈕
              //  close_all_but();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        open_all_but();
                    }
                }, 1000);
                session_one = false;

                switch_practice_homework = "practice";
                measure_shap = "triangle";

                Intent intent = new Intent();
                intent.setClass(next_one_activity2p.this, measure_session_t.class);
                startActivity(intent);

            }
        });
        parallel_pbut.setOnClickListener(new Button.OnClickListener() {//平行四邊形
            @Override
            public void onClick(View v) {
             //   close_all_but();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        open_all_but();
                    }
                }, 1000);
                session_one = false;

                switch_practice_homework = "practice";
                measure_shap = "parallel";

                Intent intent = new Intent();
                intent.setClass(next_one_activity2p.this, measure_session_t.class);
                startActivity(intent);
            }
        });
        diamond_pbut.setOnClickListener(new Button.OnClickListener() { //菱形
            @Override
            public void onClick(View v) {
            //    close_all_but();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        open_all_but();
                    }
                }, 1000);
                session_one = false;

                switch_practice_homework = "practice";
                measure_shap = "diamond";
                show_select_measure_type();
            }
        });
        trapezoidal_pbut.setOnClickListener(new Button.OnClickListener() {//梯形
            @Override
            public void onClick(View v) {  //梯形周長按鈕
           //     close_all_but();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        open_all_but();
                    }
                }, 1000);
                session_one = false;

                switch_practice_homework = "practice";
                measure_shap = "trapezoidal";
                Intent intent = new Intent();
                intent.setClass(next_one_activity2p.this, measure_session_tr.class);
                startActivity(intent);
            }
        });

        but_see_record.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    //    close_all_but();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                open_all_but();
                            }
                        }, 3000);

                        dbHelper = new SQLite(next_one_activity2p.this,
                                "see_practice_record");
                        dbHelper.see_record("see_practice");
                        dbHelper.close();// 關閉資料庫

                        Intent intent = new Intent();
                        intent.setClass(next_one_activity2p.this,
                                practice_precord.class);
                        startActivity(intent);
                    }
                });

        pmakeup_but1.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        open_all_but();
                    }
                }, 3000);
                session_one = true;
                dbHelper = new SQLite(next_one_activity2p.this,
                        "record_session_one_practice");
                dbHelper.Insert_session_one_practice("makeup");
                dbHelper.close();// 關閉資料庫

                switch_practice_homework = "practice"; // ??
                measure_shap = "rectangle";
                person_select_measure_type = "get_area_ground";


                Intent intent = new Intent();

                intent.setClass(next_one_activity2p.this,
                        measure_session4p_makeup.class);
                startActivity(intent);


            }
        });
        pmakeup_but_see_record1
                .setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                open_all_but();
                            }
                        }, 3000);

                        dbHelper = new SQLite(next_one_activity2p.this,
                                "see_practice_record");
                        dbHelper.see_record("see_practice");
                        dbHelper.close();// 關閉資料庫

                        Intent intent = new Intent();
                        intent.setClass(next_one_activity2p.this,
                                practice_record_m.class);
                        startActivity(intent);
                    }
                });




    }


    private void findview() {
        // TODO Auto-generated method stub

        previous_but = (Button) findViewById(R.id.previous_but_d1);
        // 基本概念
        rectangle_pconcept = (ImageButton) findViewById(R.id.rectangle_pconcept_but);
        triangle_pconcept = (ImageButton) findViewById(R.id.triangle_pconcept_but);
        parallel_pconcept = (ImageButton) findViewById(R.id.parallel_pconcept_but);
        diamond_pconcept = (ImageButton) findViewById(R.id.diamond_pconcept_but);
        trapezoidal_pconcept = (ImageButton) findViewById(R.id.trapezoidal_pconcept_but);

        //周長計算
        rectangle_pbut = (ImageButton) findViewById(R.id.rectangle_pbut1);
        triangle_pbut = (ImageButton) findViewById(R.id.triangle_pbut1);
        parallel_pbut = (ImageButton) findViewById(R.id.parallel_pbut1);
        diamond_pbut = (ImageButton) findViewById(R.id.diamond_pbut1);
        trapezoidal_pbut = (ImageButton) findViewById(R.id.trapezoidal_pbut1);
        but_see_record = (ImageButton) findViewById(R.id.but_see_record);

//        person_hight_tv = (TextView) findViewById(R.id.person_hight);
//        version = (TextView) findViewById(R.id.version1);

        // 形狀組合
        pmakeup_but1 = (ImageButton) findViewById(R.id.pmakeup_but1);
        pmakeup_but_see_record1 = (ImageButton) findViewById(R.id.pmakeup_but_see_record1);
    }

    private void show_select_measure_type() {
        final Intent intent = new Intent();
        new AlertDialog.Builder(next_one_activity2p.this)
                // 選擇顯示圖示
                .setTitle("請選擇物件的所在位置")
                .setItems(
                        new String[]{"目標物平躺於地面  例如:磁磚",
                                "目標物底部與地面接觸，垂直於地面  例如:門", "目標物在牆上 例如:窗戶"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichcountry) {
                                switch (whichcountry) {
                                    case 0:
                                        person_select_measure_type = "get_area_ground";
                                        intent.setClass(next_one_activity2p.this,
                                                measure_session2p.class);
                                        startActivity(intent);
                                        break;
                                    case 1:
                                        person_select_measure_type = "get_area_ground_hight";
                                        intent.setClass(next_one_activity2p.this,
                                                measure_session2p.class);
                                        startActivity(intent);
                                        break;
                                    case 2:
                                        person_select_measure_type = "get_area_wall";
                                        intent.setClass(next_one_activity2p.this,
                                                measure_session2p.class);
                                        startActivity(intent);
                                        break;
                                }
                            }
                        }).show();
    }
    /*private void show_select_measure_type2() {
        final Intent intent = new Intent();
        new AlertDialog.Builder(next_one_activity2p.this)
                // 選擇顯示圖示
                .setTitle("請選擇物件的所在位置")
                .setItems(
                        new String[]{"目標物平躺於地面  例如:磁磚",
                                "目標物底部與地面接觸，垂直於地面  例如:門", "目標物在牆上 例如:窗戶"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichcountry) {
                                switch (whichcountry) {
                                    case 0:
                                        person_select_measure_type = "get_area_ground";
                                        intent.setClass(next_one_activity2p.this, measure_session_t.class);
                                        startActivity(intent);
                                        break;
                                    case 1:
                                        person_select_measure_type = "get_area_ground_hight";
                                        intent.setClass(next_one_activity2p.this, measure_session_t.class);
                                        startActivity(intent);
                                        break;
                                    case 2:
                                        person_select_measure_type = "get_area_wall";
                                        intent.setClass(next_one_activity2p.this, measure_session_t.class);
                                        startActivity(intent);
                                        break;
                                }
                            }
                        }).show();
    }*/


    private void get_online_data() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tablename", "download_rank_allstu"));
        String TAG_PRODUCTS = "download_content";
        JSONArray d_table = null;
        // getting JSON string from URL
        JSONObject json = jsonParser.makeHttpRequest(Myservice.url_download,
                "GET", params);
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
                    if (!(user_id.equals(next_one_activity2.user_id))) {
                        SQLite db = new SQLite(this, "rank_allstu");
                        db.update_all_stu_rank(user_id, total_num, right_num,
                                right_rate, Date);
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

//    private void confirm_date_but() {
//        Integer verion = Integer.valueOf(new_version);
//        // version 1>> session_1 on
//        // version 2>> session_2 on
//        // version 3>> homework and map on (students can't add poi)
//        // version 4>> map (students can add poi)
//        // version 5>> competition on
//        if (verion > 0) {
//            makeup_but.setVisibility(View.VISIBLE);
//            rectangle_concept.setVisibility(View.VISIBLE);
//            // session1_but_distance.setVisibility(View.VISIBLE);
//            // session1_but_hight.setVisibility(View.VISIBLE);
//            // session1_but_width.setVisibility(View.VISIBLE);
//            // session1_but_distance2.setVisibility(View.VISIBLE);
//            // session1_but_hight2.setVisibility(View.VISIBLE);
//            // upgrade_but.setClickable(true);
//        }
//        if (verion > 1) {
//            rectangle_but.setVisibility(View.VISIBLE);
//            triangle_but.setVisibility(View.VISIBLE);
//            parallel_but.setVisibility(View.VISIBLE);
//            diamond_but.setVisibility(View.VISIBLE);
//            trapezoidal_but.setVisibility(View.VISIBLE);
//            session2_but_see_record.setVisibility(View.VISIBLE);
//
//            // upgrade_but.setClickable(true);
//        }
//
//    }

    private boolean isInternetConnected() {

        // get ConnectivityManager instance
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        // get network info
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        // check network is connecting
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }

    }

    //    private void close_all_but() {
//
//        previous_but.setVisibility(View.INVISIBLE);
//        rectangle_pconcept.setVisibility(View.INVISIBLE);
//        triangle_pconcept.setVisibility(View.INVISIBLE);
//        parallel_pconcept.setVisibility(View.INVISIBLE);
//        diamond_pconcept.setVisibility(View.INVISIBLE);
//        triangle_pconcept.setVisibility(View.INVISIBLE);
////        rectangle_but.setVisibility(View.INVISIBLE);
////        triangle_but.setVisibility(View.INVISIBLE);
////        parallel_but.setVisibility(View.INVISIBLE);
////        diamond_but.setVisibility(View.INVISIBLE);
////        trapezoidal_but.setVisibility(View.INVISIBLE);
////        session2_but_see_record.setVisibility(View.INVISIBLE);
////        makeup_but.setVisibility(View.INVISIBLE);
////        rectangle_concept.setVisibility(View.INVISIBLE);
//
//    }
//
    private void open_all_but() {
//        confirm_date_but();
    }

    private void get_new_permission() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tablename",
                "download_stu_permission"));
        String TAG_PRODUCTS = "download_content";
        JSONArray d_table = null;
        // getting JSON string from URL
        JSONObject json = jsonParser.makeHttpRequest(Myservice.url_download,
                "GET", params);
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
            new AlertDialog.Builder(next_one_activity2p.this)
                    .setTitle("警告")
                    .setMessage("確定要登出嗎?")
                    .setPositiveButton("確認",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {


                                    Intent intent = new Intent();
                                    intent.setClass(next_one_activity2p.this,
                                            Login.class);
                                    startActivity(intent);
                                    next_one_activity2p.this.finish();
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                }
                            }).show();
            return true;
        }
        return false;
    }

}
