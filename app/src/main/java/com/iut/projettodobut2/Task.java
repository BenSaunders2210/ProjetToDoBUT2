package com.iut.projettodobut2;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;

/**
 * Création d'une tâche incluant son titre, description, severité, date debut et fin et les tags.
 *
 * @author Saunders Benjamin, Maillard Jeremie
 */
public class Task {
    private String title;
    private String description;
    private Severity severity;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String[] tags;

    public Task(String title, String description, Severity severity, LocalDate dateDebut, LocalDate dateFin, String[] tags) {
        if(title == null || description == null || severity == null || dateDebut == null || dateFin == null || tags == null){
            throw new NullPointerException("EXCEPTION : Task.java - Les paramètres du constructeur ne devraient pas être null.");
        }
        if(dateDebut.isAfter(dateFin)){
            throw new DateTimeException("EXCEPTION : Task.java - Les dates du constructeur ne sont pas valide (date debut après date fin)");
        }

        this.title = title;
        this.description = description;
        this.severity = severity;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.tags = tags;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
}
