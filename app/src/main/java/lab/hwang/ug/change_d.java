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

public class change_d extends Activity implements ViewSwitcher.ViewFactory {
    private static final Integer[] imagelist = {R.drawable.d1, R.drawable.d2, R.drawable.d3,
            R.drawable.d4, R.drawable.d5, R.drawable.d6, R.drawable.d7, R.drawable.d8, R.drawable.d9, R.drawable.d10};
    public static int index = 0;
    Button chose;
    private SQLite dbHelper;
    private ImageSwitcher switcher;
    private Button forward;
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_diamond);
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
                if (index == imagelist.length - 1) {
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
                intent.setClass(change_d.this, change_chose_d.class);
                startActivity(intent);
                finish();

            }
        });


    }

    private void findview() {
        // TODO Auto-generated method stub
        chose = (Button) findViewById(R.id.chose_but_d1);
        forward = (Button) findViewById(R.id.forward_d1);
        next = (Button) findViewById(R.id.next_d1);
        switcher = (ImageSwitcher) findViewById(R.id.imageSwitcher1_d1);
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
                new AlertDialog.Builder(change_d.this)
                        .setTitle("警告")
                        .setMessage("確定要放棄此次測量嗎?")
                        .setPositiveButton("確認",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        Intent intent = new Intent();
                                        intent.setClass(
                                                change_d.this,
                                                homework_list.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("user_answer", "null");
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        change_d.this.finish();

                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                    }
                                }).show();
            } else {
                new AlertDialog.Builder(change_d.this)
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
                                                    change_d.this,
                                                    "record_session_one_practice");
                                            dbHelper.Insert_session_one_practice("giveup");
                                            dbHelper.close();// 關閉資料庫
                                        }
                                        next_one_activity2.session_one = false;
                                        change_d.this.finish();
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
