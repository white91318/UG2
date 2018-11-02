package lab.hwang.ug;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Myservice extends IntentService {
    private SQLite dbHelper;//操作SQLite資料庫
    // Progress Dialog
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";
    public static String server_ip = "http://140.115.126.84/";

    // url 上傳路徑
    public static String url_Insert = server_ip+"android_connect/insert.php";
    // url 下載路徑
    public static String url_download = server_ip+"android_connect/download.php";
    // url 修改rank
    public static String url_modify_rank = server_ip+"android_connect/insert_allstu_rank.php";
    // 圖片路徑
    public static String imagefile = "/sdcard/file_data/";	//??
    // 錄音路徑
    public static String audiofile = "/sdcard/file_data/audio/"; //??
    // 上傳檔案php
    public static String url_uploadfile_php = server_ip+"upload.php";
    // 上傳檔案sqlite php
    public static String url_uploadsqlite_php = server_ip+"upload_sqlite.php";
    // 下載檔案file
    public static String url_file_download =server_ip+"uploads/"; //在uploads資料夾下載檔案

    Cursor cursor;
    int rows_num;
    int count_time=0;
    //建構值
    public Myservice() {
        super("first");
    }

    public Myservice(String name) {
        super(name);
        // TODO Auto-generated constructor stub
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub
        while(true)
        {
            try {
                Thread.sleep(300*1000);
                //IF(連線中斷即停止上傳下載)
                if(isInternetConnected()){
                    Log.i("@service@", "service_on");
                    Insert_homework_question_list();
                    downlaod_homework_question_list();
                    Insert_homework_data();
                    downlaod_Insert_homework_data();
                    Insert_homework_feedback();
                    downlaod_homework_feedback();
                    Insert_mark_record();
                    downlaod_mark_record();

//                    Insert_rmap_poi();
//                    download_rmap_poi();
//
//
//                    Insert_rmap_poi_comment();
//                    download_rmap_poi_comment();
//                    update_ramp_hiden(); //刪除rmap mark point


                    Insert_practice_seesion_one();
                    Insert_rank_record_list();
                    Insert_rank_record_detail();
                    Insert_practice_record();
                    Insert_practice_record_m();
                    //Insert_practice_record_mtotal();


                    get_online_data();
                    upload_rank();

                    upload_user_update_time();

                    count_time++;
                    Log.i("count_time", String.valueOf(count_time));
                    if(count_time%15==0)
                    {
                        Log.i("upload", String.valueOf(count_time));
                        doFileUpload_sqlite();
                        count_time=0;
                    }
                }
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    //上傳session_one_practice
    public void Insert_practice_seesion_one()
    {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        cursor=null;
        cursor = GetCursor("record_session_one_practice");
        // TODO Auto-generated method stub
        if(cursor !=null){
            int i = 0;
            while(cursor.moveToNext()){
                params.add(new BasicNameValuePair("tableName", "record_session_one_practice"));
                params.add(new BasicNameValuePair("user_id", distance_MainActivity.user_id));
                params.add(new BasicNameValuePair("type", cursor.getString(1)));
                params.add(new BasicNameValuePair("Date", cursor.getString(3)));


                JSONObject json = jsonParser.makeHttpRequest(url_Insert,"POST", params);
                try {
                    // check log cat fro response
                    Log.i("Create Response", json.toString());

                    // Checking for SUCCESS TAG
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        //修改狀態
                        SQLite db = new SQLite(this,"record_session_one_practice");
                        db.update(cursor.getString(3),"record_session_one_practice");
                        db.refresh_user_updatetime("upload");
                        db.close();

                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                i=i+1;
            };
            cursor.close();
        }
    }
    //下載downlaod_mark_record資料
    public void downlaod_mark_record()
    {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tablename", "download_mark_record"));
        params.add(new BasicNameValuePair("User_id", distance_MainActivity.user_id));
        String TAG_PRODUCTS = "download_content";
        JSONArray d_table = null;
        // getting JSON string from URL
        JSONObject json = jsonParser.makeHttpRequest(url_download, "GET", params);

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
                    String question_id = c.getString("question_id");
                    String user_answer = c.getString("user_answer");
                    String mark_user = c.getString("mark_user");
                    String user_id = c.getString("user_id");
                    String correct = c.getString("correct");
                    String Date = c.getString("Date");



                    //把線上檔案下載到sqlite
                    dbHelper = new SQLite(Myservice.this, "others_marks_homework_record");
                    long no = dbHelper.Download_others_marks_record(question_id,
                            user_answer,
                            mark_user,
                            user_id,
                            correct,
                            Date);
                    dbHelper.close();//關閉資料庫
                    if(no==-1)
                        Log.i("localdb", "error");
                    else{
                        Log.i("localdb", "ok");
                        SQLite db = new SQLite(this,"record_session_one_practice");
                        db.refresh_user_updatetime("download");
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
    //上傳Mark
    public void Insert_mark_record()
    {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        cursor=null;
        cursor = GetCursor("mark_others_homework");
        // TODO Auto-generated method stub
        if(cursor !=null){
            int i = 0;
            while(cursor.moveToNext()){
                params.add(new BasicNameValuePair("tableName", "mark_others_homework"));
                params.add(new BasicNameValuePair("question_id", cursor.getString(1)));
                params.add(new BasicNameValuePair("user_answer", cursor.getString(2)));
                params.add(new BasicNameValuePair("mark_user", cursor.getString(3)));
                params.add(new BasicNameValuePair("user_id", cursor.getString(4)));
                params.add(new BasicNameValuePair("correct", cursor.getString(5)));
                params.add(new BasicNameValuePair("Date", cursor.getString(7)));


                JSONObject json = jsonParser.makeHttpRequest(url_Insert,"POST", params);
                try {
                    // check log cat fro response
                    Log.i("Create Response", json.toString());

                    // Checking for SUCCESS TAG
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        //修改狀態
                        SQLite db = new SQLite(this,"mark_others_homework");
                        db.update(cursor.getString(7),"mark_others_homework");
                        db.refresh_user_updatetime("upload");
                        db.close();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                i=i+1;
            };
            cursor.close();
        }
    }
    //上傳Insert_practice_record
    public void Insert_rank_record_detail()
    {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        cursor=null;
        cursor = GetCursor("rank_record");
        // TODO Auto-generated method stub
        if(cursor !=null){
            int i = 0;
            while(cursor.moveToNext()){
                params.add(new BasicNameValuePair("tableName", "rank_record"));
                params.add(new BasicNameValuePair("user_id", cursor.getString(1)));
                params.add(new BasicNameValuePair("key_date", cursor.getString(2)));
                params.add(new BasicNameValuePair("value_1", cursor.getString(3)));
                params.add(new BasicNameValuePair("operand1", cursor.getString(4)));
                params.add(new BasicNameValuePair("value_2", cursor.getString(5)));
                params.add(new BasicNameValuePair("operand2", cursor.getString(6)));
                params.add(new BasicNameValuePair("value_3", cursor.getString(7)));
                params.add(new BasicNameValuePair("operand3", cursor.getString(8)));
                params.add(new BasicNameValuePair("value_4", cursor.getString(9)));
                params.add(new BasicNameValuePair("ans", cursor.getString(10)));
                params.add(new BasicNameValuePair("object_pic", cursor.getString(11)));
                doFileUpload(imagefile+cursor.getString(11));
                params.add(new BasicNameValuePair("user_calculate_pic", cursor.getString(12)));
                doFileUpload(imagefile+cursor.getString(12));
                doFileUpload(imagefile+cursor.getString(12).replace("_process.png","_process2.png"));
                doFileUpload(imagefile+cursor.getString(12).replace("_process.png","_process3.png"));
                params.add(new BasicNameValuePair("shape", cursor.getString(13)));
                params.add(new BasicNameValuePair("Date", cursor.getString(15)));
                params.add(new BasicNameValuePair("object_hight", cursor.getString(16)));
                params.add(new BasicNameValuePair("object_width", cursor.getString(17)));
                params.add(new BasicNameValuePair("object_top_width", cursor.getString(18)));
                params.add(new BasicNameValuePair("correct", cursor.getString(19)));
                params.add(new BasicNameValuePair("select_formula", cursor.getString(20)));

                // getting JSON Object
                // Note that create product url accepts POST method
                JSONObject json = jsonParser.makeHttpRequest(url_Insert,"POST", params);
                try {
                    // check log cat fro response

                    // Checking for SUCCESS TAG
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        //修改狀態
                        SQLite db = new SQLite(this,"rank_record");
                        db.update(cursor.getString(15),"rank_record");
                        db.refresh_user_updatetime("upload");
                        db.close();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                i=i+1;
            };
            cursor.close();
        }
    }
    //上傳Insert_rank_record_list
    public void Insert_rank_record_list()
    {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        cursor=null;
        cursor = GetCursor("rank_record_list");
        // TODO Auto-generated method stub
        if(cursor !=null){
            int i = 0;
            while(cursor.moveToNext()){
                params.add(new BasicNameValuePair("tableName", "rank_record_list"));
                params.add(new BasicNameValuePair("user_id", distance_MainActivity.user_id));
                params.add(new BasicNameValuePair("key_date", cursor.getString(1)));
                params.add(new BasicNameValuePair("total_num", cursor.getString(2)));
                params.add(new BasicNameValuePair("right_num", cursor.getString(3)));
                params.add(new BasicNameValuePair("error_num", cursor.getString(4)));
                params.add(new BasicNameValuePair("right_rate", cursor.getString(5)));
                params.add(new BasicNameValuePair("Date", cursor.getString(7)));


                JSONObject json = jsonParser.makeHttpRequest(url_Insert,"POST", params);
                try {
                    // check log cat fro response
                    Log.i("Create Response", json.toString());

                    // Checking for SUCCESS TAG
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        //修改狀態
                        SQLite db = new SQLite(this,"rank_record_list");
                        db.update(cursor.getString(1),"rank_record_list");
                        db.refresh_user_updatetime("upload");
                        db.close();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                i=i+1;
            };
            cursor.close();
        }
    }
    //上傳Insert_practice_record
    public void Insert_practice_record()
    {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        cursor=null;
        cursor = GetCursor("practice_recorde_data");
        // TODO Auto-generated method stub
        if(cursor !=null){
            int i = 0;
            while(cursor.moveToNext()){
                params.add(new BasicNameValuePair("tableName", "practice_record_data"));
                params.add(new BasicNameValuePair("user_id", cursor.getString(1)));
                params.add(new BasicNameValuePair("question_id", cursor.getString(2)));
                params.add(new BasicNameValuePair("value_1", cursor.getString(3)));
                params.add(new BasicNameValuePair("operand1", cursor.getString(4)));
                params.add(new BasicNameValuePair("value_2", cursor.getString(5)));
                params.add(new BasicNameValuePair("operand2", cursor.getString(6)));
                params.add(new BasicNameValuePair("value_3", cursor.getString(7)));
                params.add(new BasicNameValuePair("operand3", cursor.getString(8)));
                params.add(new BasicNameValuePair("value_4", cursor.getString(9)));
                params.add(new BasicNameValuePair("ans", cursor.getString(10)));
                params.add(new BasicNameValuePair("object_pic", cursor.getString(11)));
                doFileUpload(imagefile+cursor.getString(11));
                params.add(new BasicNameValuePair("user_calculate_pic", cursor.getString(12)));
                doFileUpload(imagefile+cursor.getString(12));
                doFileUpload(imagefile+cursor.getString(12).replace("_process.png","_process2.png"));
                doFileUpload(imagefile+cursor.getString(12).replace("_process.png","_process3.png"));
                params.add(new BasicNameValuePair("user_answer", cursor.getString(13)));
                params.add(new BasicNameValuePair("Date", cursor.getString(15)));
                params.add(new BasicNameValuePair("object_hight", cursor.getString(16)));
                params.add(new BasicNameValuePair("object_width", cursor.getString(17)));
                params.add(new BasicNameValuePair("object_top_width", cursor.getString(18)));


                // getting JSON Object
                // Note that create product url accepts POST method
                JSONObject json = jsonParser.makeHttpRequest(url_Insert,"POST", params);
                try {
                    // check log cat fro response

                    // Checking for SUCCESS TAG
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        //修改狀態
                        SQLite db = new SQLite(this,"practice_recorde_data");
                        db.update(cursor.getString(13),"practice_recorde_data");
                        db.refresh_user_updatetime("upload");
                        db.close();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                i=i+1;
            };
            cursor.close();
        }
    }
    public void test()
    {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        cursor=null;
        cursor = GetCursor("test");
        // TODO Auto-generated method stub
        if(cursor !=null){
            int i = 0;
            while(cursor.moveToNext()){
                params.add(new BasicNameValuePair("tableName", "test_data"));
                params.add(new BasicNameValuePair("_TestID", cursor.getString(1)));
                params.add(new BasicNameValuePair("_Hw", cursor.getString(2)));
                params.add(new BasicNameValuePair("_YES", cursor.getString(3)));
                params.add(new BasicNameValuePair("_data", cursor.getString(4)));

                JSONObject json = jsonParser.makeHttpRequest(url_Insert,"POST", params);
                try {
                    // check log cat fro response

                    // Checking for SUCCESS TAG
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        //修改狀態
                        SQLite db = new SQLite(this,"test_ata");
                        db.update(cursor.getString(13),"test_data");
                        db.refresh_user_updatetime("upload");
                        db.close();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                i=i+1;
            };
            cursor.close();
        }

    }
    //上傳Insert_practice_record_m
    public void Insert_practice_record_m()
    {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        cursor=null;
        cursor = GetCursor("practice_recorde_data_m");
        // TODO Auto-generated method stub
        if(cursor !=null){
            int i = 0;
            while(cursor.moveToNext()){
                params.add(new BasicNameValuePair("tableName", "practice_record_data"));
                params.add(new BasicNameValuePair("user_id", cursor.getString(1)));
                params.add(new BasicNameValuePair("question_id", cursor.getString(2)));
                params.add(new BasicNameValuePair("value_1", cursor.getString(3)));
                params.add(new BasicNameValuePair("operand1", cursor.getString(4)));
                params.add(new BasicNameValuePair("value_2", cursor.getString(5)));
                params.add(new BasicNameValuePair("operand2", cursor.getString(6)));
                params.add(new BasicNameValuePair("value_3", cursor.getString(7)));
                params.add(new BasicNameValuePair("operand3", cursor.getString(8)));
                params.add(new BasicNameValuePair("value_4", cursor.getString(9)));
                params.add(new BasicNameValuePair("ans", cursor.getString(10)));
                params.add(new BasicNameValuePair("object_pic", cursor.getString(11)));
                doFileUpload(imagefile+cursor.getString(11));
                params.add(new BasicNameValuePair("user_calculate_pic", cursor.getString(12)));
                doFileUpload(imagefile+cursor.getString(12));
                doFileUpload(imagefile+cursor.getString(12).replace("_process.png","_process2.png"));
                doFileUpload(imagefile+cursor.getString(12).replace("_process.png","_process3.png"));
                params.add(new BasicNameValuePair("user_answer", cursor.getString(13)));
                params.add(new BasicNameValuePair("Date", cursor.getString(15)));
                params.add(new BasicNameValuePair("object_hight", cursor.getString(16)));
                params.add(new BasicNameValuePair("object_width", cursor.getString(17)));
                params.add(new BasicNameValuePair("object_top_width", cursor.getString(18)));


                // getting JSON Object
                // Note that create product url accepts POST method
                JSONObject json = jsonParser.makeHttpRequest(url_Insert,"POST", params);
                try {
                    // check log cat fro response

                    // Checking for SUCCESS TAG
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        //修改狀態
                        SQLite db = new SQLite(this,"practice_recorde_data_m");
                        db.update(cursor.getString(13),"practice_recorde_data_m");
                        db.refresh_user_updatetime("upload");
                        db.close();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                i=i+1;
            };
            cursor.close();
        }
    }




    //上傳Insert_homework_question_list資料
    public void upload_rank()
    {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        cursor=null;
        cursor = GetCursor_offline_userid("rank_allstu",distance_MainActivity.user_id);
        // TODO Auto-generated method stub
        if(cursor !=null){
            int i = 0;
            while(cursor.moveToNext()){
                params.add(new BasicNameValuePair("user_id", distance_MainActivity.user_id));
                params.add(new BasicNameValuePair("total", cursor.getString(2)));
                params.add(new BasicNameValuePair("right", cursor.getString(3)));
                params.add(new BasicNameValuePair("rate", cursor.getString(4)));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//日期
                params.add(new BasicNameValuePair("date", sdf.format(new Date())));
                // getting JSON Object
                // Note that create product url accepts POST method
                JSONObject json = jsonParser.makeHttpRequest(url_modify_rank,"POST", params);
                try {
                    // check log cat fro response
                    Log.i("Create Response", json.toString());

                    // Checking for SUCCESS TAG
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        //修改狀態
                        SQLite db = new SQLite(this,"rank_allstu");
                        db.update(distance_MainActivity.user_id,"rank_allstu");
                        db.refresh_user_updatetime("upload");
                        db.close();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                i=i+1;
            };
            cursor.close();
        }
    }
    public void upload_user_update_time()
    {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        cursor=null;
        cursor = GetCursor("user_permission");
        // TODO Auto-generated method stub
        if(cursor !=null){
            int i = 0;
            while(cursor.moveToNext()){
                params.add(new BasicNameValuePair("tableName", "stu_update_time"));
                params.add(new BasicNameValuePair("user_id", distance_MainActivity.user_id));
                params.add(new BasicNameValuePair("upload_time", cursor.getString(1)));
                params.add(new BasicNameValuePair("download_time", cursor.getString(2)));

                Integer upload_sqlite=Integer.valueOf(cursor.getString(6));

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");//日期
                Integer now_date = Integer.valueOf(sdf.format(new Date()));
                Log.i("upload_sqlite", String.valueOf(upload_sqlite));
                Log.i("now_date", String.valueOf(now_date));

                if(now_date>upload_sqlite)
                {
                    Log.i("upload", "upload");
                    try{
                        doFileUpload_sqlite();
                        SQLite db = new SQLite(this,"user_permission");
                        db.update_upload_sqlite(String.valueOf(now_date));
                        db.close();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                // getting JSON Object
                // Note that create product url accepts POST method
                JSONObject json = jsonParser.makeHttpRequest(url_Insert,"POST", params);
                try {
                    // check log cat fro response
                    Log.i("Create Response", json.toString());

                    // Checking for SUCCESS TAG
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        //修改狀態
                        SQLite db = new SQLite(this,"user_permission");
                        db.update("1","user_permission");
                        db.close();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                i=i+1;
            };
            cursor.close();
        }
    }
    //上傳homework_feedback
    public void Insert_homework_feedback()
    {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        cursor=null;
        cursor = GetCursor("homework_feedback");
        // TODO Auto-generated method stub
        if(cursor !=null){
            int i = 0;
            while(cursor.moveToNext()){
                params.add(new BasicNameValuePair("tableName", "homework_feedback"));
                params.add(new BasicNameValuePair("user_answer", cursor.getString(1)));
                params.add(new BasicNameValuePair("user_id", cursor.getString(2)));
                params.add(new BasicNameValuePair("commend", cursor.getString(3)));
                params.add(new BasicNameValuePair("user_feedback_pic", cursor.getString(4)));
                doFileUpload(imagefile+cursor.getString(4));
                doFileUpload(imagefile+cursor.getString(4).replace("_process.png","_process2.png"));
                doFileUpload(imagefile+cursor.getString(4).replace("_process.png","_process3.png"));
                params.add(new BasicNameValuePair("user_feedback_audio", cursor.getString(5)));
                if(!(cursor.getString(5).equals("null")))
                {
                    doFileUpload(audiofile+cursor.getString(5));
                }
                params.add(new BasicNameValuePair("Date", cursor.getString(7)));
                // getting JSON Object
                // Note that create product url accepts POST method
                JSONObject json = jsonParser.makeHttpRequest(url_Insert,"POST", params);
                try {
                    // check log cat fro response
                    Log.i("Create Response", json.toString());

                    // Checking for SUCCESS TAG
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        //修改狀態
                        SQLite db = new SQLite(this,"homework_feedback");
                        db.update(cursor.getString(4),"homework_feedback");
                        db.refresh_user_updatetime("upload");
                        db.close();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                i=i+1;
            };
            cursor.close();
        }
    }
    //下載downlaod_homework_feedback資料
    public void downlaod_homework_feedback()
    {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tablename", "download_homework_feedback"));
        params.add(new BasicNameValuePair("User_id", distance_MainActivity.user_id));
        String TAG_PRODUCTS = "download_content";
        JSONArray d_table = null;
        // getting JSON string from URL
        JSONObject json = jsonParser.makeHttpRequest(url_download, "GET", params);

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
                    String user_answer = c.getString("user_answer");
                    String user_id = c.getString("user_id");
                    String commend = c.getString("commend");
                    String user_feedback_pic = c.getString("user_feedback_pic");
                    downloadfile(user_feedback_pic,imagefile);
                    downloadfile(user_feedback_pic.replace("_process.png","_process2.png"),imagefile);
                    downloadfile(user_feedback_pic.replace("_process.png","_process3.png"),imagefile);
                    String user_feedback_audio = c.getString("user_feedback_audio");
                    if(!(user_feedback_audio.equals("null")))
                    {
                        downloadfile(user_feedback_audio,audiofile);
                    }
                    String Date = c.getString("Date");


                    //把線上檔案下載到sqlite
                    dbHelper = new SQLite(Myservice.this, "homework_feedback");
                    long no = dbHelper.Download_homework_feedback(user_answer,
                            user_id,
                            commend,
                            user_feedback_pic,
                            user_feedback_audio,
                            Date);
                    dbHelper.close();//關閉資料庫
                    if(no==-1)
                        Log.i("localdb", "error");
                    else{
                        Log.i("localdb", "ok");
                        SQLite db = new SQLite(this,"user_permission");
                        db.refresh_user_updatetime("download");
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
    //上傳Insert_homework_question_list資料
    public void Insert_homework_question_list()
    {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        cursor=null;
        cursor = GetCursor("teacher_sent_question_list");
        // TODO Auto-generated method stub
        if(cursor !=null){
            int i = 0;
            while(cursor.moveToNext()){
                params.add(new BasicNameValuePair("tableName", "homework_question_list"));
                params.add(new BasicNameValuePair("content", cursor.getString(1)));
                params.add(new BasicNameValuePair("shape", cursor.getString(2)));
                params.add(new BasicNameValuePair("Date", cursor.getString(4)));

                // getting JSON Object
                // Note that create product url accepts POST method
                JSONObject json = jsonParser.makeHttpRequest(url_Insert,"POST", params);
                try {
                    // check log cat fro response
                    Log.i("Create Response", json.toString());

                    // Checking for SUCCESS TAG
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        //修改狀態

                        SQLite db = new SQLite(this,"teacher_sent_question_list");
                        db.update(cursor.getString(4),"teacher_sent_question_list");
                        db.refresh_user_updatetime("upload");
                        db.close();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                i=i+1;
            };
            cursor.close();
        }
    }
    //下載downlaod_homework_question_list資料
    public void downlaod_homework_question_list()
    {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tablename", "homework_question_list"));
        params.add(new BasicNameValuePair("User_id", distance_MainActivity.user_id));
        String TAG_PRODUCTS = "download_content";
        JSONArray d_table = null;
        // getting JSON string from URL
        JSONObject json = jsonParser.makeHttpRequest(url_download, "GET", params);

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
                    String content = c.getString("content");
                    String shape= c.getString("shape");
                    String Date= c.getString("Date");



                    //把線上檔案下載到sqlite
                    dbHelper = new SQLite(Myservice.this, "homework_question_list");
                    long no = dbHelper.Download_homework_question_list(content,
                            shape,
                            Date);
                    dbHelper.close();//關閉資料庫
                    if(no==-1)
                        Log.i("localdb", "error");
                    else{
                        Log.i("localdb", "ok");
                        SQLite db = new SQLite(this,"user_permission");
                        db.refresh_user_updatetime("download");
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
    //上傳Insert_homework_data資料
    public void Insert_homework_data()
    {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        cursor=null;
        cursor = GetCursor("homework_data");
        // TODO Auto-generated method stub
        if(cursor !=null){
            int i = 0;
            while(cursor.moveToNext()){
                params.add(new BasicNameValuePair("tableName", "homework_data"));
                params.add(new BasicNameValuePair("user_id", cursor.getString(1)));
                params.add(new BasicNameValuePair("question_id", cursor.getString(2)));
                params.add(new BasicNameValuePair("value_1", cursor.getString(3)));
                params.add(new BasicNameValuePair("operand1", cursor.getString(4)));
                params.add(new BasicNameValuePair("value_2", cursor.getString(5)));
                params.add(new BasicNameValuePair("operand2", cursor.getString(6)));
                params.add(new BasicNameValuePair("value_3", cursor.getString(7)));
                params.add(new BasicNameValuePair("operand3", cursor.getString(8)));
                params.add(new BasicNameValuePair("value_4", cursor.getString(9)));
                params.add(new BasicNameValuePair("ans", cursor.getString(10)));
                params.add(new BasicNameValuePair("object_pic", cursor.getString(11)));
                doFileUpload(imagefile+cursor.getString(11));
                params.add(new BasicNameValuePair("user_calculate_pic", cursor.getString(12)));
                doFileUpload(imagefile+cursor.getString(12));
                doFileUpload(imagefile+cursor.getString(12).replace("_process.png","_process2.png"));
                doFileUpload(imagefile+cursor.getString(12).replace("_process.png","_process3.png"));
                params.add(new BasicNameValuePair("user_answer", cursor.getString(13)));
                params.add(new BasicNameValuePair("Date", cursor.getString(15)));
                params.add(new BasicNameValuePair("object_hight", cursor.getString(16)));
                params.add(new BasicNameValuePair("object_width", cursor.getString(17)));
                params.add(new BasicNameValuePair("object_top_width", cursor.getString(18)));


                // getting JSON Object
                // Note that create product url accepts POST method
                JSONObject json = jsonParser.makeHttpRequest(url_Insert,"POST", params);
                try {
                    // check log cat fro response
                    Log.i("Create Response", json.toString());

                    // Checking for SUCCESS TAG
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        //修改狀態
                        SQLite db = new SQLite(this,"homework_data");
                        db.update(cursor.getString(13),"homework_data");
                        db.refresh_user_updatetime("upload");
                        db.close();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                i=i+1;
            };
            cursor.close();
        }
    }
    //上傳rmap_poi資料
    public void Insert_rmap_poi()
    {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        cursor=null;
        cursor = GetCursor("rmap_poi");
        // TODO Auto-generated method stub
        if(cursor !=null){
            int i = 0;
            while(cursor.moveToNext()){
                params.add(new BasicNameValuePair("tableName", "rmap_poi"));
                params.add(new BasicNameValuePair("c_user", cursor.getString(1)));
                params.add(new BasicNameValuePair("title", cursor.getString(2)));
                params.add(new BasicNameValuePair("la", String.valueOf(cursor.getDouble(3))));
                params.add(new BasicNameValuePair("lo", String.valueOf(cursor.getDouble(4))));
                params.add(new BasicNameValuePair("shape", cursor.getString(5)));
                params.add(new BasicNameValuePair("key_num", cursor.getString(6)));
                params.add(new BasicNameValuePair("value_1", cursor.getString(7)));
                params.add(new BasicNameValuePair("value_2", cursor.getString(8)));
                params.add(new BasicNameValuePair("value_3", cursor.getString(9)));
                params.add(new BasicNameValuePair("object_pic", cursor.getString(10)));
                doFileUpload(imagefile+cursor.getString(10));
                params.add(new BasicNameValuePair("position", cursor.getString(11)));
                params.add(new BasicNameValuePair("Date", cursor.getString(13)));



                // getting JSON Object
                // Note that create product url accepts POST method
                JSONObject json = jsonParser.makeHttpRequest(url_Insert,"POST", params);
                try {
                    // check log cat fro response
                    Log.i("Create Response", json.toString());

                    // Checking for SUCCESS TAG
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        //修改狀態
                        SQLite db = new SQLite(this,"rmap_poi");
                        db.update(cursor.getString(6),"rmap_poi");
                        db.refresh_user_updatetime("upload");
                        db.close();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                i=i+1;
            };
            cursor.close();
        }
    }
    //下載download_rmaps_poi資料
//    public void download_rmap_poi()
//    {
//        String c_user,title,shape,key_num,value_1,value_2,value_3,object_pic,position,Date;
//        Double la,lo;
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("tablename", "download_rmap_poi"));
//        params.add(new BasicNameValuePair("User_id", distance_MainActivity.user_id));
//        String TAG_PRODUCTS = "download_content";
//        JSONArray d_table = null;
//        // getting JSON string from URL
//        JSONObject json = jsonParser.makeHttpRequest(url_download, "GET", params);
//
//        try {
//            // Checking for SUCCESS TAG
//            int success = json.getInt(TAG_SUCCESS);
//
//            if (success == 1) {
//                // products found
//                // Getting Array of Products
//                d_table = json.getJSONArray(TAG_PRODUCTS);
//
//                // looping through All Products
//                for (int i = 0; i < d_table.length(); i++) {
//                    JSONObject c = d_table.getJSONObject(i);
//
//                    // Storing each json item in variable
//                    c_user = c.getString("c_user");
//                    title = c.getString("title");
//                    la= Double.valueOf(c.getString("la"));
//                    lo= Double.valueOf(c.getString("lo"));
//                    shape= c.getString("shape");
//                    key_num= c.getString("key_num");
//                    value_1= c.getString("value_1");
//                    value_2= c.getString("value_2");
//                    value_3= c.getString("value_3");
//                    object_pic= c.getString("object_pic");
//                    downloadfile(object_pic,imagefile);
//                    position= c.getString("position");
//                    Date= c.getString("Date");
//
//                    //把線上檔案下載到sqlite
//                    dbHelper = new SQLite(Myservice.this, "rmap_poi");
//                    long no = dbHelper.download_poi(c_user,title,la,lo,shape,key_num,value_1,value_2,value_3,object_pic,position,Date);
//                    dbHelper.close();//關閉資料庫
//                    if(no==-1)
//                        Log.i("localdb", "error");
//                    else{
//                        Log.i("localdb", "ok");
//                        SQLite db = new SQLite(this,"poi");
//                        db.refresh_user_updatetime("download");
//                        db.close();
//                    }
//                    try {
//                        //新增至POI
//                        SQLiteDatabase mDatabase = getDatabase();
//                        final ContentValues cv = new ContentValues();
//                        cv.put("name", title);
//                        cv.put("descr", c_user);
//                        cv.put("lat", la);
//                        cv.put("lon", lo);
//                        cv.put("alt", 0);
//                        cv.put("categoryid", 0);
//                        cv.put("pointsourceid", 0);
//                        cv.put("hidden", 0);
//                        cv.put("iconid", 2130837595);
//                        cv.put("superkey",key_num);
//                        cv.put("status", Date);
//                        mDatabase.insert("points", null, cv);
//                        mDatabase.close();
//                        //新增至POI
//                    } catch (Exception e) {
//                        Log.e("ee", e.toString());
//                    }
//                }
//                MainActivity.mPoiOverlay.UpdateList();
//            } else {
//                // no products found
//                // Launch Add New product Activity
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    //上傳rmap_poi_comment資料
//    public void Insert_rmap_poi_comment()
//    {
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        cursor=null;
//        cursor = GetCursor("poi_comment");
//        // TODO Auto-generated method stub
//        if(cursor !=null){
//            int i = 0;
//            while(cursor.moveToNext()){
//                params.add(new BasicNameValuePair("tableName", "poi_comment"));
//                params.add(new BasicNameValuePair("user", cursor.getString(1)));
//                params.add(new BasicNameValuePair("comment", cursor.getString(2)));
//                params.add(new BasicNameValuePair("super_key", cursor.getString(3)));
//                params.add(new BasicNameValuePair("comment_key", cursor.getString(4)));
//                params.add(new BasicNameValuePair("mark", cursor.getString(5)));
//                params.add(new BasicNameValuePair("Date", cursor.getString(7)));
//
//
//
//                // getting JSON Object
//                // Note that create product url accepts POST method
//                JSONObject json = jsonParser.makeHttpRequest(url_Insert,"POST", params);
//                try {
//                    // check log cat fro response
//                    Log.i("Create Response", json.toString());
//
//                    // Checking for SUCCESS TAG
//                    int success = json.getInt(TAG_SUCCESS);
//
//                    if (success == 1) {
//                        //修改狀態
//                        SQLite db = new SQLite(this,"poi_comment");
//                        db.update(cursor.getString(4),"poi_comment");
//                        db.refresh_user_updatetime("upload");
//                        db.close();
//                    }
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
//                i=i+1;
//            };
//            cursor.close();
//        }
//    }
    //下載downlaod_mark_record資料
    public void download_rmap_poi_comment()
    {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tablename", "download_poi_comment"));
        params.add(new BasicNameValuePair("User_id", distance_MainActivity.user_id));
        String TAG_PRODUCTS = "download_content";
        JSONArray d_table = null;
        // getting JSON string from URL
        JSONObject json = jsonParser.makeHttpRequest(url_download, "GET", params);

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
                    String user = c.getString("user");
                    String comment = c.getString("comment");
                    String super_key = c.getString("super_key");
                    String comment_key = c.getString("comment_key");
                    String mark = c.getString("mark");
                    String Date = c.getString("Date");



                    //把線上檔案下載到sqlite
                    dbHelper = new SQLite(Myservice.this, "poi_comment");
                    long no = dbHelper.Download_poi_comment(user,
                            comment,
                            super_key,
                            comment_key,
                            mark,
                            Date);
                    dbHelper.close();//關閉資料庫
                    if(no==-1)
                        Log.i("localdb", "error");
                    else{
                        Log.i("localdb", "ok");
                        SQLite db = new SQLite(this,"poi_comment");
                        db.refresh_user_updatetime("download");
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
    //下載downlaod_Insert_homework_data資料
    public void downlaod_Insert_homework_data()
    {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tablename", "download_homework_data"));
        params.add(new BasicNameValuePair("User_id", distance_MainActivity.user_id));
        String TAG_PRODUCTS = "download_content";
        JSONArray d_table = null;
        // getting JSON string from URL
        JSONObject json = jsonParser.makeHttpRequest(url_download, "GET", params);

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
                    String question_id = c.getString("question_id");
                    String value_1 = c.getString("value_1");
                    String operand1 = c.getString("operand1");
                    String value_2 = c.getString("value_2");
                    String operand2 = c.getString("operand2");
                    String value_3 = c.getString("value_3");
                    String operand3 = c.getString("operand3");
                    String value_4 = c.getString("value_4");
                    String ans = c.getString("ans");
                    String object_pic = c.getString("object_pic");
                    downloadfile(object_pic,imagefile);
                    String user_calculate_pic = c.getString("user_calculate_pic");
                    downloadfile(user_calculate_pic,imagefile);
                    downloadfile(user_calculate_pic.replace("_process.png","_process2.png"),imagefile);
                    downloadfile(user_calculate_pic.replace("_process.png","_process3.png"),imagefile);
                    String user_answer = c.getString("user_answer");
                    String Date = c.getString("Date");
                    String object_hight = c.getString("object_hight");
                    String object_width = c.getString("object_width");
                    String object_top_width = c.getString("object_top_width");




                    //把線上檔案下載到sqlite
                    dbHelper = new SQLite(Myservice.this, "homework_data");
                    long no = dbHelper.Download_homework_data(user_id,
                            question_id,
                            value_1,
                            operand1,
                            value_2,
                            operand2,
                            value_3,
                            operand3,
                            value_4,
                            ans,
                            object_pic,
                            user_calculate_pic,
                            user_answer,
                            Date,
                            object_hight,
                            object_width,
                            object_top_width);
                    dbHelper.close();//關閉資料庫
                    if(no==-1)
                        Log.i("localdb", "error");
                    else{
                        Log.i("localdb", "ok");
                        SQLite db = new SQLite(this,"user_permission");
                        db.refresh_user_updatetime("download");
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
    //下載排名
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
                    if(!(user_id.equals(distance_MainActivity.user_id)))
                    {
                        SQLite db = new SQLite(this,"rank_allstu");
                        db.update_all_stu_rank(user_id,total_num,right_num,right_rate,Date);
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
//    private void update_ramp_hiden()//需要傳superkey
//    {
//        //修改rmap_hide
//        SQLiteDatabase mDatabase = getDatabase();
//        ContentValues cv = new ContentValues();
//        cv.put("status", 0);
//        String where = "superkey= ?";
//        String[] whereValue ={"stu2420130814142214"};
//        mDatabase.update("points", cv, where, whereValue);
//        mDatabase.close();
//    }
    private Cursor GetCursor_offline_userid(String table_name,String user_id) {
        // TODO Auto-generated method stub
        dbHelper = new SQLite(Myservice.this, table_name);
        //尋找sqlite中offline的資料欄位
        cursor=null;
        cursor = dbHelper.getAllifoffline_userid(table_name,distance_MainActivity.user_id,"offline");	//取得SQLite類別的回傳值:Cursor物件
        rows_num = cursor.getCount();	//取得資料表列數
        if(rows_num==0)
        {
            cursor=null;
            dbHelper.close();
            return null;
        }
        dbHelper.close();
        return cursor;
    }
    private Cursor GetCursor(String table_name) {
        // TODO Auto-generated method stub
        dbHelper = new SQLite(Myservice.this, table_name);
        //尋找sqlite中offline的資料欄位
        cursor=null;
        cursor = dbHelper.getAllifoffline(table_name,"offline");	//取得SQLite類別的回傳值:Cursor物件
        rows_num = cursor.getCount();	//取得資料表列數
        if(rows_num==0)
        {
            cursor=null;
            dbHelper.close();
            return null;
        }
        dbHelper.close();
        return cursor;
    }
    //上傳sqlite
    private void doFileUpload_sqlite(){
        String filepath="/data/data/com.robert.maps/databases/DistanceMathDatabase";
        HttpURLConnection conn =    null;
        DataOutputStream dos = null;
        DataInputStream inStream = null;
        String exsistingFileName = filepath;


        // Is this the place are you doing something wrong.
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary =  "*****";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1*1024*1024;
        String responseFromServer = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//日期
        String urlString = url_uploadsqlite_php+"?user_id="+distance_MainActivity.user_id+"&store_time="+sdf.format(new Date());

        try
        {
            //------------------ CLIENT REQUEST
            Log.e("MediaPlayer","Inside second Method");
            FileInputStream fileInputStream = new FileInputStream(new File(exsistingFileName) );
            // open a URL connection to the Servlet
            URL url = new URL(urlString);
            // Open a HTTP connection to the URL
            conn = (HttpURLConnection) url.openConnection();
            // Allow Inputs
            conn.setDoInput(true);
            // Allow Outputs
            conn.setDoOutput(true);
            // Don't use a cached copy.
            conn.setUseCaches(false);
            // Use a post method.
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);



            dos = new DataOutputStream( conn.getOutputStream() );
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
                    + exsistingFileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);
            Log.e("MediaPlayer","Headers are written");
            // create a buffer of maximum size
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0){
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            // close streams
            Log.e("MediaPlayer","File is written");
            fileInputStream.close();
            dos.flush();
            dos.close();
        }

        catch (MalformedURLException ex)
        {
            Log.e("MediaPlayer", "error: " + ex.getMessage(), ex);
        }
        catch (IOException ioe)
        {
            Log.e("MediaPlayer", "error: " + ioe.getMessage(), ioe);
        }
        //------------------ read the SERVER RESPONSE
        try {
            inStream = new DataInputStream ( conn.getInputStream() );
            String str;
            while (( str = inStream.readLine()) != null)
            {
                Log.e("MediaPlayer","Server Response"+str);
            }
            inStream.close();
        }
        catch (IOException ioex){
            Log.e("MediaPlayer", "error: " + ioex.getMessage(), ioex);
        }
    }

    //上傳檔案  type 1照片 0錄音
    private void doFileUpload(String filepath){
        HttpURLConnection conn =    null;
        DataOutputStream dos = null;
        DataInputStream inStream = null;
        String exsistingFileName = filepath;

        // Is this the place are you doing something wrong.
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary =  "*****";

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1*1024*1024;
        String responseFromServer = "";
        String urlString = url_uploadfile_php;

        try
        {
            //------------------ CLIENT REQUEST
            Log.e("MediaPlayer","Inside second Method");
            FileInputStream fileInputStream = new FileInputStream(new File(exsistingFileName) );
            // open a URL connection to the Servlet
            URL url = new URL(urlString);
            // Open a HTTP connection to the URL
            conn = (HttpURLConnection) url.openConnection();
            // Allow Inputs
            conn.setDoInput(true);
            // Allow Outputs
            conn.setDoOutput(true);
            // Don't use a cached copy.
            conn.setUseCaches(false);
            // Use a post method.
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
            dos = new DataOutputStream( conn.getOutputStream() );
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\""
                    + exsistingFileName + "\"" + lineEnd);
            dos.writeBytes(lineEnd);
            Log.e("MediaPlayer","Headers are written");
            // create a buffer of maximum size
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];
            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            while (bytesRead > 0){
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }
            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
            // close streams
            Log.e("MediaPlayer","File is written");
            fileInputStream.close();
            dos.flush();
            dos.close();
        }

        catch (MalformedURLException ex)
        {
            Log.e("MediaPlayer", "error: " + ex.getMessage(), ex);
        }
        catch (IOException ioe)
        {
            Log.e("MediaPlayer", "error: " + ioe.getMessage(), ioe);
        }
        //------------------ read the SERVER RESPONSE
        try {
            inStream = new DataInputStream ( conn.getInputStream() );
            String str;
            while (( str = inStream.readLine()) != null)
            {
                Log.e("MediaPlayer","Server Response"+str);
            }
            inStream.close();
        }
        catch (IOException ioex){
            Log.e("MediaPlayer", "error: " + ioex.getMessage(), ioex);
        }
    }
    //下載檔案  type 1照片 0錄音
    public void downloadfile(String filename,String filepath)
    {

        String savePath;
        URL url;
        savePath = filepath + filename;
        try {
            url = new URL(url_file_download+filename);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            InputStream is = con.getInputStream();

            File ofe = new File(savePath);
            if(!ofe.exists()){
                ofe.createNewFile();
            }
            // write string
            FileOutputStream fos = new FileOutputStream(ofe);
            byte data[] = new byte[1024];
            int length = 0, getPer = 0;
            while((getPer = is.read(data))!=-1){
                length+=getPer;
                fos.write(data, 0, getPer);
            }

            fos.flush();
            fos.close();
            is.close();
            con.disconnect();
        }
        catch (Exception ioex){
            Log.e("Debug", "error: " + ioex.getMessage(), ioex);
        }
    }

//    //新增內建資料庫poi Function
//    protected SQLiteDatabase getDatabase() {
//        File folder = Ut.getRMapsMainDir(this, "data");
//        if(!folder.exists()) // no sdcard
//            return null;
//
//        SQLiteDatabase db;
//        try {
//            db = new GeoDatabaseHelper(this, folder.getAbsolutePath() + "/geodata.db").getWritableDatabase();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//
//        return db;
//    }

//    protected class GeoDatabaseHelper extends RSQLiteOpenHelper {
//        private final static int mCurrentVersion = 21;
//
//        public GeoDatabaseHelper(final Context context, final String name) {
//            super(context, name, null, mCurrentVersion);
//        }
//
//        @Override
//        public void onCreate(final SQLiteDatabase db) {
//            db.execSQL(PoiConstants.SQL_CREATE_points);
//            db.execSQL(PoiConstants.SQL_CREATE_pointsource);
//            db.execSQL(PoiConstants.SQL_CREATE_category);
//            db.execSQL(PoiConstants.SQL_ADD_category);
//            db.execSQL(PoiConstants.SQL_CREATE_tracks);
//            db.execSQL(PoiConstants.SQL_CREATE_trackpoints);
//            db.execSQL(PoiConstants.SQL_CREATE_maps);
//            LoadActivityListFromResource(db);
//        }
//
//        @Override
//        public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
////				Ut.dd("Upgrade data.db from ver." + oldVersion + " to ver."
////						+ newVersion);
//
//            if (oldVersion < 2) {
//                db.execSQL(PoiConstants.SQL_UPDATE_1_1);
//                db.execSQL(PoiConstants.SQL_UPDATE_1_2);
//                db.execSQL(PoiConstants.SQL_UPDATE_1_3);
//                db.execSQL(PoiConstants.SQL_CREATE_points);
//                db.execSQL(PoiConstants.SQL_UPDATE_1_5);
//                db.execSQL(PoiConstants.SQL_UPDATE_1_6);
//                db.execSQL(PoiConstants.SQL_UPDATE_1_7);
//                db.execSQL(PoiConstants.SQL_UPDATE_1_8);
//                db.execSQL(PoiConstants.SQL_UPDATE_1_9);
//                db.execSQL(PoiConstants.SQL_CREATE_category);
//                db.execSQL(PoiConstants.SQL_ADD_category);
//                //db.execSQL(PoiConstants.SQL_UPDATE_1_11);
//                //db.execSQL(PoiConstants.SQL_UPDATE_1_12);
//            }
//            if (oldVersion < 3) {
//                db.execSQL(PoiConstants.SQL_UPDATE_2_7);
//                db.execSQL(PoiConstants.SQL_UPDATE_2_8);
//                db.execSQL(PoiConstants.SQL_UPDATE_2_9);
//                db.execSQL(PoiConstants.SQL_CREATE_category);
//                db.execSQL(PoiConstants.SQL_UPDATE_2_11);
//                db.execSQL(PoiConstants.SQL_UPDATE_2_12);
//            }
//            if (oldVersion < 5) {
//                db.execSQL(PoiConstants.SQL_CREATE_tracks);
//                db.execSQL(PoiConstants.SQL_CREATE_trackpoints);
//            }
//            if (oldVersion < 18) {
//                db.execSQL(PoiConstants.SQL_UPDATE_6_1);
//                db.execSQL(PoiConstants.SQL_UPDATE_6_2);
//                db.execSQL(PoiConstants.SQL_UPDATE_6_3);
//                db.execSQL(PoiConstants.SQL_CREATE_tracks);
//                db.execSQL(PoiConstants.SQL_UPDATE_6_4);
//                db.execSQL(PoiConstants.SQL_UPDATE_6_5);
//                LoadActivityListFromResource(db);
//            }
//            if (oldVersion < 20) {
//                db.execSQL(PoiConstants.SQL_UPDATE_6_1);
//                db.execSQL(PoiConstants.SQL_UPDATE_6_2);
//                db.execSQL(PoiConstants.SQL_UPDATE_6_3);
//                db.execSQL(PoiConstants.SQL_CREATE_tracks);
//                db.execSQL(PoiConstants.SQL_UPDATE_20_1);
//                db.execSQL(PoiConstants.SQL_UPDATE_6_5);
//            }
//            if (oldVersion < 21) {
//                db.execSQL(PoiConstants.SQL_CREATE_maps);
//            }
//        }
//    }
//
//    public void LoadActivityListFromResource(final SQLiteDatabase db) {
//        db.execSQL(PoiConstants.SQL_CREATE_drop_activity);
//        db.execSQL(PoiConstants.SQL_CREATE_activity);
//        String[] act = this.getResources().getStringArray(R.array.track_activity);
//        for(int i = 0; i < act.length; i++){
//            db.execSQL(String.format(PoiConstants.SQL_CREATE_insert_activity, i, act[i]));
//        }
//    }

    private boolean isInternetConnected(){

        //get ConnectivityManager instance
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        //get network info
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        //check network is connecting
        if(netInfo!=null&&netInfo.isConnected()){
            return true;
        }else{
            return false;
        }

    }
}
