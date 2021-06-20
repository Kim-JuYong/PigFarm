package com.example.pigfarm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CalendarRecyclerAdapter extends RecyclerView.Adapter<Holder> {
    Context context;
    ArrayList<ItemClass> list;
    CalendarRecyclerAdapter(Context context, ArrayList<ItemClass> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.calendar_item, parent, false);
        return new Holder(view);
    }
    @Override public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.title.setText(list.get(position).title);
        holder.howMany.setText(list.get(position).how_many);
    }
    @Override public int getItemCount() {
        return list.size();
    }
} class Holder extends RecyclerView.ViewHolder {
    TextView title;
    TextView howMany;
    public Holder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.item_title);
        howMany = itemView.findViewById(R.id.item_how_many);
    }
}

