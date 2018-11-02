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


public class pchange_r extends Activity implements ViewSwitcher.ViewFactory {
    private SQLite dbHelper;
    private ImageSwitcher switcher;
    private Button forward;
    private Button next;
    Button chose;

    public static int index = 0;

    private static final Integer[] imagelist = {R.drawable.pr1, R.drawable.pr2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_rectangle);
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
                intent.setClass(pchange_r.this,pchange_chose_r.class);
                startActivity(intent);
                finish();

            }
        });


    }

    private void findview() {
        // TODO Auto-generated method stub
        chose=(Button)findViewById(R.id.chose_but);
        forward = (Button) findViewById(R.id.forward);
        next = (Button) findViewById(R.id.next);
        switcher = (ImageSwitcher) findViewById(R.id.imageSwitcher1);
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
                new AlertDialog.Builder(pchange_r.this)
                        .setTitle("警告")
                        .setMessage("確定要放棄此次測量嗎?")
                        .setPositiveButton("確認",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        Intent intent = new Intent();
                                        intent.setClass(
                                                pchange_r.this,
                                                homework_list.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("user_answer", "null");
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        pchange_r.this.finish();
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
                new AlertDialog.Builder(pchange_r.this)
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
                                                    pchange_r.this,
                                                    "record_session_one_practice");
                                            dbHelper.Insert_session_one_practice("giveup");
                                            dbHelper.close();// 關閉資料庫
                                        }
                                        next_one_activity2.session_one = false;

                                        pchange_r.this.finish();
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
