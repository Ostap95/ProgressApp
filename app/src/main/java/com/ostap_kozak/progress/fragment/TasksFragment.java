package com.ostap_kozak.progress.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ostap_kozak.progress.R;
import com.ostap_kozak.progress.activity.MainActivity;
import com.ostap_kozak.progress.adapter.TaskAdapter;
import com.ostap_kozak.progress.model.Task;
import com.ostap_kozak.progress.util.SimpleDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by ostapkozak on 08/09/2016.
 */
public class TasksFragment extends Fragment {

    private FragmentActivity listener;
    private TaskAdapter mAdapter;
    private Realm mRealm;
    private RealmConfiguration realmConfig;
    private RecyclerView mRecyclerView;

    /*
        The onCreateView method is called when Fragment should create its View object object hierarchy,
        either dynamically or via XML layout inflation.
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Defines de XML file for the fragment.
        return inflater.inflate(R.layout.fragment_tasks,container, false);
    }

    /*
        This event is triggered soon after onCreateView().
        Any view setup should occur here. E.g., view lookups and attaching view listeners.
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Asynchronous query
        RealmResults<Task> mResults = mRealm.where(Task.class).findAllSortedAsync("taskName");

        // Tell me when the results are loaded so that I can tell my Adapter to update what it shows
        mResults.addChangeListener(new RealmChangeListener<RealmResults<Task>>() {
            @Override
            public void onChange(RealmResults<Task> element) {
                mAdapter.notifyDataSetChanged();
            }
        });

        // Lookup the RecyclerView in activity layout
        mRecyclerView = (RecyclerView) view.findViewById(R.id.tasks_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mAdapter = new TaskAdapter(getActivity(), mRealm, mResults);
        mAdapter.setHasStableIds(true);
        // Attach the adapter to the recyclerView to populate items
        mRecyclerView.setAdapter(mAdapter);

        // Set layout manager to position the items
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        mRecyclerView.setItemAnimator(new SlideInUpAnimator());
        mAdapter.add("Learning");

    }

    /*
        This event fires 1st, before creation of fragment or any views.
        The onAttach method is called when the Fragment instance is associated with an Activity.
        This does not mean the Activity is fully initialized.
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            this.listener = (FragmentActivity) context;
        }
    }

    /*
        This event fires 2nd, before views are created for the fragment.
        The onCreate method is called when the Fragment instance is being created, or re-created.
        Use onCreate for any standard setup that doesn't require the activity to be fully created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment when activity is re-initialized
        setRetainInstance(true);
        // Create the Realm configuration
        realmConfig = new RealmConfiguration.Builder(getActivity()).build();
        mRealm = Realm.getInstance(realmConfig);
    }
    /*
        This method is called when the fragment is no longer connected to the Activity.
        Any references saved onAttach should be nulled out here to prevent memory leaks.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

}
