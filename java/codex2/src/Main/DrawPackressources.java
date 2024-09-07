package Main;

import java.util.ArrayList;
import card.Cards;
import card.ResourceCard;
import fr.umlv.zen5.ApplicationContext;

/**
 * La classe DrawPackressources représente un paquet de pioche pour les cartes de ressources dans le jeu.
 */
public class DrawPackressources {

    int select;
    Cards cardselect;
    ArrayList<ResourceCard> drawPackressources;

    /**
     * Construit un paquet de pioche vide pour les cartes de ressources.
     */
    public DrawPackressources() {
        drawPackressources = new ArrayList<ResourceCard>();
    }

    /**
     * Remplit le paquet de pioche avec des cartes de ressources.
     * 
     * @param ressource La liste de cartes de ressources pour remplir le paquet de pioche.
     */
    public void remplis(ArrayList<ResourceCard> ressource) {
        drawPackressources.addAll(ressource);
    }

    /**
     * Affiche le paquet de pioche de cartes de ressources à l'écran.
     * 
     * @param context Le contexte d'application pour le rendu graphique.
     */
    public void show(ApplicationContext context) {
        if (drawPackressources.size() > 3) {
            for (int i = 0; i < 2; i++) {
                var cardVers = drawPackressources.get(i);
                if (!cardVers.recto()) {
                    cardVers = cardVers.tourne();
                }
                drawPackressources.set(i, cardVers);
                drawPackressources.get(i).affiche(context, new Position(50, i * 80 + 50));
            }

            var cardVerso = drawPackressources.get(2);
            if (cardVerso.recto()) {
                cardVerso = cardVerso.tourne();
            }
            drawPackressources.set(2, cardVerso);
            drawPackressources.get(2).affiche(context, new Position(50, 2 * 80 + 50));
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
            if (p.x() > 50 && p.x() < 150 && p.y() > 50 + i * 80 && p.y() < 130 + i * 80) {
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
            drawPackressources.remove(index);
        }
    }

    /**
     * Ajoute la carte sélectionnée au deck du joueur et la supprime du paquet de pioche.
     * 
     * @param index L'index de la carte sélectionnée.
     * @param d     Le deck du joueur auquel ajouter la carte.
     */
    public void addtodeck(int index, Deck d) {
        cardselect = drawPackressources.get(index);
        this.remove(index);
        d.add(cardselect);
    }
}
