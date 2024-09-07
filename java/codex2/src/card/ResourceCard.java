package card;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;

import Main.Position;
import fr.umlv.zen5.ApplicationContext;

/**
 * Classe représentant une carte de ressource.
 */
public class ResourceCard implements Cards {
    private final ArrayList<String> r; // Ressources de la carte
    private final String type; // Type de la carte
    private final String scoring; // Score de la carte
    private Boolean recto; // Indique si la carte est recto

    /**
     * Constructeur de la classe ResourceCard.
     * 
     * @param r Liste des ressources de la carte.
     * @param type Type de la carte.
     * @param scoring Score de la carte.
     * @param recto Indique si la carte est recto.
     */
    public ResourceCard(ArrayList<String> r, String type, String scoring, Boolean recto) {
        this.r = r;
        this.type = type;
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

            // Détermine la couleur et la lettre de la carte en fonction du type
            switch (type) {
                case "Insect" -> {
                    graphics.setColor(Color.MAGENTA);
                    graphics.fill(carte);
                    graphics.setColor(Color.WHITE);
                    graphics.drawString("I", pos.x() + 50, pos.y() + 30);
                }
                case "Fungi" -> {
                    graphics.setColor(Color.RED);
                    graphics.fill(carte);
                    graphics.setColor(Color.WHITE);
                    graphics.drawString("F", pos.x() + 50, pos.y() + 30);
                }
                case "Plant" -> {
                    graphics.setColor(Color.GREEN);
                    graphics.fill(carte);
                    graphics.setColor(Color.WHITE);
                    graphics.drawString("P", pos.x() + 50, pos.y() + 30);
                }
                case "Animal" -> {
                    graphics.setColor(Color.BLUE);
                    graphics.fill(carte);
                    graphics.setColor(Color.WHITE);
                    graphics.drawString("A", pos.x() + 50, pos.y() + 30);
                }
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

                if (recto) {
                    // Dessine les ressources sur la carte
                    switch (r.get(i)) {
                        case "Animal" -> drawResource(graphics, posx, posy, "A");
                        case "Fungi" -> drawResource(graphics, posx, posy, "F");
                        case "Plant" -> drawResource(graphics, posx, posy, "P");
                        case "Insect" -> drawResource(graphics, posx, posy, "I");
                        case "Quill" -> drawResource(graphics, posx, posy, "AQ");
                        case "Manuscript" -> drawResource(graphics, posx, posy, "AM");
                        case "Inkwell" -> drawResource(graphics, posx, posy, "AI");
                        case "Empty" -> {
                            graphics.setColor(Color.WHITE);
                            graphics.fillRect(posx, posy, 20, 20);
                        }
                    }
                    if (!scoring.equals("D:0")) {
                        // Dessine le contour noir du score
                        graphics.setColor(Color.BLACK);
                        graphics.drawString(scoring, pos.x() + 39, pos.y() + 11);

                        // Dessine le texte blanc par-dessus
                        graphics.setColor(Color.WHITE);
                        graphics.drawString(scoring, pos.x() + 40, pos.y() + 12);
                    }
                } else {
                    graphics.setColor(Color.WHITE);
                    graphics.fillRect(posx, posy, 20, 20);
                }
            }
        });
    }

    /**
     * Dessine une ressource sur la carte.
     * 
     * @param graphics Contexte graphique.
     * @param posx Position x de la ressource.
     * @param posy Position y de la ressource.
     * @param text Texte à dessiner représentant la ressource.
     */
    private void drawResource(java.awt.Graphics2D graphics, int posx, int posy, String text) {
        graphics.setColor(Color.WHITE);
        graphics.fillRect(posx, posy, 20, 20);
        graphics.setColor(Color.BLACK);
        graphics.drawString(text, posx + 4, posy + 16);
    }

    /**
     * Retourne une liste de chaînes de caractères représentant les coins de la carte.
     * 
     * @param recto Indique si la carte est recto (face visible).
     * @return Une liste de chaînes de caractères représentant les coins de la carte.
     */
    @Override
    public ArrayList<String> coin(Boolean recto) {
        if (recto) {
            return this.r;
        }
        ArrayList<String> list = new ArrayList<>(Collections.nCopies(4, "Empty"));
        return list;
    }

    /**
     * Vérifie si la carte est coté recto .
     * 
     * @return True si la carte est recto, false sinon.
     */
    @Override
    public Boolean recto() {
        return this.recto;
    }

    /**
     * Vérifie si la carte est une carte gold.
     * 
     * @return False car la carte n'est pas Gold.
     */
    @Override
    public Boolean isgold() {
        return false;
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
        return this.type;
    }

    /**
     * Tourne la carte (change son coté).
     * 
     * @return Une nouvelle ResourceCard avec l'état recto inversé.
     */
    public ResourceCard tourne() {
        return new ResourceCard(this.r, this.type, this.scoring, !this.recto);
    }
}
