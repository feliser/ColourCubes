package colourcubes;

import java.awt.Color;
import java.awt.Graphics;

public class GridSquare {
	Color color;
	
	public GridSquare() {
		int random = (int)(4 * Math.random() + 1);
		if(random == 1) {
			color = Color.RED;
		} else if(random == 2) {
			color = Color.BLUE;
		} else if(random == 3) {
			color = Color.YELLOW;
		} else if(random == 4) {
			color = Color.GREEN;
		}
	}
	
	public void draw(Graphics g, int x, int y, int widthOffset, int heightOffset) {
		g.setColor(Color.BLACK); 
		// Black border
		g.fillRect(x - 1, y - 1, Main.SIZE + 2 - widthOffset, Main.SIZE + 2 - heightOffset);
		if(Main.inGame == false && Main.ready == true) g.setColor(Color.LIGHT_GRAY.darker());
		else g.setColor(color.darker());
		// Dark edge
		g.fillRect(x + 1, y + 1, Main.SIZE - 2 - widthOffset, Main.SIZE - 2 - heightOffset);
		if(Main.inGame == false && Main.ready == true) g.setColor(Color.LIGHT_GRAY);
		else g.setColor(color);
		// Color center
		g.fillRect(x + 4, y + 4, Main.SIZE - 8 - widthOffset, Main.SIZE - 8 - heightOffset);
	}
}
