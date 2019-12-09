package cau.injiyong.slight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kakao.usermgmt.response.model.User;

import java.util.ArrayList;
import java.util.List;

public class MemoList extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private DatabaseReference mDatabase;
    String tokenID = FirebaseInstanceId.getInstance().getToken();
    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LENGTH_LIMIT = 1000;
    private FirebaseDatabase mFirebaseDatabase; // 데이터베이스에 접근할 수 있는 진입점 클래스입니다.
    private ChildEventListener mChildEventListener;
    private ListView mMessageListView;
    private MessageAdapter mMessageAdapter;
    private ProgressBar mProgressBar;
    private ImageButton mPhotoPickerButton;
    private EditText mMessageEditText;
    private Button mSendButton;
    private String mUsername;
    public static Context mContext;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadActivity();
    }

    private void loadActivity(){
        mContext=this;
        setContentView(R.layout.activity_memo_list);

        mUsername = ANONYMOUS;
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageListView = (ListView) findViewById(R.id.messageListView);

        FloatingActionButton btnWrite = findViewById(R.id.btnWrite);
        btnWrite.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v ){
                Intent intent=new Intent(getApplicationContext(),TextActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = mFirebaseDatabase.getReference().child("userInfo").child(mAuth.getUid()).child("memo_list");


        List<TextItem> friendlyMessages = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(this, R.layout.activity_text_item, friendlyMessages);
        mMessageListView.setAdapter(mMessageAdapter);
        mMessageListView.setOnItemClickListener(this);

        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        mChildEventListener = new ChildEventListener() {
            // 데이터가 추가 되었을 때.
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //dataSnapshot은 위 메소드가 호출 될때의 data이다.
                TextItem mFriendlyMessage = dataSnapshot.getValue(TextItem.class);
                mMessageAdapter.add(mFriendlyMessage);
            }

            //데이터가 변화되었을 때
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            //데이터가 제거되었을때
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {


            }

            // 데이터가 파이어베이스 DB 리스트 위치 변경되었을때
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            // DB처리중 오류가 발생했을 댸
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        mDatabase.addChildEventListener(mChildEventListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onItemClick(AdapterView<?> parent, View v, int position, long id)
    {
        TextItem item=(TextItem)parent.getItemAtPosition(position);
        Pop pop=new Pop(mContext,item);
        pop.show();


    }

    public void refresh(){
        this.finish();
        Intent intent = new Intent(MemoList.this, MemoList.class);
        startActivity(intent);

    }


}