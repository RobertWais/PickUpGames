package Adapter;

import android.app.LauncherActivity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.EventLog;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import Model.ListItem;

/**
 * Created by Markus on 4/1/2018.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    private Context context;
    private List<ListItem> listItems;

    public EventAdapter(Context context, List listItem) {

    }


    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(EventAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
