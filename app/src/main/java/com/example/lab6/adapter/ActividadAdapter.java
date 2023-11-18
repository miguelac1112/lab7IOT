package com.example.lab6.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab6.R;
import com.example.lab6.dtos.citasDia;

import java.util.List;

public class ActividadAdapter extends RecyclerView.Adapter<ActividadAdapter.ActividadViewHolder> {

    private List<citasDia> citasDiaList;
    private Context context;

    public ActividadAdapter() {
        this.context = context;
    }

    public void setActividadList(List<citasDia> citasDiaList) {
        this.citasDiaList = citasDiaList;
        notifyDataSetChanged();
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ActividadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_citas, parent, false);
        return new ActividadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActividadViewHolder holder, int position) {
        citasDia citasDia = citasDiaList.get(position);

        // Asigna los datos a los elementos de la vista
        holder.hora.setText(citasDia.getHora());
        holder.servicio.setText(citasDia.getServicio());
        holder.correo.setText(citasDia.getCorreo());
    }

    @Override
    public int getItemCount() {
        return citasDiaList != null ? citasDiaList.size() : 0;
    }

    public static class ActividadViewHolder extends RecyclerView.ViewHolder {
        TextView hora;
        TextView servicio;
        TextView correo;
        public ActividadViewHolder(@NonNull View itemView) {
            super(itemView);

            hora = itemView.findViewById(R.id.hora);
            servicio = itemView.findViewById(R.id.servicio);
            correo = itemView.findViewById(R.id.correo);
        }
    }
}
