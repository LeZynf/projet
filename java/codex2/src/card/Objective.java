package card;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import Main.Position;
import fr.umlv.zen5.ApplicationContext;


public record Objective(int hautgauche, int hautdroite, int basgauche, int basdroite) implements Cards {

    public void affiche(ApplicationContext context,Position pos) {
    		context.renderFrame(graphics -> {

    			graphics.setColor(Color.BLUE);
    			var carte = new Rectangle2D.Float(pos.x(), pos.y(), 100, 60);
    			graphics.fill(carte);
    			
    			graphics.setColor(Color.WHITE);
    			graphics.fillRect(pos.x(),pos.y(),20, 20);
    			graphics.drawString("A", pos.x()+50, pos.y()+30);
    			
    			graphics.fillRect(pos.x()+80,pos.y()+0,20, 20);
    			
    			
    			graphics.fillRect(pos.x()+80,pos.y()+40,20, 20);
    			
    			
    			graphics.fillRect(pos.x() +0,pos.y()+40,20, 20);
    			
    		});
      
    }


	@Override
	public String scoring() {
		// TODO Auto-generated method stub
		return null;
	}



	@Override
	public Boolean recto() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isgold() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<String> coin(Boolean recto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String type() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cards tourne() {
		// TODO Auto-generated method stub
		return null;
	}
}