package com.example.mypantry.Profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.mypantry.R;
import android.app.ProgressDialog;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String onlineUserID;
    private ProgressDialog loader;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        loader = new ProgressDialog(getActivity());
        mUser = mAuth.getCurrentUser();
        onlineUserID = mUser.getUid();

        TextView changeEmail = view.findViewById(R.id.mEmail);
        EditText changePass = view.findViewById(R.id.mNewPass);
        EditText confirmPass = view.findViewById(R.id.mConfirmPass);

        changeEmail.setText("Email: " + mUser.getEmail());

        changePass.setText("");
        confirmPass.setText("");

        Button btnLogout = view.findViewById(R.id.btnLogOut);
        Button btnUpdate = view.findViewById(R.id.btnUpdate);
        Button btnDeleteAccount = view.findViewById(R.id.btnDeleteAccount);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(changePass.getText().toString().trim()).equals(confirmPass.getText().toString().trim())){
                    String error = "Error: Passwords do not match";
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                }
                else if ((changePass.getText().toString().trim()).equals("")){
                    String error = "Error: Password cannot be empty";
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                }
                else if ((confirmPass.getText().toString().trim()).equals("")){
                    String error = "Error: Please confirm new password";
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                }
                else{
                    mUser.updatePassword(changePass.getText().toString().trim());
                    Toast.makeText(getActivity(), "Password has been changed successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(true);
                builder.setTitle("Delete Confirmation");
                builder.setMessage("Are you sure you want to delete your account?");
                builder.setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAuth.getCurrentUser().delete();
                                mAuth.signOut();
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
    }
}
