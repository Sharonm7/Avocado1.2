package com.example.avocado1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginPage extends BaseActivity {


    TextView signUp;
    User user;
     private FirebaseAuth mAuth;
     EditText editTextPassword, editTextEmail;
     Button loginBtn;
     ProgressBar progressBar;
    DatabaseReference myRef;
    private static final String TAG= LoginPage.class.getName();


     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        signUp= findViewById(R.id.signUpId);
         user= new User();
         editTextEmail = findViewById(R.id.emailId);
         editTextPassword = findViewById(R.id.passId);
         loginBtn= findViewById(R.id.loginId);
         progressBar= findViewById(R.id.progressBarId);
         mAuth= FirebaseAuth.getInstance();

         editTextEmail.setText("aa@g.com");
         editTextPassword.setText("123456");

         signUp.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent myIntent = new Intent(view.getContext(), RegisterPage.class);
               startActivityForResult(myIntent, 0);
           }
       });


         loginBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 progressBar.setVisibility(View.VISIBLE);

                 String Email = editTextEmail.getText().toString().trim();
                 String Password= editTextPassword.getText().toString().trim();

                 if (Email.isEmpty()) {
                     editTextEmail.setError("Email is required");
                     editTextEmail.requestFocus();
                     return;
                 }

                 if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                     editTextEmail.setError("Please enter a valid email");
                     editTextEmail.requestFocus();
                     return;
                 }

                 if (Password.isEmpty()) {
                     editTextPassword.setError("Password is required");
                     editTextPassword.requestFocus();
                     return;
                 }

                 if (Password.length() < 6) {
                     editTextPassword.setError("Minimum length of password should be 6");
                     editTextPassword.requestFocus();
                     return;
                 }

                 mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         if (task.isSuccessful()){
                             Log.d(TAG, "success");
                             FirebaseUser currentUser = mAuth.getCurrentUser();

                             String userEmail;
                             userEmail=currentUser.getEmail();

                             myRef= FirebaseDatabase.getInstance().getReference("Users");

                             myRef.addValueEventListener(new ValueEventListener() {
                                 @Override
                                 public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                     }
                                     @Override
                                     public void onCancelled(@NonNull DatabaseError databaseError) {
                                         Toast.makeText(LoginPage.this, "failed to read"+ databaseError.toException(),Toast.LENGTH_SHORT).show();
                                     }
                             });

                             Toast.makeText(LoginPage.this,"Hello "+userEmail,Toast.LENGTH_LONG).show();


                             if(currentUser != null)
                             {
                                 Intent intent = new Intent(LoginPage.this, HomePage.class);
                                 startActivity(intent);
                             }
                         }
                         else {
                             Toast.makeText(LoginPage.this, "Authentication failed."+ task.getException().getMessage(),
                                     Toast.LENGTH_SHORT).show();

                         }

                     }
                 });



             }
         });





            }



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

       // {
        //    Intent intent = new Intent(LoginPage.this, HomePage.class);
     //       startActivity(intent);
      //  }

    }
    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(LoginPage.this, LoginPage.class);
        startActivity(intent);
        finish();

    }


}



