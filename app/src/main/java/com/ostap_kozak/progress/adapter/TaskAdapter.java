package com.ostap_kozak.progress.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ostap_kozak.progress.R;
import com.ostap_kozak.progress.model.Task;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by ostapkozak on 08/09/2016.
 * The adapter's role is to convert an object at a position into a list row item to be inserted.
 * With a RecyclerView the adapter requires the existence of a "ViewHolder" object,
 * which describes and provides access to all the views within each item row.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    // Store context for easy access

    private LayoutInflater mInflater;
    private Realm mRealm;
    private RealmResults<Task> mResults; // RealmResults is basically live array


    public TaskAdapter(Context context, Realm realm, RealmResults<Task> results) {
        mRealm = realm;
        mInflater = LayoutInflater.from(context);
        setResults(results);
    }

    public Task getItem(int position) {
        return mResults.get(position);
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public TaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_task, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(TaskAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Task task = mResults.get(position);
        // Set the views based on your view and data model.
        holder.setData(task.getTaskName());
    }

    public void setResults(RealmResults<Task> results) {
        mResults = results;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return mResults.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    public void add(String taskName) {

        // Create new object that contains the data we want to add;
        Task task = new Task(taskName);

        //Set the id of creation of this object as the current time
        task.setId(System.currentTimeMillis());

        // Start a transaction
        mRealm.beginTransaction();

        //Copy or update the object if it already exists, update is possible only if your table has a primary key
        mRealm.copyToRealmOrUpdate(task);

        //Commit the transaction
        mRealm.commitTransaction();

        //Tell the Adapter to update what it shows.
        notifyDataSetChanged();
    }

    public void remove(int position) {

        //Start a transaction
        mRealm.beginTransaction();

        //Remove the item from the desired position
        mResults.remove(position);

        //Commit the transaction
        mRealm.commitTransaction();

        //Tell the Adapter to update what it shows
        notifyItemRemoved(position);
    }
    /**
     * Provide a direct reference to each of the view within a data item.
     * Used to cache the views within the item layout for fast access.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        /*
            Your holder should contain a member variable for any view that will be set as you render a row
         */
        public TextView taskName_view;
        /*
            Stores the itemView in a public final member variable that can be used
            to access the context from any ViewHolder instance.
         */
        public ViewHolder(View itemView) {
            super(itemView);
            taskName_view = (TextView) itemView.findViewById(R.id.task_name);
        }

        public void setData(String taskName) {
            taskName_view.setText(taskName);
        }
    }
}
