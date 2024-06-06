package com.example.myproyect.actividades;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.myproyect.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class LosaAdapter extends RecyclerView.Adapter<LosaAdapter.LosaViewHolder> {

    private Context mContext;
    private List<Losa> mLosas;

    public LosaAdapter(Context context, List<Losa> losas) {
        this.mContext = context;
        this.mLosas = losas;
    }

    @NonNull
    @Override
    public LosaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_losa, parent, false);
        return new LosaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LosaViewHolder holder, int position) {
        Losa losa = mLosas.get(position);
        holder.tvNombre.setText(losa.getNombre());
        holder.tvDescripcion.setText(losa.getDescripcion());
        Glide.with(mContext).load(losa.getImageUrl()).into(holder.ivImage);

        // Listener para el botón de editar
        holder.btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, ActivityCrudLosas.class);
            intent.putExtra("key", losa.getKey());
            intent.putExtra("nombre", losa.getNombre());
            intent.putExtra("descripcion", losa.getDescripcion());
            intent.putExtra("imageUrl", losa.getImageUrl());
            mContext.startActivity(intent);
        });

        // Listener para el botón de eliminar con advertencia
        holder.btnEliminar.setOnClickListener(v -> showDeleteConfirmationDialog(holder.getAdapterPosition()));
    }

    private void showDeleteConfirmationDialog(int position) {
        new AlertDialog.Builder(mContext)
                .setTitle("Eliminar Losa")
                .setMessage("¿Estás seguro de que deseas eliminar esta losa?")
                .setPositiveButton("Sí", (dialog, which) -> deleteLosa(position))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void deleteLosa(int position) {
        Losa losa = mLosas.get(position);
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("losas").child(losa.getKey());
        dbRef.removeValue();
        mLosas.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return mLosas.size();
    }

    public class LosaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDescripcion;
        ImageView ivImage;
        Button btnEditar, btnEliminar;

        public LosaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tv_nombre_losa);
            tvDescripcion = itemView.findViewById(R.id.tv_descripcion_losa);
            ivImage = itemView.findViewById(R.id.iv_losa);
            btnEditar = itemView.findViewById(R.id.btn_editar);
            btnEliminar = itemView.findViewById(R.id.btn_eliminar);
        }
    }
}
