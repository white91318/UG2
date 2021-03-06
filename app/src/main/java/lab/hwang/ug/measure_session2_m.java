package lab.hwang.ug;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public  class  measure_session2_m extends Activity implements SensorEventListener,SurfaceHolder.Callback,View.OnTouchListener {

    //class
    private SQLite dbHelper;//操作SQLite資料庫

    //measure_board_guidence
    String measure_process_first_step[] = new String[10];
    String measure_rectangle_process[] = new String[10];
    String measure_others_process[] = new String[10];
    String measure_trapezoidal_process[] = new String[10];
    String measure_diamond_process [] = new String[10];

    //物件
    SensorManager manager; //????
    TextView person_hight_alert,right_tv,left_tv,light_text,guidance_tv,measure_process_tv;
    ImageView right_pic,left_pic,light,right_cross,left_cross,right_pic_top,right_cross_top,left_pic_top,left_cross_top;
    Button but_lock_distance,get_hight,lock_hight,get_width,but_lock_width,reset,get_distance_sec,get_hight_sec,zoom_in,zoom_out,cut_pic,go_calculate,take_pic;

    //變數
    String switch_pic_side="right";
    String measure_type;
    String person_hight;
    Bitmap bmp; //照片
    Bitmap bitmap;
    private String path="file_data";//照片路徑
    Double thedegree;//平板前後傾斜角
    double distance_value,hight_value,width_value; //距離值.高值.寬值
    Boolean click_but_distance=false,click_but_hight=false,click_but_width=false,lock_width=false,start_get_width=false,width_sec=false;
    Boolean click_sec_distance_but=false,click_sec_hight_but=false,too_hight=false;
    float azimuth = 0,pitch = 0,roll = 0,x = 0,y = 0,z = 0,ac_x=0,ac_y=0,ac_z=0;
    NumberFormat nf = NumberFormat.getInstance();
    final double focal_lenth=0.218; //相機焦距
    double first_distance_num,first_hight_num;//紀錄非地板上的物件,第一次測量結果
    public ProgressDialog ImageDialog = null; //儲存圖片的thread
    public static String bundle_width_value="null",bundle_hight_value="null",bundle_width2_value="null"; //綑綁值????
    public static String temp_pic_storename; //紀錄切割後的圖檔名稱
    public static String user_practice_shape;
    Boolean touch_srceen=false;
    Boolean take_pic_boolean=false,width_touch_line=false;

    String chinese_shape="";
    String board_message;
    //相機
    Camera mCamera;
    SurfaceView mSurfaceView; //介面黏貼處
    SurfaceHolder holder;
    AbsoluteLayout lyaa; //元件佈局  ????
    /////////////////////
    double bili = 1;
    double chaw = 0;
    double chah = 0;
    int bmh = 0;
    int bmw = 0;
    float startx=0.0f;
    float starty=0.0f;
    float endx=0.0f;
    float endy=0.0f;
    boolean CutDone = false;
    private GameView mGameView	= null;

    private boolean safeToTakePicture = false;
    @Override
    public  void  onCreate(Bundle savedInstanceState) {
        super .onCreate(savedInstanceState);

        setContentView(R.layout.measure_session_two_m);  //連接到的介面
        findview();

        bundle_width_value="null";
        bundle_hight_value="null";
        bundle_width2_value="null";

        //矩形rectangle  三角形triangle  梯形trapezoidal  平行四邊形parallel  菱形diamond
        if(concept_area2.measure_shap.equals("triangle_m"))
            chinese_shape="三角形";
        else if(concept_area2.measure_shap.equals("parallel_m"))
            chinese_shape="平行四邊形";
        else if(concept_area2.measure_shap.equals("diamond_m"))
            chinese_shape="菱形";

        //measure_process 字串宣告



        measure_process_first_step[4]="■拍照 ■裁切 ■測量與物件距離"; //固定需要這三個


        measure_rectangle_process[0]=" □測量長方形長度 □測量長方形寬度 □計算面積";
        measure_rectangle_process[1]=measure_process_first_step[4]+" <font color='red'>□測量長方形長度</font> □測量長方形寬度 □計算面積";
        measure_rectangle_process[2]=measure_process_first_step[4]+" ■測量長方形長度 <font color='red'>□測量長方形寬度</font> □計算面積";
        measure_rectangle_process[3]=measure_process_first_step[4]+" ■測量長方形長度 ■測量長方形寬度 <font color='red'>□計算面積</font>";

        measure_diamond_process[0]=" □測量菱形對角線A □測量菱形對角線B □計算面積";
        measure_diamond_process[1]=measure_process_first_step[4]+" <font color='red'>□測量菱形對角線A</font> □測量菱形對角線B □計算面積";
        measure_diamond_process[2]=measure_process_first_step[4]+" ■測量菱形對角線A <font color='red'>□測量菱形對角線B</font> □計算面積";
        measure_diamond_process[3]=measure_process_first_step[4]+" ■測量菱形對角線A ■測量菱形對角線B <font color='red'>□計算面積</font>";

        measure_others_process[0]=" □測量"+chinese_shape+"高度 □測量"+chinese_shape+"寬度 □計算面積";
        measure_others_process[1]=measure_process_first_step[4]+" <font color='red'>□測量"+chinese_shape+"高度</font> □測量"+chinese_shape+"寬度 □計算面積";
        measure_others_process[2]=measure_process_first_step[4]+" ■測量"+chinese_shape+"高度 <font color='red'>□測量"+chinese_shape+"寬度</font> □計算面積";
        measure_others_process[3]=measure_process_first_step[4]+" ■測量"+chinese_shape+"高度 ■測量"+chinese_shape+"寬度 <font color='red'>□計算面積</font>";

        measure_trapezoidal_process[0]=" □測量梯形高度 □測量梯形下底 □測量梯形上底 □計算面積";
        measure_trapezoidal_process[1]=measure_process_first_step[4]+" <font color='red'>□測量梯形高度</font> □測量梯形下底 □測量梯形上底 □計算面積";
        measure_trapezoidal_process[2]=measure_process_first_step[4]+" ■測量梯形高度 <font color='red'>□測量梯形下底</font> □測量梯形上底 □計算面積";
        measure_trapezoidal_process[3]=measure_process_first_step[4]+" ■測量梯形高度 ■測量梯形下底 <font color='red'>□測量梯形上底</font> □計算面積";
        measure_trapezoidal_process[4]=measure_process_first_step[4]+" ■測量梯形高度 ■測量梯形下底 ■測量梯形上底 <font color='red'>□計算面積</font>";


        if(concept_area2.measure_shap.equals("rectangle_m")) //矩形
        {
            measure_process_first_step[1]="<font color='red'>□拍照</font> □裁切 □測量與物件距離"+measure_rectangle_process[0];
            measure_process_first_step[2]="■拍照 <font color='red'>□裁切</font> □測量與物件距離"+measure_rectangle_process[0];
            measure_process_first_step[3]="■拍照 ■裁切 <font color='red'>□測量與物件距離</font>"+measure_rectangle_process[0];
        }
        else if(concept_area2.measure_shap.equals("trapezoidal_m"))//梯形
        {
            measure_process_first_step[1]="<font color='red'>□拍照</font> □裁切 □測量與物件距離"+measure_trapezoidal_process[0];
            measure_process_first_step[2]="■拍照 <font color='red'>□裁切</font> □測量與物件距離"+measure_trapezoidal_process[0];
            measure_process_first_step[3]="■拍照 ■裁切 <font color='red'>□測量與物件距離</font>"+measure_trapezoidal_process[0];
        }
        else if(concept_area2.measure_shap.equals("diamond_m")) //菱形
        {
            measure_process_first_step[1]="<font color='red'>□拍照</font> □裁切 □測量與物件距離"+measure_diamond_process[0];
            measure_process_first_step[2]="■拍照 <font color='red'>□裁切</font> □測量與物件距離"+measure_diamond_process[0];
            measure_process_first_step[3]="■拍照 ■裁切 <font color='red'>□測量與物件距離</font>"+measure_diamond_process[0];
        }else
        { //平行四邊形 三角形
            measure_process_first_step[1]="<font color='red'>□拍照</font> □裁切 □測量與物件距離"+measure_others_process[0];
            measure_process_first_step[2]="■拍照 <font color='red'>□裁切</font> □測量與物件距離"+measure_others_process[0];
            measure_process_first_step[3]="■拍照 ■裁切 <font color='red'>□測量與物件距離</font>"+measure_others_process[0];
        }





        person_hight=String.valueOf(Double.valueOf(concept_area2.person_hight)-5);//載入使用者身高
        measure_type=concept_area2.person_select_measure_type; //載入使用者測量選項
        Log.i("person_hight", person_hight);
        //註冊sensor
        manager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        //註冊camera
        holder = mSurfaceView.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        stopCamera();
        initCamera();

        //final_but check
        if(concept_area2.switch_practice_homework.equals("map_add_poi"))
        {
            go_calculate.setBackgroundResource(R.drawable.add_poi_but);
        }else
        {
            go_calculate.setBackgroundResource(R.drawable.calculator_but);
        }

        //註冊寬度bar
        mGameView = new GameView(this);
        //註冊AbsoluteLayout變數
        lyaa = (AbsoluteLayout)findViewById(R.id.AbsoluteLayout1);
        //註冊ontouch事件
        mSurfaceView.setOnTouchListener(this);

        board_message = "測量距離<font color='red'>(將螢幕中央十字對準物件與地面交界處)</font>";//S1
        guidance_tv.setText(Html.fromHtml(board_message));//下面的框框
        //get_area_ground 長(兩個距離相減)
        //get_area_wall 物件在牆上 長 (兩個高相減)ex時鐘
        //get_area_ground_hight 物件在地板上 長(一個高)ex門
        if(measure_type.equals("get_area_ground") || measure_type.equals("get_area_wall") || measure_type.equals("get_area_ground_hight")) //拍攝面積，先隱藏距離but,先拍照存檔
        {//S2
            if(!concept_area2.session_one){
                measure_process_tv.setText(Html.fromHtml(measure_process_first_step[1]));
            }
            but_lock_distance.setVisibility(View.INVISIBLE);
            board_message = "將螢幕中央十字對準物件進行<font color='red'>拍照</font>";
            guidance_tv.setText(Html.fromHtml(board_message));//下面的框框
            //guidance_tv.setText("將螢幕中央十字對準物件進行拍照");
            take_pic.setVisibility(View.VISIBLE);
            right_tv.setVisibility(View.INVISIBLE);
        }

        if(measure_type.equals("distance_sky"))//m4
        {
            board_message = "測量距離<font color='red'>(將螢幕中央十字對準物件底部)</font>";
            guidance_tv.setText(Html.fromHtml(board_message));
            //guidance_tv.setText("測量距離(將螢幕中央十字對準物件底部)");
        }
        take_pic.setOnClickListener(new Button.OnClickListener() //拍照->裁切->測量
        {
            @Override
            public void onClick(View v)
            {
                switch_pic_side="don't show";
                mCamera.takePicture(shutterCallback, rawCallback, TakeJpeg);//???
                take_pic.setVisibility(View.INVISIBLE);
            }
        });
        but_lock_distance.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                click_but_distance=true;
                mCamera.takePicture(shutterCallback, rawCallback, TakeJpeg);
                if(measure_type.equals("distance"))
                {
                    reset.setVisibility(View.VISIBLE);
                    but_lock_distance.setVisibility(View.INVISIBLE);
                }else if(measure_type.equals("hight") || measure_type.equals("hight_sky"))
                {
                    if(measure_type.equals("hight_sky"))
                    {
                        get_hight.setBackgroundResource(R.drawable.hight_but_1);
                    }
                    get_hight.setVisibility(View.VISIBLE);
                    but_lock_distance.setVisibility(View.INVISIBLE);
                }else if(measure_type.equals("width"))
                {
                    click_but_width=true;
                    get_width.setVisibility(View.VISIBLE);
                    but_lock_distance.setVisibility(View.INVISIBLE);
                }else if(measure_type.equals("distance_sky"))
                {
                    if(!click_sec_distance_but)
                    {
                        get_distance_sec.setVisibility(View.VISIBLE);
                        but_lock_distance.setVisibility(View.INVISIBLE);
                    }else
                    {
                        reset.setVisibility(View.VISIBLE);
                        but_lock_distance.setVisibility(View.INVISIBLE);
                    }

                }else if(measure_type.equals("get_area_ground"))
                {
                    if(!click_sec_distance_but)
                    {
                        but_lock_distance.setVisibility(View.INVISIBLE);
                        get_distance_sec.setVisibility(View.VISIBLE);
                    }else
                    {
                        click_but_width=true;
                        get_width.setVisibility(View.VISIBLE);
                        but_lock_distance.setVisibility(View.INVISIBLE);
                    }
                }else if(measure_type.equals("get_area_wall")|| measure_type.equals("get_area_ground_hight"))
                {
                    if(measure_type.equals("get_area_wall"))
                    {
                        get_hight.setBackgroundResource(R.drawable.hight_but_1);
                    }
                    get_hight.setVisibility(View.VISIBLE);
                    but_lock_distance.setVisibility(View.INVISIBLE);
                }
            }
        });
        cut_pic.setOnClickListener(new Button.OnClickListener() //確認第二次測量距離
        {
            @Override
            public void onClick(View v)
            {
                cut_pic.setClickable(false);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cut_pic.setClickable(true);
                    }
                }, 2000);
                switch_pic_side="right";
                Store_Image_dialog();
                Uri imageUri = Uri.parse("file:///sdcard/file_data/"+temp_pic_storename);
                cropImageUri(imageUri);

            }
        });
        get_distance_sec.setOnClickListener(new Button.OnClickListener() //確認第二次測量距離
        {
            @Override
            public void onClick(View v)
            {
                click_sec_distance_but=true;
                click_but_distance=false;
                switch_pic_side="right_top";
                but_lock_distance.setVisibility(View.VISIBLE);
                get_distance_sec.setVisibility(View.INVISIBLE);

                if(!concept_area2.session_one){
                    if(concept_area2.measure_shap.equals("rectangle_m"))
                        measure_process_tv.setText(Html.fromHtml(measure_rectangle_process[1]));
                    else if(concept_area2.measure_shap.equals("trapezoidal_m"))
                        measure_process_tv.setText(Html.fromHtml(measure_trapezoidal_process[1]));
                    else if(concept_area2.measure_shap.equals("diamond_m"))
                        measure_process_tv.setText(Html.fromHtml(measure_diamond_process[1]));
                    else
                        measure_process_tv.setText(Html.fromHtml(measure_others_process[1]));
                }
                board_message = "測量距離<font color='red'>(將螢幕中央十字對準物件頂部)</font>";
                guidance_tv.setText(Html.fromHtml(board_message));
                //guidance_tv.setText("測量距離(將螢幕中央十字對準物件頂部)");
            }
        });
        get_hight.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                switch_pic_side="left";
                click_but_hight=true;
                left_tv.setVisibility(View.VISIBLE);
                if(measure_type.equals("get_area_wall")||measure_type.equals("hight_sky"))
                {
                    lock_hight.setBackgroundResource(R.drawable.lock_hight_but_1);
                }
                get_hight.setVisibility(View.INVISIBLE);
                lock_hight.setVisibility(View.VISIBLE);
                light.setVisibility(View.VISIBLE);


                if(measure_type.equals("hight_sky")||measure_type.equals("get_area_wall"))
                {
                    if(!concept_area2.session_one){
                        if(concept_area2.measure_shap.equals("rectangle_m"))
                            measure_process_tv.setText(Html.fromHtml(measure_rectangle_process[1]));
                        else if(concept_area2.measure_shap.equals("trapezoidal_m"))
                            measure_process_tv.setText(Html.fromHtml(measure_trapezoidal_process[1]));
                        else if(concept_area2.measure_shap.equals("diamond_m"))
                            measure_process_tv.setText(Html.fromHtml(measure_diamond_process[1]));
                        else
                            measure_process_tv.setText(Html.fromHtml(measure_others_process[1]));
                    }
                    board_message = "測量高度<font color='red'>(將螢幕中央十字對準物件底部)</font>";
                    guidance_tv.setText(Html.fromHtml(board_message));
                    //guidance_tv.setText("測量高度(將螢幕中央十字對準物件底部)");
                }
                else
                {
                    if(!concept_area2.session_one){
                        if(concept_area2.measure_shap.equals("rectangle_m"))
                            measure_process_tv.setText(Html.fromHtml(measure_rectangle_process[1]));
                        else if(concept_area2.measure_shap.equals("trapezoidal_m"))
                            measure_process_tv.setText(Html.fromHtml(measure_trapezoidal_process[1]));
                        else if(concept_area2.measure_shap.equals("diamond_m"))
                            measure_process_tv.setText(Html.fromHtml(measure_diamond_process[1]));
                        else
                            measure_process_tv.setText(Html.fromHtml(measure_others_process[1]));
                    }
                    board_message = "測量高度<font color='red'>(將螢幕中央十字對準物件頂部)</font>";
                    guidance_tv.setText(Html.fromHtml(board_message));
                    //guidance_tv.setText("測量高度(將螢幕中央十字對準物件頂部)");
                }
            }
        });
        lock_hight.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                click_but_hight=false;
                mCamera.takePicture(shutterCallback, rawCallback, TakeJpeg);
                if(measure_type.equals("hight_sky")||measure_type.equals("get_area_wall")|| measure_type.equals("get_area_ground_hight"))
                {

                    if(click_sec_hight_but) //判斷是否按過確認第二次測量
                    {
                        if(measure_type.equals("get_area_wall"))
                        {
                            lock_hight.setVisibility(View.INVISIBLE);
                            click_but_width=true;
                            get_width.setVisibility(View.VISIBLE);
                            light.setVisibility(View.INVISIBLE);
                            light_text.setVisibility(View.INVISIBLE);
                        }else
                        {
                            light.setVisibility(View.INVISIBLE);
                            light_text.setVisibility(View.INVISIBLE);
                            lock_hight.setVisibility(View.INVISIBLE);
                            reset.setVisibility(View.VISIBLE);
                        }
                    }
                    else
                    {
                        if(measure_type.equals("get_area_ground_hight"))
                        {
                            lock_hight.setVisibility(View.INVISIBLE);
                            click_but_width=true;
                            get_width.setVisibility(View.VISIBLE);
                            light.setVisibility(View.INVISIBLE);
                            light_text.setVisibility(View.INVISIBLE);
                        }else
                        {
                            get_hight_sec.setVisibility(View.VISIBLE);
                            lock_hight.setVisibility(View.INVISIBLE);
                        }
                    }
                }
                else
                {
                    lock_hight.setVisibility(View.INVISIBLE);
                    reset.setVisibility(View.VISIBLE);
                    light.setVisibility(View.INVISIBLE);
                    light_text.setVisibility(View.INVISIBLE);
                }

            }
        });
        get_hight_sec.setOnClickListener(new Button.OnClickListener() //確認第二次測量高度
        {
            @Override
            public void onClick(View v)
            {
                switch_pic_side="left_top";
                click_but_hight=true;
                click_sec_hight_but=true;
                if(measure_type.equals("get_area_wall")||measure_type.equals("hight_sky"))
                {
                    lock_hight.setBackgroundResource(R.drawable.lock_hight_but_2);
                }
                lock_hight.setVisibility(View.VISIBLE);
                get_hight_sec.setVisibility(View.INVISIBLE);
                if(!concept_area2.session_one){
                    if(concept_area2.measure_shap.equals("rectangle_m"))
                        measure_process_tv.setText(Html.fromHtml(measure_rectangle_process[1]));
                    else if(concept_area2.measure_shap.equals("trapezoidal_m"))
                        measure_process_tv.setText(Html.fromHtml(measure_trapezoidal_process[1]));
                    else if(concept_area2.measure_shap.equals("diamond_m"))
                        measure_process_tv.setText(Html.fromHtml(measure_diamond_process[1]));
                    else
                        measure_process_tv.setText(Html.fromHtml(measure_others_process[1]));
                }
                board_message = "測量高度<font color='red'>(將螢幕中央十字對準物件頂部)</font>";
                guidance_tv.setText(Html.fromHtml(board_message));
                //guidance_tv.setText("測量高度(將螢幕中央十字對準物件頂部)");
            }
        });
        get_width.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                get_width.setVisibility(View.INVISIBLE);
                left_tv.setVisibility(View.VISIBLE);
                if(!(measure_type.equals("get_area_wall")||measure_type.equals("get_area_ground_hight")))//除了測量牆上物件的面積外 其他的都是用將左邊的數字歸零
                {
                    left_tv.setText("寬度:0.0  cm");
                }else
                {
                    right_tv.setText("寬度:0.0  cm");
                }
                touch_srceen=true;
                lyaa.addView(mGameView);
                mGameView.sx =998;
                mGameView.ex = 1002;
                //mGameView.ey = 200;
                mGameView.postInvalidate();
                zoom_in.setVisibility(View.VISIBLE);
                zoom_out.setVisibility(View.VISIBLE);
                guidance_tv.setVisibility(View.VISIBLE);
                if(!(width_sec))
                {
                    if(!concept_area2.session_one){
                        if(concept_area2.measure_shap.equals("rectangle_m"))
                            measure_process_tv.setText(Html.fromHtml(measure_rectangle_process[2]));
                        else if(concept_area2.measure_shap.equals("trapezoidal_m"))
                            measure_process_tv.setText(Html.fromHtml(measure_trapezoidal_process[2]));
                        else if(concept_area2.measure_shap.equals("diamond_m"))
                            measure_process_tv.setText(Html.fromHtml(measure_diamond_process[2]));
                        else
                            measure_process_tv.setText(Html.fromHtml(measure_others_process[2]));
                    }
                    if(concept_area2.measure_shap.equals("diamond_m"))
                    {
                        board_message = "測量寬度(調整螢幕中的框架框住物件<font color='red'>左右對角線</font>的寬)<br/>觸控螢幕框架可調整大小，右方縮小與放大鍵可以微調框架大小";
                        guidance_tv.setText(Html.fromHtml(board_message));
                        //guidance_tv.setText("測量寬度(調整螢幕中的框架框住物件底部的寬)" + "\n" + "觸控螢幕框架可調整大小，右方縮小與放大鍵可以微調框架大小");
                    }else
                    {
                        board_message = "測量寬度(調整螢幕中的框架框住物件<font color='red'>底部</font>的寬)<br/>觸控螢幕框架可調整大小，右方縮小與放大鍵可以微調框架大小";
                        guidance_tv.setText(Html.fromHtml(board_message));
                        //guidance_tv.setText("測量寬度(調整螢幕中的框架框住物件底部的寬)" + "\n" + "觸控螢幕框架可調整大小，右方縮小與放大鍵可以微調框架大小");
                    }
                }else
                {
                    if(!concept_area2.session_one){
                        measure_process_tv.setText(Html.fromHtml(measure_trapezoidal_process[3]));
                    }
                    board_message = "測量寬度(調整螢幕中的框架框住物件<font color='red'>頂部</font>的寬)<br/>觸控螢幕框架可調整大小，右方縮小與放大鍵可以微調框架大小";
                    guidance_tv.setText(Html.fromHtml(board_message));
                    //guidance_tv.setText("測量寬度(調整螢幕中的框架框住物件頂部的寬)" + "\n" + "觸控螢幕框架可調整大小，右方縮小與放大鍵可以微調框架大小");
                }
            }
        });
        but_lock_width.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(concept_area2.measure_shap.equals("trapezoidal_m"))//梯形要測兩次寬度
                {
                    if(!(width_sec))//第一次測寬度
                    {
                        lock_width=true;
                        width_sec=true;
                        if(!(measure_type.equals("get_area_wall")||measure_type.equals("get_area_ground_hight")))//除了測量牆上物件的面積外 其他的都是用switch_pic_side="width"顯示在左下
                        {
                            switch_pic_side="width";
                        }else
                        {
                            switch_pic_side="get_area_wall_right_top";
                        }

                        mCamera.takePicture(shutterCallback, rawCallback, TakeJpeg);
                        but_lock_width.setVisibility(View.INVISIBLE);
                        lyaa.removeView(mGameView);
                        zoom_in.setVisibility(View.INVISIBLE);
                        zoom_out.setVisibility(View.INVISIBLE);
                        if(measure_type.equals("get_area_wall")||measure_type.equals("get_area_ground")||measure_type.equals("get_area_ground_hight"))
                        {
                            lock_width=false;
                            click_but_width=true;
                            get_width.setBackgroundResource(R.drawable.width_but_2);
                            get_width.setVisibility(View.VISIBLE);
                        }
                    }else//第二次測量寬度完畢
                    {
                        lock_width=true;
                        if(measure_type.equals("get_area_ground"))
                        {
                            switch_pic_side="left_top";
                        }else
                        {
                            switch_pic_side="right_top";
                        }
                        //mCamera.takePicture(shutterCallback, rawCallback, TakeJpeg);
                        if (safeToTakePicture) {
                            mCamera.takePicture(shutterCallback, rawCallback, TakeJpeg);
                            safeToTakePicture = false;
                        }
                        but_lock_width.setVisibility(View.INVISIBLE);
                        lyaa.removeView(mGameView);
                        zoom_in.setVisibility(View.INVISIBLE);
                        zoom_out.setVisibility(View.INVISIBLE);
                        if(measure_type.equals("get_area_wall")||measure_type.equals("get_area_ground")||measure_type.equals("get_area_ground_hight"))
                        {
                            if(!concept_area2.session_one){
                                if(concept_area2.measure_shap.equals("rectangle_m"))
                                    measure_process_tv.setText(Html.fromHtml(measure_rectangle_process[3]));
                                else if(concept_area2.measure_shap.equals("trapezoidal_m"))
                                    measure_process_tv.setText(Html.fromHtml(measure_trapezoidal_process[4]));
                                else if(concept_area2.measure_shap.equals("diamond_m"))
                                    measure_process_tv.setText(Html.fromHtml(measure_diamond_process[3]));
                                else
                                    measure_process_tv.setText(Html.fromHtml(measure_others_process[3]));
                            }
                            go_calculate.setVisibility(View.VISIBLE);
                        }else
                        {
                            reset.setVisibility(View.VISIBLE);
                        }
                    }
                }else //不是梯形的話直接計算
                {
                    lock_width=true;
                    if(!(measure_type.equals("get_area_wall")||measure_type.equals("get_area_ground_hight")))//除了測量牆上物件的面積外 其他的都是用switch_pic_side="width"顯示在左下
                    {
                        switch_pic_side="width";
                    }else
                    {
                        switch_pic_side="get_area_wall_right_top";
                    }

                    mCamera.takePicture(shutterCallback, rawCallback, TakeJpeg);
                    but_lock_width.setVisibility(View.INVISIBLE);
                    lyaa.removeView(mGameView);
                    zoom_in.setVisibility(View.INVISIBLE);
                    zoom_out.setVisibility(View.INVISIBLE);
                    if(measure_type.equals("get_area_wall")||measure_type.equals("get_area_ground")||measure_type.equals("get_area_ground_hight"))
                    {
                        if(!concept_area2.session_one){
                            if(concept_area2.measure_shap.equals("rectangle_m"))
                                measure_process_tv.setText(Html.fromHtml(measure_rectangle_process[3]));
                            else if(concept_area2.measure_shap.equals("trapezoidal_m"))
                                measure_process_tv.setText(Html.fromHtml(measure_trapezoidal_process[4]));
                            else if(concept_area2.measure_shap.equals("diamond_m"))
                                measure_process_tv.setText(Html.fromHtml(measure_diamond_process[3]));
                            else
                                measure_process_tv.setText(Html.fromHtml(measure_others_process[3]));
                        }
                        go_calculate.setVisibility(View.VISIBLE);
                    }else
                    {
                        reset.setVisibility(View.VISIBLE);
                    }
                }
                guidance_tv.setVisibility(View.INVISIBLE);
            }
        });
        zoom_out.setOnTouchListener(new Button.OnTouchListener()
        {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if(!(mGameView.ex>1278) && !(lock_width))
                {
                    mGameView.sx=mGameView.sx-1;
                    mGameView.ex=mGameView.ex+1;
                    mGameView.postInvalidate();
                    getwidth();
                }
                return false;
            }

        });
        zoom_in.setOnTouchListener(new Button.OnTouchListener()
        {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if(!(mGameView.ex<642) && !(lock_width))
                {
                    mGameView.sx=mGameView.sx+1;
                    mGameView.ex=mGameView.ex-1;
                    mGameView.postInvalidate();
                    getwidth();
                }
                return false;
            }

        });
        go_calculate.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ini_var();
                stopCamera();
                initCamera();
                final Intent intent = new Intent();
                if(concept_area2.switch_practice_homework.equals("homework"))
                {
                    if(homework_list.select_shap.equals("rectangle_m") || homework_list.select_shap.equals("parallel_m"))
                    {
     				/*
     				dbHelper = new SQLite(measure_session2_m.this, "record_action");
      				dbHelper.Insert_Action(login.homework_page,"To_submit_homework1");
      				dbHelper.close();//關閉資料庫
					*/

                        intent.setClass(measure_session2_m.this, submit_homework.class);
                        startActivity(intent);
                        measure_session2_m.this.finish();
                        release_memory();
                    }else if(homework_list.select_shap.equals("triangle_m") || homework_list.select_shap.equals("diamond_m"))
                    {
     				/*
     				dbHelper = new SQLite(measure_session2_m.this, "record_action");
      				dbHelper.Insert_Action(login.homework_page,"To_submit_homework2");
      				dbHelper.close();//關閉資料庫
      				*/
                        intent.setClass(measure_session2_m.this, submit_homework2.class);
                        startActivity(intent);
                        measure_session2_m.this.finish();
                        release_memory();
                    }else if(homework_list.select_shap.equals("trapezoidal_m"))
                    {
     				/*
     				dbHelper = new SQLite(measure_session2_m.this, "record_action");
      				dbHelper.Insert_Action(login.homework_page,"To_submit_homework3");
      				dbHelper.close();//關閉資料庫
     				*/

                        intent.setClass(measure_session2_m.this, submit_homework3.class);
                        startActivity(intent);
                        measure_session2_m.this.finish();
                        release_memory();
                    }

                }
                else if(concept_area2.switch_practice_homework.equals("practice"))
                {

                    if(concept_area2.measure_shap.equals("rectangle_m") || concept_area2.measure_shap.equals("parallel_m"))
                    {
     				/*
     				dbHelper = new SQLite(measure_session2_m.this, "record_action");
	  				dbHelper.Insert_Action(login.MainActivity_page,"To_user_practice_area1");
	  				dbHelper.close();//關閉資料庫
     				*/
                        intent.setClass(measure_session2_m.this, user_practice_area_m.class);
                        startActivity(intent);
                        measure_session2_m.this.finish();
                        release_memory();
                    }else if(concept_area2.measure_shap.equals("triangle_m") || concept_area2.measure_shap.equals("diamond_m"))
                    {
     				/*
     				dbHelper = new SQLite(measure_session2_m.this, "record_action");
	  				dbHelper.Insert_Action(login.MainActivity_page,"To_user_practice_area2");
	  				dbHelper.close();//關閉資料庫
     				*/
                        intent.setClass(measure_session2_m.this, user_practice_area2_m.class);
                        startActivity(intent);
                        measure_session2_m.this.finish();
                        release_memory();

                    }else if(concept_area2.measure_shap.equals("trapezoidal_m"))
                    {
     				/*
     				dbHelper = new SQLite(measure_session2_m.this, "record_action");
	  				dbHelper.Insert_Action(login.MainActivity_page,"To_user_practice_area3");
	  				dbHelper.close();//關閉資料庫
     				*/
                        intent.setClass(measure_session2_m.this, user_practice_area3_m.class);
                        startActivity(intent);
                        measure_session2_m.this.finish();
                        release_memory();
                    }

                }else if(concept_area2.switch_practice_homework.equals("map_add_poi"))
                {
                  //  intent.setClass(measure_session2_m.this, confirm_add_poi.class);
                    startActivity(intent);
                    measure_session2_m.this.finish();
                    release_memory();
                }else if(concept_area2.switch_practice_homework.equals("poi_comment_measurment"))
                {
                    measure_session2_m.this.setResult(RESULT_OK,intent);
                    measure_session2_m.this.finish();
                    release_memory();
                }
            }
        });
        reset.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
     		/*
     		if(too_hight)
     		{
     			Toast.makeText(measure_session2_m.this, "請退後幾步，重新測量距離", Toast.LENGTH_LONG).show();
     			ini_var();
     		}else
     		{
     			ini_var();
     		}
     		*/
			/*
			dbHelper = new SQLite(measure_session2_m.this, "record_action");
			dbHelper.Insert_Action(login.measuer_page,"Done_To_MainActivity");
			dbHelper.close();//關閉資料庫
			*/
                release_memory();
                measure_session2_m.this.finish();
            }
        });
    }





    public void findview()
    {


        mSurfaceView = (SurfaceView)findViewById(R.id.mSurfaceView6);
        ////////////////////////////TextView

        right_tv=(TextView)findViewById(R.id.right_tv6);
        left_tv=(TextView)findViewById(R.id.left_tv6);
        light_text=(TextView)findViewById(R.id.light_text6);
        guidance_tv=(TextView)findViewById(R.id.guidance_tv6);
        measure_process_tv=(TextView)findViewById(R.id.measure_process_tv6);
        ///////////////////////////ImageView
        right_cross=(ImageView)findViewById(R.id.right_cross6);
        left_cross=(ImageView)findViewById(R.id.left_cross6);
        right_pic=(ImageView)findViewById(R.id.right_pic6);
        left_pic=(ImageView)findViewById(R.id.left_pic6);
        light=(ImageView)findViewById(R.id.light6);
        right_pic_top=(ImageView)findViewById(R.id.right_pic_top6);
        right_cross_top=(ImageView)findViewById(R.id.right_cross_top6);
        left_pic_top=(ImageView)findViewById(R.id.left_pic_top6);
        left_cross_top=(ImageView)findViewById(R.id.left_cross_top6);
        ///////////////////////////Button
        but_lock_distance = (Button)findViewById(R.id.lock_distance6);
        get_hight = (Button)findViewById(R.id.get_hight6);
        lock_hight = (Button)findViewById(R.id.lock_hight6);
        get_width=(Button)findViewById(R.id.get_width6);
        but_lock_width=(Button)findViewById(R.id.lock_width6);
        reset=(Button)findViewById(R.id.reset6);
        get_distance_sec=(Button)findViewById(R.id.get_distance_sec6);
        get_hight_sec=(Button)findViewById(R.id.get_hight_sec6);
        zoom_in=(Button)findViewById(R.id.zoom_in6);
        zoom_out=(Button)findViewById(R.id.zoom_out6);
        cut_pic=(Button)findViewById(R.id.cut_pic6);
        go_calculate=(Button)findViewById(R.id.calculate_but6);
        take_pic=(Button)findViewById(R.id.take_pic6);
    }
    @Override
    public  boolean  onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.layout.menu, menu);
        return true;
    }
    private void release_memory() {
        // TODO Auto-generated method stub
        try{
            if(bmp.isRecycled()==false)
                bmp.recycle();
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
        System.gc();
    }
    //初始化變數
    public void ini_var()
    {
        thedegree=0.0;
        distance_value=0.0;
        hight_value=0.0;
        width_value=0.0;
        click_but_distance=false;
        click_but_hight=false;
        click_but_width=false;
        lock_width=false;
        start_get_width=false;
        click_sec_distance_but=false;
        click_sec_hight_but=false;
        too_hight=false;
        azimuth = 0;
        pitch = 0;
        roll = 0;
        x = 0;
        y = 0;
        z = 0;
        ac_x=0;
        ac_y=0;
        ac_z=0;
        switch_pic_side="right";

        first_distance_num=0.0;
        first_hight_num=0.0;

        right_pic.setVisibility(View.INVISIBLE);
        left_pic.setVisibility(View.INVISIBLE);
        right_cross.setVisibility(View.INVISIBLE);
        left_cross.setVisibility(View.INVISIBLE);


        get_hight.setVisibility(View.INVISIBLE);
        lock_hight.setVisibility(View.INVISIBLE);
        get_width.setVisibility(View.INVISIBLE);
        but_lock_width.setVisibility(View.INVISIBLE);
        reset.setVisibility(View.INVISIBLE);
        left_tv.setVisibility(View.INVISIBLE);
        light_text.setVisibility(View.INVISIBLE);
        light.setVisibility(View.INVISIBLE);
        but_lock_distance.setVisibility(View.VISIBLE);
        lyaa.removeView(mGameView);

        get_distance_sec.setVisibility(View.INVISIBLE);
        right_pic_top.setVisibility(View.INVISIBLE);
        right_cross_top.setVisibility(View.INVISIBLE);
        get_hight_sec.setVisibility(View.INVISIBLE);
        left_pic_top.setVisibility(View.INVISIBLE);
        left_cross_top.setVisibility(View.INVISIBLE);

        zoom_in.setVisibility(View.INVISIBLE);
        zoom_out.setVisibility(View.INVISIBLE);

        board_message = "測量距離<font color='red'>(將螢幕中央十字對準物件與地面交界處)</font>";
        guidance_tv.setText(Html.fromHtml(board_message));
        //guidance_tv.setText("測量距離(將螢幕中央十字對準物件與地面交界處)");

        cut_pic.setVisibility(View.INVISIBLE);
        go_calculate.setVisibility(View.INVISIBLE);

        take_pic.setVisibility(View.INVISIBLE);
        if(take_pic_boolean)
        {
            bmp.recycle();
            bitmap.recycle();
            System.gc();
            take_pic_boolean=false;
        }
    }



    //判斷AlertDialog input 值
    public static boolean validate_input (String s) {
        return (49<Integer.valueOf(s) && 201>Integer.valueOf(s));
    }

    public void getwidth()
    {
        //Log.i("@@@@@@@@@@@@@@", String.valueOf(first_distance_num));
        width_value=((first_distance_num/100.0+focal_lenth)*(Math.abs(mGameView.sx - mGameView.ex)/59.0))/focal_lenth;
        //nf.setMaximumFractionDigits(0);
        if(width_sec)//判斷是否是第二次測量寬度把值給bundle_width2_value
        {
            bundle_width2_value=String.valueOf(Math.round(width_value));
            but_lock_width.setBackgroundResource(R.drawable.lock_width_but_2);
        }else
        {
            bundle_width_value=String.valueOf(Math.round(width_value));
            but_lock_width.setBackgroundResource(R.drawable.lock_width_but);
        }

        if(Math.abs(width_value)<1)
        {
            but_lock_width.setVisibility(View.INVISIBLE);
        }else
        {
            but_lock_width.setVisibility(View.VISIBLE);
        }

        if(measure_type.equals("width") || measure_type.equals("get_area_ground"))
        {

            left_tv.setText("寬度:"+String.valueOf(Math.round(width_value))+"  cm");//輸出寬度
        }
        if(measure_type.equals("get_area_wall") || measure_type.equals("get_area_ground_hight"))
        {
            right_tv.setText("寬度:"+String.valueOf(Math.round(width_value))+"  cm");//輸出寬度
        }
    }

    @Override
    public  void  onAccuracyChanged(Sensor sensor, int  accuracy) {
        // TODO Auto-generated method stub

    }

    @Override
    public  void  onSensorChanged(SensorEvent e) {
        if(e.sensor.getType() ==  Sensor.TYPE_ORIENTATION)
        {
            azimuth = e.values[SensorManager.DATA_X];
            pitch = e.values[SensorManager.DATA_Y];
            roll = e.values[SensorManager.DATA_Z];
        }
        if(e.sensor.getType() ==  Sensor.TYPE_ACCELEROMETER)
        {
            ac_x = e.values[SensorManager.DATA_X];
            ac_y = e.values[SensorManager.DATA_Y];
            ac_z = e.values[SensorManager.DATA_Z];
        }
        double  degree;
        double  tana,tanb;

        tana = Math.tan(getRadians(pitch));
        tanb = Math.tan(getRadians(roll));
        degree = getDegrees(Math.acos(1d/Math.sqrt(tana*tana+tanb*tanb+ 1 )));
        thedegree=(double)degree;


        if(!click_but_distance)//尚未鎖定距離(測量距離)
        {
            if(click_sec_distance_but)//判斷是否為量第二次的距離
            {
                if(ac_z<0.6)//判斷可測量之範圍
                {
                    right_tv.setText("超出可測量範圍"); //>80度
                }
                else
                {
                    distance_value=((Double.valueOf(person_hight))*(Math.tan(Math.toRadians(thedegree))));
                    //nf.setMaximumFractionDigits(0);
                    Double the_distance = Math.abs(distance_value-first_distance_num);
                    right_tv.setText("距離:"+String.valueOf(Math.round(the_distance))+"  cm");
                    bundle_hight_value=String.valueOf(Math.round(Math.abs(the_distance)));
                    if(Math.abs(the_distance)<1)
                    {
                        but_lock_distance.setVisibility(View.INVISIBLE);
                    }else
                    {
                        but_lock_distance.setVisibility(View.VISIBLE);
                    }
                }
            }
            else
            {
                if(ac_z<0.6)//判斷可測量之範圍
                {
                    right_tv.setText("超出可測量範圍"); //>80度
                }
                else
                {
                    distance_value=((Double.valueOf(person_hight))*(Math.tan(Math.toRadians(thedegree))));
                    //nf.setMaximumFractionDigits(0);
                    first_distance_num=distance_value;
                    right_tv.setText("距離:"+String.valueOf(Math.round(distance_value))+"  cm");
                }
            }
        }

        if(click_but_hight)//開始顯示高度(測量高度)
        {
            if(click_sec_hight_but)//判斷是否按下第二次測量 (要扣掉第一次測量的值)
            {
                //判斷是否超過90度
                if(ac_z<0)
                {
                    hight_value=distance_value*(Math.tan(Math.toRadians(90-thedegree)));
                    //nf.setMaximumFractionDigits(0);
                    double the_hight =Math.abs((Double.valueOf(Double.valueOf(person_hight)))+hight_value-first_hight_num);
                    left_tv.setText("高度:"+String.valueOf(Math.round(the_hight))+"  cm");
                    bundle_hight_value=String.valueOf(Math.round(Math.abs(the_hight)));
                    if(Math.abs(the_hight)<1)
                    {
                        lock_hight.setVisibility(View.INVISIBLE);
                    }else
                    {
                        lock_hight.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    hight_value=distance_value*(Math.tan(Math.toRadians(90-thedegree)));
                    //nf.setMaximumFractionDigits(0);
                    double the_hight =Math.abs((Double.valueOf(Double.valueOf(person_hight)))-hight_value-first_hight_num);
                    left_tv.setText("高度:"+String.valueOf(Math.round(the_hight))+"  cm");
                    bundle_hight_value=String.valueOf(Math.round(Math.abs(the_hight)));
                    if(Math.abs(the_hight)<1)
                    {
                        lock_hight.setVisibility(View.INVISIBLE);
                    }else
                    {
                        lock_hight.setVisibility(View.VISIBLE);
                    }
                }

                if(ac_z<0)//警示燈
                {
                    if(Math.abs(thedegree)>40)
                    {
                        light_text.setVisibility(View.INVISIBLE);
                        light.setImageResource(R.drawable.blue);
                        reset.setVisibility(View.INVISIBLE);
                        lock_hight.setVisibility(View.VISIBLE);
                        too_hight=false;
                    }
                    else if(Math.abs(thedegree)<20)
                    {
                        light.setImageResource(R.drawable.red);
                        light_text.setVisibility(View.VISIBLE);
                        light_text.setText("目標物高於可測量距離");
                        reset.setVisibility(View.VISIBLE);
                        lock_hight.setVisibility(View.INVISIBLE);
                        too_hight=true;
                    }
                    else
                    {
                        light.setImageResource(R.drawable.yellow);
                        light_text.setVisibility(View.VISIBLE);
                        light_text.setText("目標物過高，請退後五步，重新測量距離");
                        reset.setVisibility(View.VISIBLE);
                        lock_hight.setVisibility(View.INVISIBLE);
                        too_hight=true;
                    }
                }
            }
            else
            {
                //判斷是否超過90度
                if(ac_z<0)
                {
                    hight_value=distance_value*(Math.tan(Math.toRadians(90-thedegree)));
                    //nf.setMaximumFractionDigits(0);
                    left_tv.setText("高度:"+String.valueOf(Math.round(Math.abs((Double.valueOf(Double.valueOf(person_hight)))+hight_value)))+"  cm");
                    first_hight_num=(Double.valueOf(person_hight))+hight_value;
                    bundle_hight_value=String.valueOf(Math.round(Math.abs(first_hight_num)));
                    if(Math.abs(first_hight_num)<1)
                    {
                        lock_hight.setVisibility(View.INVISIBLE);
                    }else
                    {
                        lock_hight.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    hight_value=distance_value*(Math.tan(Math.toRadians(90-thedegree)));
                    //nf.setMaximumFractionDigits(0);
                    left_tv.setText("高度:"+String.valueOf(Math.round(Math.abs((Double.valueOf(Double.valueOf(person_hight)))-hight_value)))+"  cm");
                    first_hight_num=(Double.valueOf(person_hight))-hight_value;
                    bundle_hight_value=String.valueOf(Math.round(Math.abs(first_hight_num)));
                    if(Math.abs(first_hight_num)<1)
                    {
                        lock_hight.setVisibility(View.INVISIBLE);
                    }else
                    {
                        lock_hight.setVisibility(View.VISIBLE);
                    }
                }

                if(ac_z<0)//警示燈
                {
                    if(Math.abs(thedegree)>40)
                    {
                        light_text.setVisibility(View.INVISIBLE);
                        light.setImageResource(R.drawable.blue);
                        reset.setVisibility(View.INVISIBLE);
                        lock_hight.setVisibility(View.VISIBLE);
                        too_hight=false;
                    }
                    else if(Math.abs(thedegree)<20)
                    {
                        light.setImageResource(R.drawable.red);
                        light_text.setVisibility(View.VISIBLE);
                        light_text.setText("目標物高於可測量距離");
                        reset.setVisibility(View.VISIBLE);
                        lock_hight.setVisibility(View.INVISIBLE);
                        too_hight=true;

                    }
                    else
                    {
                        light.setImageResource(R.drawable.yellow);
                        light_text.setVisibility(View.VISIBLE);
                        light_text.setText("目標物過高，請退後五步，重新測量距離");
                        reset.setVisibility(View.VISIBLE);
                        lock_hight.setVisibility(View.INVISIBLE);
                        too_hight=true;
                    }
                }
            }

        }

    }

    public  double  getRadians( double  degree){
        return  Math.PI*degree/180d;
    }

    public  double  getDegrees( double  radian){
        return  180d*radian/Math.PI;
    }


    @Override
    protected  void  onPause() {
        // TODO Auto-generated method stub
        super .onPause();
        manager.unregisterListener( this );
    }

    @Override
    protected  void  onResume() {
        // TODO Auto-generated method stub
        super .onResume();
        manager.registerListener( this , manager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
        manager.registerListener( this , manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceholder)
    {
        try
        {
	      /* 打開相機， */
            mCamera = Camera.open();
            mCamera.setPreviewDisplay(holder);
        }
        catch (IOException exception)
        {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceholder,
                               int format,int w,int h)
    {
	    /* 相機初始化 */
        initCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceholder)
    {
        stopCamera();
        mCamera.release();
        mCamera = null;
    }
    private Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback()
    {
        public void onShutter()
        {

        }
    };

    private Camera.PictureCallback rawCallback = new Camera.PictureCallback()
    {
        public void onPictureTaken(byte[] _data, Camera _camera)
        {

        }
    };

    private Camera.PictureCallback TakeJpeg  = new Camera.PictureCallback()
    {
        public void onPictureTaken(byte[] _data, Camera _camera)
        {
	      /* 取得相片 */
            try
            {
                take_pic_boolean=true;
                if(switch_pic_side.equals("right"))
                {
	    		/* 取得相片Bitmap物件 */
                    bmp = BitmapFactory.decodeByteArray(_data, 0,_data.length);
		 	       /* 儲存檔案 */
                    if(bmp!=null)
                    {
		 	          /* 檢視SDCard是否存在 */
                        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
                        {
		 	            /* SD卡不存在，顯示Toast訊息 */
                            Toast.makeText(measure_session2_m.this,"SD卡不存在!無法儲存相片",
                                    Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            try
                            {
		 	              /* 資料夾不在就先建立 */
                                File f=new File
                                        (
                                                Environment.getExternalStorageDirectory(),path
                                        );

                                if(!f.exists())
                                {
                                    f.mkdir();
                                }

		 	              /* 儲存相片檔 */
                                File n=new File(f,"1.jpg");
                                FileOutputStream bos =
                                        new FileOutputStream(n.getAbsolutePath());
		 	              /* 檔案轉換 */
                                bmp.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		 	              /* 呼叫flush()方法，更新BufferStream */

                                bos.flush();
		 	              /* 結束OutputStream */
                                bos.close();
                                stopCamera();
                                initCamera();
                                bitmap = BitmapFactory.decodeFile("/sdcard/"+path+"/1.jpg");
                                right_pic.setImageBitmap(bitmap);
                                right_pic.setVisibility(View.VISIBLE);
                                right_cross.setVisibility(View.VISIBLE);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                else if(switch_pic_side.equals("left"))
                {
	    		/* 取得相片Bitmap物件 */
                    bmp = BitmapFactory.decodeByteArray(_data, 0,_data.length);
		 	       /* 儲存檔案 */
                    if(bmp!=null)
                    {
                        try
                        {
		 	              /* 資料夾不在就先建立 */
                            File f=new File
                                    (
                                            Environment.getExternalStorageDirectory(),path
                                    );

                            if(!f.exists())
                            {
                                f.mkdir();
                            }

		 	              /* 儲存相片檔 */
                            File n=new File(f,"2.jpg");
                            FileOutputStream bos =
                                    new FileOutputStream(n.getAbsolutePath());
		 	              /* 檔案轉換 */
                            bmp.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		 	              /* 呼叫flush()方法，更新BufferStream */
                            bos.flush();
		 	              /* 結束OutputStream */
                            bos.close();
                            stopCamera();
                            initCamera();
                            bitmap = BitmapFactory.decodeFile("/sdcard/"+path+"/2.jpg");
                            left_pic.setImageBitmap(bitmap);
                            left_pic.setVisibility(View.VISIBLE);
                            left_cross.setVisibility(View.VISIBLE);

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                else if(switch_pic_side.equals("width")||switch_pic_side.equals("get_area_wall_right_top"))
                {
	    		/* 取得相片Bitmap物件 */
                    bmp = BitmapFactory.decodeByteArray(_data, 0,_data.length);
                    bmp = Bitmap.createBitmap(bmp, mGameView.sx/2, 0, bmp.getWidth() - mGameView.sx, bmp.getHeight());
		 	       /* 儲存檔案 */
                    if(bmp!=null)
                    {
                        try
                        {
		 	              /* 資料夾不在就先建立 */
                            File f=new File
                                    (
                                            Environment.getExternalStorageDirectory(),path
                                    );

                            if(!f.exists())
                            {
                                f.mkdir();
                            }
                            if(!(measure_type.equals("get_area_wall")||switch_pic_side.equals("get_area_wall_right_top")))  //如果measure_type.equals("get_area_wall") 照片顯示在右下角
                            {
			 	              /* 儲存相片檔 */
                                File n=new File(f,"2.jpg");
                                FileOutputStream bos =
                                        new FileOutputStream(n.getAbsolutePath());
			 	              /* 檔案轉換 */
                                bmp.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			 	              /* 呼叫flush()方法，更新BufferStream */
                                bos.flush();
			 	              /* 結束OutputStream */
                                bos.close();
                                stopCamera();
                                initCamera();
                                bitmap = BitmapFactory.decodeFile("/sdcard/"+path+"/2.jpg");
                                left_pic.setImageBitmap(bitmap);
                                left_pic.setVisibility(View.VISIBLE);
                                left_cross.setVisibility(View.VISIBLE);

                            }else
                            {
			 	              /* 儲存相片檔 */
                                File n=new File(f,"1.jpg");
                                FileOutputStream bos =
                                        new FileOutputStream(n.getAbsolutePath());
			 	              /* 檔案轉換 */
                                bmp.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			 	              /* 呼叫flush()方法，更新BufferStream */

                                bos.flush();
			 	              /* 結束OutputStream */
                                bos.close();
                                stopCamera();
                                initCamera();
                                bitmap = BitmapFactory.decodeFile("/sdcard/"+path+"/1.jpg");
                                right_pic.setImageBitmap(bitmap);
                                right_pic.setVisibility(View.VISIBLE);
                                right_cross.setVisibility(View.VISIBLE);

                            }

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                else if(switch_pic_side.equals("right_top"))
                {
	    		/* 取得相片Bitmap物件 */
                    bmp = BitmapFactory.decodeByteArray(_data, 0,_data.length);
                    if(concept_area2.measure_shap.equals("trapezoidal_m"))
                    {
                        bmp = Bitmap.createBitmap(bmp, mGameView.sx/2, 0, bmp.getWidth() - mGameView.sx, bmp.getHeight());
                    }
		 	       /* 儲存檔案 */
                    if(bmp!=null)
                    {
		 	          /* 檢視SDCard是否存在 */
                        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
                        {
		 	            /* SD卡不存在，顯示Toast訊息 */
                            Toast.makeText(measure_session2_m.this,"SD卡不存在!無法儲存相片",
                                    Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            try
                            {
		 	              /* 資料夾不在就先建立 */
                                File f=new File
                                        (
                                                Environment.getExternalStorageDirectory(),path
                                        );

                                if(!f.exists())
                                {
                                    f.mkdir();
                                }

		 	              /* 儲存相片檔 */
                                File n=new File(f,"3.jpg");
                                FileOutputStream bos =
                                        new FileOutputStream(n.getAbsolutePath());
		 	              /* 檔案轉換 */
                                bmp.compress(Bitmap.CompressFormat.JPEG, 80, bos);

		 	              /* 呼叫flush()方法，更新BufferStream */
                                bos.flush();
		 	              /* 結束OutputStream */
                                bos.close();
                                stopCamera();
                                initCamera();
                                bitmap = BitmapFactory.decodeFile("/sdcard/"+path+"/3.jpg");
                                right_pic_top.setImageBitmap(bitmap);
                                right_pic_top.setVisibility(View.VISIBLE);
                                right_cross_top.setVisibility(View.VISIBLE);

                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                else if(switch_pic_side.equals("left_top"))
                {
	    		/* 取得相片Bitmap物件 */
                    bmp = BitmapFactory.decodeByteArray(_data, 0,_data.length);
                    if(concept_area2.measure_shap.equals("trapezoidal_m"))
                    {
                        bmp = Bitmap.createBitmap(bmp, mGameView.sx/2, 0, bmp.getWidth() - mGameView.sx, bmp.getHeight());
                    }
		 	       /* 儲存檔案 */
                    if(bmp!=null)
                    {
		 	          /* 檢視SDCard是否存在 */
                        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
                        {
		 	            /* SD卡不存在，顯示Toast訊息 */
                            Toast.makeText(measure_session2_m.this,"SD卡不存在!無法儲存相片",
                                    Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            try
                            {
		 	              /* 資料夾不在就先建立 */
                                File f=new File
                                        (
                                                Environment.getExternalStorageDirectory(),path
                                        );

                                if(!f.exists())
                                {
                                    f.mkdir();
                                }

		 	              /* 儲存相片檔 */
                                File n=new File(f,"4.jpg");
                                FileOutputStream bos =
                                        new FileOutputStream(n.getAbsolutePath());
		 	              /* 檔案轉換 */
                                bmp.compress(Bitmap.CompressFormat.JPEG, 80, bos);

		 	              /* 呼叫flush()方法，更新BufferStream */
                                bos.flush();
		 	              /* 結束OutputStream */
                                bos.close();
                                stopCamera();
                                initCamera();
                                bitmap = BitmapFactory.decodeFile("/sdcard/"+path+"/4.jpg");
                                left_pic_top.setImageBitmap(bitmap);
                                left_pic_top.setVisibility(View.VISIBLE);
                                left_cross_top.setVisibility(View.VISIBLE);

                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }else if(switch_pic_side.equals("don't show"))
                {
	    		/* 取得相片Bitmap物件 */
                    bmp = BitmapFactory.decodeByteArray(_data, 0,_data.length);
		 	       /* 儲存檔案 */
                    if(bmp!=null)
                    {
		 	          /* 檢視SDCard是否存在 */
                        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
                        {
		 	            /* SD卡不存在，顯示Toast訊息 */
                            Toast.makeText(measure_session2_m.this,"SD卡不存在!無法儲存相片",
                                    Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            try
                            {
		 	              /* 資料夾不在就先建立 */
                                File f=new File
                                        (
                                                Environment.getExternalStorageDirectory(),path
                                        );

                                if(!f.exists())
                                {
                                    f.mkdir();
                                }
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");//日期
		 	              /* 儲存相片檔 */
                                temp_pic_storename=concept_area2.user_id+ sdf.format(new Date())+".jpg";
                                File n=new File(f,temp_pic_storename);
                                FileOutputStream bos =
                                        new FileOutputStream(n.getAbsolutePath());
		 	              /* 檔案轉換 */
                                bmp.compress(Bitmap.CompressFormat.JPEG, 80, bos);

		 	              /* 呼叫flush()方法，更新BufferStream */
                                bos.flush();
		 	              /* 結束OutputStream */
                                bos.close();
                                stopCamera();
                                initCamera();
                                final ProgressDialog myDialog = ProgressDialog.show(measure_session2_m.this, "請稍後片刻", "圖片儲存中....");
                                new Thread()
                                {
                                    public void run()
                                    {
                                        try
                                        {
                                            sleep(1000);

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
                                cut_pic.setVisibility(View.VISIBLE);
                                but_lock_distance.setVisibility(View.INVISIBLE);

                                if(!concept_area2.session_one){
                                    measure_process_tv.setText(Html.fromHtml(measure_process_first_step[2]));
                                }
                                guidance_tv.setText("點選裁切鍵，進行裁切");
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    };

    /* 相機初始化的method */
    private void initCamera()
    {
        if (mCamera != null)
        {
            try
            {
                Camera.Parameters parameters = mCamera.getParameters();
	        /* 設定相片大小為1024*768，
	                             格式為JPG */
                parameters.setPictureFormat(PixelFormat.JPEG);
                parameters.setPreviewSize(1280, 960);
                parameters.setPictureSize(640,480);
                mCamera.setParameters(parameters);
	        /* 開啟預覽畫面 */
                mCamera.startPreview();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /* 停止相機的method */
    private void stopCamera()
    {
        if (mCamera != null)
        {
            try
            {
	        /* 停止預覽 */
                mCamera.stopPreview();
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(touch_srceen)
        {
            float x=event.getX();
            float y=event.getY();

            switch(event.getAction())
            {
                case MotionEvent.ACTION_DOWN:
                    startx=x;
                    starty=y;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(!lock_width)//判斷是否鎖定寬度
                    {
                        //判斷x軸是否過半 過半的話 ex與sx對調
                        if((int) x < 1000)
                        {
                            mGameView.ex=1000+ Math.abs(1000 -(int) x);
                            mGameView.sx =(int) x;
                        }
                        else
                        {
                            mGameView.ex = (int) x;
                            mGameView.sx =1000- Math.abs(1000 -(int) x);
                        }


                        mGameView.ey = (int) y;
                        mGameView.postInvalidate();
					/*
					Log.i( "SSSSSSSSSSSSSSSSSSS", String.valueOf(mGameView.sx));
					Log.i( "EEEEEEEEEEEEEEEEEEE", String.valueOf(mGameView.ex));
					Log.i( "SESESESESESESESESES", String.valueOf( Math.abs(mGameView.sx - mGameView.ex)));
					*/
					/*
					成像大小公式 W=寬度 L=距離 f=0.218焦距 h=畫面成像大小
					W=((L+f)*h)/f
					*/
					/*
					Log.i("mGameView.sx", String.valueOf(mGameView.sx));
				 	Log.i("mGameView.ex", String.valueOf(mGameView.ex));
				 	Log.i("相減", String.valueOf(Math.abs((mGameView.ex-mGameView.sx)/59.0)));
				 	*/
                        getwidth();
                    }
                    break;
                case MotionEvent.ACTION_UP:

                    break;
            }
        }
        return true;
    }


    //畫寬度的bar框
    public class GameView extends View
    {
        int  sx = 0;
        int  sy = 0;
        int  ex = 0;
        int  ey = 0;
        public GameView(Context context)
        {
            super(context);
        }
        public void onDraw(Canvas canvas) //畫寬度bar的圖
        {
            Paint mPaint = new Paint();
            mPaint.setStrokeWidth(3);
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.STROKE);

            mPaint.setColor(Color.YELLOW);
            mPaint.setAlpha(90);
				/*
				Paint mmPaint = new Paint();
				mmPaint.setStrokeWidth(1);
				mmPaint.setAntiAlias(true);
				mmPaint.setStyle(Paint.Style.FILL_AND_STROKE);

				mmPaint.setColor(Color.BLUE);
				mmPaint.setAlpha(80);
				*/

            canvas.drawRect(sx, 0 , ex, 1485 , mPaint);
            //canvas.drawRect(sx+10, 0 , ex-10, 960 , mmPaint);
        }
    }
    //內建裁切code
    private void cropImageUri(Uri uri){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, 3);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != Activity.RESULT_OK){//result is not correct
            cut_pic.setVisibility(View.INVISIBLE);
            if(!concept_area2.session_one){
                measure_process_tv.setText(Html.fromHtml(measure_process_first_step[1]));
            }
            board_message = "將螢幕中央十字對準物件進行<font color='red'>拍照</font>";
            guidance_tv.setText(Html.fromHtml(board_message));
            //guidance_tv.setText("將螢幕中央十字對準物件進行拍照");
            take_pic.setVisibility(View.VISIBLE);
            return;
        }else
        {
            cut_pic.setVisibility(View.INVISIBLE);
            but_lock_distance.setVisibility(View.VISIBLE);
            right_tv.setVisibility(View.VISIBLE);
            if(!measure_type.equals("get_area_ground"))
            {
                if(!concept_area2.session_one){
                    measure_process_tv.setText(Html.fromHtml(measure_process_first_step[3]));
                }
                board_message = "測量距離<font color='red'>(將螢幕中央十字對準物件與地面交界處)</font>";
                guidance_tv.setText(Html.fromHtml(board_message));
                //guidance_tv.setText("測量距離(將螢幕中央十字對準物件與地面交界處)");
            }else
            {
                if(!concept_area2.session_one){
                    measure_process_tv.setText(Html.fromHtml(measure_process_first_step[3]));
                }
                board_message = "測量距離<font color='red'>(將螢幕中央十字對準物件的底部)</font>";
                guidance_tv.setText(Html.fromHtml(board_message));
                //guidance_tv.setText("測量距離(將螢幕中央十字對準物件的底部)");
            }
            return;
        }
    }
    //儲存圖片的dialog
    private void Store_Image_dialog()
    {
        ImageDialog = ProgressDialog.show(measure_session2_m.this, "請稍後片刻", "圖片儲存中....");
        new Thread()
        {
            public void run()
            {
                try
                {
                    sleep(3000);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    ImageDialog.dismiss();
                }
            }
        }.start();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(concept_area2.switch_practice_homework.equals("homework"))
            {
                try{
                    stopCamera();
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
                new AlertDialog.Builder(measure_session2_m.this)
                        .setTitle("警告")
                        .setMessage("確定要放棄此次測量嗎?")
                        .setPositiveButton("確認",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which)
                            {
		   	            	 /*
		   	            	dbHelper = new SQLite(measure_session2_m.this, "record_action");
		      				dbHelper.Insert_Action(login.homework_page,"giveup_do_homework_Back_to_homework_list");
		      				dbHelper.close();//關閉資料庫
		   	            	 */
                                Intent intent = new Intent();
                                intent.setClass(measure_session2_m.this, homework_list.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("user_answer", "null");
                                intent.putExtras(bundle);
                                startActivity(intent);
                                measure_session2_m.this.finish();
                                release_memory();
                            }
                        })
                        .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {

                            }
                        })
                        .show();
            }else
            {
                new AlertDialog.Builder(measure_session2_m.this)
                        .setTitle("警告")
                        .setMessage("確定要放棄此次測量嗎?")
                        .setPositiveButton("確認",new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int which)
                            {
                                try{
                                    stopCamera();
                                }catch(Exception e)
                                {
                                    e.printStackTrace();
                                }
                                if(concept_area2.session_one)
                                {
                                    dbHelper = new SQLite(measure_session2_m.this, "record_session_one_practice");
                                    dbHelper.Insert_session_one_practice("giveup");
                                    dbHelper.close();//關閉資料庫
                                }
                                concept_area2.session_one=false;
			   	 			/*
			   	 			dbHelper = new SQLite(measure_session2_m.this, "record_action");
			   	 			dbHelper.Insert_Action(login.measuer_page,"giveup_To_MainActivity");
			   	 			dbHelper.close();//關閉資料庫
			   	 			*/
                                release_memory();
                                measure_session2_m.this.finish();
                            }
                        })
                        .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {

                            }
                        })
                        .show();
            }
            return true;
        }
        return false;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        release_memory();
    }

}
