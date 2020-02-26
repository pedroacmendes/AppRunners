package com.example.runners;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button btn_entrar;
    private EditText edt_user;
    private EditText edt_pass;
    private TextView txt_registar;
    private TextView txt_recuperaPass;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_entrar = findViewById(R.id.btn_entrar);
        edt_user = findViewById(R.id.edt_user);
        edt_pass = findViewById(R.id.edt_pass);
        txt_registar = findViewById(R.id.txt_registar);
        txt_recuperaPass = findViewById(R.id.txt_recuperaPass);

        edt_user.setText("pedroacmendes@hotmail.com");
        edt_pass.setText("123456789");

        txt_registar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegistoActivity.class);
                startActivity(intent);
            }
        });

        btn_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edt_user.getText().toString().trim();
                String password = edt_pass.getText().toString().trim();
                login(email,password);
            }
        });

        txt_recuperaPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
            }
        });

    }

    private void login(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Login efectuado com sucesso.",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "Erro de login",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Conection.getFirebaseAuth();
    }
}
