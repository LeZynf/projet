package Main;

import fr.umlv.zen5.*;
import fr.umlv.zen5.Event.Action;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import donnee.Cardcreator;
import donnee.Mapping;
import donnee.GameInfo;

public class Main {
	
	/**
     * Met à jour l'affichage de l'application.
     * 
     * @param context     Le contexte d'application pour le rendu graphique.
     * @param deck        Le deck de cartes du joueur.
     * @param piocheRes   Le paquet de pioche de cartes de ressources.
     * @param piocheGold  Le paquet de pioche de cartes d'or.
     * @param map         La carte du jeu.
     * @param gameinfo    Les informations sur le déroulement du jeu.
     */
    public static void update(ApplicationContext context, Deck deck, DrawPackressources piocheRes, DrawPackgold piocheGold, Mapping map,GameInfo gameinfo) {
        var screenInfo = context.getScreenInfo();
        var width = screenInfo.getWidth();
        var height = screenInfo.getHeight();
        var marginleft = 200;
        var margintop = 50;
       
            
        context.renderFrame(graphics -> {
            var fond = new Rectangle2D.Float(0, 0, width, height);
            graphics.setColor(Color.BLACK);
            graphics.fill(fond); 
            
       
            map.showMap(context);
            
           
            var topBorder = new Rectangle2D.Float(0,0, width , margintop-1 );
            var bottomBorder = new Rectangle2D.Float(0, height - 160-1  , width, 160);
            var leftBorder = new Rectangle2D.Float(0, 0, marginleft-1,height);
            var rightBorder = new Rectangle2D.Float(width-marginleft * 2 ,0, marginleft * 2, height );

            graphics.setColor(Color.BLACK);
            graphics.fill(topBorder);
            graphics.fill(bottomBorder);
            graphics.fill(leftBorder);
            graphics.fill(rightBorder);
            
            
             var mapcontour = new Rectangle2D.Float(marginleft, margintop, width - marginleft * 3, height - margintop - 160);
            
            
            
            
           
            graphics.setColor(Color.WHITE);
            graphics.draw(mapcontour);
           
            
            gameinfo.show(context);
            deck.show(context);
            deck.showSelect(context);
            piocheRes.show(context);
            piocheGold.show(context);
            if (deck.select != -1) {
            	map.showPossibility(context);
            }
        });
    }

    public static void main(String[] args) {
    	
    	// Initialisation des cartes
    	var cardcreator = new Cardcreator();
        String filePath = "include/deck.txt";  

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
            	cardcreator.processLine(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
         // initialisation des pioche deck etc
        var gameinfo = new GameInfo();
        var map = new Mapping();
       
        var piocheRes = new DrawPackressources();
        var piocheGold = new DrawPackgold();
        
        var listcardRes = cardcreator.Resource();
        var listcardGol = cardcreator.Golden();
                
        var deck = new Deck();
        deck.remplis(listcardGol,listcardRes );
        
        piocheRes.remplis(listcardRes);
        piocheGold.remplis(listcardGol);
   
        var starterlist = new Starteur();
        starterlist.remplis(cardcreator.Starter());
        
      
        
        Application.run(Color.BLACK, context -> {
            context.renderFrame(graphics -> {
            	
            	
               starterlist.choice(context);
                   
            });
            // boucle pour la selection de starteur card
            while (true) {
                   var event = context.pollOrWaitEvent(10);
                   if (event == null) {
                       continue;
                   }

                   var action = event.getAction();
                   if (action == Action.POINTER_DOWN) {
                       var location = event.getLocation();
                       if (starterlist.touch(new Position((int) location.getX(), (int) location.getY()), context)) {
                    	   
                    	   starterlist.addtoMap(starterlist.select, map, gameinfo);
                    	   update(context, deck, piocheRes, piocheGold, map, gameinfo);
                    	   break;
                       } else {starterlist.choice(context);
                       
                       }
                   }
                   
            }
                       
                       // boucle principale du jeu
            	var dep = 10;
            while (true) {
                var event = context.pollOrWaitEvent(10);
                if (event == null) {
                    continue;
                }

                var action = event.getAction();
                 // deplacement de la map
                if (action == Action.KEY_PRESSED) {
        		    switch (event.getKey()) {
        		   
        		        case KeyboardKey.LEFT:
        		        	map.move(-dep, 0);
        		            break;
        		            
        		        case KeyboardKey.RIGHT:
        		        	map.move(dep, 0);
        		            break;
        		            
        		        case KeyboardKey.UP:
        		        	map.move(0, -dep);
        		            break;
        		            
        		        case KeyboardKey.DOWN:
        		        	map.move(0, dep);
        		            break;
        		            
        		        case KeyboardKey.Q:
        		        	
        		        	context.exit(1);
        		            break;
        		            
        		        default:
        		         
        		            break;
        		    	}
        		    update(context, deck, piocheRes, piocheGold, map, gameinfo);
        		    }
                	// jouer
                if (action == Action.POINTER_DOWN) {
                    var location = event.getLocation();
                    System.out.println(location);
                    gameinfo.Game(piocheRes, piocheGold, new Position((int) location.getX(), (int) location.getY()), deck, map, context, gameinfo);

                    update(context, deck, piocheRes, piocheGold, map, gameinfo);
                    gameinfo.win(context);
                
                    }
                }
            
        });
    }
}
