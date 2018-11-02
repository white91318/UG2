package lab.hwang.ug;


import android.app.Activity;
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

public class rank_test_record_list extends Activity {
    //class
    private SQLite dbHelper;//操作SQLite資料庫
    String total_num[],right_num[],error_num[],right_rate[],Date[],key_date[];
    public static String select_key_date;

    ListView All_rank_listview;
    Button back_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rank_test_record);
        findview();

        Create_rank_record_list();
        back_home.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
	     		/*
            	dbHelper = new SQLite(rank_test_record_list.this, "record_action");
  				dbHelper.Insert_Action(login.rank_record_list_page,"Back_to_MainActivity");
  				dbHelper.close();//關閉資料庫
  				*/
                rank_test_record_list.this.finish();
            }
        });
    }

    private void findview() {
        All_rank_listview = (ListView) findViewById(R.id.rank_test_record);
        back_home= (Button) findViewById(R.id.back_home);
    }
    private void Create_rank_record_list(){
        Cursor cursor = Get_rank_record_listCursor("rank_record_list");
        if(cursor!=null) {
            Show_rank_record_list(cursor);//將sqlite值傳送到list中
            cursor.close();		//關閉Cursor
            dbHelper.close();	//關閉資料庫，釋放記憶體
        }
    }
    //取得表單中，practice_record資料
    private Cursor Get_rank_record_listCursor(String table_name) {
        // TODO Auto-generated method stub
        dbHelper = new SQLite(rank_test_record_list.this, table_name);
        Cursor cursor = dbHelper.get_all_rank_record(table_name);
        int rows_num = cursor.getCount();	//取得資料表列數
        if(rows_num==0)
        {
            dbHelper.close();
            return null;
        }
        return cursor;
    }

    private void Show_rank_record_list(Cursor cursor) {
        // TODO Auto-generated method stub
        //把資料加入ArrayList中
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
        total_num= new String[cursor.getCount()];
        right_num= new String[cursor.getCount()];
        error_num= new String[cursor.getCount()];
        right_rate= new String[cursor.getCount()];
        Date= new String[cursor.getCount()];
        key_date= new String[cursor.getCount()];


        if(cursor !=null){
            int i = 0;
            while(cursor.moveToNext()){
                key_date[i]=cursor.getString(1);
                total_num[i]=cursor.getString(2);
                right_num[i]=cursor.getString(3);
                error_num[i]=cursor.getString(4);
                right_rate[i]=cursor.getString(5)+" 分";
                Date[i]=cursor.getString(7).substring(4, 6)+"月"+cursor.getString(7).substring(6, 8)+"日   "+cursor.getString(7).substring(8, 10)+":"+cursor.getString(7).substring(10, 12)+":"+cursor.getString(7).substring(12, 14);
                i=i+1;
            };
        }

        if(cursor !=null){
            int i = 0;
            cursor.moveToPosition(-1);
            while(cursor.moveToNext()){
                HashMap<String,String> item = new HashMap<String,String>();
                if(i == 0)
                    item.put("img", String.valueOf(R.drawable.rank1));
                else if(i == 1)
                    item.put("img", String.valueOf(R.drawable.rank2));
                else if(i == 2)
                    item.put("img", String.valueOf(R.drawable.rank3));
                else if(i>2)
                    item.put("img", String.valueOf(R.drawable.rank4));
                item.put("total_num", total_num[i]);
                item.put("right_num", right_num[i]);
                item.put("error_num", error_num[i]);
                item.put("right_rate", right_rate[i]);

                item.put("Date", Date[i]);
                list.add( item );
                i=i+1;
            };
        }

        SimpleAdapter rank_adapter = new SimpleAdapter( this, list, R.layout.rank_test_record_listview,
                new String[] {"img","total_num","right_num","error_num","right_rate","Date"},
                new int[] {R.id.rank_pic,R.id.total,R.id.right_num,R.id.error_num,R.id.right_rate,R.id.date} );

        All_rank_listview.setAdapter( rank_adapter );


        //註冊 ListView 被點擊的 onClick 函式
        All_rank_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                select_key_date= key_date[arg2];
			/*
    		dbHelper = new SQLite(rank_test_record_list.this, "record_action");
			dbHelper.Insert_Action(login.rank_record_list_page,"see_rank_test_record_detail");
			dbHelper.close();//關閉資料庫
			*/
                dbHelper = new SQLite(rank_test_record_list.this, "see_rank_test_record");
                dbHelper.see_record("點擊某次挑戰紀錄內容");
                dbHelper.close();//關閉資料庫
                Intent intent = new Intent();
                intent.setClass(rank_test_record_list.this, rank_test_record_detail.class);
                startActivity(intent);
                rank_test_record_list.this.finish();
            }

        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            	/*
	            	dbHelper = new SQLite(rank_test_record_list.this, "record_action");
	  				dbHelper.Insert_Action(login.rank_record_list_page,"Back_to_MainActivity");
	  				dbHelper.close();//關閉資料庫
  				*/
            rank_test_record_list.this.finish();
            return true;
        }
        return false;
    }
}
