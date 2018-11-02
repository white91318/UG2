package lab.hwang.ug;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class practice_record_m extends Activity {
    //class
    private SQLite dbHelper;//操作SQLite資料庫

    String stu[],stu_time[],stu_object_pic[],stu_calculate_process[],stu_object_hight[],stu_object_width[],
            stu_calculate_value1[],stu_calculate_operand1[],stu_calculate_value2[],stu_calculate_operand2[],
            stu_calculate_value3[],stu_calculate_operand3[],stu_calculate_value4[],stu_calculate_ans[],
            stu_answer_key[],showdate2[],shape[],_ID[],calculate_process[],stu_object_top_width[];

    ListView All_practice_listview;
    Button back_home;
    public static Boolean from_practice_record_page=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.practice_record_m);
        findview();
        Create_practice_record_list();
        back_home.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
	     		/*
	     		dbHelper = new SQLite(practice_record.this, "record_action");
  				dbHelper.Insert_Action(login.practice_record_page,"Back_to_MainActivity");
  				dbHelper.close();//關閉資料庫
  				*/
                dbHelper = new SQLite(practice_record_m.this, "see_practice_record");
                dbHelper.see_record("leave");
                dbHelper.close();//關閉資料庫

                practice_record_m.this.finish();
            }
        });
    }

    private void findview() {
        All_practice_listview = (ListView) findViewById(R.id.practice_listview1);
        back_home= (Button) findViewById(R.id.back_home1);
    }
    private void Create_practice_record_list(){
        Cursor cursor = Get_practice_record_listCursor("practice_recorde_data_m");
        if(cursor!=null) {
            Show_practice_record_list(cursor);//將sqlite值傳送到list中
            cursor.close();		//關閉Cursor
            dbHelper.close();	//關閉資料庫，釋放記憶體
        }
    }
    //取得表單中，practice_record資料
    private Cursor Get_practice_record_listCursor(String table_name) {
        // TODO Auto-generated method stub
        dbHelper = new SQLite(practice_record_m.this, table_name);
        Cursor cursor = dbHelper.get_all_practice_record(table_name);
        int rows_num = cursor.getCount();	//取得資料表列數
        if(rows_num==0)
        {
            return null;
        }
        return cursor;
    }

    private void Show_practice_record_list(Cursor cursor) {
        // TODO Auto-generated method stub
        //把資料加入ArrayList中
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
        _ID= new String[cursor.getCount()];
        stu = new String[cursor.getCount()];
        stu_time = new String[cursor.getCount()];
        stu_calculate_value1= new String[cursor.getCount()];
        stu_calculate_value2= new String[cursor.getCount()];
        stu_calculate_value3= new String[cursor.getCount()];
        stu_calculate_value4= new String[cursor.getCount()];
        stu_calculate_operand1= new String[cursor.getCount()];
        stu_calculate_operand2= new String[cursor.getCount()];
        stu_calculate_operand3= new String[cursor.getCount()];
        shape= new String[cursor.getCount()];
        calculate_process= new String[cursor.getCount()];
        stu_calculate_ans= new String[cursor.getCount()];
        stu_object_pic= new String[cursor.getCount()];
        stu_calculate_process= new String[cursor.getCount()];
        stu_object_hight= new String[cursor.getCount()];
        stu_object_width= new String[cursor.getCount()];
        stu_answer_key= new String[cursor.getCount()];
        stu_object_top_width= new String[cursor.getCount()];
        showdate2= new String[cursor.getCount()];

        if(cursor !=null){
            int i = 0;
            while(cursor.moveToNext()){
                _ID[i]=String.valueOf(i+1);
                stu[i]=cursor.getString(1);
                shape[i]=cursor.getString(2);
                stu_calculate_value1[i]=cursor.getString(3);
                stu_calculate_operand1[i]=cursor.getString(4);
                stu_calculate_value2[i]=cursor.getString(5);
                stu_calculate_operand2[i]=cursor.getString(6);
                stu_calculate_value3[i]=cursor.getString(7);
                stu_calculate_operand3[i]=cursor.getString(8);
                stu_calculate_value4[i]=cursor.getString(9);
                stu_calculate_ans[i]=cursor.getString(10);

                if(shape[i].equals("rectangle_m") || shape[i].equals("parallel_m"))
                {
                    calculate_process[i]="正確的算式:\n"+stu_calculate_value1[i]+"  "+stu_calculate_operand1[i]+"  "+stu_calculate_value2[i]+"  =  "+ stu_calculate_ans[i];
                    if(shape[i].equals("rectangle_m"))
                    {
                        calculate_process[i]=calculate_process[i]+"\n\n"+
                                "長方形正確公式:\n長 X 寬";
                    }else
                    {
                        calculate_process[i]=calculate_process[i]+"\n\n"+
                                "平行四邊形正確公式:\n底 X 高";
                    }
                }else if(shape[i].equals("triangle_m") || shape[i].equals("diamond_m"))
                {
                    calculate_process[i]="正確的算式:\n"+stu_calculate_value1[i]+"  "+stu_calculate_operand1[i]+"  "+stu_calculate_value2[i]
                            +"  "+stu_calculate_operand2[i]+"  "+stu_calculate_value3[i]+"  =  "+ stu_calculate_ans[i];

                    if(shape[i].equals("triangle_m"))
                    {
                        calculate_process[i]=calculate_process[i]+"\n\n"+
                                "三角形正確公式:\n底 X 高 ÷ 2";
                    }else
                    {
                        calculate_process[i]=calculate_process[i]+"\n\n"+
                                "菱形正確公式:\n對角線 X 對角線 ÷ 2";
                    }

                }else if (shape[i].equals("trapezoidal_m"))
                {
                    calculate_process[i]="正確的算式:\n"+"( "+stu_calculate_value1[i]+" "+stu_calculate_operand1[i]+" "+stu_calculate_value2[i] + " )"
                            +" "+stu_calculate_operand2[i]+" "+stu_calculate_value3[i]+ " " + stu_calculate_operand3[i]+" "+stu_calculate_value4[i]
                            +" = "+ stu_calculate_ans[i]+"\n\n"+
                            "梯形正確公式:\n(上底+下底) X 高 ÷ 2";
                }

                stu_object_pic[i]=cursor.getString(11);
                stu_calculate_process[i]=cursor.getString(12);
                stu_answer_key[i]=cursor.getString(13);
                stu_time[i]=cursor.getString(15);
                stu_object_hight[i]=cursor.getString(16);
                stu_object_width[i]=cursor.getString(17);
                stu_object_top_width[i]=cursor.getString(18);
                showdate2[i]=cursor.getString(15).substring(4, 6)+"月"+cursor.getString(15).substring(6, 8)+"日   "+cursor.getString(15).substring(8, 10)+":"+cursor.getString(15).substring(10, 12)+":"+cursor.getString(15).substring(12, 14);
                i=i+1;
            };
        }

        if(cursor !=null){
            int i = 0;
            cursor.moveToPosition(-1);
            while(cursor.moveToNext()){
                HashMap<String,String> item = new HashMap<String,String>();
                item.put("_ID", _ID[i]);
                item.put("stu_object_hight", stu_object_hight[i]);
                item.put("stu_object_width", stu_object_width[i]);
                item.put("stu_object_top_width", stu_object_top_width[i]);
                item.put("stu_object_pic", "/sdcard/file_data/"+stu_object_pic[i]);
                item.put("stu_calculate_process", "/sdcard/file_data/"+stu_calculate_process[i]);

                item.put("calculate_process", calculate_process[i]);
                item.put("time", showdate2[i]);
                list.add( item );
                i=i+1;
            };
        }

        SimpleAdapter practice_adapter = new SimpleAdapter( this, list, R.layout.practice_record_listview,
                new String[] {"_ID","stu_object_hight","stu_object_width","stu_object_top_width","stu_object_pic","calculate_process","stu_calculate_process","time"},
                new int[] {R.id._ID,R.id.show_hight,R.id.show_width,R.id.show_top_width,R.id.object_pic,R.id.calculate_process,R.id.calculate_pic,R.id.show_date} );

        All_practice_listview.setAdapter( practice_adapter );


        //註冊 ListView 被點擊的 onClick 函式
        All_practice_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
                                    long arg3) {
                final Intent intent = new Intent();
                final Bundle bundle = new Bundle();
                new AlertDialog.Builder(practice_record_m.this)//選擇顯示圖示
                        .setTitle("請選擇您需要的功能")
                        .setItems(new String[] { "觀看實體物件放大圖", "觀看計算過程放大圖"},
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int whichcountry)
                                    {
                                        switch (whichcountry) {
                                            case 0:
	       			/*
		       			dbHelper = new SQLite(practice_record.this, "record_action");
		  				dbHelper.Insert_Action(login.practice_record_page,"See_large_object");
		  				dbHelper.close();//關閉資料庫
		  				*/
                                                bundle.putString("from", "homework");
                                                bundle.putString("path", stu_object_pic[arg2]);
                                                intent.putExtras(bundle);
                                                intent.setClass(practice_record_m.this, show_large_image_makeup.class);
                                                startActivity(intent);
                                                break;
                                            case 1:
	       			/*
		       			dbHelper = new SQLite(practice_record.this, "record_action");
		  				dbHelper.Insert_Action(login.practice_record_page,"See_large_process");
		  				dbHelper.close();//關閉資料庫
		  				*/
                                                bundle.putString("from", "homework");
                                                bundle.putString("path", stu_calculate_process[arg2]);
                                                intent.putExtras(bundle);
                                                intent.setClass(practice_record_m.this, show_large_image_makeup.class);
                                                startActivity(intent);
                                                break;
	       			/*
	       		case 2:
	       			dbHelper = new SQLite(practice_record.this, "record_action");
	  				dbHelper.Insert_Action(login.practice_record_page,"To_practice_again");
	  				dbHelper.close();//關閉資料庫

	       			from_practice_record_page=true;
	       			measure_session1.temp_pic_storename=stu_object_pic[arg2];
	       			measure_session1.bundle_width2_value=stu_object_top_width[arg2];
	       			measure_session1.bundle_width_value=stu_object_width[arg2];
	       			measure_session1.bundle_hight_value=stu_object_hight[arg2];
	       			distance_MainActivity.measure_shap=shape[arg2];

	       			if(shape[arg2].equals("rectangle") || shape[arg2].equals("parallel"))
	     			{
	     				intent.setClass(practice_record.this, user_practice_area.class);
	          			startActivity(intent);
	          			practice_record.this.finish();
	     			}else if(shape[arg2].equals("triangle") || shape[arg2].equals("diamond"))
	     			{
	     				intent.setClass(practice_record.this, user_practice_area2.class);
	          			startActivity(intent);
	          			practice_record.this.finish();
	     			}else if (shape[arg2].equals("trapezoidal"))
	     			 {
	     				intent.setClass(practice_record.this, user_practice_area3.class);
	          			startActivity(intent);
	          			practice_record.this.finish();
	     			 }
	       			break;
	       			*/
                                        }
                                    }
                                }).show();
            }

        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            		/*
	            	dbHelper = new SQLite(practice_record.this, "record_action");
	  				dbHelper.Insert_Action(login.practice_record_page,"Back_to_MainActivity");
	  				dbHelper.close();//關閉資料庫
	  				*/
            dbHelper = new SQLite(practice_record_m.this, "see_practice_record");
            dbHelper.see_record("leave");
            dbHelper.close();//關閉資料庫

            practice_record_m.this.finish();
            return true;
        }
        return false;
    }
}

