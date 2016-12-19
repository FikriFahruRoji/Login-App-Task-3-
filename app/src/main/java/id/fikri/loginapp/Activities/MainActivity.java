package id.fikri.loginapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import id.fikri.loginapp.API.UserAPI;
import id.fikri.loginapp.Controlers.Users;
import id.fikri.loginapp.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    TextView tv_email, tv_password;
    Button btn_login;

    Gson gson = new GsonBuilder().create();
    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://private-7bb04d-signandlogin.apiary-mock.com/users/").addConverterFactory(GsonConverterFactory.create(gson)).build();
    UserAPI user_api = retrofit.create(UserAPI.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_email = (TextView)findViewById(R.id.edit_email);
        tv_password = (TextView)findViewById(R.id.edit_password);
        btn_login = (Button)findViewById(R.id.button_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    public void login(){
        Call<Users> call = user_api.getUsers();
        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                boolean login = false;
                String email = tv_email.getText().toString();
                String password = tv_password.getText().toString();

                for(Users.UserItem user : response.body().getUsers()) {
                    String getEmail = user.getEmail();
                    String getPassword = user.getPassword();

                    if (email.equals(getEmail) && password.equals(getPassword)) {
                        login = true;
                    }
                }

                if (login == true) {
                    Intent i = new Intent(MainActivity.this, WelcomeActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast toast = Toast.makeText(MainActivity.this, "fail to login", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {

            }
        });
    }
}