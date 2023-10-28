package com.example.casaportemporada.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MeusAnunciosActivity extends AppCompatActivity {

    private List<Anuncio> anuncioList = new ArrayList<>();
    private ProgressBar progressBar;
    private TextView text_info;
    private RecyclerView rv_anuncios;
    private AdapterAnuncios adapterAnuncios;


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
    }

    private void configRv(){
        rv_anuncios.setLayoutManager(new LinearLayoutManager(this));
        rv_anuncios.setHasFixedSize(true);
        adapterAnuncios = new AdapterAnuncios(anuncioList);
        rv_anuncios.setAdapter(adapterAnuncios);
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
        TextView text_titulo = findViewById(R.id.text_titulo);
        text_titulo.setText("Meus Anúncios");
    }

    private void iniciaComponentes() {
        progressBar = findViewById(R.id.progressBar);
        text_info = findViewById(R.id.text_info);
        rv_anuncios = findViewById(R.id.rv_anuncios);

    }
}