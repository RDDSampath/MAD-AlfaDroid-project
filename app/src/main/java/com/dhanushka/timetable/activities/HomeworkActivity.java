package com.dhanushka.timetable.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.dhanushka.timetable.adapters.HomeworksAdapter;
import com.dhanushka.timetable.model.Homework;
import com.dhanushka.timetable.R;
import com.dhanushka.timetable.utils.AlertDialogsHelper;
import com.dhanushka.timetable.utils.DbHelper;

import java.util.ArrayList;


public class HomeworkActivity extends AppCompatActivity {

    private HomeworkActivity context = this;
    private ListView listView;
    private HomeworksAdapter adapter;
    private DbHelper db;
    private int listposition = 0;
    




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework);
        initAll();


    }



    private void initAll() {
        setupAdapter();
        setupListViewMultiSelect();
        setupCustomDialog();

    }

    private void setupAdapter() {
        db = new DbHelper(context);
        listView = findViewById(R.id.homeworklist);
        adapter = new HomeworksAdapter(HomeworkActivity.this, listView, R.layout.listview_homework, db.getHomework());
        listView.setAdapter(adapter);
    }

    private void setupListViewMultiSelect() {
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                listposition = position;
                final int checkedCount = listView.getCheckedItemCount();
                mode.setTitle(checkedCount + " " + getResources().getString(R.string.selected));
                if(checkedCount == 0) mode.finish();
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater menuInflater = mode.getMenuInflater();
                menuInflater.inflate(R.menu.toolbar_action_mode, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        ArrayList<Homework> removelist = new ArrayList<>();
                        SparseBooleanArray checkedItems = listView.getCheckedItemPositions();
                        for (int i = 0; i < checkedItems.size(); i++) {
                            int key = checkedItems.keyAt(i);
                            if (checkedItems.get(key)) {
                                db.deleteHomeworkById(adapter.getItem(key));
                                removelist.add(adapter.getHomeworkList().get(key));
                            }
                        }
                        adapter.getHomeworkList().removeAll(removelist);
                        db.updateHomework(adapter.getHomework());
                        adapter.notifyDataSetChanged();
                        mode.finish();
                        return true;

                    default:
                        return false;
                }
            }
            @Override
            public void onDestroyActionMode(ActionMode mode) {
            }
        });
    }

    private void setupCustomDialog() {
        final View alertLayout = getLayoutInflater().inflate(R.layout.dialog_add_homework, null);
        AlertDialogsHelper.getAddHomeworkDialog(HomeworkActivity.this, alertLayout, adapter);
    }
}
