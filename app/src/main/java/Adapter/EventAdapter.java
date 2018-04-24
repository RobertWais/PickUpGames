package Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.DeadObjectException;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.robertwais.pickupgames.DetailsActivity;
import com.example.robertwais.pickupgames.EventBoardActivity;
import com.example.robertwais.pickupgames.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import Model.ListItem;
import Model.Post;
import Model.Stats;

/**
 * Created by Markus on 4/1/2018.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private Context context;
    private List<Post> listItems;
    private FirebaseDatabase fDatabase;
    private DatabaseReference dbRef;
    private String uName;

    public EventAdapter(Context context, List listItem) {
        this.context = context;
        this.listItems = listItem;
        fDatabase = FirebaseDatabase.getInstance();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EventAdapter.ViewHolder holder, int position) {
        Post item = listItems.get(position);
        holder.name.setText(item.getTitle());

        dbRef = fDatabase.getReference().child("users").child(item.getUserID()).child("name");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uName = (String)dataSnapshot.getValue();
                holder.user.setText(uName);
                System.out.println(uName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dbRef = fDatabase.getReference().child("Comments").child(item.getPostId()).child("comments");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Object> list = (ArrayList)dataSnapshot.getValue();
                if(list == null)
                    holder.comments.setText("Comments(0)");
                else
                    holder.comments.setText("Comments(" + list.size() + ")");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        dbRef = fDatabase.getReference().child("Comments").child(item.getPostId()).child("attending");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Object> list = (ArrayList)dataSnapshot.getValue();
                if(list == null)
                    holder.attending.setText("Attending(0)");
                else
                    holder.attending.setText("Attending(" + list.size() + ")");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

        public TextView name, user, comments, attending;


        public ViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            name = (TextView) itemView.findViewById(R.id.title_label);
            user = itemView.findViewById(R.id.user);
            comments = itemView.findViewById(R.id.comments);
            attending = itemView.findViewById(R.id.attending);

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
            intent.putExtra("CommentsID",item.getPostId());

            context.startActivity(intent);
            //Toast.makeText(context, item.getTitle(), Toast.LENGTH_LONG).show();
        }
    }
}
