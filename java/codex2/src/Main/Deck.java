package Main;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import card.Cards;
import card.GoldCard;
import card.ResourceCard;
import donnee.Mapping;
import donnee.GameInfo;
import fr.umlv.zen5.ApplicationContext;

/**
 * The Deck class represents a player's deck of cards.
 */
public class Deck {
    
    /** The index of the selected card in the deck. */
    int select = -1;
    
    /** The horizontal side offset for rendering the deck. */
    int side = 300;
    
    /** The list of cards in the player's deck. */
    ArrayList<Cards> deckPlayer;
    
    /**
     * Constructs a new Deck object.
     */
    public Deck() {
        deckPlayer = new ArrayList<Cards>();
    }
    
    /**
     * Fills the deck with cards from the given lists of gold cards and resource cards.
     * @param dg The list of gold cards.
     * @param dr The list of resource cards.
     */
    public void remplis(ArrayList<GoldCard> dg, ArrayList<ResourceCard> dr) {
        ArrayList<Cards> hand = new ArrayList<Cards>();
        hand.add(dr.getLast());
        dr.removeLast();
        hand.add(dr.getLast());
        dr.removeLast();
        hand.add(dg.getLast());
        dg.removeLast();
        deckPlayer.addAll(hand);
    }
    
    /**
     * Adds a card to the deck.
     * @param c The card to add.
     */
    public void add(Cards c) {
        deckPlayer.add(c);
    }
    
    /**
     * Returns the size of the deck.
     * @return The size of the deck.
     */
    public int size() {
        return deckPlayer.size();
    }
    
    /**
     * Removes a card from the deck at the specified index.
     * @param index The index of the card to remove.
     */
    public void remove(int index) {
        if (index < deckPlayer.size() - 1 && index >= 0) {
            deckPlayer.remove(index);
        }
    }
    
    /**
     * Renders the deck of cards on the screen.
     * @param context The application context for rendering.
     */
    public void show(ApplicationContext context) {
        var screenInfo = context.getScreenInfo();
        var height = screenInfo.getHeight();
        int i = 0;
        for (Cards card : deckPlayer) {
            card.affiche(context, new Position((i + 1) * 120 + side, (int) height - 130));
            i++;
        }
        context.renderFrame(graphics -> {
            var contour = new Rectangle2D.Float(4 * 120 + side, (int) height - 130, 100, 60);
            graphics.setColor(Color.white);
            graphics.fill(contour);
            graphics.setColor(Color.BLACK);
            graphics.drawString("Retourne ", 4 * 120 + side + 10, (int) height - 130 + 20);
            graphics.drawString("La Carte ", 4 * 120 + side + 10, (int) height - 130 + 40);
        });
    }
    
    /**
     * Handles the touch input on the deck of cards.
     * @param p The position of the touch input.
     * @param context The application context.
     * @return True if a card in the deck is touched, false otherwise.
     */
    public Boolean touch(Position p, ApplicationContext context) {
        var screenInfo = context.getScreenInfo();
        var height = screenInfo.getHeight();
        int cardWidth = 100;
        int cardHeight = 60;
        for (int i = 0; i < deckPlayer.size(); i++) {
            int cardX = side + 120 * (i + 1);
            int cardY = (int) height - 130;
            if (p.x() >= cardX && p.x() <= (cardX + cardWidth) && p.y() >= cardY && p.y() <= (cardY + cardHeight)) {
                select = i;
                return true;
            }
        }
        // Verify if the card is turned over
        if (p.x() >= (side + 120 * 4) && p.x() <= ((side + 120 * 4) + cardWidth) && p.y() >= ((int) height - 130)
                && p.y() <= (((int) height - 130) + cardHeight)) {
            if (select != -1 && select <= deckPlayer.size() - 1) {
                var card = deckPlayer.get(select).tourne();
                deckPlayer.set(select, card);
            }
        }
        return false;
    }
    
    /**
     * Highlights the selected card in the deck.
     * @param context The application context.
     */
    public void showSelect(ApplicationContext context) {
        if (select >= 0) {
            var screenInfo = context.getScreenInfo();
            var height = screenInfo.getHeight();
            int cardX = side + 120 * (select + 1);
            int cardY = (int) height - 130;
            int outline = 10;
            context.renderFrame(graphics -> {
                graphics.setColor(Color.ORANGE);
                graphics.drawRect(cardX - outline / 2, cardY - outline / 2, 100 + outline, 60 + outline);
            });
        }
    }
    
    /**
     * Gets the coins of the selected card in the deck.
     * @return The list of coins of the selected card.
     */
    public List<String> getCoinsOfSelectedCard() {
        if (select >= 0 && select < deckPlayer.size()) {
            Cards selectedCard = deckPlayer.get(select);
            return selectedCard.coin(selectedCard.recto());
        }
        return List.of();
    }
    
    /**
     * Adds the selected card to the map.
     * @param indexselect The index of the selected card.
     * @param map The mapping of the game.
     * @param gameinfo The game information.
     * @return True if the card is successfully added to the map, false otherwise.
     */
    public boolean addtoMap(int indexselect, Mapping map, GameInfo gameinfo) {
        if (indexselect >= 0 && indexselect < deckPlayer.size()) {
            Cards cardselect = deckPlayer.get(indexselect);
            if (cardselect instanceof GoldCard) {
                GoldCard goldCard = (GoldCard) cardselect;
                if (gameinfo.possible(goldCard.cost()) || !goldCard.recto()) {
                    deckPlayer.remove(indexselect);
                    Position poss = map.select();
                    map.add(poss, goldCard);
                    gameinfo.update(goldCard, map.cacher(poss));
                    int nbcorner = map.cacher(poss).size();
                    if (goldCard.recto()) {
                        gameinfo.countPoint(goldCard.scoring(), nbcorner);
                    }
                    return true; // Ajout avec succès pour les cartes Gold
                } else {
                    return false; // Impossible d'ajouter la carte Gold sans ressources suffisantes
                }
            } else {
                // Pour les autres types de cartes, ajout direct sans vérification
                deckPlayer.remove(indexselect);
                Position poss = map.select();

		            map.add(poss, cardselect);
		            gameinfo.update(cardselect, map.cacher(poss));
		            if (cardselect.recto()) {
		            gameinfo.countPoint( cardselect.scoring(), 0);}
		            return true;
		        }
		    }
		    return false; // Index invalide
		}


	 /**
	  * Renvoi la derniere carte du deck selectionner
	  * @return select index de La carte selectionner
	  */
	public int select() {
		return select;
	}
	/**
	 * Remet la selection a -1 (aucune carte selectionnée)
	 */
	public void resetselect() {
		select = -1;
	}
	
	
	
	
}
