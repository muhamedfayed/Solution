package com.example.muhammedd.solution;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Muhammedd on 11/12/2016.
 */

public class ProblemsAdapter extends RecyclerView.Adapter<ProblemsAdapter.MyViewHolder> {

    private List<Problem> problemsList;

    public ProblemsAdapter(List<Problem> problemsList) {
        this.problemsList = problemsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.problem_list_row,parent,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Problem p = problemsList.get(position);
        holder.problem_head.setText(p.getProblemHead());
        holder.problem_by.setText(p.getProblemBy());

        if(p.getSoulution() != null){
            holder.imageView.setImageResource(R.drawable.right_mark);
        }

    }

    @Override
    public int getItemCount() {
        return problemsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView problem_head,problem_by;
        public ImageView imageView;

        public MyViewHolder(View itemView){
            super(itemView);
            problem_head = (TextView) itemView.findViewById(R.id.problem_head_txt);
            problem_by = (TextView) itemView.findViewById(R.id.problem_by_txt);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }


    }
}
