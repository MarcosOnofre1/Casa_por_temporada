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

public class CriarContaActivity extends AppCompatActivity {

    private EditText edit_nome;
    private EditText edit_email;
    private EditText edit_telefone;
    private EditText edit_senha;
    private EditText edit_confirma_senha;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_conta);

        configCliques();

        iniciaComponentes();
    }

    private void configCliques(){
        findViewById(R.id.btn_voltar).setOnClickListener(view -> finish());


    }

    public void validaDados(View view){

        String nome = edit_nome.getText().toString();
        String email = edit_email.getText().toString();
        String telefone = edit_telefone.getText().toString();
        String senha = edit_senha.getText().toString();
        String confirmaSenha = edit_confirma_senha.getText().toString();

        if (!nome.isEmpty()){
            if (!email.isEmpty()){
                if (!telefone.isEmpty()){
                    if (!senha.isEmpty()){
                        if (!confirmaSenha.isEmpty()){
                            if (senha.equals(confirmaSenha)){

                                progressBar.setVisibility(View.VISIBLE);
                                ocutarTeclado();

                                Usuario usuario = new Usuario();
                                usuario.setNome(nome);
                                usuario.setEmail(email);
                                usuario.setTelefone(telefone);
                                usuario.setSenha(senha);

                                cadastrarUsuario(usuario);




                            }else {
                                edit_senha.setError("Senhas diferentes");
                                edit_confirma_senha.setError("Senhas diferentes");
                            }
                        }else {
                            edit_confirma_senha.requestFocus();
                            edit_confirma_senha.setError("Confirme uma senha.");
                        }
                    }else {
                        edit_senha.requestFocus();
                        edit_senha.setError("Informe uma senha.");
                    }
                }else {
                    edit_telefone.requestFocus();
                    edit_telefone.setError("Informe seu telefone.");
                }
            }else {
                edit_email.requestFocus();
                edit_email.setError("Informe seu e-email.");
            }

        }else{
            edit_nome.requestFocus();
            edit_nome.setError("Informe seu nome.");
        }



    }

    private void cadastrarUsuario(Usuario usuario){
        FirebaseHelper.getAuth().createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){

                        // cria um novo usuario e fecha a activity no finish(); e abre a tela principal em seguida.
                        String idUser = task.getResult().getUser().getUid();
                        usuario.setId(idUser);
                        usuario.salvar();
                        finish();

                        startActivity(new Intent(this, MainActivity.class));

                    }else {
                        String error = task.getException().getMessage();
                        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                    }
        });
    }

    private void ocutarTeclado() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(edit_nome.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void iniciaComponentes(){

        edit_nome = findViewById(R.id.edit_nome);
        edit_email = findViewById(R.id.edit_email);
        edit_telefone = findViewById(R.id.edit_telefone);
        edit_senha = findViewById(R.id.edit_senha);
        edit_confirma_senha = findViewById(R.id.edit_confirma_senha);
        progressBar = findViewById(R.id.progressBar);

        TextView text_titulo = findViewById(R.id.text_titulo_anuncio);
        text_titulo.setText("Crie sua conta");
    }
}