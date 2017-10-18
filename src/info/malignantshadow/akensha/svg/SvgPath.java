package info.malignantshadow.akensha.svg;

import java.util.ArrayList;
import java.util.List;

public class SvgPath implements SvgPart {

	private List<String> _commands;
	private int _strokeWidth;

	public SvgPath(int strokeWidth) {
		_commands = new ArrayList<String>();
		_strokeWidth = strokeWidth;
	}

	public void moveTo(int x, int y) {
		_commands.add("M" + x + " " + y);
	}

	public void lineTo(int x, int y) {
		_commands.add("L" + x + " " + y);
	}

	@Override
	public String render() {
		String d = "";
		for (String s : _commands) {
			d += s + " ";
		}

		return "\n  <path fill=\"none\" stroke=\"#000\" stroke-linecap=\"round\" stroke-width=\"" + _strokeWidth + "\" d=\"" + d.trim() + "\"/>";
	}
}
