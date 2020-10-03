package com.example.taskapp.ui.home;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskapp.R;
import com.example.taskapp.models.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<Task> list;
    private int position = 0;
    private ItemClickListener listener;

    public TaskAdapter(List<Task> list, ItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(list.get(position));
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.WHITE);
        } else {
            holder.itemView.setBackgroundColor(Color.GRAY);
        }
    }

    public void update(int position, Task task) {
        list.remove(position);
        list.add(position, task);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItem(Task task) {

        list.add(0, task);
        notifyItemChanged(0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textTitle;
        private TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            time = itemView.findViewById(R.id.time);
            itemView.setOnClickListener(this);
        }

        public void bind(Task task) {
            textTitle.setText(task.getTitle());
            time.setText(Date(task.getCreatedAt()));
        }

        private String Date(long date) {
            DateFormat data = new SimpleDateFormat("HH:mm,  d MMMM yyyy ");
            return data.format(date);
        }

        @Override
        public void onClick(View view) {
            position = getAdapterPosition();
            listener.onItemClick(position);
        }
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }
}
