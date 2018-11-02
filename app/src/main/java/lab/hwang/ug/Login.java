package lab.hwang.ug;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;

import static android.content.ContentValues.TAG;

//import static android.content.ContentValues.TAG;


public class Login extends Activity {


    private DatabaseReference mDatabase;

    public String[] log_Username={"stu01","stu02","stu03","stu04","stu05","stu06","stu07","stu08","stu09","stu10",
            "stu11","stu12","stu13","stu14","stu15","stu16","stu17","stu18","stu19","stu10",
            "stu21","stu22","stu23","stu24","stu25","stu26","stu27","stu28","stu29","stu20",
            "stu31","stu32","stu33","stu34","stu35","stu36","stu37","stu38","stu39","stu30",
            "stu41","stu42","stu43","stu44","stu45","stu46","stu47","stu48","stu49","admin","teacher"};
    public String[] log_Password = {"stu01","stu02","stu03","stu04","stu05","stu06","stu07","stu08","stu09","stu10",
            "stu11","stu12","stu13","stu14","stu15","stu16","stu17","stu18","stu19","stu10",
            "stu21","stu22","stu23","stu24","stu25","stu26","stu27","stu28","stu29","stu20",
            "stu31","stu32","stu33","stu34","stu35","stu36","stu37","stu38","stu39","stu30",
            "stu41","stu42","stu43","stu44","stu45","stu46","stu47","stu48","stu49","admin","teacher"};


    public String[] email_list={"stu01@gmail.com","stu02@gmail.com","stu03@gmail.com","stu04@gmail.com","stu05@gmail.com","stu06@gmail.com","stu07@gmail.com","stu08@gmail.com","stu0@gmail.com9","stu10@gmail.com",
            "stu11@gmail.com","stu12@gmail.com","stu13@gmail.com","stu14@gmail.com","stu15@gmail.com","stu16@gmail.com","stu17@gmail.com","stu18@gmail.com","stu19@gmail.com","stu10@gmail.com",
            "stu21@gmail.com","stu22@gmail.com","stu23@gmail.com","stu24@gmail.com","stu25@gmail.com","stu26@gmail.com","stu27@gmail.com","stu28@gmail.com","stu29@gmail.com","stu20@gmail.com",
            "stu31@gmail.com","stu32@gmail.com","stu33@gmail.com","stu34@gmail.com","stu35@gmail.com","stu36@gmail.com","stu37@gmail.com","stu3@gmail.com8","stu39@gmail.com","stu30@gmail.com",
            "stu41@gmail.com","stu42@gmail.com","stu43@gmail.com","stu44@gmail.com","stu45@gmail.com","stu46@gmail.com","stu47@gmail.com","stu48@gmail.com","stu49@gmail.com","admin@gmail.com","teacher@gmail.com"};
    public String email;
    public String password="kelly509";

    public static int stu_num=30;
    public static String UserName;
    public EditText user_id_EditText,user_password_EditText;
    private Button login_but;
    Boolean login_flag;
    //private SQLite dbHelper;//操作SQLite資料庫
    public static String user;

    //記錄所有頁面與動作變數
    public static String login_page="login";
    public static String MainActivity_page="MainActivity";
    public static String measuer_page="measure";
    public static String practice_page="pracetice";
    public static String practice_record_page="pracetice_record";
    public static String homework_page="homework";
    public static String homewrok_submit_page="homewrok_submit";
    public static String comment_others_page="comment_others";
    public static String see_others_comments_page="see_others_comments";
    public static String rank_page="rank";
    public static String rank_record_person_page="rank_record_person";
    public static String rank_record_all_page="rank_record_all";
    public static String rank_record_list_page="rank_record_list";
    public static String rank_record_detail_page="rank_record_detail";
    public static String show_large_img_page="show_large_img";

    // Progress Dialog 等待畫面
    JSONParser jsonParser = new JSONParser();
    private static final String TAG_SUCCESS = "success";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        findview();
        checkdatafile();
       mAuth = FirebaseAuth.getInstance();
//        for (int i=0; i<email_list.length; i++){
//            email = email_list[i];
//            mAuth.createUserWithEmailAndPassword(email, password)
//                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                        @Override
//                        public void onComplete(@NonNull Task<AuthResult> task) {
//                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
//
//                            // If sign in fails, display a message to the user. If sign in succeeds
//                            // the auth state listener will be notified and logic to handle the
//                            // signed in user can be handled in the listener.
//                            if (!task.isSuccessful()) {
//                                Toast.makeText(Login.this, "Authentication failed.",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//
//                            // ...
//                        }
//                    });
//
//        }

        login_but.setOnClickListener(new Button.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                user= user_id_EditText.getText().toString();
                String pass= user_password_EditText.getText().toString();

                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(user)){

                        }else{
                            mDatabase.child(user).setValue(user);

                        }
                    }@Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


                if (user.length() > 0 && pass.length() > 0){
                    for (int i=0;i<log_Username.length;i++){
                        if (user.equals(log_Username[i]) == true && pass.equals(log_Password[i]) == true){
                            distance_MainActivity.user_id=user;
                            if(!(distance_MainActivity.user_id==null)||!(distance_MainActivity.user_id.equals("")))
                            {
                                login_flag = true;
                                email = email_list[i];
                            }
                            break;
                        }else{
                            login_flag = false;
                        }
                    }

                    if (login_flag == true){

                        final ProgressDialog PDialog = ProgressDialog.show(Login.this,"登入中", "請稍後....", true); //等待畫面
                        new Thread(){
                            public void run(){
                                try{
                                    sleep(1000);
                                    Intent intent = new Intent(Login.this, distance_MainActivity.class);
                                    startActivity(intent);
                                    Login.this.finish();
                                }
                                catch(Exception e){
                                    e.printStackTrace();
                                }
                                finally{

                                    signIn();

                                    PDialog.dismiss();
                                }






                            }
                        }.start();

                    }else{
                        Toast.makeText(getApplicationContext(), "輸入錯誤",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "請勿空白！",Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void signIn(){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

    }
    private void findview() {
        login_but=(Button)findViewById(R.id.login_but);
        user_id_EditText=(EditText)findViewById(R.id.user_id);
        user_password_EditText=(EditText)findViewById(R.id.user_password);

    }

    private void checkdatafile(){
        File direct = new File(Environment.getExternalStorageDirectory() + "/file_data");
        if(!direct.exists())
        {
            direct.mkdir();
        }
        File direct2 = new File(Environment.getExternalStorageDirectory() + "/file_data/audio");
        if(!direct2.exists())
        {
            direct2.mkdir();
        }

    }

    private boolean isInternetConnected(){

        //get ConnectivityManager instance
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        //get network info
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        //check network is connecting
        if(netInfo!=null&&netInfo.isConnected()){
            return true;
        }else{
            return false;
        }

    }
}
