package lab.hwang.ug;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.MaskFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import static lab.hwang.ug.measure_session2p.temp_pic_storename;

public class user_practice_p extends Activity {
    //畫板
    private static final float MINP = 0.25f;
    private static final float MAXP = 0.75f;
    public static String user_id = Login.user;
    AbsoluteLayout right_lay;
    ImageView show_pic_large_mess;
    Bitmap bitmap, block_bitmap;
    //左上拍照的物件
    ImageView object_pic;
    TextView width_tv, hight_tv;
    Button erase, pen_size, pen_color, pen, clean, draw_page_1, draw_page_2, draw_page_3;//右邊畫版
    Button keyboard_1, keyboard_2, keyboard_3, keyboard_4, keyboard_5, keyboard_6, keyboard_7, keyboard_8, keyboard_9, keyboard_0,
            keyboard_clean, keyboard_erase, keyboard_point;//數字鍵盤
    Button keyboard_plus, keyboard_minus, keyboard_multiply, keyboard_division;//運算元鍵盤
    TextView input_tv1, input_tv2, input_tv3, input_tv4, input_tv5, input_tv6, input_tv7, show_input_hint;
    ImageView input_tv1_bg, input_tv2_bg, input_tv3_bg, input_tv5_bg, input_tv6_bg, input_tv7_bg, show_process;
    Button hint1_but, confirm_but, hint2_but;
    ImageView ca_hint_pic;
    Boolean check_input_zero = true;
    String tempsdf, process_store_name;
    SimpleDateFormat sdf;
    int draw_page = 1;
    Boolean first_page1 = true, first_page2 = true, first_page3 = true;
    //class
    private SQLite dbHelper;//操作SQLite資料庫
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    private Paint mPaint;
    private MaskFilter mEmboss;
    private MaskFilter mBlur;
    //左下運算
    private Animation myAnimation_Alpha;
    private StringBuffer user_input_value = new StringBuffer();
    private int user_select_input_tv = 0, user_select_operand_input_tv = 0;
    private int keyboard_status = 0; //0初始化,1數字鍵盤,2運算元
    private String select_ans_mode = "area"; //area沒有點選hint2,others有點選hint2
    private DatabaseReference mDatabase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_practice_p);
        findview();
        sdf = new SimpleDateFormat("yyyyMMddHHmmss");//日期
        tempsdf = sdf.format(new Date());
        process_store_name = temp_pic_storename + tempsdf;

        bitmap = BitmapFactory.decodeFile("/sdcard/file_data/" + temp_pic_storename);
        object_pic.setImageBitmap(bitmap);

        block_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.draw_block);
        show_process.setImageBitmap(block_bitmap);


        width_tv.setText(measure_session2p.bundle_width_value + "(cm)");
        hight_tv.setText(measure_session2p.bundle_hight_value + "(cm)");


        final ProgressDialog PDialog = ProgressDialog.show(user_practice_p.this, "請稍後", "導入練習資訊", true);
        new Thread() {
            public void run() {
                try {
                    sleep(2000);
                    store_block_process(2);
                    store_block_process(3);

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    PDialog.dismiss();
                }
            }
        }.start();

        if (next_one_activity2p.measure_shap.equals("rectangle")) {
            input_tv1.setText("長");
            input_tv3.setText("寬");
        } else if (next_one_activity2p.measure_shap.equals("parallel")) {
            input_tv1.setText("底");
            input_tv3.setText("斜邊");
        }

        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("from", "practice");
        bundle.putString("path", next_one_activity2p.measure_shap);
        intent.putExtras(bundle);
        intent.setClass(user_practice_p.this, show_large_imagep.class);
        startActivity(intent);

        pen.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                pen_color.setBackgroundResource(R.drawable.paint_color);
                pen_size.setBackgroundResource(R.drawable.paint_size);
                mPaint.setColor(0xFF000000);
                mPaint.setStrokeWidth(6);
                pen_size.setClickable(true);
                pen_color.setClickable(true);
            }
        });
        pen_size.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(user_practice_p.this)//選擇顯示圖示
                        .setTitle("請選擇鉛筆粗細")
                        .setItems(R.array.pen_size,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichcountry) {
                                        switch (whichcountry) {
                                            case 0:
                                                mPaint.setStrokeWidth(3);
                                                break;
                                            case 1:
                                                mPaint.setStrokeWidth(6);
                                                break;
                                            case 2:
                                                mPaint.setStrokeWidth(9);
                                                break;
                                        }
                                    }
                                }).show();
            }
        });

        pen_color.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(user_practice_p.this)//選擇顯示圖示
                        .setTitle("請選擇鉛筆粗細")
                        .setItems(R.array.pen_color,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichcountry) {
                                        switch (whichcountry) {
                                            case 0:
                                                mPaint.setColor(Color.BLACK);
                                                break;
                                            case 1:
                                                mPaint.setColor(Color.BLUE);
                                                break;
                                            case 2:
                                                mPaint.setColor(Color.RED);
                                                break;
                                            case 3:
                                                mPaint.setColor(Color.YELLOW);
                                                break;
                                            case 4:
                                                mPaint.setColor(Color.GREEN);
                                                break;
                                        }
                                    }
                                }).show();
            }
        });
        erase.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPaint.setColor(Color.WHITE);
                mPaint.setStrokeWidth(30);
                pen_size.setClickable(false);
                pen_color.setClickable(false);
                pen_color.setBackgroundResource(R.drawable.paint_color_x);
                pen_size.setBackgroundResource(R.drawable.paint_size_x);
            }
        });
        clean.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                clean_draw_page();
                block_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.draw_block);
                show_process.setImageBitmap(block_bitmap);
            }
        });
        draw_page_1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_draw_page_change_color(1);
                if (draw_page == 1) {
                    store_process(1);
                    first_page1 = false;
                } else if (draw_page == 2) {
                    store_process(2);
                } else if (draw_page == 3) {
                    store_process(3);
                }
                draw_page = 1;
                if (first_page1) {
                    clean_draw_page();
                    block_bitmap = BitmapFactory.decodeFile("/sdcard/file_data/" + process_store_name + "_process.png");
                    show_process.setImageBitmap(block_bitmap);
                } else {
                    clean_draw_page();
                    block_bitmap = BitmapFactory.decodeFile("/sdcard/file_data/" + process_store_name + "_process.png");
                    show_process.setImageBitmap(block_bitmap);
                }
            }
        });
        draw_page_2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_draw_page_change_color(2);
                if (draw_page == 1) {
                    store_process(1);
                } else if (draw_page == 2) {
                    store_process(2);
                } else if (draw_page == 3) {
                    store_process(3);
                }
                draw_page = 2;
                clean_draw_page();
                if (first_page2) {
                    block_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.draw_block);
                    show_process.setImageBitmap(block_bitmap);
                    first_page2 = false;
                } else {
                    block_bitmap = BitmapFactory.decodeFile("/sdcard/file_data/" + process_store_name + "_process2.png");
                    show_process.setImageBitmap(block_bitmap);
                }
            }
        });
        draw_page_3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                click_draw_page_change_color(3);
                if (draw_page == 1) {
                    store_process(1);
                } else if (draw_page == 2) {
                    store_process(2);
                } else if (draw_page == 3) {
                    store_process(3);
                }
                draw_page = 3;
                clean_draw_page();
                if (first_page3) {
                    block_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.draw_block);
                    show_process.setImageBitmap(block_bitmap);
                    first_page3 = false;
                } else {
                    block_bitmap = BitmapFactory.decodeFile("/sdcard/file_data/" + process_store_name + "_process3.png");
                    show_process.setImageBitmap(block_bitmap);
                }
            }
        });
        confirm_but.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                final double ans = Double.valueOf(input_tv5.getText().toString());
                //double real_ans =  Double.valueOf(measure_session2.bundle_width_value)*Double.valueOf(measure_session2.bundle_hight_value);

                final double double_value1 = Double.valueOf(input_tv1.getText().toString());
                final double double_value2 = Double.valueOf(input_tv3.getText().toString());
                final double double_value3 = Double.valueOf(input_tv7.getText().toString());
                final String operand1 = input_tv2.getText().toString();
                final String operand2 = input_tv6.getText().toString();

                final String fans=input_tv5.getText().toString();

                BigDecimal value1 = new BigDecimal(String.valueOf(double_value1));
                BigDecimal value2 = new BigDecimal(String.valueOf(double_value2));
                BigDecimal value3 = new BigDecimal(String.valueOf(double_value3));


                double calculate_ans = 0;
                double real_ans = 0;
                if ("+".equals(operand1) && "X".equals(operand2) && 2.0 == double_value3) {
                    calculate_ans = (value1.add(value2)).doubleValue();
                    BigDecimal temp = new BigDecimal(String.valueOf(calculate_ans));
                    calculate_ans = temp.multiply(value3).doubleValue();


                    BigDecimal real_width = new BigDecimal(measure_session2p.bundle_width_value);
                    BigDecimal real_hight = new BigDecimal(measure_session2p.bundle_hight_value);
                    real_ans = real_width.add(real_hight).multiply(value3).doubleValue();

                    if (operand1.equals("+") && operand2.equals("X") && 2.0 == double_value3 && ((String.valueOf(double_value1).equals(String.valueOf(Double.valueOf(measure_session2p.bundle_width_value))) && String.valueOf(double_value2).equals(String.valueOf(Double.valueOf(measure_session2p.bundle_hight_value)))) || (String.valueOf(double_value2).equals(String.valueOf(Double.valueOf(measure_session2p.bundle_width_value))) && String.valueOf(double_value1).equals(String.valueOf(Double.valueOf(measure_session2p.bundle_hight_value)))))) {
                        if (real_ans == calculate_ans && calculate_ans == ans) {
                            new AlertDialog.Builder(user_practice_p.this)
                                    .setCancelable(false)
                                    .setTitle("儲存練習紀錄")
                                    .setMessage("計算正確，儲存練習紀錄")
                                    .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            if (draw_page == 1) {
                                                store_process(1);
                                            } else if (draw_page == 2) {
                                                store_process(2);
                                            } else if (draw_page == 3) {
                                                store_process(3);
                                            }

                                            //儲存sqlite
                                            dbHelper = new SQLite(user_practice_p.this, "practice_recorde_data");
                                            String user_answer = next_one_activity2p.user_id + sdf.format(new Date());
                                            String object_pic = temp_pic_storename;

                                            long no = dbHelper.InsertSubmit_Homework_one2(next_one_activity2p.user_id, next_one_activity2p.measure_shap, String.valueOf(double_value1), String.valueOf(operand1), String.valueOf(double_value2), String.valueOf(ans),
                                                    object_pic, process_store_name + "_process.png", user_answer, measure_session2p.bundle_hight_value, measure_session2p.bundle_width_value);

                                            StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                                            StorageReference storageRef = mStorageRef.child(user_id).child("Perimeter");
                                            StorageReference uploadRef = storageRef.child(process_store_name + "_process.png");
                                            File x=new File(new File(Environment.getExternalStorageDirectory(),"file_data"), process_store_name +"_process.png");
                                            Uri tmpuri=Uri.fromFile(x);
                                            UploadTask uploadTask = uploadRef.putFile(tmpuri);
                                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                }
                                            });
                                            if(concept_area3.measure_shap2.equals("rectangle_m")){
                                                SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");//日期
                                                //儲存sqlite
                                                dbHelper = new SQLite(user_practice_p.this, "practice_recorde_data_m");
                                                String user_answer2 = distance_MainActivity.user_id+homework_list.question_id+sdf.format(new Date());
                                                String object_pic2 = measure_session1.temp_pic_storename;

                                                long no2 = dbHelper.InsertSubmit_Homework_second(distance_MainActivity.user_id,homework_list.question_id,
                                                        String.valueOf(double_value1),String.valueOf(operand1),String.valueOf(double_value2),String.valueOf(operand2),String.valueOf(double_value3),String.valueOf(ans),
                                                        object_pic2,process_store_name+"_process.png",user_answer2,measure_session1.bundle_hight_value,measure_session1.bundle_width_value);

                                                dbHelper.close();//關閉資料庫
                                                //Intent intent = new Intent();
                                                Bundle bundle = new Bundle();
                                                bundle.putString("user_id", distance_MainActivity.user_id);
                                                bundle.putString("question_id", homework_list.question_id);
                                                bundle.putString("value_1", String.valueOf(double_value1));
                                                bundle.putString("operand1", String.valueOf(operand1));
                                                bundle.putString("value_2", String.valueOf(double_value2));
                                                bundle.putString("operand2", String.valueOf(operand2));
                                                bundle.putString("value_3", String.valueOf(double_value3));
                                                bundle.putString("ans", String.valueOf(ans));
                                                bundle.putString("shape", concept_area3.measure_shap2);
                                                bundle.putString("object_pic", object_pic2);
                                                bundle.putString("user_calculate_pic", process_store_name+"_process.png");
                                                bundle.putString("user_answer", user_answer2);
                                                bundle.putString("object_hight", measure_session1.bundle_hight_value);
                                                bundle.putString("object_width", measure_session1.bundle_width_value);

                                                //intent.putExtras(bundle);
                                                //intent.setClass(user_practice_p.this, homework_list.class);
                                                //startActivity(intent);
                                                user_practice_p.this.finish();
                                            }
                                            dbHelper.close();//關閉資料庫

                                            //上傳firebase
                                            mDatabase = FirebaseDatabase.getInstance().getReference();
                                            DatabaseReference DatabaseRef=mDatabase.child(user_id).child("Perimeter");
                                            mDatabase.child(next_one_activity2p.user_id).child("rectangle"+tempsdf).child("長").setValue(double_value1 + "(cm)");
                                            mDatabase.child(next_one_activity2p.user_id).child("rectangle"+tempsdf).child("寬").setValue(double_value2 + "(cm)");
                                            mDatabase.child(next_one_activity2p.user_id).child("rectangle"+tempsdf).child("正確").setValue(ans + "(cm)");
                                            temp_pic_storename = next_one_activity2p.user_id +"_"+ sdf.format(new Date()) + "_rectangle";
                                            Uri file = Uri.fromFile(new File(bitmap.toString()));
                                            StorageReference riversRef = mStorageRef.child(next_one_activity2p.user_id+"/"+" rectangle"+"/"+file.getLastPathSegment());
                                            uploadTask = riversRef.putFile(file);
                                            uploadTask.addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getBaseContext(),"請開啟網路", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                            final DatabaseReference areaRef = DatabaseRef.child(temp_pic_storename);
                                            areaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if(dataSnapshot.hasChild(fans)){

                                                    }else{
                                                        areaRef.child("side1").setValue(double_value1+" cm");
                                                        areaRef.child("side2").setValue(double_value2+"cm");
                                                        areaRef.child("ans").setValue(ans+"cm");


                                                    }
                                                }@Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                }
                                            });



                                            user_practice_p.this.finish();
                                        }
                                    })
                                    .show();
                        } else {
                            dbHelper = new SQLite(user_practice_p.this, "record_action");
                            dbHelper.Insert_Action(Login.practice_page, "算式有誤");
                            dbHelper.close();//關閉資料庫
                            show_error_dialog("算式有誤!");

                            //上傳firebase
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            DatabaseReference DatabaseRef=mDatabase.child(user_id).child("Perimeter");

                            //答案計算錯誤
                            temp_pic_storename = next_one_activity2p.user_id +"_"+ sdf.format(new Date()) + "_rectangle_error1";
                            final DatabaseReference areaRef = DatabaseRef.child(temp_pic_storename);
                            areaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(fans)){

                                    }else{
                                        areaRef.child("side1").setValue(double_value1+" cm");
                                        areaRef.child("side2").setValue(double_value2+"cm");
                                        areaRef.child("ans").setValue(ans+"cm");


                                    }
                                }@Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });

                        }

                    } else {
                        dbHelper = new SQLite(user_practice_p.this, "record_action");
                        dbHelper.Insert_Action(Login.practice_page, "計算過程有誤");
                        dbHelper.close();//關閉資料庫
                        show_error_dialog("計算過程有誤!");
                        //上傳firebase
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference DatabaseRef=mDatabase.child(user_id).child("Perimeter");

                        //數值輸入錯誤and
                        temp_pic_storename = next_one_activity2p.user_id +"_"+ sdf.format(new Date()) + "_rectangle_error2";
                        final DatabaseReference areaRef = DatabaseRef.child(temp_pic_storename);
                        areaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild(fans)){

                                }else{
                                    areaRef.child("side1").setValue(double_value1+" cm");
                                    areaRef.child("side2").setValue(double_value2+"cm");
                                    areaRef.child("ans").setValue(ans+"cm");


                                }
                            }@Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }

                } else {
                    dbHelper = new SQLite(user_practice_p.this, "record_action");
                    dbHelper.Insert_Action(Login.practice_page, "算式有誤");
                    dbHelper.close();//關閉資料庫
                    show_error_dialog("算式有誤!");
                    //上傳firebase
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference DatabaseRef=mDatabase.child(user_id).child("Perimeter");

                    temp_pic_storename = next_one_activity2p.user_id +"_"+ sdf.format(new Date()) + "_rectangle_error3";
                    final DatabaseReference areaRef = DatabaseRef.child(temp_pic_storename);
                    areaRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(fans)){

                            }else{
                                areaRef.child("side1").setValue(double_value1+" cm");
                                areaRef.child("side2").setValue(double_value2+"cm");
                                areaRef.child("ans").setValue(ans+"cm");


                            }
                        }@Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });

                }

            }
        });

        //數字鍵盤物件
        keyboard_1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                put_value_in_tv("1");
                check_block_zero();
            }
        });
        keyboard_2.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                put_value_in_tv("2");
                check_block_zero();
            }
        });
        keyboard_3.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                put_value_in_tv("3");
                check_block_zero();
            }
        });
        keyboard_4.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                put_value_in_tv("4");
                check_block_zero();
            }
        });
        keyboard_5.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                put_value_in_tv("5");
                check_block_zero();
            }
        });
        keyboard_6.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                put_value_in_tv("6");
                check_block_zero();
            }
        });
        keyboard_7.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                put_value_in_tv("7");
                check_block_zero();
            }
        });
        keyboard_8.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                put_value_in_tv("8");
                check_block_zero();
            }
        });
        keyboard_9.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                put_value_in_tv("9");
                check_block_zero();
            }
        });
        keyboard_0.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_input_value.length() == 1) {
                    if (!(user_input_value.toString().contains("0"))) {
                        put_value_in_tv("0");
                        check_block_zero();
                    }
                } else {
                    put_value_in_tv("0");
                    check_block_zero();
                }
            }
        });
        keyboard_erase.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(0 == user_input_value.length())) {
                    user_input_value.delete(user_input_value.length() - 1, user_input_value.length());
                    chang_input_tv_value();
                    check_block_value();
                }

            }
        });
        keyboard_clean.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(0 == user_input_value.length())) {
                    user_input_value.delete(0, user_input_value.length());
                    chang_input_tv_value();
                    check_block_value();
                }
            }
        });
        keyboard_point.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(0 == user_input_value.length()) && !(user_input_value.toString().contains(".")))//如果沒有個位數不給打小數點,判斷有沒有打過.
                {
                    put_value_in_tv(".");
                }
            }
        });

        keyboard_plus.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_select_operand_input_tv == 1) {
                    input_tv2.setText("+");
                } else if (user_select_operand_input_tv == 2) {
                    input_tv6.setText("+");
                }
                check_block_value();
            }
        });
        keyboard_minus.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_select_operand_input_tv == 1) {
                    input_tv2.setText("-");
                } else if (user_select_operand_input_tv == 2) {
                    input_tv6.setText("-");
                }
                check_block_value();
            }
        });
        keyboard_multiply.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_select_operand_input_tv == 1) {
                    input_tv2.setText("X");
                } else if (user_select_operand_input_tv == 2) {
                    input_tv6.setText("X");
                }

                check_block_value();
            }
        });
        keyboard_division.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_select_operand_input_tv == 1) {
                    input_tv2.setText("÷");
                } else if (user_select_operand_input_tv == 2) {
                    input_tv6.setText("÷");
                }

                check_block_value();
            }
        });

        //使用者輸入物件
        input_tv1.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                keyboard_status = 1;
                inikeyborad_operand();
                input_tv1_bg.setVisibility(View.VISIBLE);
                user_input_value.delete(0, user_input_value.length());

                if (input_tv1.getText().toString().equals("長") || input_tv1.getText().toString().equals("底")) {
                    input_tv1.setText("");
                    input_tv1.setTextColor(Color.BLACK);
                    showkeyborad_num();
                    user_select_input_tv = 1;
                } else {
                    showkeyborad_num();
                    user_select_input_tv = 1;
                    user_input_value.append(input_tv1.getText().toString());
                }
            }
        });

        input_tv2.setOnClickListener(new Button.OnClickListener() {
            @Override

            public void onClick(View v) {

                input_tv2.setText("");
                input_tv2.setTextColor(Color.BLACK);
                keyboard_status = 2;
                user_select_operand_input_tv = 1;
                inikeyborad_num();
                input_tv2_bg.setVisibility(View.VISIBLE);
                showkeyborad_operand();
                check_block_value();
            }
        });
        input_tv3.setOnClickListener(new Button.OnClickListener() {
            @Override

            public void onClick(View v) {

                keyboard_status = 1;
                inikeyborad_operand();
                input_tv3_bg.setVisibility(View.VISIBLE);
                user_input_value.delete(0, user_input_value.length());
                if (input_tv3.getText().toString().equals("寬") || input_tv3.getText().toString().equals("高")) {
                    input_tv3.setText("");
                    input_tv3.setTextColor(Color.BLACK);
                    showkeyborad_num();
                    user_select_input_tv = 2;
                } else {
                    showkeyborad_num();
                    user_select_input_tv = 2;
                    user_input_value.append(input_tv3.getText().toString());
                }
            }
        });
        input_tv5.setOnClickListener(new Button.OnClickListener() {
            @Override

            public void onClick(View v) {

                keyboard_status = 1;
                inikeyborad_operand();
                input_tv5_bg.setVisibility(View.VISIBLE);
                user_input_value.delete(0, user_input_value.length());
                if (input_tv5.getText().toString().equals("周長(CM)")) {
                    input_tv5.setText("");
                    input_tv5.setTextColor(Color.BLACK);
                    showkeyborad_num();
                    user_select_input_tv = 3;
                } else {
                    showkeyborad_num();
                    user_select_input_tv = 3;
                    user_input_value.append(input_tv5.getText().toString());
                }
            }
        });
        input_tv6.setOnClickListener(new Button.OnClickListener() {
            @Override

            public void onClick(View v) {

                input_tv6.setText("");
                input_tv6.setTextColor(Color.BLACK);
                keyboard_status = 2;
                user_select_operand_input_tv = 2;
                inikeyborad_num();
                input_tv6_bg.setVisibility(View.VISIBLE);
                showkeyborad_operand();
                check_block_value();
            }
        });

        input_tv7.setOnClickListener(new Button.OnClickListener() {
            @Override

            public void onClick(View v) {
                keyboard_status = 1;
                inikeyborad_operand();
                input_tv7_bg.setVisibility(View.VISIBLE);
                user_input_value.delete(0, user_input_value.length());
                if (input_tv7.getText().toString().equals("2")) {
                    input_tv7.setText("");
                    input_tv7.setTextColor(Color.BLACK);
                    showkeyborad_num();
                    user_select_input_tv = 4;
                } else {
                    showkeyborad_num();
                    user_select_input_tv = 4;
                    user_input_value.append(input_tv7.getText().toString());
                }
            }
        });


        hint1_but.setOnClickListener(new Button.OnClickListener() {
            @Override

            public void onClick(View v) {
                dbHelper = new SQLite(user_practice_p.this, "record_action");
                dbHelper.Insert_Action(Login.practice_page, "圖形提示");
                dbHelper.close();//關閉資料庫

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("from", "practice");
                bundle.putString("path", next_one_activity2p.measure_shap);
                intent.putExtras(bundle);
                intent.setClass(user_practice_p.this, show_large_imagep.class);
                startActivity(intent);
                show_pic_large_mess.setVisibility(View.INVISIBLE);
            }
        });
        hint2_but.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper = new SQLite(user_practice_p.this, "record_action");
                dbHelper.Insert_Action(Login.practice_page, "算式提示");
                dbHelper.close();//關閉資料庫

                input_tv1.setTextColor(Color.GRAY);
                input_tv2.setTextColor(Color.GRAY);
                input_tv3.setTextColor(Color.GRAY);
                input_tv5.setTextColor(Color.GRAY);
                input_tv6.setTextColor(Color.GRAY);
                input_tv7.setTextColor(Color.GRAY);
                if (next_one_activity2p.measure_shap.equals("rectangle")) {
                    input_tv1.setText("長");
                    input_tv2.setText("+");
                    input_tv3.setText("寬");
                    input_tv5.setText("周長(CM)");
                    input_tv6.setText("X");
                    input_tv7.setText("2");
                } else if (next_one_activity2p.measure_shap.equals("parallel")) {
                    input_tv1.setText("底");
                    input_tv2.setText(" X");
                    input_tv3.setText("高");
                    input_tv5.setText("面積(CM²)");
                }
                check_block_value();
                input_tv1_bg.setVisibility(View.INVISIBLE);
                input_tv2_bg.setVisibility(View.INVISIBLE);
                input_tv3_bg.setVisibility(View.INVISIBLE);
                input_tv5_bg.setVisibility(View.INVISIBLE);
                input_tv6_bg.setVisibility(View.INVISIBLE);
                input_tv7_bg.setVisibility(View.INVISIBLE);
                keyboard_plus.setVisibility(View.INVISIBLE);
                keyboard_minus.setVisibility(View.INVISIBLE);
                keyboard_multiply.setVisibility(View.INVISIBLE);
                keyboard_division.setVisibility(View.INVISIBLE);

                keyboard_1.setVisibility(View.INVISIBLE);
                keyboard_2.setVisibility(View.INVISIBLE);
                keyboard_3.setVisibility(View.INVISIBLE);
                keyboard_4.setVisibility(View.INVISIBLE);
                keyboard_5.setVisibility(View.INVISIBLE);
                keyboard_6.setVisibility(View.INVISIBLE);
                keyboard_7.setVisibility(View.INVISIBLE);
                keyboard_8.setVisibility(View.INVISIBLE);
                keyboard_9.setVisibility(View.INVISIBLE);
                keyboard_0.setVisibility(View.INVISIBLE);
                keyboard_clean.setVisibility(View.INVISIBLE);
                keyboard_erase.setVisibility(View.INVISIBLE);

                keyboard_point.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void findview() {
        object_pic = (ImageView) findViewById(R.id.object_pic);
        width_tv = (TextView) findViewById(R.id.width_value);
        hight_tv = (TextView) findViewById(R.id.hight_value);
        erase = (Button) findViewById(R.id.erase);
        pen_size = (Button) findViewById(R.id.pen_size);
        pen_color = (Button) findViewById(R.id.pen_color);
        pen = (Button) findViewById(R.id.pen);
        clean = (Button) findViewById(R.id.clean);
        confirm_but = (Button) findViewById(R.id.confirm_but);
        hint2_but = (Button) findViewById(R.id.hint2_but);
        draw_page_1 = (Button) findViewById(R.id.draw_page_1);
        draw_page_2 = (Button) findViewById(R.id.draw_page_2);
        draw_page_3 = (Button) findViewById(R.id.draw_page_3);
        show_process = (ImageView) findViewById(R.id.show_process);


        //鍵盤數字介面
        keyboard_1 = (Button) findViewById(R.id.keyboard_1);
        keyboard_2 = (Button) findViewById(R.id.keyboard_2);
        keyboard_3 = (Button) findViewById(R.id.keyboard_3);
        keyboard_4 = (Button) findViewById(R.id.keyboard_4);
        keyboard_5 = (Button) findViewById(R.id.keyboard_5);
        keyboard_6 = (Button) findViewById(R.id.keyboard_6);
        keyboard_7 = (Button) findViewById(R.id.keyboard_7);
        keyboard_8 = (Button) findViewById(R.id.keyboard_8);
        keyboard_9 = (Button) findViewById(R.id.keyboard_9);
        keyboard_0 = (Button) findViewById(R.id.keyboard_0);
        keyboard_clean = (Button) findViewById(R.id.keyboard_clean);
        keyboard_erase = (Button) findViewById(R.id.keyboard_erase);

        keyboard_point = (Button) findViewById(R.id.keyboard_point);
        //鍵盤加減乘除介面
        keyboard_plus = (Button) findViewById(R.id.keyboard_plus);
        keyboard_minus = (Button) findViewById(R.id.keyboard_minus);
        keyboard_multiply = (Button) findViewById(R.id.keyboard_multiply);
        keyboard_division = (Button) findViewById(R.id.keyboard_division);


        //使用者輸入介面
        input_tv1 = (TextView) findViewById(R.id.input_tv1);
        input_tv2 = (TextView) findViewById(R.id.input_tv2);
        input_tv3 = (TextView) findViewById(R.id.input_tv3);
        input_tv4 = (TextView) findViewById(R.id.input_tv4);
        input_tv5 = (TextView) findViewById(R.id.input_tv5);
        input_tv6 = (TextView) findViewById(R.id.input_tv6);
        input_tv7 = (TextView) findViewById(R.id.input_tv7);

        input_tv1_bg = (ImageView) findViewById(R.id.input_tv1_bg);
        input_tv2_bg = (ImageView) findViewById(R.id.input_tv2_bg);
        input_tv3_bg = (ImageView) findViewById(R.id.input_tv3_bg);
        input_tv5_bg = (ImageView) findViewById(R.id.input_tv5_bg);
        input_tv6_bg = (ImageView) findViewById(R.id.input_tv6_bg);
        input_tv7_bg = (ImageView) findViewById(R.id.input_tv7_bg);

        show_input_hint = (TextView) findViewById(R.id.show_input_hint);
        hint1_but = (Button) findViewById(R.id.hint1_but);
        show_pic_large_mess = (ImageView) findViewById(R.id.show_pic_large_mess);

        right_lay = (AbsoluteLayout) findViewById(R.id.abs2);
        right_lay.addView(new MyView(this));
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFF000000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(6);

        mEmboss = new EmbossMaskFilter(new float[]{1, 1, 1}, 0.4f, 6, 3.5f);

        mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
    }

    public void put_value_in_tv(String i)//判斷是否超過字數限制
    {
        if (!(null == user_input_value)) {
            if (!(user_select_input_tv == 4)) {
                if (!(5 < user_input_value.length())) {
                    user_input_value.append(String.valueOf(i));
                    chang_input_tv_value();
                }
            } else {
                if (!(8 < user_input_value.length())) {
                    user_input_value.append(String.valueOf(i));
                    chang_input_tv_value();
                }
            }
        } else {
            user_input_value.append(String.valueOf(i));
            chang_input_tv_value();
        }

    }

    public void chang_input_tv_value()//改變輸入的值
    {
        switch (user_select_input_tv) {
            case 1:
                input_tv1.setText(user_input_value.toString());
                break;
            case 2:
                input_tv3.setText(user_input_value.toString());
                break;
            case 3:
                input_tv5.setText(user_input_value.toString());
                break;
            case 4:
                input_tv7.setText(user_input_value.toString());
                break;
        }
        check_block_value();
    }

    public void check_block_value() //確認空格填滿顯示確認button
    {

        if ((!("".equals(input_tv1.getText().toString())) && !("".equals(input_tv2.getText().toString())) && !("".equals(input_tv3.getText().toString()))
                && !("".equals(input_tv5.getText().toString())) && !("".equals(input_tv6.getText().toString())) && !("".equals(input_tv7.getText().toString()))) && !("長".equals(input_tv1.getText().toString()) || "  +".equals(input_tv2.getText().toString()) || "寬".equals(input_tv3.getText().toString()) || " X".equals(input_tv6.getText().toString()) || " 2".equals(input_tv7.getText().toString())
                || "周長(CM)".equals(input_tv5.getText().toString())) && !("0".equals(input_tv1.getText().toString())) && !("0".equals(input_tv3.getText().toString())) && !("0".equals(input_tv5.getText().toString()))&& !("0".equals(input_tv7.getText().toString()))
                && !("0.".equals(input_tv1.getText().toString())) && !("0.".equals(input_tv3.getText().toString())) && !("0.".equals(input_tv5.getText().toString()))&& !("0.".equals(input_tv7.getText().toString()))) {
            check_input_zero();

            if (!(check_input_zero)) {
                confirm_but.setVisibility(View.VISIBLE);

            } else {
                confirm_but.setVisibility(View.INVISIBLE);

            }
        } else {
            confirm_but.setVisibility(View.INVISIBLE);

        }
    }

    public void check_input_zero()//確認input 為零
    {

        if (Double.valueOf(input_tv1.getText().toString()) == 0 || Double.valueOf(input_tv3.getText().toString()) == 0
                || Double.valueOf(input_tv5.getText().toString()) == 0 || Double.valueOf(input_tv7.getText().toString()) == 0) {
            check_input_zero = true;
        } else {
            check_input_zero = false;
        }
    }

    public void check_block_zero() //確認空格以零開頭
    {
        if (user_input_value.length() == 2) {
            if ("0".equals(String.valueOf(user_input_value.charAt(0))) && !(".".equals(String.valueOf(user_input_value.charAt(1))))) {
                String temp = user_input_value.delete(0, 1).toString();
                user_input_value.delete(0, user_input_value.length());
                put_value_in_tv(temp);
            }
        }
    }

    public void inikeyborad_operand()//關閉運算元鍵盤
    {

        //關閉空格選取框框
        input_tv1_bg.setVisibility(View.INVISIBLE);
        input_tv2_bg.setVisibility(View.INVISIBLE);
        input_tv3_bg.setVisibility(View.INVISIBLE);
        input_tv5_bg.setVisibility(View.INVISIBLE);
        input_tv6_bg.setVisibility(View.INVISIBLE);
        input_tv7_bg.setVisibility(View.INVISIBLE);

        myAnimation_Alpha = new AlphaAnimation(1.0f, 0.1f);
        myAnimation_Alpha.setDuration(700);
        if (!(keyboard_status == 1)) {
            keyboard_plus.startAnimation(myAnimation_Alpha);
            keyboard_minus.startAnimation(myAnimation_Alpha);
            keyboard_multiply.startAnimation(myAnimation_Alpha);
            keyboard_division.startAnimation(myAnimation_Alpha);

        }

        keyboard_plus.setVisibility(View.INVISIBLE);
        keyboard_minus.setVisibility(View.INVISIBLE);
        keyboard_multiply.setVisibility(View.INVISIBLE);
        keyboard_division.setVisibility(View.INVISIBLE);

    }

    public void showkeyborad_operand() {
        myAnimation_Alpha = new AlphaAnimation(0.1f, 1.0f);
        myAnimation_Alpha.setDuration(1000);

        keyboard_plus.setVisibility(View.VISIBLE);
        keyboard_minus.setVisibility(View.VISIBLE);
        keyboard_multiply.setVisibility(View.VISIBLE);
        keyboard_division.setVisibility(View.VISIBLE);

        keyboard_plus.startAnimation(myAnimation_Alpha);
        keyboard_minus.startAnimation(myAnimation_Alpha);
        keyboard_multiply.startAnimation(myAnimation_Alpha);
        keyboard_division.startAnimation(myAnimation_Alpha);

    }

    public void inikeyborad_num()//關閉數字鍵盤
    {
        //關閉空格選取框框
        input_tv1_bg.setVisibility(View.INVISIBLE);
        input_tv2_bg.setVisibility(View.INVISIBLE);
        input_tv3_bg.setVisibility(View.INVISIBLE);
        input_tv5_bg.setVisibility(View.INVISIBLE);
        input_tv6_bg.setVisibility(View.INVISIBLE);
        input_tv7_bg.setVisibility(View.INVISIBLE);


        myAnimation_Alpha = new AlphaAnimation(1.0f, 0.1f);
        myAnimation_Alpha.setDuration(700);
        if (!(keyboard_status == 2)) {
            keyboard_1.startAnimation(myAnimation_Alpha);
            keyboard_2.startAnimation(myAnimation_Alpha);
            keyboard_3.startAnimation(myAnimation_Alpha);
            keyboard_4.startAnimation(myAnimation_Alpha);
            keyboard_5.startAnimation(myAnimation_Alpha);
            keyboard_6.startAnimation(myAnimation_Alpha);
            keyboard_7.startAnimation(myAnimation_Alpha);
            keyboard_8.startAnimation(myAnimation_Alpha);
            keyboard_9.startAnimation(myAnimation_Alpha);
            keyboard_0.startAnimation(myAnimation_Alpha);
            keyboard_clean.startAnimation(myAnimation_Alpha);
            keyboard_erase.startAnimation(myAnimation_Alpha);

            keyboard_point.startAnimation(myAnimation_Alpha);
        }
        keyboard_1.setVisibility(View.INVISIBLE);
        keyboard_2.setVisibility(View.INVISIBLE);
        keyboard_3.setVisibility(View.INVISIBLE);
        keyboard_4.setVisibility(View.INVISIBLE);
        keyboard_5.setVisibility(View.INVISIBLE);
        keyboard_6.setVisibility(View.INVISIBLE);
        keyboard_7.setVisibility(View.INVISIBLE);
        keyboard_8.setVisibility(View.INVISIBLE);
        keyboard_9.setVisibility(View.INVISIBLE);
        keyboard_0.setVisibility(View.INVISIBLE);
        keyboard_clean.setVisibility(View.INVISIBLE);
        keyboard_erase.setVisibility(View.INVISIBLE);

        keyboard_point.setVisibility(View.INVISIBLE);
    }

    public void showkeyborad_num()//啟動數字鍵盤
    {
        myAnimation_Alpha = new AlphaAnimation(0.1f, 1.0f);
        myAnimation_Alpha.setDuration(1000);

        keyboard_1.setVisibility(View.VISIBLE);
        keyboard_2.setVisibility(View.VISIBLE);
        keyboard_3.setVisibility(View.VISIBLE);
        keyboard_4.setVisibility(View.VISIBLE);
        keyboard_5.setVisibility(View.VISIBLE);
        keyboard_6.setVisibility(View.VISIBLE);
        keyboard_7.setVisibility(View.VISIBLE);
        keyboard_8.setVisibility(View.VISIBLE);
        keyboard_9.setVisibility(View.VISIBLE);
        keyboard_0.setVisibility(View.VISIBLE);
        keyboard_clean.setVisibility(View.VISIBLE);
        keyboard_erase.setVisibility(View.VISIBLE);

        keyboard_point.setVisibility(View.VISIBLE);
        keyboard_1.startAnimation(myAnimation_Alpha);
        keyboard_2.startAnimation(myAnimation_Alpha);
        keyboard_3.startAnimation(myAnimation_Alpha);
        keyboard_4.startAnimation(myAnimation_Alpha);
        keyboard_5.startAnimation(myAnimation_Alpha);
        keyboard_6.startAnimation(myAnimation_Alpha);
        keyboard_7.startAnimation(myAnimation_Alpha);
        keyboard_8.startAnimation(myAnimation_Alpha);
        keyboard_9.startAnimation(myAnimation_Alpha);
        keyboard_0.startAnimation(myAnimation_Alpha);
        keyboard_clean.startAnimation(myAnimation_Alpha);
        keyboard_erase.startAnimation(myAnimation_Alpha);

        keyboard_point.startAnimation(myAnimation_Alpha);
    }

    public void colorChanged(int color) {
        mPaint.setColor(color);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    public void show_error_dialog(String error) {
        new AlertDialog.Builder(user_practice_p.this)
                .setCancelable(false)
                .setTitle("警告")
                .setMessage(error)
                .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    //圖片合併
    private Bitmap mergeBitmap(Bitmap firstBitmap, Bitmap secondBitmap) {
        bitmap = Bitmap.createBitmap(firstBitmap.getWidth(), firstBitmap.getHeight(),
                firstBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(firstBitmap, new Matrix(), null);
        canvas.drawBitmap(secondBitmap, 0, 0, null);
        return bitmap;
    }

    //儲存process圖片
    public void store_block_process(int i) {
        try {
            File f = new File
                    (
                            Environment.getExternalStorageDirectory(), "file_data"
                    );
            File n = null;
            if (i == 1) {
                n = new File(f, process_store_name + "_process.png");
            } else if (i == 2) {
                n = new File(f, process_store_name + "_process2.png");
            } else if (i == 3) {
                n = new File(f, process_store_name + "_process3.png");
            }
            FileOutputStream out = new FileOutputStream(n.getAbsolutePath());
            block_bitmap.compress(Bitmap.CompressFormat.PNG, 5, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //儲存process圖片
    public void store_process(int i) {
        try {
            File f = new File
                    (
                            Environment.getExternalStorageDirectory(), "file_data"
                    );
            File n = null;
            if (i == 1) {
                n = new File(f, process_store_name + "_process.png");

                StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                StorageReference storageRef = mStorageRef.child(user_id).child("Perimeter");
                Uri uri = Uri.fromFile(n);
                StorageReference uploadRef = storageRef.child(process_store_name + "_process.png");
                UploadTask uploadTask = uploadRef.putFile(uri);
                Log.i("@@@is upload1@@@", String.valueOf(uploadRef));
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                });

            } else if (i == 2) {
                n = new File(f, process_store_name + "_process2.png");

                StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                StorageReference storageRef = mStorageRef.child(user_id).child("Perimeter");
                Uri uri = Uri.fromFile(n);
                StorageReference uploadRef = storageRef.child(process_store_name + "_process2.png");
                UploadTask uploadTask = uploadRef.putFile(uri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                });
            } else if (i == 3) {
                n = new File(f, process_store_name + "_process3.png");
                StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
                StorageReference storageRef = mStorageRef.child(user_id).child("Perimeter");
                Uri uri = Uri.fromFile(n);
                StorageReference uploadRef = storageRef.child(process_store_name + "_process3.png");
                UploadTask uploadTask = uploadRef.putFile(uri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                });
            }
            FileOutputStream out = new FileOutputStream(n.getAbsolutePath());
            mergeBitmap(block_bitmap, mBitmap).compress(Bitmap.CompressFormat.PNG, 5, out);  //合併後儲存為q3.png
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clean_draw_page() {
        mBitmap.eraseColor(Color.TRANSPARENT);
        mCanvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        right_lay.invalidate();
    }

    //click draw 改變顏色
    public void click_draw_page_change_color(int i) {
        if (i == 1) {
            draw_page_1.setBackgroundColor(0xffdddddd);
            draw_page_2.setBackgroundColor(0xffffffff);
            draw_page_3.setBackgroundColor(0xffffffff);
        } else if (i == 2) {
            draw_page_1.setBackgroundColor(0xffffffff);
            draw_page_2.setBackgroundColor(0xffdddddd);
            draw_page_3.setBackgroundColor(0xffffffff);
        } else if (i == 3) {
            draw_page_1.setBackgroundColor(0xffffffff);
            draw_page_2.setBackgroundColor(0xffffffff);
            draw_page_3.setBackgroundColor(0xffdddddd);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(user_practice_p.this)
                    .setTitle("警告")
                    .setMessage("確定要放棄此次練習嗎?")
                    .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (practice_record.from_practice_record_page) {
                                Intent intent = new Intent();
                                intent.setClass(user_practice_p.this, practice_record.class);
                                startActivity(intent);
                                user_practice_p.this.finish();
                            } else {
                                user_practice_p.this.finish();
                            }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mBitmap.isRecycled() == false)
                mBitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (bitmap.isRecycled() == false)
                bitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (block_bitmap.isRecycled() == false)
                block_bitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.gc();
    }

    public class MyView extends View {


        private static final float TOUCH_TOLERANCE = 4;
        private float mX, mY;

        public MyView(Context c) {
            super(c);
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
            mCanvas.drawColor(Color.TRANSPARENT);//儲存畫板顏色
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.TRANSPARENT);//畫板顏色
            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath(mPath, mPaint);
        }

        private void touch_start(float x, float y) {
            mPath.reset();
            //mPath.quadTo(x, x, (x + 1)/2, (y + 1)/2);
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
            mPath.lineTo(mX + 1, mY + 1);
        }

        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
            }
        }

        private void touch_up() {
            mPath.lineTo(mX, mY);
            mPath.setLastPoint(mX, mY);
            // commit the path to our offscreen
            mCanvas.drawPath(mPath, mPaint);
            // kill this so we don't double draw
            mPath.reset();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }
    }
}
