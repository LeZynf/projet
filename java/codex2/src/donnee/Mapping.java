package donnee;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import Main.Position;
import card.Cards;
import card.StarterCard;
import fr.umlv.zen5.ApplicationContext;

/**
 * Classe responsable de la gestion de la carte et des interactions avec l'interface utilisateur.
 */
public class Mapping {
    // Déclaration des variables statiques pour la taille des cartes
    static int widthCard = 100;
    static int lengthCard = 60;

    // Déclaration des structures de données pour stocker les cartes et les positions possibles
    static HashMap<Position, Cards> map;
    static ArrayList<Position> posibility;

    // Variables pour le déplacement des cartes
    static int depx = 0;
    static int depy = 0;

    // Position sélectionnée
    Position select;

    // Constructeur
    public Mapping() {
        map = new HashMap<Position, Cards>();

        // Test : ajout d'une carte au position (0,0)
        ArrayList<String> coin = new ArrayList<>();
        ArrayList<String> ressource = new ArrayList<>();
        coin.addAll(List.of("Insect", "Fungi", "Plant", "Animal"));
        ressource.addAll(List.of("Fungi", "Plant", "Animal"));
        map.put(new Position(0, 0), new StarterCard(coin, coin, ressource, "0", true));
    }

    /**
     * Méthode pour déplacer les cartes.
     *
     * @param x La quantité à déplacer en abscisse.
     * @param y La quantité à déplacer en ordonnée.
     */
    public void move(int x, int y) {
        depx += x;
        depy += y;
    }

    /**
     * Convertit les coordonnées pour l'affichage.
     *
     * @param context Le contexte de l'application.
     * @param p       La position à convertir.
     * @return La position convertie.
     */
    public static Position ConvertCoordinates(ApplicationContext context, Position p) {
        Objects.requireNonNull(context);
        Objects.requireNonNull(p);

        // Récupération des dimensions de l'écran
        var screenInfo = context.getScreenInfo();
        var width = screenInfo.getWidth();
        var height = screenInfo.getHeight();
        var sizecorner = 20;

        // Calcul des coordonnées réelles
        double xdeb = depx + (width / 2) - 100 - (widthCard / 2);
        double ydeb = depy + (height / 2) - 25 - (lengthCard / 2);
        double xfin = xdeb + depx + ((widthCard - sizecorner) * p.x());
        double yfin = ydeb + depy + ((lengthCard - sizecorner) * p.y());

        return new Position((int) xfin, (int) yfin);
    }

    /**
     * Affiche les positions possibles.
     *
     * @param context Le contexte de l'application.
     * @param p       La position à afficher.
     */
    public static void possible(ApplicationContext context, Position p) {
        int outline = 8;
        context.renderFrame(graphics -> {
            graphics.setColor(Color.ORANGE);
            graphics.drawRect(p.x() - outline / 2, p.y() - outline / 2, 100 + outline, 60 + outline);
        });
    }

    /**
     * Affiche la map.
     *
     * @param context Le contexte de l'application.
     */
    public void showMap(ApplicationContext context) {
        context.renderFrame(graphics -> {
            for (Map.Entry<Position, Cards> entry : map.entrySet()) {
                Position position = entry.getKey();
                Cards card = entry.getValue();
                Position realPosition = ConvertCoordinates(context, position);
                card.affiche(context, realPosition);
            }
        });
    }


    /**
     * Recherche les positions jouables autour des cartes existantes.
     *
     * @param context Le contexte de l'application.
     */
    public void findPosibility(ApplicationContext context) {
        posibility = new ArrayList<>();

        // Regarde autour d'une carte posée si on peut jouer
        for (Map.Entry<Position, Cards> cardee : map.entrySet()) {
            var pos = cardee.getKey();
            var card = cardee.getValue();
            var listCorner = card.coin(card.recto());

            // Ajoute les positions possibles en fonction de si le coin est invisible ou pas
            if (!map.containsKey(new Position(pos.x() - 1, pos.y() - 1)) && !listCorner.get(0).equals("Invisible")) {
                posibility.add(new Position(pos.x() - 1, pos.y() - 1));
            }
            if (!map.containsKey(new Position(pos.x() + 1, pos.y() - 1)) && !listCorner.get(1).equals("Invisible")) {
                posibility.add(new Position(pos.x() + 1, pos.y() - 1));
            }
            if (!map.containsKey(new Position(pos.x() + 1, pos.y() + 1)) && !listCorner.get(2).equals("Invisible")) {
                posibility.add(new Position(pos.x() + 1, pos.y() + 1));
            }
            if (!map.containsKey(new Position(pos.x() - 1, pos.y() + 1)) && !listCorner.get(3).equals("Invisible")) {
                posibility.add(new Position(pos.x() - 1, pos.y() + 1));
            }
        }

        // Regarde autour d'une carte possible si on peut jouer
        if (posibility.size() > 0) {
            List<Position> toRemove = new ArrayList<>(); // Liste des possibilités à enlever

            for (var pos : posibility) {
                if (map.containsKey(new Position(pos.x() - 1, pos.y() - 1))) {
                    var card = map.get(new Position(pos.x() - 1, pos.y() - 1));
                    if (card.coin(card.recto()).get(2).equals("Invisible")) {
                        toRemove.add(pos);
                    }
                }  if (map.containsKey(new Position(pos.x() + 1, pos.y() - 1))) {
                    var card = map.get(new Position(pos.x() + 1, pos.y() - 1));
                    if (card.coin(card.recto()).get(3).equals("Invisible")) {
                        toRemove.add(pos);
                    }
                } if (map.containsKey(new Position(pos.x() + 1, pos.y() + 1))) {
                    var card = map.get(new Position(pos.x() + 1, pos.y() + 1));
                    if (card.coin(card.recto()).get(0).equals("Invisible")) {
                        toRemove.add(pos);
                    }
                } if (map.containsKey(new Position(pos.x() - 1, pos.y() + 1))) {
                    var card = map.get(new Position(pos.x() - 1, pos.y() + 1));
                    if (card.coin(card.recto()).get(1).equals("Invisible")) {
                        toRemove.add(pos);
                    }
                }
            }

            // Enlève les éléments de la liste des possibilités
            posibility.removeAll(toRemove);
        }
    }

    /**
     * Affiche les positions possibles.
     *
     * @param context Le contexte de l'application.
     */
    public void showPossibility(ApplicationContext context) {
        this.findPosibility(context);
        if (posibility.size() != 0) {
            System.out.println(posibility);
            for (var pos : posibility) {
                possible(context, ConvertCoordinates(context, pos));
            }
        }
    }

    /**
     * Vérifie si une position est touchée.
     *
     * @param context Le contexte de l'application.
     * @param p       La position à vérifier.
     * @return true si la position est touchée, sinon false.
     */
    public Boolean touch(ApplicationContext context, Position p) {
        if (posibility.size() > 0) {
            for (var pos : posibility) {
                var mapPos = ConvertCoordinates(context, pos);
                if ((p.x() - mapPos.x()) < 100 && (p.x() - mapPos.x()) > 0 && (p.y() - mapPos.y()) < 60 && (p.y() - mapPos.y()) > 0) {
                    select = pos;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Ajoute une carte à la position spécifiée.
     *
     * @param p La position où ajouter la carte.
     * @param c La carte à ajouter.
     */
    public void add(Position p, Cards c) {
        map.put(p, c);
    }

    /**
     * Renvoie la position sélectionnée.
     *
     * @return La position sélectionnée.
     */
    public Position select() {
        return select;
    }

    /**
     * Renvoie la liste des éléments cacher par la carte de la position spécifiée.
     *
     * @param pos La position à vérifier.
     * @return La liste des éléments cacher a retirer.
     */		
    public ArrayList<String> cacher(Position pos) {
        var Listcover = new ArrayList<String>();

        if (map.containsKey(new Position(pos.x() - 1, pos.y() - 1))) {
            var card = map.get(new Position(pos.x() - 1, pos.y() - 1));
						var list = card.coin(card.recto());
			            Listcover.add(list.get(2));
			        } else if (map.containsKey(new Position(pos.x() + 1, pos.y() - 1))) {
			            var card = map.get(new Position(pos.x() + 1, pos.y() - 1));
			            var list = card.coin(card.recto());
			            Listcover.add(list.get(3));
			        } else if (map.containsKey(new Position(pos.x() + 1, pos.y() + 1))) {
			            var card = map.get(new Position(pos.x() + 1, pos.y() + 1));
			            var list = card.coin(card.recto());
			            Listcover.add(list.get(0));
			        } else if (map.containsKey(new Position(pos.x() - 1, pos.y() + 1))) {
			            var card = map.get(new Position(pos.x() - 1, pos.y() + 1));
			            var list = card.coin(card.recto());
			            Listcover.add(list.get(1));
			        }

			        return Listcover;
			    }
			}
		
		
		

