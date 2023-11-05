package com.example.casaportemporada.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.casaportemporada.Model.Filtro;
import com.example.casaportemporada.R;

public class FiltarAnunciosActivity extends AppCompatActivity {

    private TextView text_quarto;
    private TextView text_banheiro;
    private TextView text_garagem;

    private SeekBar sb_quarto;
    private SeekBar sb_banheiro;
    private SeekBar sb_garagem;

    private int qtd_quarto;
    private int qtd_banheiro;
    private int qtd_garagem;

    private Filtro filtro;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitrar_anuncios);

        iniciaComponentes();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            filtro = (Filtro) bundle.getSerializable("filtro");
            if (filtro != null){
                configFiltros();

            }
        }

        configCliques();

        configSb();

    }

    private void configFiltros() {
        sb_quarto.setProgress(filtro.getQtdQuarto());
        sb_banheiro.setProgress(filtro.getQtdBanheiro());
        sb_garagem.setProgress(filtro.getQtdGaragem());

        text_quarto.setText(filtro.getQtdQuarto() + " quarto ou mais");
        text_banheiro.setText(filtro.getQtdBanheiro() + " banheiro ou mais");
        text_garagem.setText(filtro.getQtdGaragem() + " garagem ou mais");

        qtd_quarto = filtro.getQtdQuarto();
        qtd_banheiro = filtro.getQtdBanheiro();
        qtd_garagem = filtro.getQtdGaragem();

    }

    public void filtar(View view){

        if (filtro == null) filtro = new Filtro();

        // so vai guardar se for acima de 0.
        if (qtd_quarto > 0) filtro.setQtdQuarto(qtd_quarto);
        if(qtd_banheiro > 0) filtro.setQtdBanheiro(qtd_banheiro);
        if (qtd_garagem > 0) filtro.setQtdGaragem(qtd_garagem);

        if  (qtd_quarto >0 || qtd_banheiro > 0 || qtd_garagem > 0){
            Intent intent = new Intent();
            intent.putExtra("filtro", filtro);
            setResult(RESULT_OK, intent);

        }
        finish();

    }

    private void configSb(){
        sb_quarto.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            // enquanto esta movendo ele
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                text_quarto.setText(i + " quarto ou mais");
                qtd_quarto = i;

            }

            // quando clicou no ponteiro e começou a movimentar
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }


            // quando soltou o componente
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sb_banheiro.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            // enquanto esta movendo ele
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                text_banheiro.setText(i + " banheiro ou mais");
                qtd_banheiro = i;


            }

            // quando clicou no ponteiro e começou a movimentar
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }


            // quando soltou o componente
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sb_garagem.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            // enquanto esta movendo ele
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                text_garagem.setText(i + " garagem ou mais");
                qtd_garagem = i;

            }

            // quando clicou no ponteiro e começou a movimentar
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }


            // quando soltou o componente
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    public void limparFiltro(View view){
        qtd_quarto = 0;
        qtd_banheiro = 0;
        qtd_garagem = 0;

        sb_quarto.setProgress(0);
        sb_banheiro.setProgress(0);
        sb_garagem.setProgress(0);

       //finish();
    }

    private void configCliques(){
        findViewById(R.id.btn_voltar).setOnClickListener(v -> finish());
    }

    private void iniciaComponentes(){
        TextView text_titulo_toolbar = findViewById(R.id.text_titulo_toolbar);
        text_titulo_toolbar.setText("Filtrar Anúncios");


        text_quarto = findViewById(R.id.text_quarto);
        text_banheiro = findViewById(R.id.text_banheiro);
        text_garagem = findViewById(R.id.text_garagem);

        sb_quarto = findViewById(R.id.sb_quarto);
        sb_banheiro = findViewById(R.id.sb_banheiro);
        sb_garagem = findViewById(R.id.sb_garagem);
    }
}