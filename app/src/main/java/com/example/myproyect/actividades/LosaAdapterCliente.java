package com.example.myproyect.actividades;

import android.content.Context;
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

public class LosaAdapterCliente extends RecyclerView.Adapter<LosaAdapterCliente.LosaViewHolder> {

    private Context context;
    private List<Losa> losaList;

    public LosaAdapterCliente(Context context, List<Losa> losaList) {
        this.context = context;
        this.losaList = losaList;
    }

    @NonNull
    @Override
    public LosaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_losa_cliente, parent, false);
        return new LosaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LosaViewHolder holder, int position) {
        Losa losa = losaList.get(position);
        holder.tvNombreLosa.setText(losa.getNombre());
        holder.tvDescripcionLosa.setText(losa.getDescripcion());
        Glide.with(context).load(losa.getImageUrl()).into(holder.ivLosa);
    }

    @Override
    public int getItemCount() {
        return losaList.size();
    }

    public static class LosaViewHolder extends RecyclerView.ViewHolder {
        ImageView ivLosa;
        TextView tvNombreLosa;
        TextView tvDescripcionLosa;

        public LosaViewHolder(@NonNull View itemView) {
            super(itemView);
            ivLosa = itemView.findViewById(R.id.iv_losa);
            tvNombreLosa = itemView.findViewById(R.id.tv_nombre_losa);
            tvDescripcionLosa = itemView.findViewById(R.id.tv_descripcion_losa);
        }
    }
}
