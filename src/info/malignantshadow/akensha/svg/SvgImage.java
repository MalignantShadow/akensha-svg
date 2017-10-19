package info.malignantshadow.akensha.svg;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SvgImage {

	private List<SvgPart> _parts;
	private int _w, _h;

	public SvgImage(int w, int h) {
		_w = w;
		_h = h;
		_parts = new ArrayList<SvgPart>();
	}

	public void add(SvgPart part) {
		_parts.add(part);
	}

	public String render() {
		String viewBox = "0 0 " + _w + " " + _h;

		String s = "";
		for (SvgPart p : _parts) {
			s += p.render();
		}

		DateFormat format = new SimpleDateFormat("MMM dd, Y; hh:mm:ssa z");
		Calendar cal = Calendar.getInstance();
		String now = format.format(cal.getTime());

		String comment = "<!-- Generated by info.malignantshadow.akensha.svg -->\n";
		comment += "<!-- Created " + now + " -->\n";

		return comment + "<svg viewBox=\"" + viewBox + "\" xmlns=\"http://www.w3.org/2000/svg\">" + s + "\n</svg>";
	}

}
