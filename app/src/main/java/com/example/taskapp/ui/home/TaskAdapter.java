package com.example.taskapp.ui.home;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskapp.App;
import com.example.taskapp.R;
import com.example.taskapp.models.Task;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<Task> list;
    private int position = 0;
    private ItemClickListener listener;


    public TaskAdapter(List<Task> list) {
        this.list = list;
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

    public void deleteItem(int position) {
        Task task = list.get(position);
        App.instance.getAppDatabase().taskDao().delete(task);
        list.remove(task);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addItem(Task task) {

        list.add(0, task);
        notifyItemInserted(0);
    }

    public void setList(List<Task> list) {
        this.list.addAll(list);
        notifyDataSetChanged();

    }

    public void setListener(ItemClickListener listener) {
        this.listener = listener;
    }

    public Task getItem(int position) {
        return list.get(position);
    }

    public void setNewList(ArrayList<Task> list) {
        this.list.retainAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textTitle;
        private TextView time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.textTitle);
            time = itemView.findViewById(R.id.time);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(getAdapterPosition());
                    notifyItemChanged(position);
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onLongClick(getAdapterPosition());
                    return true;
                }
            });
        }

        public void bind(Task task) {
            textTitle.setText(task.getTitle());
            time.setText(Date(task.getCreatedAt()));
        }

        private String Date(long date) {
            DateFormat data = new SimpleDateFormat("HH:mm,  d MMMM yyyy ");
            return data.format(date);
        }
    }

    public interface ItemClickListener {
        void onItemClick(int position);

        void onLongClick(int position);
    }
}