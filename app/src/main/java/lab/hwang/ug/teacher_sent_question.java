package lab.hwang.ug;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class teacher_sent_question extends Activity {
    //class
    private SQLite dbHelper;//操作SQLite資料庫
    EditText question_content;
    TextView select_shape;
    Button sent;
    String temp_shape="rectangle";

    String content[],Date[],shap[],showdate[],chshap[];
    ListView All_homework_listview;
    SimpleAdapter homework_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.teacher_sent_question);
        findview();
        Create_homework_list();//開啟作業的list

        select_shape.setOnClickListener(new Button.OnClickListener()
        {
            @Override

            public void onClick(View v)
            {
                new AlertDialog.Builder(teacher_sent_question.this)//選擇顯示圖示
                        .setTitle("請選擇形狀")
                        .setItems(new String[] { "矩形", "三角形" , "平行四邊形", "菱形" , "梯形"},
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int whichcountry)
                                    {
                                        switch (whichcountry) {
                                            case 0:
                                                select_shape.setText("矩形");
                                                temp_shape="rectangle";
                                                break;
                                            case 1:
                                                select_shape.setText("三角形");
                                                temp_shape="triangle";
                                                break;
                                            case 2:
                                                select_shape.setText("平行四邊形");
                                                temp_shape="parallel";
                                                break;
                                            case 3:
                                                select_shape.setText("菱形");
                                                temp_shape="diamond";
                                                break;
                                            case 4:
                                                select_shape.setText("梯形");
                                                temp_shape="trapezoidal";
                                                break;
                                        }
                                    }
                                }).show();
            }
        });
        sent.setOnClickListener(new Button.OnClickListener()
        {
            @Override

            public void onClick(View v)
            {

                new AlertDialog.Builder(teacher_sent_question.this)
                        .setTitle("新增作業")
                        .setMessage("確定要新增-"+question_content.getText().toString()+"這個作業嗎? ")
                        .setPositiveButton("確認",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which)
                            {
                                homework_adapter= null;
                                All_homework_listview.setAdapter( homework_adapter );
                                dbHelper = new SQLite(teacher_sent_question.this, "teacher_sent_question_list");
                                dbHelper.Teacher_InsertHomework(question_content.getText().toString(),temp_shape);
                                dbHelper.close();//關閉資料庫

	  				/*
	  				dbHelper = new SQLite(teacher_sent_question.this, "homework_question_list");
	  				dbHelper.InsertHomework(question_content.getText().toString(),temp_shape);
	  				dbHelper.close();//關閉資料庫
	  				*/
                                Create_homework_list();
                            }
                        })
                        .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {

                            }
                        })
                        .show();
            }
        });

    }

    private void findview() {
        question_content =(EditText)findViewById(R.id.question_content);
        select_shape=(TextView)findViewById(R.id.select_shape);
        sent=(Button)findViewById(R.id.sent);
        All_homework_listview = (ListView)findViewById(R.id.homework_listview);
    }
    //開啟homework_list
    private void Create_homework_list() {
        // TODO Auto-generated method stub
        Cursor cursor = Get_homework_listCursor("teacher_sent_question_list");

        if(cursor!=null) {
            Show_homework_list_content(cursor);//將sqlite值傳送到list中
            cursor.close();		//關閉Cursor
            dbHelper.close();	//關閉資料庫，釋放記憶體
        }
    }
    //取得表單中，homework_list資料
    private Cursor Get_homework_listCursor(String table_name) {
        // TODO Auto-generated method stub

        dbHelper = new SQLite(teacher_sent_question.this, table_name);
        Cursor cursor = dbHelper.get_all_homework_list_item(table_name);
        int rows_num = cursor.getCount();	//取得資料表列數
        if(rows_num==0)
        {
            dbHelper.close();
            Toast.makeText(getApplicationContext(),	"沒有資料,無法開啟", Toast.LENGTH_SHORT).show();
            return null;
        }
        return cursor;
    }
    private void Show_homework_list_content(Cursor cursor) {
        // TODO Auto-generated method stub
        //把資料加入ArrayList中
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
        content = new String[cursor.getCount()];
        shap = new String[cursor.getCount()];
        Date = new String[cursor.getCount()];
        showdate= new String[cursor.getCount()];
        chshap= new String[cursor.getCount()];
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
                Date[i]=cursor.getString(4);
                showdate[i]=cursor.getString(4).substring(4, 6)+"月"+cursor.getString(4).substring(6, 8)+"日   "+cursor.getString(4).substring(8, 10)+":"+cursor.getString(4).substring(10, 12)+":"+cursor.getString(4).substring(12, 14);
                i=i+1;
            };
        }

        if(cursor !=null){
            int i = 0;
            cursor.moveToPosition(-1);
            while(cursor.moveToNext()){
                HashMap<String,String> item = new HashMap<String,String>();
                item.put("content", content[i]);
                item.put("date", showdate[i]);
                list.add( item );
                i=i+1;
            };
        }

        homework_adapter = new SimpleAdapter( this, list, R.layout.homework_showlist, new String[] {"content","date"}, new int[] { R.id.text1, R.id.text2} );
        All_homework_listview.setAdapter( homework_adapter );

    }
}
