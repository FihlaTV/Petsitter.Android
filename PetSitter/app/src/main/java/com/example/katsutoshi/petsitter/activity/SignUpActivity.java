package com.example.katsutoshi.petsitter.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.katsutoshi.petsitter.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText login, password;

    private Button btnSignUp;

    private TextView btnLogin;

    private FirebaseAuth auth;

    private RelativeLayout signUpActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        login = (EditText) findViewById(R.id.txtSignUpLogin);
        password = (EditText)findViewById(R.id.txtSignUpPassword);

        btnSignUp = (Button)findViewById(R.id.btnSignUp);

        btnLogin = (TextView)findViewById(R.id.login);

        btnLogin.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

        signUpActivity = (RelativeLayout)findViewById(R.id.signUpActivity);
        //initialize firebase auth
        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnSignUp:
                if(!(login.getText().toString().isEmpty() || password.getText().toString().isEmpty())) {
                    signUpUser(login.getText().toString(), password.getText().toString());
                }
                else
                {
                    Snackbar snackbar = Snackbar.make(signUpActivity, "Email e/ou Senha não informado.", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                break;
            case R.id.login:
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }

    private void signUpUser(String email, String password) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                   if(!task.isSuccessful())
                   {
                       Snackbar snackbar = Snackbar.make(signUpActivity, "Erro:" + task.getException(), Snackbar.LENGTH_SHORT);
                       snackbar.show();
                   }
                   else
                   {

                       Snackbar snackbar = Snackbar.make(signUpActivity, "Registro com sucesso", Snackbar.LENGTH_SHORT);
                       snackbar.show();
                   }
                    }
                });
    }
}
