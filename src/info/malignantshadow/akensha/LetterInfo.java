package info.malignantshadow.akensha;

import info.malignantshadow.akensha.Renderer.Point;

public class LetterInfo {

	private Letter _letter;
	private int _lx, _ly, _rx, _ry;

	public LetterInfo(Letter letter) {
		this(letter, -1, -1, -1, -1);
	}

	public LetterInfo(Letter letter, int leftX, int leftY, int rightX, int rightY) {
		_letter = letter;
		_lx = leftX;
		_ly = leftY;
		_rx = rightX;
		_ry = rightY;
	}

	public static LetterInfo left(Letter letter, int x, int y) {
		return new LetterInfo(letter, x, y, -1, -1);
	}

	public static LetterInfo right(Letter letter, int x, int y) {
		return new LetterInfo(letter, -1, -1, x, y);
	}

	public Letter getLetter() {
		return _letter;
	}

	public void setLetter(Letter letter) {
		_letter = letter == null ? Letter.A : letter;
	}

	public Point getLeftDelta() {
		Point p = new Point();
		p.x = _lx;
		p.y = _ly;
		return p;
	}

	public int getLeftX() {
		return _lx;
	}

	public void setLeftX(int x) {
		_lx = x;
	}

	public int getLeftY() {
		return _ly;
	}

	public void setLeftY(int y) {
		_ly = y;
	}

	public Point getRightDelta() {
		Point p = new Point();
		p.x = _rx;
		p.y = _ry;
		return p;
	}

	public int getRightX() {
		return _rx;
	}

	public void setRightX(int x) {
		_rx = x;
	}

	public int getRightY() {
		return _ry;
	}

	public void setRightY(int y) {
		_ry = y;
	}
}
