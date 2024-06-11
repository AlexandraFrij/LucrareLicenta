package com.example.licenta.holder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.licenta.R;

public class StudentAttendancesHolder  extends RecyclerView.ViewHolder {
    public TextView nameTextView;
    public TextView idNumberTextView;
    public TextView courseAttendancesTextView;
    public TextView seminarAttendancesTextView;

    public StudentAttendancesHolder(@NonNull View itemView) {
        super(itemView);
        nameTextView = itemView.findViewById(R.id.student_name);
        idNumberTextView =itemView.findViewById(R.id.student_id_number);
        courseAttendancesTextView = itemView.findViewById(R.id.course_attendances);
        seminarAttendancesTextView= itemView.findViewById(R.id.seminar_attendances);

    }
}
