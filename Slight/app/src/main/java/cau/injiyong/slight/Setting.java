package cau.injiyong.slight;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;


public class Setting extends AppCompatActivity {

    Button button4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        button4=(Button) findViewById(R.id.exit);

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                finish();                                                                                           //finish() 현재 엑티비티(화면)을 종료하는 메소드이다.
            }
        });
    }

}
