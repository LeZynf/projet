package donnee;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Objects;

import Main.Deck;
import Main.DrawPackgold;
import Main.DrawPackressources;
import card.Cards;
import fr.umlv.zen5.ApplicationContext;
import fr.umlv.zen5.Event.Action;

/**
 * Classe pour gérer les informations du jeu.
 */
public class GameInfo {
    private int resultatr = -1;
    private int resultatdg = -1;
    private int resultatd = -1;

    private int nbArteEnkIll;
    private int nbArtePaper;
    private int nbArteFeather;
    private int nbAnimals;
    private int nbMushrooms;
    private int nbBugs;
    private int nbPlants;
    private int point;
    private int nbTour;

    /**
     * Constructeur de la classe GameInfo.
     */
    public GameInfo() {
    	// Initialisation des données.
        nbArteEnkIll = 0;
        nbArtePaper = 0;
        nbArteFeather = 0;
        point = 0;
        nbAnimals = 0;
        nbMushrooms = 0;
        nbBugs = 0;
        nbPlants = 0;
        nbTour = 0;
    }

    /**
     * Méthode pour gérer le déroulement du jeu.
     * @param dr Le paquet de ressources.
     * @param dg Le paquet de GoldenCards.
     * @param p La position du clic.
     * @param d Le deck du joueur.
     * @param map La carte du jeu.
     * @param context Le contexte de l'application.
     * @param gameinfo Les informations sur le jeu.
     */
    public void Game(DrawPackressources dr, DrawPackgold dg, Main.Position p, Deck d, Mapping map, ApplicationContext context, GameInfo gameinfo) {
        if (d.size() < 3) {
            if (dr.touch(p)) {
                resultatr = dr.select();
                System.out.println(resultatr);
                dr.addtodeck(resultatr, d);
                return;
            }
            if (dg.touch(p)) {
                resultatdg = dg.select();
                dg.addtodeck(resultatdg, d);
                return;
            }
        } else if (d.touch(p, context)) {
            resultatd = d.select();
            System.out.println(resultatd);
            map.showPossibility(context);
        } else if (resultatd != -1) {
            if (map.touch(context, p)) {
                if (d.addtoMap(resultatd, map, gameinfo)) {
                    resultatd = -1;
                    d.resetselect();
                }
            }
        }
    }

    /**
     * Méthode pour afficher l'écran de victoire.
     * @param context Le contexte de l'application.
     */
    public void win(ApplicationContext context) {
        if (point >= 20) {

            var screenInfo = context.getScreenInfo();
            var width = screenInfo.getWidth();
            var height = screenInfo.getHeight();

            context.renderFrame(graphics -> {
                var fond = new Rectangle2D.Float(0, 0, width, height);
                graphics.setColor(Color.BLACK);
                graphics.fill(fond);
                Font font1 = new Font("SansSerif", Font.PLAIN, 50);
                graphics.setFont(font1);
                graphics.setColor(Color.white);
                graphics.drawString("Bravo vous avez Gagnez en " + nbTour + " tours GG!", 50, 320);
            });
            while (true) {
                var event = context.pollOrWaitEvent(10);
                if (event == null) {
                    continue;
                }

                var action = event.getAction();

                if (action == Action.KEY_PRESSED) {
                    context.exit(1);
                }
            }

        }

    }

    /**
     * Méthode pour afficher les informations sur le jeu.
     * @param context Le contexte de l'application.
     */
    public void show(ApplicationContext context) {
        var screenInfo = context.getScreenInfo();
        var width = screenInfo.getWidth();
        float side = width - 300;
        float top = 50;

        context.renderFrame(graphics -> {

            var font = new Font("SansSerif", Font.PLAIN, 26);
            graphics.setFont(font);
            graphics.setColor(Color.WHITE);
            graphics.drawString("Art Inkwell = " + nbArteEnkIll, side, top + 40);
            graphics.drawString("Art Manuscrit = " + nbArtePaper, side, top + 80);
            graphics.drawString("Art Quill = " + nbArteFeather, side, top + 120);
            graphics.drawString("Animals = " + nbAnimals, side, top + 160);
            graphics.drawString("Plants = " + nbPlants, side, top + 200);
            graphics.drawString("Insect = " + nbBugs, side, top + 240);
            graphics.drawString("Fungi = " + nbMushrooms, side, top + 280);
            graphics.drawString("Points = " + point, side, top + 320);

            graphics.drawString("Nombre de Tours = " + nbTour, side, top + 400);

        });
    }

    /**
     * Méthode pour initialiser les valeurs des ressources au début du jeu.
     * @param coin La liste des ressources.
     */
    public void start(ArrayList<String> coin) {
        Objects.requireNonNull(coin);

        for (var corner : coin) {
            switch (corner) {
                case "Inkwell" -> nbArteEnkIll++;
                case "Manuscript" -> nbArtePaper++;
                case "Quill" -> nbArteFeather++;
                case "Animal" -> nbAnimals++;
                case "Fungi" -> nbMushrooms++;
                case "Insect" -> nbBugs++;
                case "Plant" -> nbPlants++;
            }
        }
    }

    /**
     * Méthode pour mettre à jour les informations du jeu après l'ajout d'une carte.
     * @param card La carte ajoutée.
     * @param cover Les coins recouverts par la carte ajoutée.
     */
    public void update(Cards card, ArrayList<String> cover) {
        ArrayList<String> coin = card.coin(card.recto());
        var recto = card.recto();
        var type = card.type();
        if (!recto) {
           
          
                switch (type) {
                    case "Inkwell" -> nbArteEnkIll++;
                    case "Manuscript" -> nbArtePaper++;
                    case "Quill" -> nbArteFeather++;
                    case "Animal" -> nbAnimals++;
                    case "Fungi" -> nbMushrooms++;
                    case "Insect" -> nbBugs++;
                    case "Plant" -> nbPlants++;
                
            }
            } else {
                for (var corner : coin) {
                    switch (corner) {
                        case "Inkwell" -> nbArteEnkIll++;
                        case "Manuscript" -> nbArtePaper++;
                        case "Quill" -> nbArteFeather++;
                        case "Animal" -> nbAnimals++;
                        case "Fungi" -> nbMushrooms++;
                        case "Insect" -> nbBugs++;
                        case "Plant" -> nbPlants++;
                    }
                }
            }

            for (var corner : cover) {
                switch (corner) {
                    case "Inkwell" -> nbArteEnkIll--;
                    case "Manuscript" -> nbArtePaper--;
                    case "Quill" -> nbArteFeather--;
                    case "Animal" -> nbAnimals--;
                    case "Fungi" -> nbMushrooms--;
                    case "Insect" -> nbBugs--;
                    case "Plant" -> nbPlants--;
                }
            }
            nbTour++;
        }

        /**
         * Méthode pour calculer les points obtenus en fonction du scoring d'une carte.
         * @param scoring Le scoring de la carte.
         * @param cover Le nombre de coins recouverts par la carte.
         */
        public void countPoint(String scoring, int cover) {
            if (scoring.length() >= 3) {
                int number = Integer.parseInt(scoring.substring(2));
                char type = scoring.charAt(0);
                int addpoint;

                switch (type) {
                    case 'D':
                        // Scoring de type D: pour les points directs
                        point += number;
                        break;

                    case 'M':
                        // Scoring de type M: Manuscrit
                        addpoint = nbArtePaper * number;
                        point += addpoint;
                        break;

                    case 'Q':
                        // Scoring de type Q: Quill
                        addpoint = nbArteFeather * number;
                        point += addpoint;
                        break;

                    case 'I':
                        // Scoring de type I: Inkwell
                        addpoint = nbArteEnkIll * number;
                        point += addpoint;
                        break;

                    case 'C':
                        // Scoring de type C: Coin recouvert
                        addpoint = cover * number;
                        point += addpoint;
                        break;

                    default:
                        // Cas par défaut si le type n'est pas reconnu
                
                        break;
                }
            }
        }

        /**
         * Méthode pour compter le nombre d'occurrences d'un élément dans une liste.
         * @param list La liste dans laquelle chercher.
         * @param target L'élément à rechercher.
         * @return Le nombre d'occurrences de l'élément dans la liste.
         */
        public static int countOccurrences(ArrayList<String> list, String target) {
            return (int) list.stream().filter(item -> item.equals(target)).count();
        }

        /**
         * Méthode pour vérifier si une carte peut être posée en fonction des ressources disponibles.
         * @param cost Les ressources nécessaires pour poser la carte.
         * @return true si la carte peut être posée, sinon false.
         */
        public Boolean possible(ArrayList<String> cost) {
            long plant = countOccurrences(cost, "Plant");
            long fungi = countOccurrences(cost, "Fungi");
            long animal = countOccurrences(cost, "Animal");
            long insect = countOccurrences(cost, "Insect");

            // Vérification si les ressources sont suffisantes
            boolean possible = plant <= nbPlants && fungi <= nbMushrooms && animal <= nbAnimals && insect <= nbBugs;
            System.out.println("Carte posable : " + possible);

            return possible;
        }
    }
