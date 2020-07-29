package com.example.drfind.Vista.paciente;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.drfind.R;
import com.example.drfind.modelo.conexion;

import java.sql.ResultSet;
import java.sql.Statement;

public class loginpac extends AppCompatActivity implements View.OnClickListener {
    Button loginpaciente, registropaciente;
    EditText usuariopac,passpac;
    CheckBox mantendapac;
    TextView relutlog;
    private SharedPreferences preferencespac;
    private SharedPreferences.Editor paceditor;
    String usernamepac, passwordmpac;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpac);
        loginpaciente=(Button)findViewById(R.id.btnloginpaciente);
        loginpaciente.setOnClickListener(this);
        relutlog=(TextView)findViewById(R.id.resulpaclog);
        registropaciente=(Button)findViewById(R.id.btnregispaciente);
        registropaciente.setOnClickListener(this);
        usuariopac=(EditText) findViewById(R.id.edtusuariopaci);
        passpac=(EditText)findViewById(R.id.edtpasspaci);
        mantendapac=(CheckBox)findViewById(R.id.checkBoxuserpac);
        preferencespac= PreferenceManager.getDefaultSharedPreferences(this);
        paceditor=preferencespac.edit();
        SharedPreference();
    }
public void SharedPreference(){
        String checkbox= preferencespac.getString(getString(R.string.checkBoxuserpac),"False");
        String name=preferencespac.getString(getString(R.string.edtusuariopaci),"");
    String password=preferencespac.getString(getString(R.string.edtpasspaci),"");
        usuariopac.setText(name);
        passpac.setText(password);
        if(checkbox.equals("True")){
            mantendapac.setChecked(true);
        }else {
            mantendapac.setChecked(false);
        }
}
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnloginpaciente:
                if(usuariopac.getText().toString().isEmpty()||passpac.getText().toString().isEmpty()){
                    relutlog.setText("uno o mas campos estan vacios");
                }else{
                    if(mantendapac.isChecked()){
                        paceditor.putString(getString(R.string.checkBoxuserpac),"True");
                        paceditor.commit();
                        String name=usuariopac.getText().toString();
                        paceditor.putString(getString(R.string.edtusuariopaci),name);
                        paceditor.commit();
                        String password=passpac.getText().toString();
                        paceditor.putString(getString(R.string.edtpasspaci),password);
                        paceditor.commit();

                    }else {
                        paceditor.putString(getString(R.string.checkBoxuserpac),"False");
                        paceditor.commit();

                        paceditor.putString(getString(R.string.edtusuariopaci),"");
                        paceditor.commit();

                        paceditor.putString(getString(R.string.edtpasspaci),"");
                        paceditor.commit();
                    }
                    usernamepac=usuariopac.getText().toString();
                    passwordmpac=passpac.getText().toString();
                    CheckLogin checkLogin=new CheckLogin();
                    checkLogin.execute("");

                }

                break;
            case R.id.btnregispaciente:
                Intent o = new Intent(this, regispaciente.class);
                startActivity(o);
                break;
        }
    }
    public class CheckLogin extends AsyncTask<String,String,String> {
        String z="";
        Boolean isSuccess=false;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(loginpac.this,s,Toast.LENGTH_SHORT).show();
            if(isSuccess){
                Toast.makeText(loginpac.this,"Inicio correcto",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            if(usernamepac.trim().equals("")||passwordmpac.trim().equals(""))
                z="Ingrese usuario y contraseña";
            else {
                conexion db=new conexion();
                try {
                    Statement stm=db.conexionbd().createStatement();
                    ResultSet resultSet=stm.executeQuery("SELECT * FROM Paciente where usuariopac= '"+usernamepac.toString()+"'and contraseñapac='"+passwordmpac.toString()+"'");
                    if(resultSet.next()){
                        z="Inicio exitoso";
                        isSuccess=true;
                        Intent i = new Intent(loginpac.this, menupaciente.class);
                        i.putExtra("usu",usernamepac.toString());
                        startActivity(i);
                    }else {
                        z="Usuario o contraseña incorrectos";
                        isSuccess=false;
                        Intent m = new Intent(loginpac.this, loginpac.class);
                        startActivity(m);
                    }
                }catch (Exception e){
                    isSuccess=false;
                    z=e.getMessage();
                }
            }return z;
        }
    }
}
