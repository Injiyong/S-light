package cau.injiyong.slight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

import cau.injiyong.slight.MainActivity;

public class TextActivity extends AppCompatActivity {

    private static final String TAG = "TextActivity";
    private final int m_nMaxLengthOfDeviceName = 500;
//    androidx.constraintlayout.widget.ConstraintLayout relative_color;


    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    String userID;

    EditText etNewMessage;
    Button btUpdate;

    String strDate;
    TextView Datepick;
    String sentimentResult;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        mAuth = FirebaseAuth.getInstance();

        etNewMessage = (EditText) findViewById(R.id.et_newData);
        btUpdate = (Button) findViewById(R.id.bt_update);
        //    relative_color = (androidx.constraintlayout.widget.ConstraintLayout)findViewById(R.id.relative_color);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("userInfo");
        userID=mAuth.getUid();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("분석중....");
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);


        if (! Python.isStarted())
            Python.start(new AndroidPlatform(this));

        //버튼 이벤트
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickUpdate();
            }
        });

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss", java.util.Locale.getDefault());
        strDate = dateFormat.format(date);
        Datepick = (TextView) findViewById(R.id.date);
        Datepick.setText(strDate);
    }

    public void onClickUpdate(){

        progressDialog.show();

        etNewMessage.setFilters(new InputFilter[] { new InputFilter.LengthFilter(m_nMaxLengthOfDeviceName) });
        String userID = mAuth.getUid();
        String newMessage = etNewMessage.getText().toString();

        Python py = Python.getInstance();
        PyObject pyf = py.getModule("myscript"); // py file name
        PyObject obj = pyf.callAttr("test", newMessage); // def name in py file

        sentimentResult = obj.toString();
        int mySentimentColor = 0;


        if (sentimentResult.equals("0")){
            mySentimentColor = Color.argb(255,239,228,210);
            MainActivity.relative_color.setBackgroundColor(mySentimentColor);
        }

        else if (sentimentResult.equals("1")){
            mySentimentColor = Integer.parseInt(MainActivity.joyColor);
            MainActivity.relative_color.setBackgroundColor(mySentimentColor);
        }

        else if (sentimentResult.equals("2")){
            mySentimentColor = Integer.parseInt(MainActivity.sadnessColor);
            MainActivity.relative_color.setBackgroundColor(mySentimentColor);
        }

        else if (sentimentResult.equals("3")){
            mySentimentColor = Integer.parseInt(MainActivity.angerColor);
            MainActivity.relative_color.setBackgroundColor(mySentimentColor);
        }

        else if (sentimentResult.equals("4")){
            mySentimentColor = Integer.parseInt(MainActivity.fearColor);
            MainActivity.relative_color.setBackgroundColor(mySentimentColor);
        }

        else if (sentimentResult.equals("5")){
            mySentimentColor = Integer.parseInt(MainActivity.disgustColor);
            MainActivity.relative_color.setBackgroundColor(mySentimentColor);
        }

        else if (sentimentResult.equals("6")){
            mySentimentColor = Integer.parseInt(MainActivity.restlessColor);
            MainActivity.relative_color.setBackgroundColor(mySentimentColor);
        }

        TextItem mMessage = new TextItem(newMessage, strDate,String.valueOf(mySentimentColor));
        myRef.child(userID).child("memo_list").push().setValue(mMessage);

        progressDialog.dismiss();

        MainActivity.lightOn(mySentimentColor);
        Toast.makeText(TextActivity.this, "등록완료", Toast.LENGTH_SHORT).show();
        finish();
    }
}