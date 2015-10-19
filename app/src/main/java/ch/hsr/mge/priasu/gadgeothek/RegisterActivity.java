package ch.hsr.mge.priasu.gadgeothek;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.EditText;

import ch.hsr.mge.gadgeothek.service.Callback;
import ch.hsr.mge.gadgeothek.service.LibraryService;

public class RegisterActivity extends Activity {

    // UI references
    private EditText mPwd1View;
    private EditText mPwd2View;
    private EditText mNameView;
    private EditText mStudNrView;
    private EditText mMailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mPwd1View = (EditText) findViewById(R.id.regPwd1);
        mPwd2View = (EditText) findViewById(R.id.regPwd2);
        mNameView = (EditText) findViewById(R.id.regName);
        mStudNrView = (EditText) findViewById(R.id.regStudNr);
        mMailView = (EditText) findViewById(R.id.regMail);

    }

    protected void sendRegistration(){
        if (mPwd1View==mPwd2View){
            LibraryService.register(mMailView.toString(), mPwd1View.toString(),
                    mNameView.toString(), mStudNrView.toString(), new Callback<Boolean>() {
                        @Override
                        public void onCompletion(Boolean input) {
                            if (input) {
                                // TODO Go to next Activity
                                Intent loginActivity = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(loginActivity);
                            } else {
                                // TODO Registration Failed
                            }
                        }

                        @Override
                        public void onError(String message) {
                            // TODO Show Error.. (Server unrechable etc.)

                        }
                    });

    }
}
}
