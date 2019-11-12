
package cau.injiyong.slight;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kakao.network.ApiErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import android.graphics.Color;
import android.widget.RelativeLayout;
import android.graphics.Color;
import android.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ColorPickerDialog.OnColorChangedListener {
    
    //String strNickname, strProfile, strEmail, strAgeRange, strGender, strBirthday;
    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    
    androidx.constraintlayout.widget.ConstraintLayout relative_color;
    Button btncolor;
    int color;
    TextView textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("userInfo");
        
        relative_color = (androidx.constraintlayout.widget.ConstraintLayout)findViewById(R.id.relative_color);
        
        if (! Python.isStarted())
            Python.start(new AndroidPlatform(this));
        
        Python py = Python.getInstance();
        PyObject pyf = py.getModule("myscript"); // py file name
        PyObject obj = pyf.callAttr("test"); // def name in py file
        textview = findViewById(R.id.pytext);
        textview.setText(obj.toString());
        
        btncolor = (Button)findViewById(R.id.btncolor);
        btncolor.setOnClickListener(this);
        
        
        
        Intent intent = getIntent();
        
        
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
                Intent intent=new Intent(getApplicationContext(),
                                         Setting.class); //넘어갈 클래스
                startActivity(intent);
            }
        });
        
        
    }
    @Override
    public void onClick(View v) {
        color = PreferenceManager.getDefaultSharedPreferences(this).getInt("color", Color.WHITE);
        new ColorPickerDialog(this, this, color).show();
    }
    
    @Override
    public void colorChanged(int color) {
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("color", color).commit();
        relative_color.setBackgroundColor(color);
        
        String hexColor = String.format("#%06X", (0xFFFFFF & color));
        String userID = mAuth.getUid();
        myRef.child(userID).child("current_color").setValue(hexColor);
    }
}

