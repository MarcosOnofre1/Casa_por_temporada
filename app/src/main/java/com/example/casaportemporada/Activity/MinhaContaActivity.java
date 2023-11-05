package com.example.casaportemporada.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.casaportemporada.Model.Usuario;
import com.example.casaportemporada.R;
import com.example.casaportemporada.helper.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class MinhaContaActivity extends AppCompatActivity {

    private static final int REQUEST_GALERIA = 100;

    private EditText edit_nome;
    private EditText edit_telefone;
    private EditText edit_email;
    private ImageView imagemPerfil;
    private String caminhoImagem;
    private ProgressBar progressBar;
    private Usuario usuario;

    private Bitmap imagem;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_conta);

        iniciaComponentes();

        recuperaDados();

        configCliques();


    }


    private void recuperaDados(){
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("usuarios")
                .child(FirebaseHelper.getIdFirebase());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usuario = snapshot.getValue(Usuario.class);

                configDados();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configCliques() {
        findViewById(R.id.ib_salvar).setOnClickListener(v -> validaDados());
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
    }

    private void configDados() {

        if(usuario.getUrlImagem() != null){
            Picasso.get().load(usuario.getUrlImagem())
                    .placeholder(R.drawable.loading)
                    .into(imagemPerfil);
        }

        edit_nome.setText(usuario.getNome());
        edit_telefone.setText(usuario.getTelefone());
        edit_email.setText(usuario.getEmail());

        progressBar.setVisibility(View.GONE);

    }

    public void configImagem(View view){
        imagemPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_GALERIA);
        });

    }

    private void validaDados(){

        String nome = edit_nome.getText().toString();
        String telefone = edit_telefone.getText().toString();

        if(!nome.isEmpty()){
            if(!telefone.isEmpty()){

                progressBar.setVisibility(View.VISIBLE);

                ocutarTeclado();

                usuario.setNome(nome);
                usuario.setTelefone(telefone);
                usuario.salvar();

                if (caminhoImagem != null){

                    salvarImagemFirebase();
                }

                progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Informações salvas com sucesso!", Toast.LENGTH_SHORT).show();

            }else {
                edit_telefone.requestFocus();
                edit_telefone.setError("Informe seu número");
            }

        }else{
            edit_nome.requestFocus();
            edit_nome.setError("Informe seu nome.");
        }

    }

    //fica salvo no Storage do firebase
    private void salvarImagemFirebase() {

        StorageReference storageReference = FirebaseHelper.getStorageReference()
                .child("imagens")
                .child("perfil")
                .child(FirebaseHelper.getIdFirebase() + ".JPEG");

        UploadTask uploadTask = storageReference.putFile(Uri.parse(caminhoImagem));
        uploadTask.addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnCompleteListener(task -> {

            usuario.setUrlImagem(task.getResult().toString());
            salvarDadosUsuario();

        })).addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void salvarDadosUsuario() {
        DatabaseReference usuarioRef = FirebaseHelper.getDatabaseReference()
                .child("usuarios")
                .child(usuario.getId());
        usuarioRef.setValue(usuario).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Informações salvas com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Não foi possível salvar as informações.", Toast.LENGTH_SHORT).show();
            }

            progressBar.setVisibility(View.GONE);
        });

    }

    private void ocutarTeclado() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(edit_nome.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void iniciaComponentes() {
        TextView text_titulo_anuncio = findViewById(R.id.text_titulo_anuncio);
        text_titulo_anuncio.setText("Minha Conta");

        edit_nome = findViewById(R.id.edit_nome);
        edit_telefone = findViewById(R.id.edit_telefone);
        edit_email = findViewById(R.id.edit_email);
        progressBar = findViewById(R.id.progressBar);
        imagemPerfil = findViewById(R.id.imagemPerfil);

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALERIA) {



                Uri imagemselecionada = data.getData();
                caminhoImagem = imagemselecionada.toString();

                // VERIFICA SE A API DO ANDROID É MAIOR OU MENOR QUE 28, SE FOR MENOR, ENTAO SERA FEITO:
                if (Build.VERSION.SDK_INT < 28) {

                    try {
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), imagemselecionada);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // CASO CONTRARIO, SE FOR MAIOR QUE 28:
                } else {

                    ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), imagemselecionada);

                    try {
                        imagem = ImageDecoder.decodeBitmap(source);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    imagemPerfil.setImageBitmap(imagem);

                }
            }
        }
    }
}