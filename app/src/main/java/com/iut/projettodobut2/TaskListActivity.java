package com.iut.projettodobut2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Activity de la liste des tâches à afficher.
 *
 * @author Saunders Benjamin, Maillard Jeremie
 */
public class TaskListActivity extends Activity {
    private ArrayList<Task> tasks;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // TODO: Système de Tag : le tag sera null par défaut sur la page d'accueil, donc ici on affiche tout les tâches
        // TODO : si filtre tag == null, sauf si un filtre est choisie, alors ça montre que si filtre tag == tag_choisie
        String[] test = {"Tag"};
        this.tasks.add(new Task("Aller à l'école", "BUS 1 à 7h20 à République", Severity.LOW, LocalDate.of(2026, 3, 31), LocalDate.of(2026, 4, 1), test));
        this.tasks.add(new Task("Faire les courses", "Lait, pain, oeufs", Severity.MEDIUM, LocalDate.of(2026, 3, 31), LocalDate.of(2026, 4, 1), test));
        this.tasks.add(new Task("Rendre le TP sur moodle", "R4.A.11 TP ToDo Moodle", Severity.HIGH, LocalDate.of(2026, 3, 31), LocalDate.of(2026, 4, 3), test));


        Adapter adapter = new Adapter(this, tasks);

        //TODO : Ajout de la liste des tâches dans la ListView + enlever commentaire quand le layout existe.
        ListView list = (ListView) findViewById(R.id.task_list);
        ConstraintLayout noTasks = (ConstraintLayout) findViewById(R.id.no_tasks);
        list.setEmptyView(noTasks);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Task selectedItem = (Task) adapterView.getItemAtPosition(position);
                Toast.makeText(TaskListActivity.this,selectedItem.getTitle(),Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.constraintLayout2), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


}
