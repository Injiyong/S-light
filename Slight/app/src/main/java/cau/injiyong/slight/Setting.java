package cau.injiyong.slight;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

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

    public void OnClickHandlerPurple(View view) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle("보라").setMessage("신중함을 요하는 상황이나 창의적인 아이디어가 필요할때, 혹은 터닝포인트가 필요할때 도움을 줍니다.");
        AlertDialog alertDialog = builder1.create();
        alertDialog.show();
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
}