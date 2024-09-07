package Main;

import java.util.ArrayList;
import card.StarterCard;
import donnee.Mapping;
import donnee.GameInfo;
import fr.umlv.zen5.ApplicationContext;

public class Starteur {
    int select;
    StarterCard cardselect;
    static ArrayList<StarterCard> starterList;
    
    /**
     * Constructeur de la classe Starteur.
     * Initialise la liste des cartes de départ.
     */
    public Starteur() {
        starterList = new ArrayList<StarterCard>();
    }
    
    /**
     * Remplit la liste des cartes de départ avec celles fournies en paramètre.
     *
     * @param list La liste des cartes de départ à ajouter.
     */
    public void remplis(ArrayList<StarterCard> list) {    
        starterList.addAll(list);
        System.out.println(starterList);
    }
    
    /**
     * Vérifie si la position spécifiée correspond à une carte de départ .
     *
     * @param p       La position à vérifier.
     * @param context Le contexte de l'application.
     * @return true si la position correspond à une carte sélectionnable, sinon false.
     */
    public Boolean touch(Position p, ApplicationContext context){
        var screenInfo = context.getScreenInfo();
        var width = screenInfo.getWidth();
        var height = screenInfo.getHeight();
        int cardWidth = 100;
        int cardHeight = 60;
        var side = (int) width/2 -350;
        var top = (int) (height/2 -30);
        for (int i = 0; i < 6; i++) {
            int cardX = side + 120 * i;
            int cardY = top;
            if (p.x() >= cardX && p.x() <= (cardX + cardWidth) && 
                p.y() >= cardY && p.y() <= (cardY + cardHeight)) {
                select = i;
                return true;
            }
        } 
        return false;
    }
    
    /**
     * Affiche les cartes de départ à l'écran.
     *
     * @param context Le contexte de l'application.
     */
    public void choice(ApplicationContext context) {
        var screenInfo = context.getScreenInfo();
        var width = screenInfo.getWidth();
        var height = screenInfo.getHeight();
        
        for (int i = 0; i< starterList.size(); i++) {
            starterList.get(i).affiche(context, new Position((int) (width/2 -350+ i*120), (int) (height/2 -30)));
        }
    }
    
    /**
     * Ajoute la carte sélectionnée à la Map de jeu et met à jour les informations du jeu.
     *
     * @param index     L'indice de la carte sélectionnée dans la liste des cartes de départ.
     * @param map       L'objet Mapping représentant la carte de jeu.
     * @param gameinfo  L'objet GameInfo contenant les informations sur le jeu.
     */
    public void addtoMap(int index, Mapping map, GameInfo gameinfo) {
        cardselect = starterList.get(index);
        map.add(new Position(0,0), cardselect);
        gameinfo.start(cardselect.coin(cardselect.recto()));
    }
}
