package lab.hwang.ug;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

public class change_t extends Activity implements ViewSwitcher.ViewFactory {
    private SQLite dbHelper;
    private ImageSwitcher switcher;
    private Button forward;
    private Button next;
    Button chose;

    public static int index = 0;

    private static final Integer[] imagelist = {R.drawable.t1, R.drawable.t2, R.drawable.t3,
            R.drawable.t4,R.drawable.t5,R.drawable.t6,R.drawable.t7,R.drawable.t8,R.drawable.t9,R.drawable.t10 };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_trangle);
        findview();


        forward.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {



                if (index == 0) {

                    //index = imagelist.length - 1;
                    return;
                }
                index--;
                switcher.setImageResource(imagelist[index]);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                ;
                if (index == imagelist.length-1) {
                    return;
                    // index = 0;
                }
                index++;
                switcher.setImageResource(imagelist[index]);
            }
        });
        chose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent();
                intent.setClass(change_t.this,change_chose_t.class);
                startActivity(intent);
                finish();

            }
        });


    }

    private void findview() {
        // TODO Auto-generated method stub
        chose=(Button)findViewById(R.id.chose_but_t1);
        forward = (Button) findViewById(R.id.forward_t1);
        next = (Button) findViewById(R.id.next_t1);
        switcher = (ImageSwitcher) findViewById(R.id.imageSwitcher1_t1);
        switcher.setFactory(this);
        switcher.setImageResource(imagelist[index]);

    }


    public View makeView() {
        return new ImageView(this);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (next_one_activity2.switch_practice_homework.equals("homework")) {
                try {
                    //stopCamera();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new AlertDialog.Builder(change_t.this)
                        .setTitle("警告")
                        .setMessage("確定要放棄此次測量嗎?")
                        .setPositiveButton("確認",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
										/*
										 * dbHelper = new
										 * SQLite(measure_session4_concept.this,
										 * "record_action");
										 * dbHelper.Insert_Action
										 * (login.homework_page,
										 * "giveup_do_homework_Back_to_homework_list"
										 * ); dbHelper.close();//關閉資料庫
										 */
                                        Intent intent = new Intent();
                                        intent.setClass(
                                                change_t.this,
                                                homework_list.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("user_answer", "null");
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        change_t.this.finish();
                                        //release_memory();
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                    }
                                }).show();
            } else {
                new AlertDialog.Builder(change_t.this)
                        .setTitle("警告")
                        .setMessage("確定要放棄此次測量嗎?")
                        .setPositiveButton("確認",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        try {
                                            //stopCamera();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        if (next_one_activity2.session_one) {
                                            dbHelper = new SQLite(
                                                    change_t.this,
                                                    "record_session_one_practice");
                                            dbHelper.Insert_session_one_practice("giveup");
                                            dbHelper.close();// 關閉資料庫
                                        }
                                        next_one_activity2.session_one = false;
										/*
										 * dbHelper = new
										 * SQLite(change_r.this,
										 * "record_action");
										 * dbHelper.Insert_Action
										 * (login.measuer_page
										 * ,"giveup_To_MainActivity");
										 * dbHelper.close();//關閉資料庫
										 */
                                        //release_memory();
                                        change_t.this.finish();
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                    }
                                }).show();

            }
            return true;
        }
        return false;
    }
}
