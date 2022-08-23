package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout inputEmail,inputPassword,inputConformPassword;
    Button btnRegister;
    TextView alreadyHaveAcoount;
    FirebaseAuth mAuth;
    ProgressDialog mLoadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputEmail=findViewById(R.id.inputEmailrg);
        inputPassword=findViewById(R.id.inputPasswordrg);
        inputConformPassword=findViewById(R.id.inputConformPassword);
        btnRegister=findViewById(R.id.btnRegister);
        alreadyHaveAcoount=findViewById(R.id.AlraedyHaveanAccount);

        mAuth=  FirebaseAuth.getInstance();
        mLoadingBar=new ProgressDialog(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AtemptRegistertion();
            }
        });
        alreadyHaveAcoount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void AtemptRegistertion() {
        String email=inputEmail.getEditText().getText().toString();
        String password=inputPassword.getEditText().getText().toString();
        String conformPassword=inputConformPassword.getEditText().getText().toString();

        if(email.isEmpty() || !email.contains("@gmail.com"))
        {
            showError(inputEmail,"Email is not Valid");
        }
        else if(password.isEmpty() || password.length()<5)
        {
            showError(inputPassword,"Password must be greater than 5 latter");
        }
        else if(!conformPassword.equals(password))
        {
            showError(inputConformPassword,"Password did not Match");
        }
        else
        {
                mLoadingBar.setTitle("Registration");
                mLoadingBar.setMessage("Please Wait,While your Credential");
               mLoadingBar.setCanceledOnTouchOutside(false);
                mLoadingBar.show();
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            mLoadingBar.dismiss();
                            Toast.makeText(RegisterActivity.this,"Registration is Succesfull",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(RegisterActivity.this,SetupActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();

                        }
                        else
                        {
                            mLoadingBar.dismiss();
                            Toast.makeText(RegisterActivity.this,"Registration is Failed",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
    }

    private void showError(TextInputLayout field, String text) {
        field.setError(text);
        field.requestFocus();
    }


}