package com.example.speakliz.social_network;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.Registrar;

public class SignActivity extends AppCompatActivity {
    EditText EnterUser, EnterPassword, Repeatpassword, EnterAge;
    Button registrar,verificar;
    TextView info;

    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    public static final String ANONYMOUS = "anonymous";
    public static final int DEFAULT_MSG_LIMIT = 1000;
    private String mUserName;
    private FirebaseAuth mFirebaseAuth;
    private static final String TAG = SignActivity.class.getName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        EnterUser = (EditText) findViewById(R.id.editText_EnterUser);
        EnterPassword = (EditText) findViewById(R.id.editText_EnterPassword);
        Repeatpassword = (EditText) findViewById(R.id.editText_RepeatPassword);
        registrar = (Button) findViewById(R.id.button_Sign);
        EnterAge = (EditText) findViewById(R.id.editText_Age);
        verificar=(Button)findViewById(R.id.button_verificar) ;
        info=(TextView)findViewById(R.id.textView_info) ;

        String email = EnterUser.getText().toString();
        String password = EnterPassword.getText().toString();

        verificar.setEnabled(false);

        mUserName = ANONYMOUS;

        mFirebaseAuth = FirebaseAuth.getInstance();




        progressDialog = new ProgressDialog(this);
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario();
            }
        });


        verificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        verification();
                    }
                });
            }
        });


    }


//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
    //REGISTRAR USUARIO Y UNIR EN FIREBASE
    private void registrarUsuario() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

       // String id, email1;
        //id=user.getUid().toString();
        //email1=user.getEmail().toString();



        //Obtenemos el email y la contraseÒa desde las cajas de texto
        String email = EnterUser.getText().toString().trim();
        String password = EnterPassword.getText().toString().trim();
        String password1=Repeatpassword.getText().toString().trim();


        //Verificamos que las cajas de texto no esten vacÌas
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Se debe ingresar un email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Falta ingresar la contraseÒa", Toast.LENGTH_LONG).show();
            //return;
        }

        //registrar.setEnabled(false);
        //verificar.setEnabled(true);

        //creating a new user
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success

                        if (task.isSuccessful()) {
                            Toast.makeText(SignActivity.this, "Se ha enviado un email de verificacion: " + EnterUser.getText(), Toast.LENGTH_LONG).show();
                            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull final Task<Void> task) {
                                    //registrar.setEnabled(false);
                                    verificar.setEnabled(true);

                                }
                            });

                            //Intent intento=new Intent(getApplication(),LoginActivity.class);
                            //startActivity(intento);
                        } else {
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(SignActivity.this, "Este usuario ya existe ", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(SignActivity.this, "No se pudo registrar el usuario ", Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                });



    }

    public void verification() {
        final Boolean email_verification;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        email_verification=user.isEmailVerified();
        info.setText(email_verification.toString());

        if(email_verification==true){
            Intent intento=new Intent(getApplication(),LoginActivity.class);
            startActivity(intento);


        }else{
            Toast.makeText(SignActivity.this, "Verifique el link de confirmacion", Toast.LENGTH_LONG).show();
        }

    }

    private void setInfo(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String id, email;
        id=user.getUid().toString();
        email=user.getEmail().toString();
        Boolean email_verification;


    }

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------


}