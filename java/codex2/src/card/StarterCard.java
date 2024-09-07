package card;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import Main.Position;
import fr.umlv.zen5.ApplicationContext;

/**
 * Classe représentant une carte de démarrage.
 */
public class StarterCard implements Cards {
    private final ArrayList<String> r; // Ressources de la face recto de la carte
    private final ArrayList<String> v; // Ressources de la face verso de la carte
    private ArrayList<String> c; // Ressources courantes à afficher
    private final ArrayList<String> ressource; // Ressources supplémentaires
    private final String scoring; // Score de la carte
    private Boolean recto; // Indique si la carte est coté recto 

    private static int select = -1; // Variable statique pour sélectionner une carte

    /**
     * Constructeur de la classe StarterCard.
     * 
     * @param r Liste des ressources de la face recto.
     * @param v Liste des ressources de la face verso.
     * @param ressource Liste des ressources supplémentaires.
     * @param scoring Score de la carte.
     * @param recto Indique si la carte est coté recto .
     */
    public StarterCard(ArrayList<String> r, ArrayList<String> v, ArrayList<String> ressource, String scoring, Boolean recto) {
        this.r = r;
        this.v = v;
        this.ressource = ressource;
        this.scoring = scoring;
        this.recto = recto;
    }

    /**
     * Affiche la carte dans le contexte de l'application à une position donnée.
     * 
     * @param context Contexte de l'application.
     * @param pos Position où la carte doit être affichée.
     */
    public void affiche(ApplicationContext context, Position pos) {
        context.renderFrame(graphics -> {
            var carte = new Rectangle2D.Float(pos.x(), pos.y(), 100, 60);
            Position hautGauche = new Position(pos.x(), pos.y());
            Position hautDroit = new Position(pos.x() + 80, pos.y());
            Position basGauche = new Position(pos.x(), pos.y() + 40);
            Position basDroit = new Position(pos.x() + 80, pos.y() + 40);

            graphics.setColor(Color.GRAY);
            graphics.fill(carte);
            graphics.setColor(Color.BLACK);

            // Affiche les ressources selon le côté de la carte (recto ou verso)
            if (!recto) {
                c = v;
                var sb = new StringBuilder();
                for (var ress : ressource) {
                    sb.append(ress.charAt(0)).append(" ");
                }
                graphics.drawString(sb.toString(), pos.x() + 40, pos.y() + 40);
            } else {
                c = r;
                graphics.drawString("S", pos.x() + 45, pos.y() + 40);
            }

            graphics.setColor(Color.WHITE);
            int posx = 0;
            int posy = 0;
            for (int i = 0; i < 4; i++) {
                switch (i) {
                    case 0 -> {
                        posx = hautGauche.x();
                        posy = hautGauche.y();
                    }
                    case 1 -> {
                        posx = hautDroit.x();
                        posy = hautDroit.y();
                    }
                    case 2 -> {
                        posx = basDroit.x();
                        posy = basDroit.y();
                    }
                    case 3 -> {
                        posx = basGauche.x();
                        posy = basGauche.y();
                    }
                }
                drawResource(graphics, posx, posy, c.get(i));
            }
        });
    }

    /**
     * Dessine une ressource sur la carte.
     * 
     * @param graphics Contexte graphique.
     * @param posx Position x de la ressource.
     * @param posy Position y de la ressource.
     * @param resource Ressource à dessiner.
     */
    private void drawResource(java.awt.Graphics2D graphics, int posx, int posy, String resource) {
        graphics.setColor(Color.WHITE);
        graphics.fillRect(posx, posy, 20, 20);
        graphics.setColor(Color.BLACK);
        switch (resource) {
            case "Animal" -> graphics.drawString("A", posx + 4, posy + 16);
            case "Fungi" -> graphics.drawString("F", posx + 4, posy + 16);
            case "Plant" -> graphics.drawString("P", posx + 4, posy + 16);
            case "Insect" -> graphics.drawString("I", posx + 4, posy + 16);
            case "Empty" -> {} // No need to draw anything for "Empty"
        }
    }

    /**
     * Retourne une liste de chaînes de caractères représentant les coins de la carte.
     * 
     * @param recto Indique si la carte est recto (face visible).
     * @return Une liste de chaînes de caractères représentant les coins de la carte.
     */
    @Override
    public ArrayList<String> coin(Boolean recto) {
        return recto ? this.r : this.v;
    }

    /**
     * Vérifie si la carte est recto.
     * 
     * @return True si la carte est recto, false sinon.
     */
    @Override
    public Boolean recto() {
        return this.recto;
    }

    /**
     * Vérifie si la carte est Gold.
     * 
     * @return False car ce n'est pas une carte Gold.
     */
    @Override
    public Boolean isgold() {
        return false;
    }

    /**
     * Retourne la valeur de la variable statique select.
     * 
     * @return La valeur de select.
     */
    public static int select() {
        return select;
    }

    /**
     * Retourne le score de la carte sous forme de chaîne de caractères.
     * 
     * @return Le score de la carte.
     */
    @Override
    public String scoring() {
        return this.scoring;
    }

    /**
     * Retourne le type de la carte sous forme de chaîne de caractères.
     * 
     * @return Le type de la carte.
     */
    @Override
    public String type() {
        return "StarterCard";
    }

    /**
     * Retourne la liste des ressources supplémentaires de la carte.
     * 
     * @return Une liste des ressources supplémentaires.
     */
    public ArrayList<String> ressource() {
        return this.ressource;
    }

    /**
     * Tourne la carte (change son orientation ou son état).
     * 
     * @return Une nouvelle StarterCard avec l'état recto inversé.
     */
    public StarterCard tourne() {
        return new StarterCard(this.r, this.v, this.ressource, this.scoring, !this.recto);
    }
}
