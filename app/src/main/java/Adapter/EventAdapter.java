package Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robertwais.pickupgames.DetailsActivity;
import com.example.robertwais.pickupgames.EventBoardActivity;
import com.example.robertwais.pickupgames.R;

import java.util.List;

import Model.ListItem;
import Model.Post;

/**
 * Created by Markus on 4/1/2018.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private Context context;
    private List<Post> listItems;

    public EventAdapter(Context context, List listItem) {
        this.context = context;
        this.listItems = listItem;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventAdapter.ViewHolder holder, int position) {
        Post item = listItems.get(position);
        holder.name.setText(item.getTitle());
        holder.description.setText(item.getDescription());



    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

        public TextView name;
        public TextView description;

        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            name = (TextView) itemView.findViewById(R.id.title_label);
            description = (TextView) itemView.findViewById(R.id.description);
        }

        @Override
        public void onClick(View view) {

            //This is where the user has tapped
            int position = getAdapterPosition();
            Post item = listItems.get(position);

            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("Title", item.getTitle());
            intent.putExtra("Description", item.getDescription());
            intent.putExtra("Time",item.getTime());
            intent.putExtra("CommentsID",item.getPostID());

            context.startActivity(intent);
            //Toast.makeText(context, item.getTitle(), Toast.LENGTH_LONG).show();
        }
    }
}
