package com.example.presencecontroltestapp.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.presencecontroltestapp.R;
import com.example.presencecontroltestapp.entities.Students;
import com.example.presencecontroltestapp.ui.fragments.ClassInformation;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.util.List;

public class AdapterFiles extends RecyclerView.Adapter<AdapterFiles.FilesViewHolder> {
    private static final String TAG = AdapterFiles.class.getSimpleName();
    private List<ClassInformation> mFilesList;
    private Context mContext;
    private Students mStudents;

    public AdapterFiles (List<ClassInformation> fileList) {
        Log.d(TAG, "<---Higa---> fileList.size : " + fileList.size());
        mFilesList = fileList;
    }

    @NonNull
    @Override
    public AdapterFiles.FilesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new AdapterFiles.FilesViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_class_information, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterFiles.FilesViewHolder holder, int position) {
        holder.txtClassName.setText(mFilesList.get(position).getName());
        holder.txtProfessorName.setText(mFilesList.get(position).getProfessorName());
        holder.txtQtdAulasFrequentadas.setText(String.valueOf(mFilesList.get(position).getQtdAulasAssistidas()));
        holder.txtQtdAulasDadas.setText(String.valueOf(mFilesList.get(position).getQtdAulasDadas()));

        holder.pizzaGraphic.addPieSlice(new PieModel("Aulas frequentadas", mFilesList.get(position).getQtdAulasDadas(), R.color.black_text));
        holder.pizzaGraphic.addPieSlice(new PieModel("Aulas frequentadas", mFilesList.get(position).getQtdAulasAssistidas(), R.color.yellow));
        holder.pizzaGraphic.startAnimation();
    }

    @Override
    public int getItemCount() {
        return mFilesList.size();
    }

    protected class FilesViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {
        private TextView txtClassName, txtProfessorName, txtQtdAulasDadas, txtQtdAulasFrequentadas;
        private PieChart pizzaGraphic;

        public FilesViewHolder(@NonNull View itemView) {
            super(itemView);
            txtClassName = itemView.findViewById(R.id.txtClassName);
            txtProfessorName = itemView.findViewById(R.id.tv_professor_name);
            txtQtdAulasDadas = itemView.findViewById(R.id.tv_aulas_lecionadas);
            txtQtdAulasFrequentadas = itemView.findViewById(R.id.tv_aulas_assistidas);
            pizzaGraphic = itemView.findViewById(R.id.pizza);
        }

        @Override
        public void onClick(View view) {
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }
    }
}
