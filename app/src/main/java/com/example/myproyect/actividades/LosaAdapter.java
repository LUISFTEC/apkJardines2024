package com.example.myproyect.actividades;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.myproyect.R;
import java.util.List;

public class LosaAdapter extends RecyclerView.Adapter<LosaAdapter.LosaViewHolder> {

    private List<ActivityListaLosas.Losa> losaList;

    public LosaAdapter(List<ActivityListaLosas.Losa> losaList) {
        this.losaList = losaList;
    }

    @NonNull
    @Override
    public LosaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_losa, parent, false);
        return new LosaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LosaViewHolder holder, int position) {
        ActivityListaLosas.Losa losa = losaList.get(position);
        holder.tvNombre.setText(losa.nombre);
        holder.tvDescripcion.setText(losa.descripcion);
        Glide.with(holder.itemView.getContext())
                .load(losa.imageUrl)
                .into(holder.ivLosa);
    }

    @Override
    public int getItemCount() {
        return losaList.size();
    }

    public static class LosaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDescripcion;
        ImageView ivLosa;

        public LosaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tv_nombre_losa);
            tvDescripcion = itemView.findViewById(R.id.tv_descripcion_losa);
            ivLosa = itemView.findViewById(R.id.iv_losa);
        }
    }
}
