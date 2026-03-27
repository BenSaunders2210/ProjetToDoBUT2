package com.iut.projettodobut2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.iut.projettodobut2.databinding.ActivityMainBinding;

import java.util.ArrayList;

/**
 *
 * @author Saunders Benjamin, Maillard Jeremie
 */
public class taskListActivity extends AppCompatActivity {
    private ArrayList<Task> tasks;

    private void onCreate(){
        String[] test = {""};
        this.tasks.add(new Task("Aller à l'école", "BUS 1 à 7h20 à République", Severity.LOW, "", "", test));
    }
}
