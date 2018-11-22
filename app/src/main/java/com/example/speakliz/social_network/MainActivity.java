package com.example.speakliz.social_network;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.signin.SignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.firebase.ui.auth.viewmodel.RequestCodes.GOOGLE_PROVIDER;

public class MainActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    EditText usuario,contraseña;
    Button Login, Sign,fab;
    SignInButton Google;


    private FirebaseAuth Auth;
    private static int SIGN_IN_REQUEST_CODE=1;
    private FirebaseListAdapter<ChatMessagges> adapter;
    RelativeLayout activity_main;
    private static final String TAG="MainActivity";
    public static final String ANONYMOUS="anonymous";
    public static  final int DEFAULT_MSG_LIMIT=1000;

    private String mUserName;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEvenListener;
    private FirebaseAuth nFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private  static  final  int  RC_SIGN_IN  =  1;

    private GoogleSignInClient mGoogleSignInClient;










    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usuario = (EditText) findViewById(R.id.editText_LoginUsuario);
        contraseña = (EditText) findViewById(R.id.editText_LoginContraseña);
        Login = (Button) findViewById(R.id.button_login);
        Sign = (Button) findViewById(R.id.button_Sign);
        fab = (Button) findViewById(R.id.fab);
        //Google=(Button)findViewById(R.id.button_Google);
        //Login.setEnabled(false);
        usuario.addTextChangedListener(enableLogin);
        contraseña.addTextChangedListener(enableLogin);
        //activity_main=(RelativeLayout)findViewById(R.id.activity_main);
        Google=(SignInButton)findViewById(R.id.sign_in_button);
        progressDialog = new ProgressDialog(MainActivity.this);



        nFirebaseAuth = FirebaseAuth.getInstance();
        //fab=(FloatingActionButton)findViewById(R.id.fab);
        mUserName=ANONYMOUS;
        Auth=FirebaseAuth.getInstance();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();



        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();


            }
        });



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
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        progressDialog.show();
        Auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = Auth.getCurrentUser();
                            updateUI(user);


                            Intent i= new Intent(getApplicationContext(),LoginActivity.class);

                            startActivity(i);
                            progressDialog.dismiss();



                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this,"no se pudo ingresar con google",Toast.LENGTH_SHORT).show();
                            
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
            Toast.makeText(this,"Nombre de usuario: "+personName+"id de usuario: "+personId,Toast.LENGTH_LONG).show();
        }

    }


    //--------------------------------------------------------------------------------------------------------

    private TextWatcher enableLogin=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String usernameimput=usuario.getText().toString().trim();
            String passwordinput=contraseña.getText().toString().trim();
            Login.setEnabled(!usernameimput.isEmpty() && !passwordinput.isEmpty());



        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

//--------------------------------------------------------------------------------------------------------


    public void OpenRegister(View v){
        Intent i= new Intent(this,SignActivity.class);
        startActivity(i);
//        switch (v.getId()){
//            case R.id.button_login:
//                loguearUsuario();
//
//
//            case R.id.button_Sign:
//                Intent i= new Intent(this,SignActivity.class);
//                startActivity(i);
//                break;


        }





    public void loguearUsuario(View v) {
        nFirebaseAuth = FirebaseAuth.getInstance();

        //Obtenemos el email y la contraseÒa desde las cajas de texto
        String email1 = usuario.getText().toString().trim();
        String password1 = contraseña.getText().toString().trim();



        //Verificamos que las cajas de texto no esten vacÌas
        if (TextUtils.isEmpty(email1)) {
            Toast.makeText(this, "Se debe ingresar un email", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password1)) {
            Toast.makeText(this, "Falta ingresar la contraseÒa", Toast.LENGTH_LONG).show();
            //return;
        }



        //progressDialog.setMessage("registrando en linea ...");
        //progressDialog.show();


        //creating a new user
        nFirebaseAuth.signInWithEmailAndPassword(email1, password1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success

                        if (task.isSuccessful()) {

                            Toast.makeText(MainActivity.this, "Bienvenido al chat" , Toast.LENGTH_LONG).show();


                            Intent intento=new Intent(getApplication(),LoginActivity.class);
                            startActivity(intento);

                        } else {
                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
                                Toast.makeText(MainActivity.this, "Este usuario ya existe ", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(MainActivity.this, "No se pudo ingresar el usuario ", Toast.LENGTH_LONG).show();
                            }
                        }
//                        if (task.isSuccessful()) {
//
//                            Toast.makeText(SignActivity.this, "Se ha registrado el usuario con el email: " + EnterUser.getText(), Toast.LENGTH_LONG).show();
//                            Intent intento=new Intent(getApplication(),LoginActivity.class);
//                            startActivity(intento);
//                        } else {
//                            Toast.makeText(SignActivity.this, "No se pudo registrar el usuario ", Toast.LENGTH_LONG).show();
//                        }
                       // progressDialog.dismiss();
                    }
                });
    }

//--------------------------------------------------------------------------------------------------------
    public void OpenLogin1(View v){
        Intent i= new Intent(this,LoginActivity.class);
        startActivity(i);


    }


}




//--------------------------------------------------------------------------------------------------------
