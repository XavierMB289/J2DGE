package gui.copying;

import java.awt.Dimension;

import engine.Window;

public class BasicMain extends Window{
	
	private static final long serialVersionUID = 2616195005007926169L;

	public BasicMain() {
		super("Window Name");
		setSize(new Dimension(500, 400));
		setVisible();
	}
	
	public static void main(String[] args) {
		new BasicMain();
	}
	
}
