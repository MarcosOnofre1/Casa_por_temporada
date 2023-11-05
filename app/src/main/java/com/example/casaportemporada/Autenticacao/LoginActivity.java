package com.example.casaportemporada.Autenticacao;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.casaportemporada.Activity.MainActivity;
import com.example.casaportemporada.Model.Usuario;
import com.example.casaportemporada.R;
import com.example.casaportemporada.helper.FirebaseHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText edit_email;
    private EditText edit_senha;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        configCliques();

        iniciaComponentes();


    }


    private void configCliques() {
        findViewById(R.id.text_criar_conta).setOnClickListener(view ->
                startActivity(new Intent(this, CriarContaActivity.class)));

        findViewById(R.id.text_recuperar_senha).setOnClickListener(view ->
                startActivity(new Intent(this, RecuperarContaActivity.class)));

    }

    public void validaDados(View view) {

        String email = edit_email.getText().toString();
        String senha = edit_senha.getText().toString();

        if (!email.isEmpty()) {
            if (!senha.isEmpty()) {

                progressBar.setVisibility(View.VISIBLE);
                ocutarTeclado();
                logar(email, senha);

            } else {
                edit_senha.requestFocus();
                edit_senha.setError("Informe uma senha.");
            }
        } else {
            edit_email.requestFocus();
            edit_email.setError("Informe seu e-email.");
        }

    }

    private void logar(String email, String senha) {
        FirebaseHelper.getAuth().signInWithEmailAndPassword(email, senha).addOnCompleteListener(task -> {
            if (task.isSuccessful()){

                finish();
                startActivity(new Intent(this, MainActivity.class));

            }else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Senha ou e-mail não cadastrado. Por favor, digite novamente.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void ocutarTeclado() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(edit_email.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void iniciaComponentes() {
        edit_email = findViewById(R.id.edit_email);
        edit_senha = findViewById(R.id.edit_senha);
        progressBar = findViewById(R.id.progressBar);
    }


}