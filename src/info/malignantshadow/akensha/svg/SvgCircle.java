package info.malignantshadow.akensha.svg;

public class SvgCircle implements SvgPart {

	private int _x, _y, _r;

	public SvgCircle(int x, int y, int radius) {
		_x = x;
		_y = y;
		_r = radius;
	}

	@Override
	public String render() {
		return "\n  <circle fill=\"black\" cx=\"" + _x + "\" cy=\"" + _y + "\" r=\"" + _r + "\"/>";
	}

}
