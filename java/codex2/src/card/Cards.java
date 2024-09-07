package card;

import java.util.ArrayList;
import Main.Position;
import fr.umlv.zen5.ApplicationContext;

/**
 * Interface représentant les action sur les cartes.
 */
public interface Cards {

    /**
     * Retourne une liste de chaînes de caractères représentant les coins de la carte.
     * 
     * @param recto Un paramètre booléen influençant quels coins prendre entre ceux du recto ou du verso.
     * @return Une liste de chaînes de caractères représentant les coins de la carte.
     */
    public ArrayList<String> coin(Boolean recto);

    /**
     * Retourne le score de la carte sous forme de chaîne de caractères.
     * 
     * @return Le score de la carte.
     */
    public String scoring();

    /**
     * Retourne le type de la carte sous forme de chaîne de caractères.
     * 
     * @return Le type de la carte.
     */
    public String type();

    /**
     * Affiche la carte à une position donnée dans le contexte de l'application.
     * 
     * @param context Le contexte de l'application dans lequel la carte est affichée.
     * @param pos La position à laquelle la carte doit être affichée.
     */
    public void affiche(ApplicationContext context, Position pos);

    /**
     * Vérifie si la carte est recto (face visible).
     * 
     * @return True si la carte est recto, false sinon.
     */
    public Boolean recto();

    /**
     * Vérifie si la carte est une carte gold.
     * 
     * @return True si la carte est Gold, false sinon.
     */
    public Boolean isgold();

    /**
     * Tourne la carte (change la face visible).
     * 
     * @return La carte après l'opération de rotation.
     */
    public Cards tourne();
}
