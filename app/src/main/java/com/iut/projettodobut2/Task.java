package com.iut.projettodobut2;

import java.util.Date;

public class Task {
    private String title;
    private String description;
    private Severity severity;
    private String dateDebut;
    private String dateFin;
    private String[] tags;

    public Task(String title, String description, Severity severity, String dateDebut, String dateFin, String[] tags) {
        if(title == null || description == null || severity == null || dateDebut == null || dateFin == null || tags == null){
            throw new RuntimeException();
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

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
}
