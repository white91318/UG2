package lab.hwang.ug;


import android.app.Activity;
import android.app.AlertDialog;
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
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class comment_others_homework_area  extends Activity {
    //class
    private SQLite dbHelper;//操作SQLite資料庫
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
    ImageView show_pic_large_mess,show_process,audio_mess;
    Bitmap bitmap,block_bitmap;

    //左上拍照的物件
    ImageView object_pic,object_frame;
    TextView width_tv,hight_tv,audio_stats,top_width_value,stu_process;

    //左下運算
    private Animation myAnimation_Alpha;

    //audio
    public String audio_fileName="null"; //audio檔案名稱
    public File temp_file;
    public MediaRecorder mediaRecorder = null;
    public MediaPlayer mediaPlayer = null;
    Boolean start_audio_record=false;

    Button erase,pen_size,pen_color,pen,clean,draw_page_1,draw_page_2,draw_page_3;//右邊畫版
    Button audio_bt1,audio_bt2,audio_bt3,audio_bt4,audio_bt5;
    EditText comment_edit;
    Button hint1_but,confirm_but;
    ImageView ca_hint_pic,alert_process_hint;
    String tempsdf,process_store_name;
    SimpleDateFormat sdf;
    int draw_page=1;
    Boolean first_page1=true,first_page2=true,first_page3=true;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_others_homework_area);
        findview();
        int img_id = getResources().getIdentifier(homework_list.select_shap+"_ca_hint", "drawable", getPackageName());
        ca_hint_pic.setBackgroundResource(img_id);
        bitmap = BitmapFactory.decodeFile("/sdcard/file_data/"+homework_list.temp_object_path);
        object_pic.setImageBitmap(bitmap);

        sdf = new SimpleDateFormat("HHmmss");//日期
        tempsdf = sdf.format(new Date());
        process_store_name=distance_MainActivity.user_id+"_to_"+homework_list.temp_answer_key+tempsdf;

        if(homework_list.select_shap.equals("trapezoidal"))
        {
            object_frame.setBackgroundResource(R.drawable.object_frame2);
            top_width_value.setVisibility(View.VISIBLE);
            top_width_value.setText(homework_list.temp_object_top_width + "(cm)");
        }
        stu_process.setText(homework_list.temp_stu+"的計算過程:\n"+homework_list.temp_process+"\n\n"+"你的意見");
        width_tv.setText(homework_list.temp_object_width + "(cm)");
        hight_tv.setText(homework_list.temp_object_hight + "(cm)");

        block_bitmap = BitmapFactory.decodeFile("/sdcard/file_data/"+homework_list.temp_process_path);
        show_process.setImageBitmap(block_bitmap);


        myAnimation_Alpha=new AlphaAnimation(1.0f, 0.0f);
        myAnimation_Alpha.setDuration(5000);
        myAnimation_Alpha.setFillAfter(true);
        alert_process_hint.setAnimation(myAnimation_Alpha);
        pen.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pen_color.setBackgroundResource(R.drawable.paint_color);
                pen_size.setBackgroundResource(R.drawable.paint_size);
                mPaint.setColor(0xFFFF0000);
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
                new AlertDialog.Builder(comment_others_homework_area.this)//選擇顯示圖示
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
                new AlertDialog.Builder(comment_others_homework_area.this)//選擇顯示圖示
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
                mBitmap.eraseColor(Color.TRANSPARENT);
                mCanvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
                //mCanvas.drawLine(0, 0, 0, 0, mPaint);
                right_lay.invalidate();
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
                    block_bitmap = BitmapFactory.decodeFile("/sdcard/file_data/"+homework_list.temp_process_path.replace("_process.png","_process2.png"));
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
                    block_bitmap = BitmapFactory.decodeFile("/sdcard/file_data/"+homework_list.temp_process_path.replace("_process.png","_process3.png"));
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
                new AlertDialog.Builder(comment_others_homework_area.this)
                        .setTitle("您的意見")
                        .setMessage("是否確定要送出意見")
                        .setPositiveButton("確認",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which)
                            {
                                if(start_audio_record)
                                {
                                    new AlertDialog.Builder(comment_others_homework_area.this)
                                            .setTitle("錄音中")
                                            .setMessage("目前正在錄音中... 是否要儲存錄音檔")
                                            .setPositiveButton("確認",new DialogInterface.OnClickListener(){
                                                public void onClick(DialogInterface dialog, int which)
                                                {
                                                    try
                                                    {
                                                        mediaRecorder.stop();
                                                        mediaRecorder.release();
                                                        mediaRecorder = null;
                                                    }catch (Exception e)
                                                    {

                                                    }
	  		  	            	 /*
		  		  	 			dbHelper = new SQLite(comment_others_homework_area.this, "record_action");
			  		  			dbHelper.Insert_Action(login.comment_others_page,"stop_record_audio");
			  		  			dbHelper.close();//關閉資料庫
			  		  			*/
                                                    stroe_sql();
                                                }
                                            })
                                            .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which)
                                                {
                                                    try
                                                    {
                                                        mediaRecorder.stop();
                                                        mediaRecorder.release();
                                                        mediaRecorder = null;
                                                    }catch (Exception e)
                                                    {

                                                    }
                                                    if(temp_file.canRead()){
                                                        temp_file.delete();
                                                        audio_fileName="null";
                                                    }
	  		  	            	/*
		  		  				dbHelper = new SQLite(comment_others_homework_area.this, "record_action");
			  		  			dbHelper.Insert_Action(login.comment_others_page,"delete_record_audio");
			  		  			dbHelper.close();//關閉資料庫
			  		  			*/
                                                    stroe_sql();
                                                }
                                            })
                                            .show();
                                }else
                                {
                                    stroe_sql();
                                }
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

        hint1_but.setOnClickListener(new Button.OnClickListener()
        {
            @Override

            public void onClick(View v)
            {
                hint1_but.setVisibility(View.INVISIBLE);
                myAnimation_Alpha=new AlphaAnimation(0.1f, 1f);
                myAnimation_Alpha.setDuration(1000);
                ca_hint_pic.setVisibility(View.VISIBLE);
                ca_hint_pic.startAnimation(myAnimation_Alpha);
                show_pic_large_mess.setVisibility(View.VISIBLE);
                show_pic_large_mess.startAnimation(myAnimation_Alpha);

            }
        });
        ca_hint_pic.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                show_pic_large_mess.setVisibility(View.INVISIBLE);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("from", "practice");
                bundle.putString("path", homework_list.select_shap);
                intent.putExtras(bundle);
                intent.setClass(comment_others_homework_area.this, show_large_image.class);
                startActivity(intent);
            }
        });

        audio_bt1.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
     		/*
			dbHelper = new SQLite(comment_others_homework_area.this, "record_action");
			dbHelper.Insert_Action(login.comment_others_page,"start_record_audio");
			dbHelper.close();//關閉資料庫
			*/
                audio_bt1.setVisibility(View.INVISIBLE);
                audio_bt2.setVisibility(View.VISIBLE);
                audio_mess.setVisibility(View.INVISIBLE);
                audio_stats.setVisibility(View.VISIBLE);
                audio_stats.setText("錄音中....");

                if(audio_fileName.equals("null")){
                    SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");//日期	     			;
                    audio_fileName=distance_MainActivity.user_id+homework_list.temp_answer_key+sdf.format(new Date())+".amr";
                    temp_file = new File("/sdcard/file_data/audio/"+audio_fileName);
                }
                try {

                    File SDCardpath = Environment.getExternalStorageDirectory();
                    File myDataPath = new File(SDCardpath.getAbsolutePath()
                            + "/file_data/audio/");
                    String sdcardroot = SDCardpath.toString();
                    if (!myDataPath.exists())
                        myDataPath.mkdirs();
                    File recodeFile = new File(SDCardpath.getAbsolutePath()
                            + "/file_data/audio/" + audio_fileName);
                    String recodeFileTemp = SDCardpath.getAbsolutePath() + "/file_data/audio/";

                    mediaRecorder = new MediaRecorder();
                    //設定音源
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setMaxDuration(60000);
                    //mediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
                    //設定輸出檔案的格式
                    mediaRecorder
                            .setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
                    //設定編碼格式
                    mediaRecorder
                            .setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    //設定錄音檔位置
                    mediaRecorder.setOutputFile(recodeFile.getAbsolutePath());
                    mediaRecorder.prepare();

                    //開始錄音
                    mediaRecorder.start();
                    start_audio_record=true;
                }
                catch(Exception e) {
                    Log.i("Log", "test(DialogMove) recordStart error = "+e);
                }
            }
        });
        audio_bt2.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
    		/*
			dbHelper = new SQLite(comment_others_homework_area.this, "record_action");
			dbHelper.Insert_Action(login.comment_others_page,"stop_record_audio");
			dbHelper.close();//關閉資料庫
			*/
                start_audio_record=false;
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                audio_bt2.setVisibility(View.INVISIBLE);
                audio_stats.setVisibility(View.INVISIBLE);
                audio_bt3.setVisibility(View.VISIBLE);
                audio_bt4.setVisibility(View.VISIBLE);
                audio_bt5.setVisibility(View.VISIBLE);

            }
        });
        audio_bt3.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
    		/*
			dbHelper = new SQLite(comment_others_homework_area.this, "record_action");
			dbHelper.Insert_Action(login.comment_others_page,"delete_record_audio");
			dbHelper.close();//關閉資料庫
			*/
                if(temp_file.canRead()){
                    temp_file.delete();
                    audio_fileName="null";
                }
                audio_bt1.setVisibility(View.VISIBLE);
                audio_bt2.setVisibility(View.INVISIBLE);
                audio_bt3.setVisibility(View.INVISIBLE);
                audio_bt4.setVisibility(View.INVISIBLE);
                audio_bt5.setVisibility(View.INVISIBLE);
                audio_stats.setVisibility(View.VISIBLE);
                audio_stats.setText("錄音檔已經刪除....");
            }
        });
        audio_bt4.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
    		/*
			dbHelper = new SQLite(comment_others_homework_area.this, "record_action");
			dbHelper.Insert_Action(login.comment_others_page,"listen_record_audio");
			dbHelper.close();//關閉資料庫
			*/
                if(temp_file.canRead())
                    Open_Audio_File(temp_file);
            }
        });
        audio_bt5.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                audio_bt1.setVisibility(View.INVISIBLE);
                audio_bt2.setVisibility(View.INVISIBLE);
                audio_bt3.setVisibility(View.INVISIBLE);
                audio_bt4.setVisibility(View.VISIBLE);
                audio_bt5.setVisibility(View.INVISIBLE);
            }
        });
    }
    private void findview()
    {
        object_pic=(ImageView)findViewById(R.id.object_pic);
        alert_process_hint=(ImageView)findViewById(R.id.alert_process_hint);
        show_process=(ImageView)findViewById(R.id.show_process);
        audio_mess=(ImageView)findViewById(R.id.audio_mess);
        width_tv=(TextView)findViewById(R.id.width_value);
        top_width_value=(TextView)findViewById(R.id.top_width_value);
        hight_tv=(TextView)findViewById(R.id.hight_value);
        object_frame=(ImageView)findViewById(R.id.object_frame);
        stu_process=(TextView)findViewById(R.id.stu_process);
        draw_page_1=(Button)findViewById(R.id.draw_page_1);
        draw_page_2=(Button)findViewById(R.id.draw_page_2);
        draw_page_3=(Button)findViewById(R.id.draw_page_3);


        erase= (Button)findViewById(R.id.erase);
        pen_size=(Button)findViewById(R.id.pen_size);
        pen_color=(Button)findViewById(R.id.pen_color);
        pen=(Button)findViewById(R.id.pen);
        clean=(Button)findViewById(R.id.clean);
        confirm_but=(Button)findViewById(R.id.confirm_but);
        comment_edit=(EditText)findViewById(R.id.comment_edit);


        hint1_but=(Button)findViewById(R.id.hint1_but);
        ca_hint_pic=(ImageView)findViewById(R.id.ca_hint_pic);
        show_pic_large_mess=(ImageView)findViewById(R.id.show_pic_large_mess);

        //旋轉hight
        Animation mAnimationRight = AnimationUtils.loadAnimation(comment_others_homework_area.this, R.layout.rotate_right);
        mAnimationRight.setFillAfter(true);

        hight_tv.setAnimation(mAnimationRight);


        //audio
        audio_bt1=(Button)findViewById(R.id.audio_bt1);//錄音
        audio_bt2=(Button)findViewById(R.id.audio_bt2);//暫停
        audio_bt3=(Button)findViewById(R.id.audio_bt3);//刪除
        audio_bt4=(Button)findViewById(R.id.audio_bt4);//試聽
        audio_bt5=(Button)findViewById(R.id.audio_confirm);//完成
        audio_stats=(TextView)findViewById(R.id.audio_tv);//顯示狀態


        right_lay = (AbsoluteLayout)findViewById(R.id.abs2);
        right_lay.addView(new MyView(this));
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(0xFFFF0000);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(6);

        mEmboss = new EmbossMaskFilter(new float[] { 1, 1, 1 },0.4f, 6, 3.5f);

        mBlur = new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL);
    }
    public void stroe_sql()
    {
			/*
			dbHelper = new SQLite(comment_others_homework_area.this, "record_action");
			dbHelper.Insert_Action(login.comment_others_page,"done_To_see_others_comments_page");
			dbHelper.close();//關閉資料庫
			*/
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
            block_bitmap = BitmapFactory.decodeFile("/sdcard/file_data/"+homework_list.temp_process_path.replace("_process.png","_process2.png"));
            show_process.setImageBitmap(block_bitmap);
            store_process(2);
        }
        if(first_page3)
        {
            clean_draw_page();
            block_bitmap = BitmapFactory.decodeFile("/sdcard/file_data/"+homework_list.temp_process_path.replace("_process.png","_process3.png"));
            show_process.setImageBitmap(block_bitmap);
            store_process(3);
        }

        //儲存sqlite
        dbHelper = new SQLite(comment_others_homework_area.this, "homework_feedback");
        long no = dbHelper.InsertHomework_feedback(homework_list.temp_answer_key,distance_MainActivity.user_id,comment_edit.getText().toString(),process_store_name+"_process.png",audio_fileName);
        dbHelper.close();//關閉資料庫

        Intent intent = new Intent();
        intent.setClass(comment_others_homework_area.this, see_others_comment.class);
        startActivity(intent);
        comment_others_homework_area.this.finish();
    }
    public void colorChanged(int color) {
        mPaint.setColor(color);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
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
    public void Open_Audio_File(File f) //撥放音樂檔
    {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(f), "audio");
        startActivity(intent);
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
            new AlertDialog.Builder(comment_others_homework_area.this)
                    .setTitle("警告")
                    .setMessage("確定要離開嗎?")
                    .setPositiveButton("確認",new DialogInterface.OnClickListener(){
                        public void onClick(DialogInterface dialog, int which)
                        {
		   	            	 /*
		 	            	dbHelper = new SQLite(comment_others_homework_area.this, "record_action");
			  				dbHelper.Insert_Action(login.comment_others_page,"giveup_Back_to_homework_list");
			  				dbHelper.close();//關閉資料庫
			  				*/
                            comment_others_homework_area.this.finish();
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