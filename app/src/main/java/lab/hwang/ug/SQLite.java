package lab.hwang.ug;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLite extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 4;				//資料庫版本
    private static final String DATABASE_NAME = "DistanceMathDatabase";	//資料庫名稱
    private String TABLE_NAME = "";	//資料表名稱
    private SQLiteDatabase db;
    public SQLite(Context context, String tableName) {//建構子
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        TABLE_NAME = tableName;
        //context=內容物件；name=傳入資料庫名稱；factory=複雜查詢時使用；version=資料庫版本
        db = this.getWritableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        // TODO Auto-generated method stub
        db.execSQL("CREATE TABLE IF NOT EXISTS personhight "
                + "("
                + "_ID INTEGER PRIMARY KEY,"
                + "personhight STRING);");

        db.execSQL("CREATE TABLE IF NOT EXISTS homework_question_list "
                + "("
                + "_ID Integer PRIMARY KEY,"
                + "content STRING,"
                + "shape STRING,"
                + "Date TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS teacher_sent_question_list "
                + "("
                + "_ID Integer PRIMARY KEY,"
                + "content STRING,"
                + "shape STRING,"
                + "status STRING,"
                + "Date TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS homework_data "
                + "("
                + "_ID Integer PRIMARY KEY,"
                + "user_id STRING,"
                + "question_id STRING,"
                + "value_1 STRING,"
                + "operand1 STRING,"
                + "value_2 STRING,"
                + "operand2 STRING,"
                + "value_3 STRING,"
                + "operand3 STRING,"
                + "value_4 STRING,"
                + "ans STRING,"
                + "object_pic STRING,"
                + "user_calculate_pic STRING,"
                + "user_answer STRING,"
                + "status STRING,"
                + "Date TEXT,"
                + "object_hight STRING,"
                + "object_width STRING,"
                + "object_top_width STRING);");

        db.execSQL("CREATE TABLE IF NOT EXISTS practice_recorde_data "
                + "("
                + "_ID Integer PRIMARY KEY,"
                + "user_id STRING,"
                + "question_id STRING,"
                + "value_1 STRING,"
                + "operand1 STRING,"
                + "value_2 STRING,"
                + "operand2 STRING,"
                + "value_3 STRING,"
                + "operand3 STRING,"
                + "value_4 STRING,"
                + "ans STRING,"
                + "object_pic STRING,"
                + "user_calculate_pic STRING,"
                + "user_answer STRING,"
                + "status STRING,"
                + "Date TEXT,"
                + "object_hight STRING,"
                + "object_width STRING,"
                + "object_top_width STRING);");
        db.execSQL("CREATE TABLE IF NOT EXISTS practice_recorde_data_m "
                + "("
                + "_ID Integer PRIMARY KEY,"
                + "user_id STRING,"
                + "question_id STRING,"
                + "value_1 STRING,"
                + "operand1 STRING,"
                + "value_2 STRING,"
                + "operand2 STRING,"
                + "value_3 STRING,"
                + "operand3 STRING,"
                + "value_4 STRING,"
                + "ans STRING,"
                + "object_pic STRING,"
                + "user_calculate_pic STRING,"
                + "user_answer STRING,"
                + "status STRING,"
                + "Date TEXT,"
                + "object_hight STRING,"
                + "object_width STRING,"
                + "object_top_width STRING);");


        db.execSQL("CREATE TABLE IF NOT EXISTS rank_record "
                + "("
                + "_ID Integer PRIMARY KEY,"
                + "user_id STRING,"
                + "key_date STRING,"
                + "value_1 STRING,"
                + "operand1 STRING,"
                + "value_2 STRING,"
                + "operand2 STRING,"
                + "value_3 STRING,"
                + "operand3 STRING,"
                + "value_4 STRING,"
                + "ans STRING,"
                + "object_pic STRING,"
                + "user_calculate_pic STRING,"
                + "shape STRING,"
                + "status STRING,"
                + "Date TEXT,"
                + "object_hight STRING,"
                + "object_width STRING,"
                + "object_top_width STRING,"
                + "correct STRING,"
                + "select_formula STRING);");
        db.execSQL("CREATE TABLE IF NOT EXISTS rank_record_list "
                + "("
                + "_ID Integer PRIMARY KEY,"
                + "key_date STRING,"
                + "total_num STRING,"
                + "right_num STRING,"
                + "error_num STRING,"
                + "right_rate STRING,"
                + "status STRING,"
                + "Date TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS homework_feedback "
                + "("
                + "_ID Integer PRIMARY KEY,"
                + "user_answer STRING,"
                + "user_id STRING,"
                + "commend STRING,"
                + "user_feedback_pic STRING,"
                + "user_feedback_audio STRING,"
                + "status STRING,"
                + "Date TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS rank_allstu "
                + "("
                + "_ID Integer PRIMARY KEY,"
                + "user_id STRING,"
                + "total_num STRING,"
                + "right_num STRING,"
                + "right_rate STRING,"
                + "status STRING,"
                + "Date TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS record_action "
                + "("
                + "_ID Integer PRIMARY KEY,"
                + "page STRING,"
                + "action STRING,"
                + "status STRING,"
                + "Date TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS record_session_one_practice "
                + "("
                + "_ID Integer PRIMARY KEY,"
                + "type STRING,"
                + "status STRING,"
                + "Date TEXT);");

        db.execSQL("CREATE TABLE IF NOT EXISTS test_data "
                + "("
                + "_ID Integer PRIMARY KEY,"
                + "TestID STRING,"
                + "_Hw STRING,"
                + "_Yes STRING,"
                + "_data STRING);");



        db.execSQL("CREATE TABLE IF NOT EXISTS see_practice_record "
                + "("
                + "_ID Integer PRIMARY KEY,"
                + "action STRING,"
                + "status STRING,"
                + "Date TEXT);");

        db.execSQL("CREATE TABLE IF NOT EXISTS see_practice_record_m"
                + "("
                + "_ID Integer PRIMARY KEY,"
                + "action STRING,"
                + "status STRING,"
                + "Date TEXT);");


        db.execSQL("CREATE TABLE IF NOT EXISTS see_rank_test_record "
                + "("
                + "_ID Integer PRIMARY KEY,"
                + "action STRING,"
                + "status STRING,"
                + "Date TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS mark_others_homework "
                + "("
                + "_ID Integer PRIMARY KEY,"
                + "question_id STRING,"
                + "user_answer STRING,"
                + "mark_user STRING,"
                + "user_id STRING,"
                + "correct STRING,"
                + "status STRING,"
                + "Date TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS others_marks_homework_record "
                + "("
                + "_ID Integer PRIMARY KEY,"
                + "question_id STRING,"
                + "user_answer STRING,"
                + "mark_user STRING,"
                + "user_id STRING,"
                + "correct STRING,"
                + "status STRING,"
                + "Date TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS rmap_poi "
                + "("
                + "_ID Integer PRIMARY KEY,"
                + "c_user STRING,"
                + "title STRING,"
                + "la STRING,"
                + "lo STRING,"
                + "shape STRING,"
                + "key_num STRING,"
                + "value_1 STRING,"
                + "value_2 STRING,"
                + "value_3 STRING,"
                + "object_pic STRING,"
                + "position STRING,"
                + "status STRING,"
                + "Date TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS poi_comment "
                + "("
                + "_ID Integer PRIMARY KEY,"
                + "user STRING,"
                + "comment STRING,"
                + "super_key STRING,"
                + "comment_key STRING,"
                + "mark STRING,"
                + "status STRING,"
                + "Date TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS user_permission "
                + "("
                + "_ID Integer PRIMARY KEY,"
                + "upload_time STRING,"
                + "download_time STRING,"
                + "rmap_add STRING,"
                + "permission String,"
                + "status String,"
                + "upload_sqlite STRING);");

        for(int i = 1;i<=Login.stu_num;i++)
        {
            ContentValues args = new ContentValues();
            if(i<=9)
            {
                args.put("user_id", "stu0"+String.valueOf(i));
            }else
            {
                args.put("user_id", "stu"+String.valueOf(i));
            }
            args.put("total_num", "0");
            args.put("right_num", "0");
            args.put("right_rate", "0");
            args.put("status", "online");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//日期
            args.put("Date", sdf.format(new Date()));
            db.insert("rank_allstu", null, args);
        }

        ContentValues args = new ContentValues();
        args.put("_ID", 1);
        args.put("personhight", 1);
        db.insert("personhight", null, args);

        ContentValues args2 = new ContentValues();
        args2.put("_ID", 1);
        args2.put("upload_time", "201307241646");
        args2.put("download_time", "201307241646");
        args2.put("rmap_add", "none");
        args2.put("permission", "5");
        args2.put("status", "offline");
        args2.put("upload_sqlite", "20131025");
        db.insert("user_permission", null, args2);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // 取得所有記錄
    public Cursor getAll(String s) {
        return db.rawQuery("SELECT * FROM " + s, null);
    }

    public Cursor getpersonhight(String tb)
    {
        return db.rawQuery("SELECT * FROM " + tb +" WHERE _ID= "+ "1", null);
    }

    public Cursor getversion(String tb)
    {
        return db.rawQuery("SELECT * FROM " + tb +" WHERE _ID= "+ "1", null);
    }

    public Cursor check_done_homework(String table_name,String question_id,String user_id)
    {
        return db.rawQuery("SELECT * FROM " + table_name +" WHERE question_id= \'"+ question_id +"\' AND user_id= \'"+ user_id +"\' ", null);
    }

    public Cursor get_all_marks(String table_name,String user_answer)
    {
        return db.rawQuery("SELECT * FROM " + table_name +" WHERE user_answer= \'"+ user_answer +"\'", null);
    }

    //搜尋所有homework_list資料
    public Cursor get_all_practice_record(String table_name)
    {
        return db.rawQuery("SELECT * FROM " + table_name + " ORDER BY Date desc", null);
    }
    public Cursor get_all_practice_record2(String table_name)
    {
        return db.rawQuery("SELECT * FROM " + table_name + " ORDER BY Date desc", null);
    }


	/*public Cursor get_num_practice_record(String table_name)
	{
		Cursor result = db.rawQuery("SELECT * FROM " + table_name + " Where num <> 1", null);

		return result;
	}*/

    //搜尋所有rank_record資料
    public Cursor get_all_rank_record(String table_name)
    {
        return db.rawQuery("SELECT * FROM " + table_name + " ORDER BY right_num desc , right_rate desc, Date desc", null);
    }

    //搜尋所有homework_list資料
    public Cursor get_all_rank_record_detail(String table_name,String key_date)
    {
        return db.rawQuery("SELECT * FROM " + table_name +" WHERE key_date= \'"+ key_date +"\' ORDER BY Date ASC", null);
    }

    //搜尋所有homework_list資料
    public Cursor get_all_homework_list_item(String table_name)
    {
        return db.rawQuery("SELECT * FROM " + table_name + " ORDER BY Date desc", null);
    }
    //搜尋所有homework_data資料
    public Cursor get_all_homework_data_join_list_date()
    {
        return db.rawQuery("SELECT homework_data.*,homework_question_list.shape FROM homework_data,homework_question_list where homework_data.question_id = homework_question_list.Date ", null);
    }
    public Cursor get_all_practice_data_join_list_date()
    {
        return db.rawQuery("SELECT * FROM practice_recorde_data", null);
    }

    //搜尋已繳交作業的同學
    public Cursor get_all_poi()
    {
        return db.rawQuery("SELECT * FROM rmap_poi", null);
    }
    //搜尋已繳交作業的同學
    public Cursor get_test_shape(String table_name,String key_date)
    {
        return db.rawQuery("SELECT * FROM " + table_name  +" WHERE Date= \'"+ key_date +"\'", null);
    }

    //搜尋已繳交作業的同學
    public Cursor get_submit_stu_list(String table_name,String question_id)
    {
        return db.rawQuery("SELECT homework_data.*,mark_others_homework.correct FROM homework_data LEFT JOIN mark_others_homework ON (homework_data.question_id = mark_others_homework.question_id and homework_data.user_id = mark_others_homework.user_id) where homework_data.question_id = \'"+ question_id +"\' ORDER BY user_id ASC", null);

    }
    //搜尋feedback的內容
    public Cursor get_feedback_list_item(String table_name,String user_answer)
    {
        return db.rawQuery("SELECT * FROM " + table_name  +" WHERE user_answer= \'"+ user_answer +"\' ORDER BY Date desc", null);
    }
    //搜尋offline的資料表單
    public Cursor getAllifoffline(String s,String f)
    {

        return db.rawQuery("SELECT * FROM " + s +" WHERE status= \'"+ f +"\'", null);
    }
    //搜尋offline的資料表單where user_id
    public Cursor getAllifoffline_userid(String s,String user_id,String f)
    {

        return db.rawQuery("SELECT * FROM " + s +" WHERE status= \'"+ f +"\' AND user_id= \'"+ user_id +"\' ", null);
    }

    //搜尋所有homework_list資料
    public Cursor get_user_rank_record(String table_name,String user_id)
    {
        return db.rawQuery("SELECT * FROM " + table_name +" WHERE user_id= \'"+ user_id +"\'", null);
    }



    //搜尋所有學生rank資料
    public Cursor get_all_stu_rank(String table_name)
    {
        return db.rawQuery("SELECT * FROM " + table_name + " ORDER BY right_rate desc, right_num desc, Date desc", null);
    }

    //搜尋offline的資料表單where user_id
    public Cursor select_touch_poi(String s,String la,String lo)
    {

        return db.rawQuery("SELECT * FROM " + s +" WHERE la= \'"+ la +"\' AND lo= \'"+ lo +"\' ", null);
    }

    public Cursor get_poi_commnet(String table_name,String superkey)
    {
        return db.rawQuery("SELECT * FROM " + table_name +" WHERE super_key= \'"+ superkey +"\' ORDER BY Date desc", null);
    }

    public long InsertHomework_feedback(String user_answer,String user_id,String commend,String user_feedback_pic,String audio_fileName) {
        ContentValues args = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//日期
        args.put("user_answer", user_answer);
        args.put("user_id", user_id);
        args.put("commend", commend);
        args.put("user_feedback_pic", user_feedback_pic);
        args.put("user_feedback_audio", audio_fileName);
        args.put("status", "offline");
        args.put("Date", sdf.format(new Date()));
        return db.insert(TABLE_NAME, null, args);
    }
    public long Teacher_InsertHomework(String question,String shape) {
        ContentValues args = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//日期
        args.put("content", question);
        args.put("shape", shape);
        args.put("status", "offline");
        args.put("Date", sdf.format(new Date()));
        return db.insert(TABLE_NAME, null, args);
    }
    public long Insert_mark(String question_id,String user_answer,String user_id,String correct) {
        ContentValues args = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//日期
        args.put("question_id", question_id);
        args.put("user_answer", user_answer);
        args.put("mark_user", distance_MainActivity.user_id);
        args.put("user_id", user_id);
        args.put("correct", correct);
        args.put("status", "offline");
        args.put("Date", distance_MainActivity.user_id+sdf.format(new Date()));
        return db.insert(TABLE_NAME, null, args);
    }
    public long submit_homework2(String user_id,String key_date,String value1,String operand1,String value2,String ans,String object_pic,String user_calculate_pic, String shape,String object_hight,String object_width,String object_top_width,String correct,String select_input_style) {
        ContentValues args = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//日期
        args.put("user_id", user_id);
        args.put("key_date", key_date);
        args.put("value_1", value1);
        args.put("operand1", operand1);
        args.put("value_2", value2);
        args.put("operand2", "");
        args.put("value_3", "");
        args.put("operand3", "");
        args.put("value_4", "");
        args.put("ans", ans);
        args.put("object_pic", object_pic);
        args.put("user_calculate_pic", user_calculate_pic);
        args.put("shape", shape);
        args.put("status", "offline");
        args.put("Date", sdf.format(new Date()));
        args.put("object_hight", object_hight);
        args.put("object_width", object_width);
        args.put("object_top_width", object_top_width);
        args.put("correct", correct);
        args.put("select_formula", select_input_style);

        return db.insert(TABLE_NAME, null, args);
    }
    public long Insert_rank_test_record2(String user_id,String key_date,String value1,String operand1,String value2,String operand2,String value3,String ans,String object_pic,String user_calculate_pic, String shape,String object_hight,String object_width,String object_top_width,String correct,String select_input_style) {
        ContentValues args = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//日期
        args.put("user_id", user_id);
        args.put("key_date", key_date);
        args.put("value_1", value1);
        args.put("operand1", operand1);
        args.put("value_2", value2);
        args.put("operand2", operand2);
        args.put("value_3", value3);
        args.put("operand3", "");
        args.put("value_4", "");
        args.put("ans", ans);
        args.put("object_pic", object_pic);
        args.put("user_calculate_pic", user_calculate_pic);
        args.put("shape", shape);
        args.put("status", "offline");
        args.put("Date", sdf.format(new Date()));
        args.put("object_hight", object_hight);
        args.put("object_width", object_width);
        args.put("object_top_width", object_top_width);
        args.put("correct", correct);
        args.put("select_formula", select_input_style);

        return db.insert(TABLE_NAME, null, args);
    }


    public long Insert_rank_test_record3(String user_id,String key_date,String value1,String operand1,String value2,String operand2,String value3,String operand3,String value4,String ans,String object_pic,String user_calculate_pic, String shape,String object_hight,String object_width,String object_top_width,String correct,String select_input_style) {
        ContentValues args = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//日期
        args.put("user_id", user_id);
        args.put("key_date", key_date);
        args.put("value_1", value1);
        args.put("operand1", operand1);
        args.put("value_2", value2);
        args.put("operand2", operand2);
        args.put("value_3", value3);
        args.put("operand3", operand3);
        args.put("value_4", value4);
        args.put("ans", ans);
        args.put("object_pic", object_pic);
        args.put("user_calculate_pic", user_calculate_pic);
        args.put("shape", shape);
        args.put("status", "offline");
        args.put("Date", sdf.format(new Date()));
        args.put("object_hight", object_hight);
        args.put("object_width", object_width);
        args.put("object_top_width", object_top_width);
        args.put("correct", correct);
        args.put("select_formula", select_input_style);

        return db.insert(TABLE_NAME, null, args);
    }

    public long Insert_rank_test_list(String key_date,String total_num,String right_num,String error_num,String right_rate) {
        ContentValues args = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//日期
        args.put("key_date", key_date);
        args.put("total_num", total_num);
        args.put("right_num", right_num);
        args.put("error_num", error_num);
        args.put("right_rate", right_rate);
        args.put("status", "offline");
        args.put("Date", sdf.format(new Date()));

        return db.insert(TABLE_NAME, null, args);
    }

    public long InsertHomework(String question,String shape) {
        ContentValues args = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//日期
        args.put("content", question);
        args.put("shape", shape);
        args.put("Date", sdf.format(new Date()));
        return db.insert(TABLE_NAME, null, args);
    }
    public long InsertSubmit_Homework_third(String user_id,String question_id,String value1,String operand1,String value2,String operand2,String value3,String operand3,String value4,String ans,String object_pic,String user_calculate_pic, String user_answer_key,String object_hight,String object_width,String object_top_width) {
        ContentValues args = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//日期
        args.put("user_id", user_id);
        args.put("question_id", question_id);
        args.put("value_1", value1);
        args.put("operand1", operand1);
        args.put("value_2", value2);
        args.put("operand2", operand2);
        args.put("value_3", value3);
        args.put("operand3", operand3);
        args.put("value_4", value4);
        args.put("ans", ans);
        args.put("object_pic", object_pic);
        args.put("user_calculate_pic", user_calculate_pic);
        args.put("user_answer", user_answer_key);
        args.put("status", "offline");
        args.put("Date", sdf.format(new Date()));
        args.put("object_hight", object_hight);
        args.put("object_width", object_width);
        args.put("object_top_width", object_top_width);

        return db.insert(TABLE_NAME, null, args);
    }

    public long InsertSubmit_Homework_second(String user_id,String question_id,String value1,String operand1,String value2,String operand2,String value3 ,String ans,String object_pic,String user_calculate_pic, String user_answer_key,String object_hight,String object_width) {
        ContentValues args = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//日期
        args.put("user_id", user_id);
        args.put("question_id", question_id);
        args.put("value_1", value1);
        args.put("operand1", operand1);
        args.put("value_2", value2);
        args.put("operand2", operand2);
        args.put("value_3", value3);
        args.put("operand3", "");
        args.put("value_4", "");
        args.put("ans", ans);
        args.put("object_pic", object_pic);
        args.put("user_calculate_pic", user_calculate_pic);
        args.put("user_answer", user_answer_key);
        args.put("status", "offline");
        args.put("Date", sdf.format(new Date()));
        args.put("object_hight", object_hight);
        args.put("object_width", object_width);
        args.put("object_top_width", "");

        return db.insert(TABLE_NAME, null, args);
    }
    public long InsertSubmit_Homework_second2(String user_id,String question_id,String value1,String operand1,String value2,String operand2,String value3,String operand3,String value4,String ans,String object_pic,String user_calculate_pic, String user_answer_key,String object_hight,String object_width) {
        ContentValues args = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//日期
        args.put("user_id", user_id);
        args.put("question_id", question_id);
        args.put("value_1", value1);
        args.put("operand1", operand1);
        args.put("value_2", value2);
        args.put("operand2", operand2);
        args.put("value_3", value3);
        args.put("operand3", operand3);
        args.put("value_4", value4);
        args.put("operand4","");
        args.put("value_5", "");
        args.put("ans", ans);
        args.put("object_pic", object_pic);
        args.put("user_calculate_pic", user_calculate_pic);
        args.put("user_answer", user_answer_key);
        args.put("status", "offline");
        args.put("Date", sdf.format(new Date()));
        args.put("object_hight", object_hight);
        args.put("object_width", object_width);
        args.put("object_top_width", "");

        return db.insert(TABLE_NAME, null, args);
    }


    public long Insert_Action(String page,String action) {
        ContentValues args = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//日期
        args.put("page", page);
        args.put("action", action);
        args.put("status", "offline");
        args.put("Date", sdf.format(new Date()));
        return db.insert(TABLE_NAME, null, args);
    }

    public long see_record(String action) {
        ContentValues args = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//日期
        args.put("action", action);
        args.put("status", "offline");
        args.put("Date", sdf.format(new Date()));
        return db.insert(TABLE_NAME, null, args);
    }
    public long Insert_session_one_practice(String type) {
        ContentValues args = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//日期
        args.put("type", type);
        args.put("status", "offline");
        args.put("Date", sdf.format(new Date()));
        return db.insert(TABLE_NAME, null, args);
    }

    public long Insert_test(String test) {
        // TODO Auto-generated method stub
        ContentValues args = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//日期
        args.put("_TestID", "1");
        args.put("_Hw", "1");
        args.put("_YES", "2");
        args.put("_data", "2");
        return db.insert(TABLE_NAME, null, args);
    }

    public long InsertSubmit_Homework_one(String user_id,String question_id,String value1,String operand1,String value2,String ans,String object_pic,String user_calculate_pic, String user_answer_key,String object_hight,String object_width) {
        ContentValues args = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//日期
        args.put("user_id", user_id);
        args.put("question_id", question_id);
        args.put("value_1", value1);
        args.put("operand1", operand1);
        args.put("value_2", value2);
        args.put("operand2", "");
        args.put("value_3", "");
        args.put("operand3", "");
        args.put("value_4", "");
        args.put("ans", ans);
        args.put("object_pic", object_pic);
        args.put("user_calculate_pic", user_calculate_pic);
        args.put("user_answer", user_answer_key);
        args.put("status", "offline");
        args.put("Date", sdf.format(new Date()));
        args.put("object_hight", object_hight);
        args.put("object_width", object_width);
        args.put("object_top_width", "");

        return db.insert(TABLE_NAME, null, args);
    }

    public long InsertSubmit_Homework_one2(String user_id,String question_id,String value1,String operand1,String value2,String ans,String object_pic,String user_calculate_pic, String user_answer_key,String object_hight,String object_width) {
        ContentValues args = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//日期
        args.put("user_id", user_id);
        args.put("question_id", question_id);
        args.put("value_1", value1);
        args.put("operand1", operand1);
        args.put("value_2", value2);
        args.put("operand2", "");//0
        args.put("value_3", "");
        args.put("operand3", "");
        args.put("value_4", "");
        args.put("ans", ans);
        args.put("object_pic", object_pic);
        args.put("user_calculate_pic", user_calculate_pic);
        args.put("user_answer", user_answer_key);
        args.put("status", "offline");
        args.put("Date", sdf.format(new Date()));
        args.put("object_hight", object_hight);
        args.put("object_width", object_width);
        args.put("object_top_width", "");

        return db.insert(TABLE_NAME, null, args);
    }

    //修改使用者身高
    public void update_person_hight(String personhight)
    {

        ContentValues cv = new ContentValues();
        cv.put("personhight", personhight);
        String where = "_ID= ?";
        String[] whereValue ={"1"};
        db.update("personhight", cv, where, whereValue);
    }

    public void update_num(String key)
    {

        ContentValues cv = new ContentValues();
        cv.put("num", 1);
        String where = "_ID in (" + key + ")";
        db.update("practice_recorde_data_m", cv, where, null);

    }

    //新增一筆homework_question_list
    public long Download_homework_question_list(String content, String shape,String Date) {
        ContentValues args = new ContentValues();
        args.put("content", content);
        args.put("shape", shape);
        args.put("Date", Date);
        return db.insert(TABLE_NAME, null, args);
    }
    //新增一筆homework_question_list
    public long Download_others_marks_record(String question_id, String user_answer,String mark_user,String user_id,String correct,String Date) {
        ContentValues args = new ContentValues();
        args.put("question_id", question_id);
        args.put("user_answer", user_answer);
        args.put("mark_user", mark_user);
        args.put("user_id", user_id);
        args.put("correct", correct);
        args.put("status", "online");
        args.put("Date", Date);
        return db.insert(TABLE_NAME, null, args);
    }
    //新增一筆homework_question_list
    public long Download_poi_comment(String user, String comment,String super_key,String comment_key,String mark,String Date) {
        ContentValues args = new ContentValues();
        args.put("user", user);
        args.put("comment", comment);
        args.put("super_key", super_key);
        args.put("comment_key", comment_key);
        args.put("mark", mark);
        args.put("status", "online");
        args.put("Date", Date);
        return db.insert(TABLE_NAME, null, args);
    }
    //新增Download_homework_data
    public long Download_homework_data(String user_id, String question_id,String value_1,String operand1,String value_2,String operand2,String value_3,String operand3,String value_4,String ans,String object_pic,String user_calculate_pic,String user_answer,String Date,String object_hight,String object_width,String object_top_width) {
        ContentValues args = new ContentValues();
        args.put("user_id", user_id);
        args.put("question_id", question_id);
        args.put("value_1", value_1);
        args.put("operand1", operand1);
        args.put("value_2", value_2);
        args.put("operand2", operand2);
        args.put("value_3", value_3);
        args.put("operand3", operand3);
        args.put("value_4", value_4);
        args.put("ans", ans);
        args.put("object_pic", object_pic);
        args.put("user_calculate_pic", user_calculate_pic);
        args.put("user_answer", user_answer);
        args.put("Date", Date);
        args.put("object_hight", object_hight);
        args.put("object_width", object_width);
        args.put("object_top_width", object_top_width);
        args.put("status", "online");
        return db.insert(TABLE_NAME, null, args);
    }
    //新增Download_homework_feedback
    public long Download_homework_feedback(String user_answer, String user_id,String commend,String user_feedback_pic,String user_feedback_audio,String Date) {
        ContentValues args = new ContentValues();
        args.put("user_answer", user_answer);
        args.put("user_id", user_id);
        args.put("commend", commend);
        args.put("user_feedback_pic", user_feedback_pic);
        args.put("user_feedback_audio", user_feedback_audio);
        args.put("Date", Date);
        args.put("status", "online");
        return db.insert(TABLE_NAME, null, args);
    }
    //新增POI
    public long add_poi(String c_user,String title,Double la,Double lo,String shape,String value1,String value2, String value3,String object_pic,String position) {
        ContentValues args = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//日期
        String date = sdf.format(new Date());
        args.put("c_user", c_user);
        args.put("title", title);
        args.put("la", la);
        args.put("lo", lo);
        args.put("shape", shape);
        args.put("status", "offline");
        args.put("key_num", c_user+date);
        args.put("value_1", value1);
        args.put("value_2", value2);
        args.put("value_3", value3);
        args.put("object_pic", object_pic);
        args.put("Date", date);
        args.put("position", position);
        return db.insert(TABLE_NAME, null, args);
    }
    //新增POI
    public long add_poi_comment(String user,String comment,String super_key,String mark) {
        ContentValues args = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//日期
        String date = sdf.format(new Date());
        SimpleDateFormat sdf2 = new SimpleDateFormat("ddHHmmss");//日期
        String date2 = sdf.format(new Date());
        args.put("user", user);
        args.put("comment", comment);
        args.put("super_key", super_key);
        args.put("comment_key", user+date2+super_key);
        args.put("mark", mark);
        args.put("status", "offline");
        args.put("Date", date);
        return db.insert(TABLE_NAME, null, args);
    }

    //新增POI
    public long download_poi(String c_user,String title,Double la,Double lo,String shape,String key_num,String value_1,String value_2,String value_3,String object_pic,String position,String Date) {
        ContentValues args = new ContentValues();
        args.put("c_user", c_user);
        args.put("title", title);
        args.put("la", la);
        args.put("lo", lo);
        args.put("shape", shape);
        args.put("status", "online");
        args.put("key_num", key_num);
        args.put("value_1", value_1);
        args.put("value_2", value_2);
        args.put("value_3", value_3);
        args.put("object_pic", object_pic);
        args.put("position", position);
        args.put("Date", Date);
        return db.insert(TABLE_NAME, null, args);
    }
    //修改onlie
    public void update(String whereid,String table_name)
    {
        String wherefield="";
        if(table_name.equals("teacher_sent_question_list"))
            wherefield="Date";
        else if(table_name.equals("homework_data"))
            wherefield="user_answer";
        else if(table_name.equals("homework_feedback"))
            wherefield="user_feedback_pic";
        else if(table_name.equals("rank_allstu"))
            wherefield="user_id";
        else if(table_name.equals("practice_recorde_data"))
            wherefield="user_answer";
        else if(table_name.equals("practice_recorde_data_m"))
            wherefield="user_answer";
        else if(table_name.equals("rank_record_list"))
            wherefield="key_date";
        else if(table_name.equals("rank_record"))
            wherefield="Date";
        else if(table_name.equals("mark_others_homework"))
            wherefield="Date";
        else if(table_name.equals("record_session_one_practice"))
            wherefield="Date";
        else if(table_name.equals("rmap_poi"))
            wherefield="key_num";
        else if(table_name.equals("user_permission"))
            wherefield="_ID";
        else if(table_name.equals("poi_comment"))
            wherefield="comment_key";


        ContentValues cv = new ContentValues();
        cv.put("status", "online");
        String where = wherefield + " = ?";
        String[] whereValue ={whereid};
        db.update(table_name, cv, where, whereValue);
    }
    public void update_upload_sqlite(String now_date)
    {
        String wherefield="_ID";

        ContentValues cv = new ContentValues();
        cv.put("upload_sqlite", now_date);
        String where = wherefield + " = ?";
        String[] whereValue ={"1"};
        db.update("user_permission", cv, where, whereValue);
    }
    public void update_rank(String whereid,String count_total_test,String count_right_num,String Correct_rate)
    {
        String wherefield="user_id";

        ContentValues cv = new ContentValues();
        cv.put("total_num", count_total_test);
        cv.put("right_num", count_right_num);
        cv.put("right_rate", Correct_rate);
        cv.put("status", "offline");
        String where = wherefield + " = ?";
        String[] whereValue ={whereid};
        db.update("rank_allstu", cv, where, whereValue);
    }
    public void update_all_stu_rank(String whereid,String count_total_test,String count_right_num,String Correct_rate,String Date)
    {
        String wherefield="user_id";

        ContentValues cv = new ContentValues();
        cv.put("total_num", count_total_test);
        cv.put("right_num", count_right_num);
        cv.put("right_rate", Correct_rate);
        cv.put("Date", Date);
        String where = wherefield + " = ?";
        String[] whereValue ={whereid};
        db.update("rank_allstu", cv, where, whereValue);
    }
    public void refresh_user_updatetime(String type)
    {
        ContentValues cv = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//日期
        String date = sdf.format(new Date());
        if(type.equals("upload"))
        {
            cv.put("upload_time", date);
        }else if(type.equals("download"))
        {
            cv.put("download_time", date);
        }
        String where = "_ID = ?";
        String[] whereValue ={"1"};
        db.update("user_permission", cv, where, whereValue);

        ContentValues cv2 = new ContentValues();
        cv2.put("status", "offline");
        db.update("user_permission", cv2, where, whereValue);
    }
    public void update_version(String version)
    {
        ContentValues cv = new ContentValues();
        cv.put("permission", version);
        String where = "_ID = ?";
        String[] whereValue ={"1"};
        Log.i("@@@@@@@@@@@", "version");
        db.update("user_permission", cv, where, whereValue);
    }
    public void close_db()
    {
        if(db!=null)
        {
            db=null;
            db.close();
        }

    }
}
