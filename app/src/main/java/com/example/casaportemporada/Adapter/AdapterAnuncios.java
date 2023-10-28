package com.example.casaportemporada.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.casaportemporada.Model.Anuncio;
import com.example.casaportemporada.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterAnuncios extends RecyclerView.Adapter<AdapterAnuncios.MyViewHolder> {

    private List<Anuncio> anuncioList;

    public AdapterAnuncios(List<Anuncio> anuncioList) {
        this.anuncioList = anuncioList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anuncio, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //aqui vamos recuperar as info para exibir no nosso layout

        Anuncio anuncio = anuncioList.get(position);

        // exibir imagem no anuncio
        Picasso.get().load(anuncio.getUrlImagem()).into(holder.img_anuncio);
        holder.text_titulo.setText(anuncio.getTitulo());
        holder.text_descricao.setText(anuncio.getDescricao());
        holder.text_data.setText("");

    }

    @Override
    public int getItemCount() {
        return anuncioList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView img_anuncio;
        TextView text_titulo, text_descricao, text_data;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            img_anuncio = itemView.findViewById(R.id.img_anuncio);
            text_titulo = itemView.findViewById(R.id.text_titulo);
            text_descricao = itemView.findViewById(R.id.text_descricao);
            text_data = itemView.findViewById(R.id.text_data);
        }
    }
}
