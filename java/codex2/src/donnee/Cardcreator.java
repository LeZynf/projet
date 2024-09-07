package donnee;

import java.util.ArrayList;
import java.util.Collections;

import card.GoldCard;
import card.ResourceCard;
import card.StarterCard;

/**
 * Classe responsable de la création des cartes.
 */
public class Cardcreator {

    private final ArrayList<StarterCard> starterCards; // Liste des cartes de départ
    private final ArrayList<GoldCard> goldCards; // Liste des cartes Gold
    private final ArrayList<ResourceCard> resourceCards; // Liste des cartes de ressources

    /**
     * Constructeur de la classe CardCreator.
     */
    public Cardcreator() {
        starterCards = new ArrayList<>();
        goldCards = new ArrayList<>();
        resourceCards = new ArrayList<>();
    }

    /**
     * Méthode pour traiter une ligne de données et créer les cartes correspondantes.
     * 
     * @param line Ligne de données à traiter.
     */
    public void processLine(String line) {
        String[] parts = line.split(" ");
        if (parts.length > 0) {
            switch (parts[0]) {
                case "ResourceCard" -> processResourceCard(parts);
                case "GoldCard" -> processGoldCard(parts);
                case "StarterCard" -> processStarterCard(parts);
            }
        }
    }

    /**
     * Traite les données pour créer une carte de ressource.
     * 
     * @param parts Les parties de la ligne de données.
     */
    private void processResourceCard(String[] parts) {
        ArrayList<String> rectoList = new ArrayList<>();
        String type = "";
        String scoring = "D:0";

        // Parcours des parties pour extraire les informations nécessaires à la création de la carte de ressource
        for (int i = 1; i < parts.length; i++) {
            switch (parts[i]) {
                case "Recto" -> {
                    // Traitement de la liste recto
                    for (int j = 1; j <= 4 && (i + j) < parts.length; j++) {
                        String rectoItem = parts[i + j];
                        if (rectoItem.startsWith("R:") || rectoItem.startsWith("A:")) {
                            rectoItem = rectoItem.substring(2);
                        }
                        rectoList.add(rectoItem);
                    }
                    i += 4; // Skip the next 4 elements after Recto
                }
                case "Kingdom" -> {
                    if (i + 1 < parts.length) {
                        type = parts[++i];
                    }
                }
                case "D:" -> scoring = parts[i];
            }
        }
        resourceCards.add(new ResourceCard(rectoList, type, scoring, true));
    }

    /**
     * Traite les données pour créer une carte Gold.
     * 
     * @param parts Les parties de la ligne de données.
     */
    private void processGoldCard(String[] parts) {
        ArrayList<String> rectoList = new ArrayList<>();
        String type = "";
        ArrayList<String> costList = new ArrayList<>();
        String scoring = "D:0";

        // Parcours des parties pour extraire les informations nécessaires à la création de la carte Gold
        for (int i = 1; i < parts.length; i++) {
            switch (parts[i]) {
                case "Recto" -> {
                    // Traitement de la liste recto
                    for (int j = 1; j <= 4 && (i + j) < parts.length; j++) {
                        String rectoItem = parts[i + j];
                        if (rectoItem.startsWith("R:") || rectoItem.startsWith("A:")) {
                            rectoItem = rectoItem.substring(2);
                        }
                        rectoList.add(rectoItem);
                    }
                    i += 4; // Skip the next 4 elements after Recto
                }
                case "Kingdom" -> {
                    if (i + 1 < parts.length) {
                        type = parts[++i];
                    }
                }
                case "Cost" -> {
                    int j = i + 1;
                    while (j < parts.length && !parts[j].startsWith("Scoring")) {
                        costList.add(parts[j]);
                        j++;
                    }
                    i = j - 1; // Update i to skip the cost elements
                }
                case "Scoring" -> scoring = parts[++i];
            }
        }
        goldCards.add(new GoldCard(rectoList, type, costList, scoring, true));
    }

    /**
     * Traite les données pour créer une carte de départ.
     * 
     * @param parts Les parties de la ligne de données.
     */
    private void processStarterCard(String[] parts) {
        ArrayList<String> rectoList = new ArrayList<>();
        ArrayList<String> versoList = new ArrayList<>();
        ArrayList<String> resourcesList = new ArrayList<>();

        // Parcours des parties pour extraire les informations nécessaires à la création de la carte de départ
        for (int i = 1; i < parts.length; i++) {
            switch (parts[i]) {
                case "Recto" -> {
                    // Traitement de la liste recto
                    for (int j = 1; j <= 4 && (i + j) < parts.length; j++) {
                        String rectoItem = parts[i + j];
                        if (rectoItem.startsWith("R:") || rectoItem.startsWith("A:")) {
                            rectoItem = rectoItem.substring(2);
                        }
                        rectoList.add(rectoItem);
                    }
                }
                case "Resources" -> {
                    // Traitement de la liste de ressources
                    for (int j = 1; j <= 4 && (i + j) < parts.length; j++) {
                        String resourcesItem = parts[i + j];
                        if (resourcesItem.startsWith("R:") || resourcesItem.startsWith("A:")) {
                            resourcesItem = resourcesItem.substring(2);
                        }
                        resourcesList.add(resourcesItem);
                    }
                }
                case "Verso" -> {
                    // Traitement de la liste verso
                    for (int j = 1; j <= 4 && (i + j) < parts.length; j++) {
                        String versoItem = parts[i + j];
                        if (versoItem.startsWith("R:") || versoItem.startsWith("A:")) {
                            versoItem = versoItem.substring(2);
                        }
                        versoList.add(versoItem);
                    }
                }
            }
        }
        starterCards.add(new StarterCard(rectoList, versoList, resourcesList, "0", true));
    }

    /**
     * Obtient une liste de cartes Gold mélangées.
     * 
     * @return Liste de cartes Gold mélangées.
     */
    public ArrayList<GoldCard> Golden() {
        Collections.shuffle(goldCards);
        return goldCards;
    }

    /**
     * Obtient une liste de cartes de ressources mélangées.
     * 
     * @return Liste de cartes de ressources mélangées.
     */
    public ArrayList<ResourceCard> Resource() {
        Collections.shuffle(resourceCards);
        return resourceCards;
    }
    /**
     * Obtient une liste de cartes de starter mélangées.
     * 
     * @return Liste de cartes de starter mélangées.
     */
    public ArrayList<StarterCard> Starter() {
        Collections.shuffle(starterCards);
        return starterCards;
    }
    
}
