package com.example.mypantry.Profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mypantry.MainActivity;
import com.example.mypantry.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private EditText RegEmail, RegPwd, GroupId;
    private Button RegBtn;
    private TextView RegQn;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private DatabaseReference reference2;

    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();
        loader = new ProgressDialog(this);

        RegEmail = findViewById(R.id.RegistrationEmail);
        RegPwd = findViewById(R.id.RegistrationPassword);
        RegBtn = findViewById(R.id.RegistrationButton);
        RegQn = findViewById(R.id.RegistrationPageQuestion);
        GroupId = findViewById(R.id.GroupId);

        RegQn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        RegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = RegEmail.getText().toString().trim();
                String password = RegPwd.getText().toString().trim();
                String Groupid = GroupId.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    RegEmail.setError("A valid email is required");
                    return;
                }
                if (TextUtils.isEmpty(Groupid)){
                    GroupId.setError("A valid Group is required");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    RegPwd.setError("A valid password is required");
                    return;

                } else{
                    loader.setMessage("Registration in progress");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

                                // Obtener referencias a la base de datos
                                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
                                DatabaseReference usersRef = databaseRef.child("users");

                                // Obtener datos relevantes
                                String email = currentUser.getEmail(); // Correo electrónico del usuario actual
                                String userId = currentUser.getUid(); // ID de usuario del usuario actual
                                String groupId = Groupid; // ID del grupo obtenido del EditText

                                // Crear un nuevo nodo para el usuario actual
                                DatabaseReference userRef = usersRef.child(userId);

                                // Guardar los datos del usuario actual en el nodo correspondiente
                                userRef.child("email").setValue(email);
                                userRef.child("groupId").setValue(groupId);

                                Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else{
                                String error = task.getException().toString();
                                Toast.makeText(RegistrationActivity.this, "Registration failed: " + error, Toast.LENGTH_SHORT).show();
                            }
                            loader.dismiss();
                        }
                    });
                }
            }
        });
    }
}