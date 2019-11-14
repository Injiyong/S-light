package cau.injiyong.slight;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.preference.PreferenceManager;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;




public class Setting extends AppCompatActivity implements View.OnClickListener, ColorPickerDialog.OnColorChangedListener {

    Button button4;
    int color;
    int ID;
    ImageView iv;
    Button btnChange[]=new Button[6];




    TextView mTvBluetoothStatus;
    TextView mTvReceiveData;
    TextView mTvSendData;
    Button mBtnBluetoothOn;
    Button mBtnBluetoothOff;
    Button mBtnConnect;
    Button mBtnSendData;

    BluetoothAdapter mBluetoothAdapter;
    Set<BluetoothDevice> mPairedDevices;
    List<String> mListPairedDevices;

    Handler mBluetoothHandler;
    ConnectedBluetoothThread mThreadConnectedBluetooth;
    BluetoothDevice mBluetoothDevice;
    BluetoothSocket mBluetoothSocket;

    final static int BT_REQUEST_ENABLE = 1;
    final static int BT_MESSAGE_READ = 2;
    final static int BT_CONNECTING_STATUS = 3;
    final static UUID BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");



    RadioGroup rgSad,rgJoy,rgFear,rgAnger,rgDisgust,rgRestless;
    String[] pick=new String[6];

    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);





        mTvBluetoothStatus = (TextView)findViewById(R.id.tvBluetoothStatus);
        mTvReceiveData = (TextView)findViewById(R.id.tvReceiveData);
        //mTvSendData =  (EditText) findViewById(R.id.tvSendData);
        mBtnBluetoothOn = (Button)findViewById(R.id.btnBluetoothOn);
        mBtnBluetoothOff = (Button)findViewById(R.id.btnBluetoothOff);
        // mBtnConnect = (Button)findViewById(R.id.btnConnect);
        // mBtnSendData = (Button)findViewById(R.id.btnSendData);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mBluetoothHandler = new Handler(){
            public void handleMessage(android.os.Message msg){
                if(msg.what == BT_MESSAGE_READ){
                    String readMessage = null;
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    mTvReceiveData.setText(readMessage);
                }
            }
        };




        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("userInfo");






        btnChange[0] = (Button) findViewById(R.id.rbAnger3);
        btnChange[1] = (Button) findViewById(R.id.rbDisgust3);
        btnChange[2] = (Button) findViewById(R.id.rbFear3);
        btnChange[3] = (Button) findViewById(R.id.rbJoy3);
        btnChange[4] = (Button) findViewById(R.id.rbRestless3);
        btnChange[5] = (Button) findViewById(R.id.rbSad3);



        for(int i=0;i<6;i++)
            btnChange[i].setOnClickListener(this);


        rgJoy=  (RadioGroup) findViewById(R.id.rgJoy);
        rgSad = (RadioGroup) findViewById(R.id.rgSad);
        rgAnger = (RadioGroup) findViewById(R.id.rgAnger);
        rgDisgust = (RadioGroup) findViewById(R.id.rgDisgust);
        rgFear = (RadioGroup) findViewById(R.id.rgFear);
        rgRestless = (RadioGroup) findViewById(R.id.rgRestless);



        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                String userID = mAuth.getUid();

                int btnId=rgJoy.getCheckedRadioButtonId();
                switch(btnId){

                    case R.id.rbJoy1://보라
                        myRef.child(userID).child("color").child("joy").setValue("#8041D9");
                        break;
                    case R.id.rbJoy2://파랑
                        myRef.child(userID).child("color").child("joy").setValue("#0054FF");
                        break;
                    case R.id.rbJoy3:
                        myRef.child(userID).child("color").child("joy").setValue(pick[0]);
                        break;

                }

                btnId=rgSad.getCheckedRadioButtonId();
                switch(btnId){

                    case R.id.rbSad1://분홍
                        myRef.child(userID).child("color").child("sad").setValue("#F361DC");
                        break;
                    case R.id.rbSad2://주황
                        myRef.child(userID).child("color").child("sad").setValue("#FF5E00");
                        break;
                    case R.id.rbSad3:
                        myRef.child(userID).child("color").child("sad").setValue(pick[1]);
                        break;

                }

                btnId=rgAnger.getCheckedRadioButtonId();
                switch(btnId){

                    case R.id.rbAnger1://초록
                        myRef.child(userID).child("color").child("anger").setValue("#1DDB16");
                        break;
                    case R.id.rbAnger2://파랑
                        myRef.child(userID).child("color").child("anger").setValue("#0054FF");
                        break;
                    case R.id.rbAnger3:
                        myRef.child(userID).child("color").child("anger").setValue(pick[2]);
                        break;

                }
                btnId=rgFear.getCheckedRadioButtonId();
                switch(btnId){

                    case R.id.rbFear1://주황
                        myRef.child(userID).child("color").child("fear").setValue("#FF5E00");
                        break;
                    case R.id.rbFear2://초록
                        myRef.child(userID).child("color").child("fear").setValue("#1DDB16");
                        break;
                    case R.id.rbFear3:
                        myRef.child(userID).child("color").child("fear").setValue(pick[3]);
                        break;

                }
                btnId=rgDisgust.getCheckedRadioButtonId();
                switch(btnId){

                    case R.id.rbDisgust1://파랑
                        myRef.child(userID).child("color").child("disgust").setValue("#0054FF");
                        break;
                    case R.id.rbDisgust2://보라
                        myRef.child(userID).child("color").child("disgust").setValue("#8041D9");
                        break;
                    case R.id.rbDisgust3:
                        myRef.child(userID).child("color").child("disgust").setValue(pick[4]);
                        break;

                }
                btnId=rgRestless.getCheckedRadioButtonId();
                switch(btnId){

                    case R.id.rbRestless1://노랑
                        myRef.child(userID).child("color").child("restless").setValue("#FFE400");
                        break;
                    case R.id.rbRestless2://주황
                        myRef.child(userID).child("color").child("restless").setValue("#FF5E00");
                        break;
                    case R.id.rbRestless3:
                        myRef.child(userID).child("color").child("restless").setValue(pick[5]);
                        break;

                }

                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();                                                                                           //finish() 현재 엑티비티(화면)을 종료하는 메소드이다.
            }
        });



    }

    public void OnClickHandlerPurple(View view) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("보라").setMessage("신중함을 요하는 상황이나 창의적인 아이디어가 필요할때, 혹은 터닝포인트가 필요할때 도움을 줍니다.");
        AlertDialog alertDialog = builder1.create();
        alertDialog.show();
        if(mThreadConnectedBluetooth!=null){
            mThreadConnectedBluetooth.write("1");
        }
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



    @Override
    public void onClick(View v) {
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
                break;
            case R.id.rbSad3:
                iv = (ImageView) findViewById(R.id.imageView2);
                break;
            case R.id.rbAnger3:
                iv = (ImageView) findViewById(R.id.imageView3);
                break;
            case R.id.rbFear3:
                iv = (ImageView) findViewById(R.id.imageView4);
                break;
            case R.id.rbDisgust3:
                iv = (ImageView) findViewById(R.id.imageView5);
                break;
            case R.id.rbRestless3:
                iv = (ImageView) findViewById(R.id.imageView6);
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






    private class ConnectedBluetoothThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedBluetoothThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "소켓 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = mmInStream.available();
                    if (bytes != 0) {
                        SystemClock.sleep(100);
                        bytes = mmInStream.available();
                        bytes = mmInStream.read(buffer, 0, bytes);
                        mBluetoothHandler.obtainMessage(BT_MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }
        public void write(String str) {
            byte[] bytes = str.getBytes();
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "데이터 전송 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        }
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "소켓 해제 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
            }
        }
    }






}