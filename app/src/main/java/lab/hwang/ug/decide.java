package lab.hwang.ug;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class decide extends Activity {
    private SQLite dbHelper;
    public static String user_id = Login.user;
    public static String person_hight = "", new_version = "";
    public static String person_select_measure_type = "";
    public static String switch_practice_homework = "";
    public static Boolean session_one = false;
    public static String measure_shap = "";
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";



    ImageButton p_learning, a_learning;
    Button previous_but_d;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.decide);
        findview();


        // 開啟SQLite 取得使用者身高
        GetSqlite_person_ghiht();
        GetSqlite_version();

        p_learning.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent();
                intent.setClass(decide.this, next_one_activity2p.class);
                startActivity(intent);
                finish();
            }
        });

        a_learning.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent();
                intent.setClass(decide.this, next_one_activity2.class);
                startActivity(intent);
                finish();
            }
        });

        previous_but_d.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(decide.this,
                        distance_MainActivity.class);
                startActivity(intent);
                finish();
            }
        });



    }
    private void findview() {
        p_learning=(ImageButton)findViewById(R.id.p_learning);
        a_learning=(ImageButton)findViewById(R.id.a_learning);
        previous_but_d=(Button)findViewById(R.id.previous_but_d);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(decide.this)
                    .setTitle("警告")
                    .setMessage("確定要登出嗎?")
                    .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                                /*
		   	            	dbHelper = new SQLite(distance_MainActivity.this, "record_action");
			  				dbHelper.Insert_Action(login.MainActivity_page,"login_out");
			  				dbHelper.close();//關閉資料庫
							*/

                            Intent intent = new Intent();
                            intent.setClass(decide.this, Login.class);
                            startActivity(intent);
                            decide.this.finish();
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
            return true;
        }
        return false;
    }
    // 判斷AlertDialog input 值
    public static boolean validate_input(String s) {
        return ((50 < Integer.valueOf(s) && 201 > Integer.valueOf(s)) || (1 == Integer
                .valueOf(s)));
    }

    private void GetSqlite_person_ghiht() {
        // TODO Auto-generated method stub
        dbHelper = new SQLite(decide.this, "personhight");
        Cursor cursor = dbHelper.getpersonhight("personhight");

        cursor.moveToNext();
        person_hight = cursor.getString(1);
        cursor.close();
        dbHelper.close();
    }

    private void GetSqlite_version() {
        // TODO Auto-generated method stub
        dbHelper = new SQLite(decide.this, "user_permission");
        Cursor cursor = dbHelper.getversion("user_permission");
        cursor.moveToNext();
        new_version = cursor.getString(4);
        cursor.close();
        dbHelper.close();
    }

}
