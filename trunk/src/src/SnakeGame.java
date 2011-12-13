package src;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import tools.Position;

public class SnakeGame {

	Etat etat;
	int etatActual = MENU;
	int etatTemp = MENU;
	// Constant for States
	public static final int MENU = 0;
	public static final int GAME = 4;
	public static final int HIGHSCORE = 2;
	public static final int QUIT = 3;
	public static final int COUNTDOWN = 1;
	public static final int PERDU = 5;

	public static final int APPLENUMBER = 5;
	
	public static final int HEIGHT=600;
	public static final int WIDTH=800;
	
	private float lightAmbient[] = { 0.5f, 0.5f, 0.5f, 1.0f };  // Ambient Light Values ( NEW )
    private float lightDiffuse[] = { 1.0f, 1.0f, 1.0f, 1.0f };      // Diffuse Light Values ( NEW )
    private float lightPosition[] = { 0.0f, 0.0f, 2.0f, 1.0f }; // Light Position ( NEW )
	
	public static final Position MAP_MILIEU = new Position(WIDTH-300, HEIGHT-300); 
	// float lightPosition1[] = { -MAP_SIZE, -MAP_SIZE, 1f, 1f };

	/** time at last frame */
	long lastFrame;
	/** frames per second */
	int fps;
	/** last fps time */
	long lastFPS;

	public static boolean exit = false;
	public static boolean switchView = false;

	public int chooseBestDisplay() throws LWJGLException {
		int res = 3;
		return res;
	}

	public void start() throws LWJGLException, InterruptedException,
			IOException {

		try {
			int bestDisplay = chooseBestDisplay();
			DisplayMode dm = Display.getAvailableDisplayModes()[bestDisplay];
			System.out.println(dm);
			Display.setDisplayMode(dm);
			Display.setFullscreen(false);
			Display.setResizable(true);
			Display.setTitle("Graphics");
			Display.create();

		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		initGL();
		etat = new Menu();
		getDelta();
		lastFPS = getTime();
		while (!Display.isCloseRequested() && !exit) { // Done Drawing The Quad
			if (etatTemp == QUIT) {
				exit = true;
			} else if (etatTemp != etatActual) {
				switch (etatTemp) {
				case MENU:
					etat = new Menu();
					break;
				case GAME:
					etat = new Game();
					break;
				case COUNTDOWN:
					etat = new Countdown();
					break;
				case PERDU:
					etat = new Menu();
					break;
				case HIGHSCORE:
					etat = new HighScore();
				}
				etatActual = etatTemp;
			}

			int delta = getDelta();
			etatTemp = etat.update(delta);
			etat.renderGL();
			pollInput();
			Display.update(); // flushes OpenGL pipeline and swaps back and
								// front buffers. perhaps waits for v-sync.
		}

		Display.destroy();
	}

	private void initGL() throws IOException {
		glShadeModel(GL_SMOOTH); // Enable Smooth Shading
		glClearColor(0.0f, 0.0f, 0.0f, 0.5f); // Black Background
		glClearDepth(1.0f); // Depth Buffer Setup
		glEnable(GL_DEPTH_TEST); // Enables Depth Testing
		glDepthFunc(GL_LEQUAL); // The Type Of Depth Testing To Do
		glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // Really Nice
															// Perspective
															// Calculations
		glEnable(GL_COLOR_MATERIAL);
		glEnable(GL_LIGHTING);
		glEnable(GL_TEXTURE_2D);
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, WIDTH, HEIGHT, 0, 100, -100);
		GLU.gluPerspective(0.0f, (float) Display.getWidth() / (float) Display.getWidth(),0.1f,100.0f);

		glEnable(GL_LIGHTING);
		glEnable(GL_LIGHT0);

		
		glMatrixMode(GL_MODELVIEW);
		ByteBuffer temp = ByteBuffer.allocateDirect(16);
        temp.order(ByteOrder.nativeOrder());
		GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, (FloatBuffer)temp.asFloatBuffer().put(lightAmbient).flip());              // Setup The Ambient Light
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, (FloatBuffer)temp.asFloatBuffer().put(lightDiffuse).flip());              // Setup The Diffuse Light
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION,(FloatBuffer)temp.asFloatBuffer().put(lightPosition).flip());         // Position The Light
        GL11.glEnable(GL11.GL_LIGHT1);
	}

	public static void main(String[] argv) throws LWJGLException,
			InterruptedException, IOException {
		SnakeGame game = new SnakeGame();
		game.start();
	}

	public void pollInput() throws LWJGLException, IOException {

		if (Mouse.isButtonDown(0)) {
			int x = Mouse.getX();
			int y = Mouse.getY();
			System.out.println("MOUSE DOWN @ X: " + x + " Y: " + y);
		}

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_A) {
					System.out.println("A Key Pressed");
					if (Display.isFullscreen())
						Display.setFullscreen(false);
					else
						Display.setFullscreen(true);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
					System.out.println("Escape Key Pressed");
					exit = true;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_V) {
					System.out.println("V Key Pressed");
					switchView = switchView ? false : true;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_R) {
					System.out.println("R Key Pressed - Game restart");
					etat = new Game();
				}
			}
		}
	}

	private int getDelta() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;

		return delta;
	}

	private long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
}