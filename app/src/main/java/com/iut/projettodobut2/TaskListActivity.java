package com.iut.projettodobut2;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDate;
import java.util.ArrayList;

public class TaskListActivity extends Activity {
    private ArrayList<Task> tasks = new ArrayList<>(); // ← initialized here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] test = {"Tag"};
        tasks.add(new Task("Aller à l'école", "BUS 1 à 7h20 à République", Severity.LOW, LocalDate.of(2026, 3, 31), LocalDate.of(2026, 4, 1), test));
        tasks.add(new Task("Faire les courses", "Lait, pain, oeufs", Severity.MEDIUM, LocalDate.of(2026, 3, 31), LocalDate.of(2026, 4, 1), test));
        tasks.add(new Task("Rendre le TP sur moodle", "R4.A.11 TP ToDo Moodle", Severity.HIGH, LocalDate.of(2026, 3, 31), LocalDate.of(2026, 4, 3), test));
        ListView list = findViewById(R.id.task_list);
        ConstraintLayout noTasks = findViewById(R.id.no_tasks);
        list.setEmptyView(noTasks);
        list.setAdapter(new Adapter(this, tasks));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.constraintLayout2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}