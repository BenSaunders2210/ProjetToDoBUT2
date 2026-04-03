package com.iut.projettodobut2;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Adapter extends BaseAdapter {

    public interface SwipeActionListener {
        void onDelete(int position);
        void onEdit(int position);
    }

    private List<Task> listTasks;
    private Context context;
    private LayoutInflater inflater;
    private Set<Integer> openItems = new HashSet<>();
    private SwipeActionListener listener;

    public Adapter(Context context, List<Task> listTasks, SwipeActionListener listener) {
        this.listTasks = listTasks;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override public Object getItem(int i) { return listTasks.get(i); }
    @Override public int getCount() { return listTasks.size(); }
    @Override public long getItemId(int i) { return i; }

    public void openItem(int position) {
        openItems.add(position);
        notifyDataSetChanged();
    }

    public void closeItem(int position) {
        openItems.remove((Integer) position); // ← cast to Integer, not index removal
        notifyDataSetChanged();
    }

    public boolean isOpen(int position) {
        return openItems.contains(position);
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = inflater.inflate(R.layout.task_list_item, parent, false);
        } else {
            view = convertView;
        }

        TextView title       = view.findViewById(R.id.task_title);
        TextView description = view.findViewById(R.id.task_description);
        TextView dateDebut   = view.findViewById(R.id.task_date_debut);
        TextView dateFin     = view.findViewById(R.id.task_date_fin);
        TextView severity    = view.findViewById(R.id.task_severity);
        LinearLayout details   = view.findViewById(R.id.task_details);
        LinearLayout container = view.findViewById(R.id.task_item_container);
        Button btnEdit   = view.findViewById(R.id.btn_edit);
        Button btnDelete = view.findViewById(R.id.btn_delete);

        Task currentTask = listTasks.get(i);

        title.setText(currentTask.getTitle());
        description.setText(currentTask.getDescription());
        dateDebut.setText(context.getString(R.string.task_date_debut, currentTask.getDateDebut().toString()));
        dateFin.setText(context.getString(R.string.task_date_fin, currentTask.getDateFin().toString()));
        severity.setText(context.getString(R.string.task_severity, currentTask.getSeverity().toString()));

        details.setVisibility(View.GONE);

        float revealPx = dpToPx(160);
        container.setTranslationX(openItems.contains(i) ? -revealPx : 0);

        GradientDrawable bg = (GradientDrawable) container.getBackground();
        switch (currentTask.getSeverity()) {
            case LOW:    bg.setColor(Color.parseColor("#cdb4db")); break;
            case MEDIUM: bg.setColor(Color.parseColor("#ffc8dd")); break;
            case HIGH:   bg.setColor(Color.parseColor("#ffafcc")); break;
        }

        final int position = i;

        container.setOnClickListener(v -> {
            if (openItems.contains(position)) {
                closeItem(position);
            } else {
                details.setVisibility(
                        details.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });

        btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(position);
        });

        btnEdit.setOnClickListener(v -> {
            if (listener != null) listener.onEdit(position);
        });



        return view;
    }

    private float dpToPx(int dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}