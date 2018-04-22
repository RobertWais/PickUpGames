package Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.robertwais.pickupgames.R;

import java.util.List;

import Model.Comment;

/**
 * Created by robertwais on 4/20/18.
 */

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.ViewHolder> {
private Context context;
private List<Comment> listItems;

    public commentAdapter(Context context, List listitem){
        this.context = context;
        this.listItems=listitem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(commentAdapter.ViewHolder holder, int position) {

        Comment comm = listItems.get(position);
        holder.username.setText(comm.getUser());
        holder.comment.setText(comm.getComment());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public TextView comment;
        public ViewHolder(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.usernameCard);
            comment = (TextView) itemView.findViewById(R.id.commentCard);
        }
    }

}

