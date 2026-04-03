package com.iut.projettodobut2;

import java.time.DateTimeException;
import java.time.LocalDate;

/**
 * Création d'une tâche incluant son titre, description, severité, date debut et fin et les tags.
 *
 * @author Saunders Benjamin, Maillard Jeremie
 */
public class Task {
    /**
     * Le titre de la tâche. S'affiche sur la page d'accueil.
     */
    private String titre;
    /**
     * La description de la tâche. Visible seulement après appui sur la tâche dans l'accueil.
     */
    private String description;
    /**
     * La sévérité de la tâche. Uniquement 3 possibilités : LOW, MEDIUM, HIGH ( = FAIBLE, MOYEN, URGENT)
     */
    private Severity severite;
    /**
     * Date de début de la tâche. La date est toujours la date de création de la tâche.
     */
    private LocalDate dateDebut;
    /**
     * Date de fin de la tâche. Indique la date de fin.
     */
    private LocalDate dateFin;
    /**
     * Les tags affectué a la tâche. Utilisé pour les filtres et sont récupéré dans le TextView de style CSV (ex: travail,ecole,amis,courses..etc)
     */
    private String[] tags;

    /**
     * l'Unique constructeur de la classe. Utilisé pour la gestion des tâches de l'application.
     * @param titre Le titre de la tâche. S'affiche sur la page d'accueil.
     * @param description La description de la tâche. Visible seulement après appui sur la tâche dans l'accueil.
     * @param severite La sévérité de la tâche. Uniquement 3 possibilités : LOW, MEDIUM, HIGH ( = FAIBLE, MOYEN, URGENT)
     * @param dateDebut Date de début de la tâche. La date est toujours la date de création de la tâche.
     * @param dateFin Date de fin de la tâche. Indique la date de fin.
     * @param tags Les tags affectué a la tâche. Utilisé pour les filtres et sont récupéré dans le TextView de style CSV (ex: travail,ecole,amis,courses..etc)
     */
    public Task(String titre, String description, Severity severite, LocalDate dateDebut, LocalDate dateFin, String[] tags) {
        if(titre == null || description == null || severite == null || dateDebut == null || dateFin == null || tags == null){
            throw new NullPointerException("EXCEPTION : Task.java - Les paramètres du constructeur ne devraient pas être null.");
        }
        if(dateDebut.isAfter(dateFin)){
            throw new DateTimeException("EXCEPTION : Task.java - Les dates du constructeur ne sont pas valide (date debut après date fin)");
        }

        this.titre = titre;
        this.description = description;
        this.severite = severite;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.tags = tags;
    }

    /**
     * Getter du titre.
     * @return le titre.
     */
    public String getTitre() {
        return titre;
    }

    /**
     * Setter du titre.
     * @param titre le nouveau titre.
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
     * Getter de la description.
     * @return la description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter de la description
     * @param description la nouvelle description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter de la sévérité.
     * @return la sévérité de la tâche.
     */
    public Severity getSeverite() {
        return severite;
    }

    /**
     * Setter de la sévérité.
     * @param severite la nouvelle sévérité de la tâche.
     */
    public void setSeverite(Severity severite) {
        this.severite = severite;
    }

    /**
     * Getter de la date de début.
     * @return la date de début.
     */
    public LocalDate getDateDebut() {
        return dateDebut;
    }

    /**
     * Setter de la date de début.
     * @param dateDebut la nouvelle date de début.
     */
    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    /**
     * Getter de la date de fin.
     * @return la date de fin.
     */
    public LocalDate getDateFin() {
        return dateFin;
    }

    /**
     * Setter de la date de fin.
     * @param dateFin la nouvelle date de fin.
     */
    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    /**
     * Getter des tags de la tâche.
     * @return les tags.
     */
    public String[] getTags() {
        return tags;
    }

    /**
     * Setter des tags de la tâche.
     * @param tags les nouvelles tags.
     */
    public void setTags(String[] tags) {
        this.tags = tags;
    }
}
