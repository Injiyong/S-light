package cau.injiyong.slight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.preference.PreferenceManager;
import android.text.TextUtils;
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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.lang.reflect.Method;
import java.util.UUID;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Setting extends AppCompatActivity implements ColorPickerDialog.OnColorChangedListener {

    Button button4;
    int color;
    int ID;
    ImageView iv;
    Button btnChange[]=new Button[6];
//
//    TextView mTvBluetoothStatus;
//    TextView mTvReceiveData;
//    TextView mTvSendData;
//    Button mBtnBluetoothOn;
//    Button mBtnBluetoothOff;
//    Button mBtnConnect;
//    Button mBtnSendData;
//
//    BluetoothAdapter mBluetoothAdapter;
//    Set<BluetoothDevice> mPairedDevices;
//    List<String> mListPairedDevices;
//
//    Handler mBluetoothHandler;
//    ConnectedBluetoothThread mThreadConnectedBluetooth;
//    BluetoothDevice mBluetoothDevice;
//    BluetoothSocket mBluetoothSocket;
//
//    final static int BT_REQUEST_ENABLE = 1;
//    final static int BT_MESSAGE_READ = 2;
//    final static int BT_CONNECTING_STATUS = 3;
//    final static UUID BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
//
//

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getUid();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("userInfo");


//       // mTvBluetoothStatus = (TextView)findViewById(R.id.tvBluetoothStatus);
//      //  mTvReceiveData = (TextView)findViewById(R.id.tvReceiveData);
//        //mTvSendData =  (EditText) findViewById(R.id.tvSendData);
//        mBtnBluetoothOn = (Button)findViewById(R.id.btnBluetoothOn);
//        mBtnBluetoothOff = (Button)findViewById(R.id.btnBluetoothOff);
//        // mBtnConnect = (Button)findViewById(R.id.btnConnect);
//        // mBtnSendData = (Button)findViewById(R.id.btnSendData);
//
//        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//
//        mBluetoothHandler = new Handler(){
//            public void handleMessage(android.os.Message msg){
//                if(msg.what == BT_MESSAGE_READ){
//                    String readMessage = null;
//                    try {
//                        readMessage = new String((byte[]) msg.obj, "UTF-8");
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                    mTvReceiveData.setText(readMessage);
//                }
//            }
//        };

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
            btnChange[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setBtnChange(view);
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
                String tempcolor;
                int tempColor;

                for(int i=0;i<6;i++) {
                    btnID[i] = String.valueOf(dataSnapshot.child("button").child(emotion[i]).getValue());
                    if (btnID[i].equals("null"))
                        rg[i].check(rb1[i]);
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



    }

    public void bk(int color, int i){
        pick[i] = Integer.toString(color);
        Drawable roundDrawable = getResources().getDrawable(R.drawable.shape);
        roundDrawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            tempiv[i].setBackgroundDrawable(roundDrawable);
        } else {
            tempiv[i].setBackground(roundDrawable);
        }
    }

    public void OnClickHandlerPurple(View view) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("보라").setMessage("신중함을 요하는 상황이나 창의적인 아이디어가 필요할때, 혹은 터닝포인트가 필요할때 도움을 줍니다.");
        AlertDialog alertDialog = builder1.create();
        alertDialog.show();
//        if(mThreadConnectedBluetooth!=null){
//            mThreadConnectedBluetooth.write("1");
//        }
    }

    public void OnClickHandlerPink(View view) {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setTitle("분홍").setMessage("우울하거나 혼란스러울때 마음에 안정을주며, 감정을 추스리는데 도움을 줍니다.");
        AlertDialog alertDialog = builder2.create();
        alertDialog.show();
    }

    public void OnClickHandlerBlue(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("파랑").setMessage("긴장하거나 신경이 예민해졌을때 마음을 진정시켜줍니다.");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void OnClickHandlerGreen(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("초록").setMessage("마음이 불안정하며 긴장될때나 마음의 상처를 받았을때 도움을 줍니다.");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void OnClickHandlerOrange(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("주황").setMessage("자신감을 높여주는 효과가 있어 적극적인 마인드를 일꺠워주고 머리가 복잡하거나 침체기의 상황에서 활기를 되찾아줍니다.");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void OnClickHandlerYellow(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("노랑").setMessage("의욕이 저하됐을때 도움을 줍니다. 결단력을 일꺠워주기도합니다");
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void complete(){
        String userID = mAuth.getUid();

        int btnId=rg[0].getCheckedRadioButtonId();
        switch(btnId){

            case R.id.rbJoy1://보라
                myRef.child(userID).child("color").child("joy").setValue("-5635864");
                myRef.child(userID).child("button").child("joy").setValue(R.id.rbJoy1);
                break;
            case R.id.rbJoy2://파랑
                myRef.child(userID).child("color").child("joy").setValue("-14451969");
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
                myRef.child(userID).child("color").child("sad").setValue("-65324");
                myRef.child(userID).child("button").child("sad").setValue(R.id.rbSad1);
                break;
            case R.id.rbSad2://주황
                myRef.child(userID).child("color").child("sad").setValue("-1245434");
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
                myRef.child(userID).child("color").child("anger").setValue("-11927752");
                myRef.child(userID).child("button").child("anger").setValue(R.id.rbAnger1);
                break;
            case R.id.rbAnger2://파랑
                myRef.child(userID).child("color").child("anger").setValue("-14451969");
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
                myRef.child(userID).child("color").child("fear").setValue("-1245434");
                myRef.child(userID).child("button").child("fear").setValue(R.id.rbFear1);
                break;
            case R.id.rbFear2://초록
                myRef.child(userID).child("color").child("fear").setValue("-11927752");
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
                myRef.child(userID).child("color").child("disgust").setValue("-14451969");
                myRef.child(userID).child("button").child("disgust").setValue(R.id.rbDisgust1);
                break;
            case R.id.rbDisgust2://보라
                myRef.child(userID).child("color").child("disgust").setValue("-5635864");
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
                myRef.child(userID).child("color").child("restless").setValue("-1245434");
                myRef.child(userID).child("button").child("restless").setValue(R.id.rbRestless1);
                break;
            case R.id.rbRestless2://주황
                myRef.child(userID).child("color").child("restless").setValue("-1245434");
                myRef.child(userID).child("button").child("restless").setValue(R.id.rbRestless2);
                break;
            case R.id.rbRestless3:
                myRef.child(userID).child("color").child("restless").setValue(pick[5]);
                myRef.child(userID).child("button").child("restless").setValue(R.id.rbRestless3);
                break;

        }

        //Intent intent=new Intent(Setting.this,MainActivity.class);
        //startActivity(intent);
        finish();

    }


    public void setBtnChange(View v){

        ID=v.getId();
        color = PreferenceManager.getDefaultSharedPreferences(this).getInt("color", Color.WHITE);
        new ColorPickerDialog(this, this, color).show();

    }

    @Override
    public void colorChanged(int color) {
        StateListDrawable state = new StateListDrawable();
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("color", color).commit();

        switch(ID) {
            case R.id.rbJoy3:
                iv = (ImageView) findViewById(R.id.imageView);
                pick[0]=String.valueOf(color);
                break;
            case R.id.rbSad3:
                iv = (ImageView) findViewById(R.id.imageView2);
                pick[1]=String.valueOf(color);
                break;
            case R.id.rbAnger3:
                iv = (ImageView) findViewById(R.id.imageView3);
                pick[2]=String.valueOf(color);
                break;
            case R.id.rbFear3:
                iv = (ImageView) findViewById(R.id.imageView4);
                pick[3]=String.valueOf(color);
                break;
            case R.id.rbDisgust3:
                iv = (ImageView) findViewById(R.id.imageView5);
                pick[4]=String.valueOf(color);
                break;
            case R.id.rbRestless3:
                iv = (ImageView) findViewById(R.id.imageView6);
                pick[5]=String.valueOf(color);
                break;

        }

        Drawable roundDrawable = getResources().getDrawable(R.drawable.shape);
        roundDrawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            iv.setBackgroundDrawable(roundDrawable);
        } else {
            iv.setBackground(roundDrawable);
        }


    }

    public Drawable GetDrawable(int drawableResId, int color) {
        Drawable drawable =  getResources().getDrawable(drawableResId);
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        return drawable;
    }





//
//    private class ConnectedBluetoothThread extends Thread {
//        private final BluetoothSocket mmSocket;
//        private final InputStream mmInStream;
//        private final OutputStream mmOutStream;
//
//        public ConnectedBluetoothThread(BluetoothSocket socket) {
//            mmSocket = socket;
//            InputStream tmpIn = null;
//            OutputStream tmpOut = null;
//
//            try {
//                tmpIn = socket.getInputStream();
//                tmpOut = socket.getOutputStream();
//            } catch (IOException e) {
//                Toast.makeText(getApplicationContext(), "소켓 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
//            }
//
//            mmInStream = tmpIn;
//            mmOutStream = tmpOut;
//        }
//        public void run() {
//            byte[] buffer = new byte[1024];
//            int bytes;
//
//            while (true) {
//                try {
//                    bytes = mmInStream.available();
//                    if (bytes != 0) {
//                        SystemClock.sleep(100);
//                        bytes = mmInStream.available();
//                        bytes = mmInStream.read(buffer, 0, bytes);
//                        mBluetoothHandler.obtainMessage(BT_MESSAGE_READ, bytes, -1, buffer).sendToTarget();
//                    }
//                } catch (IOException e) {
//                    break;
//                }
//            }
//        }
//        public void write(String str) {
//            byte[] bytes = str.getBytes();
//            try {
//                mmOutStream.write(bytes);
//            } catch (IOException e) {
//                Toast.makeText(getApplicationContext(), "데이터 전송 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
//            }
//        }
//        public void cancel() {
//            try {
//                mmSocket.close();
//            } catch (IOException e) {
//                Toast.makeText(getApplicationContext(), "소켓 해제 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//




}