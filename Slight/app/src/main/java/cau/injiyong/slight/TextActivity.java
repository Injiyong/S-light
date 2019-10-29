package cau.injiyong.slight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TextActivity extends AppCompatActivity {

    private static final String TAG = "TextActivity";
    private final int m_nMaxLengthOfDeviceName = 500;

    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;

    EditText etNewMessage;
    Button btUpdate;

    String strDate;
    TextView Datepick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        mAuth = FirebaseAuth.getInstance();

        etNewMessage = (EditText) findViewById(R.id.et_newData);
        btUpdate = (Button) findViewById(R.id.bt_update);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("userInfo");

        //List 버튼 클릭시 list 창으로 넘어감
//        FloatingActionButton btnNext= findViewById(R.id.bt_list);
//        btnNext.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v ){
//                Intent intent=new Intent(getApplicationContext(),MemoList.class);
//                startActivity(intent);
//            }
//        });


        //버튼 이벤트
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etNewMessage.setFilters(new InputFilter[] { new InputFilter.LengthFilter(m_nMaxLengthOfDeviceName) });

                String userID = mAuth.getUid();
                String newMessage = etNewMessage.getText().toString();
                TextItem mMessage = new TextItem(newMessage, strDate, null);

                myRef.child(userID).child("memo_list").push().setValue(mMessage); // 원래 newMessage
                Intent intent=new Intent(getApplicationContext(),MemoList.class);
                startActivity(intent);
            }
        });

        // Read from the database
        // 그리고 데이터베이스에 변경사항이 있으면 실행된다.
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //     String value = dataSnapshot.getValue(String.class);
                //데이터를 화면에 출력해 준다.
                //   tvMessage.setText(value);
                //     Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm", java.util.Locale.getDefault());
        strDate = dateFormat.format(date);
        Datepick = (TextView) findViewById(R.id.date);
        Datepick.setText(strDate);

    }
}