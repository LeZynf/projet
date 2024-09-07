package card;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import Main.Position;
import fr.umlv.zen5.ApplicationContext;

/**
 * Classe représentant une carte en or.
 */
public class GoldCard implements Cards {
    private final ArrayList<String> r; // Ressources de la carte
    private final String type; // Type de la carte
    private final ArrayList<String> cost; // Coût de la carte
    private final String scoring; // Score de la carte
    private Boolean recto; // Indique si la carte est recto (face visible)

    /**
     * Constructeur de la classe GoldCard.
     * 
     * @param r Liste des ressources de la carte.
     * @param type Type de la carte.
     * @param cost Liste des coûts de la carte.
     * @param scoring Score de la carte.
     * @param recto Indique si la carte est recto (face visible).
     */
    public GoldCard(ArrayList<String> r, String type, ArrayList<String> cost, String scoring, Boolean recto) {
        this.r = r;
        this.type = type;
        this.cost = cost;
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
            var contour = new Rectangle2D.Float(pos.x() - 2, pos.y() - 2, 104, 64);
            graphics.setColor(Color.ORANGE);
            graphics.fill(contour);
            var carte = new Rectangle2D.Float(pos.x(), pos.y(), 100, 60);
            
            // definition des 4 coins de la carte
            Position hautGauche = new Position(pos.x(), pos.y());
            Position hautDroit = new Position(pos.x() + 80, pos.y());
            Position basGauche = new Position(pos.x(), pos.y() + 40);
            Position basDroit = new Position(pos.x() + 80, pos.y() + 40);
            
            // Base de la carte avec le font de la couleur du type 
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
            
            // Definition de l'emplacemnt dans l'ordre de la liste
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
                    drawResource(graphics, posx, posy, r.get(i));
                    if (!scoring.equals("D:0")) {
                        // Dessine le contour noir du score
                        graphics.setColor(Color.BLACK);
                        graphics.drawString(scoring, pos.x() + 39, pos.y() + 11);

                        // Dessine le texte blanc par-dessus
                        graphics.setColor(Color.WHITE);
                        graphics.drawString(scoring, pos.x() + 40, pos.y() + 12);
                    }

                    Map<String, Integer> costMap = new HashMap<>();
                    for (String costItem : cost) {
                        costMap.put(costItem, costMap.getOrDefault(costItem, 0) + 1);
                    }

                    // Dessine les coûts en bas de la carte
                    int costPosX = pos.x() + 25;
                    int costPosY = pos.y() + 55;
                    for (Map.Entry<String, Integer> entry : costMap.entrySet()) {
                        String initial = entry.getKey().substring(0, 1).toUpperCase();
                        String displayText = initial + ":" + entry.getValue();

                        // Dessine le texte avec un contour noir
                        graphics.setColor(Color.BLACK);
                        graphics.drawString(displayText, costPosX - 1, costPosY - 1);
                        graphics.drawString(displayText, costPosX - 1, costPosY + 1);
                        graphics.drawString(displayText, costPosX + 1, costPosY - 1);
                        graphics.drawString(displayText, costPosX + 1, costPosY + 1);
                        graphics.drawString(displayText, costPosX, costPosY - 1);
                        graphics.drawString(displayText, costPosX, costPosY + 1);
                        graphics.drawString(displayText, costPosX - 1, costPosY);
                        graphics.drawString(displayText, costPosX + 1, costPosY);
                        graphics.setColor(Color.WHITE);
                        graphics.drawString(displayText, costPosX, costPosY);
                        costPosX += 25; // Ajuste l'espacement entre les coûts
                    }
                } else {
                    graphics.setColor(Color.ORANGE);
                    graphics.fillRect(posx - 2, posy - 2, 24, 24);
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
     * @param resource Type de la ressource.
     */
    private void drawResource(java.awt.Graphics2D graphics, int posx, int posy, String resource) {
        switch (resource) {
            case "Animal" -> {
                graphics.setColor(Color.ORANGE);
                graphics.fillRect(posx - 2, posy - 2, 24, 24);
                graphics.setColor(Color.WHITE);
                graphics.fillRect(posx, posy, 20, 20);
                graphics.setColor(Color.BLACK);
                graphics.drawString("A", posx + 4, posy + 16);
            }
            case "Fungi" -> {
                graphics.setColor(Color.ORANGE);
                graphics.fillRect(posx - 2, posy - 2, 24, 24);
                graphics.setColor(Color.WHITE);
                graphics.fillRect(posx, posy, 20, 20);
                graphics.setColor(Color.BLACK);
                graphics.drawString("F", posx + 4, posy + 16);
            }
            case "Plant" -> {
                graphics.setColor(Color.ORANGE);
                graphics.fillRect(posx - 2, posy - 2, 24, 24);
                graphics.setColor(Color.WHITE);
                graphics.fillRect(posx, posy, 20, 20);
                graphics.setColor(Color.BLACK);
                graphics.drawString("P", posx + 4, posy + 16);
            }
            case "Insect" -> {
                graphics.setColor(Color.ORANGE);
                graphics.fillRect(posx - 2, posy - 2, 24, 24);
                graphics.setColor(Color.WHITE);
                graphics.fillRect(posx, posy, 20, 20);
                graphics.setColor(Color.BLACK);
                graphics.drawString("I", posx + 4, posy + 16);
            }
            case "Quill" -> {
                graphics.setColor(Color.ORANGE);
                graphics.fillRect(posx - 2, posy - 2, 24, 24);
                graphics.setColor(Color.WHITE);
                graphics.fillRect(posx, posy, 20, 20);
                graphics.setColor(Color.BLACK);
                graphics.drawString("AQ", posx + 4, posy + 16);
            }
            case "Manuscript" -> {
                graphics.setColor(Color.ORANGE);
                graphics.fillRect(posx - 2, posy - 2, 24, 24);
                graphics.setColor(Color.WHITE);
                graphics.fillRect(posx, posy, 20, 20);
                graphics.setColor(Color.BLACK);
                graphics.drawString("AM", posx + 4, posy + 16);
            }
            case "Inkwell" -> {
                graphics.setColor(Color.ORANGE);
                graphics.fillRect(posx - 2, posy - 2, 24, 24);
                graphics.setColor(Color.WHITE);
                graphics.fillRect(posx, posy, 20, 20);
                graphics.setColor(Color.BLACK);
                graphics.drawString("AI", posx + 4, posy + 16);
            }
            case "Empty" -> {
                graphics.setColor(Color.ORANGE);
                graphics.fillRect(posx - 2, posy - 2, 24, 24);
                graphics.setColor(Color.WHITE);
                graphics.fillRect(posx, posy, 20, 20);
            }
        }
    }

    /**
     * Retourne une liste de chaînes de caractères représentant les coins de la carte.
     * 
     * @param recto Indique si la carte est du coté recto.
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
     * Vérifie si la carte est du coté recto.
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
     * @return True car la carte est Gold.
     */
    @Override
    public Boolean isgold() {
        return true;
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
     * Retourne la liste des coûts de la carte.
     * 
     * @return La liste des coûts de la carte.
     */
    public ArrayList<String> cost() {
        return this.cost;
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
     * Tourne la carte (change son orientation ou son état).
     * 
     * @return Une nouvelle  GoldCard avec l'état recto inversé.
     */
    public GoldCard tourne() {
        return new GoldCard(this.r, this.type, this.cost, this.scoring, !this.recto);
    }
}
