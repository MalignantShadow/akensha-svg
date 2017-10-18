package info.malignantshadow.akensha;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import info.malignantshadow.akensha.Letter.Arm;
import info.malignantshadow.akensha.svg.SvgCircle;
import info.malignantshadow.akensha.svg.SvgImage;
import info.malignantshadow.akensha.svg.SvgPath;

public class Renderer {

	private List<List<LetterPoints>> _words;
	private int _letterH, _strokeW, _padding, _dotR;
	private int _svgW = 0, _svgH = 0;

	public Renderer(List<LetterInfo> words, int padding, int letterHeight, int strokeWidth) {
		_letterH = letterHeight;
		_strokeW = strokeWidth;
		_padding = padding;
		_dotR = strokeWidth;
		_words = getPoints(words);
	}

	public static Renderer getRenderer(String string, int padding, int letterHeight, int strokeWidth) {
		List<LetterInfo> info = new ArrayList<LetterInfo>();
		if (string != null) {
			for (int i = 0; i < string.length(); i++) {
				char c = string.charAt(i);
				Letter letter = Letter.getLetter(c);
				LetterInfo letterInfo = null; // non-letters are treated like spaces
				if (letter != null)
					letterInfo = new LetterInfo(letter);

				info.add(letterInfo);
			}
		}

		return new Renderer(info, padding, letterHeight, strokeWidth);
	}

	public String render() {
		SvgImage svg = new SvgImage(_svgW, _svgH);

		for (List<LetterPoints> word : _words) {
			renderWord(svg, word);
		}

		return svg.render();
	}

	public void render(String filename) throws IOException {
		Path path = Paths.get(filename);

		Files.write(path, render().getBytes());
	}

	private void renderWord(SvgImage svg, List<LetterPoints> word) {
		if (word.isEmpty())
			return;

		for (LetterPoints p : word) {
			SvgPath letterPath = new SvgPath(_strokeW);
			if (p == null)
				continue;

			// render base
			letterPath.moveTo(p.start.x, p.start.y);
			letterPath.lineTo(p.lower.x, p.lower.y);
			letterPath.lineTo(p.end.x, p.end.y);

			//render arms (and their dots)
			renderArm(svg, letterPath, p.left);
			renderArm(svg, letterPath, p.right);

			svg.add(letterPath);
		}

		//render start dot, end dot
		LetterPoints first = word.get(0);
		svg.add(new SvgCircle(first.start.x, first.start.y, _dotR));

		LetterPoints last = word.get(word.size() - 1);
		svg.add(new SvgCircle(last.end.x, last.end.y, _dotR));
	}

	private void renderArm(SvgImage svg, SvgPath path, Point[] arm) {
		if (arm == null || arm.length == 0)
			return;

		path.moveTo(arm[0].x, arm[0].y);

		for (int i = 1; i < arm.length; i++) {
			Point p = arm[i];
			path.lineTo(p.x, p.y);
		}

		Point end = arm[arm.length - 1];
		svg.add(new SvgCircle(end.x, end.y, _dotR));
	}

	private void setMin(Point min, Point p) {
		min.x = Math.min(min.x, p.x);
		min.y = Math.min(min.y, p.y);
	}

	private void setMax(Point max, Point p) {
		max.x = Math.max(max.x, p.x);
		max.y = Math.max(max.y, p.y);
	}

	private Point diagonal(Point from, int length, int xMod, int yMod) {
		Point p = new Point();
		p.x = from.x + (length * xMod);
		p.y = from.y + (length * yMod);
		return p;
	}

	private Point[] getArmPoints(boolean left, Point start, Point delta, Arm arm) {
		int xMod = left ? -1 : 1;
		int yMod = arm.getYModifier();
		if (arm.isClass3()) {
			return new Point[] { start, diagonal(start, delta.x, xMod, yMod) };
		} else {
			Point bend = new Point(), end = new Point();
			if (arm.isClass1()) { //diagonal then up/down
				bend = diagonal(start, delta.x, xMod, yMod);

				end.x = bend.x;
				end.y = bend.y + (delta.y * yMod);
			} else { // right/left then diagonal
				bend.x = start.x + (delta.x * xMod);
				bend.y = start.y;

				end = diagonal(bend, delta.y, xMod, yMod);
			}

			return new Point[] { start, bend, end };
		}
	}

	private void setPoints(boolean left, LetterPoints points, Point delta, Arm arm, Consumer<Point[]> setPoints, Point min, Point max) {
		if (arm == null)
			return;

		//cloning to prevent being offset twice
		Point ref = (arm.isUpper() ? points.upper : points.lower).clone();

		int armPartition = (_letterH / 3);

		if (delta.x < 0)
			delta.x = armPartition;
		if (delta.y < 0)
			delta.y = armPartition;

		Point[] armPoints = getArmPoints(left, ref, delta, arm);
		setPoints.accept(armPoints);

		Point end = armPoints[armPoints.length - 1];
		setMin(min, end);
		setMax(max, end);
	}

	private void offsetPoints(int offsetX, int offsetY, Point[] points) {
		if (points == null)
			return;

		for (Point p : points) {
			if (p == null)
				continue;

			p.x += offsetX;
			p.y += offsetY;
		}
	}

	// Represents all points in the word
	private List<List<LetterPoints>> getPoints(List<LetterInfo> word) {
		int third = (_letterH / 3);
		int halfStroke = _strokeW / 2;
		List<List<LetterPoints>> wordList = new ArrayList<List<LetterPoints>>();
		List<LetterPoints> wordPoints = new ArrayList<LetterPoints>();

		LetterPoints last = new LetterPoints();

		Point min = new Point();
		Point max = new Point();
		for (LetterInfo l : word) {
			LetterPoints points = new LetterPoints();
			if (l == null) {
				points.end.y = last.end.y + _letterH; //everything else is 0
				last = points;
				wordList.add(wordPoints);
				wordPoints = new ArrayList<LetterPoints>();
				continue;
			}

			points.start.x = last.end.x;
			points.start.y = last.end.y;
			setMin(min, points.start);
			setMax(max, points.start);

			points.upper.x = points.start.x;
			points.upper.y = points.start.y + third - halfStroke;

			points.lower.x = points.upper.x;
			points.lower.y = points.upper.y + third + (halfStroke * 2);

			Arm left = l.getLetter().getLeftArm();
			Arm right = l.getLetter().getRightArm();

			setPoints(true, points, l.getLeftDelta(), left, ((arm) -> points.left = arm), min, max);
			setPoints(false, points, l.getRightDelta(), right, ((arm) -> points.right = arm), min, max);

			if (l.getLetter().getBaseType() == Letter.BASE_LEFT)
				points.end.x = points.lower.x - third;
			else if (l.getLetter().getBaseType() == Letter.BASE_RIGHT)
				points.end.x = points.lower.x + third;
			else
				points.end.x = points.lower.x;
			points.end.y = points.start.y + _letterH;

			setMin(min, points.end);
			setMax(max, points.end);

			wordPoints.add(points);

			last = points;
		}

		wordList.add(wordPoints);

		int totalPadding = (_strokeW + _dotR + _padding) * 2;
		_svgW = max.x - min.x + totalPadding;
		_svgH = max.y - min.y + totalPadding;

		//since everything started at 0, shift everything
		int offsetX = -min.x + (totalPadding / 2);
		int offsetY = -min.y + (totalPadding / 2);
		for (List<LetterPoints> list : wordList) {
			for (LetterPoints lp : list) {
				if (lp == null)
					continue;

				offsetPoints(offsetX, offsetY, new Point[] { lp.start, lp.upper, lp.lower, lp.end });
				offsetPoints(offsetX, offsetY, lp.left);
				offsetPoints(offsetX, offsetY, lp.right);
			}
		}

		return wordList;
	}

	private static class LetterPoints {

		public Point start = new Point();
		public Point upper = new Point();
		public Point[] left; //start must be upper or lower
		public Point[] right; // start must be upper or lower
		public Point lower = new Point();
		public Point end = new Point();

	}

	public static class Point {

		public int x = 0;
		public int y = 0;

		@Override
		public Point clone() {
			Point p = new Point();
			p.x = this.x;
			p.y = this.y;
			return p;
		}

		@Override
		public String toString() {
			return "Point{" + x + ", " + y + "}";
		}

	}

}
