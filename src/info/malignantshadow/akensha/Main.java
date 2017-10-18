package info.malignantshadow.akensha;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {
//		List<LetterInfo> word = new ArrayList<LetterInfo>();
//		word.add(new LetterInfo(Letter.C, 125, 275, 75, 0));
//		word.add(new LetterInfo(Letter.A, 0, 0, 75, 0));
//		word.add(new LetterInfo(Letter.L, 75, 0, 100, 50));
//		word.add(new LetterInfo(Letter.E, 25, 50, 50, 100));
//		word.add(new LetterInfo(Letter.B, 50, 0, 50, 75));
//
//		Renderer r = new Renderer(word, 10, 100, 6);
		Renderer r = Renderer.getRenderer("Sarah", 10, 100, 6);
		try {
			r.render("D:\\Programming\\Ascension\\word_test.svg");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
