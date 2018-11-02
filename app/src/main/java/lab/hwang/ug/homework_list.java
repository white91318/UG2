package lab.hwang.ug;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class homework_list extends Activity {
    //class
    private SQLite dbHelper;//操作SQLite資料庫

    public static String question_id;//紀錄使用者點選哪個question_id (=question_list Date)

    ListView All_homework_listview,submit_stu_listview;
    SimpleAdapter homework_adapter,stu_adapter;


    //homework_listView
    String content[],Date[],shap[],showdate[],chshap[];
    String stu[],stu_time[],stu_object_pic[],stu_calculate_process[],stu_object_hight[],stu_object_width[],
            stu_calculate_value1[],stu_calculate_operand1[],stu_calculate_value2[],stu_calculate_operand2[],
            stu_calculate_value3[],stu_calculate_operand3[],stu_calculate_value4[],stu_calculate_ans[],
            stu_answer_key[],showdate2[],stu_object_top_width[];

    List<String> homework_listViewId = new LinkedList<String>();
    //temp click var
    int click_list,click_stu;


    //mark
    String M_correct[];
    ImageView mark_homework,show_mark,see_others_mark;
    Boolean mark_process=false;

    //object
    TextView question_content,stu_number,stu_value_process,hight_value,width_value,width_top_value;
    ImageView object_pic,stu_process,back_home_mess;
    Button want_comment,see_others_comment,home_bt;
    Bitmap bitmap;

    AbsoluteLayout layout_bg;
    //變數
    public static String temp_question_content;
    public static String temp_object_path,temp_process_path,temp_object_hight,temp_object_width,temp_answer_key,temp_object_top_width,temp_stu,temp_process;
    public static String select_shap;
    String temp_click_user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homework_list);
        findview();
        Create_homework_list();//開啟作業的list
        Bundle bunde = this.getIntent().getExtras();
        String user_answer = bunde.getString("user_answer");

        Animation myAnimation_Alpha=new AlphaAnimation(1.0f, 0.0f);
        myAnimation_Alpha.setDuration(2500);
        myAnimation_Alpha.setFillAfter(true);
        back_home_mess.setAnimation(myAnimation_Alpha);

        Log.i("@@@@userid", distance_MainActivity.user_id);
        if(!(user_answer.equals("null")) && (select_shap.equals("rectangle") || select_shap.equals("parallel")))
        {
            String user_id = bunde.getString("user_id");
            String question_id = bunde.getString("question_id");
            String value_1 = bunde.getString("value_1");
            String operand1 = bunde.getString("operand1");
            String value_2 = bunde.getString("value_2");
            String ans = bunde.getString("ans");
            String object_pic_path = bunde.getString("object_pic");
            String user_calculate_pic = bunde.getString("user_calculate_pic");
            String object_hight = bunde.getString("object_hight");
            String object_width = bunde.getString("object_width");
            if(Double.valueOf(value_1) % 2==0||Double.valueOf(value_1) % 2==1)
            {
                value_1=String.valueOf(Double.valueOf(value_1).intValue());
            }
            if(Double.valueOf(value_2) % 2==0||Double.valueOf(value_2) % 2==1)
            {
                value_2=String.valueOf(Double.valueOf(value_2).intValue());
            }
            if(Double.valueOf(ans) % 2==0||Double.valueOf(ans) % 2==1)
            {
                ans=String.valueOf(Double.valueOf(ans).intValue());
            }
            Create_homework_sut_submit_list(question_id);
            temp_process=value_1+"  "+operand1+"  "+value_2+"  =  "+ ans;
            question_content.setText("題目:"+temp_question_content);
            stu_number.setText("學生:"+user_id);
            stu_value_process.setText("計算過程:"+"  "+temp_process);
            hight_value.setText(object_hight+" (cm)");
            width_value.setText(object_width+" (cm)");

            bitmap = BitmapFactory.decodeFile("/sdcard/file_data/"+object_pic_path);
            object_pic.setImageBitmap(bitmap);
            bitmap = BitmapFactory.decodeFile("/sdcard/file_data/"+user_calculate_pic);
            stu_process.setImageBitmap(bitmap);

            //暫存放大圖的路徑
            temp_object_path=object_pic_path;
            temp_process_path=user_calculate_pic;
            temp_object_hight=object_hight;
            temp_object_width=object_width;
            temp_answer_key=user_answer;
            temp_stu=user_id;

            show_have_list_bg_done_homework();
        }else if (!(user_answer.equals("null")) && (select_shap.equals("triangle") || select_shap.equals("diamond")))
        {
            String user_id = bunde.getString("user_id");
            String question_id = bunde.getString("question_id");
            String value_1 = bunde.getString("value_1");
            String operand1 = bunde.getString("operand1");
            String value_2 = bunde.getString("value_2");
            String operand2 = bunde.getString("operand2");
            String value_3 = bunde.getString("value_3");
            String ans = bunde.getString("ans");
            String object_pic_path = bunde.getString("object_pic");
            String user_calculate_pic = bunde.getString("user_calculate_pic");
            String object_hight = bunde.getString("object_hight");
            String object_width = bunde.getString("object_width");

            if(Double.valueOf(value_1) % 2==0||Double.valueOf(value_1) % 2==1)
            {
                value_1=String.valueOf(Double.valueOf(value_1).intValue());
            }
            if(Double.valueOf(value_2) % 2==0||Double.valueOf(value_2) % 2==1)
            {
                value_2=String.valueOf(Double.valueOf(value_2).intValue());
            }
            if(Double.valueOf(value_3) % 2==0||Double.valueOf(value_3) % 2==1)
            {
                value_3=String.valueOf(Double.valueOf(value_3).intValue());
            }
            if(Double.valueOf(ans) % 2==0||Double.valueOf(ans) % 2==1)
            {
                ans=String.valueOf(Double.valueOf(ans).intValue());
            }

            Create_homework_sut_submit_list(question_id);
            temp_process=value_1+"  "+operand1+"  "+value_2+"  "+operand2+"  "+value_3+"  =  "+ ans;
            question_content.setText("題目:"+temp_question_content);
            stu_number.setText("學生:"+user_id);
            stu_value_process.setText("計算過程:"+"  "+temp_process);
            hight_value.setText(object_hight+" (cm)");
            width_value.setText(object_width+" (cm)");
            temp_stu=user_id;

            bitmap = BitmapFactory.decodeFile("/sdcard/file_data/"+object_pic_path);
            object_pic.setImageBitmap(bitmap);
            bitmap = BitmapFactory.decodeFile("/sdcard/file_data/"+user_calculate_pic);
            stu_process.setImageBitmap(bitmap);

            //暫存放大圖的路徑
            temp_object_path=object_pic_path;
            temp_process_path=user_calculate_pic;
            temp_object_hight=object_hight;
            temp_object_width=object_width;
            temp_answer_key=user_answer;
            show_have_list_bg_done_homework();
        }else if(!(user_answer.equals("null")) && (select_shap.equals("trapezoidal")))
        {
            String user_id = bunde.getString("user_id");
            String question_id = bunde.getString("question_id");
            String value_1 = bunde.getString("value_1");
            String operand1 = bunde.getString("operand1");
            String value_2 = bunde.getString("value_2");
            String operand2 = bunde.getString("operand2");
            String value_3 = bunde.getString("value_3");
            String operand3 = bunde.getString("operand3");
            String value_4 = bunde.getString("value_4");
            String ans = bunde.getString("ans");
            String object_pic_path = bunde.getString("object_pic");
            String user_calculate_pic = bunde.getString("user_calculate_pic");
            String object_hight = bunde.getString("object_hight");
            String object_top_hight=bunde.getString("object_top_width");
            String object_width = bunde.getString("object_width");


            if(Double.valueOf(value_1) % 2==0||Double.valueOf(value_1) % 2==1)
            {
                value_1=String.valueOf(Double.valueOf(value_1).intValue());
            }
            if(Double.valueOf(value_2) % 2==0||Double.valueOf(value_2) % 2==1)
            {
                value_2=String.valueOf(Double.valueOf(value_2).intValue());
            }
            if(Double.valueOf(value_3) % 2==0||Double.valueOf(value_3) % 2==1)
            {
                value_3=String.valueOf(Double.valueOf(value_3).intValue());
            }
            if(Double.valueOf(value_4) % 2==0||Double.valueOf(value_4) % 2==1)
            {
                value_4=String.valueOf(Double.valueOf(value_4).intValue());
            }
            if(Double.valueOf(ans) % 2==0||Double.valueOf(ans) % 2==1)
            {
                ans=String.valueOf(Double.valueOf(ans).intValue());
            }

            Create_homework_sut_submit_list(question_id);
            temp_process="( "+value_1+"  "+operand1+"  "+value_2+" )  "+operand2+"  "+value_3+"   "+operand3+"  "+value_4+"  =  "+ ans;
            question_content.setText("題目:"+temp_question_content);
            stu_number.setText("學生:"+user_id);
            stu_value_process.setText("計算過程: "+temp_process);
            hight_value.setText(object_hight+" (cm)");
            width_value.setText(object_width+" (cm)");
            width_top_value.setText(object_top_hight+" (cm)");
            temp_stu=user_id;

            bitmap = BitmapFactory.decodeFile("/sdcard/file_data/"+object_pic_path);
            object_pic.setImageBitmap(bitmap);
            bitmap = BitmapFactory.decodeFile("/sdcard/file_data/"+user_calculate_pic);
            stu_process.setImageBitmap(bitmap);

            //暫存放大圖的路徑
            temp_object_path=object_pic_path;
            temp_process_path=user_calculate_pic;
            temp_object_hight=object_hight;
            temp_object_width=object_width;
            temp_object_top_width=object_top_hight;
            temp_answer_key=user_answer;
            show_have_list_bg_done_homework();
            layout_bg.setBackgroundResource(R.drawable.homework_list_bg2);
            width_top_value.setVisibility(View.VISIBLE);
        }
        else
        {
            show_ini_bg();
        }

        object_pic.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
        		/*
        		dbHelper = new SQLite(homework_list.this, "record_action");
  				dbHelper.Insert_Action(login.homework_page,"See_large_object");
  				dbHelper.close();//關閉資料庫
  				*/

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("from", "homework");
                bundle.putString("path", temp_object_path);
                intent.putExtras(bundle);
                intent.setClass(homework_list.this, show_large_image.class);
                startActivity(intent);
            }
        });
        stu_process.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
        		/*
        		dbHelper = new SQLite(homework_list.this, "record_action");
  				dbHelper.Insert_Action(login.homework_page,"See_large_process");
  				dbHelper.close();//關閉資料庫
        		*/

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("from", "homework");
                bundle.putString("path", temp_process_path);
                intent.putExtras(bundle);
                intent.setClass(homework_list.this, show_large_image.class);
                startActivity(intent);
            }
        });
        want_comment.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
        		/*
        		dbHelper = new SQLite(homework_list.this, "record_action");
  				dbHelper.Insert_Action(login.homework_page,"Want_comment_To_comment_others");
  				dbHelper.close();//關閉資料庫
        		*/

                Intent intent = new Intent();
                intent.setClass(homework_list.this, comment_others_homework_area.class);
                startActivity(intent);
            }
        });
        see_others_mark.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dbHelper = new SQLite(homework_list.this, "record_action");
                dbHelper.Insert_Action(Login.homework_page,"觀看其他人的批閱");
                dbHelper.close();//關閉資料庫


                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("user_answer",temp_answer_key);
                intent.putExtras(bundle);
                intent.setClass(homework_list.this, show_others_marks.class);
                startActivity(intent);
            }
        });
        mark_homework.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                new AlertDialog.Builder(homework_list.this)//選擇顯示圖示
                        .setTitle("批閱")
                        .setItems(new String[] { "我覺得這題是正確的","我覺得這題是錯誤的"},
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int whichcountry)
                                    {
                                        switch (whichcountry) {
                                            case 0:
                                                new AlertDialog.Builder(homework_list.this)
                                                        .setTitle("確認")
                                                        .setMessage("你認為這份作業是正確的")
                                                        .setPositiveButton("確認",new DialogInterface.OnClickListener(){
                                                            public void onClick(DialogInterface dialog, int which)
                                                            {
		   	            	 /*
		   	            	dbHelper = new SQLite(homework_list.this, "record_action");
	          				dbHelper.Insert_Action(login.homework_page,"mark_homework:"+stu[click_stu]);
	          				dbHelper.close();//關閉資料庫
	              			*/

                                                                dbHelper = new SQLite(homework_list.this, "mark_others_homework");
                                                                dbHelper.Insert_mark(Date[click_list],temp_answer_key,stu[click_stu],"right");
                                                                dbHelper.close();//關閉資料庫
                                                                mark_process=true;
                                                                Create_homework_sut_submit_list(Date[click_list]);
                                                                mark_homework.setVisibility(View.INVISIBLE);
                                                                show_mark.setImageResource(R.drawable.mark_o);
                                                                show_mark.setVisibility(View.VISIBLE);
                                                            }
                                                        })
                                                        .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which)
                                                            {

                                                            }
                                                        })
                                                        .show();
                                                break;
                                            case 1:
                                                new AlertDialog.Builder(homework_list.this)
                                                        .setTitle("確認")
                                                        .setMessage("你認為這份作業是錯誤的")
                                                        .setPositiveButton("確認",new DialogInterface.OnClickListener(){
                                                            public void onClick(DialogInterface dialog, int which)
                                                            {
		   	            	 /*
		   	            	dbHelper = new SQLite(homework_list.this, "record_action");
	          				dbHelper.Insert_Action(login.homework_page,"mark_homework:"+stu[click_stu]);
	          				dbHelper.close();//關閉資料庫
	              			*/

                                                                dbHelper = new SQLite(homework_list.this, "mark_others_homework");
                                                                dbHelper.Insert_mark(Date[click_list],temp_answer_key,stu[click_stu],"error");
                                                                dbHelper.close();//關閉資料庫
                                                                mark_process=true;
                                                                Create_homework_sut_submit_list(Date[click_list]);
                                                                mark_homework.setVisibility(View.INVISIBLE);
                                                                show_mark.setImageResource(R.drawable.mark_x);
                                                                show_mark.setVisibility(View.VISIBLE);
                                                            }
                                                        })
                                                        .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int which)
                                                            {

                                                            }
                                                        })
                                                        .show();

                                                break;
                                        }
                                    }
                                }).show();
            }
        });
        see_others_comment.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                dbHelper = new SQLite(homework_list.this, "homework_feedback");
                Cursor cursor = dbHelper.get_feedback_list_item("homework_feedback",temp_answer_key);
                int rows_num = cursor.getCount();	//取得資料表列數
                cursor.close();
                dbHelper.close();

                if(rows_num==0)
                {
                    new AlertDialog.Builder(homework_list.this)
                            .setTitle("觀看其他人意見")
                            .setMessage("目前沒有任何人給予意見，你想給予"+temp_click_user_id+"一些意見嗎?")
                            .setPositiveButton("確認",new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int which)
                                {
   	            	 /*
   	            	dbHelper = new SQLite(homework_list.this, "record_action");
   	  				dbHelper.Insert_Action(login.homework_page,"Want_comment_To_comment_others");
   	  				dbHelper.close();//關閉資料庫
   	  				*/

                                    dbHelper = new SQLite(homework_list.this, "record_action");
                                    dbHelper.Insert_Action(Login.homework_page,"觀看其他人的回饋");
                                    dbHelper.close();//關閉資料庫

                                    Intent intent = new Intent();
                                    intent.setClass(homework_list.this, comment_others_homework_area.class);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which)
                                {

                                }
                            })
                            .show();

                }else
                {
	      			/*
	      			dbHelper = new SQLite(homework_list.this, "record_action");
	  				dbHelper.Insert_Action(login.homework_page,"See_others_comments");
	  				dbHelper.close();//關閉資料庫
	      			*/

                    Intent intent = new Intent();
                    intent.setClass(homework_list.this, see_others_comment.class);
                    startActivity(intent);
                }
            }
        });
        home_bt.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
        		/*
        		dbHelper = new SQLite(homework_list.this, "record_action");
    			dbHelper.Insert_Action(login.homework_page,"Back_To_MainActivity");
    			dbHelper.close();//關閉資料庫
    			*/

                homework_list.this.finish();
            }
        });
    }

    private void findview() {
        layout_bg = (AbsoluteLayout)findViewById(R.id.layout_bg);
        All_homework_listview = (ListView) findViewById(R.id.homework_listview);
        submit_stu_listview = (ListView) findViewById(R.id.submit_homework_stu_listview);

        width_top_value= (TextView) findViewById(R.id.width_top_value);
        question_content= (TextView) findViewById(R.id.click_question_content);
        stu_number= (TextView) findViewById(R.id.click_stu_number);
        stu_value_process= (TextView) findViewById(R.id.click_stu_value_process);
        hight_value = (TextView) findViewById(R.id.hight_value);
        width_value= (TextView) findViewById(R.id.width_value);
        object_pic= (ImageView) findViewById(R.id.click_object_pic);
        back_home_mess= (ImageView) findViewById(R.id.back_home_mess);
        stu_process= (ImageView) findViewById(R.id.click_stu_process);
        want_comment =(Button) findViewById(R.id.click_want_comment);
        see_others_comment =(Button) findViewById(R.id.click_see_others_comment);
        mark_homework=(ImageView) findViewById(R.id.mark_homework);
        show_mark=(ImageView) findViewById(R.id.show_mark);
        see_others_mark=(ImageView) findViewById(R.id.see_others_mark);
        home_bt=(Button) findViewById(R.id.home_bt);

    }

    //開啟homework_list
    private void Create_homework_list() {
        // TODO Auto-generated method stub
        Cursor cursor = Get_homework_listCursor("homework_question_list");

        if(cursor!=null) {
            Show_homework_list_content(cursor);//將sqlite值傳送到list中
            cursor.close();		//關閉Cursor
            dbHelper.close();	//關閉資料庫，釋放記憶體
        }
    }

    //取得表單中，homework_list資料
    private Cursor Get_homework_listCursor(String table_name) {
        // TODO Auto-generated method stub

        dbHelper = new SQLite(homework_list.this, table_name);
        Cursor cursor = dbHelper.get_all_homework_list_item(table_name);
        int rows_num = cursor.getCount();	//取得資料表列數
        if(rows_num==0)
        {
            Toast.makeText(getApplicationContext(),	"沒有資料,無法開啟", Toast.LENGTH_SHORT).show();
            dbHelper.close();
            return null;
        }
        return cursor;
    }
    private void Show_homework_list_content(Cursor cursor) {
        // TODO Auto-generated method stub
        //把資料加入ArrayList中

        if(homework_listViewId != null)
            homework_listViewId.clear();
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
        content = new String[cursor.getCount()];
        shap = new String[cursor.getCount()];
        Date = new String[cursor.getCount()];
        chshap= new String[cursor.getCount()];
        showdate= new String[cursor.getCount()];
        if(cursor !=null){
            int i = 0;
            while(cursor.moveToNext()){
                content[i]=cursor.getString(1);
                shap[i]=cursor.getString(2);
                if(shap[i].equals("rectangle"))
                {
                    chshap[i]="(矩形)";
                }else if(shap[i].equals("triangle"))
                {
                    chshap[i]="(三角形)";
                }else if(shap[i].equals("trapezoidal"))
                {
                    chshap[i]="(梯形)";
                }else if(shap[i].equals("parallel"))
                {
                    chshap[i]="(平行四邊形)";
                }else if(shap[i].equals("diamond"))
                {
                    chshap[i]="(菱形)";
                }
                Date[i]=cursor.getString(3);
                showdate[i]=cursor.getString(3).substring(4, 6)+"月"+cursor.getString(3).substring(6, 8)+"日   "+cursor.getString(3).substring(8, 10)+":"+cursor.getString(3).substring(10, 12)+":"+cursor.getString(3).substring(12, 14);
                i=i+1;
            };
        }

        if(cursor !=null){
            int i = 0;
            cursor.moveToPosition(-1);
            while(cursor.moveToNext()){
                HashMap<String,String> item = new HashMap<String,String>();
                item.put("content", content[i]);
                item.put("date", chshap[i]+"   "+showdate[i]);
                list.add( item );
                i=i+1;
            };
        }

        homework_adapter = new SimpleAdapter( this, list, R.layout.homework_showlist, new String[] {"content","date"}, new int[] {R.id.text1, R.id.text2} );
        All_homework_listview.setAdapter( homework_adapter );


        //註冊 ListView 被點擊的 onClick 函式
        All_homework_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
                                    long arg3) {
				/*
				dbHelper = new SQLite(homework_list.this, "record_action");
  				dbHelper.Insert_Action(login.homework_page,"Click_homework:"+content[arg2]);
  				dbHelper.close();//關閉資料庫
  				*/

                click_list=arg2;
                temp_question_content=content[arg2];
                //question_content.setText("題目:"+temp_question_content);
                select_shap=shap[arg2];
                distance_MainActivity.measure_shap=shap[arg2];
                new AlertDialog.Builder(homework_list.this).setTitle(content[arg2]).setItems(new String[] { "我要繳交作業", "觀看他人作業" }, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        switch (which){
                            case 0://選擇繳交作業
                                Boolean done_homework=false;
                                dbHelper = new SQLite(homework_list.this, "homework_data");
                                Cursor cursor = dbHelper.check_done_homework("homework_data",Date[arg2],distance_MainActivity.user_id);
                                int rows_num = cursor.getCount();	//取得資料表列數
                                dbHelper.close();
                                cursor.close();
                                if(rows_num>0)
                                {
                                    done_homework=true;
                                }

                                if(!(done_homework))//已經交過作業
                                {
                                    distance_MainActivity.switch_practice_homework="homework";
                                    question_id = Date[arg2];
                                    final Intent intent = new Intent();

                                    new AlertDialog.Builder(homework_list.this)//選擇顯示圖示
                                            .setTitle("請選擇物件的所在位置")
                                            .setItems(new String[] { "目標物平躺於地面  例如:磁磚", "目標物底部與地面接觸，垂直於地面  例如:門" , "目標物在牆上 例如:窗戶"},
                                                    new DialogInterface.OnClickListener()
                                                    {
                                                        public void onClick(DialogInterface dialog, int whichcountry)
                                                        {
                                                            switch (whichcountry) {
                                                                case 0:
					                			/*
					                			dbHelper = new SQLite(homework_list.this, "record_action");
					              				dbHelper.Insert_Action(login.homework_page,"Click_do_homework_To_measure");
					              				dbHelper.close();//關閉資料庫
					              				*/

                                                                    distance_MainActivity.person_select_measure_type="get_area_ground";
                                                                    intent.setClass(homework_list.this, measure_session1.class);
                                                                    startActivity(intent);
                                                                    homework_list.this.finish();
                                                                    break;
                                                                case 1:
					                			/*
					                			dbHelper = new SQLite(homework_list.this, "record_action");
					              				dbHelper.Insert_Action(login.homework_page,"Click_do_homework_To_measure");
					              				dbHelper.close();//關閉資料庫
					              				*/

                                                                    distance_MainActivity.person_select_measure_type="get_area_ground_hight";
                                                                    intent.setClass(homework_list.this, measure_session1.class);
                                                                    startActivity(intent);
                                                                    homework_list.this.finish();
                                                                    break;
                                                                case 2:
					                			/*
					                			dbHelper = new SQLite(homework_list.this, "record_action");
					              				dbHelper.Insert_Action(login.homework_page,"Click_do_homework_To_measure");
					              				dbHelper.close();//關閉資料庫
					              				*/

                                                                    distance_MainActivity.person_select_measure_type="get_area_wall";
                                                                    intent.setClass(homework_list.this, measure_session1.class);
                                                                    startActivity(intent);
                                                                    homework_list.this.finish();
                                                                    break;
                                                            }
                                                        }
                                                    }).show();
                                }else
                                {
						  			/*
						  			dbHelper = new SQLite(homework_list.this, "record_action");
		              				dbHelper.Insert_Action(login.homework_page,"user_have_done_this_homework");
		              				dbHelper.close();//關閉資料庫
		              				*/
                                    new AlertDialog.Builder(homework_list.this)
                                            .setCancelable(false)
                                            .setTitle("警告")
                                            .setMessage("你已經繳交過作業囉!")
                                            .setPositiveButton("確認",new DialogInterface.OnClickListener(){
                                                public void onClick(DialogInterface dialog, int which)
                                                {
                                                    Create_homework_sut_submit_list(Date[arg2]);
                                                }
                                            })
                                            .show();
                                }

                                break;
                            case 1://選擇觀看他人作業
                                mark_process=false;
                                show_mark.setVisibility(View.INVISIBLE);
								/*
								dbHelper = new SQLite(homework_list.this, "record_action");
	              				dbHelper.Insert_Action(login.homework_page,"Click_see_others_homework");
	              				dbHelper.close();//關閉資料庫
	              				*/
                                Create_homework_sut_submit_list(Date[arg2]);
                                break;
                        }

                    }
                }).setNegativeButton("取消", null).show();
            }

        });
    }


    //開啟homework_list
    private void Create_homework_sut_submit_list(String question_id) {
        // TODO Auto-generated method stub
        Cursor cursor = Get_homework_stu_submit_listCursor("homework_data",question_id);
        if(cursor!=null) {
            Show_submit_homework_stu_list(cursor);//將sqlite值傳送到list中
            cursor.close();		//關閉Cursor
            dbHelper.close();	//關閉資料庫，釋放記憶體
        }


    }
    //取得表單中，homework_list資料
    private Cursor Get_homework_stu_submit_listCursor(String table_name,String question_id) {
        // TODO Auto-generated method stub
        dbHelper = new SQLite(homework_list.this, table_name);
        Cursor cursor = dbHelper.get_submit_stu_list(table_name,question_id);
        int rows_num = cursor.getCount();	//取得資料表列數
        if(rows_num==0)
        {
            if(submit_stu_listview != null)
            {
                stu_adapter = null;
                submit_stu_listview.setAdapter( stu_adapter );
            }
            show_no_one_bg();
            return null;
        }
        return cursor;
    }

    //顯示學生繳交list
    private void Show_submit_homework_stu_list(Cursor cursor) {
        // TODO Auto-generated method stub
        //把資料加入ArrayList中
        if(!mark_process)
        {
            show_list_bg();
            mark_process=false;
        }
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
        stu = new String[cursor.getCount()];
        stu_time = new String[cursor.getCount()];
        stu_calculate_value1= new String[cursor.getCount()];
        stu_calculate_value2= new String[cursor.getCount()];
        stu_calculate_value3= new String[cursor.getCount()];
        stu_calculate_value4= new String[cursor.getCount()];
        stu_calculate_operand1= new String[cursor.getCount()];
        stu_calculate_operand2= new String[cursor.getCount()];
        stu_calculate_operand3= new String[cursor.getCount()];

        stu_calculate_ans= new String[cursor.getCount()];
        stu_object_pic= new String[cursor.getCount()];
        stu_calculate_process= new String[cursor.getCount()];
        stu_object_hight= new String[cursor.getCount()];
        stu_object_top_width= new String[cursor.getCount()];
        stu_object_width= new String[cursor.getCount()];
        stu_answer_key= new String[cursor.getCount()];
        M_correct = new String[cursor.getCount()];

        showdate2= new String[cursor.getCount()];

        if(cursor !=null){
            int i = 0;
            while(cursor.moveToNext()){
                stu[i]=cursor.getString(1);
                stu_calculate_value1[i]=cursor.getString(3);
                stu_calculate_operand1[i]=cursor.getString(4);
                stu_calculate_value2[i]=cursor.getString(5);
                stu_calculate_operand2[i]=cursor.getString(6);
                stu_calculate_value3[i]=cursor.getString(7);
                stu_calculate_operand3[i]=cursor.getString(8);
                stu_calculate_value4[i]=cursor.getString(9);
                stu_calculate_ans[i]=cursor.getString(10);
                stu_object_pic[i]=cursor.getString(11);
                stu_calculate_process[i]=cursor.getString(12);
                stu_answer_key[i]=cursor.getString(13);
                stu_time[i]=cursor.getString(15);
                stu_object_hight[i]=cursor.getString(16);
                stu_object_width[i]=cursor.getString(17);
                stu_object_top_width[i]=cursor.getString(18);
                showdate2[i]=cursor.getString(15).substring(4, 6)+"月"+cursor.getString(15).substring(6, 8)+"日   "+cursor.getString(15).substring(8, 10)+":"+cursor.getString(15).substring(10, 12)+":"+cursor.getString(15).substring(12, 14);
                M_correct[i]=cursor.getString(19);
                if( M_correct[i] == null )
                {
                    M_correct[i]="null";
                }
                i=i+1;
            };
        }

        if(cursor !=null){
            int i = 0;
            cursor.moveToPosition(-1);
            while(cursor.moveToNext()){
                HashMap<String,String> item = new HashMap<String,String>();

                if(distance_MainActivity.user_id.equals(stu[i]))
                {
                    item.put("img", String.valueOf(R.drawable.stu_icon_self));
                }else
                {
                    item.put("img", String.valueOf(R.drawable.stu_icon));
                    if(M_correct[i].equals("right"))
                    {
                        item.put("mark", String.valueOf(R.drawable.mark_o));
                    }else if(M_correct[i].equals("error"))
                    {
                        item.put("mark", String.valueOf(R.drawable.mark_x));
                    }
                }
                item.put("number", stu[i]);
                item.put("time", showdate2[i]);
                list.add( item );
                i=i+1;
            };
        }

        stu_adapter = new SimpleAdapter( this, list, R.layout.homework_showstu, new String[] {"img","number","time","mark"}, new int[] {R.id.stu_icon, R.id.stu_number,R.id.stu_time,R.id.mark} );
        submit_stu_listview.setAdapter( stu_adapter );


        //註冊 ListView 被點擊的 onClick 函式
        submit_stu_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
                                    long arg3) {
                show_mark.setVisibility(View.INVISIBLE);
                click_stu=arg2;
                switch_showmark_marking();//檢測有沒有批閱過

                dbHelper = new SQLite(homework_list.this, "record_action");
                dbHelper.Insert_Action(Login.homework_page,"觀看"+stu[arg2]+"作業內容");
                dbHelper.close();//關閉資料庫

                show_have_list_bg();
                if(select_shap.equals("trapezoidal"))
                {
                    layout_bg.setBackgroundResource(R.drawable.homework_list_bg2);
                    width_top_value.setVisibility(View.VISIBLE);
                }else
                {
                    layout_bg.setBackgroundResource(R.drawable.homework_list_bg);
                    width_top_value.setVisibility(View.INVISIBLE);
                }

                question_content.setText("題目:"+temp_question_content);
                stu_number.setText("學生:"+stu[arg2]);
                if(select_shap.equals("rectangle") || select_shap.equals("parallel"))
                {
                    stu_value_process.setText("計算過程:"+"  "+stu_calculate_value1[arg2]+"  "+stu_calculate_operand1[arg2]+"  "+stu_calculate_value2[arg2]+"  =  "+ stu_calculate_ans[arg2]);
                    temp_process=stu_calculate_value1[arg2]+"  "+stu_calculate_operand1[arg2]+"  "+stu_calculate_value2[arg2]+"  =  "+ stu_calculate_ans[arg2];

                }else if(select_shap.equals("triangle") || select_shap.equals("diamond"))
                {
                    stu_value_process.setText("計算過程:"+"  "+stu_calculate_value1[arg2]+"  "+ stu_calculate_operand1[arg2]+"  "+ stu_calculate_value2[arg2]+"  "+ stu_calculate_operand2[arg2]+"  "+stu_calculate_value3[arg2]+"  =  "+ stu_calculate_ans[arg2]);
                    temp_process=stu_calculate_value1[arg2]+"  "+ stu_calculate_operand1[arg2]+"  "+ stu_calculate_value2[arg2]+"  "+ stu_calculate_operand2[arg2]+"  "+stu_calculate_value3[arg2]+"  =  "+ stu_calculate_ans[arg2];
                }else if(select_shap.equals("trapezoidal"))
                {
                    stu_value_process.setText("計算過程:"+"  ( "+stu_calculate_value1[arg2]+"  "+ stu_calculate_operand1[arg2]+"  "+ stu_calculate_value2[arg2]+" )  "+ stu_calculate_operand2[arg2]+"  "+stu_calculate_value3[arg2]+"  "+stu_calculate_operand3[arg2]+"  "+stu_calculate_value4[arg2]+"  =  "+ stu_calculate_ans[arg2]);
                    temp_process="( "+stu_calculate_value1[arg2]+"  "+ stu_calculate_operand1[arg2]+"  "+ stu_calculate_value2[arg2]+" )  "+ stu_calculate_operand2[arg2]+"  "+stu_calculate_value3[arg2]+"  "+stu_calculate_operand3[arg2]+"  "+stu_calculate_value4[arg2]+"  =  "+ stu_calculate_ans[arg2];
                }
                hight_value.setText(stu_object_hight[arg2]+" (cm)");
                width_value.setText(stu_object_width[arg2]+" (cm)");
                width_top_value.setText(stu_object_top_width[arg2]+" (cm)");

                bitmap = BitmapFactory.decodeFile("/sdcard/file_data/"+stu_object_pic[arg2]);
                object_pic.setImageBitmap(bitmap);
                bitmap = BitmapFactory.decodeFile("/sdcard/file_data/"+stu_calculate_process[arg2]);
                stu_process.setImageBitmap(bitmap);

                //暫存放大圖的路徑
                temp_object_path=stu_object_pic[arg2];
                temp_process_path=stu_calculate_process[arg2];
                temp_object_hight=stu_object_hight[arg2];
                temp_object_width=stu_object_width[arg2];
                temp_object_top_width=stu_object_top_width[arg2];
                temp_answer_key=stu_answer_key[arg2];
                temp_stu=stu[arg2];


                //暫存按下的user_id
                temp_click_user_id=stu[arg2];
            }

        });
    }
    //顯示最一開始的畫面   沒有任何object
    private void show_ini_bg(){
        layout_bg.setBackgroundResource(R.drawable.homework_list_bg_ini);
        question_content.setVisibility(View.INVISIBLE);
        stu_number.setVisibility(View.INVISIBLE);
        stu_value_process.setVisibility(View.INVISIBLE);
        hight_value.setVisibility(View.INVISIBLE);
        width_value.setVisibility(View.INVISIBLE);
        width_top_value.setVisibility(View.INVISIBLE);
        object_pic.setVisibility(View.INVISIBLE);
        stu_process.setVisibility(View.INVISIBLE);
        want_comment.setVisibility(View.INVISIBLE);
        see_others_comment.setVisibility(View.INVISIBLE);
        mark_homework.setVisibility(View.INVISIBLE);
        show_mark.setVisibility(View.INVISIBLE);
        see_others_mark.setVisibility(View.INVISIBLE);
    }
    //顯示list 但沒有人繳交作業
    private void show_no_one_bg(){
        layout_bg.setBackgroundResource(R.drawable.homework_list_bg_noone);
        question_content.setVisibility(View.INVISIBLE);
        stu_number.setVisibility(View.INVISIBLE);
        stu_value_process.setVisibility(View.INVISIBLE);
        hight_value.setVisibility(View.INVISIBLE);
        width_value.setVisibility(View.INVISIBLE);
        width_top_value.setVisibility(View.INVISIBLE);
        object_pic.setVisibility(View.INVISIBLE);
        stu_process.setVisibility(View.INVISIBLE);
        want_comment.setVisibility(View.INVISIBLE);
        see_others_comment.setVisibility(View.INVISIBLE);
        mark_homework.setVisibility(View.INVISIBLE);
        show_mark.setVisibility(View.INVISIBLE);
        see_others_mark.setVisibility(View.INVISIBLE);
    }
    //顯示list 尚未點選點人 沒有object
    private void show_list_bg(){
        layout_bg.setBackgroundResource(R.drawable.homework_list_bg_showlist);
        question_content.setVisibility(View.INVISIBLE);
        stu_number.setVisibility(View.INVISIBLE);
        stu_value_process.setVisibility(View.INVISIBLE);
        hight_value.setVisibility(View.INVISIBLE);
        width_value.setVisibility(View.INVISIBLE);
        width_top_value.setVisibility(View.INVISIBLE);
        object_pic.setVisibility(View.INVISIBLE);
        stu_process.setVisibility(View.INVISIBLE);
        want_comment.setVisibility(View.INVISIBLE);
        see_others_comment.setVisibility(View.INVISIBLE);
        width_top_value.setVisibility(View.INVISIBLE);
        mark_homework.setVisibility(View.INVISIBLE);
        show_mark.setVisibility(View.INVISIBLE);
        see_others_mark.setVisibility(View.INVISIBLE);
    }
    //顯示所有object
    private void show_have_list_bg(){
        layout_bg.setBackgroundResource(R.drawable.homework_list_bg);
        question_content.setVisibility(View.VISIBLE);
        stu_number.setVisibility(View.VISIBLE);
        stu_value_process.setVisibility(View.VISIBLE);
        hight_value.setVisibility(View.VISIBLE);
        width_value.setVisibility(View.VISIBLE);
        object_pic.setVisibility(View.VISIBLE);
        stu_process.setVisibility(View.VISIBLE);
        want_comment.setVisibility(View.VISIBLE);
        see_others_comment.setVisibility(View.VISIBLE);
        switch_showmark_marking();//檢測有沒有批閱過
    }
    //繳交完作業 顯示所有object
    private void show_have_list_bg_done_homework(){
        layout_bg.setBackgroundResource(R.drawable.homework_list_bg);
        question_content.setVisibility(View.VISIBLE);
        stu_number.setVisibility(View.VISIBLE);
        stu_value_process.setVisibility(View.VISIBLE);
        hight_value.setVisibility(View.VISIBLE);
        width_value.setVisibility(View.VISIBLE);
        object_pic.setVisibility(View.VISIBLE);
        stu_process.setVisibility(View.VISIBLE);
        want_comment.setVisibility(View.VISIBLE);
        see_others_comment.setVisibility(View.VISIBLE);
        see_others_mark.setVisibility(View.VISIBLE);
    }
    private void switch_showmark_marking()//檢測有沒有批閱過
    {
        if(!(stu[click_stu].equals(distance_MainActivity.user_id)))
        {
            if(M_correct[click_stu].equals("null"))
            {
                show_mark.setVisibility(View.INVISIBLE);
                mark_homework.setVisibility(View.VISIBLE);
                see_others_mark.setVisibility(View.INVISIBLE);
            }else if(M_correct[click_stu].equals("right"))
            {
                mark_homework.setVisibility(View.INVISIBLE);
                show_mark.setImageResource(R.drawable.mark_o);
                show_mark.setVisibility(View.VISIBLE);
                see_others_mark.setVisibility(View.INVISIBLE);
            }else if(M_correct[click_stu].equals("error"))
            {
                mark_homework.setVisibility(View.INVISIBLE);
                show_mark.setImageResource(R.drawable.mark_x);
                show_mark.setVisibility(View.VISIBLE);
                see_others_mark.setVisibility(View.INVISIBLE);
            }
        }else
        {
            show_mark.setVisibility(View.INVISIBLE);
            mark_homework.setVisibility(View.INVISIBLE);
            see_others_mark.setVisibility(View.VISIBLE);
        }


    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            	/*
            	dbHelper = new SQLite(homework_list.this, "record_action");
  				dbHelper.Insert_Action(login.homework_page,"Back_to_MainActivity");
  				dbHelper.close();//關閉資料庫
  				*/
            homework_list.this.finish();
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
