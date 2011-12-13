package tools;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Fichier {

	public static final int MAX_SCORE = 10;
	static LinkedList<Integer> highScore = new LinkedList<Integer>();

	public static void ecrire(String nomFic, int i) {

		try {
			FileInputStream fichier = new FileInputStream("highscore.txt");
			ObjectInputStream ois = new ObjectInputStream(fichier);
			highScore = (LinkedList<Integer>) ois.readObject();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
		}
		int position = sort(highScore,i);
		highScore.add(position,i);
		if(highScore.size()>MAX_SCORE)
			highScore = new LinkedList<Integer>(highScore.subList(0, MAX_SCORE));
		System.out.println(highScore);
		
		try {
			FileOutputStream fichier = new FileOutputStream("highscore.txt");
			ObjectOutputStream oos = new ObjectOutputStream(fichier);
			oos.writeObject(highScore);
			oos.flush();
			oos.close();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}

	}

	private static int sort(List<Integer> highScore2, int a) {
		int position = 0;
		Iterator<Integer> ite = highScore2.iterator();
		while(ite.hasNext()){
			int b = ite.next().intValue();
			if(a>b){
				return position;
			}
			position++;
		}
		return position;
	}

	public static void main(String[] args){
		ecrire("highScore.txt", 350);
	}

	public static LinkedList<Integer> getScore(String string) {
		try {
			FileInputStream fichier = new FileInputStream("highscore.txt");
			ObjectInputStream ois = new ObjectInputStream(fichier);
			highScore = (LinkedList<Integer>) ois.readObject();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
		}
		System.out.println(highScore);
		return highScore;
	}
}
