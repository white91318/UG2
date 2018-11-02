package lab.hwang.ug;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class show_rules extends Activity {
    ImageView show_pic_large_mess;
    Bitmap bitmap;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_rules);
        findview();
        Bundle bunde = this.getIntent().getExtras();
        String from = bunde.getString("from");
        String path = bunde.getString("path");

        if(from.equals("homework"))
        {
            bitmap = BitmapFactory.decodeFile("/sdcard/file_data/"+path);
            show_pic_large_mess.setImageBitmap(bitmap);
        }else
        {
            int img_id = getResources().getIdentifier(  //設定要顯示的圖片
                    path+"_ca_hint_large",
                    "drawable", getPackageName());
            show_pic_large_mess.setImageResource(img_id);
        }

    }
    private void findview()
    {
        show_pic_large_mess=(ImageView)findViewById(R.id.show_pic_large_mess);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
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

