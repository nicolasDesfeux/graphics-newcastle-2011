package tools;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Reading and writing in files
 * @author Nicolas
 *
 */
public class Fichier {

	public static final int MAX_SCORE = 10;
	static LinkedList<Integer> highScore = new LinkedList<Integer>();

	/**
	 * Write a string into a file
	 * @param nomFic File to write in
	 * @param i String to write
	 */
	public static void ecrire(String nomFic, int i) {

		highScore = getScore(nomFic);
		int position = getInsertionPosition(highScore,i);
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

	private static int getInsertionPosition(List<Integer> highScore2, int a) {
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

	//Test
	public static void main(String[] args){
		ecrire("highScore.txt", 350);
	}

	/**
	 * Generate the score list from the high score file
	 * @param string File to read
	 * @return high score list
	 */
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

	/**
	 * Generate a hashmap from a text file
	 * @param nomFich file to read
	 * @return hashmap from the file.
	 */
	public static HashMap<String, String> lire(String nomFich) {
		HashMap<String, String> res = new HashMap<String, String>();
		try{
			InputStream ips=new FileInputStream(nomFich); 
			InputStreamReader ipsr=new InputStreamReader(ips);
			BufferedReader br=new BufferedReader(ipsr);
			String ligne;
			while ((ligne=br.readLine())!=null){
				String[] tmp = ligne.split("\\s");
				res.put(tmp[0], tmp[1]);
			}
			br.close(); 
		}		
		catch (Exception e){
			//System.out.println(e.toString());
		}
		return res;
	}
}
