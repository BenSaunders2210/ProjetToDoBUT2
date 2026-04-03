package com.iut.projettodobut2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import java.util.EnumSet;
import java.util.Set;

public class TaskListActivity extends Activity {

    private static final int REQUEST_CREATE_TASK = 1;

    private ArrayList<Task> tasks = new ArrayList<>();
    private ArrayList<Task> filteredTasks = new ArrayList<>();
    private Adapter adapter;
    private ListView list;

    // Filtres actifs (aucun actif par défaut)
    private Set<Severity> activeFilters = EnumSet.noneOf(Severity.class);

    private Button btnFaible, btnMoyen, btnUrgent;

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

        // Boutons de filtre sévérité (navbar bas)
        btnFaible = findViewById(R.id.btn_filter_low);
        btnMoyen  = findViewById(R.id.btn_filter_medium);
        btnUrgent = findViewById(R.id.btn_filter_high);

        btnFaible.setOnClickListener(v -> toggleFilter(Severity.LOW,    btnFaible));
        btnMoyen .setOnClickListener(v -> toggleFilter(Severity.MEDIUM, btnMoyen));
        btnUrgent.setOnClickListener(v -> toggleFilter(Severity.HIGH,   btnUrgent));

        // Apparence initiale (tous grisés)
        updateButtonState(btnFaible, false, "#66BB6A");
        updateButtonState(btnMoyen,  false, "#FFA726");
        updateButtonState(btnUrgent, false, "#EF5350");

        // Bouton ajouter — utilise startActivityForResult pour récupérer la tâche créée
        Button btnAddTask = findViewById(R.id.btn_add_task);
        btnAddTask.setOnClickListener(v -> {
            Intent intent = new Intent(TaskListActivity.this, TaskCreateActivity.class);
            startActivityForResult(intent, REQUEST_CREATE_TASK);
        });

        // Adapter sur filteredTasks (pas tasks directement)
        applyFilters();
        adapter = new Adapter(this, filteredTasks, new Adapter.SwipeActionListener() {
            @Override
            public void onDelete(int position) {
                Task toRemove = filteredTasks.get(position);
                tasks.remove(toRemove);
                applyFilters();
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

    // Reçoit la nouvelle tâche créée dans TaskCreateActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CREATE_TASK && resultCode == RESULT_OK && data != null) {
            String title        = data.getStringExtra("task_title");
            String description  = data.getStringExtra("task_description");
            Severity severity   = Severity.valueOf(data.getStringExtra("task_severity"));
            LocalDate dateDebut = LocalDate.parse(data.getStringExtra("task_date_debut"));
            LocalDate dateFin   = LocalDate.parse(data.getStringExtra("task_date_fin"));
            String[] tags       = data.getStringArrayExtra("task_tags");

            tasks.add(new Task(title, description, severity, dateDebut, dateFin, tags));
            applyFilters(); // reapply so the new task respects active filters
        }
    }

    // --- Logique de filtre ---

    private void toggleFilter(Severity severity, Button btn) {
        String color;
        switch (severity) {
            case LOW:    color = "#66BB6A"; break;
            case MEDIUM: color = "#FFA726"; break;
            case HIGH:
            default:     color = "#EF5350"; break;
        }

        if (activeFilters.contains(severity)) {
            activeFilters.remove(severity);
            updateButtonState(btn, false, color);
        } else {
            activeFilters.add(severity);
            updateButtonState(btn, true, color);
        }

        applyFilters();
    }

    private void applyFilters() {
        filteredTasks.clear();
        if (activeFilters.isEmpty()) {
            // Aucun filtre actif = tout afficher
            filteredTasks.addAll(tasks);
        } else {
            for (Task task : tasks) {
                if (activeFilters.contains(task.getSeverity())) {
                    filteredTasks.add(task);
                }
            }
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    /** Applique un style actif (coloré) ou inactif (grisé) au bouton */
    private void updateButtonState(Button btn, boolean active, String activeColor) {
        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.RECTANGLE);
        bg.setCornerRadius(dpToPx(18));
        if (active) {
            bg.setColor(Color.parseColor(activeColor));
            btn.setTextColor(Color.WHITE);
            btn.setAlpha(1.0f);
        } else {
            bg.setColor(Color.parseColor("#BDBDBD"));
            btn.setTextColor(Color.parseColor("#757575"));
            btn.setAlpha(0.7f);
        }
        btn.setBackground(bg);
    }

    private float dpToPx(int dp) {
        return dp * getResources().getDisplayMetrics().density;
    }

    // --- Gestion du swipe ---

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