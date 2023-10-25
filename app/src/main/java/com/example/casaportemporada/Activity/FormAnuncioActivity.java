package com.example.casaportemporada.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.casaportemporada.Model.Produto;
import com.example.casaportemporada.R;

import java.io.EOFException;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_anuncio);

        iniciaComponentes();

        configToolbar();

        configCliques();
    }


    private void configCliques() {
        findViewById(R.id.ib_salvar).setOnClickListener(v -> validaDados());
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

                                Produto produto = new Produto();
                                produto.setTitulo(titulo);
                                produto.setDescricao(descricao);
                                produto.setQuarto(quarto);
                                produto.setBanheiro(banheiro);
                                produto.setGaragem(garagem);
                                produto.setStatus(cb_status.isChecked());

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

    private void iniciaComponentes() {

        edit_titulo = findViewById(R.id.edit_titulo);
        edit_descricao = findViewById(R.id.edit_descricao);
        edit_quarto = findViewById(R.id.edit_quarto);
        edit_banheiro = findViewById(R.id.edit_banheiro);
        edit_garagem = findViewById(R.id.edit_garagem);
        cb_status = findViewById(R.id.cb_status);
        img_anuncio = findViewById(R.id.img_anuncio);

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
        TextView text_titulo = findViewById(R.id.text_titulo);
        text_titulo.setText("Novo Formulario");
    }


}