package com.iut.projettodobut2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDate;
import java.util.ArrayList;

public class TaskListActivity extends Activity {
    private ArrayList<Task> tasks = new ArrayList<>();
    private Adapter adapter;
    private ListView list;

    private float touchStartX, touchStartY;
    private static final int SWIPE_THRESHOLD = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] test = {"Tag"};
        tasks.add(new Task("Aller à l'école", "BUS 1 à 7h20 à République", Severity.LOW, LocalDate.of(2026, 3, 31), LocalDate.of(2026, 4, 1), test));
        tasks.add(new Task("Faire les courses", "Lait, pain, oeufs", Severity.MEDIUM, LocalDate.of(2026, 3, 31), LocalDate.of(2026, 4, 1), test));
        tasks.add(new Task("Rendre le TP sur moodle", "R4.A.11 TP ToDo Moodle", Severity.HIGH, LocalDate.of(2026, 3, 31), LocalDate.of(2026, 4, 3), test));

        list = findViewById(R.id.task_list);
        ConstraintLayout noTasks = findViewById(R.id.no_tasks);
        list.setEmptyView(noTasks);

        Button btnAddTask = (Button) findViewById(R.id.btn_add_task);

        btnAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(TaskListActivity.this, TaskCreateActivity.class);
            startActivity(intent);
        });

        adapter = new Adapter(this, tasks, new Adapter.SwipeActionListener() {
            @Override
            public void onDelete(int position) {
                tasks.remove(position);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onEdit(int position) {
                Intent intent = new Intent(TaskListActivity.this, TaskEditActivity.class);
                startActivity(intent);
            }
        });

        list.setAdapter(adapter);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.constraintLayout2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Intercept ALL touches at the Activity level — before any view sees them
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchStartX = event.getX();
                touchStartY = event.getY();
                break;

            case MotionEvent.ACTION_UP:
                float dX = event.getX() - touchStartX;
                float dY = event.getY() - touchStartY;

                if (Math.abs(dX) > Math.abs(dY) && Math.abs(dX) > SWIPE_THRESHOLD) {
                    int position = getPositionForPoint((int) touchStartX, (int) touchStartY);
                    if (position != ListView.INVALID_POSITION) {
                        if (dX < 0) {
                            adapter.openItem(position);
                        } else {
                            adapter.closeItem(position);
                        }
                        return true;
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    private int getPositionForPoint(int x, int y) {
        int[] listLocation = new int[2];
        list.getLocationOnScreen(listLocation);

        int localY = y - listLocation[1];

        for (int idx = 0; idx < list.getChildCount(); idx++) {
            View child = list.getChildAt(idx);
            if (child != null && localY >= child.getTop() && localY <= child.getBottom()) {
                return list.getFirstVisiblePosition() + idx;
            }
        }
        return ListView.INVALID_POSITION;
    }
}