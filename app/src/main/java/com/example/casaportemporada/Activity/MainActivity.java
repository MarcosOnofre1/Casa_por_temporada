package com.example.casaportemporada.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.example.casaportemporada.Autenticacao.LoginActivity;
import com.example.casaportemporada.R;
import com.example.casaportemporada.helper.FirebaseHelper;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private ImageButton ib_menu;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciaComponentes();

        configCliques();

    }

    private void configCliques() {
        ib_menu.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(this, ib_menu);
            popupMenu.getMenuInflater().inflate(R.menu.menu_home, popupMenu.getMenu());

            popupMenu.setOnMenuItemClickListener(menuItem -> {

                //toda vez que o usuario clicar nas opções de menu, ele verifica se o usuario é autenticado no app
                if (menuItem.getItemId() == R.id.menu_filtar) {
                    startActivity(new Intent(this, FiltarAnunciosActivity.class));

                } else if (menuItem.getItemId() == R.id.menu_meus_anuncios) {
                    if (FirebaseHelper.getAutenticado()) {
                        startActivity(new Intent(this, MeusAnunciosActivity.class));

                    } else {
                        showDialog("Você não está autenticado.\nDeseja fazer isso agora?");
                    }

                } else if (menuItem.getItemId() == R.id.sair_acc) {
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
        //dialog.dismiss faz com que assim que clica no botao, feche a tela
        btnFechar.setOnClickListener(v -> dialog.dismiss());

        //para exibir as view
        builder.setView(view);

        dialog = builder.create();
        dialog.show();

    }


    private void iniciaComponentes() {
        ib_menu = findViewById(R.id.ib_menu);
    }

}