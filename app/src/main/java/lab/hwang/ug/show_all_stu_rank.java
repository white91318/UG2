package lab.hwang.ug;


import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class show_all_stu_rank extends Activity {
    //class
    private SQLite dbHelper;//操作SQLite資料庫
    String total_num[],right_num[],error_num[],right_rate[],Date[],user_id[];
    TextView rank1,rank2,rank3;
    ListView All_rank_listview;
    Button back_home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_all_stu_rank);
        findview();
        try{
            if(isInternetConnected())
            {
                final ProgressDialog myDialog = ProgressDialog.show(show_all_stu_rank.this, "請稍後片刻", "載入最新排行榜中....");
                new Thread()
                {
                    public void run()
                    {
                        try
                        {
                            sleep(2000);
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                        finally
                        {
                            myDialog.dismiss();
                        }
                    }
                }.start();
            }
        }catch(Exception e)
        {

        }

        Create_rank_record_list();
        back_home.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                show_all_stu_rank.this.finish();
            }
        });
    }


    private void findview() {
        All_rank_listview = (ListView) findViewById(R.id.rank_test_record);
        back_home= (Button) findViewById(R.id.back_home);
        rank1= (TextView) findViewById(R.id.rank1);
        rank2= (TextView) findViewById(R.id.rank2);
        rank3= (TextView) findViewById(R.id.rank3);
    }
    private void Create_rank_record_list(){
        Cursor cursor = Get_rank_record_listCursor("rank_allstu");
        if(cursor!=null) {
            Show_rank_record_list(cursor);//將sqlite值傳送到list中
            cursor.close();		//關閉Cursor
            dbHelper.close();	//關閉資料庫，釋放記憶體
        }
    }
    //取得表單中，practice_record資料
    private Cursor Get_rank_record_listCursor(String table_name) {
        // TODO Auto-generated method stub
        dbHelper = new SQLite(show_all_stu_rank.this, table_name);
        Cursor cursor = dbHelper.get_all_stu_rank(table_name);
        int rows_num = cursor.getCount();	//取得資料表列數
        if(rows_num==0)
        {
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
        user_id= new String[cursor.getCount()];


        if(cursor !=null){
            int i = 0;
            while(cursor.moveToNext()){
                user_id[i]=cursor.getString(1);
                total_num[i]=cursor.getString(2);
                right_num[i]=cursor.getString(3);
                error_num[i]=String.valueOf(Integer.valueOf(total_num[i])-Integer.valueOf(right_num[i]));
                right_rate[i]=cursor.getString(4);
                Date[i]=cursor.getString(6).substring(4, 6)+"月"+cursor.getString(6).substring(6, 8)+"日   "+cursor.getString(6).substring(8, 10)+":"+cursor.getString(6).substring(10, 12)+":"+cursor.getString(6).substring(12, 14);
                if(i==0)
                    rank1.setText(user_id[i]);
                else if(i==1)
                    rank2.setText(user_id[i]);
                else if(i==2)
                    rank3.setText(user_id[i]);

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
                {
                    int resID = getResources().getIdentifier("rank_no_"+(i+1) , "drawable", getPackageName());
                    item.put("img", String.valueOf(resID));
                }

                item.put("user_id", user_id[i]);
                item.put("total_num", total_num[i]);
                item.put("right_num", right_num[i]);
                item.put("error_num", error_num[i]);
                item.put("right_rate", right_rate[i]);

                item.put("Date", Date[i]);
                list.add( item );
                i=i+1;
            };
        }

        SimpleAdapter rank_adapter = new SimpleAdapter( this, list, R.layout.show_all_stu_rank_list,
                new String[] {"user_id","img","total_num","right_num","error_num","right_rate","Date"},
                new int[] {R.id.user_id,R.id.rank_pic,R.id.total,R.id.right_num,R.id.error_num,R.id.right_rate,R.id.date} );

        All_rank_listview.setAdapter( rank_adapter );


        //註冊 ListView 被點擊的 onClick 函式
        All_rank_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {

            }

        });
    }
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

