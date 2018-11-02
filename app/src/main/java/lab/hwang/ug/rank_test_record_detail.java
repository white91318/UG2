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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class rank_test_record_detail extends Activity {
    //class
    private SQLite dbHelper;//操作SQLite資料庫
    String value_1[],operand1[],value_2[],operand2[],value_3[],operand3[],value_4[],ans[],object_pic[],user_calculate_pic[],
            shape[],Date[],object_hight[],object_width[],object_top_width[],correct[],stu_calculate_process[],user_select_formula[];


    ListView All_rank_listview;
    Button back_home;
    String select_formula[]={"","長 X 寬", "底 X 高" , "底 X 高  ÷ 2", "對角線 X 對角線  ÷ 2", "( 上底 + 下底 ) X 高 ÷ 2"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rank_test_record_detail);
        findview();
        Create_rank_record_detail_list();
        back_home.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
	     		/*
				dbHelper = new SQLite(rank_test_record_detail.this, "record_action");
  				dbHelper.Insert_Action(login.rank_record_detail_page,"Back_to_rank_test_list");
  				dbHelper.close();//關閉資料庫
  				*/
                dbHelper = new SQLite(rank_test_record_detail.this, "see_rank_test_record");
                dbHelper.see_record("leave");
                dbHelper.close();//關閉資料庫
                Intent intent = new Intent();
                intent.setClass(rank_test_record_detail.this, rank_test_record_list.class);
                startActivity(intent);
                rank_test_record_detail.this.finish();
            }
        });
    }

    private void findview() {
        All_rank_listview = (ListView) findViewById(R.id.rank_test_record_detail);
        back_home= (Button) findViewById(R.id.back_home);
    }
    private void Create_rank_record_detail_list(){
        Cursor cursor = Get_rank_record_listCursor("rank_record");
        if(cursor!=null) {
            Show_rank_record_list(cursor);//將sqlite值傳送到list中
            cursor.close();		//關閉Cursor
        }
    }
    //取得表單中，practice_record資料
    private Cursor Get_rank_record_listCursor(String table_name) {
        // TODO Auto-generated method stub
        dbHelper = new SQLite(rank_test_record_detail.this, table_name);
        Cursor cursor = dbHelper.get_all_rank_record_detail(table_name,rank_test_record_list.select_key_date);
        int rows_num = cursor.getCount();	//取得資料表列數
        if(rows_num==0)
        {
            dbHelper.close();
            cursor.close();		//關閉Cursor
            return null;
        }
        dbHelper.close();
        return cursor;
    }

    private void Show_rank_record_list(Cursor cursor) {
        // TODO Auto-generated method stub
        //把資料加入ArrayList中
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
        value_1= new String[cursor.getCount()];
        operand1= new String[cursor.getCount()];
        value_2= new String[cursor.getCount()];
        operand2= new String[cursor.getCount()];
        value_3= new String[cursor.getCount()];
        operand3= new String[cursor.getCount()];
        value_4= new String[cursor.getCount()];
        ans= new String[cursor.getCount()];
        object_pic= new String[cursor.getCount()];
        user_calculate_pic= new String[cursor.getCount()];
        shape= new String[cursor.getCount()];
        Date= new String[cursor.getCount()];
        object_hight= new String[cursor.getCount()];
        object_width= new String[cursor.getCount()];
        object_top_width= new String[cursor.getCount()];
        correct= new String[cursor.getCount()];
        stu_calculate_process= new String[cursor.getCount()];
        user_select_formula= new String[cursor.getCount()];

        if(cursor !=null){
            int i = 0;
            while(cursor.moveToNext()){
                value_1[i]=cursor.getString(3);
                operand1[i]=cursor.getString(4);
                value_2[i]=cursor.getString(5);
                operand2[i]=cursor.getString(6);
                value_3[i]=cursor.getString(7);
                operand3[i]=cursor.getString(8);
                value_4[i]=cursor.getString(9);
                ans[i]=cursor.getString(10);
                if(!("".equals(operand3[i])))
                {
                    stu_calculate_process[i]="( "+value_1[i]+" "+operand1[i]+" "+value_2[i] + " )"
                            +" "+operand2[i]+" "+value_3[i]+ " " + operand3[i]+" "+value_4[i]
                            +" = "+ ans[i];
                }else if(!("".equals(operand2[i])))
                {
                    stu_calculate_process[i]=value_1[i]+"  "+operand1[i]+"  "+value_2[i]
                            +"  "+operand2[i]+"  "+value_3[i]+"  =  "+ ans[i];
                }else
                {
                    stu_calculate_process[i]=value_1[i]+"  "+operand1[i]+"  "+value_2[i]+"  =  "+ ans[i];
                }


                object_pic[i]=cursor.getString(11);
                user_calculate_pic[i]=cursor.getString(12);
                if(cursor.getString(13).equals("rectangle"))
                    shape[i]="矩形";
                if(cursor.getString(13).equals("triangle"))
                    shape[i]="三角形";
                if(cursor.getString(13).equals("trapezoidal"))
                    shape[i]="梯形";
                if(cursor.getString(13).equals("parallel"))
                    shape[i]="平行四邊形";
                if(cursor.getString(13).equals("diamond"))
                    shape[i]="菱形";

                Date[i]=cursor.getString(15);
                object_hight[i]=cursor.getString(16);
                object_width[i]=cursor.getString(17);
                object_top_width[i]=cursor.getString(18);
                correct[i]=cursor.getString(19);
                user_select_formula[i]=cursor.getString(20);
                if(correct[i].equals("true"))
                    correct[i]="正確";
                else
                {
                    if(cursor.getString(13).equals("trapezoidal"))
                    {
                        BigDecimal real_top_width = new BigDecimal(object_top_width[i]);
                        BigDecimal real_width = new BigDecimal(object_width[i]);
                        BigDecimal real_hight = new BigDecimal(object_hight[i]);
                        double real_ans1=real_top_width.add(real_width).multiply(real_hight).doubleValue();
                        stu_calculate_process[i]="您的算式:\n"+stu_calculate_process[i]
                                +"\n\n"+
                                "正確的算式:\n"+
                                "( "+object_top_width[i]+" "+"+"+" "+object_width[i] + " )"
                                +" "+"X"+" "+object_hight[i]+ " " +"÷"+" "+"2"
                                +" = "+ String.valueOf(real_ans1/2.0)+"\n\n"+
                                "你選擇的公式:"+select_formula[Integer.valueOf(user_select_formula[i])]+
                                "\n正確公式:(上底+下底) X 高 ÷ 2";
                    }

                    if(cursor.getString(13).equals("triangle")||cursor.getString(13).equals("diamond"))
                    {
                        BigDecimal real_width = new BigDecimal(object_width[i]);
                        BigDecimal real_hight = new BigDecimal(object_hight[i]);
                        double real_ans1=real_width.multiply(real_hight).doubleValue();

                        stu_calculate_process[i]="您的算式:\n"+stu_calculate_process[i]
                                +"\n\n"+
                                "正確的算式:\n"+
                                object_width[i] +" "+"X"+" "+object_hight[i]+ " " +"÷"+" "+"2"
                                +" = "+ String.valueOf(real_ans1/2.0);
                        if(cursor.getString(13).equals("triangle"))
                        {
                            stu_calculate_process[i]=stu_calculate_process[i]+"\n\n"+
                                    "你選擇的公式:"+select_formula[Integer.valueOf(user_select_formula[i])]+
                                    "\n正確公式:底 X 高 ÷ 2";
                        }else
                        {
                            stu_calculate_process[i]=stu_calculate_process[i]+"\n\n"+
                                    "你選擇的公式:"+select_formula[Integer.valueOf(user_select_formula[i])]+
                                    "\n正確公式:對角線 X 對角線 ÷ 2";
                        }
                    }
                    if(cursor.getString(13).equals("rectangle")||cursor.getString(13).equals("parallel"))
                    {
                        BigDecimal real_width = new BigDecimal(object_width[i]);
                        BigDecimal real_hight = new BigDecimal(object_hight[i]);
                        double real_ans1=real_width.multiply(real_hight).doubleValue();

                        stu_calculate_process[i]="您的算式:\n"+stu_calculate_process[i]
                                +"\n\n"+
                                "正確的算式:\n"+
                                object_width[i] +" "+"X"+" "+object_hight[i]
                                +" = "+ String.valueOf(real_ans1);
                        if(cursor.getString(13).equals("rectangle"))
                        {
                            stu_calculate_process[i]=stu_calculate_process[i]+"\n\n"+
                                    "你選擇的公式:"+select_formula[Integer.valueOf(user_select_formula[i])]+
                                    "\n正確公式:長 X 寬";
                        }else
                        {
                            stu_calculate_process[i]=stu_calculate_process[i]+"\n\n"+
                                    "你選擇的公式:"+select_formula[Integer.valueOf(user_select_formula[i])]+
                                    "\n正確公式:底 X 高";
                        }
                    }

                }
                i=i+1;
            };
        }

        if(cursor !=null){
            int i = 0;
            cursor.moveToPosition(-1);
            while(cursor.moveToNext()){
                HashMap<String,String> item = new HashMap<String,String>();
                item.put("no", String.valueOf(i+1));
                item.put("stu_object_hight", object_hight[i]);
                item.put("stu_object_width", object_width[i]);
                if("null".equals(object_top_width[i]))
                {object_top_width[i]="";}
                item.put("stu_object_top_width", object_top_width[i]);
                item.put("stu_object_pic", "/sdcard/file_data/"+object_pic[i]);
                item.put("calculate_shape", shape[i]);
                item.put("stu_calculate_process", "/sdcard/file_data/"+user_calculate_pic[i]);
                item.put("calculate_process", stu_calculate_process[i]);
                item.put("check_right", correct[i]);

                list.add( item );
                i=i+1;
            };
        }

        SimpleAdapter rank_detail_adapter = new SimpleAdapter( this, list, R.layout.rank_test_record_detail_listview,
                new String[] {"no","stu_object_hight","stu_object_width","stu_object_top_width","stu_object_pic","calculate_shape","stu_calculate_process","calculate_process","check_right"},
                new int[] {R.id._ID,R.id.show_hight,R.id.show_width,R.id.show_top_width,R.id.object_pic,R.id.calculate_shape,R.id.calculate_pic,R.id.calculate_process,R.id.check_right} );

        All_rank_listview.setAdapter( rank_detail_adapter );


        //註冊 ListView 被點擊的 onClick 函式
        All_rank_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
                                    long arg3) {
                final Intent intent = new Intent();
                final Bundle bundle = new Bundle();
                new AlertDialog.Builder(rank_test_record_detail.this)//選擇顯示圖示
                        .setTitle("請選擇您需要的功能")
                        .setItems(new String[] { "觀看實體物件放大圖", "觀看計算過程放大圖"},
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int whichcountry)
                                    {
                                        switch (whichcountry) {
                                            case 0:
	       			/*
		       			dbHelper = new SQLite(rank_test_record_detail.this, "record_action");
		  				dbHelper.Insert_Action(login.rank_record_detail_page,"See_large_object");
		  				dbHelper.close();//關閉資料庫
	       				*/
                                                dbHelper = new SQLite(rank_test_record_detail.this, "see_rank_test_record");
                                                dbHelper.see_record("See_large_object");
                                                dbHelper.close();//關閉資料庫

                                                bundle.putString("from", "homework");
                                                bundle.putString("path", object_pic[arg2]);
                                                intent.putExtras(bundle);
                                                intent.setClass(rank_test_record_detail.this, show_large_image.class);
                                                startActivity(intent);
                                                break;
                                            case 1:
	       			/*
		       			dbHelper = new SQLite(rank_test_record_detail.this, "record_action");
		  				dbHelper.Insert_Action(login.rank_record_detail_page,"See_large_process");
		  				dbHelper.close();//關閉資料庫
	       			*/
                                                dbHelper = new SQLite(rank_test_record_detail.this, "see_rank_test_record");
                                                dbHelper.see_record("See_large_process");
                                                dbHelper.close();//關閉資料庫

                                                bundle.putString("from", "homework");
                                                bundle.putString("path", user_calculate_pic[arg2]);
                                                intent.putExtras(bundle);
                                                intent.setClass(rank_test_record_detail.this, show_large_image.class);
                                                startActivity(intent);
                                                break;
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
					dbHelper = new SQLite(rank_test_record_detail.this, "record_action");
	  				dbHelper.Insert_Action(login.rank_record_detail_page,"Back_to_rank_test_list");
	  				dbHelper.close();//關閉資料庫
	  				*/
            dbHelper = new SQLite(rank_test_record_detail.this, "see_rank_test_record");
            dbHelper.see_record("leave");
            dbHelper.close();//關閉資料庫
            Intent intent = new Intent();
            intent.setClass(rank_test_record_detail.this, rank_test_record_list.class);
            startActivity(intent);
            rank_test_record_detail.this.finish();
            return true;
        }
        return false;
    }
}
