package info.malignantshadow.akensha;

import java.util.ArrayList;
import java.util.List;

public class Letter {

	static {
		_letters = new ArrayList<Letter>();
	}

	public static final int BASE_STRAIGHT = 0;
	public static final int BASE_LEFT = 1;
	public static final int BASE_RIGHT = 2;

	public static Letter A = Letter.right('a', BASE_LEFT, Arm.LOWER);
	public static Letter B = new Letter('b', Arm.UPPER, Arm.DOWN, false);
	public static Letter C = new Letter('c', Arm.DOWN, Arm.UPPER);
	public static Letter D = Letter.left('d', BASE_LEFT, Arm.UPPER);
	public static Letter E = new Letter('e', Arm.OUT_DOWN, Arm.OUT_UP);
	public static Letter F = Letter.right('f', BASE_RIGHT, Arm.LOWER);
	public static Letter G = Letter.left('g', BASE_LEFT, Arm.LOWER);
	public static Letter H = new Letter('h', Arm.LOWER, Arm.UPPER);
	public static Letter I = new Letter('i', Arm.OUT_DOWN, Arm.UPPER);
	public static Letter J = new Letter('j', Arm.OUT_UP, Arm.UPPER, false);
	public static Letter K = new Letter('k', Arm.UP, Arm.UPPER, false);
	public static Letter L = new Letter('l', Arm.LOWER, Arm.OUT_UP);
	public static Letter M = new Letter('m', Arm.LOWER, Arm.LOWER);
	public static Letter N = new Letter('n', Arm.UP, Arm.LOWER, false);
	public static Letter O = Letter.left('o', BASE_RIGHT, Arm.OUT_DOWN);
	public static Letter P = Letter.right('p', BASE_LEFT, Arm.OUT_DOWN);
	public static Letter Q = Letter.right('q', BASE_RIGHT, Arm.OUT_UP);
	public static Letter R = new Letter('r', Arm.OUT_UP, Arm.DOWN, false);
	public static Letter S = new Letter('s', Arm.DOWN, Arm.UP);
	public static Letter T = Letter.right('t', BASE_RIGHT, Arm.UPPER);
	public static Letter U = new Letter('u', Arm.OUT_UP, Arm.LOWER, false);
	public static Letter V = Letter.right('v', BASE_LEFT, Arm.UPPER);
	public static Letter W = new Letter('w', Arm.UPPER, Arm.UPPER);
	public static Letter X = new Letter('x', Arm.UP, Arm.DOWN, false);
	public static Letter Y = Letter.right('y', BASE_LEFT, Arm.UP);
	public static Letter Z = new Letter('z', Arm.LOWER, Arm.UPPER, false);

	private int _base;
	private Arm _left, _right;
	private char _character;

	private static List<Letter> _letters;

	private Letter(char c, int leftType, int rightType) {
		this(c, BASE_STRAIGHT, leftType, rightType);
	}

	private Letter(char c, int leftType, int rightType, boolean leftIsUpper) {
		this(c, BASE_STRAIGHT, leftType, rightType, leftIsUpper);
	}

	private Letter(char c, int base, int leftType, int rightType) {
		this(c, base, leftType, rightType, true);
	}

	private Letter(char c, int base, int leftType, int rightType, boolean leftIsUpper) {
		this(c, base,
				(leftType >= 0 ? new Arm(leftType, leftIsUpper) : null),
				(rightType >= 0 ? new Arm(rightType, !leftIsUpper) : null));
	}

	private Letter(char c, int base, Arm left, Arm right) {
		_character = c;
		_base = base;
		_left = left;
		_right = right;
		_letters.add(this);
	}

	private static Letter left(char c, int base, int leftType) {
		return new Letter(c, base, leftType, -1);
	}

	private static Letter right(char c, int base, int rightType) {
		return new Letter(c, base, -1, rightType, false);
	}

	public char getCharacter() {
		return _character;
	}

	public int getBaseType() {
		return _base;
	}

	public Arm getLeftArm() {
		return _left;
	}

	public Arm getRightArm() {
		return _right;
	}

	public static Letter getLetter(char c) {
		c = Character.toLowerCase(c);
		for (Letter l : _letters) {
			if (l._character == c)
				return l;
		}

		return null;
	}

	public static class Arm {

		// Arms that are diagonal then go up/down
		public static final int UP = 0;
		public static final int DOWN = 1;

		// Arms that go out then point 45deg
		public static final int OUT_UP = 2;
		public static final int OUT_DOWN = 3;

		// Arms that point straight 45deg
		public static final int UPPER = 4;
		public static final int LOWER = 5;

		private int _type;
		private boolean _upper;

		private Arm(int type) {
			this(type, false);
		}

		private Arm(int type, boolean isUpper) {
			_type = type;
			_upper = isUpper;
		}

		public int getType() {
			return _type;
		}

		public boolean isUpper() {
			return _upper;
		}

		public boolean isClass1() {
			return isClass1(_type);
		}

		public static boolean isClass1(int type) {
			switch (type) {
				case UP:
				case DOWN:
					return true;

				default:
					return false;
			}
		}

		public boolean isClass2() {
			return isClass2(_type);
		}

		public static boolean isClass2(int type) {
			switch (type) {
				case OUT_UP:
				case OUT_DOWN:
					return true;

				default:
					return false;
			}
		}

		public boolean isClass3() {
			return isClass3(_type);
		}

		public static boolean isClass3(int type) {
			switch (type) {
				case UPPER:
				case LOWER:
					return true;

				default:
					return false;
			}
		}

		public int getYModifier() {
			return getYModifier(_type);
		}

		public static int getYModifier(int type) {
			switch (type) {
				case UP:
				case OUT_UP:
				case UPPER:
					return -1;
				default:
					return 1;
			}
		}

	}

}
