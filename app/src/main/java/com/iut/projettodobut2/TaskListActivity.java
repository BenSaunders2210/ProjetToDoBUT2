package com.iut.projettodobut2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class TaskListActivity extends Activity {

    private static final int REQUEST_CREATE_TASK = 1;
    private static final int REQUEST_EDIT_TASK = 2;

    private ArrayList<Task> tasks = new ArrayList<>();
    private ArrayList<Task> filteredTasks = new ArrayList<>();
    private Adapter adapter;
    private ListView list;
    private LinearLayout tagsContainer;

    private Set<Severity> activeFilters = EnumSet.noneOf(Severity.class);
    private Set<String> activeTagFilters = new HashSet<>();

    private Button btnFaible, btnMoyen, btnUrgent;
    private float touchStartX, touchStartY;
    private static final int SWIPE_THRESHOLD = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialisation des vues
        list = findViewById(R.id.task_list);
        tagsContainer = findViewById(R.id.tags_container);
        ConstraintLayout noTasks = findViewById(R.id.no_tasks);
        list.setEmptyView(noTasks);

        btnFaible = findViewById(R.id.btn_filter_low);
        btnMoyen  = findViewById(R.id.btn_filter_medium);
        btnUrgent = findViewById(R.id.btn_filter_high);

        // Données de test initiales
   /*     tasks.add(new Task("Réviser Android", "Pratiquer les Intents", Severity.HIGH, LocalDate.now(), LocalDate.now().plusDays(2), new String[]{"Ecole", "Dev"}));
        tasks.add(new Task("Courses", "Acheter du café", Severity.LOW, LocalDate.now(), LocalDate.now().plusDays(1), new String[]{"Perso"}));
*/
        // Listeners sévérité
        btnFaible.setOnClickListener(v -> toggleSeverityFilter(Severity.LOW, btnFaible));
        btnMoyen.setOnClickListener(v -> toggleSeverityFilter(Severity.MEDIUM, btnMoyen));
        btnUrgent.setOnClickListener(v -> toggleSeverityFilter(Severity.HIGH, btnUrgent));

        updateButtonState(btnFaible, false, "#66BB6A");
        updateButtonState(btnMoyen,  false, "#FFA726");
        updateButtonState(btnUrgent, false, "#EF5350");

        findViewById(R.id.btn_add_task).setOnClickListener(v -> {
            startActivityForResult(new Intent(this, TaskCreateActivity.class), REQUEST_CREATE_TASK);
        });

        adapter = new Adapter(this, filteredTasks, new Adapter.SwipeActionListener() {
            @Override public void onDelete(int pos) { tasks.remove(filteredTasks.get(pos)); applyFilters(); }
            @Override public void onEdit(int pos) {
                Task t = filteredTasks.get(pos);
                Intent intent = new Intent(TaskListActivity.this, TaskEditActivity.class);
                intent.putExtra("task_index", tasks.indexOf(t));
                intent.putExtra("task_title", t.getTitle());
                intent.putExtra("task_description", t.getDescription());
                intent.putExtra("task_severity", t.getSeverity().name());
                intent.putExtra("task_date_fin", t.getDateFin().toString());
                intent.putExtra("task_tags", t.getTags());
                startActivityForResult(intent, REQUEST_EDIT_TASK);
                adapter.closeItem(pos);
            }
        });
        list.setAdapter(adapter);

        applyFilters();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.constraintLayout2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void applyFilters() {
        filteredTasks.clear();
        for (Task task : tasks) {
            boolean severityMatch = activeFilters.isEmpty() || activeFilters.contains(task.getSeverity());
            boolean tagMatch = activeTagFilters.isEmpty();
            if (!tagMatch && task.getTags() != null) {
                for (String tag : task.getTags()) {
                    if (activeTagFilters.contains(tag)) { tagMatch = true; break; }
                }
            }
            if (severityMatch && tagMatch) filteredTasks.add(task);
        }
        updateTagList();
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    private void updateTagList() {
        tagsContainer.removeAllViews();
        Set<String> allUniqueTags = new HashSet<>();
        for (Task t : tasks) {
            if (t.getTags() != null) {
                for (String tag : t.getTags()) if (!tag.trim().isEmpty()) allUniqueTags.add(tag.trim());
            }
        }

        for (String tag : allUniqueTags) {
            Button btn = new Button(this);
            btn.setText(tag);
            btn.setAllCaps(false);
            boolean isActive = activeTagFilters.contains(tag);

            // Style du tag
            GradientDrawable gd = new GradientDrawable();
            gd.setCornerRadius(dpToPx(15));
            gd.setColor(isActive ? Color.parseColor("#3F51B5") : Color.parseColor("#E0E0E0"));
            btn.setBackground(gd);
            btn.setTextColor(isActive ? Color.WHITE : Color.BLACK);

            btn.setOnClickListener(v -> {
                if (isActive) activeTagFilters.remove(tag);
                else activeTagFilters.add(tag);
                applyFilters();
            });

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, (int)dpToPx(35));
            lp.setMargins(0, 0, (int)dpToPx(8), 0);
            tagsContainer.addView(btn, lp);
        }
    }

    private void toggleSeverityFilter(Severity s, Button b) {
        if (activeFilters.contains(s)) activeFilters.remove(s);
        else activeFilters.add(s);

        String color = (s == Severity.LOW) ? "#66BB6A" : (s == Severity.MEDIUM ? "#FFA726" : "#EF5350");
        updateButtonState(b, activeFilters.contains(s), color);
        applyFilters();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            String title = data.getStringExtra("task_title");
            String desc = data.getStringExtra("task_description");
            Severity sev = Severity.valueOf(data.getStringExtra("task_severity"));
            LocalDate dFin = LocalDate.parse(data.getStringExtra("task_date_fin"));
            String[] tags = data.getStringArrayExtra("task_tags");

            if (requestCode == REQUEST_CREATE_TASK) {
                tasks.add(new Task(title, desc, sev, LocalDate.now(), dFin, tags));
            } else if (requestCode == REQUEST_EDIT_TASK) {
                int idx = data.getIntExtra("task_index", -1);
                if (idx != -1) {
                    Task t = tasks.get(idx);
                    t.setTitle(title); t.setDescription(desc); t.setSeverity(sev); t.setDateFin(dFin); t.setTags(tags);
                }
            }
            applyFilters();
        }
    }

    private void updateButtonState(Button btn, boolean active, String color) {
        GradientDrawable bg = new GradientDrawable();
        bg.setShape(GradientDrawable.RECTANGLE);
        bg.setCornerRadius(dpToPx(18));
        bg.setColor(Color.parseColor(active ? color : "#BDBDBD"));
        btn.setTextColor(active ? Color.WHITE : Color.parseColor("#757575"));
        btn.setBackground(bg);
    }

    private float dpToPx(int dp) { return dp * getResources().getDisplayMetrics().density; }

    // --- Garder ta gestion du swipe (dispatchTouchEvent) à l'identique ---
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) { touchStartX = event.getX(); touchStartY = event.getY(); }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float dX = event.getX() - touchStartX;
            if (Math.abs(dX) > SWIPE_THRESHOLD && Math.abs(dX) > Math.abs(event.getY() - touchStartY)) {
                int pos = getPositionForPoint((int)touchStartX, (int)touchStartY);
                if (pos != ListView.INVALID_POSITION) {
                    if (dX < 0) adapter.openItem(pos); else adapter.closeItem(pos);
                    return true;
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    private int getPositionForPoint(int x, int y) {
        int[] loc = new int[2]; list.getLocationOnScreen(loc);
        int localY = y - loc[1];
        for (int i = 0; i < list.getChildCount(); i++) {
            View v = list.getChildAt(i);
            if (v != null && localY >= v.getTop() && localY <= v.getBottom()) return list.getFirstVisiblePosition() + i;
        }
        return ListView.INVALID_POSITION;
    }
}