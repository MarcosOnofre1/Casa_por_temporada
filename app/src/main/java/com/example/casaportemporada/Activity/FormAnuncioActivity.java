package com.example.casaportemporada.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.casaportemporada.R;

public class FormAnuncioActivity extends AppCompatActivity {

    private EditText edit_titulo;
    private EditText edit_descricao;
    private EditText edit_quarto;
    private EditText edit_banheiro;
    private EditText edit_garagem;
    private CheckBox cb_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_anuncio);
    }
}