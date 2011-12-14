package tools;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Fichier {

	public static final int MAX_SCORE = 10;
	static LinkedList<Integer> highScore = new LinkedList<Integer>();

	@SuppressWarnings("unchecked")
	public static void ecrire(String nomFic, int i) {

		highScore = getScore(nomFic);
		int position = sort(highScore,i);
		highScore.add(position,i);
		if(highScore.size()>MAX_SCORE)
			highScore = new LinkedList<Integer>(highScore.subList(0, MAX_SCORE));
		
		try {
			FileOutputStream fichier = new FileOutputStream(nomFic);
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

	@SuppressWarnings("unchecked")
	public static LinkedList<Integer> getScore(String string) {
		try {
			FileInputStream fichier = new FileInputStream(string);
			ObjectInputStream ois = new ObjectInputStream(fichier);
			highScore = (LinkedList<Integer>) ois.readObject();
		} catch (java.io.IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
		}
		return highScore;
	}
}