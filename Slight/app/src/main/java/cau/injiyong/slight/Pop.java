package cau.injiyong.slight;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

public class Pop extends Dialog {


    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth mAuth;
    String userID;
    Query findItem;

    Pop m_oDialog;
    TextItem item;
    Button eBtn;
    Button dBtn;
    Button xBtn;

    Context mContext;
    ProgressDialog progressDialog;

    public Pop(Context context, TextItem item){
        super(context,android.R.style.Theme_Translucent_NoTitleBar);
        this.item=item;
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("분석중....");
        progressDialog.setCancelable(true);
        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Horizontal);

        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.5f;
        getWindow().setAttributes(lpWindow);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getUid();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("userInfo");

        setContentView(R.layout.memo_dialog);

        m_oDialog = this;

        TextView oView = (TextView) this.findViewById(R.id.dialog_text);
        oView.setText(item.getText());

        eBtn = (Button)this.findViewById(R.id.btnExe);
        eBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onClickExecute(v);
            }
        });

        dBtn = (Button)this.findViewById(R.id.btnDel);
        dBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onClickDelete(v);
            }
        });

        xBtn = (Button)this.findViewById(R.id.xbtn);
        xBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onClickX(v);
            }
        });





    }

    public void onClickX(View v)
    {
        this.dismiss();
    }

    public void onClickExecute(View v)
    {
        progressDialog.show();

        int cColor=Integer.parseInt(item.getColor());
        MainActivity.relative_color.setBackgroundColor(cColor);

        progressDialog.dismiss();

        MainActivity.lightOn(cColor);
        this.dismiss();
        ((Activity)mContext).finish();
    }

    public void onClickDelete(View v){
        findItem=myRef.child(userID).child("memo_list").orderByChild("date").equalTo(item.getDate());

        findItem.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot textSnapshot: dataSnapshot.getChildren()) {
                    textSnapshot.getRef().removeValue();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        ((MemoList)MemoList.mContext).refresh();
        Toast.makeText(getContext(),"삭제가 완료되었습니다.",Toast.LENGTH_SHORT).show();
        this.dismiss();

    }


}