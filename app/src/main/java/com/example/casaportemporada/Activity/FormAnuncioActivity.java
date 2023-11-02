package com.example.casaportemporada.Activity;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.casaportemporada.Model.Anuncio;
import com.example.casaportemporada.R;
import com.example.casaportemporada.helper.FirebaseHelper;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class FormAnuncioActivity extends AppCompatActivity {

    //REQUEST CODE PRA VALIDAR OQUE A GENTE ESTA ESPERANDO (IMAGEM DA GALERIA)
    private static final int REQUEST_GALERIA = 100;

    private EditText edit_titulo;
    private EditText edit_descricao;
    private EditText edit_quarto;
    private EditText edit_banheiro;
    private EditText edit_garagem;
    private CheckBox cb_status;

    private ImageView img_anuncio;
    private String caminhoImagem;
    private Bitmap imagem;
    private ProgressBar progressBar;

    private Anuncio anuncio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_anuncio);

        iniciaComponentes();

        // vamos ter 2 tipos diferentes de clicks, 1 pr editar anuncio e outro pra add anuncio novo
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            anuncio = (Anuncio) bundle.getSerializable("anuncio");

            configDados();

        }

        configToolbar();

        configCliques();
    }

    private void configDados() {
        Picasso.get().load(anuncio.getUrlImagem()).into(img_anuncio);
        edit_titulo.setText(anuncio.getTitulo());
        edit_descricao.setText(anuncio.getDescricao());
        edit_quarto.setText(anuncio.getQuarto());
        edit_banheiro.setText(anuncio.getBanheiro());
        edit_garagem.setText(anuncio.getGaragem());
        cb_status.setChecked(anuncio.isStatus());
    }


    private void configCliques() {
        findViewById(R.id.ib_salvar).setOnClickListener(v -> validaDados());
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
    }

    public void configImagem(View view) {
        img_anuncio.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_GALERIA);
        });

    }


    private void validaDados() {

        String titulo = edit_titulo.getText().toString();
        String descricao = edit_descricao.getText().toString();
        String quarto = edit_quarto.getText().toString();
        String banheiro = edit_banheiro.getText().toString();
        String garagem = edit_garagem.getText().toString();
        String status = cb_status.getText().toString();

        if (!titulo.isEmpty()) {
            if (!descricao.isEmpty()) {
                if (!quarto.isEmpty()) {
                    if (!banheiro.isEmpty()) {
                        if (!garagem.isEmpty()) {
                            if (!status.isEmpty()) {

                                if (anuncio == null) anuncio = new Anuncio();
                                anuncio.setIdUsuario(FirebaseHelper.getIdFirebase());
                                anuncio.setTitulo(titulo);
                                anuncio.setDescricao(descricao);
                                anuncio.setQuarto(quarto);
                                anuncio.setBanheiro(banheiro);
                                anuncio.setGaragem(garagem);
                                anuncio.setStatus(cb_status.isChecked());

                                //salva a imagem idependentemente se é edição ou anuncio novo
                                if (caminhoImagem != null) {
                                    salvarImagemAnuncio();

                                    //caso seja edição
                                } else {
                                    if (anuncio.getUrlImagem() != null) {
                                        anuncio.salvar();

                                        // caso seja anuncio novo
                                    } else {
                                        Toast.makeText(this, "Selecione uma imagem para o anuncio.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                ocutarTeclado();


                            } else {
                                cb_status.requestFocus();
                                cb_status.setError("Selecione um status.");
                            }
                        } else {
                            edit_garagem.requestFocus();
                            edit_garagem.setError("Informação obrigatória.");
                        }
                    } else {
                        edit_banheiro.requestFocus();
                        edit_banheiro.setError("Informação obrigatória.");
                    }
                } else {
                    edit_quarto.requestFocus();
                    edit_quarto.setError("Informação obrigatória.");
                }
            } else {
                edit_descricao.requestFocus();
                edit_descricao.setError("Informe uma descrição.");
            }
        } else {
            edit_titulo.requestFocus();
            edit_titulo.setError("Informe um título.");
        }


    }

    private void salvarImagemAnuncio() {

        progressBar.setVisibility(View.VISIBLE);
        ocutarTeclado();

        StorageReference storageReference = FirebaseHelper.getStorageReference()
                .child("imagens")
                .child("anuncios")
                // .jpeg será a estensão do arquivo
                .child(anuncio.getId() + ".jpeg");

        UploadTask uploadTask = storageReference.putFile(Uri.parse(caminhoImagem));
        uploadTask.addOnSuccessListener(taskSnapshot -> storageReference.getDownloadUrl().addOnCompleteListener(task -> {

            // recupera a imagem salva no firebase
            String urlImagem = task.getResult().toString();
            anuncio.setUrlImagem(urlImagem);

            anuncio.salvar();
            finish();

        })).addOnFailureListener(e -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();

        });
    }

    private void ocutarTeclado() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(edit_titulo.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void iniciaComponentes() {
        edit_titulo = findViewById(R.id.edit_titulo);
        edit_descricao = findViewById(R.id.edit_descricao);
        edit_quarto = findViewById(R.id.edit_quarto);
        edit_banheiro = findViewById(R.id.edit_banheiro);
        edit_garagem = findViewById(R.id.edit_garagem);
        cb_status = findViewById(R.id.cb_status);
        img_anuncio = findViewById(R.id.img_anuncio);
        progressBar = findViewById(R.id.progressBar);

    }

    @Override
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

                    img_anuncio.setImageBitmap(imagem);

                }
            }
        }
    }


    private void configToolbar() {
        TextView text_titulo = findViewById(R.id.text_titulo_anuncio);
        text_titulo.setText("Novo Anúncio");
    }


}