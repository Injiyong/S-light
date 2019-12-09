package cau.injiyong.slight;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kakao.network.ApiErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import android.graphics.Color;
import android.widget.RelativeLayout;
import android.graphics.Color;
import android.preference.PreferenceManager;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Switch;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;

import yuku.ambilwarna.AmbilWarnaDialog;//new colorpicker



public class MainActivity extends AppCompatActivity {

    private static final String TAG = "bluetooth2";
    public static Context mContext;

    int tColor, n=0; ////*****

    public static Activity activity;

    static androidx.constraintlayout.widget.ConstraintLayout relative_color;
    static Switch lightSwitch;
    SeekBar seekbar;
    Button btncolor;
    Button btnbright;
    int color;

    Button mBtnBluetoothOn;
    Button mBtnBluetoothOff;
    Button mBtnConnect;
    BluetoothAdapter mBluetoothAdapter;  //폰의 블루투스 모듈을 사용하기위한 오브젝트
    Set<BluetoothDevice> mPairedDevices;
    List<String> mListPairedDevices;
    Handler mBluetoothHandler;
    static ConnectedBluetoothThread mThreadConnectedBluetooth;
    BluetoothDevice mBluetoothDevice; //기기의 장치정보, 다른 기기랑 연결
    static BluetoothSocket mBluetoothSocket; //폰과 디바이스간 통신 채널에 대응
    static SharedPreferences mSharedPreferences;
    static SharedPreferences.Editor editor;
    final static int BT_REQUEST_ENABLE = 1;
    final static int BT_MESSAGE_READ = 2;
    final static int BT_CONNECTING_STATUS = 3;
    final static UUID BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    String userID;
    String voiceResult;
    String sentimentResult;

    static String joyColor;
    static String sadnessColor;
    static String angerColor;
    static String fearColor;
    static String disgustColor;
    static String restlessColor;

    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;

    Intent intent;
    SpeechRecognizer mRecognizer;
    TextView txt;
    //Voice 버튼
    Button btnVoice;
    ProgressDialog progressDialog;

    private final int MY_PERMISSIONS_RECORD_AUDIO = 1;

    private boolean flag_BackKey = false;                //BACK KEY가 눌려졌는지에 대한 FLAG 로 사용됩니다.
    private long currentTime = 0;                          // Time(Millis) Interval을 계산합니다.
    private static final int MSG_TIMER = 1;              // Switch문에서 사용하게 되는 Key값입니다.
    private static final int BACKKEY_TIMEOUT = 2;    // Interval을 정의합니다.
    private static final int IN_MILLISEC = 1000;        // Millis를 정의합니다.
    int max = 255;
    int bk_1 = 125;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSharedPreferences = getSharedPreferences("color",MODE_PRIVATE);
        editor = mSharedPreferences.edit();
        mContext=this;
        activity = MainActivity.this;

        int x = mSharedPreferences.getInt("myColor", 0);

        final Paint mCenterPaint; ///***
        mCenterPaint = new Paint(Paint.ANTI_ALIAS_FLAG); ///***
        tColor = mCenterPaint.getColor();///**

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("분석중....");
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);


        mBluetoothHandler = new Handler(){
            public void handleMessage(android.os.Message msg){
                if(msg.what == BT_MESSAGE_READ){
                    String readMessage = null;
                    try {
                        readMessage = new String((byte[]) msg.obj, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        lightSwitch=(Switch)findViewById(R.id.lightSwitch);
        seekbar=(SeekBar)findViewById(R.id.seekBar1);
        btnbright=(Button)findViewById(R.id.btnbright);
        mBtnBluetoothOn = (Button)findViewById(R.id.btnBluetoothOn);
        //   mBtnBluetoothOff = (Button)findViewById(R.id.btnBluetoothOff);
        mBtnConnect = (Button)findViewById(R.id.btnConnect);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); //해당 장치가 블루투스 기능을 지원하는지 알아오는 메소드

        if(mBluetoothAdapter != null && mBluetoothAdapter.isEnabled()){
            // 활성화
            connectDevice("HC-06");
        }
        else{
            // 비활성화
        }


        lightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(lightSwitch.isChecked()==true){
                    Log.d("하....", "노답...");
                    if(mThreadConnectedBluetooth!=null){
                        int x = mSharedPreferences.getInt("myColor", 0);
                        mThreadConnectedBluetooth.sendData("1");
                        String r = String.format("%s", Color.red(x));
                        mThreadConnectedBluetooth.sendData(r);
                        String g = String.format("%s", Color.green(x));
                        mThreadConnectedBluetooth.sendData(g);
                        String t = String.format("%s", Color.blue(x));
                        mThreadConnectedBluetooth.sendData(t);
                    }
                }
                else{
                    Log.d("하......", "유답...");
                    if(mThreadConnectedBluetooth!=null){
                        mThreadConnectedBluetooth.write("2");
                    }
                }
            }
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                bk_1 = seekbar.getProgress();
                if(mThreadConnectedBluetooth!=null){
                    mThreadConnectedBluetooth.sendData("4");
                    Log.d("병국", bk_1 + "");
                    mThreadConnectedBluetooth.sendData(Integer.toString(bk_1));
                }
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                bk_1 = seekbar.getProgress();

                if (progress < 20){
                    seekBar.setProgress(20); // magic solution, ha
                }

            }
        });



        mBtnBluetoothOn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mBtnBluetoothOn.getText().equals("블루투스 ON")) {
                    mBtnBluetoothOn.setText("블루투스 OFF");
                    mBtnBluetoothOn.setTextColor(Color.rgb(255,205,74));

                    Drawable btncolor = getResources().getDrawable(R.drawable.btncolor);
                    btncolor.setColorFilter(0, PorterDuff.Mode.SRC_ATOP);
                    mBtnBluetoothOn.setBackground(btncolor);

                    bluetoothOn();
                }
                else {
                    mBtnBluetoothOn.setText("블루투스 ON");
                    mBtnBluetoothOn.setTextColor(Color.rgb(64,64,64));

                    Drawable roundDrawable = getResources().getDrawable(R.drawable.btncolor);
                    roundDrawable.setColorFilter(-12982, PorterDuff.Mode.SRC_ATOP);
                    mBtnBluetoothOn.setBackground(roundDrawable);

                    bluetoothOff();
                }
            }
        });

        mBtnConnect.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                listPairedDevices();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("userInfo");

        btnVoice= findViewById(R.id.btnVoice);
        relative_color = (androidx.constraintlayout.widget.ConstraintLayout)findViewById(R.id.relative_color);
        relative_color.setBackgroundColor(x);

        userID=mAuth.getUid();

        myRef.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                joyColor = String.valueOf(dataSnapshot.child("color").child("joy").getValue());
                sadnessColor = String.valueOf(dataSnapshot.child("color").child("sad").getValue());
                angerColor = String.valueOf(dataSnapshot.child("color").child("anger").getValue());
                fearColor = String.valueOf(dataSnapshot.child("color").child("fear").getValue());
                disgustColor = String.valueOf(dataSnapshot.child("color").child("disgust").getValue());
                restlessColor = String.valueOf(dataSnapshot.child("color").child("restless").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        if (! Python.isStarted())
            Python.start(new AndroidPlatform(this));

        btncolor = (Button)findViewById(R.id.btncolor);
        btncolor.setOnClickListener(new View.OnClickListener() { //*****
            @Override
            public void onClick(View v) {
                n=1;
                openColorPicker();
            }
        });

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_RECORD_AUDIO
                );
            }
        }

        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
        // intent.putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, new Long(5000));
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(this);
        mRecognizer.setRecognitionListener(recognitionListener);

        btnVoice.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v ){
                btnVoice.setBackgroundResource(R.drawable.vs_micbtn_pressed);
                mRecognizer.startListening(intent);
                btnVoice.setBackgroundResource(R.drawable.vs_micbtn_off);
            }
        });


        //NEXT버튼 click 시 다음 화면으로 이동하기
        Button btnNext= findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v ){
                Intent intent=new Intent(getApplicationContext(),MemoList.class);
                startActivity(intent);
            }
        });

        // setting 화면으로 이동
        Button btnSet= findViewById(R.id.btnSet);
        btnSet.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v ){

                Intent intent=new Intent(getApplicationContext(), Setting.class); //넘어갈 클래스
                startActivity(intent);
            }
        });
    }

    void bluetoothOn() { // 블루투스 ON 버튼 메소드

        if(mBluetoothAdapter == null) { // 블루투스 지원함?
            Toast.makeText(getApplicationContext(), "블루투스를 지원하지 않는 기기입니다.", Toast.LENGTH_LONG).show();
        }
        else {
            if (mBluetoothAdapter.isEnabled()) { //블루투스 활성화됨?
                if(mPairedDevices != null && mPairedDevices.size() > 0){
                    connectSelectedDevice("HC-06");
                }
                Toast.makeText(getApplicationContext(), "블루투스가 이미 활성화 되어 있습니다.", Toast.LENGTH_LONG).show();
                //   mTvBluetoothStatus.setText("활성화");
            }
            else {
                Toast.makeText(getApplicationContext(), "블루투스가 활성화 되어 있지 않습니다.", Toast.LENGTH_LONG).show();
                Intent intentBluetoothEnable = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intentBluetoothEnable, BT_REQUEST_ENABLE);

            }
        }
    }
    void bluetoothOff() {
        if(mThreadConnectedBluetooth != null){
            mThreadConnectedBluetooth.write("2");
        }
        lightSwitch.setChecked(false);
        if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
            Toast.makeText(getApplicationContext(), "블루투스가 비활성화 되었습니다.", Toast.LENGTH_SHORT).show();
            //  mTvBluetoothStatus.setText("비활성화");
        }
        else {
            Toast.makeText(getApplicationContext(), "블루투스가 이미 비활성화 되어 있습니다.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //블루투스 활성화 결과를 위한 메소드
        switch (requestCode) {
            case BT_REQUEST_ENABLE:
                if (resultCode == RESULT_OK) { // 블루투스 활성화를 확인을 클릭하였다면
                    Toast.makeText(getApplicationContext(), "블루투스 활성화", Toast.LENGTH_LONG).show();
                    //    mTvBluetoothStatus.setText("활성화");
                    if(mPairedDevices != null && mPairedDevices.size() > 0){
                        connectSelectedDevice("HC-06");
                    }
                } else if (resultCode == RESULT_CANCELED) { // 블루투스 활성화를 취소를 클릭하였다면
                    Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_LONG).show();
                    //   mTvBluetoothStatus.setText("비활성화");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    public void connectDevice(String s){
        mPairedDevices = mBluetoothAdapter.getBondedDevices();
        if(mPairedDevices.size() > 0){
            connectSelectedDevice("HC-06");
        }
    }

    public void listPairedDevices() {
        if (mBluetoothAdapter.isEnabled()) {
            mPairedDevices = mBluetoothAdapter.getBondedDevices();


            if (mPairedDevices.size() > 0) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("장치 선택");

                mListPairedDevices = new ArrayList<String>();

                for (BluetoothDevice device : mPairedDevices) {
                    mListPairedDevices.add(device.getName());
                    //mListPairedDevices.add(device.getName() + "\n" + device.getAddress());
                }

                final CharSequence[] items = mListPairedDevices.toArray(new CharSequence[mListPairedDevices.size()]);
                mListPairedDevices.toArray(new CharSequence[mListPairedDevices.size()]);


                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        Log.d("ㅇㅂㅇ", items[item].toString());
                        connectSelectedDevice(items[item].toString());

                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                Toast.makeText(getApplicationContext(), "페어링된 장치가 없습니다.", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "블루투스가 비활성화 되어 있습니다.", Toast.LENGTH_SHORT).show();
        }
    }
    void connectSelectedDevice(String selectedDeviceName) {
        for(BluetoothDevice tempDevice : mPairedDevices) {
            if (selectedDeviceName.equals(tempDevice.getName())) {
                mBluetoothDevice = tempDevice;
                break;
            }
        }
        try {
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(BT_UUID);
            mBluetoothSocket.connect();
            mThreadConnectedBluetooth = new ConnectedBluetoothThread(mBluetoothSocket);
            mThreadConnectedBluetooth.start();
            mBluetoothHandler.obtainMessage(BT_CONNECTING_STATUS, 1, -1).sendToTarget();
            Toast.makeText(getApplicationContext(), "블루투스 연결 성공", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "블루투스 연결 중 오류가 발생했습니다.", Toast.LENGTH_LONG).show();
        }
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
            byte[] msgBuffer = str.getBytes();
            try {
                mmOutStream.write(msgBuffer);
            } catch (IOException e) {
                Log.d(TAG, "...Error data send: " + e.getMessage() + "...");
            }
        }

        void sendData(String msg) {
            msg += "\n";  // 문자열 종료표시 (\n)
            try{
                // getBytes() : String을 byte로 변환
                // OutputStream.write : 데이터를 쓸때는 write(byte[]) 메소드를 사용함.
                // byte[] 안에 있는 데이터를 한번에 기록해 준다.
                mmOutStream.write(msg.getBytes());  // 문자열 전송.
            }catch(Exception e) {  // 문자열 전송 도중 오류가 발생한 경우
                Toast.makeText(getApplicationContext(), "기기가 연결되어 있지 않습니다.",
                        Toast.LENGTH_LONG).show();
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



    public void openColorPicker() {    ///***


        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("color", tColor).commit();

        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, tColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {

                tColor = color;
                editor.putInt("myColor", tColor);
                editor.commit();
                int x = mSharedPreferences.getInt("myColor", 0);
                if( n == 1) {
                    relative_color.setBackgroundColor(tColor);
                }

                lightSwitch.setChecked(true);
                if(mThreadConnectedBluetooth != null) {
                    mThreadConnectedBluetooth.sendData("3");

                    String r = String.format("%s", Color.red(tColor));
                    mThreadConnectedBluetooth.sendData(r);
                    String g = String.format("%s", Color.green(tColor));
                    mThreadConnectedBluetooth.sendData(g);
                    String b = String.format("%s", Color.blue(tColor));
                    mThreadConnectedBluetooth.sendData(b);
                }

            }


        });

        colorPicker.show();
    }


    private RecognitionListener recognitionListener = new RecognitionListener() {
        @Override
        public void onReadyForSpeech(Bundle bundle) {
            btnVoice.setBackgroundResource(R.drawable.vs_micbtn_on);
            Toast.makeText(getApplicationContext(), "지금 말해주세요", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onBeginningOfSpeech() {
            //말이 시작된 부분
        }

        @Override
        public void onRmsChanged(float v) {
        }

        @Override
        public void onBufferReceived(byte[] bytes) {
        }

        @Override
        public void onEndOfSpeech() {
            btnVoice.setBackgroundResource(R.drawable.vs_micbtn_off);
            progressDialog.show(); //로딩창 보여주기
        }

        @Override
        public void onError(int i) {

            btnVoice.setBackgroundResource(R.drawable.vs_micbtn_off);
            Toast.makeText(getApplicationContext(), "다시시도해주세요", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onResults(Bundle bundle) {

            String key = "";
            key = SpeechRecognizer.RESULTS_RECOGNITION;
            ArrayList<String> mResult = bundle.getStringArrayList(key);

            String[] rs = new String[mResult.size()];
            mResult.toArray(rs);

            voiceResult = rs[0];
            int mySentimentColor=runPy(voiceResult);
            updateToDB(mySentimentColor);
            progressDialog.dismiss(); //로딩창 사라짐

            if (mBluetoothAdapter.isEnabled())
                lightOn(mySentimentColor);
            else
                Toast.makeText(getApplicationContext(), "연결된 기기가 없습니다.", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onPartialResults(Bundle bundle) {
        }

        @Override
        public void onEvent(int i, Bundle bundle) {
        }
    };



    public int runPy(String text){

        Python py = Python.getInstance();
        PyObject pyf = py.getModule("myscript"); // py file name
        PyObject obj = pyf.callAttr("test", text); // def name in py file

        sentimentResult = obj.toString();
        int mySentimentColor = 0;

        if (sentimentResult.equals("0")){
            mySentimentColor = Color.argb(255,239,228,210);
            relative_color.setBackgroundColor(mySentimentColor);
            // myRef.child(userID).child("result").push().setValue("default");
        }

        else if (sentimentResult.equals("1")){
            mySentimentColor = Integer.parseInt(joyColor);
            relative_color.setBackgroundColor(mySentimentColor);
            // myRef.child(userID).child("result").push().setValue("joy");
        }

        else if (sentimentResult.equals("2")){
            mySentimentColor = Integer.parseInt(sadnessColor);
            relative_color.setBackgroundColor(mySentimentColor);
            // myRef.child(userID).child("result").push().setValue("sadness");
        }

        else if (sentimentResult.equals("3")){
            mySentimentColor = Integer.parseInt(angerColor);
            relative_color.setBackgroundColor(mySentimentColor);
            // myRef.child(userID).child("result").push().setValue("anger");
        }

        else if (sentimentResult.equals("4")){
            mySentimentColor = Integer.parseInt(fearColor);
            relative_color.setBackgroundColor(mySentimentColor);
            // myRef.child(userID).child("result").push().setValue("fear");
        }

        else if (sentimentResult.equals("5")){
            mySentimentColor = Integer.parseInt(disgustColor);
            relative_color.setBackgroundColor(mySentimentColor);
            // myRef.child(userID).child("result").push().setValue("disgust");
        }

        else if (sentimentResult.equals("6")){
            mySentimentColor = Integer.parseInt(restlessColor);
            relative_color.setBackgroundColor(mySentimentColor);
            // myRef.child(userID).child("result").push().setValue("restless");
        }

        return mySentimentColor;
    }

    public static void logoutoff(){
        mThreadConnectedBluetooth.sendData("2");

        try {
            mBluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void lightOn(int mySentimentColor){

        mThreadConnectedBluetooth.sendData("3");

        String sr = String.format("%s",Color.red(mySentimentColor));
        mThreadConnectedBluetooth.sendData(sr);
        String sg = String.format("%s",Color.green(mySentimentColor));
        mThreadConnectedBluetooth.sendData(sg);
        String sb = String.format("%s",Color.blue(mySentimentColor));
        mThreadConnectedBluetooth.sendData(sb);

        lightSwitch.setChecked(true);
        int jColor = mySentimentColor;
        editor.putInt("myColor", jColor);
        editor.commit();
        int x = mSharedPreferences.getInt("myColor", 0);
        Toast.makeText(mContext, "인식완료", Toast.LENGTH_LONG).show();
    }

    public void updateToDB(int color){

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", java.util.Locale.getDefault());
        String strDate = dateFormat.format(date);

        TextItem mMessage = new TextItem(voiceResult, strDate, String.valueOf(color));
        myRef.child(userID).child("memo_list").push().setValue(mMessage);

    }

    @Override
    public void onBackPressed(){            //BACK키가 눌렸을 때의 함수로 Override하여 사용합니다.

        if ( flag_BackKey == false ){

            // 첫 번째로 클릭했을 경우로, false에서 true로 바꿔줍니다.
            flag_BackKey = true;

            currentTime = Calendar.getInstance().getTimeInMillis();       //Java의 Calendar를 import하여 사용합니다.

            Toast.makeText(this, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();

            startTimer();

        }else{            //만약 BACKKey의 플래그가 현재 false가 아닌, true 라면 아래를 수행합니다.

            // second click : 2초 이내면 종료! 아니면 아무것도 안한다.

            flag_BackKey = false;

            if ( Calendar.getInstance().getTimeInMillis() <= (currentTime + (BACKKEY_TIMEOUT * IN_MILLISEC )) ) {


                mThreadConnectedBluetooth.sendData("2");

                try {
                    mBluetoothSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                finish();                //currentTime + 2000 (2초) 이므로, 2초 안에 클릭 했을 때, MainActivity를 종료해주는 부분입니다.

            }

        }

    }

    private void startTimer() {                                    //2초의 시간적 여유를 가지도록 Delay 시킵니다.
        backTimerHandler.sendEmptyMessageDelayed(MSG_TIMER , BACKKEY_TIMEOUT * IN_MILLISEC );
    }



    private Handler backTimerHandler = new Handler(){

        public void handleMessage(Message msg){

            switch( msg.what ){                //msg의 값이 무엇인지 파악하여 만약 MSG_TIMER라면, flag를 다시 false로 해줍니다.

                case MSG_TIMER :{

                    flag_BackKey = false;

                }
                break;
            }
        }
    };
}