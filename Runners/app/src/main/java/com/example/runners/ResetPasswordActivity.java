package com.example.runners;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText editEmail;
    private Button btnResetPassword;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        editEmail = (EditText) findViewById(R.id.edt_email);
        btnResetPassword = (Button) findViewById(R.id.btn_resetPass);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString().trim();
                resetPassword(email);
            }
        });
    }

    private void resetPassword(String email) {

        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(ResetPasswordActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(ResetPasswordActivity.this, "Um email foi enviado", Toast.LENGTH_SHORT).show();
                            finish();
                        } else{
                            Toast.makeText(ResetPasswordActivity.this, "Email não registado", Toast.LENGTH_SHORT).show();
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
