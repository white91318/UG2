package lab.hwang.ug;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class measure_session4p_makeup extends Activity implements
        SensorEventListener, SurfaceHolder.Callback, View.OnTouchListener {

    // class
    private SQLite dbHelper;// 操作SQLite資料庫

    // measure_board_guidence
    String measure_process_first_step[] = new String[10];
    String measure_rectangle_process[] = new String[10];
    String measure_others_process[] = new String[10];
    String measure_trapezoidal_process[] = new String[10];
    String measure_diamond_process[] = new String[10];

    // 物件
    SensorManager manager; // ????
    TextView person_hight_alert, right_tv, left_tv, light_text, guidance_tv,
            measure_process_tv;
    ImageView right_pic, left_pic, light, right_cross, left_cross,
            right_pic_top, right_cross_top, left_pic_top, left_cross_top;
    Button but_lock_distance, get_hight, lock_hight, get_width, but_lock_width,
            reset, get_distance_sec, get_hight_sec, zoom_in, zoom_out, cut_pic,
            go_calculate, take_pic;

    // 變數
    String switch_pic_side = "right";
    String measure_type;
    String person_hight;
    Bitmap bmp; // 照片
    Bitmap bitmap;
    private String path = "file_data";// 照片路徑
    Double thedegree;// 平板前後傾斜角
    double distance_value, hight_value, width_value; // 距離值.高值.寬值
    Boolean click_but_distance = false, click_but_hight = false,
            click_but_width = false, lock_width = false,
            start_get_width = false, width_sec = false;
    Boolean click_sec_distance_but = false, click_sec_hight_but = false,
            too_hight = false;
    float azimuth = 0, pitch = 0, roll = 0, x = 0, y = 0, z = 0, ac_x = 0,
            ac_y = 0, ac_z = 0;
    NumberFormat nf = NumberFormat.getInstance();
    final double focal_lenth = 0.218; // 相機焦距
    double first_distance_num, first_hight_num;// 紀錄非地板上的物件,第一次測量結果
    public ProgressDialog ImageDialog = null; // 儲存圖片的thread
    public static String bundle_width_value = "null",
            bundle_hight_value = "null", bundle_width2_value = "null"; // 綑綁值????
    public static String temp_pic_storename; // 紀錄切割後的圖檔名稱
    public static String user_practice_shape;
    Boolean touch_srceen = false;
    Boolean take_pic_boolean = false, width_touch_line = false;

    String chinese_shape = "";
    String board_message;
    // 相機
    Camera mCamera;
    SurfaceView mSurfaceView; // 介面黏貼處
    SurfaceHolder holder;
    AbsoluteLayout lyaa; // 元件佈局 ????
    // ///////////////////
    double bili = 1;
    double chaw = 0;
    double chah = 0;
    int bmh = 0;
    int bmw = 0;
    float startx = 0.0f;
    float starty = 0.0f;
    float endx = 0.0f;
    float endy = 0.0f;
    boolean CutDone = false;

    // private GameView mGameView = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.measure_session_four_makeup); // 連接到的介面
        findview();

        bundle_width_value = "null";
        bundle_hight_value = "null";
        bundle_width2_value = "null";

        // 矩形rectangle 三角形triangle 梯形trapezoidal 平行四邊形parallel 菱形diamond
        if (next_one_activity2p.measure_shap.equals("triangle"))
            chinese_shape = "三角形";
        else if (next_one_activity2p.measure_shap.equals("parallel"))
            chinese_shape = "平行四邊形";
        else if (next_one_activity2p.measure_shap.equals("diamond"))
            chinese_shape = "菱形";

        // measure_process 字串宣告

        measure_process_first_step[4] = "■拍照 ■裁切 ■測量與物件距離"; // 固定需要這三個

        measure_rectangle_process[0] = " □測量長方形長度 □測量長方形寬度 □計算周長";
        measure_rectangle_process[1] = measure_process_first_step[4]
                + " <font color='red'>□測量長方形長度</font> □測量長方形寬度 □計算周長";
        measure_rectangle_process[2] = measure_process_first_step[4]
                + " ■測量長方形長度 <font color='red'>□測量長方形寬度</font> □計算周長";
        measure_rectangle_process[3] = measure_process_first_step[4]
                + " ■測量長方形長度 ■測量長方形寬度 <font color='red'>□計算周長</font>";

        measure_diamond_process[0] = " □測量菱形對角線A □測量菱形對角線B □計算周長";
        measure_diamond_process[1] = measure_process_first_step[4]
                + " <font color='red'>□測量菱形對角線A</font> □測量菱形對角線B □計算周長";
        measure_diamond_process[2] = measure_process_first_step[4]
                + " ■測量菱形對角線A <font color='red'>□測量菱形對角線B</font> □計算周長";
        measure_diamond_process[3] = measure_process_first_step[4]
                + " ■測量菱形對角線A ■測量菱形對角線B <font color='red'>□計算周長</font>";

        measure_others_process[0] = " □測量" + chinese_shape + "高度 □測量"
                + chinese_shape + "寬度 □計算周長";
        measure_others_process[1] = measure_process_first_step[4]
                + " <font color='red'>□測量" + chinese_shape + "高度</font> □測量"
                + chinese_shape + "寬度 □計算周長";
        measure_others_process[2] = measure_process_first_step[4] + " ■測量"
                + chinese_shape + "高度 <font color='red'>□測量" + chinese_shape
                + "寬度</font> □計算周長";
        measure_others_process[3] = measure_process_first_step[4] + " ■測量"
                + chinese_shape + "高度 ■測量" + chinese_shape
                + "寬度 <font color='red'>□計算周長</font>";

        measure_trapezoidal_process[0] = " □測量梯形高度 □測量梯形下底 □測量梯形上底 □計算周長";
        measure_trapezoidal_process[1] = measure_process_first_step[4]
                + " <font color='red'>□測量梯形高度</font> □測量梯形下底 □測量梯形上底 □計算周長";
        measure_trapezoidal_process[2] = measure_process_first_step[4]
                + " ■測量梯形高度 <font color='red'>□測量梯形下底</font> □測量梯形上底 □計算周長";
        measure_trapezoidal_process[3] = measure_process_first_step[4]
                + " ■測量梯形高度 ■測量梯形下底 <font color='red'>□測量梯形上底</font> □計算周長";
        measure_trapezoidal_process[4] = measure_process_first_step[4]
                + " ■測量梯形高度 ■測量梯形下底 ■測量梯形上底 <font color='red'>□計算周長</font>";

        if (next_one_activity2p.measure_shap.equals("rectangle")) // 矩形
        {
            measure_process_first_step[1] = "<font color='red'>□拍照</font> □裁切 □測量與物件距離"
                    + measure_rectangle_process[0];
            measure_process_first_step[2] = "■拍照 <font color='red'>□裁切</font> □測量與物件距離"
                    + measure_rectangle_process[0];
            measure_process_first_step[3] = "■拍照 ■裁切 <font color='red'>□測量與物件距離</font>"
                    + measure_rectangle_process[0];
        } else if (next_one_activity2p.measure_shap.equals("trapezoidal"))// 梯形
        {
            measure_process_first_step[1] = "<font color='red'>□拍照</font> □裁切 □測量與物件距離"
                    + measure_trapezoidal_process[0];
            measure_process_first_step[2] = "■拍照 <font color='red'>□裁切</font> □測量與物件距離"
                    + measure_trapezoidal_process[0];
            measure_process_first_step[3] = "■拍照 ■裁切 <font color='red'>□測量與物件距離</font>"
                    + measure_trapezoidal_process[0];
        } else if (next_one_activity2p.measure_shap.equals("diamond")) // 菱形
        {
            measure_process_first_step[1] = "<font color='red'>□拍照</font> □裁切 □測量與物件距離"
                    + measure_diamond_process[0];
            measure_process_first_step[2] = "■拍照 <font color='red'>□裁切</font> □測量與物件距離"
                    + measure_diamond_process[0];
            measure_process_first_step[3] = "■拍照 ■裁切 <font color='red'>□測量與物件距離</font>"
                    + measure_diamond_process[0];
        } else { // 平行四邊形 三角形
            measure_process_first_step[1] = "<font color='red'>□拍照</font> □裁切 □測量與物件距離"
                    + measure_others_process[0];
            measure_process_first_step[2] = "■拍照 <font color='red'>□裁切</font> □測量與物件距離"
                    + measure_others_process[0];
            measure_process_first_step[3] = "■拍照 ■裁切 <font color='red'>□測量與物件距離</font>"
                    + measure_others_process[0];
        }

        person_hight = String.valueOf(Double
                .valueOf(next_one_activity2p.person_hight) - 5);// 載入使用者身高
        measure_type = next_one_activity2p.person_select_measure_type; // 載入使用者測量選項
        Log.i("person_hight", person_hight);
        // 註冊sensor
        manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // 註冊camera
        holder = mSurfaceView.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        stopCamera();
        initCamera();

        // final_but check
        if (next_one_activity2p.switch_practice_homework.equals("map_add_poi")) {
            go_calculate.setBackgroundResource(R.drawable.add_poi_but);
        } else {
            go_calculate.setBackgroundResource(R.drawable.makeup_pbut);
        }

        // 註冊寬度bar
        // mGameView = new GameView(this);
        // 註冊AbsoluteLayout變數
        lyaa = (AbsoluteLayout) findViewById(R.id.AbsoluteLayout1);
        // 註冊ontouch事件
        mSurfaceView.setOnTouchListener(this);

        board_message = "測量距離<font color='red'>(將螢幕中央十字對準物件與地面交界處)</font>";// S1
        guidance_tv.setText(Html.fromHtml(board_message));// 下面的框框
        // get_area_ground 長(兩個距離相減)
        // get_area_wall 物件在牆上 長 (兩個高相減)ex時鐘
        // get_area_ground_hight 物件在地板上 長(一個高)ex門
        if (measure_type.equals("get_area_ground")
                || measure_type.equals("get_area_wall")
                || measure_type.equals("get_area_ground_hight")) // 拍攝面積，先隱藏距離but,先拍照存檔
        {// S2
            if (!next_one_activity2p.session_one) {
                measure_process_tv.setText(Html
                        .fromHtml(measure_process_first_step[1]));
            }
            go_calculate.setVisibility(View.INVISIBLE);
            but_lock_distance.setVisibility(View.INVISIBLE);
            board_message = "將螢幕中央十字對準物件進行<font color='red'>拍照</font>";
            guidance_tv.setText(Html.fromHtml(board_message));// 下面的框框
            // guidance_tv.setText("將螢幕中央十字對準物件進行拍照");
            take_pic.setVisibility(View.VISIBLE);
            // right_tv.setVisibility(View.INVISIBLE);
        }

        if (measure_type.equals("distance_sky"))// m4
        {
            board_message = "拍攝完成<font color='red'>(請進入周長組合進行學習)</font>";
            guidance_tv.setText(Html.fromHtml(board_message));

            // guidance_tv.setText("測量距離(將螢幕中央十字對準物件底部)");
        }
        take_pic.setOnClickListener(new Button.OnClickListener() // 拍照->裁切->測量
        {
            @Override
            public void onClick(View v) {
                switch_pic_side = "don't show";
                mCamera.takePicture(shutterCallback, rawCallback, TakeJpeg);
                take_pic.setVisibility(View.INVISIBLE);

            }
        });

        cut_pic.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                cut_pic.setClickable(false);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cut_pic.setClickable(true);
                    }
                }, 2000);
                switch_pic_side = "right";
                Store_Image_dialog();
                Uri imageUri = Uri.parse("file:///sdcard/file_data/"
                        + temp_pic_storename);
                cropImageUri(imageUri);

            }
        });

        go_calculate.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                ini_var();
                stopCamera();
                initCamera();

                final Intent intent = new Intent();

                if (next_one_activity2p.switch_practice_homework
                        .equals("practice")) {

                    if (next_one_activity2p.measure_shap.equals("rectangle")) {// 長方形
                        intent.setClass(measure_session4p_makeup.this,
                                concept_area3.class);
                        startActivity(intent);
                        measure_session4p_makeup.this.finish();
                        release_memory();

                    }
                }
            }
        });
        reset.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                release_memory();
                measure_session4p_makeup.this.finish();
            }
        });
    }

    public void findview() {

        mSurfaceView = (SurfaceView) findViewById(R.id.mSurfaceView4);
        // //////////////////////////TextView

        right_tv = (TextView) findViewById(R.id.right_tv4);
        left_tv = (TextView) findViewById(R.id.left_tv4);
        light_text = (TextView) findViewById(R.id.light_text4);
        guidance_tv = (TextView) findViewById(R.id.guidance_tv4);
        measure_process_tv = (TextView) findViewById(R.id.measure_process_tv4);
        // /////////////////////////ImageView
        right_cross = (ImageView) findViewById(R.id.right_cross4);
        left_cross = (ImageView) findViewById(R.id.left_cross4);
        right_pic = (ImageView) findViewById(R.id.right_pic4);
        left_pic = (ImageView) findViewById(R.id.left_pic4);
        light = (ImageView) findViewById(R.id.light4);
        right_pic_top = (ImageView) findViewById(R.id.right_pic_top4);
        right_cross_top = (ImageView) findViewById(R.id.right_cross_top4);
        left_pic_top = (ImageView) findViewById(R.id.left_pic_top4);
        left_cross_top = (ImageView) findViewById(R.id.left_cross_top4);
        // /////////////////////////Button
        but_lock_distance = (Button) findViewById(R.id.lock_distance4);
        get_hight = (Button) findViewById(R.id.get_hight4);
        lock_hight = (Button) findViewById(R.id.lock_hight4);
        get_width = (Button) findViewById(R.id.get_width4);
        but_lock_width = (Button) findViewById(R.id.lock_width4);
        reset = (Button) findViewById(R.id.reset4);
        get_distance_sec = (Button) findViewById(R.id.get_distance_sec4);
        get_hight_sec = (Button) findViewById(R.id.get_hight_sec4);
        zoom_in = (Button) findViewById(R.id.zoom_in4);
        zoom_out = (Button) findViewById(R.id.zoom_out4);
        cut_pic = (Button) findViewById(R.id.cut_pic4);
        go_calculate = (Button) findViewById(R.id.calculate_but4);
        take_pic = (Button) findViewById(R.id.take_pic4);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.layout.menu, menu);
        return true;
    }

    private void release_memory() {
        // TODO Auto-generated method stub
        try {
            if (bmp.isRecycled() == false)
                bmp.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (bitmap.isRecycled() == false)
                bitmap.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.gc();
    }

    // 初始化變數
    public void ini_var() {
        thedegree = 0.0;
        distance_value = 0.0;
        hight_value = 0.0;
        width_value = 0.0;
        click_but_distance = false;
        click_but_hight = false;
        click_but_width = false;
        lock_width = false;
        start_get_width = false;
        click_sec_distance_but = false;
        click_sec_hight_but = false;
        too_hight = false;
        azimuth = 0;
        pitch = 0;
        roll = 0;
        x = 0;
        y = 0;
        z = 0;
        ac_x = 0;
        ac_y = 0;
        ac_z = 0;
        switch_pic_side = "right";

        first_distance_num = 0.0;
        first_hight_num = 0.0;

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
        but_lock_distance.setVisibility(View.INVISIBLE);
        // lyaa.removeView(mGameView);

        get_distance_sec.setVisibility(View.INVISIBLE);
        right_pic_top.setVisibility(View.INVISIBLE);
        right_cross_top.setVisibility(View.INVISIBLE);
        get_hight_sec.setVisibility(View.INVISIBLE);
        left_pic_top.setVisibility(View.INVISIBLE);
        left_cross_top.setVisibility(View.INVISIBLE);

        zoom_in.setVisibility(View.INVISIBLE);
        zoom_out.setVisibility(View.INVISIBLE);

        board_message = "拍攝完成<font color='red'>(請進入周長組合進行學習)</font>";
        guidance_tv.setText(Html.fromHtml(board_message));
        // guidance_tv.setText("測量距離(將螢幕中央十字對準物件與地面交界處)");

        cut_pic.setVisibility(View.INVISIBLE);
        go_calculate.setVisibility(View.VISIBLE);

        take_pic.setVisibility(View.INVISIBLE);
        if (take_pic_boolean) {
            bmp.recycle();
            bitmap.recycle();
            System.gc();
            take_pic_boolean = false;
        }
    }

    // 判斷AlertDialog input 值
    public static boolean validate_input(String s) {
        return (49 < Integer.valueOf(s) && 201 > Integer.valueOf(s));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSensorChanged(SensorEvent e) {

    }

    public double getRadians(double degree) {
        return Math.PI * degree / 180d;
    }

    public double getDegrees(double radian) {
        return 180d * radian / Math.PI;
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        manager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        manager.registerListener(this,
                manager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_NORMAL);
        manager.registerListener(this,
                manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceholder) {
        try {
			/* 打開相機， */
            mCamera = Camera.open();
            mCamera.setPreviewDisplay(holder);
        } catch (IOException exception) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceholder, int format, int w,
                               int h) {
		/* 相機初始化 */
        initCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceholder) {
        stopCamera();
        mCamera.release();
        mCamera = null;
    }

    private Camera.ShutterCallback shutterCallback = new Camera.ShutterCallback() {
        public void onShutter() {

        }
    };

    private Camera.PictureCallback rawCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] _data, Camera _camera) {

        }
    };

    private Camera.PictureCallback TakeJpeg = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] _data, Camera _camera) {
            // 取得相片
            if (switch_pic_side.equals("don't show")) {
				/* 取得相片Bitmap物件 */
                bmp = BitmapFactory.decodeByteArray(_data, 0, _data.length);
				/* 儲存檔案 */
                if (bmp != null) {
					/* 檢視SDCard是否存在 */
                    if (!Environment.MEDIA_MOUNTED.equals(Environment
                            .getExternalStorageState())) {
						/* SD卡不存在，顯示Toast訊息 */
                        Toast.makeText(measure_session4p_makeup.this,
                                "SD卡不存在!無法儲存相片", Toast.LENGTH_LONG).show();
                    } else {
                        try {
							/* 資料夾不在就先建立 */
                            File f = new File(
                                    Environment.getExternalStorageDirectory(),
                                    path);

                            if (!f.exists()) {
                                f.mkdir();
                            }
                            SimpleDateFormat sdf = new SimpleDateFormat(
                                    "yyyyMMddHHmmss");// 日期
							/* 儲存相片檔 */
                            temp_pic_storename = next_one_activity2p.user_id
                                    + sdf.format(new Date()) + ".jpg";
                            File n = new File(f, temp_pic_storename);
                            FileOutputStream bos = new FileOutputStream(
                                    n.getAbsolutePath());
							/* 檔案轉換 */
                            bmp.compress(Bitmap.CompressFormat.JPEG, 80, bos);

							/* 呼叫flush()方法，更新BufferStream */
                            bos.flush();
							/* 結束OutputStream */
                            bos.close();
                            stopCamera();
                            initCamera();
                            final ProgressDialog myDialog = ProgressDialog
                                    .show(measure_session4p_makeup.this,
                                            "請稍後片刻", "圖片儲存中....");
                            new Thread() {
                                public void run() {
                                    try {
                                        sleep(1000);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                        myDialog.dismiss();
                                    }
                                }
                            }.start();
                            cut_pic.setVisibility(View.VISIBLE);
                            go_calculate.setVisibility(View.INVISIBLE);

                            if (!next_one_activity2p.session_one) {
                                measure_process_tv
                                        .setText(Html
                                                .fromHtml(measure_process_first_step[2]));
                            }
                            guidance_tv.setText("點選裁切鍵，進行裁切");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

    };

    /* 相機初始化的method */
    private void initCamera() {
        if (mCamera != null) {
            try {
                Camera.Parameters parameters = mCamera.getParameters();
				/*
				 * 設定相片大小為1024*768， 格式為JPG
				 */
                parameters.setPictureFormat(PixelFormat.JPEG);
                parameters.setPreviewSize(1280, 960);
                parameters.setPictureSize(640, 480);
                mCamera.setParameters(parameters);
				/* 開啟預覽畫面 */
                mCamera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* 停止相機的method */
    private void stopCamera() {
        if (mCamera != null) {
            try {
				/* 停止預覽 */
                mCamera.stopPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 內建裁切code
    private void cropImageUri(Uri uri) {
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {// result is not correct
            cut_pic.setVisibility(View.INVISIBLE);
            if (!next_one_activity2p.session_one) {
                measure_process_tv.setText(Html
                        .fromHtml(measure_process_first_step[1]));
            }
            board_message = "將螢幕中央十字對準物件進行<font color='red'>拍照</font>";
            guidance_tv.setText(Html.fromHtml(board_message));
            // guidance_tv.setText("將螢幕中央十字對準物件進行拍照");
            take_pic.setVisibility(View.VISIBLE);
            return;
        } else {
            cut_pic.setVisibility(View.INVISIBLE);
            go_calculate.setVisibility(View.VISIBLE);
            // right_tv.setVisibility(View.VISIBLE);
            if (!measure_type.equals("get_area_ground")) {
                if (!next_one_activity2p.session_one) {
                    measure_process_tv.setText(Html
                            .fromHtml(measure_process_first_step[3]));
                }
                board_message = "測量距離<font color='red'>(將螢幕中央十字對準物件與地面交界處)</font>";
                guidance_tv.setText(Html.fromHtml(board_message));
                // guidance_tv.setText("測量距離(將螢幕中央十字對準物件與地面交界處)");
            } else {
                if (!next_one_activity2p.session_one) {
                    measure_process_tv.setText(Html
                            .fromHtml(measure_process_first_step[3]));
                }
                board_message = "拍攝完成<font color='red'>(請進入周長組合進行學習)</font>";
                guidance_tv.setText(Html.fromHtml(board_message));
                // guidance_tv.setText("測量距離(將螢幕中央十字對準物件的底部)");
            }
            return;
        }
    }

    // 儲存圖片的dialog
    private void Store_Image_dialog() {
        ImageDialog = ProgressDialog.show(measure_session4p_makeup.this,
                "請稍後片刻", "圖片儲存中....");
        new Thread() {
            public void run() {
                try {
                    sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    ImageDialog.dismiss();
                }
            }
        }.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (next_one_activity2p.switch_practice_homework.equals("homework")) {
                try {
                    stopCamera();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                new AlertDialog.Builder(measure_session4p_makeup.this)
                        .setTitle("警告")
                        .setMessage("確定要放棄此次測量嗎?")
                        .setPositiveButton("確認",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        Intent intent = new Intent();
                                        intent.setClass(
                                                measure_session4p_makeup.this,
                                                homework_list.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("user_answer", "null");
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                        measure_session4p_makeup.this.finish();
                                        release_memory();
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                    }
                                }).show();
            } else {
                new AlertDialog.Builder(measure_session4p_makeup.this)
                        .setTitle("警告")
                        .setMessage("確定要放棄此次測量嗎?")
                        .setPositiveButton("確認",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        try {
                                            stopCamera();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        if (next_one_activity2p.session_one) {
                                            dbHelper = new SQLite(
                                                    measure_session4p_makeup.this,
                                                    "record_session_one_practice");
                                            dbHelper.Insert_session_one_practice("giveup");
                                            dbHelper.close();// 關閉資料庫
                                        }
                                        next_one_activity2p.session_one = false;

                                        release_memory();
                                        measure_session4p_makeup.this.finish();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        release_memory();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        return false;
    }

}