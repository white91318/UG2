package lab.hwang.ug;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class next_activity extends Activity {

    private SQLite dbHelper;

    Button previous_but1,button_test1;

    public static Boolean session_two=true;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next_main);
        findview();
        previous_but1.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                intent.setClass(next_activity.this,distance_MainActivity.class);
                startActivity(intent);

            }
        });


    }

    private void findview() {
        // TODO Auto-generated method stub
        previous_but1=(Button)findViewById(R.id.previous_but);
        //button_test1=(Button)findViewById(R.id.button_test);
    }
}

