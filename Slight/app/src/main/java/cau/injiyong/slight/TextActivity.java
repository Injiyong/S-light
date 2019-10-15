package cau.injiyong.slight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class TextActivity extends AppCompatActivity {

    private static final String TAG = "TextActivity";

    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;

    TextView tvMessage;
    EditText etNewMessage;
    Button btUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        mAuth = FirebaseAuth.getInstance();

        tvMessage = (TextView) findViewById(R.id.tv_message);
        etNewMessage = (EditText) findViewById(R.id.et_newData);
        btUpdate = (Button) findViewById(R.id.bt_update);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("userInfo");

        //List 버튼 클릭시 list 창으로 넘어감
        Button btnNext= findViewById(R.id.bt_list);
        btnNext.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v ){
                Intent intent=new Intent(getApplicationContext(),MemoList.class);
                startActivity(intent);
            }
        });


        //버튼 이벤트
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userID = mAuth.getUid();
                String newMessage = etNewMessage.getText().toString();

                myRef.child(userID).child("memo_list").push().setValue(newMessage);
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
    }
}