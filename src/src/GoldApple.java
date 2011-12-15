package src;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

/**
 * Create an Apple with the multi points action
 * @author Nicolas
 *
 */
public class GoldApple extends Eatable {

	public GoldApple() {
		super(0,0, new Color(255,218,0), Eatable.MULTI);
	}

	/**
	 * @inheritDoc
	 */
	public void draw() {	
		double red = ((float)color.getRed())/255;
		double green = ((float)color.getGreen())/255;
		double blue = ((float)color.getBlue())/255;
		glColor3d(red, green, blue);
		super.draw();
	}
}
