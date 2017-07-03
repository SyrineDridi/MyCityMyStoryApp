package com.example.android.pim.pimandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.pim.pimandroid.Handler.UserHandler;
import com.example.android.pim.pimandroid.entities.User;

import de.hdodenhof.circleimageview.CircleImageView;


public class Login extends AppCompatActivity {


    User user ;
    CircleImageView User_Image;
    EditText txtEmail , txtFirstName , txtLastName , txtTel ;
    Button BtnSignUp , BtnChooseImage ;
    UserHandler db_user ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db_user = new UserHandler(this);

        User_Image = (CircleImageView) findViewById(R.id.User_Image) ;
        txtEmail = (EditText) findViewById(R.id.txtEmail) ;
        txtFirstName = ( EditText) findViewById(R.id.txtFirstName);
        txtLastName = (EditText)findViewById(R.id.txtLastName);
        txtTel = (EditText)findViewById(R.id.txtTel);
        BtnChooseImage = (Button) findViewById(R.id.BtnChooseImage) ;
        BtnSignUp = (Button) findViewById(R.id.BtnSignUp) ;

        BtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String   email = txtEmail.getText().toString() ;
                String firstname = txtFirstName.getText().toString() ;
                String lastname = txtLastName.getText().toString();
                int tel = Integer.parseInt(txtTel.getText().toString()) ;
                String urlImage = "syrine just a try ";
                user = new User(1, email,firstname,lastname,urlImage,tel);
                Log.d("Insert: ", "Inserting ..");
                db_user.addUser(user);
                db_user.getUser(1);

            }
        });
    }
}
