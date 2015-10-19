package ch.hsr.mge.priasu.gadgeothek;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import ch.hsr.mge.gadgeothek.service.Callback;
import ch.hsr.mge.gadgeothek.service.LibraryService;

public class RegisterActivity extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button registerButton = (Button) findViewById(R.id.mRegisterButton);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRegistration();
            }
        });
    }

    protected void sendRegistration(){
        EditText mPwd1 = (EditText) findViewById(R.id.regPwd1);
        String sPwd1 = mPwd1.getText().toString();
        EditText mPwd2 = (EditText) findViewById(R.id.regPwd2);
        String sPwd2 = mPwd2.getText().toString();
        EditText mMail = (EditText) findViewById(R.id.regMail);
        String sMail = mMail.getText().toString();
        EditText mName = (EditText) findViewById(R.id.regName);
        String sName = mName.getText().toString();
        final EditText mStudNr = (EditText) findViewById(R.id.regStudNr);
        String sStudNr = mStudNr.getText().toString();

        if (sPwd1.contentEquals(sPwd2)){
            LibraryService.register(sMail, sPwd1, sName, sStudNr, new Callback<Boolean>() {
                        @Override
                        public void onCompletion(Boolean input) {
                            if (input) {
                                // TODO Go to next Activity
                                Intent loginActivity = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(loginActivity);
                            } else {
                                // TODO Registration Failed
                                Intent loginActivity = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(loginActivity);
                            }
                        }

                        @Override
                        public void onError(String message) {
                            // TODO Show Error.. (Server unrechable etc.)
                            mStudNr.setError(message);
                            mStudNr.requestFocus();
                            //Intent registerActivity = new Intent(RegisterActivity.this, RegisterActivity.class);
                            //startActivity(registerActivity);

                        }
                    });

        } else{
            mPwd2.setError("Passwords are not the same");
            mPwd2.requestFocus();
        }
    }
}
