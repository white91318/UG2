package lab.hwang.ug;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class show_others_marks extends Activity {
    //class
    private SQLite dbHelper;//操作SQLite資料庫

    String mark_user[],correct[],input_mark_user[],input_correct[];
    ListView others_marks;
    SimpleAdapter marks_adapter;
    String user_answer;
    TextView no_marks;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_others_marks);
        findview();
        Bundle bunde = this.getIntent().getExtras();
        user_answer = bunde.getString("user_answer");
        Create_marks_list();//開啟作業的list
    }
    private void findview()
    {
        no_marks = (TextView) findViewById(R.id.no_marks);
        others_marks = (ListView) findViewById(R.id.show_others_marks);
    }
    //開啟homework_list
    private void Create_marks_list() {
        // TODO Auto-generated method stub
        Cursor cursor = Get_marks_listCursor();

        if(cursor!=null) {
            Show_marks_list_content(cursor);//將sqlite值傳送到list中
            cursor.close();		//關閉Cursor
            dbHelper.close();	//關閉資料庫，釋放記憶體
        }
    }

    //取得表單中，homework_list資料
    private Cursor Get_marks_listCursor() {
        // TODO Auto-generated method stub

        dbHelper = new SQLite(show_others_marks.this, "others_marks_homework_record");
        Cursor cursor = dbHelper.get_all_marks("others_marks_homework_record",user_answer);
        int rows_num = cursor.getCount();	//取得資料表列數
        if(rows_num==0)
        {
            no_marks.setVisibility(View.VISIBLE);
            //Toast.makeText(getApplicationContext(),	"沒有資料,無法開啟", Toast.LENGTH_SHORT).show();
            return null;
        }
        return cursor;
    }
    private void Show_marks_list_content(Cursor cursor) {
        // TODO Auto-generated method stub
        //把資料加入ArrayList中
        int count_num=0;
        Boolean marked=false;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
        mark_user = new String[Login.stu_num];
        correct = new String[Login.stu_num];
        input_mark_user= new String[Login.stu_num];
        input_correct= new String[Login.stu_num];

        if(cursor !=null){
            while(cursor.moveToNext()){
                mark_user[count_num]=cursor.getString(3);
                correct[count_num]=cursor.getString(5);
                count_num=count_num+1;
            };
        }
        String temp_user;
        for(int k=1;k<=Login.stu_num;k++)
        {
            HashMap<String,String> item = new HashMap<String,String>();
            if(k<10)
            {
                temp_user="stu0"+k;
            }else
            {
                temp_user="stu"+k;
            }
            for(int j = 0;j<count_num;j++)
            {
                if(temp_user.equals(mark_user[j]))
                {
                    marked=true;
                    if(correct[j].equals("right"))
                    {
                        item.put("mark", String.valueOf(R.drawable.mark_o));
                    }else if(correct[j].equals("error"))
                    {
                        item.put("mark", String.valueOf(R.drawable.mark_x));
                    }
                }
            }
            if(!marked){
                item.put("mark", String.valueOf(R.drawable.mark_no));
            }

            item.put("mark_user", temp_user);
            list.add( item );
            marked=false;
        }


        marks_adapter = new SimpleAdapter( this, list, R.layout.show_others_mark_list, new String[] {"mark_user","mark"}, new int[] { R.id.user_id, R.id.mark} );
        others_marks.setAdapter( marks_adapter );
    }
}

