package lab.hwang.ug;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class see_others_comment extends Activity {
    //class
    private SQLite dbHelper;//操作SQLite資料庫


    //object
    ListView feedback_list;
    ImageView feedback_process,stu_calculate_process,stu_object,back_home_mess;
    TextView comment_tv,width_value,hight_value,stu_num,top_width_value;
    Button listen_feedback_audio,give_feedback_bt,home_bt;

    //變數
    String user_id[],commend[],user_feedback_pic[],date[],show_all_comment[],user_feedback_audio[],showdate[];
    String temp_show_large_process;
    File temp_audio_file;
    AbsoluteLayout feedback_bg;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_others_comment);
        findview();
        Animation myAnimation_Alpha=new AlphaAnimation(1.0f, 0.0f);
        myAnimation_Alpha.setDuration(2500);
        myAnimation_Alpha.setFillAfter(true);
        back_home_mess.setAnimation(myAnimation_Alpha);
        Create_feedback_list();//開啟作業的list

        bitmap = BitmapFactory.decodeFile("/sdcard/file_data/"+homework_list.temp_object_path);
        stu_object.setImageBitmap(bitmap);

        bitmap = BitmapFactory.decodeFile("/sdcard/file_data/"+homework_list.temp_process_path);
        stu_calculate_process.setImageBitmap(bitmap);
        if(homework_list.select_shap.equals("trapezoidal"))
        {
            feedback_bg.setBackgroundResource(R.drawable.feedback_bg2);
            top_width_value.setVisibility(View.VISIBLE);
            top_width_value.setText(homework_list.temp_object_top_width+" (m)");
        }
        comment_tv.setText(show_all_comment[0]);
        stu_num.setText(user_id[0]);
        bitmap = BitmapFactory.decodeFile("/sdcard/file_data/"+user_feedback_pic[0]);
        feedback_process.setImageBitmap(bitmap);
        temp_show_large_process=user_feedback_pic[0];

        width_value.setText(homework_list.temp_object_width+" (m)");
        hight_value.setText(homework_list.temp_object_hight+" (m)");

        //假設有錄音檔就顯示button
        if(!(user_feedback_audio[0].equals("null")))
        {
            listen_feedback_audio.setVisibility(View.VISIBLE);
            temp_audio_file=new File("/sdcard/file_data/audio/"+user_feedback_audio[0]);;
        }else
        {
            listen_feedback_audio.setVisibility(View.INVISIBLE);
        }

        stu_object.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
      		/*
        	dbHelper = new SQLite(see_others_comment.this, "record_action");
			dbHelper.Insert_Action(login.see_others_comments_page,"See_large_object");
			dbHelper.close();//關閉資料庫
      		*/
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("from", "homework");
                bundle.putString("path", homework_list.temp_object_path);
                intent.putExtras(bundle);
                intent.setClass(see_others_comment.this, show_large_image.class);
                startActivity(intent);
            }
        });
        stu_calculate_process.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
        		/*
        		dbHelper = new SQLite(see_others_comment.this, "record_action");
    			dbHelper.Insert_Action(login.see_others_comments_page,"See_large_original_process");
    			dbHelper.close();//關閉資料庫
        		*/

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("from", "homework");
                bundle.putString("path", homework_list.temp_process_path);
                intent.putExtras(bundle);
                intent.setClass(see_others_comment.this, show_large_image.class);
                startActivity(intent);
            }
        });
        feedback_process.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
	      		/*
	      		dbHelper = new SQLite(see_others_comment.this, "record_action");
    			dbHelper.Insert_Action(login.see_others_comments_page,"See_large_feedback_process");
    			dbHelper.close();//關閉資料庫
	      		*/
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("from", "homework");
                bundle.putString("path", temp_show_large_process);
                intent.putExtras(bundle);
                intent.setClass(see_others_comment.this, show_large_image.class);
                startActivity(intent);
            }
        });
        listen_feedback_audio.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dbHelper = new SQLite(see_others_comment.this, "record_action");
                dbHelper.Insert_Action(Login.see_others_comments_page,"聆聽回饋錄音");
                dbHelper.close();//關閉資料庫

                Open_Audio_File(temp_audio_file);
            }
        });
        give_feedback_bt.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
	      		/*
		      		dbHelper = new SQLite(see_others_comment.this, "record_action");
	    			dbHelper.Insert_Action(login.see_others_comments_page,"Want_comment_To_comment_others");
	    			dbHelper.close();//關閉資料庫
	      		*/
                Intent intent = new Intent();
                intent.setClass(see_others_comment.this, comment_others_homework_area.class);
                startActivity(intent);
                see_others_comment.this.finish();
            }
        });
        home_bt.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
		   		/*
		   		dbHelper = new SQLite(see_others_comment.this, "record_action");
    			dbHelper.Insert_Action(login.see_others_comments_page,"Back_to_homework_list");
    			dbHelper.close();//關閉資料庫
		   		*/
                see_others_comment.this.finish();
            }
        });
    }

    private void findview() {
        feedback_bg=(AbsoluteLayout)findViewById(R.id.feedback_bg);
        feedback_list = (ListView) findViewById(R.id.feedback_listview);
        feedback_process = (ImageView) findViewById(R.id.feedback_process);
        stu_calculate_process = (ImageView) findViewById(R.id.stu_calculate_process);
        stu_object = (ImageView) findViewById(R.id.stu_object);
        comment_tv = (TextView) findViewById(R.id.comment);
        listen_feedback_audio= (Button) findViewById(R.id.listen_feedback_audio);
        give_feedback_bt= (Button) findViewById(R.id.give_feedback_bt);
        width_value= (TextView) findViewById(R.id.width_value);
        top_width_value= (TextView) findViewById(R.id.top_width_value);
        hight_value= (TextView) findViewById(R.id.hight_value);
        stu_num= (TextView) findViewById(R.id.stu_num);

        back_home_mess= (ImageView) findViewById(R.id.back_home_mess);
        home_bt= (Button) findViewById(R.id.home_bt);

    }
    public void Open_Audio_File(File f) //撥放音樂檔
    {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(f), "audio");
        startActivity(intent);
    }
    private void Create_feedback_list(){
        // TODO Auto-generated method stub
        Cursor cursor = Get_feedback_listCursor("homework_feedback");

        if(cursor!=null) {
            Show_feedback_list_content(cursor);//將sqlite值傳送到list中
            cursor.close();		//關閉Cursor
            dbHelper.close();	//關閉資料庫，釋放記憶體
        }
    }

    //取得表單中，homework_list資料
    private Cursor Get_feedback_listCursor(String table_name) {
        // TODO Auto-generated method stub

        dbHelper = new SQLite(see_others_comment.this, table_name);
        Cursor cursor = dbHelper.get_feedback_list_item(table_name,homework_list.temp_answer_key);
        int rows_num = cursor.getCount();	//取得資料表列數

        if(rows_num==0)
        {
            Toast.makeText(getApplicationContext(),	"沒有資料,無法開啟", Toast.LENGTH_SHORT).show();
            return null;
        }
        return cursor;
    }

    private void Show_feedback_list_content(Cursor cursor) {
        // TODO Auto-generated method stub
        //把資料加入ArrayList中

        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
        user_id= new String[cursor.getCount()];
        commend= new String[cursor.getCount()];
        user_feedback_pic= new String[cursor.getCount()];
        date= new String[cursor.getCount()];
        show_all_comment= new String[cursor.getCount()];
        user_feedback_audio= new String[cursor.getCount()];
        showdate= new String[cursor.getCount()];
        if(cursor !=null){
            int i = 0;
            while(cursor.moveToNext()){
                user_id[i]=cursor.getString(2);
                if(cursor.getString(3).length()>5)
                {
                    commend[i]=cursor.getString(3).substring(0, 5)+".....";
                }else
                {
                    commend[i]=cursor.getString(3);
                }
                show_all_comment[i]=cursor.getString(3);
                user_feedback_pic[i]=cursor.getString(4);
                user_feedback_audio[i]=cursor.getString(5);
                date[i]=cursor.getString(7);
                showdate[i]=cursor.getString(7).substring(4, 6)+"月"+cursor.getString(7).substring(6, 8)+"日   "+cursor.getString(7).substring(8, 10)+":"+cursor.getString(7).substring(10, 12)+":"+cursor.getString(7).substring(12, 14);
                i=i+1;
            };
        }

        if(cursor !=null){
            int i = 0;
            cursor.moveToPosition(-1);
            while(cursor.moveToNext()){
                HashMap<String,String> item = new HashMap<String,String>();
                if(distance_MainActivity.user_id.equals(user_id[i]))
                {
                    item.put("img", String.valueOf(R.drawable.stu_icon_self));
                }else
                {
                    item.put("img", String.valueOf(R.drawable.stu_icon));
                }
                item.put("user_id", user_id[i]);
                item.put("commend", commend[i]);
                item.put("date", showdate[i]);
                list.add( item );
                i=i+1;
            };
        }

        SimpleAdapter feedback_adapter = new SimpleAdapter( this, list, R.layout.see_others_listview, new String[] {"img","user_id","commend","date"}, new int[] { R.id.stu_icon,R.id.stu_number, R.id.stu_comment, R.id.stu_time} );
        feedback_list.setAdapter( feedback_adapter );


        //註冊 ListView 被點擊的 onClick 函式
        feedback_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
                                    long arg3) {

                dbHelper = new SQLite(see_others_comment.this, "record_action");
                dbHelper.Insert_Action(Login.see_others_comments_page,"觀看其他人的回饋");
                dbHelper.close();//關閉資料庫

                comment_tv.setText(show_all_comment[arg2]);
                stu_num.setText(user_id[arg2]);
                bitmap = BitmapFactory.decodeFile("/sdcard/file_data/"+user_feedback_pic[arg2]);
                feedback_process.setImageBitmap(bitmap);
                temp_show_large_process=user_feedback_pic[arg2];

                //假設有錄音檔就顯示button
                if(!(user_feedback_audio[arg2].equals("null")))
                {
                    listen_feedback_audio.setVisibility(View.VISIBLE);
                    temp_audio_file=new File("/sdcard/file_data/audio/"+user_feedback_audio[arg2]);;
                }else
                {
                    listen_feedback_audio.setVisibility(View.INVISIBLE);
                }
            }

        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(see_others_comment.this)
                    .setTitle("警告")
                    .setMessage("確定要離開嗎?")
                    .setPositiveButton("確認",new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which)
                        {
	   	            	 /*
	   	            	dbHelper = new SQLite(see_others_comment.this, "record_action");
	   	    			dbHelper.Insert_Action(login.see_others_comments_page,"Back_to_homework_list");
	   	    			dbHelper.close();//關閉資料庫
	   	    			*/
                            see_others_comment.this.finish();
                        }
                    })
                    .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {

                        }
                    })
                    .show();
            return true;
        }
        return false;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            if(bitmap.isRecycled()==false)
                bitmap.recycle();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        System.gc();
    }
}
