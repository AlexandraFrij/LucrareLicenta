package com.example.licenta.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.example.licenta.R;
import com.example.licenta.holder.StudentAttendancesHolder;
import com.example.licenta.item.StudentAttendancesRecyclerViewItem;

public class StudentAttendancesAdapter extends RecyclerView.Adapter<StudentAttendancesHolder> {
    private final Context context;
    private final List<StudentAttendancesRecyclerViewItem> items;

    public StudentAttendancesAdapter(Context context, List<StudentAttendancesRecyclerViewItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public StudentAttendancesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new StudentAttendancesHolder((LayoutInflater.from(context).inflate(R.layout.student_attendances_list, parent, false)));
    }

    @Override
    public void onBindViewHolder(@NonNull StudentAttendancesHolder holder, int position) {
        StudentAttendancesRecyclerViewItem studentAttendancesRecyclerView = items.get(position);
        String name = studentAttendancesRecyclerView.getName();
        String idNumber = studentAttendancesRecyclerView.getIdNumber();
        int courseAttendances = studentAttendancesRecyclerView.getCourseAttendances();
        int seminarAttendances = studentAttendancesRecyclerView.getSeminarAttendances();

        holder.nameTextView.setText(name);
        holder.idNumberTextView.setText(idNumber);
        holder.courseAttendancesTextView.setText("Prezente curs: "+courseAttendances);
        holder.seminarAttendancesTextView.setText("Prezente seminar: "+ seminarAttendances);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
