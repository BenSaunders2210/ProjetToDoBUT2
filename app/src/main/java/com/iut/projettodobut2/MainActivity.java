package com.iut.projettodobut2;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDate;

/**
 *
 * @author Saunders Benjamin, Maillard Jeremie
 */
public class MainActivity extends AppCompatActivity {
        private Task[] taskList = {new Task("Aller à l'école", "BUS 1 à 7h20 à République", Severity.LOW, LocalDate.of(2026, 3, 31), LocalDate.of(2026, 4, 1), new String[]{""})};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Launch TaskListActivity immediately
        Intent intent = new Intent(this, TaskListActivity.class);
        startActivity(intent);
        finish(); // prevents going back to a blank MainActivity
    }

}