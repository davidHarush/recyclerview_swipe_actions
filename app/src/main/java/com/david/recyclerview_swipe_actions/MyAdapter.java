package com.david.recyclerview_swipe_actions;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    public List<Integer> mItems;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView text, pow;

        public MyViewHolder(View view) {
            super(view);
            text = (TextView) view.findViewById(R.id.name);
            pow = (TextView) view.findViewById(R.id.nationality);
        }
    }

    public MyAdapter(List<Integer> items) {
        this.mItems = items;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        int item = mItems.get(position);
        holder.text.setText("Element #" + item);
        String text = String.format("2^%s = %s", item,(int) Math.pow(2, item));
        holder.pow.setText(text);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
