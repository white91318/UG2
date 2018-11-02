package lab.hwang.ug;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class distance_MainActivity extends Activity {
    private static final String TAG_SUCCESS = "success";
    //變數
    public static String user_id = Login.user;
    public static String person_hight = "", new_version = "";
    public static String person_select_measure_type = "";
    public static String switch_practice_homework = "";
    public static Boolean session_one = false;
    public static String measure_shap = ""; //object的形狀 ex三角形 矩形
    //物件
    Button session1_but_distance, session1_but_hight, session1_but_width, session1_but_distance2, session1_but_hight2, input_person_hight;
    Button rectangle_but, triangle_but, parallel_but, diamond_but, trapezoidal_but, session2_but_see_record;
    Button session3_but_homework, session3_teacher_sent_question, session3_but_map, rank_test, upgrade_but;
    Button basic_session1, mesure_session1;
    TextView person_hight_tv, version;
    //矩形rectangle  三角形triangle  梯形trapezoidal  平行四邊形parallel  菱形diamond
    // Progress Dialog 進度對化框
    JSONParser jsonParser = new JSONParser();
    //class
    private SQLite dbHelper;//操作SQLite資料庫

    public String hight;
    private DatabaseReference mDatabase;

    //判斷AlertDialog input 值
    public static boolean validate_input(String s) {
        return ((50 < Integer.valueOf(s) && 201 > Integer.valueOf(s)) || (1 == Integer.valueOf(s)));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findview();

        //開啟SQLite  取得使用者身高
        GetSqlite_person_ghiht();
        GetSqlite_version();
        confirm_date_but();


        hight=person_hight;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference idRef = mDatabase.child(user_id);
        idRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(hight)){

                }else{
                    idRef.child("height").setValue(hight+" cm");

                }
            }@Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        Log.i("@@@@userid", user_id);

        if (user_id.equals("teacher") || user_id.equals("admin")) {
            session3_teacher_sent_question.setVisibility(View.VISIBLE);
            input_person_hight.setVisibility(View.VISIBLE);
            new_version = "100";
        }

        if (person_hight.equals("1")) {
            AlertDialog.Builder editDialog = new AlertDialog.Builder(distance_MainActivity.this);
            editDialog.setTitle("請輸入你的身高");
            editDialog.setCancelable(false);
            final EditText editText = new EditText(distance_MainActivity.this);
            editDialog.setView(editText);
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    final String temp_personhight;
                    temp_personhight = editText.getText().toString();

                    if (validate_input(temp_personhight)) {
                        SQLite db = new SQLite(distance_MainActivity.this, "personhight");
                        db.update_person_hight(temp_personhight);
                        db.close();

                        person_hight_tv.setText("你的身高:  " + temp_personhight + "(cm)");
                        person_hight = temp_personhight;

                        Intent myservice = new Intent(distance_MainActivity.this, Myservice.class);//??
                        startService(myservice); //??
                        //Log.i("@@@@@@@@@@@@@@@@@@@@@@@", "start service");
                    } else {
                        Toast.makeText(distance_MainActivity.this, "身高輸入格式有誤!", Toast.LENGTH_LONG).show();
                    }



                }
            });
            editDialog.show();
        } else {
            Intent myservice = new Intent(distance_MainActivity.this, Myservice.class);
            startService(myservice);
            //Log.i("@@@@@@@@@@@@@@@@@@@@@@@", "start service");
        }



        //基本量測學習
        basic_session1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(distance_MainActivity.this, next_one_activity1.class);
                startActivity(intent);
                finish();

            }
        });

        //幾何形狀學習
        mesure_session1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setClass(distance_MainActivity.this, decide.class);
                startActivity(intent);
                finish();

            }
        });


        input_person_hight.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder editDialog = new AlertDialog.Builder(distance_MainActivity.this);
                editDialog.setTitle("請輸入你的身高");
                editDialog.setCancelable(true);
                final EditText editText = new EditText(distance_MainActivity.this);
                editDialog.setView(editText);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        //person_hight_tv.setText(editText.getText());

                        String temp_personhight;
                        temp_personhight = editText.getText().toString();

                        if (validate_input(temp_personhight)) {
                            SQLite db = new SQLite(distance_MainActivity.this, "personhight");
                            db.update_person_hight(temp_personhight);
                            db.close();

                            person_hight_tv.setText("你的身高:  " + temp_personhight + "(cm)");
                            person_hight = temp_personhight;
                            dbHelper.close();//關閉資料庫
                        } else {
                            Toast.makeText(distance_MainActivity.this, "身高輸入格式有誤!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                editDialog.show();
            }
        });


//        session3_but_map.setOnClickListener(new Button.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                close_all_but();
//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        open_all_but();
//                    }
//                }, 3000);
//                session_one = false;
//
//                Intent intent = new Intent();
//                intent.setClass(distance_MainActivity.this, com.robert.maps.MainActivity.class);
//                startActivity(intent);
//            }
//        });

        session3_teacher_sent_question.setOnClickListener(new Button.OnClickListener() {
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
                Intent intent = new Intent();
                intent.setClass(distance_MainActivity.this, teacher_sent_question.class);
                startActivity(intent);
            }
        });
        rank_test.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                close_all_but();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        open_all_but();
                    }
                }, 1000);
                session_one = false;
                final Intent intent = new Intent();
                new AlertDialog.Builder(distance_MainActivity.this)//選擇顯示圖示
                        .setTitle("功能")
                        .setItems(new String[]{"我要挑戰", "觀看個人挑戰紀錄", "班級排行榜"},
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichcountry) {
                                        switch (whichcountry) {
                                            case 0:
                                                intent.setClass(distance_MainActivity.this, rank_test.class);
                                                startActivity(intent);
                                                break;
                                            case 1:
                                                dbHelper = new SQLite(distance_MainActivity.this, "record_action");
                                                dbHelper.Insert_Action(Login.MainActivity_page, "觀看自己挑戰紀錄排名");
                                                dbHelper.close();//關閉資料庫

                                                intent.setClass(distance_MainActivity.this, rank_test_record_list.class);
                                                startActivity(intent);
                                                break;
                                            case 2:
                                                try {
                                                    if (isInternetConnected()) {
                                                        new Thread(new Runnable() {
                                                            public void run() {
                                                                try {
                                                                    get_online_data();
                                                                    Thread.sleep(500);
                                                                } catch (InterruptedException e) {
                                                                    // TODO Auto-generated catch block
                                                                    e.printStackTrace();
                                                                }
                                                            }
                                                        }).start();
                                                    }
                                                } catch (Exception e) {

                                                }


                                                dbHelper = new SQLite(distance_MainActivity.this, "record_action");
                                                dbHelper.Insert_Action(Login.MainActivity_page, "觀看全班排名");
                                                dbHelper.close();//關閉資料庫

                                                intent.setClass(distance_MainActivity.this, show_all_stu_rank.class);
                                                startActivity(intent);
                                                break;
                                        }
                                    }
                                }).show();
            }
        });
        upgrade_but.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                close_all_but();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        open_all_but();
                    }
                }, 2000);
                new AlertDialog.Builder(distance_MainActivity.this)
                        .setTitle("警告")
                        .setMessage("確定要更新嗎?")
                        .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final ProgressDialog PDialog = ProgressDialog.show(distance_MainActivity.this, "更新中", "請稍後....", true);
                                new Thread() {
                                    public void run() {
                                        try {
                                            sleep(1000);
                                            if (isInternetConnected()) {
                                                get_new_permission();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        } finally {
                                            PDialog.dismiss();
                                        }
                                    }
                                }.start();
                                GetSqlite_version();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();

            }
        });

    }

    private void findview() {
        session1_but_distance = (Button) findViewById(R.id.session1_but_distance);
        session1_but_hight = (Button) findViewById(R.id.session1_but_hight);
        session1_but_width = (Button) findViewById(R.id.session1_but_width);
        session1_but_distance2 = (Button) findViewById(R.id.session1_but_distance2);
        session1_but_hight2 = (Button) findViewById(R.id.session1_but_hight2);
        input_person_hight = (Button) findViewById(R.id.input_person_hight);
        rectangle_but = (Button) findViewById(R.id.rectangle_but);
        triangle_but = (Button) findViewById(R.id.triangle_but);
        parallel_but = (Button) findViewById(R.id.parallel_but);
        diamond_but = (Button) findViewById(R.id.diamond_but);
        trapezoidal_but = (Button) findViewById(R.id.trapezoidal_but);
        session2_but_see_record = (Button) findViewById(R.id.session2_but_see_record);
        session3_but_map = (Button) findViewById(R.id.session3_but_map);
        session3_but_homework = (Button) findViewById(R.id.session3_but_homework);
        session3_teacher_sent_question = (Button) findViewById(R.id.session3_teacher_sent_question);
        rank_test = (Button) findViewById(R.id.rank_test);
        upgrade_but = (Button) findViewById(R.id.upgrade_but);
        person_hight_tv = (TextView) findViewById(R.id.person_hight);
        version = (TextView) findViewById(R.id.version);
        basic_session1 = (Button) findViewById(R.id.basic_session1_ge);
        mesure_session1 = (Button) findViewById(R.id.mesure_session1_ge);

    }

    private void GetSqlite_person_ghiht() {
        // TODO Auto-generated method stub
        dbHelper = new SQLite(distance_MainActivity.this, "personhight");
        Cursor cursor = dbHelper.getpersonhight("personhight");
        cursor.moveToNext();
        person_hight = cursor.getString(1);
        person_hight_tv.setText("你的身高:  " + person_hight + "(cm)");
        cursor.close();
        dbHelper.close();
    }

    private void GetSqlite_version() {
        // TODO Auto-generated method stub
        dbHelper = new SQLite(distance_MainActivity.this, "user_permission");
        Cursor cursor = dbHelper.getversion("user_permission");
        cursor.moveToNext();
        new_version = cursor.getString(4);
        version.setText("Version: " + new_version + ".1");
        cursor.close();
        dbHelper.close();
    }

    private void show_select_measure_type() {
        final Intent intent = new Intent();
        new AlertDialog.Builder(distance_MainActivity.this)//選擇顯示圖示
                .setTitle("請選擇物件的所在位置")
                .setItems(new String[]{"目標物平躺於地面  例如:磁磚", "目標物底部與地面接觸，垂直於地面  例如:門", "目標物在牆上 例如:窗戶"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichcountry) {
                                switch (whichcountry) {
                                    case 0:
                                        person_select_measure_type = "get_area_ground";
                                        intent.setClass(distance_MainActivity.this, measure_session1.class);
                                        startActivity(intent);
                                        break;
                                    case 1:
                                        person_select_measure_type = "get_area_ground_hight";
                                        intent.setClass(distance_MainActivity.this, measure_session1.class);
                                        startActivity(intent);
                                        break;
                                    case 2:
                                        person_select_measure_type = "get_area_wall";
                                        intent.setClass(distance_MainActivity.this, measure_session1.class);
                                        startActivity(intent);
                                        break;
                                }
                            }
                        }).show();
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
                    if (!(user_id.equals(distance_MainActivity.user_id))) {
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
            upgrade_but.setClickable(true);
        }
        if (verion > 1) {
            rectangle_but.setVisibility(View.VISIBLE);
            triangle_but.setVisibility(View.VISIBLE);
            parallel_but.setVisibility(View.VISIBLE);
            diamond_but.setVisibility(View.VISIBLE);
            trapezoidal_but.setVisibility(View.VISIBLE);
            session2_but_see_record.setVisibility(View.VISIBLE);
            upgrade_but.setClickable(true);
        }
        if (verion > 2) {
            session3_but_map.setVisibility(View.VISIBLE);
            session3_but_homework.setVisibility(View.VISIBLE);
            upgrade_but.setClickable(true);
        }
        if (verion > 4) {
            rank_test.setVisibility(View.VISIBLE);
            upgrade_but.setClickable(true);
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
        rectangle_but.setVisibility(View.INVISIBLE);
        triangle_but.setVisibility(View.INVISIBLE);
        parallel_but.setVisibility(View.INVISIBLE);
        diamond_but.setVisibility(View.INVISIBLE);
        trapezoidal_but.setVisibility(View.INVISIBLE);
        session2_but_see_record.setVisibility(View.INVISIBLE);
        session3_but_map.setVisibility(View.INVISIBLE);
        session3_but_homework.setVisibility(View.INVISIBLE);
        rank_test.setVisibility(View.INVISIBLE);
        upgrade_but.setClickable(false);
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
            new AlertDialog.Builder(distance_MainActivity.this)
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
                            intent.setClass(distance_MainActivity.this, Login.class);
                            startActivity(intent);
                            distance_MainActivity.this.finish();
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
