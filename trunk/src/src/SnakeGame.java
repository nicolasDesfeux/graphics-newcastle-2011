package src;

import java.io.IOException;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
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
	public static final int RESTART = 6;

	public static final int APPLENUMBER = 5;

	public static final int HEIGHT = 600;
	public static final int WIDTH = 800;

	public static int score;

	public static final Position MAP_MILIEU = new Position(WIDTH - 400,
			HEIGHT - 300);

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
			Display.setDisplayMode(dm);
			Display.setFullscreen(false);
			Display.setResizable(true);

			Display.setTitle("Snake 3D - 2012 - version 1.0");
			Display.create();

		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

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
				case RESTART:
					etat = new Game();
					break;
				case COUNTDOWN:
					etat = new Countdown();
					break;
				case PERDU:
					etat = new Perdu(score);
					break;
				case HIGHSCORE:
					etat = new HighScore();
					break;

				}
				etatActual = etatTemp;
			}

			int delta = getDelta();
			etatTemp = etat.update(delta);
			etat.renderGL();
			int etatT = etat.pollInput();
			if (etatT != etatActual) {
				etatTemp = etatT;
			}
			Display.update(); // flushes OpenGL pipeline and swaps back and
								// front buffers. perhaps waits for v-sync.
		}

		Display.destroy();
	}

	public static void main(String[] argv) throws LWJGLException,
			InterruptedException, IOException {
		SnakeGame game = new SnakeGame();
		game.start();
	}

	public void pollInput() throws LWJGLException, IOException {
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_A) {
					if (Display.isFullscreen())
						Display.setFullscreen(false);
					else
						Display.setFullscreen(true);
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
					exit = true;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_V) {
					switchView = switchView ? false : true;
				}
				if (Keyboard.getEventKey() == Keyboard.KEY_R) {
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