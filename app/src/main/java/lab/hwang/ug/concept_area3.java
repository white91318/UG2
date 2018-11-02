package lab.hwang.ug;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static lab.hwang.ug.next_one_activity2p.measure_shap;
import static lab.hwang.ug.next_one_activity2p.person_select_measure_type;
public class concept_area3 extends Activity implements View.OnTouchListener {
    private SQLite dbHelper;
    private Bitmap mBitmap;
    Bitmap bitmap, block_bitmap;
    TextView person_hight_tv, version;
    ImageView object_concept1, imageView_r1, imageView_t1, imageView_d2,
            imageView_d1, imageView_t2;
    ImageView r_image01, r_image02, r_image03, t_image01, t_image02, t_image03,p_image01, p_image02, p_image03,d_image01, d_image02, d_image03, t2_image01, t2_image02, t2_image03;
    Button r_but01, r_but02, r_but03, t_but01, t_but02, t_but03, p_but01, p_but02, p_but03, d_but01, d_but02, d_but03, t2_but01, t2_but02, t2_but03;
    Button concept_total; //計算總面積
    String tempsdf, process_store_name;
    SimpleDateFormat sdf;
    boolean con=false; //判斷長按之後拖動

    public static String user_id = Login.user;
    public static String person_hight = "", new_version = "";
    //public String person_select_measure_type = "";
    public static String switch_practice_homework = "";
    public static Boolean session_one = false;
    //public String measure_shap = "";
    public static String measure_shap2 = "";
    // public static Boolean session_two=true;

    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";

    Matrix matrix_pic1_r = new Matrix();
    Matrix matrix_pic2_r = new Matrix();
    Matrix matrix_pic3_r = new Matrix();
    Matrix matrix_pic1_t = new Matrix();
    Matrix matrix_pic2_t = new Matrix();
    Matrix matrix_pic3_t = new Matrix();
    Matrix matrix_pic1_p = new Matrix();
    Matrix matrix_pic2_p = new Matrix();
    Matrix matrix_pic3_p = new Matrix();
    Matrix matrix_pic1_d = new Matrix();
    Matrix matrix_pic2_d = new Matrix();
    Matrix matrix_pic3_d = new Matrix();
    Matrix matrix_pic1_t2 = new Matrix();
    Matrix matrix_pic2_t2= new Matrix();
    Matrix matrix_pic3_t2 = new Matrix();
    Button b1,b2;
    float[] lastEvent = null;
    float d = 0f;
    float newRot = 0f;
    public static String fileNAME;
    public static int framePos = 0;

    private float newDist = 0;
    Matrix matrix = new Matrix();
    Matrix savedMatrix = new Matrix();

    // Fields
    private String TAG = this.getClass().getSimpleName();

    // We can be in one of these 3 states
    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private int mode = NONE;

    // Remember some things for zooming
    private PointF start = new PointF();
    private PointF mid = new PointF();
    float oldDist = 1f;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.concept_area3);
        findview();

        // 開啟SQLite 取得使用者身高
        GetSqlite_person_ghiht();
        GetSqlite_version();
        //confirm_date_but();

        //矩形rectangle  三角形triangle  梯形trapezoidal  平行四邊形parallel  菱形diamond

        r_but01.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                r_image01.setImageResource(R.drawable.r_but011);
                //Toast.makeText(concept_area2.this, "可以移動了!!", Toast.LENGTH_SHORT).show();
                // TODO Auto-generated method stub
                r_image01.setOnTouchListener(concept_area3.this);
                r_image02.setOnTouchListener(null);
                r_image03.setOnTouchListener(null);
                t_image01.setOnTouchListener(null);
                t_image02.setOnTouchListener(null);
                t_image03.setOnTouchListener(null);
                p_image01.setOnTouchListener(null);
                p_image02.setOnTouchListener(null);
                p_image03.setOnTouchListener(null);
                d_image01.setOnTouchListener(null);
                d_image02.setOnTouchListener(null);
                d_image03.setOnTouchListener(null);
                t2_image01.setOnTouchListener(null);
                t2_image02.setOnTouchListener(null);
                t2_image03.setOnTouchListener(null);

            }

        });
        r_but02.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                r_image02.setImageResource(R.drawable.r_but022);
                r_image01.setOnTouchListener(null);
                r_image02.setOnTouchListener(concept_area3.this);
                r_image03.setOnTouchListener(null);
                t_image01.setOnTouchListener(null);
                t_image02.setOnTouchListener(null);
                t_image03.setOnTouchListener(null);
                p_image01.setOnTouchListener(null);
                p_image02.setOnTouchListener(null);
                p_image03.setOnTouchListener(null);
                d_image01.setOnTouchListener(null);
                d_image02.setOnTouchListener(null);
                d_image03.setOnTouchListener(null);
                t2_image01.setOnTouchListener(null);
                t2_image02.setOnTouchListener(null);
                t2_image03.setOnTouchListener(null);
            }
        });
        r_but03.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                r_image03.setImageResource(R.drawable.r_but033);
                r_image01.setOnTouchListener(null);
                r_image02.setOnTouchListener(null);
                r_image03.setOnTouchListener(concept_area3.this);
                t_image01.setOnTouchListener(null);
                t_image02.setOnTouchListener(null);
                t_image03.setOnTouchListener(null);
                p_image01.setOnTouchListener(null);
                p_image02.setOnTouchListener(null);
                p_image03.setOnTouchListener(null);
                d_image01.setOnTouchListener(null);
                d_image02.setOnTouchListener(null);
                d_image03.setOnTouchListener(null);
                t2_image01.setOnTouchListener(null);
                t2_image02.setOnTouchListener(null);
                t2_image03.setOnTouchListener(null);
            }

        });


        r_but01.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub

                longbutton_r();
                return false;
            }
        });
        r_but02.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub

                longbutton_r();
                return false;
            }
        });
        r_but03.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub

                longbutton_r();
                return false;
            }
        });

        t_but01.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                t_image01.setImageResource(R.drawable.t_but011);
                //Toast.makeText(concept_area2.this, "可以移動了!!", Toast.LENGTH_SHORT).show();
                // TODO Auto-generated method stub
                r_image01.setOnTouchListener(null);
                r_image02.setOnTouchListener(null);
                r_image03.setOnTouchListener(null);
                t_image01.setOnTouchListener(concept_area3.this);
                t_image02.setOnTouchListener(null);
                t_image03.setOnTouchListener(null);
                p_image01.setOnTouchListener(null);
                p_image02.setOnTouchListener(null);
                p_image03.setOnTouchListener(null);
                d_image01.setOnTouchListener(null);
                d_image02.setOnTouchListener(null);
                d_image03.setOnTouchListener(null);
                t2_image01.setOnTouchListener(null);
                t2_image02.setOnTouchListener(null);
                t2_image03.setOnTouchListener(null);
            }

        });
        t_but02.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                t_image02.setImageResource(R.drawable.t_but022);
                r_image01.setOnTouchListener(null);
                r_image02.setOnTouchListener(null);
                r_image03.setOnTouchListener(null);
                t_image01.setOnTouchListener(null);
                t_image02.setOnTouchListener(concept_area3.this);
                t_image03.setOnTouchListener(null);
                p_image01.setOnTouchListener(null);
                p_image02.setOnTouchListener(null);
                p_image03.setOnTouchListener(null);
                d_image01.setOnTouchListener(null);
                d_image02.setOnTouchListener(null);
                d_image03.setOnTouchListener(null);
                t2_image01.setOnTouchListener(null);
                t2_image02.setOnTouchListener(null);
                t2_image03.setOnTouchListener(null);
            }
        });
        t_but03.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                t_image03.setImageResource(R.drawable.t_but033);
                r_image01.setOnTouchListener(null);
                r_image02.setOnTouchListener(null);
                r_image03.setOnTouchListener(null);
                t_image01.setOnTouchListener(null);
                t_image02.setOnTouchListener(null);
                t_image03.setOnTouchListener(concept_area3.this);
                p_image01.setOnTouchListener(null);
                p_image02.setOnTouchListener(null);
                p_image03.setOnTouchListener(null);
                d_image01.setOnTouchListener(null);
                d_image02.setOnTouchListener(null);
                d_image03.setOnTouchListener(null);
                t2_image01.setOnTouchListener(null);
                t2_image02.setOnTouchListener(null);
                t2_image03.setOnTouchListener(null);
            }

        });
        t_but01.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub

                longbutton_t();
                return false;
            }
        });
        t_but02.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub

                longbutton_t();
                return false;
            }
        });
        t_but03.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub

                longbutton_t();
                return false;
            }
        });

        p_but01.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                p_image01.setImageResource(R.drawable.p_but011);
                //Toast.makeText(concept_area2.this, "可以移動了!!", Toast.LENGTH_SHORT).show();
                // TODO Auto-generated method stub
                r_image01.setOnTouchListener(null);
                r_image02.setOnTouchListener(null);
                r_image03.setOnTouchListener(null);
                t_image01.setOnTouchListener(null);
                t_image02.setOnTouchListener(null);
                t_image03.setOnTouchListener(null);
                p_image01.setOnTouchListener(concept_area3.this);
                p_image02.setOnTouchListener(null);
                p_image03.setOnTouchListener(null);
                d_image01.setOnTouchListener(null);
                d_image02.setOnTouchListener(null);
                d_image03.setOnTouchListener(null);
                t2_image01.setOnTouchListener(null);
                t2_image02.setOnTouchListener(null);
                t2_image03.setOnTouchListener(null);
            }

        });
        p_but02.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                p_image02.setImageResource(R.drawable.p_but022);
                r_image01.setOnTouchListener(null);
                r_image02.setOnTouchListener(null);
                r_image03.setOnTouchListener(null);
                t_image01.setOnTouchListener(null);
                t_image02.setOnTouchListener(null);
                t_image03.setOnTouchListener(null);
                p_image01.setOnTouchListener(null);
                p_image02.setOnTouchListener(concept_area3.this);
                p_image03.setOnTouchListener(null);
                d_image01.setOnTouchListener(null);
                d_image02.setOnTouchListener(null);
                d_image03.setOnTouchListener(null);
                t2_image01.setOnTouchListener(null);
                t2_image02.setOnTouchListener(null);
                t2_image03.setOnTouchListener(null);
            }
        });
        p_but03.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                p_image03.setImageResource(R.drawable.p_but033);
                r_image01.setOnTouchListener(null);
                r_image02.setOnTouchListener(null);
                r_image03.setOnTouchListener(null);
                t_image01.setOnTouchListener(null);
                t_image02.setOnTouchListener(null);
                t_image03.setOnTouchListener(null);
                p_image01.setOnTouchListener(null);
                p_image02.setOnTouchListener(null);
                p_image03.setOnTouchListener(concept_area3.this);
                d_image01.setOnTouchListener(null);
                d_image02.setOnTouchListener(null);
                d_image03.setOnTouchListener(null);
                t2_image01.setOnTouchListener(null);
                t2_image02.setOnTouchListener(null);
                t2_image03.setOnTouchListener(null);
            }

        });
        p_but01.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub

                longbutton_p();
                return false;
            }
        });
        p_but02.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub

                longbutton_p();
                return false;
            }
        });
        p_but03.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub

                longbutton_p();
                return false;
            }
        });
        d_but01.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                d_image01.setImageResource(R.drawable.d_but011);
                //Toast.makeText(concept_area2.this, "可以移動了!!", Toast.LENGTH_SHORT).show();
                // TODO Auto-generated method stub
                r_image01.setOnTouchListener(null);
                r_image02.setOnTouchListener(null);
                r_image03.setOnTouchListener(null);
                t_image01.setOnTouchListener(null);
                t_image02.setOnTouchListener(null);
                t_image03.setOnTouchListener(null);
                p_image01.setOnTouchListener(null);
                p_image02.setOnTouchListener(null);
                p_image03.setOnTouchListener(null);
                d_image01.setOnTouchListener(concept_area3.this);
                d_image02.setOnTouchListener(null);
                d_image03.setOnTouchListener(null);
                t2_image01.setOnTouchListener(null);
                t2_image02.setOnTouchListener(null);
                t2_image03.setOnTouchListener(null);
            }

        });
        d_but02.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                d_image02.setImageResource(R.drawable.d_but022);
                r_image01.setOnTouchListener(null);
                r_image02.setOnTouchListener(null);
                r_image03.setOnTouchListener(null);
                t_image01.setOnTouchListener(null);
                t_image02.setOnTouchListener(null);
                t_image03.setOnTouchListener(null);
                p_image01.setOnTouchListener(null);
                p_image02.setOnTouchListener(null);
                p_image03.setOnTouchListener(null);
                d_image01.setOnTouchListener(null);
                d_image02.setOnTouchListener(concept_area3.this);
                d_image03.setOnTouchListener(null);
                t2_image01.setOnTouchListener(null);
                t2_image02.setOnTouchListener(null);
                t2_image03.setOnTouchListener(null);
            }
        });
        d_but03.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                d_image03.setImageResource(R.drawable.d_but033);
                r_image01.setOnTouchListener(null);
                r_image02.setOnTouchListener(null);
                r_image03.setOnTouchListener(null);
                t_image01.setOnTouchListener(null);
                t_image02.setOnTouchListener(null);
                t_image03.setOnTouchListener(null);
                p_image01.setOnTouchListener(null);
                p_image02.setOnTouchListener(null);
                p_image03.setOnTouchListener(null);
                d_image01.setOnTouchListener(null);
                d_image02.setOnTouchListener(null);
                d_image03.setOnTouchListener(concept_area3.this);
                t2_image01.setOnTouchListener(null);
                t2_image02.setOnTouchListener(null);
                t2_image03.setOnTouchListener(null);
            }

        });
        d_but01.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub

                longbutton_d();
                return false;
            }
        });
        d_but02.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub

                longbutton_d();
                return false;
            }
        });
        d_but03.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub

                longbutton_d();
                return false;
            }
        });
        t2_but01.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                t2_image01.setImageResource(R.drawable.t2_but011);
                //Toast.makeText(concept_area2.this, "可以移動了!!", Toast.LENGTH_SHORT).show();
                // TODO Auto-generated method stub
                r_image01.setOnTouchListener(null);
                r_image02.setOnTouchListener(null);
                r_image03.setOnTouchListener(null);
                t_image01.setOnTouchListener(null);
                t_image02.setOnTouchListener(null);
                t_image03.setOnTouchListener(null);
                p_image01.setOnTouchListener(null);
                p_image02.setOnTouchListener(null);
                p_image03.setOnTouchListener(null);
                d_image01.setOnTouchListener(null);
                d_image02.setOnTouchListener(null);
                d_image03.setOnTouchListener(null);
                t2_image01.setOnTouchListener(concept_area3.this);
                t2_image02.setOnTouchListener(null);
                t2_image03.setOnTouchListener(null);
            }

        });
        t2_but02.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                t2_image02.setImageResource(R.drawable.t2_but022);
                r_image01.setOnTouchListener(null);
                r_image02.setOnTouchListener(null);
                r_image03.setOnTouchListener(null);
                t_image01.setOnTouchListener(null);
                t_image02.setOnTouchListener(null);
                t_image03.setOnTouchListener(null);
                p_image01.setOnTouchListener(null);
                p_image02.setOnTouchListener(null);
                p_image03.setOnTouchListener(null);
                d_image01.setOnTouchListener(null);
                d_image02.setOnTouchListener(null);
                d_image03.setOnTouchListener(null);
                t2_image01.setOnTouchListener(null);
                t2_image02.setOnTouchListener(concept_area3.this);
                t2_image03.setOnTouchListener(null);
            }
        });
        t2_but03.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                t2_image03.setImageResource(R.drawable.t2_but033);
                r_image01.setOnTouchListener(null);
                r_image02.setOnTouchListener(null);
                r_image03.setOnTouchListener(null);
                t_image01.setOnTouchListener(null);
                t_image02.setOnTouchListener(null);
                t_image03.setOnTouchListener(null);
                p_image01.setOnTouchListener(null);
                p_image02.setOnTouchListener(null);
                p_image03.setOnTouchListener(null);
                d_image01.setOnTouchListener(null);
                d_image02.setOnTouchListener(null);
                d_image03.setOnTouchListener(null);
                t2_image01.setOnTouchListener(null);
                t2_image02.setOnTouchListener(null);
                t2_image03.setOnTouchListener(concept_area3.this);
            }

        });
        t2_but01.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub

                longbutton_t2();
                return false;
            }
        });
        t2_but02.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub

                longbutton_t2();
                return false;
            }
        });
        t2_but03.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub

                longbutton_t2();
                return false;
            }
        });

        concept_total.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent();
                intent.setClass(concept_area3.this, user_practice_p_mtotal.class);
                startActivity(intent);
                finish();

            }
        });



        sdf = new SimpleDateFormat("HHmmss");// 日期
        tempsdf = sdf.format(new Date());
        process_store_name = measure_session4p_makeup.temp_pic_storename
                + tempsdf;

        bitmap = BitmapFactory.decodeFile("/sdcard/file_data/"
                + measure_session4p_makeup.temp_pic_storename);
        object_concept1.setImageBitmap(bitmap);
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        ImageView view = (ImageView) v;
        view.setScaleType(ImageView.ScaleType.MATRIX);
        float scale;
        // Dump touch event to log
        dumpEvent(event);
        if (view.getId() == R.id.r_image01) {
            matrix = matrix_pic1_r;
        } else if (view.getId() == R.id.r_image02) {
            matrix = matrix_pic2_r;
        } else if (view.getId() == R.id.r_image03) {
            matrix = matrix_pic3_r;
        } else if (view.getId() == R.id.t_image01) {
            matrix = matrix_pic1_t;
        } else if (view.getId() == R.id.t_image02) {
            matrix = matrix_pic2_t;
        } else if (view.getId() == R.id.t_image03) {
            matrix = matrix_pic3_t;
        } else if (view.getId() == R.id.p_image01) {
            matrix = matrix_pic1_p;
        } else if (view.getId() == R.id.p_image02) {
            matrix = matrix_pic2_p;
        } else if (view.getId() == R.id.p_image03) {
            matrix = matrix_pic3_p;
        } else if (view.getId() == R.id.d_image01) {
            matrix = matrix_pic1_d;
        } else if (view.getId() == R.id.d_image02) {
            matrix = matrix_pic2_d;
        } else if (view.getId() == R.id.d_image03) {
            matrix = matrix_pic3_d;
        } else if (view.getId() == R.id.t2_image01) {
            matrix = matrix_pic1_t2;
        } else if (view.getId() == R.id.t2_image02) {
            matrix = matrix_pic2_t2;
        } else if (view.getId() == R.id.t2_image03) {
            matrix = matrix_pic3_t2;
        }

        // Handle touch events here...
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: //first finger down only
                savedMatrix.set(matrix);
                start.set(event.getX(), event.getY());
                Log.d(TAG, "mode=DRAG"); // write to LogCat
                mode = DRAG;
                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                oldDist = spacing(event);
                if (oldDist > 10f) {
                    midPoint(mid, event);
                    mode = ZOOM;
                }

                lastEvent = new float[4];
                lastEvent[0] = event.getX(0);
                lastEvent[1] = event.getX(1);
                lastEvent[2] = event.getY(0);
                lastEvent[3] = event.getY(1);
                d = rotation(event);
                break;

            case MotionEvent.ACTION_UP: //first finger lifted
            case MotionEvent.ACTION_POINTER_UP: //second finger lifted
                mode = NONE;
                if (view.getId() == R.id.r_image01) {
                    matrix_pic1_r = matrix;
                } else if (view.getId() == R.id.r_image02) {
                    matrix_pic2_r = matrix;
                } else if (view.getId() == R.id.r_image03) {
                    matrix_pic3_r = matrix;
                } else if (view.getId() == R.id.t_image01) {
                    matrix_pic1_t = matrix;
                } else if (view.getId() == R.id.t_image02) {
                    matrix_pic2_t = matrix;
                } else if (view.getId() == R.id.t_image03) {
                    matrix_pic3_t = matrix;
                } else if (view.getId() == R.id.p_image01) {
                    matrix_pic1_p = matrix;
                } else if (view.getId() == R.id.p_image02) {
                    matrix_pic2_p = matrix;
                } else if (view.getId() == R.id.p_image03) {
                    matrix_pic3_p = matrix;
                } else if (view.getId() == R.id.d_image01) {
                    matrix_pic1_d = matrix;
                } else if (view.getId() == R.id.d_image02) {
                    matrix_pic2_d = matrix;
                } else if (view.getId() == R.id.d_image03) {
                    matrix_pic3_d = matrix;
                } else if (view.getId() == R.id.t2_image01) {
                    matrix_pic1_t2 = matrix;
                } else if (view.getId() == R.id.t2_image02) {
                    matrix_pic2_t2 = matrix;
                } else if (view.getId() == R.id.t2_image03) {
                    matrix_pic3_t2 = matrix;
                }
                Log.d(TAG, "mode=NONE" );
                break;


            case MotionEvent.ACTION_MOVE:

                if (mode == DRAG) {
                    // ...
                    matrix.set(savedMatrix);
                    float dx = event.getX() - start.x;
                    float dy = event.getY() - start.y;
                    matrix.postTranslate(dx, dy);
                } else if (mode == ZOOM && event.getPointerCount() == 2) {
                    float newDist = spacing(event);

                    if (newDist > 10f) {
                        matrix.set(savedMatrix);
                        scale = (newDist / oldDist);
                        matrix.postScale(scale, scale, mid.x, mid.y);
                    }
                    if (lastEvent != null) {
                        newRot = rotation(event);
                        float r = newRot - d;
                        matrix.postRotate(r, view.getMeasuredWidth() / 2,
                                view.getMeasuredHeight() / 2);
                    }
                }

                break;

        }
        // Perform the transformation
        view.setImageMatrix(matrix);

        return true; // indicate event was handled

    }
    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);

        return (float) Math.toDegrees(radians);
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float)Math.sqrt(x * x + y * y);

    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x/2, y/2);

    }


    /** Show an event in the LogCat view, for debugging */

    private void dumpEvent(MotionEvent event) {
        String names[] = { "DOWN" , "UP" , "MOVE" , "CANCEL" , "OUTSIDE" ,
                "POINTER_DOWN" , "POINTER_UP" , "7?" , "8?" , "9?" };
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_" ).append(names[actionCode]);
        if (actionCode == MotionEvent.ACTION_POINTER_DOWN
                || actionCode == MotionEvent.ACTION_POINTER_UP) {
            sb.append("(pid " ).append(
                    action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")" );
        }

        sb.append("[" );

        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append("#" ).append(i);
            sb.append("(pid " ).append(event.getPointerId(i));
            sb.append(")=" ).append((int) event.getX(i));
            sb.append("," ).append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())

                sb.append(";" );
        }

        sb.append("]" );
        Log.d(TAG, sb.toString());
    }
    private void longbutton_r() {
        // TODO Auto-generated method stub
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                open_all_but();
            }
        }, 1000);
        session_one = false;

        // dbHelper = new SQLite(distance_MainActivity.this,
        // "record_action");
        // dbHelper.Insert_Action(login.MainActivity_page,"To_session2_rectangle");
        // dbHelper.close();//關閉資料庫

        switch_practice_homework = "practice";
        measure_shap = "rectangle";
        measure_shap2 = "rectangle_m";
        show_select_measure_type();
    }
    private void longbutton_t() {
        // TODO Auto-generated method stub
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                open_all_but();
            }
        }, 1000);
        session_one = false;

        // dbHelper = new SQLite(distance_MainActivity.this,
        // "record_action");
        // dbHelper.Insert_Action(login.MainActivity_page,"To_session2_rectangle");
        // dbHelper.close();//關閉資料庫

        switch_practice_homework = "practice";
        measure_shap = "triangle";
        measure_shap2 = "triangle_m";
        Intent intent = new Intent();
        intent.setClass(concept_area3.this, measure_session_t.class);
        startActivity(intent);
    }
    private void longbutton_p() {
        // TODO Auto-generated method stub
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                open_all_but();
            }
        }, 1000);
        session_one = false;

        // dbHelper = new SQLite(distance_MainActivity.this,
        // "record_action");
        // dbHelper.Insert_Action(login.MainActivity_page,"To_session2_rectangle");
        // dbHelper.close();//關閉資料庫

        switch_practice_homework = "practice";
        measure_shap = "parallel";
        measure_shap2 = "parallel_m";
        show_select_measure_type();
    }
    private void longbutton_d() {
        // TODO Auto-generated method stub
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                open_all_but();
            }
        }, 1000);
        session_one = false;

        // dbHelper = new SQLite(distance_MainActivity.this,
        // "record_action");
        // dbHelper.Insert_Action(login.MainActivity_page,"To_session2_rectangle");
        // dbHelper.close();//關閉資料庫

        switch_practice_homework = "practice";
        measure_shap = "diamond";
        measure_shap2 = "diamond_m";
        show_select_measure_type();
    }
    private void longbutton_t2() {
        // TODO Auto-generated method stub
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                open_all_but();
            }
        }, 1000);
        session_one = false;

        // dbHelper = new SQLite(distance_MainActivity.this,
        // "record_action");
        // dbHelper.Insert_Action(login.MainActivity_page,"To_session2_rectangle");
        // dbHelper.close();//關閉資料庫

        switch_practice_homework = "practice";
        measure_shap = "trapezoidal";
        measure_shap2 = "trapezoidal_m";
        Intent intent = new Intent();
        intent.setClass(concept_area3.this, measure_session_tr.class);
        startActivity(intent);
    }



    protected void close_all_but() {
        // TODO Auto-generated method stub
        imageView_r1.setVisibility(View.INVISIBLE);
    }

    private void open_all_but() {
        confirm_date_but();
    }

    private void show_select_measure_type() {
        // TODO Auto-generated method stub
        final Intent intent = new Intent();
        new AlertDialog.Builder(concept_area3.this)
                // 選擇顯示圖示
                .setTitle("請選擇物件的所在位置")
                .setItems(
                        new String[] { "目標物平躺於地面  例如:磁磚",
                                "目標物底部與地面接觸，垂直於地面  例如:門", "目標物在牆上 例如:窗戶" },
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichcountry) {
                                switch (whichcountry) {
                                    case 0:
                                        person_select_measure_type = "get_area_ground";
                                        intent.setClass(concept_area3.this,
                                                measure_session2p.class);
                                        startActivity(intent);
                                        break;
                                    case 1:
                                        person_select_measure_type = "get_area_ground_hight";
                                        intent.setClass(concept_area3.this,
                                                measure_session2p.class);
                                        startActivity(intent);
                                        break;
                                    case 2:
                                        person_select_measure_type = "get_area_wall";
                                        intent.setClass(concept_area3.this,
                                                measure_session2p.class);
                                        startActivity(intent);
                                        break;
                                }
                            }
                        }).show();
    }

    private void get_online_data() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tablename", "download_rank_allstu"));
        String TAG_PRODUCTS = "download_content";
        JSONArray d_table = null;
        // getting JSON string from URL
        JSONObject json = jsonParser.makeHttpRequest(Myservice.url_download,
                "GET", params);
        try {
            // Checking for SUCCESS TAG
            int success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // products found
                // Getting Array of Products
                d_table = json.getJSONArray(TAG_PRODUCTS);

                // looping through All Products
                for (int i = 0; i < d_table.length(); i++) {
                    JSONObject c = d_table.getJSONObject(i);
                    // Storing each json item in variable
                    String user_id = c.getString("user_id");
                    String total_num = c.getString("total_num");
                    String right_num = c.getString("right_num");
                    String right_rate = c.getString("right_rate");
                    String Date = c.getString("Date");
                    if (!(user_id.equals(next_one_activity2p.user_id))) {
                        SQLite db = new SQLite(this, "rank_allstu");
                        db.update_all_stu_rank(user_id, total_num, right_num,
                                right_rate, Date);
                        db.close();
                    }
                }
            } else {
                // no products found
                // Launch Add New product Activity
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void confirm_date_but() {
        // TODO Auto-generated method stub
        Integer verion = Integer.valueOf(new_version);
        // version 1>> session_1 on
        // version 2>> session_2 on
        // version 3>> homework and map on (students can't add poi)
        // version 4>> map (students can add poi)
        // version 5>> competition on
        if (verion > 0) {
            // makeup_but.setVisibility(View.VISIBLE);
            // rectangle_concept.setVisibility(View.VISIBLE);
            // session1_but_distance.setVisibility(View.VISIBLE);
            // session1_but_hight.setVisibility(View.VISIBLE);
            // session1_but_width.setVisibility(View.VISIBLE);
            // session1_but_distance2.setVisibility(View.VISIBLE);
            // session1_but_hight2.setVisibility(View.VISIBLE);
            // upgrade_but.setClickable(true);
        }
        if (verion > 1) {
            //imageView_r1.setVisibility(View.VISIBLE);
            // triangle_but.setVisibility(View.VISIBLE);
            // parallel_but.setVisibility(View.VISIBLE);
            // diamond_but.setVisibility(View.VISIBLE);
            // trapezoidal_but.setVisibility(View.VISIBLE);
            // session2_but_see_record.setVisibility(View.VISIBLE);

            // upgrade_but.setClickable(true);
        }
        // if(verion>2)
        // {
        // session3_but_map.setVisibility(View.VISIBLE);
        // session3_but_homework.setVisibility(View.VISIBLE);
        // upgrade_but.setClickable(true);
        // }
        // if(verion>4)
        // {
        // rank_test.setVisibility(View.VISIBLE);
        // upgrade_but.setClickable(true);
        // }
    }

    private boolean isInternetConnected() {

        // get ConnectivityManager instance
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        // get network info
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        // check network is connecting
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }

    }

    // 判斷AlertDialog input 值
    public static boolean validate_input(String s) {
        return ((50 < Integer.valueOf(s) && 201 > Integer.valueOf(s)) || (1 == Integer
                .valueOf(s)));
    }

    private void GetSqlite_person_ghiht() {
        // TODO Auto-generated method stub
        dbHelper = new SQLite(concept_area3.this, "personhight");
        Cursor cursor = dbHelper.getpersonhight("personhight");

        cursor.moveToNext();
        person_hight = cursor.getString(1);
        person_hight_tv.setText("你的身高:  " + person_hight + "(cm)");
        cursor.close();
        dbHelper.close();
    }

    private void GetSqlite_version() {
        // TODO Auto-generated method stub
        dbHelper = new SQLite(concept_area3.this, "user_permission");
        Cursor cursor = dbHelper.getversion("user_permission");
        cursor.moveToNext();
        new_version = cursor.getString(4);
        version.setText("Version: " + new_version + ".1");
        cursor.close();
        dbHelper.close();
    }

    private void get_new_permission() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("tablename",
                "download_stu_permission"));
        String TAG_PRODUCTS = "download_content";
        JSONArray d_table = null;
        // getting JSON string from URL
        JSONObject json = jsonParser.makeHttpRequest(Myservice.url_download,
                "GET", params);
        try {
            // Checking for SUCCESS TAG
            int success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // products found
                // Getting Array of Products
                d_table = json.getJSONArray(TAG_PRODUCTS);

                // looping through All Products
                for (int i = 0; i < d_table.length(); i++) {
                    JSONObject c = d_table.getJSONObject(i);
                    // Storing each json item in variable
                    String permission = c.getString("permission");

                    SQLite db = new SQLite(this, "user_permission");
                    db.update_version(permission);
                    db.close();

                }
            } else {
                // no products found
                // Launch Add New product Activity
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findview() {
        // TODO Auto-generated method stub
        object_concept1 = (ImageView) findViewById(R.id.object_concept1);
        r_image01 = (ImageView) findViewById(R.id.r_image01);
        r_but01=(Button)findViewById(R.id.r_but01);
        r_image02 = (ImageView) findViewById(R.id.r_image02);
        r_but02=(Button)findViewById(R.id.r_but02);
        r_image03 = (ImageView) findViewById(R.id.r_image03);
        r_but03=(Button)findViewById(R.id.r_but03);

        t_image01 = (ImageView) findViewById(R.id.t_image01);
        t_but01=(Button)findViewById(R.id.t_but01);
        t_image02 = (ImageView) findViewById(R.id.t_image02);
        t_but02=(Button)findViewById(R.id.t_but02);
        t_image03 = (ImageView) findViewById(R.id.t_image03);
        t_but03=(Button)findViewById(R.id.t_but03);

        p_image01 = (ImageView) findViewById(R.id.p_image01);
        p_but01=(Button)findViewById(R.id.p_but01);
        p_image02 = (ImageView) findViewById(R.id.p_image02);
        p_but02=(Button)findViewById(R.id.p_but02);
        p_image03 = (ImageView) findViewById(R.id.p_image03);
        p_but03=(Button)findViewById(R.id.p_but03);

        d_image01 = (ImageView) findViewById(R.id.d_image01);
        d_but01=(Button)findViewById(R.id.d_but01);
        d_image02 = (ImageView) findViewById(R.id.d_image02);
        d_but02=(Button)findViewById(R.id.d_but02);
        d_image03 = (ImageView) findViewById(R.id.d_image03);
        d_but03=(Button)findViewById(R.id.d_but03);

        t2_image01 = (ImageView) findViewById(R.id.t2_image01);
        t2_but01=(Button)findViewById(R.id.t2_but01);
        t2_image02 = (ImageView) findViewById(R.id.t2_image02);
        t2_but02=(Button)findViewById(R.id.t2_but02);
        t2_image03 = (ImageView) findViewById(R.id.t2_image03);
        t2_but03=(Button)findViewById(R.id.t2_but03);

        concept_total=(Button)findViewById(R.id.concept_total);
        person_hight_tv = (TextView) findViewById(R.id.person_hight);
        version = (TextView) findViewById(R.id.version3);

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(concept_area3.this)
                    .setTitle("警告")
                    .setMessage("確定要放棄此次練習嗎?")
                    .setPositiveButton("確認",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    if (practice_record.from_practice_record_page) {
										/*
										 * dbHelper = new
										 * SQLite(concept_area1.this,
										 * "record_action");
										 * dbHelper.Insert_Action
										 * (login.practice_page
										 * ,"giveup_practice_To_session2_record"
										 * ); dbHelper.close();//關閉資料庫
										 */
                                        Intent intent = new Intent();
                                        intent.setClass(concept_area3.this,
                                                practice_record.class);
                                        startActivity(intent);
                                        concept_area3.this.finish();
                                    } else {
										/*
										 * dbHelper = new
										 * SQLite(concept_area1.this,
										 * "record_action");
										 * dbHelper.Insert_Action
										 * (login.practice_page
										 * ,"giveup_practice_To_MainActivity");
										 * dbHelper.close();//關閉資料庫
										 */
                                        concept_area3.this.finish();
                                    }
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                }
                            }).show();
            return true;
        }
        return false;
    }

}
