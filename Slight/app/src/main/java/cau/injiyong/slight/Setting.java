package cau.injiyong.slight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.io.UnsupportedEncodingException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Set;
//import java.lang.reflect.Method;
//import java.util.UUID;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothSocket;
//import android.os.Build;
//import android.os.Handler;
//import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

import yuku.ambilwarna.AmbilWarnaDialog;//new colorpicker



public class Setting extends AppCompatActivity {

    int tColor; ////*****

    public static Activity activity;

    private GoogleSignInClient mGoogleSignInClient;
    Button button4;
    int color;
    int ID;
    ImageView iv;
    Button btnChange[]=new Button[6];

    RadioGroup[] rg=new RadioGroup[6];
    Integer[] rb1=new Integer[6];
    Integer[] rb3=new Integer[6];
    ImageView[] tempiv=new ImageView[6];
    String[] pick=new String[6];
    String[] emotion=new String[6];

    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    String userID;

    String[] btnID=new String[6];

    String colororange=Integer.toString(Color.argb(255,255,60,3));
    String colorpurple=Integer.toString(Color.argb(255,199,53,252));
    String colorblue=Integer.toString(Color.argb(255,53,166,252));
    String colorpink=Integer.toString(Color.argb(255,255,51,110));
    String colorgreen=Integer.toString(Color.argb(255,102,204,0));
    String coloryellow=Integer.toString(Color.argb(255,255,130,0));

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final Paint mCenterPaint; ///***
        mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG); ///***
        tColor = mCenterPaint.getColor();///**

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getUid();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("userInfo");

        Button logout=(Button)findViewById(R.id.custom_logout) ;

        tempiv[0] = (ImageView) findViewById(R.id.imageView);
        tempiv[1] = (ImageView) findViewById(R.id.imageView2);
        tempiv[2] = (ImageView) findViewById(R.id.imageView3);
        tempiv[3] = (ImageView) findViewById(R.id.imageView4);
        tempiv[4] = (ImageView) findViewById(R.id.imageView5);
        tempiv[5] = (ImageView) findViewById(R.id.imageView6);

        btnChange[0] = (Button) findViewById(R.id.rbAnger3);
        btnChange[1] = (Button) findViewById(R.id.rbDisgust3);
        btnChange[2] = (Button) findViewById(R.id.rbFear3);
        btnChange[3] = (Button) findViewById(R.id.rbJoy3);
        btnChange[4] = (Button) findViewById(R.id.rbRestless3);
        btnChange[5] = (Button) findViewById(R.id.rbSad3);
        for(int i=0;i<6;i++) {
            btnChange[i].setOnClickListener(new View.OnClickListener() { //*****
                @Override
                public void onClick(View v) {

                    openColorPicker(v);
                }
            });
        }

        emotion[0]="joy";
        emotion[1]="sad";
        emotion[2]="anger";
        emotion[3]="fear";
        emotion[4]="disgust";
        emotion[5]="restless";

        rg[0] =  (RadioGroup) findViewById(R.id.rgJoy);
        rg[1] = (RadioGroup) findViewById(R.id.rgSad);
        rg[2] = (RadioGroup) findViewById(R.id.rgAnger);
        rg[3] = (RadioGroup) findViewById(R.id.rgFear);
        rg[4] = (RadioGroup) findViewById(R.id.rgDisgust);
        rg[5] = (RadioGroup) findViewById(R.id.rgRestless);

        rb1[0] = R.id.rbJoy1;
        rb1[1] = R.id.rbSad1;
        rb1[2] = R.id.rbAnger1;
        rb1[3] = R.id.rbFear1;
        rb1[4] = R.id.rbDisgust1;
        rb1[5] = R.id.rbRestless1;

        rb3[0] = R.id.rbJoy3;
        rb3[1] = R.id.rbSad3;
        rb3[2] = R.id.rbAnger3;
        rb3[3] = R.id.rbFear3;
        rb3[4] = R.id.rbDisgust3;
        rb3[5] = R.id.rbRestless3;

        myRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int temp;

                for(int i=0;i<6;i++) {
                    btnID[i] = String.valueOf(dataSnapshot.child("button").child(emotion[i]).getValue());
                    if (btnID[i].equals("null")) {
                        rg[i].check(rb1[i]);
                        setDefaultColor();

                    }
                    else {
                        temp = Integer.parseInt(btnID[i]);
                        if(temp==rb3[i]){
                            String s = String.valueOf(dataSnapshot.child("color").child(emotion[i]).getValue());
                            if(s.equals("null")){
                                bk(0, i);
                            }
                            else{
                                bk(Integer.parseInt(s), i);
                            }
                        }
                        rg[i].check(temp);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        button4=(Button) findViewById(R.id.exit);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                complete();
            }
        });
        logout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.v("알림", "구글 LOGOUT");
                AlertDialog.Builder alert = new AlertDialog.Builder(Setting.this);
                alert.setMessage("로그아웃 하시겠습니까?");
                alert.setCancelable(false);
                alert.setPositiveButton("네",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mAuth.signOut();
                                mGoogleSignInClient.signOut();
                                Toast.makeText(getApplicationContext(),"정상적으로 로그아웃 되었습니다.",Toast.LENGTH_SHORT).show();
                                MainActivity.logoutoff();
                                MainActivity MA = (MainActivity) MainActivity.activity;
                                MA.finish();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("아니오",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                alert.show();
            }
        });

    }

    public void bk(int color, int i){
        pick[i] = Integer.toString(color);
        Drawable roundDrawable = getResources().getDrawable(R.drawable.shape);
        roundDrawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        tempiv[i].setBackground(roundDrawable);
    }

    public void OnClickHandlerPurple(View view) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("보라").setMessage("신중함을 요하는 상황이나 창의적인 아이디어가 필요할 때, 혹은 터닝포인트가 필요할 때 도움을 줍니다.");
        AlertDialog alertDialog = builder1.create();
        alertDialog.show();
//        if(mThreadConnectedBluetooth!=null){
//            mThreadConnectedBluetooth.write("1");
//        }
    }

    public void OnClickHandlerPink(View view) {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setTitle("분홍").setMessage("우울하거나 혼란스러울 때 마음에 안정을 주며, 감정을 추스리는데 도움을 줍니다.");
        AlertDialog alertDialog = builder2.create();
        alertDialog.show();
    }

    public void OnClickHandlerBlue(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("파랑").setMessage("긴장하거나 신경이 예민해졌을 때 마음을 진정시켜줍니다.");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void OnClickHandlerGreen(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("초록").setMessage("마음이 불안정하며 긴장될때나 마음의 상처를 받았을 때 도움을 줍니다.");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void OnClickHandlerOrange(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("주황").setMessage("자신감을 높여주는 효과가 있어 적극적인 마인드를 일깨워주고 머리가 복잡하거나 침체기의 상황에서 활기를 되찾아줍니다.");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void OnClickHandlerYellow(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("노랑").setMessage("의욕이 저하됐을 때 도움을 줍니다. 결단력을 일꺠워주기도합니다.");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void complete(){
        String userID = mAuth.getUid();

        int btnId=rg[0].getCheckedRadioButtonId();


        switch(btnId){

            case R.id.rbJoy1://보라
                myRef.child(userID).child("color").child("joy").setValue(colorpurple);
                myRef.child(userID).child("button").child("joy").setValue(R.id.rbJoy1);
                break;
            case R.id.rbJoy2://파랑
                myRef.child(userID).child("color").child("joy").setValue(colorblue);
                myRef.child(userID).child("button").child("joy").setValue(R.id.rbJoy2);
                break;
            case R.id.rbJoy3:
                myRef.child(userID).child("color").child("joy").setValue(pick[0]);
                myRef.child(userID).child("button").child("joy").setValue(R.id.rbJoy3);
                break;

        }

        btnId=rg[1].getCheckedRadioButtonId();
        switch(btnId){

            case R.id.rbSad1://분홍
                myRef.child(userID).child("color").child("sad").setValue(colorpink);
                myRef.child(userID).child("button").child("sad").setValue(R.id.rbSad1);
                break;
            case R.id.rbSad2://주황
                myRef.child(userID).child("color").child("sad").setValue(colororange);
                myRef.child(userID).child("button").child("sad").setValue(R.id.rbSad2);
                break;
            case R.id.rbSad3:
                myRef.child(userID).child("color").child("sad").setValue(pick[1]);
                myRef.child(userID).child("button").child("sad").setValue(R.id.rbSad3);
                break;

        }

        btnId=rg[2].getCheckedRadioButtonId();
        switch(btnId){

            case R.id.rbAnger1://초록
                myRef.child(userID).child("color").child("anger").setValue(colorgreen);
                myRef.child(userID).child("button").child("anger").setValue(R.id.rbAnger1);
                break;
            case R.id.rbAnger2://파랑
                myRef.child(userID).child("color").child("anger").setValue(colorblue);
                myRef.child(userID).child("button").child("anger").setValue(R.id.rbAnger2);
                break;
            case R.id.rbAnger3:
                myRef.child(userID).child("color").child("anger").setValue(pick[2]);
                myRef.child(userID).child("button").child("anger").setValue(R.id.rbAnger3);
                break;

        }
        btnId=rg[3].getCheckedRadioButtonId();
        switch(btnId){

            case R.id.rbFear1://주황
                myRef.child(userID).child("color").child("fear").setValue(colororange);
                myRef.child(userID).child("button").child("fear").setValue(R.id.rbFear1);
                break;
            case R.id.rbFear2://초록
                myRef.child(userID).child("color").child("fear").setValue(colorgreen);
                myRef.child(userID).child("button").child("fear").setValue(R.id.rbFear2);
                break;
            case R.id.rbFear3:
                myRef.child(userID).child("color").child("fear").setValue(pick[3]);
                myRef.child(userID).child("button").child("fear").setValue(R.id.rbFear3);
                break;

        }
        btnId=rg[4].getCheckedRadioButtonId();
        switch(btnId){

            case R.id.rbDisgust1://파랑
                myRef.child(userID).child("color").child("disgust").setValue(colorblue);
                myRef.child(userID).child("button").child("disgust").setValue(R.id.rbDisgust1);
                break;
            case R.id.rbDisgust2://보라
                myRef.child(userID).child("color").child("disgust").setValue(colorpurple);
                myRef.child(userID).child("button").child("disgust").setValue(R.id.rbDisgust2);
                break;
            case R.id.rbDisgust3:
                myRef.child(userID).child("color").child("disgust").setValue(pick[4]);
                myRef.child(userID).child("button").child("disgust").setValue(R.id.rbDisgust3);
                break;

        }
        btnId=rg[5].getCheckedRadioButtonId();
        switch(btnId){

            case R.id.rbRestless1://노랑
                myRef.child(userID).child("color").child("restless").setValue(coloryellow);
                myRef.child(userID).child("button").child("restless").setValue(R.id.rbRestless1);
                break;
            case R.id.rbRestless2://주황
                myRef.child(userID).child("color").child("restless").setValue(colororange);
                myRef.child(userID).child("button").child("restless").setValue(R.id.rbRestless2);
                break;
            case R.id.rbRestless3:
                myRef.child(userID).child("color").child("restless").setValue(pick[5]);
                myRef.child(userID).child("button").child("restless").setValue(R.id.rbRestless3);
                break;

        }

        myRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                MainActivity.joyColor = String.valueOf(dataSnapshot.child("color").child("joy").getValue());
                MainActivity.sadnessColor = String.valueOf(dataSnapshot.child("color").child("sad").getValue());
                MainActivity.angerColor = String.valueOf(dataSnapshot.child("color").child("anger").getValue());
                MainActivity.fearColor = String.valueOf(dataSnapshot.child("color").child("fear").getValue());
                MainActivity.disgustColor = String.valueOf(dataSnapshot.child("color").child("disgust").getValue());
                MainActivity.restlessColor = String.valueOf(dataSnapshot.child("color").child("restless").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        finish();
    }

//    public void setBtnChange(View v){
//
//        ID=v.getId();
//        color = PreferenceManager.getDefaultSharedPreferences(this).getInt("color", Color.WHITE);
////        new ColorPickerDialog(this, this, color).show(); ////----***
//        openColorPicker(v);
//
//    }

//    @Override
//    public void colorChanged(int color) {
//        StateListDrawable state = new StateListDrawable();
//        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("color", color).commit();
//
//        switch(ID) {
//            case R.id.rbJoy3:
//                iv = (ImageView) findViewById(R.id.imageView);
//                pick[0]=String.valueOf(color);
//                break;
//            case R.id.rbSad3:
//                iv = (ImageView) findViewById(R.id.imageView2);
//                pick[1]=String.valueOf(color);
//                break;
//            case R.id.rbAnger3:
//                iv = (ImageView) findViewById(R.id.imageView3);
//                pick[2]=String.valueOf(color);
//                break;
//            case R.id.rbFear3:
//                iv = (ImageView) findViewById(R.id.imageView4);
//                pick[3]=String.valueOf(color);
//                break;
//            case R.id.rbDisgust3:
//                iv = (ImageView) findViewById(R.id.imageView5);
//                pick[4]=String.valueOf(color);
//                break;
//            case R.id.rbRestless3:
//                iv = (ImageView) findViewById(R.id.imageView6);
//                pick[5]=String.valueOf(color);
//                break;
//
//        }
//
//        Drawable roundDrawable = getResources().getDrawable(R.drawable.shape);
//        roundDrawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
//        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//            iv.setBackgroundDrawable(roundDrawable);
//        } else {
//            iv.setBackground(roundDrawable);
//        }
//
//
//    }


    public void openColorPicker(View v) {    ///***

        ID=v.getId();
        StateListDrawable state = new StateListDrawable();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("color", color).commit();


        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, tColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                tColor = color;

                switch(ID) {
                    case R.id.rbJoy3:
                        iv = (ImageView) findViewById(R.id.imageView);
                        pick[0]=String.valueOf(tColor);
                        break;
                    case R.id.rbSad3:
                        iv = (ImageView) findViewById(R.id.imageView2);
                        pick[1]=String.valueOf(tColor);
                        break;
                    case R.id.rbAnger3:
                        iv = (ImageView) findViewById(R.id.imageView3);
                        pick[2]=String.valueOf(tColor);
                        break;
                    case R.id.rbFear3:
                        iv = (ImageView) findViewById(R.id.imageView4);
                        pick[3]=String.valueOf(tColor);
                        break;
                    case R.id.rbDisgust3:
                        iv = (ImageView) findViewById(R.id.imageView5);
                        pick[4]=String.valueOf(tColor);
                        break;
                    case R.id.rbRestless3:
                        iv = (ImageView) findViewById(R.id.imageView6);
                        pick[5]=String.valueOf(tColor);
                        break;

                }
                Drawable roundDrawable = getResources().getDrawable(R.drawable.shape);
                roundDrawable.setColorFilter(tColor, PorterDuff.Mode.SRC_ATOP);
                iv.setBackground(roundDrawable);


            }



        });
        colorPicker.show();
    }

    public void setDefaultColor(){

        int i=0;

        myRef.child(userID).child("color").child("joy").setValue(colorpurple);
        myRef.child(userID).child("color").child("sad").setValue(colorpink);
        myRef.child(userID).child("color").child("anger").setValue(colorgreen);
        myRef.child(userID).child("color").child("fear").setValue(colororange);
        myRef.child(userID).child("color").child("disgust").setValue(colorblue);
        myRef.child(userID).child("color").child("restless").setValue(coloryellow);
    }




    public Drawable GetDrawable(int drawableResId, int color) {
        Drawable drawable =  getResources().getDrawable(drawableResId);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        return drawable;
    }

}