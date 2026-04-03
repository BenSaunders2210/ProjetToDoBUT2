package com.iut.projettodobut2;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import java.time.LocalDate;
import java.util.Calendar;

public class TaskEditActivity extends Activity {
    private LocalDate selectedDate = null;
    private int taskIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_task);

        EditText editTitre = findViewById(R.id.titre);
        EditText editDescription = findViewById(R.id.description);
        EditText editTags = findViewById(R.id.tags);
        Spinner spinner = findViewById(R.id.severite);
        Button btnDate = findViewById(R.id.date);
        Button btnSave = findViewById(R.id.btn_ajout);

        // Initialisation Spinner
        ArrayAdapter<CharSequence> sAdapter = ArrayAdapter.createFromResource(this, R.array.severite_array, android.R.layout.simple_spinner_item);
        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(sAdapter);

        // Récupération Intent
        Intent intent = getIntent();
        taskIndex = intent.getIntExtra("task_index", -1);
        editTitre.setText(intent.getStringExtra("task_title"));
        editDescription.setText(intent.getStringExtra("task_description"));
        String[] tags = intent.getStringArrayExtra("task_tags");
        if (tags != null) editTags.setText(String.join(", ", tags));

        // Sévérité
        String sev = intent.getStringExtra("task_severity");
        if (sev != null) {
            Severity s = Severity.valueOf(sev);
            spinner.setSelection(s == Severity.LOW ? 0 : (s == Severity.MEDIUM ? 1 : 2));
        }

        // Date
        selectedDate = LocalDate.parse(intent.getStringExtra("task_date_fin"));
        btnDate.setText(selectedDate.toString());
        btnDate.setOnClickListener(v -> {
            new DatePickerDialog(this, (view, y, m, d) -> {
                selectedDate = LocalDate.of(y, m + 1, d);
                btnDate.setText(selectedDate.toString());
            }, selectedDate.getYear(), selectedDate.getMonthValue()-1, selectedDate.getDayOfMonth()).show();
        });

        btnSave.setText("Modifier");
        btnSave.setOnClickListener(v -> {
            String[] finalTags = editTags.getText().toString().split(",");
            for(int i=0; i<finalTags.length; i++) finalTags[i] = finalTags[i].trim();

            Intent res = new Intent();
            res.putExtra("task_index", taskIndex);
            res.putExtra("task_title", editTitre.getText().toString());
            res.putExtra("task_description", editDescription.getText().toString());
            res.putExtra("task_severity", (spinner.getSelectedItemPosition()==0 ? "LOW" : (spinner.getSelectedItemPosition()==1 ? "MEDIUM" : "HIGH")));
            res.putExtra("task_date_fin", selectedDate.toString());
            res.putExtra("task_tags", finalTags);
            setResult(RESULT_OK, res);
            finish();
        });
    }
}