package com.iut.projettodobut2;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Adapter de la ListView des tâches de l'utilisateur.
 *
 * @author Saunders Benjamin, Maillard Jeremie
 */
public class Adapter extends BaseAdapter {

    /**
     * Interface interne pour l'action de swipe.
     */
    public interface SwipeActionListener {
        void onDelete(int position);
        void onEdit(int position);
    }

    /**
     * La liste des tâches saisies par l'utilisateur.
     */
    private List<Task> listTasks;

    private Context context;
    private LayoutInflater inflater;
    /**
     * Les tâches avec les description visibles.
     */
    private Set<Integer> openItems = new HashSet<>();
    private Set<Integer> expandedItems = new HashSet<>();
    /**
     * Attends pour l'action de l'utilisater de swipe.
     */
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

    /**
     * Ouverture de la description de la tâche.
     * @param position la position de la tâche a ouvrir.
     */
    public void openItem(int position) {
        openItems.add(position);
        notifyDataSetChanged();
    }

    /**
     * Fermeture de la description de la tâche.
     * @param position la position de la tâche a fermer.
     */
    public void closeItem(int position) {
        openItems.remove((Integer) position);
        notifyDataSetChanged();
    }

    /**
     * Ouverture ou fermerture de la description des tâches.
     * @param position la position dont doit ouvrir la description.
     * @return true si c'est ouvert, false sinon.
     */
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
        TextView chevron     = view.findViewById(R.id.task_chevron);
        LinearLayout details   = view.findViewById(R.id.task_details);
        LinearLayout container = view.findViewById(R.id.task_item_container);
        Button btnEdit   = view.findViewById(R.id.btn_edit);
        Button btnDelete = view.findViewById(R.id.btn_delete);

        Task currentTask = listTasks.get(i);

        title.setText(currentTask.getTitre());
        description.setText(currentTask.getDescription());
        dateDebut.setText(context.getString(R.string.task_date_debut, currentTask.getDateDebut().toString()));
        dateFin.setText(context.getString(R.string.task_date_fin, currentTask.getDateFin().toString()));
        severity.setText(context.getString(R.string.task_severity, currentTask.getSeverite().toString()));

        // Couleurs selon la sévérité
        int badgeColor;
        int cardColor;
        switch (currentTask.getSeverite()) {
            case LOW:
                badgeColor = Color.parseColor("#66BB6A");
                cardColor  = Color.parseColor("#F1F8E9");
                break;
            case MEDIUM:
                badgeColor = Color.parseColor("#FFA726");
                cardColor  = Color.parseColor("#FFF8E1");
                break;
            case HIGH:
            default:
                badgeColor = Color.parseColor("#EF5350");
                cardColor  = Color.parseColor("#FCE4EC");
                break;
        }

        // Badge sévérité : GradientDrawable créé proprement en Java (évite tout cast risqué)
        GradientDrawable badge = new GradientDrawable();
        badge.setShape(GradientDrawable.RECTANGLE);
        badge.setCornerRadius(dpToPx(12));
        badge.setColor(badgeColor);
        severity.setBackground(badge);
        severity.setTextColor(Color.WHITE);

        // Fond de la carte : GradientDrawable créé proprement en Java
        GradientDrawable card = new GradientDrawable();
        card.setShape(GradientDrawable.RECTANGLE);
        card.setCornerRadius(dpToPx(20));
        card.setColor(cardColor);
        card.setStroke((int) dpToPx(1), Color.parseColor("#E8EAF6"));
        container.setBackground(card);

        // État expand/collapse sans animation lors du rebind
        boolean isExpanded = expandedItems.contains(i);
        details.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        chevron.setText(isExpanded ? "▲" : "▼");

        // Position du swipe
        float revealPx = dpToPx(160);
        container.setTranslationX(openItems.contains(i) ? -revealPx : 0);

        final int position = i;

        container.setOnClickListener(v -> {
            if (openItems.contains(position)) {
                closeItem(position);
            } else {
                boolean expanded = expandedItems.contains(position);
                if (!expanded) {
                    expandedItems.add(position);
                    details.setVisibility(View.VISIBLE);
                    Animation slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);
                    details.startAnimation(slideDown);
                    chevron.setText("▲");
                } else {
                    expandedItems.remove((Integer) position);
                    Animation slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up);
                    slideUp.setAnimationListener(new Animation.AnimationListener() {
                        @Override public void onAnimationStart(Animation a) {}
                        @Override public void onAnimationRepeat(Animation a) {}
                        @Override public void onAnimationEnd(Animation a) {
                            details.setVisibility(View.GONE);
                        }
                    });
                    details.startAnimation(slideUp);
                    chevron.setText("▼");
                }
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

    /**
     * Conversion de dp en pixels.
     * @param dp la taille à convertir.
     * @return la conversion de la taille.
     */
    private float dpToPx(int dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}