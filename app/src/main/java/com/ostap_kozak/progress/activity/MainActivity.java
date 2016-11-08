package com.ostap_kozak.progress.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.ostap_kozak.progress.R;
import com.ostap_kozak.progress.fragment.AddTaskFragment;
import com.ostap_kozak.progress.fragment.TasksFragment;
import com.ostap_kozak.progress.model.Task;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AddTaskFragment.AddActivityDialogListener {

    private Fragment tasks_fragment;
    private RealmConfiguration realmConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        realmConfig = new RealmConfiguration.Builder(this).build();

        tasks_fragment = new TasksFragment();

        // Creates toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // FloatingActionBar
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                AddTaskFragment addTask = AddTaskFragment.newInstance();
                addTask.show(fm, "Add_tas");

            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        displayTasksFragment();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /*
    Called when an item in the navigation menu is selected.
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_tasks) {
            // Handle the task action
            // Begin the transaction
            displayTasksFragment();

        } else if (id == R.id.nav_statistics) {
            // Handle the statistics action
        } else if (id == R.id.nav_settings) {
            // Handle settings menu
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected void displayTasksFragment() {
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        /*if (tasks_fragment.isAdded()) { // if the fragment is already in container
            ft.show(tasks_fragment);
        } else { // fragment needs to be added to frame container
            ft.add(R.id.fragment_placeholder, tasks_fragment, "taskFragment");
        }

        // Hide other fragments
        // if (fragmentB.isAdded()) { ft.hide(fragmentB); }
        // Commit changes
        ft.commit();
*/
        ft.replace(R.id.fragment_placeholder, new TasksFragment());
        ft.commit();
        getSupportActionBar().setTitle("Tasks");
    }

    /*
        This method is called when the user starts the new task in AddTaskFragment
     */
    @Override
    public void onFinishAddTask(String taskName, int hours, int minutes, int seconds) {
        addToDatabase(taskName);
    }


    /*
        Adds new task to the database
     */
    public void addToDatabase(String taskName) {

        Task task = new Task(taskName);
        // Obtain a Realm instance
        Realm realm = Realm.getInstance(realmConfig);

        realm.beginTransaction();

        realm.copyToRealmOrUpdate(task);

        realm.commitTransaction();

        Intent intent = new Intent(this, RunningTask.class);
        intent.putExtra(RunningTask.TASK_NAME, taskName);
        startActivity(intent);
    }


}
