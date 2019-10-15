package cau.injiyong.slight;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.ApiErrorCode;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 10;
    //    private SessionCallback sessionCallback;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        SignInButton button = (SignInButton)findViewById(R.id.login_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
//        sessionCallback = new SessionCallback();
//        Session.getCurrentSession().addCallback(sessionCallback);
//        Session.getCurrentSession().checkAndImplicitOpen();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                //Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        //Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //유저 정보 디비에 넘기려고 추가한 부분
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userID = mAuth.getUid();
                            String tokenID=FirebaseInstanceId.getInstance().getToken();
                            database = FirebaseDatabase.getInstance();
                            myRef = database.getReference("userInfo");


                            if(!TextUtils.isEmpty(userID)){
                                UserData UserData=new UserData();
                                UserData.firebasekey=tokenID;
                                UserData.userID=userID;
                                myRef.child(userID).setValue(UserData);

                            }

                            Toast.makeText(LoginActivity.this, "ID 생성완료", Toast.LENGTH_SHORT).show();
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success")
                            //  updateUI(user);


                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(findViewById(R.id.main_layout), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
//            super.onActivityResult(requestCode, resultCode, data);
//            return;
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Session.getCurrentSession().removeCallback(sessionCallback);
//    }
//
//    private class SessionCallback implements ISessionCallback {
//        @Override
//        public void onSessionOpened() {
//            UserManagement.getInstance().me(new MeV2ResponseCallback() {
//                @Override
//                public void onFailure(ErrorResult errorResult) {
//                    int result = errorResult.getErrorCode();
//
//                    if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
//                        Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
//                        finish();
//                    } else {
//                        Toast.makeText(getApplicationContext(),"로그인 도중 오류가 발생했습니다: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onSessionClosed(ErrorResult errorResult) {
//                    Toast.makeText(getApplicationContext(),"세션이 닫혔습니다. 다시 시도해 주세요: "+errorResult.getErrorMessage(),Toast.LENGTH_SHORT).show();
//                }
//
//                @Override
//                public void onSuccess(MeV2Response result) {
//                    String needsScopeAutority = ""; // 정보 제공이 허용되지 않은 항목의 이름을 저장하는 변수
//
//                    // 이메일, 성별, 연령대, 생일 정보를 제공하는 것에 동의했는지 체크
//                    if(result.getKakaoAccount().needsScopeAccountEmail()) {
//                        needsScopeAutority = needsScopeAutority + "이메일";
//                    }
//                    if(result.getKakaoAccount().needsScopeGender()) {
//                        needsScopeAutority = needsScopeAutority + ", 성별";
//                    }
//                    if(result.getKakaoAccount().needsScopeAgeRange()) {
//                        needsScopeAutority = needsScopeAutority + ", 연령대";
//                    }
//                    if(result.getKakaoAccount().needsScopeBirthday()) {
//                        needsScopeAutority = needsScopeAutority + ", 생일";
//                    }
//
//                    if(needsScopeAutority.length() != 0) { // 정보 제공이 허용되지 않은 항목이 있다면 -> 허용되지 않은 항목을 안내하고 회원탈퇴 처리
//                        if(needsScopeAutority.charAt(0) == ',') {
//                            needsScopeAutority = needsScopeAutority.substring(2);
//                        }
//                        Toast.makeText(getApplicationContext(), needsScopeAutority+"에 대한 권한이 허용되지 않았습니다. 개인정보 제공에 동의해주세요.", Toast.LENGTH_SHORT).show(); // 개인정보 제공에 동의해달라는 Toast 메세지 띄움
//
//                        // 회원탈퇴 처리
//                        UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
//                            @Override
//                            public void onFailure(ErrorResult errorResult) {
//                                int result = errorResult.getErrorCode();
//
//                                if(result == ApiErrorCode.CLIENT_ERROR_CODE) {
//                                    Toast.makeText(getApplicationContext(), "네트워크 연결이 불안정합니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
//                                } else {
//                                    Toast.makeText(getApplicationContext(), "오류가 발생했습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//
//                            @Override
//                            public void onSessionClosed(ErrorResult errorResult) {
//                                Toast.makeText(getApplicationContext(), "로그인 세션이 닫혔습니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onNotSignedUp() {
//                                Toast.makeText(getApplicationContext(), "가입되지 않은 계정입니다. 다시 로그인해 주세요.", Toast.LENGTH_SHORT).show();
//                            }
//
//                            @Override
//                            public void onSuccess(Long result) { }
//                        });
//
//                    } else { // 모든 항목에 동의했다면 -> 유저 정보를 가져와서 MainActivity에 전달하고 MainActivity 실행.
//                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                        intent.putExtra("name", result.getNickname());
//                        intent.putExtra("profile", result.getProfileImagePath());
//
//                        if (result.getKakaoAccount().hasEmail() == OptionalBoolean.TRUE)
//                            intent.putExtra("email", result.getKakaoAccount().getEmail());
//                        else
//                            intent.putExtra("email", "none");
//                        if (result.getKakaoAccount().hasAgeRange() == OptionalBoolean.TRUE)
//                            intent.putExtra("ageRange", result.getKakaoAccount().getAgeRange().getValue());
//                        else
//                            intent.putExtra("ageRange", "none");
//                        if (result.getKakaoAccount().hasGender() == OptionalBoolean.TRUE)
//                            intent.putExtra("gender", result.getKakaoAccount().getGender().getValue());
//                        else
//                            intent.putExtra("gender", "none");
//                        if (result.getKakaoAccount().hasBirthday() == OptionalBoolean.TRUE)
//                            intent.putExtra("birthday", result.getKakaoAccount().getBirthday());
//                        else
//                            intent.putExtra("birthday", "none");
//
//
//                        startActivity(intent);
//                        finish();
//                    }
//                }
//            });
//        }
//
//        @Override
//        public void onSessionOpenFailed(KakaoException e) {
//            Toast.makeText(getApplicationContext(), "로그인 도중 오류가 발생했습니다. 인터넷 연결을 확인해주세요: "+e.toString(), Toast.LENGTH_SHORT).show();
//        }
//    }
}