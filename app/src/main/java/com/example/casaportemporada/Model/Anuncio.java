package com.example.casaportemporada.Model;

import com.example.casaportemporada.helper.FirebaseHelper;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;

public class Anuncio implements Serializable {

    private String id;
    private String idUsuario;
    private String titulo;
    private String descricao;
    private String quarto;
    private String banheiro;
    private String garagem;
    private boolean status;
    private String urlImagem;


    public Anuncio() {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference();
        //criamos um ID para cada anuncio novo criado.
        this.setId(reference.push().getKey());
    }

    public void salvar() {
        DatabaseReference reference = FirebaseHelper.getDatabaseReference()
                .child("anuncios")
                .child(FirebaseHelper.getIdFirebase())
                .child(this.getId());
        reference.setValue(this);

        DatabaseReference anuncioPublico = FirebaseHelper.getDatabaseReference()
                .child("anuncios_publicos")
                .child(this.getId());
        anuncioPublico.setValue(this);

    }

    public void delete() {
        DatabaseReference anuncioPrivadoRef = FirebaseHelper.getDatabaseReference()
                .child("anuncios")
                .child(FirebaseHelper.getIdFirebase())
                .child(this.getId());
        anuncioPrivadoRef.removeValue().addOnCompleteListener(task -> {
           // so ira deletar a imagem caso ocorra com sucesso o delete das informações do banco de dados do Realtime Database
            // Pode ser que ocorra um erro de deletar o anuncio, a imagem pode ser deletada e o anuncio fique sem imagem
            if (task.isSuccessful()){
               StorageReference storageReference = FirebaseHelper.getStorageReference()
                       .child("imagens")
                       .child("anuncios")
                       // .jpeg será a extensão do arquivo
                       .child(this.getId() + ".jpeg");
               storageReference.delete();
           }
        });

        DatabaseReference anuncioPublicoRef = FirebaseHelper.getDatabaseReference()
                .child("anuncios_publicos")
                .child(this.getId());
        anuncioPublicoRef.removeValue();

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getQuarto() {
        return quarto;
    }

    public void setQuarto(String quarto) {
        this.quarto = quarto;
    }

    public String getBanheiro() {
        return banheiro;
    }

    public void setBanheiro(String banheiro) {
        this.banheiro = banheiro;
    }

    public String getGaragem() {
        return garagem;
    }

    public void setGaragem(String garagem) {
        this.garagem = garagem;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getUrlImagem() {
        return urlImagem;
    }

    public void setUrlImagem(String urlImagem) {
        this.urlImagem = urlImagem;
    }
}
