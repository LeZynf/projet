package Main;

import java.util.ArrayList;
import card.Cards;
import card.GoldCard;
import fr.umlv.zen5.ApplicationContext;

/**
 * La classe DrawPackgold représente un paquet de pioche pour les cartes en or dans le jeu.
 */
public class DrawPackgold {
    int select;
    Cards cardselect;
    ArrayList<GoldCard> drawPackgold;

    /**
     * Construit un paquet de pioche vide pour les cartes en or.
     */
    public DrawPackgold() {
        drawPackgold = new ArrayList<GoldCard>();
    }

    /**
     * Remplit le paquet de pioche avec des cartes en or.
     * 
     * @param goldencards La liste de cartes en or pour remplir le paquet de pioche.
     */
    public void remplis(ArrayList<GoldCard> goldencards) {
        drawPackgold.addAll(goldencards);
    }

    /**
     * Affiche le paquet de pioche de cartes en or à l'écran.
     * 
     * @param context Le contexte d'application pour le rendu graphique.
     */
    public void show(ApplicationContext context) {
        if (drawPackgold.size() > 3) {
            for (int i = 0; i < 2; i++) {
                var cardVers = drawPackgold.get(i);
                if (!cardVers.recto()) {
                    cardVers = cardVers.tourne();
                }
                drawPackgold.set(i, cardVers);
                drawPackgold.get(i).affiche(context, new Position(50, i * 80 + 300));
            }

            var cardVerso = drawPackgold.get(2);
            if (cardVerso.recto()) {
                cardVerso = cardVerso.tourne();
            }
            drawPackgold.set(2, cardVerso);
            drawPackgold.get(2).affiche(context, new Position(50, 2 * 80 + 300));
        }
    }

    /**
     * Vérifie si l'utilisateur a cliqué sur une carte dans le paquet de pioche.
     * 
     * @param p La position du clic de la souris.
     * @return true si une carte est sélectionnée, false sinon.
     */
    public Boolean touch(Position p) {
        for (int i = 0; i < 3; i++) {
            if (p.x() > 50 && p.x() < 150 && p.y() > 300 + i * 80 && p.y() < 380 + i * 80) {
                select = i;
                return true;
            }
        }
        return false;
    }

    /**
     * Récupère l'index de la carte sélectionnée.
     * 
     * @return L'index de la carte sélectionnée.
     */
    public int select() {
        return select;
    }

    /**
     * Supprime une carte du paquet de pioche à l'index spécifié.
     * 
     * @param index L'index de la carte à supprimer.
     */
    public void remove(int index) {
        if (index < 3 && index >= 0) {
            drawPackgold.remove(index);
        }
    }

    /**
     * Ajoute la carte sélectionnée au deck du joueur et la supprime du paquet de pioche.
     * 
     * @param index L'index de la carte sélectionnée.
     * @param d     Le deck du joueur auquel ajouter la carte.
     */
    public void addtodeck(int index, Deck d) {
        cardselect = drawPackgold.get(index);
        this.remove(index);
        d.add(cardselect);
    }
}
