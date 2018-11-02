package lab.hwang.ug;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
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

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class rank_test extends Activity {
    //class
    private SQLite dbHelper;//操作SQLite資料庫
    //傳送放大圖的路徑
    //public String select_pic=homework_list.select_shap;


    //畫板
    private static final float MINP = 0.25f;
    private static final float MAXP = 0.75f;

    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Path mPath;
    private Paint mBitmapPaint;
    private Paint       mPaint;
    private MaskFilter mEmboss;
    private MaskFilter  mBlur;
    AbsoluteLayout right_lay;
    ImageView show_pic_large_mess;
    Bitmap bitmap,block_bitmap;

    //左上拍照的物件
    ImageView object_pic;
    TextView top_width_tv,button_width_tv,hight_tv;

    //左下運算
    private Animation myAnimation_Alpha;
    private StringBuffer user_input_value = new StringBuffer();
    private int user_select_input_tv=0,user_select_operand_input_tv=0;

    private int keyboard_status=0; //0初始化,1數字鍵盤,2運算元
    Button erase,pen_size,pen_color,pen,clean,draw_page_1,draw_page_2,draw_page_3;//右邊畫版
    Button keyboard_1,keyboard_2,keyboard_3,keyboard_4,keyboard_5,keyboard_6,keyboard_7,keyboard_8,keyboard_9,keyboard_0,
            keyboard_clean,keyboard_erase,keyboard_point;//數字鍵盤

    Button keyboard_plus,keyboard_minus,keyboard_multiply,keyboard_division;//運算元鍵盤
    String select_input_style="1";

    //style 1
    TextView style_1_input_tv1,style_1_input_tv2,style_1_input_tv3,style_1_input_tv4,style_1_input_tv5;
    ImageView style_1_input_tv1_bg,style_1_input_tv2_bg,style_1_input_tv3_bg,style_1_input_tv5_bg,show_process;

    //style 2
    TextView style_2_input_tv1,style_2_input_tv2,style_2_input_tv3,style_2_input_tv4,style_2_input_tv5,style_2_input_tv6,style_2_input_tv7;
    ImageView style_2_input_tv1_bg,style_2_input_tv2_bg,style_2_input_tv3_bg,style_2_input_tv4_bg,style_2_input_tv5_bg,style_2_input_tv7_bg;

    //style 3
    TextView input_tv1,input_tv2,input_tv3,input_tv4,input_tv5,input_tv6,input_tv7,input_tv8,input_tv9,textView1,textView2;
    ImageView input_tv1_bg,input_tv2_bg,input_tv3_bg,input_tv4_bg,input_tv5_bg,input_tv6_bg,input_tv7_bg,input_tv9_bg;

    String stu_object_pic[],stu_object_hight[],stu_object_top_width[],stu_object_width[],test_shape[];//接收題目的array
    Integer question_level_2[],question_level_3[],amout_question_level_2,amout_question_level_3;
    int total_test_number = 0,show_count_test_num=0;; //題目從0開始
    MyCount mc;//計時器
    TextView timer_tv,test_title;
    Button hint1_but,confirm_but;
    ImageView ca_hint_pic;
    Boolean check_input_zero=true;
    String test_date;
    int a;//亂數產生題目
    Button test_confirm_but;
    Boolean check_input_style;//判斷選擇公式是否正確
    String correctness;//sqlite正確與否
    int count_right_num=0,count_error_num=0;
    String tempsdf,process_store_name,temp_process_store_name;
    SimpleDateFormat sdf;
    int draw_page=1;
    Boolean first_page1=true,first_page2=true,first_page3=true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rank_test);
        findview();
        block_bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.draw_block);
        show_process.setImageBitmap(block_bitmap);

        ini_input_block();//所有input_view 清空
        get_all_test_data();//取得所有考題
        ImageView iv = new ImageView(rank_test.this);
        iv.setImageResource(R.drawable.show_rules_im);
        new AlertDialog.Builder(rank_test.this)
                .setView(iv)
                .setCancelable(false)
                .setPositiveButton("開始",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if(!(total_test_number<1))
                        {
                            ContentValues args = new ContentValues();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//日期
                            test_date = sdf.format(new Date());
                            temp_process_store_name=next_one_activity2.user_id+test_date;
                            //mc = new MyCount(11000, 1000);
                            mc = new MyCount(361000, 1000);
                            mc.start();
                            rand_get_test();
                        }else
                        {
                            Toast.makeText(rank_test.this, "題目數不足，無法進行測驗!", Toast.LENGTH_LONG).show();
                            rank_test.this.finish();
                        }
                    }
                })
                .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        rank_test.this.finish();
                    }
                })
                .show();


        pen.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pen_color.setBackgroundResource(R.drawable.paint_color);
                pen_size.setBackgroundResource(R.drawable.paint_size);
                mPaint.setColor(0xFF000000);
                mPaint.setStrokeWidth(6);
                pen_size.setClickable(true);
                pen_color.setClickable(true);
            }
        });
        pen_size.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(rank_test.this)//選擇顯示圖示
                        .setTitle("請選擇鉛筆粗細")
                        .setItems(R.array.pen_size,
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int whichcountry)
                                    {
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

        pen_color.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(rank_test.this)//選擇顯示圖示
                        .setTitle("請選擇鉛筆粗細")
                        .setItems(R.array.pen_color,
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int whichcountry)
                                    {
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
        erase.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mPaint.setColor(Color.WHITE);
                mPaint.setStrokeWidth(30);
                pen_size.setClickable(false);
                pen_color.setClickable(false);
                pen_color.setBackgroundResource(R.drawable.paint_color_x);
                pen_size.setBackgroundResource(R.drawable.paint_size_x);
            }
        });
        clean.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                clean_draw_page();
                block_bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.draw_block);
                show_process.setImageBitmap(block_bitmap);
            }
        });
        test_confirm_but.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                rand_get_test();
            }
        });
        draw_page_1.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                click_draw_page_change_color(1);
                if(draw_page==1)
                {
                    store_process(1);
                    first_page1=false;
                }else if(draw_page==2)
                {
                    store_process(2);
                }else if (draw_page==3)
                {
                    store_process(3);
                }
                draw_page=1;
                if(first_page1)
                {
                    clean_draw_page();
                    block_bitmap = BitmapFactory.decodeFile("/sdcard/file_data/"+process_store_name+"_process.png");
                    show_process.setImageBitmap(block_bitmap);
                }else
                {
                    clean_draw_page();
                    block_bitmap = BitmapFactory.decodeFile("/sdcard/file_data/"+process_store_name+"_process.png");
                    show_process.setImageBitmap(block_bitmap);
                }
            }
        });
        draw_page_2.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                click_draw_page_change_color(2);
                if(draw_page==1)
                {
                    store_process(1);
                }else if(draw_page==2)
                {
                    store_process(2);
                }else if (draw_page==3)
                {
                    store_process(3);
                }
                draw_page=2;
                clean_draw_page();
                if(first_page2)
                {
                    block_bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.draw_block);
                    show_process.setImageBitmap(block_bitmap);
                    first_page2=false;
                }else
                {
                    block_bitmap = BitmapFactory.decodeFile("/sdcard/file_data/"+process_store_name+"_process2.png");
                    show_process.setImageBitmap(block_bitmap);
                }
            }
        });
        draw_page_3.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                click_draw_page_change_color(3);
                if(draw_page==1)
                {
                    store_process(1);
                }else if(draw_page==2)
                {
                    store_process(2);
                }else if (draw_page==3)
                {
                    store_process(3);
                }
                draw_page=3;
                clean_draw_page();
                if(first_page3)
                {
                    block_bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.draw_block);
                    show_process.setImageBitmap(block_bitmap);
                    first_page3=false;
                }else
                {
                    block_bitmap = BitmapFactory.decodeFile("/sdcard/file_data/"+process_store_name+"_process3.png");
                    show_process.setImageBitmap(block_bitmap);
                }
            }
        });
        confirm_but.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                new AlertDialog.Builder(rank_test.this)
                        .setTitle("確認")
                        .setMessage("確定要送出答案嗎?")
                        .setPositiveButton("確認",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which)
                            {
                                String process_pic="";//計算過程圖片
                                if(test_shape[a].equals("rectangle"))
                                {
                                    if(select_input_style.equals("1"))
                                    {
                                        check_input_style = true;
                                    }
                                    else
                                    {
                                        check_input_style = false;
                                    }
                                }else if (test_shape[a].equals("triangle"))
                                {
                                    if(select_input_style.equals("3"))
                                    {
                                        check_input_style = true;
                                    }
                                    else
                                    {
                                        check_input_style = false;
                                    }
                                }else if (test_shape[a].equals("trapezoidal"))
                                {
                                    if(select_input_style.equals("5"))
                                    {
                                        check_input_style = true;
                                    }
                                    else
                                    {
                                        check_input_style = false;
                                    }
                                }else if (test_shape[a].equals("parallel"))
                                {
                                    if(select_input_style.equals("2"))
                                    {
                                        check_input_style = true;
                                    }
                                    else
                                    {
                                        check_input_style = false;
                                    }
                                }else if (test_shape[a].equals("diamond"))
                                {
                                    if(select_input_style.equals("4"))
                                    {
                                        check_input_style = true;
                                    }
                                    else
                                    {
                                        check_input_style = false;
                                    }
                                }

                                //儲存計算過程
                                if(draw_page==1)
                                {
                                    store_process(1);
                                }else if(draw_page==2)
                                {
                                    store_process(2);
                                }else if (draw_page==3)
                                {
                                    store_process(3);
                                }
                                if(first_page2)
                                {
                                    clean_draw_page();
                                    block_bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.draw_block);
                                    show_process.setImageBitmap(block_bitmap);
                                    store_process(2);
                                }
                                if(first_page3)
                                {
                                    clean_draw_page();
                                    block_bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.draw_block);
                                    show_process.setImageBitmap(block_bitmap);
                                    store_process(3);
                                }

                                if(select_input_style.equals("1")||select_input_style.equals("2"))
                                {
                                    final double ans = Double.valueOf(style_1_input_tv5.getText().toString());
                                    final double double_value1 = Double.valueOf(style_1_input_tv1.getText().toString());
                                    final String operand1 = style_1_input_tv2.getText().toString();
                                    final double double_value2 = Double.valueOf(style_1_input_tv3.getText().toString());
                                    BigDecimal value1 = new BigDecimal(String.valueOf(double_value1));
                                    BigDecimal value2 = new BigDecimal(String.valueOf(double_value2));
                                    if(check_input_style)
                                    {
                                        if(operand1.equals("X")&&((double_value1==Double.valueOf(stu_object_width[a])&&double_value2==Double.valueOf(stu_object_hight[a]))||
                                                (double_value2==Double.valueOf(stu_object_width[a])&&double_value1==Double.valueOf(stu_object_hight[a]))))
                                        {
                                            BigDecimal real_width = new BigDecimal(stu_object_width[a]);
                                            BigDecimal real_hight = new BigDecimal(stu_object_hight[a]);
                                            double real_ans=real_width.multiply(real_hight).doubleValue();
                                            if(real_ans==ans)
                                            {
                                                correctness="true";
                                            }else
                                            {
                                                correctness="計算過程有誤";
                                            }
                                        }else
                                        {
                                            correctness="計算公式有誤";
                                        }
                                    }else
                                    {
                                        correctness="選擇計算公式有誤";
                                    }
                                    //儲存sqlite
                                    dbHelper = new SQLite(rank_test.this, "rank_record");
                                    /*long no = dbHelper.Insert_rank_test_record1(next_one_activity2.user_id,test_date,
                                            String.valueOf(double_value1),String.valueOf(operand1),String.valueOf(double_value2),
                                            String.valueOf(ans),stu_object_pic[a],process_store_name+"_process.png",test_shape[a],stu_object_hight[a],stu_object_width[a],stu_object_top_width[a],correctness,select_input_style);

                                    dbHelper.close();//關閉資料庫*/
                                }else if(select_input_style.equals("3")||select_input_style.equals("4"))
                                {
                                    final double ans = Double.valueOf(style_2_input_tv7.getText().toString());
                                    final double double_value1 = Double.valueOf(style_2_input_tv1.getText().toString());
                                    final double double_value2 = Double.valueOf(style_2_input_tv3.getText().toString());
                                    final double double_value3 = Double.valueOf(style_2_input_tv5.getText().toString());
                                    final String operand1 = style_2_input_tv2.getText().toString();
                                    final String operand2 = style_2_input_tv4.getText().toString();
                                    BigDecimal value1 = new BigDecimal(String.valueOf(double_value1));
                                    BigDecimal value2 = new BigDecimal(String.valueOf(double_value2));
                                    if(check_input_style)
                                    {
                                        if("X".equals(operand1)&&"÷".equals(operand2)&&double_value3==2&&((double_value1==Double.valueOf(stu_object_width[a])&&double_value2==Double.valueOf(stu_object_hight[a]))
                                                ||(double_value2==Double.valueOf(stu_object_width[a])&&double_value1==Double.valueOf(stu_object_hight[a]))))
                                        {

                                            BigDecimal real_width = new BigDecimal(stu_object_width[a]);
                                            BigDecimal real_hight = new BigDecimal(stu_object_hight[a]);
                                            double real_ans=real_width.multiply(real_hight).doubleValue();

                                            if(real_ans==(ans*2))
                                            {
                                                correctness="true";
                                            }else
                                            {
                                                correctness="計算過程有誤";
                                            }
                                        }else
                                        {
                                            correctness="計算公式有誤";
                                        }
                                    }else
                                    {
                                        correctness="選擇計算公式有誤";
                                    }
                                    //儲存sqlite
                                    dbHelper = new SQLite(rank_test.this, "rank_record");
                                    long no = dbHelper.Insert_rank_test_record2(next_one_activity2.user_id,test_date,
                                            String.valueOf(double_value1),String.valueOf(operand1),String.valueOf(double_value2),String.valueOf(operand2),String.valueOf(double_value3),
                                            String.valueOf(ans),stu_object_pic[a],process_store_name+"_process.png",test_shape[a],stu_object_hight[a],stu_object_width[a],stu_object_top_width[a],correctness,select_input_style);

                                    dbHelper.close();//關閉資料庫
                                }else if(select_input_style.equals("5"))
                                {
                                    final double ans = Double.valueOf(input_tv9.getText().toString());
                                    final double double_value1 = Double.valueOf(input_tv1.getText().toString());
                                    final double double_value2 = Double.valueOf(input_tv3.getText().toString());
                                    final double double_value3 = Double.valueOf(input_tv5.getText().toString());
                                    final double double_value4 = Double.valueOf(input_tv7.getText().toString());
                                    final String operand1 = input_tv2.getText().toString();
                                    final String operand2 = input_tv4.getText().toString();
                                    final String operand3 = input_tv6.getText().toString();
                                    BigDecimal value1 = new BigDecimal(String.valueOf(double_value1));
                                    BigDecimal value2 = new BigDecimal(String.valueOf(double_value2));
                                    BigDecimal value3 = new BigDecimal(String.valueOf(double_value3));
                                    if(check_input_style)
                                    {
                                        if(("+".equals(operand1)&&"X".equals(operand2)&&"÷".equals(operand3)&&2.0==double_value4&&
                                                ((double_value1==Double.valueOf(stu_object_width[a])&&double_value2==Double.valueOf(stu_object_top_width[a]))||(
                                                        double_value2==Double.valueOf(stu_object_width[a])&&double_value1==Double.valueOf(stu_object_top_width[a]))
                                                )&&double_value3==Double.valueOf(stu_object_hight[a])))
                                        {
                                            BigDecimal real_top_width = new BigDecimal(stu_object_top_width[a]);
                                            BigDecimal real_width = new BigDecimal(stu_object_width[a]);
                                            BigDecimal real_hight = new BigDecimal(stu_object_hight[a]);
                                            double real_ans=real_top_width.add(real_width).multiply(real_hight).doubleValue();

                                            if(real_ans==(ans*2))
                                            {
                                                correctness="true";
                                            }else
                                            {
                                                correctness="計算過程有誤";
                                            }
                                        }else
                                        {
                                            correctness="計算公式有誤";
                                        }
                                    }else
                                    {
                                        correctness="選擇計算公式有誤";
                                    }

                                    //儲存sqlite
                                    dbHelper = new SQLite(rank_test.this, "rank_record");
                                    long no = dbHelper.Insert_rank_test_record3(next_one_activity2.user_id,test_date,
                                            String.valueOf(double_value1),String.valueOf(operand1),String.valueOf(double_value2),String.valueOf(operand2),String.valueOf(double_value3),
                                            String.valueOf(operand3),String.valueOf(double_value4),String.valueOf(ans),
                                            stu_object_pic[a],process_store_name+"_process.png",test_shape[a],stu_object_hight[a],stu_object_width[a],stu_object_top_width[a],correctness,select_input_style);

                                    dbHelper.close();//關閉資料庫

                                }

                                if(correctness.equals("true"))
                                    count_right_num++;
                                else
                                    count_error_num++;

                                //右邊畫佈清空
                                clean_draw_page();
                                block_bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.draw_block);
                                show_process.setImageBitmap(block_bitmap);

                                //input clean
                                ini_input_block();

                                //重新出題目
                                rand_get_test();
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

        //數字鍵盤物件
        keyboard_1.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                put_value_in_tv("1");
                check_block_zero();
            }
        });
        keyboard_2.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                put_value_in_tv("2");
                check_block_zero();
            }
        });
        keyboard_3.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                put_value_in_tv("3");
                check_block_zero();
            }
        });
        keyboard_4.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                put_value_in_tv("4");
                check_block_zero();
            }
        });
        keyboard_5.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                put_value_in_tv("5");
                check_block_zero();
            }
        });
        keyboard_6.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                put_value_in_tv("6");
                check_block_zero();
            }
        });
        keyboard_7.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                put_value_in_tv("7");
                check_block_zero();
            }
        });
        keyboard_8.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                put_value_in_tv("8");
                check_block_zero();
            }
        });
        keyboard_9.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                put_value_in_tv("9");
                check_block_zero();
            }
        });
        keyboard_0.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(user_input_value.length()==1)
                {
                    if(!(user_input_value.toString().contains("0")))
                    {
                        put_value_in_tv("0");
                        check_block_zero();
                    }
                }else
                {
                    put_value_in_tv("0");
                    check_block_zero();
                }
            }
        });
        keyboard_erase.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!(0==user_input_value.length()))
                {
                    user_input_value.delete(user_input_value.length() - 1, user_input_value.length());
                    chang_input_tv_value();
                    check_block_value();
                }

            }
        });
        keyboard_clean.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!(0==user_input_value.length()))
                {
                    user_input_value.delete(0, user_input_value.length());
                    chang_input_tv_value();
                    check_block_value();
                }
            }
        });
        keyboard_point.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(!(0==user_input_value.length()) && !(user_input_value.toString().contains(".")))//如果沒有個位數不給打小數點,判斷有沒有打過.
                {
                    put_value_in_tv(".");
                }
            }
        });

        keyboard_plus.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(user_select_operand_input_tv==1)
                {
                    input_tv2.setText("+");
                }else if(user_select_operand_input_tv==2)
                {
                    input_tv4.setText("+");
                }else if(user_select_operand_input_tv==3)
                {
                    input_tv6.setText("+");
                }else if(user_select_operand_input_tv==4)
                {
                    style_1_input_tv2.setText("+");
                }else if(user_select_operand_input_tv==5)
                {
                    style_2_input_tv2.setText("+");
                }else if(user_select_operand_input_tv==6)
                {
                    style_2_input_tv4.setText("+");
                }
                check_block_value();

            }
        });
        keyboard_minus.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(user_select_operand_input_tv==1)
                {
                    input_tv2.setText("-");
                }else if(user_select_operand_input_tv==2)
                {
                    input_tv4.setText("-");
                }else if(user_select_operand_input_tv==3)
                {
                    input_tv6.setText("-");
                }else if(user_select_operand_input_tv==4)
                {
                    style_1_input_tv2.setText("-");
                }else if(user_select_operand_input_tv==5)
                {
                    style_2_input_tv2.setText("-");
                }else if(user_select_operand_input_tv==6)
                {
                    style_2_input_tv4.setText("-");
                }
                check_block_value();
            }
        });
        keyboard_multiply.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(user_select_operand_input_tv==1)
                {
                    input_tv2.setText("X");
                }else if(user_select_operand_input_tv==2)
                {
                    input_tv4.setText("X");
                }else if(user_select_operand_input_tv==3)
                {
                    input_tv6.setText("X");
                }else if(user_select_operand_input_tv==4)
                {
                    style_1_input_tv2.setText("X");
                }else if(user_select_operand_input_tv==5)
                {
                    style_2_input_tv2.setText("X");
                }else if(user_select_operand_input_tv==6)
                {
                    style_2_input_tv4.setText("X");
                }
                check_block_value();
            }
        });
        keyboard_division.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(user_select_operand_input_tv==1)
                {
                    input_tv2.setText("÷");
                }else if(user_select_operand_input_tv==2)
                {
                    input_tv4.setText("÷");
                }else if(user_select_operand_input_tv==3)
                {
                    input_tv6.setText("÷");
                }else if(user_select_operand_input_tv==4)
                {
                    style_1_input_tv2.setText("÷");
                }else if(user_select_operand_input_tv==5)
                {
                    style_2_input_tv2.setText("÷");
                }else if(user_select_operand_input_tv==6)
                {
                    style_2_input_tv4.setText("÷");
                }
                check_block_value();
            }
        });

        //使用者輸入物件 style1
        style_1_input_tv1.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                keyboard_status=1;
                inikeyborad_operand();
                style_1_input_tv1_bg.setVisibility(View.VISIBLE);
                user_input_value.delete(0, user_input_value.length());
                showkeyborad_num();
                user_select_input_tv=6;
                user_input_value.append(style_1_input_tv1.getText().toString());

            }
        });

        style_1_input_tv2.setOnClickListener(new Button.OnClickListener()
        {
            @Override

            public void onClick(View v)
            {
                style_1_input_tv2.setText("");
                style_1_input_tv2.setTextColor(Color.BLACK);
                keyboard_status=2;
                user_select_operand_input_tv=4;
                inikeyborad_num();
                style_1_input_tv2_bg.setVisibility(View.VISIBLE);
                showkeyborad_operand();
                check_block_value();
            }
        });
        style_1_input_tv3.setOnClickListener(new Button.OnClickListener()
        {
            @Override

            public void onClick(View v)
            {
                keyboard_status=1;
                inikeyborad_operand();
                style_1_input_tv3_bg.setVisibility(View.VISIBLE);
                user_input_value.delete(0, user_input_value.length());
                showkeyborad_num();
                user_select_input_tv=7;
                user_input_value.append(style_1_input_tv3.getText().toString());
            }
        });
        style_1_input_tv5.setOnClickListener(new Button.OnClickListener()
        {
            @Override

            public void onClick(View v)
            {
                keyboard_status=1;
                inikeyborad_operand();
                style_1_input_tv5_bg.setVisibility(View.VISIBLE);
                user_input_value.delete(0, user_input_value.length());
                showkeyborad_num();
                user_select_input_tv=8;
                user_input_value.append(style_1_input_tv5.getText().toString());
            }
        });
        //使用者輸入物件 style2
        style_2_input_tv1.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                keyboard_status=1;
                inikeyborad_operand();
                style_2_input_tv1_bg.setVisibility(View.VISIBLE);
                user_input_value.delete(0, user_input_value.length());
                showkeyborad_num();
                user_select_input_tv=9;
                user_input_value.append(style_2_input_tv1.getText().toString());
            }
        });

        style_2_input_tv2.setOnClickListener(new Button.OnClickListener()
        {
            @Override

            public void onClick(View v)
            {
                style_2_input_tv2.setText("");
                style_2_input_tv2.setTextColor(Color.BLACK);
                keyboard_status=2;
                user_select_operand_input_tv=5;
                inikeyborad_num();
                style_2_input_tv2_bg.setVisibility(View.VISIBLE);
                showkeyborad_operand();
                check_block_value();
            }
        });
        style_2_input_tv3.setOnClickListener(new Button.OnClickListener()
        {
            @Override

            public void onClick(View v)
            {
                keyboard_status=1;
                inikeyborad_operand();
                style_2_input_tv3_bg.setVisibility(View.VISIBLE);
                user_input_value.delete(0, user_input_value.length());
                showkeyborad_num();
                user_select_input_tv=10;
                user_input_value.append(style_2_input_tv3.getText().toString());
            }
        });
        style_2_input_tv4.setOnClickListener(new Button.OnClickListener()
        {
            @Override

            public void onClick(View v)
            {
                style_2_input_tv4.setText("");
                style_2_input_tv4.setTextColor(Color.BLACK);
                keyboard_status=2;
                user_select_operand_input_tv=6;
                inikeyborad_num();
                style_2_input_tv4_bg.setVisibility(View.VISIBLE);
                showkeyborad_operand();
                check_block_value();
            }
        });
        style_2_input_tv5.setOnClickListener(new Button.OnClickListener()
        {
            @Override

            public void onClick(View v)
            {
                keyboard_status=1;
                inikeyborad_operand();
                style_2_input_tv5_bg.setVisibility(View.VISIBLE);
                user_input_value.delete(0, user_input_value.length());
                showkeyborad_num();
                user_select_input_tv=11;
                user_input_value.append(style_2_input_tv5.getText().toString());
            }
        });
        style_2_input_tv7.setOnClickListener(new Button.OnClickListener()
        {
            @Override

            public void onClick(View v)
            {
                keyboard_status=1;
                inikeyborad_operand();
                style_2_input_tv7_bg.setVisibility(View.VISIBLE);
                user_input_value.delete(0, user_input_value.length());
                showkeyborad_num();
                user_select_input_tv=12;
                user_input_value.append(style_2_input_tv7.getText().toString());
            }
        });
        //使用者輸入物件 style3
        input_tv1.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                keyboard_status=1;
                inikeyborad_operand();
                input_tv1_bg.setVisibility(View.VISIBLE);
                user_input_value.delete(0, user_input_value.length());
                showkeyborad_num();
                user_select_input_tv=1;
                user_input_value.append(input_tv1.getText().toString());
            }
        });

        input_tv2.setOnClickListener(new Button.OnClickListener()
        {
            @Override

            public void onClick(View v)
            {
                input_tv2.setText("");
                input_tv2.setTextColor(Color.BLACK);
                keyboard_status=2;
                user_select_operand_input_tv=1;
                inikeyborad_num();
                input_tv2_bg.setVisibility(View.VISIBLE);
                showkeyborad_operand();
                check_block_value();

            }
        });
        input_tv3.setOnClickListener(new Button.OnClickListener()
        {
            @Override

            public void onClick(View v)
            {
                keyboard_status=1;
                inikeyborad_operand();
                input_tv3_bg.setVisibility(View.VISIBLE);
                user_input_value.delete(0, user_input_value.length());
                showkeyborad_num();
                user_select_input_tv=2;
                user_input_value.append(input_tv3.getText().toString());

            }
        });
        input_tv4.setOnClickListener(new Button.OnClickListener()
        {
            @Override

            public void onClick(View v)
            {
                input_tv4.setText("");
                input_tv4.setTextColor(Color.BLACK);
                keyboard_status=2;
                user_select_operand_input_tv=2;
                inikeyborad_num();
                input_tv4_bg.setVisibility(View.VISIBLE);
                showkeyborad_operand();
                check_block_value();

            }
        });
        input_tv5.setOnClickListener(new Button.OnClickListener()
        {
            @Override

            public void onClick(View v)
            {
                keyboard_status=1;
                inikeyborad_operand();
                input_tv5_bg.setVisibility(View.VISIBLE);
                user_input_value.delete(0, user_input_value.length());
                showkeyborad_num();
                user_select_input_tv=3;
                user_input_value.append(input_tv5.getText().toString());

            }
        });
        input_tv6.setOnClickListener(new Button.OnClickListener()
        {
            @Override

            public void onClick(View v)
            {
                input_tv6.setText("");
                input_tv6.setTextColor(Color.BLACK);
                keyboard_status=2;
                user_select_operand_input_tv=3;
                inikeyborad_num();
                input_tv6_bg.setVisibility(View.VISIBLE);
                showkeyborad_operand();
                check_block_value();

            }
        });
        input_tv7.setOnClickListener(new Button.OnClickListener()
        {
            @Override

            public void onClick(View v)
            {
                keyboard_status=1;
                inikeyborad_operand();
                input_tv7_bg.setVisibility(View.VISIBLE);
                user_input_value.delete(0, user_input_value.length());
                showkeyborad_num();
                user_select_input_tv=4;
                user_input_value.append(input_tv7.getText().toString());

            }
        });
        input_tv9.setOnClickListener(new Button.OnClickListener()
        {
            @Override

            public void onClick(View v)
            {
                keyboard_status=1;
                inikeyborad_operand();
                input_tv9_bg.setVisibility(View.VISIBLE);
                user_input_value.delete(0, user_input_value.length());
                showkeyborad_num();
                user_select_input_tv=5;
                user_input_value.append(input_tv9.getText().toString());

            }
        });
        hint1_but.setOnClickListener(new Button.OnClickListener()
        {
            @Override

            public void onClick(View v)
            {
                new AlertDialog.Builder(rank_test.this)//選擇顯示圖示
                        .setTitle("請選擇運算公式")
                        .setItems(new String[] { "長 X 寬", "底 X 高" , "底 X 高  ÷ 2", "對角線 X 對角線  ÷ 2", "( 上底 + 下底 ) X 高 ÷ 2"},
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int whichcountry)
                                    {
                                        switch (whichcountry) {
                                            case 0:
                                                select_input_style="1";
                                                ini_input_block();
                                                show_sytle1_input();
                                                break;
                                            case 1:
                                                select_input_style="2";
                                                ini_input_block();
                                                show_sytle1_input();
                                                break;
                                            case 2:
                                                select_input_style="3";
                                                ini_input_block();
                                                show_sytle2_input();
                                                break;
                                            case 3:
                                                select_input_style="4";
                                                ini_input_block();
                                                show_sytle2_input();
                                                break;
                                            case 4:
                                                select_input_style="5";
                                                ini_input_block();
                                                show_sytle3_input();
                                                break;

                                        }
                                    }
                                }).show();
            }
        });
    }
    private void findview()
    {
        test_confirm_but=(Button)findViewById(R.id.test_confirm_but);

        object_pic=(ImageView)findViewById(R.id.object_pic);
        top_width_tv=(TextView)findViewById(R.id.top_width_value);
        button_width_tv=(TextView)findViewById(R.id.button_width_value);
        hight_tv=(TextView)findViewById(R.id.hight_value);
        erase= (Button)findViewById(R.id.erase);
        pen_size=(Button)findViewById(R.id.pen_size);
        pen_color=(Button)findViewById(R.id.pen_color);
        pen=(Button)findViewById(R.id.pen);
        clean=(Button)findViewById(R.id.clean);
        confirm_but=(Button)findViewById(R.id.confirm_but);
        draw_page_1=(Button)findViewById(R.id.draw_page_1);
        draw_page_2=(Button)findViewById(R.id.draw_page_2);
        draw_page_3=(Button)findViewById(R.id.draw_page_3);
        show_process=(ImageView)findViewById(R.id.show_process);

        //鍵盤數字介面
        keyboard_1=(Button)findViewById(R.id.keyboard_1);
        keyboard_2=(Button)findViewById(R.id.keyboard_2);
        keyboard_3=(Button)findViewById(R.id.keyboard_3);
        keyboard_4=(Button)findViewById(R.id.keyboard_4);
        keyboard_5=(Button)findViewById(R.id.keyboard_5);
        keyboard_6=(Button)findViewById(R.id.keyboard_6);
        keyboard_7=(Button)findViewById(R.id.keyboard_7);
        keyboard_8=(Button)findViewById(R.id.keyboard_8);
        keyboard_9=(Button)findViewById(R.id.keyboard_9);
        keyboard_0=(Button)findViewById(R.id.keyboard_0);
        keyboard_clean=(Button)findViewById(R.id.keyboard_clean);
        keyboard_erase=(Button)findViewById(R.id.keyboard_erase);

        keyboard_point=(Button)findViewById(R.id.keyboard_point);
        //鍵盤加減乘除介面
        keyboard_plus=(Button)findViewById(R.id.keyboard_plus);
        keyboard_minus=(Button)findViewById(R.id.keyboard_minus);
        keyboard_multiply=(Button)findViewById(R.id.keyboard_multiply);
        keyboard_division=(Button)findViewById(R.id.keyboard_division);



        //使用者輸入介面 style 1
        style_1_input_tv1=(TextView)findViewById(R.id.style_1_input_tv1);
        style_1_input_tv2=(TextView)findViewById(R.id.style_1_input_tv2);
        style_1_input_tv3=(TextView)findViewById(R.id.style_1_input_tv3);
        style_1_input_tv4=(TextView)findViewById(R.id.style_1_input_tv4);
        style_1_input_tv5=(TextView)findViewById(R.id.style_1_input_tv5);

        style_1_input_tv1_bg=(ImageView)findViewById(R.id.style_1_input_tv1_bg);
        style_1_input_tv2_bg=(ImageView)findViewById(R.id.style_1_input_tv2_bg);
        style_1_input_tv3_bg=(ImageView)findViewById(R.id.style_1_input_tv3_bg);
        style_1_input_tv5_bg=(ImageView)findViewById(R.id.style_1_input_tv5_bg);

        //使用者輸入介面 style2
        style_2_input_tv1=(TextView)findViewById(R.id.style_2_input_tv1);
        style_2_input_tv2=(TextView)findViewById(R.id.style_2_input_tv2);
        style_2_input_tv3=(TextView)findViewById(R.id.style_2_input_tv3);
        style_2_input_tv4=(TextView)findViewById(R.id.style_2_input_tv4);
        style_2_input_tv5=(TextView)findViewById(R.id.style_2_input_tv5);
        style_2_input_tv6=(TextView)findViewById(R.id.style_2_input_tv6);
        style_2_input_tv7=(TextView)findViewById(R.id.style_2_input_tv7);

        style_2_input_tv1_bg=(ImageView)findViewById(R.id.style_2_input_tv1_bg);
        style_2_input_tv2_bg=(ImageView)findViewById(R.id.style_2_input_tv2_bg);
        style_2_input_tv3_bg=(ImageView)findViewById(R.id.style_2_input_tv3_bg);
        style_2_input_tv4_bg=(ImageView)findViewById(R.id.style_2_input_tv4_bg);
        style_2_input_tv5_bg=(ImageView)findViewById(R.id.style_2_input_tv5_bg);
        style_2_input_tv7_bg=(ImageView)findViewById(R.id.style_2_input_tv7_bg);

        //使用者輸入介面 style3
        input_tv1=(TextView)findViewById(R.id.input_tv1);
        input_tv2=(TextView)findViewById(R.id.input_tv2);
        input_tv3=(TextView)findViewById(R.id.input_tv3);
        input_tv4=(TextView)findViewById(R.id.input_tv4);
        input_tv5=(TextView)findViewById(R.id.input_tv5);
        input_tv6=(TextView)findViewById(R.id.input_tv6);
        input_tv7=(TextView)findViewById(R.id.input_tv7);
        input_tv8=(TextView)findViewById(R.id.input_tv8);
        input_tv9=(TextView)findViewById(R.id.input_tv9);
        textView1=(TextView)findViewById(R.id.textView1);
        textView2=(TextView)findViewById(R.id.textView2);

        input_tv1_bg=(ImageView)findViewById(R.id.input_tv1_bg);
        input_tv2_bg=(ImageView)findViewById(R.id.input_tv2_bg);
        input_tv3_bg=(ImageView)findViewById(R.id.input_tv3_bg);
        input_tv4_bg=(ImageView)findViewById(R.id.input_tv4_bg);
        input_tv5_bg=(ImageView)findViewById(R.id.input_tv5_bg);
        input_tv6_bg=(ImageView)findViewById(R.id.input_tv6_bg);
        input_tv7_bg=(ImageView)findViewById(R.id.input_tv7_bg);
        input_tv9_bg=(ImageView)findViewById(R.id.input_tv9_bg);

        timer_tv=(TextView)findViewById(R.id.timer_tv);
        test_title=(TextView)findViewById(R.id.test_title);


        hint1_but=(Button)findViewById(R.id.hint1_but);
        show_pic_large_mess=(ImageView)findViewById(R.id.show_pic_large_mess);

        right_lay = (AbsoluteLayout)findViewById(R.id.abs2);
        right_lay.addView(new MyView(this));
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFF000000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(6);

        mEmboss = new EmbossMaskFilter(new float[] { 1, 1, 1 },0.4f, 6, 3.5f);

        mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
    }

    class MyCount extends CountDownTimer {  //時間倒數碼表

        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onFinish() {
            timer_tv.setText("剩餘時間: 0  秒");
            cancel();

            int count_total_test = count_error_num+count_right_num;
            double d_count_right_num= Double.valueOf(count_right_num);

            int Correct_rate = (int)(Math.rint((d_count_right_num/count_total_test)*100));
            int score = count_right_num - count_error_num;
            if(score<0)
                score=0;

            //儲存sqlite
            dbHelper = new SQLite(rank_test.this, "rank_record_list");
            long no = dbHelper.Insert_rank_test_list(test_date,String.valueOf(count_total_test),String.valueOf(count_right_num),
                    String.valueOf(count_error_num),String.valueOf(score));

            dbHelper.close();//關閉資料庫

            dbHelper = new SQLite(rank_test.this, "rank_allstu");
            Cursor cursor = dbHelper.get_user_rank_record("rank_allstu",next_one_activity2.user_id);
            String total_num="0",right_num="0",right_rate="0";

            if(cursor !=null){
                int i = 0;
                while(cursor.moveToNext()){
                    total_num=cursor.getString(2);
                    right_num=cursor.getString(3);
                    right_rate=cursor.getString(4);
                    i=i+1;
                };
            }
            cursor.close();
            dbHelper.close();
            if(Integer.valueOf(right_num)<Integer.valueOf(count_right_num))
            {
                SQLite db = new SQLite(rank_test.this,"rank_allstu");
                db.update_rank(next_one_activity2.user_id,String.valueOf(count_total_test),String.valueOf(count_right_num),String.valueOf(score));
                db.close();
            }else if(Integer.valueOf(right_num)==Integer.valueOf(count_right_num))
            {
                if(Integer.valueOf(right_rate)<score)
                {
                    SQLite db = new SQLite(rank_test.this,"rank_allstu");
                    db.update_rank(next_one_activity2.user_id,String.valueOf(count_total_test),String.valueOf(count_right_num),String.valueOf(score));
                    db.close();
                }
            }

            new AlertDialog.Builder(rank_test.this)
                    .setCancelable(false)
                    .setTitle("本次測驗結束囉")
                    .setMessage("這次總測驗題數共計: " + count_total_test + "題\n"+
                            "答對題數: " + count_right_num + "題"+ "       錯誤題數: "+ count_error_num + "題\n"+
                            "測驗分數: "+score+
                            "正確率: " +Correct_rate +"%")
                    .setPositiveButton("觀看答題記錄",new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which)
                        {
	        	   /*
	        	   dbHelper = new SQLite(rank_test.this, "record_action");
  				   dbHelper.Insert_Action(login.rank_page,"done_test_Back_to_MainActivity");
  				   dbHelper.close();//關閉資料庫
	        	   */
                            rank_test_record_list.select_key_date=test_date;
                            Intent intent = new Intent();
                            intent.setClass(rank_test.this, rank_test_record_detail.class);
                            startActivity(intent);
                            rank_test.this.finish();
                        }
                    })
                    .show();
        }

        public void onTick(long millisUntilFinished) {
            timer_tv.setText("剩餘時間: "+ millisUntilFinished / 1000 + " 秒");
        }

    }

    private void get_all_test_data() {
        // TODO Auto-generated method stub
        Cursor cursor = Get_all_test("practice_recorde_data"); // homework_data practice_recorde_data
        Cursor cursor2 = Get_all_test("rmpi_data");

        int count_cursor=cursor.getCount()+cursor2.getCount();
        int tempq2=0,tempq3=0;
        stu_object_pic= new String[count_cursor];
        stu_object_hight= new String[count_cursor];
        stu_object_top_width= new String[count_cursor];
        stu_object_width= new String[count_cursor];
        test_shape= new String[count_cursor];
        question_level_2= new Integer[count_cursor];
        question_level_3= new Integer[count_cursor];

        if(cursor!=null) {
            if(cursor !=null){
                int i = 0;
                while(cursor.moveToNext()){
                    File temp_file = new File("/sdcard/file_data/"+cursor.getString(11));
                    if(temp_file.canRead())
                    {
                        stu_object_pic[i]=cursor.getString(11);
                        stu_object_hight[i]=cursor.getString(16);
                        stu_object_width[i]=cursor.getString(17);
                        stu_object_top_width[i]=cursor.getString(18);
                        test_shape[i]=cursor.getString(2);
                        if(test_shape[i].equals("triangle")||test_shape[i].equals("diamond"))
                        {
                            question_level_2[tempq2]=i;
                            tempq2++;

                        }else if(test_shape[i].equals("trapezoidal"))
                        {
                            question_level_3[tempq3]=i;
                            tempq3++;
                        }
                        i=i+1;
                    }
                };
                total_test_number = i;
                //amout_question_level_2 =tempq2-1;
                //amout_question_level_3 =tempq3-1;
            }
            cursor.close();		//關閉Cursor
        }
        if(cursor2!=null) {
            if(cursor2 !=null){
                int i = cursor.getCount(),q2=tempq2,q3=tempq3;
                while(cursor2.moveToNext()){
                    File temp_file = new File("/sdcard/file_data/"+cursor2.getString(10));
                    if(temp_file.canRead())
                    {
                        stu_object_pic[i]=cursor2.getString(10);
                        stu_object_hight[i]=cursor2.getString(8);
                        stu_object_width[i]=cursor2.getString(7);
                        stu_object_top_width[i]=cursor2.getString(9);
                        if(cursor2.getString(5).equals("0"))
                        {
                            test_shape[i]="rectangle";
                        }else if(cursor2.getString(5).equals("1"))
                        {
                            test_shape[i]="triangle";
                        }else if(cursor2.getString(5).equals("2"))
                        {
                            test_shape[i]="parallel";
                        }else if(cursor2.getString(5).equals("3"))
                        {
                            test_shape[i]="diamond";
                        }else if(cursor2.getString(5).equals("4"))
                        {
                            test_shape[i]="trapezoidal";
                        }
                        if(test_shape[i].equals("triangle")||test_shape[i].equals("diamond"))
                        {
                            question_level_2[q2]=i;
                            q2++;
                        }else if(test_shape[i].equals("trapezoidal"))
                        {
                            question_level_3[q3]=i;
                            q3++;
                        }
                        i=i+1;
                    }
                };
                total_test_number = i-1;
                amout_question_level_2 =q2-1;
                amout_question_level_3 =q3-1;
            }

            cursor2.close();//關閉Cursor
        }
    }

    //取得表單中，homework_list資料
    private Cursor Get_all_test(String table_name) {
        // TODO Auto-generated method stub
        dbHelper = new SQLite(rank_test.this, table_name);
        Cursor cursor = null;
        int rows_num=0;
        if(table_name.equals("practice_recorde_data")){ //1
            cursor = dbHelper.get_all_practice_data_join_list_date();//2
            rows_num = cursor.getCount();	//取得資料表列數
        }else if(table_name.equals("rmpi_data")){
            cursor = dbHelper.get_all_poi();
            rows_num = cursor.getCount();	//取得資料表列數
        }
        dbHelper.close();
        if(rows_num==0)
        {
            Toast.makeText(getApplicationContext(),	"沒有資料,無法開啟", Toast.LENGTH_SHORT).show();
            cursor.close();
            return null;
        }
        return  cursor;
    }
    private void rand_get_test()
    {
        Random rand=new Random();
        //ini  but1,but2,but3;
        draw_page=1;
        first_page1=true;
        first_page2=true;
        first_page3=true;
        click_draw_page_change_color(1);

        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");//日期
        process_store_name=temp_process_store_name+sdf.format(new Date());
        show_count_test_num++;
        if(show_count_test_num % 5==3)
        {
            int temp_a = question_level_2[rand.nextInt(amout_question_level_2)];
            while(temp_a==a)
            {
                temp_a = question_level_2[rand.nextInt(amout_question_level_2)];
            }
            a=temp_a;

        }else if(show_count_test_num % 5==0)
        {
            int temp_a = question_level_3[rand.nextInt(amout_question_level_3)];
            while(temp_a==a)
            {
                temp_a = question_level_3[rand.nextInt(amout_question_level_3)];
            }
            a=temp_a;
        }else if(!(show_count_test_num % 5==3 || show_count_test_num % 5==0))
        {
            int temp_a = rand.nextInt(total_test_number);
            while(temp_a==a)
            {
                temp_a = rand.nextInt(total_test_number);
            }
            a = temp_a;
        }


        bitmap = BitmapFactory.decodeFile("/sdcard/file_data/"+stu_object_pic[a]);
        object_pic.setImageBitmap(bitmap);

        if("".equals(stu_object_top_width[a]) || "  ".equals(stu_object_top_width[a]) || "null".equals(stu_object_top_width[a]))
        {
            top_width_tv.setText("");
        }else
        {
            top_width_tv.setText(stu_object_top_width[a] + "(cm)");
        }
        button_width_tv.setText(stu_object_width[a] + "(cm)");
        hight_tv.setText(stu_object_hight[a] + "(cm)");

        if(test_shape[a].equals("rectangle"))
        {
            test_title.setText("第 " + show_count_test_num + " 題：" + "計算矩形面積");
        }else if (test_shape[a].equals("triangle"))
        {
            test_title.setText("第 " + show_count_test_num + " 題：" + "計算三角形面積");
        }else if (test_shape[a].equals("trapezoidal"))
        {
            test_title.setText("第 " + show_count_test_num + " 題：" + "計算梯形面積");
        }else if (test_shape[a].equals("parallel"))
        {
            test_title.setText("第 " + show_count_test_num + " 題：" + "計算平行四邊形面積");
        }else if (test_shape[a].equals("diamond"))
        {
            test_title.setText("第 " + show_count_test_num + " 題：" + "計算菱形面積");
        }

    }
    public void put_value_in_tv(String i)//判斷是否超過字數限制
    {
        if(!(null==user_input_value))
        {
            if(!(user_select_input_tv==5 || user_select_input_tv==8 || user_select_input_tv==12))
            {
                if(!(5<user_input_value.length())){
                    user_input_value.append(String.valueOf(i));
                    chang_input_tv_value();
                }
            }else
            {
                if(!(8<user_input_value.length())){
                    user_input_value.append(String.valueOf(i));
                    chang_input_tv_value();
                }
            }
        }else
        {
            user_input_value.append(String.valueOf(i));
            chang_input_tv_value();
        }

    }
    public void chang_input_tv_value()//改變輸入的值
    {
        switch(user_select_input_tv)
        {
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
            case 5:
                input_tv9.setText(user_input_value.toString());
                break;
            case 6:
                style_1_input_tv1.setText(user_input_value.toString());
                break;
            case 7:
                style_1_input_tv3.setText(user_input_value.toString());
                break;
            case 8:
                style_1_input_tv5.setText(user_input_value.toString());
                break;
            case 9:
                style_2_input_tv1.setText(user_input_value.toString());
                break;
            case 10:
                style_2_input_tv3.setText(user_input_value.toString());
                break;
            case 11:
                style_2_input_tv5.setText(user_input_value.toString());
                break;
            case 12:
                style_2_input_tv7.setText(user_input_value.toString());
                break;
        }
        check_block_value();
    }
    public void check_block_value() //確認空格填滿顯示確認button
    {
        if(select_input_style.equals("5"))
        {
            if((!("".equals(input_tv1.getText().toString())) && !("".equals(input_tv2.getText().toString())) && !("".equals(input_tv3.getText().toString()))&& !("".equals(input_tv4.getText().toString())) && !("".equals(input_tv5.getText().toString()))
                    && !("".equals(input_tv6.getText().toString()))&& !("".equals(input_tv7.getText().toString()))&& !("".equals(input_tv9.getText().toString()))))
            {
                check_input_zero();
                if(!(check_input_zero))
                {
                    confirm_but.setVisibility(View.VISIBLE);
                }else
                {
                    confirm_but.setVisibility(View.INVISIBLE);
                }
            }else
            {
                confirm_but.setVisibility(View.INVISIBLE);
            }
        }else if(select_input_style.equals("1")||select_input_style.equals("2"))
        {
            if((!("".equals(style_1_input_tv1.getText().toString())) && !("".equals(style_1_input_tv2.getText().toString())) && !("".equals(style_1_input_tv3.getText().toString()))
                    && !("".equals(style_1_input_tv5.getText().toString()))))
            {
                check_input_zero();
                if(!(check_input_zero))
                {
                    confirm_but.setVisibility(View.VISIBLE);
                }else
                {
                    confirm_but.setVisibility(View.INVISIBLE);
                }
            }else
            {
                confirm_but.setVisibility(View.INVISIBLE);
            }
        }else if(select_input_style.equals("3")||select_input_style.equals("4"))
        {
            if((!("".equals(style_2_input_tv1.getText().toString())) && !("".equals(style_2_input_tv2.getText().toString())) && !("".equals(style_2_input_tv3.getText().toString()))
                    && !("".equals(style_2_input_tv4.getText().toString())) && !("".equals(style_2_input_tv5.getText().toString()))&& !("".equals(style_2_input_tv7.getText().toString()))))
            {
                check_input_zero();
                if(!(check_input_zero))
                {
                    confirm_but.setVisibility(View.VISIBLE);
                }else
                {
                    confirm_but.setVisibility(View.INVISIBLE);
                }
            }else
            {
                confirm_but.setVisibility(View.INVISIBLE);
            }
        }
    }
    public void check_input_zero()//確認input 為零
    {
        if(select_input_style.equals("5"))
        {
            if(Double.valueOf(input_tv1.getText().toString())==0 || Double.valueOf(input_tv3.getText().toString())==0
                    || Double.valueOf(input_tv5.getText().toString())==0|| Double.valueOf(input_tv7.getText().toString())==0|| Double.valueOf(input_tv9.getText().toString())==0)
            {
                check_input_zero=true;
            }else
            {
                check_input_zero=false;
            }
        }else if(select_input_style.equals("1")||select_input_style.equals("2"))
        {
            if(Double.valueOf(style_1_input_tv1.getText().toString())==0 || Double.valueOf(style_1_input_tv3.getText().toString())==0
                    || Double.valueOf(style_1_input_tv5.getText().toString())==0)
            {
                check_input_zero=true;
            }else
            {
                check_input_zero=false;
            }
        }else if(select_input_style.equals("3")||select_input_style.equals("4"))
        {
            if(Double.valueOf(style_2_input_tv1.getText().toString())==0 || Double.valueOf(style_2_input_tv3.getText().toString())==0
                    || Double.valueOf(style_2_input_tv5.getText().toString())==0|| Double.valueOf(style_2_input_tv7.getText().toString())==0)
            {
                check_input_zero=true;
            }else
            {
                check_input_zero=false;
            }
        }


    }
    public void check_block_zero() //確認空格以零開頭
    {
        if(user_input_value.length()==2)
        {
            if("0".equals(String.valueOf(user_input_value.charAt(0))) && !(".".equals(String.valueOf(user_input_value.charAt(1)))))
            {
                String temp = user_input_value.delete(0, 1).toString();
                user_input_value.delete(0, user_input_value.length());
                put_value_in_tv(temp);
            }
        }
    }
    public void ini_input_block()  //關閉所有input view
    {
        confirm_but.setVisibility(View.INVISIBLE);
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

        //style 1
        style_1_input_tv1.setVisibility(View.INVISIBLE);
        style_1_input_tv2.setVisibility(View.INVISIBLE);
        style_1_input_tv3.setVisibility(View.INVISIBLE);
        style_1_input_tv4.setVisibility(View.INVISIBLE);
        style_1_input_tv5.setVisibility(View.INVISIBLE);
        style_1_input_tv1.setText("");
        style_1_input_tv2.setText("");
        style_1_input_tv3.setText("");
        style_1_input_tv5.setText("");

        style_1_input_tv1_bg.setVisibility(View.INVISIBLE);
        style_1_input_tv2_bg.setVisibility(View.INVISIBLE);
        style_1_input_tv3_bg.setVisibility(View.INVISIBLE);
        style_1_input_tv5_bg.setVisibility(View.INVISIBLE);

        //style 2
        style_2_input_tv1.setVisibility(View.INVISIBLE);
        style_2_input_tv2.setVisibility(View.INVISIBLE);
        style_2_input_tv3.setVisibility(View.INVISIBLE);
        style_2_input_tv4.setVisibility(View.INVISIBLE);
        style_2_input_tv5.setVisibility(View.INVISIBLE);
        style_2_input_tv6.setVisibility(View.INVISIBLE);
        style_2_input_tv7.setVisibility(View.INVISIBLE);
        style_2_input_tv1.setText("");
        style_2_input_tv2.setText("");
        style_2_input_tv3.setText("");
        style_2_input_tv4.setText("");
        style_2_input_tv5.setText("");
        style_2_input_tv7.setText("");

        style_2_input_tv1_bg.setVisibility(View.INVISIBLE);
        style_2_input_tv2_bg.setVisibility(View.INVISIBLE);
        style_2_input_tv3_bg.setVisibility(View.INVISIBLE);
        style_2_input_tv4_bg.setVisibility(View.INVISIBLE);
        style_2_input_tv5_bg.setVisibility(View.INVISIBLE);
        style_2_input_tv7_bg.setVisibility(View.INVISIBLE);

        //style 3
        input_tv1.setVisibility(View.INVISIBLE);
        input_tv2.setVisibility(View.INVISIBLE);
        input_tv3.setVisibility(View.INVISIBLE);
        input_tv4.setVisibility(View.INVISIBLE);
        input_tv5.setVisibility(View.INVISIBLE);
        input_tv6.setVisibility(View.INVISIBLE);
        input_tv7.setVisibility(View.INVISIBLE);
        input_tv8.setVisibility(View.INVISIBLE);
        input_tv9.setVisibility(View.INVISIBLE);
        input_tv1.setText("");
        input_tv2.setText("");
        input_tv3.setText("");
        input_tv4.setText("");
        input_tv5.setText("");
        input_tv6.setText("");
        input_tv7.setText("");
        input_tv9.setText("");

        textView1.setVisibility(View.INVISIBLE);
        textView2.setVisibility(View.INVISIBLE);

        input_tv1_bg.setVisibility(View.INVISIBLE);
        input_tv2_bg.setVisibility(View.INVISIBLE);
        input_tv3_bg.setVisibility(View.INVISIBLE);
        input_tv4_bg.setVisibility(View.INVISIBLE);
        input_tv5_bg.setVisibility(View.INVISIBLE);
        input_tv6_bg.setVisibility(View.INVISIBLE);
        input_tv7_bg.setVisibility(View.INVISIBLE);
        input_tv9_bg.setVisibility(View.INVISIBLE);
    }
    public void show_sytle1_input()//顯示 style1 input
    {
        style_1_input_tv1.setVisibility(View.VISIBLE);
        style_1_input_tv2.setVisibility(View.VISIBLE);
        style_1_input_tv3.setVisibility(View.VISIBLE);
        style_1_input_tv4.setVisibility(View.VISIBLE);
        style_1_input_tv5.setVisibility(View.VISIBLE);
    }
    public void show_sytle2_input()//顯示 style1 input
    {
        style_2_input_tv1.setVisibility(View.VISIBLE);
        style_2_input_tv2.setVisibility(View.VISIBLE);
        style_2_input_tv3.setVisibility(View.VISIBLE);
        style_2_input_tv4.setVisibility(View.VISIBLE);
        style_2_input_tv5.setVisibility(View.VISIBLE);
        style_2_input_tv6.setVisibility(View.VISIBLE);
        style_2_input_tv7.setVisibility(View.VISIBLE);
    }
    public void show_sytle3_input() //顯示style3 input
    {
        input_tv1.setVisibility(View.VISIBLE);
        input_tv2.setVisibility(View.VISIBLE);
        input_tv3.setVisibility(View.VISIBLE);
        input_tv4.setVisibility(View.VISIBLE);
        input_tv5.setVisibility(View.VISIBLE);
        input_tv6.setVisibility(View.VISIBLE);
        input_tv7.setVisibility(View.VISIBLE);
        input_tv8.setVisibility(View.VISIBLE);
        input_tv9.setVisibility(View.VISIBLE);

        textView1.setVisibility(View.VISIBLE);
        textView2.setVisibility(View.VISIBLE);

    }

    public void inikeyborad_operand()//關閉運算元鍵盤
    {
        //關閉空格選取框框
        //style 1
        style_1_input_tv1_bg.setVisibility(View.INVISIBLE);
        style_1_input_tv2_bg.setVisibility(View.INVISIBLE);
        style_1_input_tv3_bg.setVisibility(View.INVISIBLE);
        style_1_input_tv5_bg.setVisibility(View.INVISIBLE);

        //style 2
        style_2_input_tv1_bg.setVisibility(View.INVISIBLE);
        style_2_input_tv2_bg.setVisibility(View.INVISIBLE);
        style_2_input_tv3_bg.setVisibility(View.INVISIBLE);
        style_2_input_tv4_bg.setVisibility(View.INVISIBLE);
        style_2_input_tv5_bg.setVisibility(View.INVISIBLE);
        style_2_input_tv7_bg.setVisibility(View.INVISIBLE);

        //style 3
        input_tv1_bg.setVisibility(View.INVISIBLE);
        input_tv2_bg.setVisibility(View.INVISIBLE);
        input_tv3_bg.setVisibility(View.INVISIBLE);
        input_tv4_bg.setVisibility(View.INVISIBLE);
        input_tv5_bg.setVisibility(View.INVISIBLE);
        input_tv6_bg.setVisibility(View.INVISIBLE);
        input_tv7_bg.setVisibility(View.INVISIBLE);
        input_tv9_bg.setVisibility(View.INVISIBLE);

        myAnimation_Alpha=new AlphaAnimation(1.0f, 0.1f);
        myAnimation_Alpha.setDuration(700);
        if(!(keyboard_status==1))
        {
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
    public void showkeyborad_operand()
    {
        myAnimation_Alpha=new AlphaAnimation(0.1f, 1.0f);
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
        //stule 1
        style_1_input_tv1_bg.setVisibility(View.INVISIBLE);
        style_1_input_tv2_bg.setVisibility(View.INVISIBLE);
        style_1_input_tv3_bg.setVisibility(View.INVISIBLE);
        style_1_input_tv5_bg.setVisibility(View.INVISIBLE);

        //style 2
        style_2_input_tv1_bg.setVisibility(View.INVISIBLE);
        style_2_input_tv2_bg.setVisibility(View.INVISIBLE);
        style_2_input_tv3_bg.setVisibility(View.INVISIBLE);
        style_2_input_tv4_bg.setVisibility(View.INVISIBLE);
        style_2_input_tv5_bg.setVisibility(View.INVISIBLE);
        style_2_input_tv7_bg.setVisibility(View.INVISIBLE);

        //style 3
        input_tv1_bg.setVisibility(View.INVISIBLE);
        input_tv2_bg.setVisibility(View.INVISIBLE);
        input_tv3_bg.setVisibility(View.INVISIBLE);
        input_tv4_bg.setVisibility(View.INVISIBLE);
        input_tv5_bg.setVisibility(View.INVISIBLE);
        input_tv6_bg.setVisibility(View.INVISIBLE);
        input_tv7_bg.setVisibility(View.INVISIBLE);
        input_tv9_bg.setVisibility(View.INVISIBLE);

        myAnimation_Alpha=new AlphaAnimation(1.0f, 0.1f);
        myAnimation_Alpha.setDuration(700);
        if(!(keyboard_status==2))
        {
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
        myAnimation_Alpha=new AlphaAnimation(0.1f, 1.0f);
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

    public class MyView extends View {


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

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        private void touch_start(float x, float y) {
            mPath.reset();
            //mPath.quadTo(x, x, (x + 1)/2, (y + 1)/2);
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
            mPath.lineTo(mX+1, mY+1);
        }
        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
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
    //圖片合併
    private Bitmap mergeBitmap(Bitmap firstBitmap, Bitmap secondBitmap) {
        Bitmap bitmap = Bitmap.createBitmap(firstBitmap.getWidth(), firstBitmap.getHeight(),
                firstBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(firstBitmap, new Matrix(), null);
        canvas.drawBitmap(secondBitmap, 0, 0, null);
        return bitmap;
    }
    //儲存process圖片
    public void store_process(int i)
    {
        try {
            File f=new File
                    (
                            Environment.getExternalStorageDirectory(),"file_data"
                    );
            File n=null;
            if(i==1)
            {
                n=new File(f,process_store_name+"_process.png");
            }
            else if(i==2)
            {
                n=new File(f,process_store_name+"_process2.png");
            }
            else if(i==3)
            {
                n=new File(f,process_store_name+"_process3.png");
            }
            FileOutputStream out = new FileOutputStream(n.getAbsolutePath());
            mergeBitmap(block_bitmap,mBitmap).compress(Bitmap.CompressFormat.PNG, 5, out);  //合併後儲存為q3.png
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void clean_draw_page()
    {
        mBitmap.eraseColor(Color.TRANSPARENT);
        mCanvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        right_lay.invalidate();
    }
    //click draw 改變顏色
    public void click_draw_page_change_color(int i)
    {
        if(i==1)
        {
            draw_page_1.setBackgroundColor(0xffdddddd);
            draw_page_2.setBackgroundColor(0xffffffff);
            draw_page_3.setBackgroundColor(0xffffffff);
        }else if(i==2)
        {
            draw_page_1.setBackgroundColor(0xffffffff);
            draw_page_2.setBackgroundColor(0xffdddddd);
            draw_page_3.setBackgroundColor(0xffffffff);
        }else if(i==3)
        {
            draw_page_1.setBackgroundColor(0xffffffff);
            draw_page_2.setBackgroundColor(0xffffffff);
            draw_page_3.setBackgroundColor(0xffdddddd);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            new AlertDialog.Builder(rank_test.this)
                    .setTitle("警告")
                    .setMessage("確定要放棄此次挑戰嗎?")
                    .setPositiveButton("確認",new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which)
                        {
		   	            	 /*
		   	            	dbHelper = new SQLite(rank_test.this, "record_action");
			  				dbHelper.Insert_Action(login.rank_page,"giveup_Back_to_MainActivity");
			  				dbHelper.close();//關閉資料庫
			  				*/
                            mc.cancel();
                            mBitmap.recycle();
                            System.gc();
                            rank_test.this.finish();
                        }
                    })
                    .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {

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
        try{
            if(mBitmap.isRecycled()==false)
                mBitmap.recycle();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        try{
            if(bitmap.isRecycled()==false)
                bitmap.recycle();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        try{
            if(block_bitmap.isRecycled()==false)
                block_bitmap.recycle();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        System.gc();
    }
}
