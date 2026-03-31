package com.iut.projettodobut2;



import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Adapteur pour transformer une Task en Vue qui sera affiché dans la ListView.
 *
 * @author Saunders Benjamin, Maillard Jeremie
 */
public class Adapter extends BaseAdapter {
    private List<Task> listTasks;
    private Context context;
    private LayoutInflater inflater;

    public Adapter(Context context, List<Task> listMovies ) {
        this.listTasks = listMovies;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public Object getItem(int i) {
        return this.listTasks.get(i);
    }

    @Override
    public int getCount() {
        return listTasks.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        View view;

        if(convertView == null){
            view = inflater.inflate(R.layout.task_list_item, parent, false);
        } else {
            view = convertView;
        }

        TextView title = view.findViewById(R.id.task_title);
        TextView description = view.findViewById(R.id.task_description);
        TextView dateDebut = view.findViewById(R.id.task_date_debut);
        TextView dateFin = view.findViewById(R.id.task_date_fin);
        TextView severity = view.findViewById(R.id.task_severity);
        LinearLayout details = view.findViewById(R.id.task_details);
        LinearLayout container = view.findViewById(R.id.task_item_container);

        Task currentTask = listTasks.get(i);

        title.setText(currentTask.getTitle());
        description.setText(currentTask.getDescription());
        dateDebut.setText(context.getString(R.string.task_date_debut, currentTask.getDateDebut().toString()));
        dateFin.setText(context.getString(R.string.task_date_fin, currentTask.getDateFin().toString()));
        severity.setText(context.getString(R.string.task_severity, currentTask.getSeverity().toString()));

        // reset état
        details.setVisibility(View.GONE);

        view.setOnClickListener(v -> {
            if(details.getVisibility() == View.GONE){
                details.setVisibility(View.VISIBLE);
            } else {
                details.setVisibility(View.GONE);
            }
        });

        GradientDrawable bg = (GradientDrawable) container.getBackground();

        switch(currentTask.getSeverity()){
            case LOW:
                bg.setColor(Color.parseColor("#cdb4db"));
                break;
            case MEDIUM:
                bg.setColor(Color.parseColor("#ffc8dd"));
                break;
            case HIGH:
                bg.setColor(Color.parseColor("#ffafcc"));
                break;
        }

        return view;
    }
}
