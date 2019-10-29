package cau.injiyong.slight;

import androidx.appcompat.app.AppCompatActivity;

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

public class MemoList extends AppCompatActivity {

    private DatabaseReference mDatabase;
    String tokenID = FirebaseInstanceId.getInstance().getToken();
    static final String[] LIST_MENU = {"LIST1", "LIST2", "LIST3"};
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
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo_list);

        mUsername = ANONYMOUS;
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mMessageListView = (ListView) findViewById(R.id.messageListView);
//        mPhotoPickerButton = (ImageButton) findViewById(R.id.photoPickerButton);
//        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
//        mSendButton = (Button) findViewById(R.id.sendButton);

        FloatingActionButton btnWrite = findViewById(R.id.btnWrite);
        btnWrite.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v ){
                Intent intent=new Intent(getApplicationContext(),TextActivity.class);
                startActivity(intent);
            }
        });

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = mFirebaseDatabase.getReference().child("userInfo").child(mAuth.getUid()).child("memo_list"); // jj


        List<TextItem> friendlyMessages = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(this, R.layout.activity_text_item, friendlyMessages);
        mMessageListView.setAdapter(mMessageAdapter);

        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        // ImagePickerButton shows an image picker to upload a image for a message
//        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // TODO: Fire an intent to show an image picker
//            }
//        });

//        mMessageEditText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (charSequence.toString().trim().length() > 0) {
//                    mSendButton.setEnabled(true);
//                } else {
//                    mSendButton.setEnabled(false);
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//            }
//        });
//        mMessageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(DEFAULT_MSG_LENGTH_LIMIT)});

        // Send button sends a message and clears the EditText
//        mSendButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // TODO: Send messages on click
//                TextItem mFriendlyMessage = new TextItem(mMessageEditText.getText().toString(), mUsername, null); //jj
//                mDatabase.push().setValue(mFriendlyMessage); // 등록
//                // Clear input box
//                mMessageEditText.setText("");
//            }
//        });

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
} // end onCreate

  //      ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, LIST_MENU) ;

        //ListView listview = (ListView) findViewById(R.id.listview) ;
//        //listview.setAdapter(adapter) ;
//
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            // 코드 계속 ...
//
//            @Override
//            public void onItemClick(AdapterView parent, View v, int position, long id) {
//
//                // get TextView's Text.
//                String strText = (String) parent.getItemAtPosition(position) ;
//
//                // TODO : use strText
//            }
//        }) ;


//    }
