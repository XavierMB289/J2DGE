package engine;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import backends.objs.AppPage;

public class StartingPanel extends AppPage{

	private static final long serialVersionUID = 839804302267614473L;
	
	JComboBox<String> resolutions;
	JComboBox<String> screens;
	String res;
	JCheckBox fsCheck;
	JButton play;

	public StartingPanel(GameWindow win) {
		super(win);
	}
	
	public void init() {
		resolutions = new JComboBox<String>(w.ALLOWED_RESOLUTIONS);
		resolutions.setSelectedIndex(0);
		res = resolutions.getItemAt(0);
		resolutions.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				res = (String)((JComboBox<?>)e.getSource()).getSelectedItem();
				System.out.println("Resolution selected: "+res);
			}
			
		});
		resolutions.validate();
		
		GraphicsEnvironment g = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] devices = g.getScreenDevices();
		String[] screenNum = new String[devices.length];
		for(int i = 0; i < devices.length; i++){
			screenNum[i] = "Display "+i;
		}
		screens = new JComboBox<String>(screenNum);
		screens.setSelectedIndex(0);
		screens.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				w.MAIN_SCREEN = Integer.parseInt(((String)((JComboBox<?>)e.getSource()).getSelectedItem()).split(" ")[1]);
			}
			
		});
		screens.validate();
		
		fsCheck = new JCheckBox("Fullscreen?");
		fsCheck.setToolTipText("Overrides the resolution...");
		fsCheck.setMnemonic('F');
		if(w.FORCE_FULLSCREEN){
			fsCheck.setSelected(true);
			fsCheck.setEnabled(false);
		}
		fsCheck.validate();
		
		play = new JButton("PLAY");
		play.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				w.windowSetup();
				
				if(fsCheck.isSelected()) {
					w.fullscreenExclusive(w.MAIN_SCREEN);
					w.setVisible();
					if(w.paintImg == null){
						w.paintImg = new BufferedImage(w.getFrame().getWidth(), w.getFrame().getHeight(), BufferedImage.TYPE_INT_ARGB);
					}
					w.getLoop().init();
				}else {
					if(res != null && res.contains("x")) {
						String[] temp = res.split("x");
						w.setWindowSize(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
						w.setVisible();
						if(w.paintImg == null){
							w.paintImg = new BufferedImage(w.getFrame().getWidth(), w.getFrame().getHeight(), BufferedImage.TYPE_INT_ARGB);
						}
						w.getLoop().init();
					}
				}
			}
			
		});
		play.validate();
		
		int halfW = w.getFrame().getWidth()/2;
		int halfH = w.getFrame().getHeight()/2;
		int h12 = w.getFrame().getHeight()/12;
		
		Dimension temp = resolutions.getPreferredSize();
		resolutions.setBounds(halfW-temp.width/2, (int)h12*2-temp.height/2, temp.width, temp.height);
		w.addComp(resolutions);
		
		temp = screens.getPreferredSize();
		screens.setBounds(halfW-temp.width/2, (int)h12*4-temp.height/2, temp.width, temp.height);
		w.addComp(screens);
		
		temp = fsCheck.getPreferredSize();
		fsCheck.setBounds(halfW-temp.width/2, halfH-temp.height/2, temp.width, temp.height);
		w.addComp(fsCheck);
		
		temp = play.getPreferredSize();
		play.setBounds(halfW-temp.width/2, (int)h12*10-temp.height/2, temp.width, temp.height);
		w.addComp(play);
	}

	@Override
	public void onChange() {
		w.removeComp(resolutions);
		w.removeComp(screens);
		w.removeComp(fsCheck);
		w.removeComp(play);
	}

	@Override
	public void paint(Graphics2D g) {
		
	}

	@Override
	public void update(double delta) {  }
	
}
