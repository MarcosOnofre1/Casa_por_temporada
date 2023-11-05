package com.example.casaportemporada.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.casaportemporada.Adapter.AdapterAnuncios;
import com.example.casaportemporada.Autenticacao.LoginActivity;
import com.example.casaportemporada.Model.Anuncio;
import com.example.casaportemporada.Model.Filtro;
import com.example.casaportemporada.R;
import com.example.casaportemporada.helper.FirebaseHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterAnuncios.OnClick {

    private final int REQUEST_FILTRO = 100;

    private RecyclerView rv_anuncios;
    private ProgressBar progressBar;
    private TextView text_info;

    private List<Anuncio> anuncioList = new ArrayList<>();
    private AdapterAnuncios adapterAnuncios;

    private ImageButton ib_menu;
    private AlertDialog dialog;
    private Filtro filtro;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciaComponentes();

        configRv();

        recuperaAnuncios();

        configCliques();

    }

    private void configRv() {
        rv_anuncios.setLayoutManager(new LinearLayoutManager(this));
        rv_anuncios.setHasFixedSize(true);
        adapterAnuncios = new AdapterAnuncios(anuncioList, this);
        rv_anuncios.setAdapter(adapterAnuncios);
    }

    private void recuperaAnuncios() {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("anuncios_publicos");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // verifica se existe algum anuncio cadastrado no Firebase
                if (snapshot.exists()) {
                    anuncioList.clear();

                    // ira percorrer entre todos os children do nó anuncios_publicos
                    for (DataSnapshot snap : snapshot.getChildren()) {

                        Anuncio anuncio = snap.getValue(Anuncio.class);
                        anuncioList.add(anuncio);

                    }
                    text_info.setText("");

                } else {
                    text_info.setText("Nenhum anúncio cadastrado.");

                }
                progressBar.setVisibility(View.GONE);
                Collections.reverse(anuncioList);
                adapterAnuncios.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void recuperaAnunciosFiltro() {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("anuncios_publicos");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // verifica se existe algum anuncio cadastrado no Firebase
                if (snapshot.exists()) {
                    anuncioList.clear();

                    // ira percorrer entre todos os children do nó anuncios_publicos
                    for (DataSnapshot snap : snapshot.getChildren()) {

                        Anuncio anuncio = snap.getValue(Anuncio.class);

                        int quarto = Integer.parseInt(anuncio.getQuarto());
                        int banheiro = Integer.parseInt(anuncio.getBanheiro());
                        int garagem = Integer.parseInt(anuncio.getGaragem());

                        if (quarto >= filtro.getQtdQuarto() &&
                                banheiro >= filtro.getQtdBanheiro() &&
                                garagem >= filtro.getQtdGaragem()){
                            anuncioList.add(anuncio);
                        }
                    }
                }

                if (anuncioList.size() == 0){
                    text_info.setText("Nenhum anúncio encontrado.");

                }else {
                    text_info.setText("");
                }

                progressBar.setVisibility(View.GONE);
                Collections.reverse(anuncioList);
                adapterAnuncios.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void configCliques() {
        ib_menu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, ib_menu);
            popupMenu.getMenuInflater().inflate(R.menu.menu_home, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(menuItem -> {

                //toda vez que o usuario clicar nas opções de menu, ele verifica se o usuario é autenticado no app
                if (menuItem.getItemId() == R.id.menu_filtar) {
                    Intent intent = new Intent(this, FiltarAnunciosActivity.class);
                    intent.putExtra("filtro", filtro);
                    startActivityForResult(intent, REQUEST_FILTRO);

                } else if (menuItem.getItemId() == R.id.menu_meus_anuncios) {
                    if (FirebaseHelper.getAutenticado()) {
                        startActivity(new Intent(this, MeusAnunciosActivity.class));

                    } else {
                        showDialog("Você não está autenticado.\nDeseja fazer isso agora?");
                    }

                } else if (menuItem.getItemId() == R.id.sair_acc) {
                    FirebaseHelper.getAuth().signOut();
                    finish();
                    startActivity(new Intent(this, LoginActivity.class));

                } else {
                    if (FirebaseHelper.getAutenticado()) {
                        startActivity(new Intent(this, MinhaContaActivity.class));

                    } else {
                        showDialog("Você não está autenticado.\nDeseja fazer isso agora?");
                    }
                }
                return true;
            });
            popupMenu.show();

        });

    }

    private void showDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);

        View view = getLayoutInflater().inflate(R.layout.layout_dialog, null);

        //titulo e corpo da caixa de janela quando aparecer o erro
        TextView textView = view.findViewById(R.id.textTitulo);
        textView.setText("Atenção");

        TextView mensagem = view.findViewById(R.id.textMensagem);
        mensagem.setText(msg);

        //Botao da caixa de aviso
        Button btnOk = view.findViewById(R.id.btnOk);
        //dialog.dismiss faz com que assim que clica no botao, feche a tela
        btnOk.setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class)));

        Button btnFechar = view.findViewById(R.id.btnFechar);
        btnFechar.setOnClickListener(v -> dialog.dismiss());

        //para exibir as view
        builder.setView(view);

        dialog = builder.create();
        dialog.show();

    }


    private void iniciaComponentes() {
        ib_menu = findViewById(R.id.ib_menu);
        rv_anuncios = findViewById(R.id.rv_anuncios);
        progressBar = findViewById(R.id.progressBar);
        text_info = findViewById(R.id.text_info);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_FILTRO) {

                filtro = (Filtro) data.getSerializableExtra("filtro");
                if (filtro.getQtdQuarto() > 0 || filtro.getQtdBanheiro() > 0 || filtro.getQtdGaragem() > 0)
                    ;
                // recupera os anuncios com base nos filtros.
                recuperaAnunciosFiltro();
            }
        }else {
            recuperaAnuncios();
        }

    }

    @Override
    public void OnClickListener(Anuncio anuncio) {
        Intent intent = new Intent(this, DetalheAnuncioActivity.class);
        intent.putExtra("anuncio", anuncio);
        startActivity(intent);
    }
}