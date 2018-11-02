package lab.hwang.ug;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class show_large_image_makeup extends Activity {
    //class
    private SQLite dbHelper;//操作SQLite資料庫

    ImageView show_pic_large_mess;
    Button draw_page_1,draw_page_2,draw_page_3;
    String from,path;
    Bitmap bitmap;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_large_image_makeup);
        findview();
        Bundle bunde = this.getIntent().getExtras();
        from = bunde.getString("from");
        path = bunde.getString("path");
        this.setFinishOnTouchOutside(true);

        if(from.equals("homework"))
        {
            if(path.indexOf("process")<0)//照片不會有draw_page*3
            {
                draw_page_1.setVisibility(View.INVISIBLE);
                draw_page_2.setVisibility(View.INVISIBLE);
                draw_page_3.setVisibility(View.INVISIBLE);
            }
            bitmap = BitmapFactory.decodeFile("/sdcard/file_data/"+path);
            show_pic_large_mess.setImageBitmap(bitmap);
        }else
        {
            draw_page_1.setVisibility(View.INVISIBLE);
            draw_page_2.setVisibility(View.INVISIBLE);
            draw_page_3.setVisibility(View.INVISIBLE);
            int img_id = getResources().getIdentifier(  //設定要顯示的圖片
                    path+"_ca_hint_large",
                    "drawable", getPackageName());
            show_pic_large_mess.setImageResource(img_id);
        }
        draw_page_1.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
	     		/*
	     		dbHelper = new SQLite(show_large_image.this, "record_action");
  				dbHelper.Insert_Action(login.show_large_img_page,"click_large_img_1");
  				dbHelper.close();//關閉資料庫
  				*/
                click_draw_page_change_color(1);
                bitmap = BitmapFactory.decodeFile("/sdcard/file_data/"+path);
                show_pic_large_mess.setImageBitmap(bitmap);
            }
        });
        draw_page_2.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
	     		/*
	     		dbHelper = new SQLite(show_large_image.this, "record_action");
  				dbHelper.Insert_Action(login.show_large_img_page,"click_large_img_2");
  				dbHelper.close();//關閉資料庫
  				*/
                click_draw_page_change_color(2);
                String tempbitmap_path = path.replace("_process.png","_process2.png");
                bitmap = BitmapFactory.decodeFile("/sdcard/file_data/"+tempbitmap_path);
                show_pic_large_mess.setImageBitmap(bitmap);
            }
        });
        draw_page_3.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
	     		/*
	     		dbHelper = new SQLite(show_large_image.this, "record_action");
  				dbHelper.Insert_Action(login.show_large_img_page,"click_large_img_3");
  				dbHelper.close();//關閉資料庫
  				*/
                click_draw_page_change_color(3);
                String tempbitmap_path = path.replace("_process.png","_process3.png");
                bitmap = BitmapFactory.decodeFile("/sdcard/file_data/"+tempbitmap_path);
                show_pic_large_mess.setImageBitmap(bitmap);
            }
        });
    }
    private void findview()
    {
        show_pic_large_mess=(ImageView)findViewById(R.id.show_pic_large_mess1);
        draw_page_1=(Button)findViewById(R.id.draw_page_11);
        draw_page_2=(Button)findViewById(R.id.draw_page_21);
        draw_page_3=(Button)findViewById(R.id.draw_page_31);
    }
    //click draw 改變顏色
    public void click_draw_page_change_color(int i)
    {
        if(i==1)
        {
            draw_page_1.setBackgroundColor(0xffffff00);
            draw_page_2.setBackgroundColor(0xffdddddd);
            draw_page_3.setBackgroundColor(0xffdddddd);
        }else if(i==2)
        {
            draw_page_1.setBackgroundColor(0xffdddddd);
            draw_page_2.setBackgroundColor(0xffffff00);
            draw_page_3.setBackgroundColor(0xffdddddd);
        }else if(i==3)
        {
            draw_page_1.setBackgroundColor(0xffdddddd);
            draw_page_2.setBackgroundColor(0xffdddddd);
            draw_page_3.setBackgroundColor(0xffffff00);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("@@@@@@@@", "DES");
        try{
            if(bitmap.isRecycled()==false)
                bitmap.recycle();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        System.gc();
    }
}

