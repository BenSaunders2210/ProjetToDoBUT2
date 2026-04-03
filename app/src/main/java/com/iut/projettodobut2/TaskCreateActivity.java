package com.iut.projettodobut2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDate;
import java.util.Calendar;

/**
 * Controller de la page création.
 *
 * @author Saunders Benjamin, Maillard Jeremie
 */
public class TaskCreateActivity extends Activity {

    // Sauvegarde la date choisi par l'utilisateur
    private LocalDate selectedDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task);

        // --- Spinner setup ---
        Spinner spinner = findViewById(R.id.severite);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.severite_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // --- Date button ---
        Button btnDate = findViewById(R.id.date);
        btnDate.setOnClickListener(v -> {
            Calendar cal = Calendar.getInstance();
            int year  = selectedDate != null ? selectedDate.getYear()       : cal.get(Calendar.YEAR);
            int month = selectedDate != null ? selectedDate.getMonthValue() - 1 : cal.get(Calendar.MONTH);
            int day   = selectedDate != null ? selectedDate.getDayOfMonth() : cal.get(Calendar.DAY_OF_MONTH);

            new DatePickerDialog(this, (view, y, m, d) -> {
                selectedDate = LocalDate.of(y, m + 1, d);
                btnDate.setText(String.format("%02d/%02d/%04d", d, m + 1, y));
                btnDate.setTextColor(0xFF000000); // turn black once a date is picked
            }, year, month, day).show();
        });

        // --- Ajouter button ---
        Button btnAjouter = findViewById(R.id.btn_ajout);
        btnAjouter.setOnClickListener(v -> {

            EditText editTitre       = findViewById(R.id.titre);
            EditText editDescription = findViewById(R.id.description);
            EditText editTags        = findViewById(R.id.tags);

            String titre       = editTitre.getText().toString().trim();
            String description = editDescription.getText().toString().trim();
            String tagsStr     = editTags.getText().toString().trim();

            // --- Validation ---
            if (titre.isEmpty()) {
                editTitre.setError("Le titre est obligatoire");
                return;
            }
            if (selectedDate == null) {
                Toast.makeText(this, "Veuillez sélectionner une date", Toast.LENGTH_SHORT).show();
                return;
            }


            Severity severity;
            switch (spinner.getSelectedItemPosition()) {
                case 0:  severity = Severity.LOW;   break;
                case 1:  severity = Severity.MEDIUM; break;
                default: severity = Severity.HIGH;    break;
            }


            String[] tags;
            if (tagsStr.isEmpty()) {
                tags = new String[]{};
            } else {
                String[] raw = tagsStr.split(",");
                tags = new String[raw.length];
                for (int i = 0; i < raw.length; i++) {
                    tags[i] = raw[i].trim();
                }
            }


            Task newTask = new Task(titre, description, severity, LocalDate.now(), selectedDate, tags);

            Intent result = new Intent();
            result.putExtra("task_title",       newTask.getTitre());
            result.putExtra("task_description", newTask.getDescription());
            result.putExtra("task_severity",    newTask.getSeverite().name());
            result.putExtra("task_date_debut",  newTask.getDateDebut().toString());
            result.putExtra("task_date_fin",    newTask.getDateFin().toString());
            result.putExtra("task_tags",        newTask.getTags());

            setResult(RESULT_OK, result);
            finish();

        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.parent_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}