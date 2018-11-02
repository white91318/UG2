package lab.hwang.ug;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class change_chose_t1 extends Activity {
    private SQLite dbHelper;



    Button sure_But1;
    TextView Text1;
    RadioGroup RadioGroup1;
    RadioButton radio_But1, radio_But2, radio_But3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_chose_trapezoidal);
        findview();

		/* RadioGroup.OnCheckedChangeListener listener = new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == radio_But1.getId())
					Text1.setText(R.string.str_guess_error);
				else if (checkedId == radio_But2.getId())
					Text1.setText(R.string.str_guess_correct);
				else
					Text1.setText(R.string.str_guess_error);
			}
		};*/

        sure_But1.setOnClickListener(new Button.OnClickListener(){

            @Override
            public void onClick(View v) {
                int checkedId = RadioGroup1.getCheckedRadioButtonId();
                // TODO Auto-generated method stub

                if (checkedId == radio_But3.getId())
                //Text1.setText(R.string.str_guess_error);
                //else if (checkedId == radio_But2.getId())
                //Text1.setText(R.string.str_guess_correct);
                //else if (checkedId == radio_But3.getId())
                //Text1.setText(R.string.str_guess_error_again);
                //else
                //Text1.setText(R.string.str_guess);

                {
			     				/*
			     				dbHelper = new SQLite(change_chose_t1.this, "record_action");
				  				dbHelper.Insert_Action(login.practice_page,"practice_ok_To_session2_record");
				  				dbHelper.close();//關閉資料庫
				  				*/

                    new AlertDialog.Builder(change_chose_t1.this)
                            .setCancelable(false)
                            .setTitle("選擇是否正確??")
                            .setMessage("選擇正確的答案!!!")
                            .setPositiveButton("確認",new DialogInterface.OnClickListener(){
                                public void onClick(DialogInterface dialog, int which)
                                {



                                    //儲存sqlite
                                    dbHelper = new SQLite(change_chose_t1.this, "practice_recorde_data");
                                    //String user_answer = next_one_activity2.user_id+sdf.format(new Date());
                                    //String object_pic = measure_session2.temp_pic_storename;

                                    //long no = dbHelper.InsertSubmit_Homework_one(next_one_activity2.user_id,next_one_activity2.measure_shap,String.valueOf(double_value1),String.valueOf(operand1),String.valueOf(double_value2),String.valueOf(ans),
                                    //object_pic,process_store_name+"_process.png",user_answer,measure_session2.bundle_hight_value,measure_session2.bundle_width_value);

                                    dbHelper.close();//關閉資料庫

			    	 				/*
			    	        		Intent intent = new Intent();
			    		     		intent.setClass(change_chlose_r.this, practice_record.class);
			    		     		startActivity(intent);
			    		     		*/
                                    change_chose_t1.this.finish();
                                }
                            })
                            .show();

                }else
                {
                    dbHelper = new SQLite(change_chose_t1.this, "record_action");
                    dbHelper.Insert_Action(Login.practice_page,"選擇過程有誤_t1");
                    dbHelper.close();//關閉資料庫
                    show_error_dialog("選擇錯誤在試一次!!加油");
                }
            }

        });


    }
    public void show_error_dialog(String error){
        new AlertDialog.Builder(change_chose_t1.this)
                .setCancelable(false)
                .setTitle("警告")
                .setMessage(error)
                .setPositiveButton("確認",new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                })
                .show();
    }


    private void findview() {
        sure_But1 = (Button) findViewById(R.id.sure5);
        Text1 = (TextView) findViewById(R.id.text1_t2);
        RadioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1_t2);

        radio_But1 = (RadioButton) findViewById(R.id.radioBut1_t2);
        radio_But2 = (RadioButton) findViewById(R.id.radioBut2_t2);
        radio_But3 = (RadioButton) findViewById(R.id.radioBut3_t2);

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {


            new AlertDialog.Builder(change_chose_t1.this)
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
                                                change_chose_t1.this,
                                                "record_session_one_practice");
                                        dbHelper.Insert_session_one_practice("giveup_t1");
                                        dbHelper.close();// 關閉資料庫
                                    }
                                    next_one_activity2.session_one = false;
										/*
										 * dbHelper = new
										 * SQLite(change_chose_t1.this,
										 * "record_action");
										 * dbHelper.Insert_Action
										 * (login.measuer_page
										 * ,"giveup_To_MainActivity");
										 * dbHelper.close();//關閉資料庫
										 */
                                    //release_memory();
                                    change_chose_t1.this.finish();
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

}
