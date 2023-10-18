package com.example.casaportemporada.Autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.casaportemporada.R;
import com.example.casaportemporada.helper.FirebaseHelper;

public class RecuperarContaActivity extends AppCompatActivity {

    private EditText edit_recuperaEmail;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueceu_senha);

        iniciaComponentes();

        configToolbar();

        configCliques();
    }

    public void validaDados(View view){

        String email = edit_recuperaEmail.getText().toString();

        if (!email.isEmpty()){

            progressBar.setVisibility(View.VISIBLE);
            recuperaSenha(email);

        }else {
            edit_recuperaEmail.requestFocus();
            edit_recuperaEmail.setError("Informe seu e-mail.");
        }
    }

    private void recuperaSenha(String email){
        FirebaseHelper.getAuth().sendPasswordResetEmail(
                email
        ).addOnCompleteListener(task -> {

            if (task.isSuccessful()){

                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "E-mail enviado com sucesso!", Toast.LENGTH_SHORT).show();

                finish();
                startActivity(new Intent(this, LoginActivity.class));

            }else {

                Toast.makeText(this, "E-mail inválido, por favor, digite seu email corretamente.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configCliques(){
        findViewById(R.id.btn_voltar).setOnClickListener(view -> finish());

    }

    private void configToolbar(){
        TextView text_titulo = findViewById(R.id.text_titulo);
        text_titulo.setText("Recuperar conta");
    }

    private void iniciaComponentes(){

        edit_recuperaEmail = findViewById(R.id.edit_recuperaEmail);
        progressBar = findViewById(R.id.progressBar);

    }
}