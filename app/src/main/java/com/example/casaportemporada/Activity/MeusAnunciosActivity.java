package com.example.casaportemporada.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.casaportemporada.Adapter.AdapterAnuncios;
import com.example.casaportemporada.Model.Anuncio;
import com.example.casaportemporada.R;
import com.example.casaportemporada.helper.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tsuryo.swipeablerv.SwipeLeftRightCallback;
import com.tsuryo.swipeablerv.SwipeableRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MeusAnunciosActivity extends AppCompatActivity implements AdapterAnuncios.OnClick {

    private List<Anuncio> anuncioList = new ArrayList<>();
    private ProgressBar progressBar;
    private TextView text_info;
    private SwipeableRecyclerView rv_anuncios;
    private AdapterAnuncios adapterAnuncios;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_anuncios);

        iniciaComponentes();

        configTitulo();

        configRv();

        configCliques();

    }

    // vai limpar a lista e via recuperar anuncios novos
    @Override
    protected void onStart() {
        super.onStart();

        recuperaAnuncios();
    }

    private void configCliques(){
        findViewById(R.id.ib_add).setOnClickListener(v ->
                startActivity(new Intent(this, FormAnuncioActivity.class)));
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
    }

    private void configRv(){
        rv_anuncios.setLayoutManager(new LinearLayoutManager(this));
        rv_anuncios.setHasFixedSize(true);
        adapterAnuncios = new AdapterAnuncios(anuncioList, this);
        rv_anuncios.setAdapter(adapterAnuncios);

        rv_anuncios.setListener(new SwipeLeftRightCallback.Listener() {
            @Override
            public void onSwipedLeft(int position) {

            }

            @Override
            public void onSwipedRight(int position) {
                showDialogDelete(position);
            }
        });

    }

    private void showDialogDelete(int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);

        View view = getLayoutInflater().inflate(R.layout.layout_dialog, null);

        //titulo e corpo da caixa de janela quando aparecer o erro
        TextView textView = view.findViewById(R.id.textTitulo);
        textView.setText("Delete Anúncio");

        TextView mensagem = view.findViewById(R.id.textMensagem);
        mensagem.setText("Deseja deletar esse anúncio?");

        //Botao da caixa de aviso
        Button btnOk = view.findViewById(R.id.btnOk);
        //dialog.dismiss faz com que assim que clica no botao, feche a tela
        btnOk.setOnClickListener(v -> {
            anuncioList.get(pos).delete();
            adapterAnuncios.notifyDataSetChanged();
            dialog.dismiss();
        });

        Button btnFechar = view.findViewById(R.id.btnFechar);
        btnFechar.setOnClickListener(v -> {
            dialog.dismiss();
            adapterAnuncios.notifyDataSetChanged();

        });

        //para exibir as view
        builder.setView(view);

        dialog = builder.create();
        dialog.show();

    }

    private void recuperaAnuncios(){
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("anuncios")
                .child(FirebaseHelper.getIdFirebase());
        //monitoramente em tempo real addValueEventListener()
        reference.addValueEventListener(new ValueEventListener() {

            // onDataChenge é onde vamos recuperar de fato as info do Firebase
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // ou seja, tenho uma info que eu cadastrei algum anuncio que foi registrado pro firebase
                if (snapshot.exists()){
                    anuncioList.clear();
                    // nos percorremos todas as listas, todos os childrens do anuncio do idFirebase
                    for (DataSnapshot snap : snapshot.getChildren()){
                        Anuncio anuncio = snap.getValue(Anuncio.class);
                        anuncioList.add(anuncio);
                    }
                    // p/ tirar o text de nenhum anuncio cadastrado
                    text_info.setText("");

                }else {
                    text_info.setText("Nenhum anúncio cadastrado.");
                }

                progressBar.setVisibility(View.GONE);
                // Reverte sempre do ultimo anuncio p/ o primeiro na lista
                Collections.reverse(anuncioList);
                // depois que carregar a lista, tendo anuncios ou nao, irá notificar o adpter.
                adapterAnuncios.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void configTitulo(){
        TextView text_titulo = findViewById(R.id.text_titulo_anuncio);
        text_titulo.setText("Meus Anúncios");
    }

    private void iniciaComponentes() {
        progressBar = findViewById(R.id.progressBar);
        text_info = findViewById(R.id.text_info);
        rv_anuncios = findViewById(R.id.rv_anuncios);

    }

    @Override
    public void OnClickListener(Anuncio anuncio) {
       Intent intent = new Intent(this, FormAnuncioActivity.class);
       intent.putExtra("anuncio", anuncio);
       startActivity(intent);
    }
}