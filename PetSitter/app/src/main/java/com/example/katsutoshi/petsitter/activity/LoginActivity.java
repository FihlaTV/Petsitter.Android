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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText user, password;
    private Button btnlogin;
    private TextView btnforgotPass, btnsignup;

    private RelativeLayout loginActivity;

    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = (EditText)findViewById(R.id.txtLogin);
        password = (EditText)findViewById(R.id.txtPassword);

        btnlogin =(Button)findViewById(R.id.btnLogin);
        btnforgotPass = (TextView)findViewById(R.id.forgotPassword);
        btnsignup = (TextView)findViewById(R.id.signUp);

        loginActivity = (RelativeLayout)findViewById(R.id.loginActivity);

        btnlogin.setOnClickListener(this);
        btnforgotPass.setOnClickListener(this);
        btnsignup.setOnClickListener(this);


        //initiate firebase auth

        auth = FirebaseAuth.getInstance();

        //check session
        if(auth.getCurrentUser() != null)
        {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra("uid", auth.getCurrentUser().getUid());
            intent.putExtra("user", auth.getCurrentUser().getEmail());
            startActivity(intent);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btnLogin:
                loginUser(user.getText().toString(), password.getText().toString());
                if(!(user.getText().toString().isEmpty() || password.getText().toString().isEmpty())) {
                    loginUser(user.getText().toString(), password.getText().toString());
                }
                else
                {
                    Snackbar snackbar = Snackbar.make(loginActivity, "Email e/ou Senha não informado.", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                break;
            case R.id.forgotPassword:
                resetPassword(user.getText().toString());
                break;

            case R.id.signUp:
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                finish();
                break;

        }
    }

    @Override
    public void onBackPressed() {
            moveTaskToBack(true);
    }
    private void resetPassword(String email) {
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Snackbar snackbar = Snackbar.make(loginActivity, "O email para resetar sua senha foi enviado!", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                        else
                        {
                            Snackbar snackbar = Snackbar.make(loginActivity, "Falha ao enviar o email!", Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    }
                });
    }

    private void loginUser(String email, final String password) {

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful())
                        {
                            if(password.length() < 6)
                            {
                                Snackbar snackbar = Snackbar.make(loginActivity, "Senha deve ter pelo menos 6 caracteres!", Snackbar.LENGTH_SHORT);
                                snackbar.show();
                            }
                        }
                        else
                        {
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.putExtra("uid", auth.getCurrentUser().getUid());
                            intent.putExtra("user", auth.getCurrentUser().getEmail());
                            startActivity(intent);
                        }
                    }
                });
    }
}
