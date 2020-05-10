package engine;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
	
	public void preInit() {
		resolutions = new JComboBox<String>(w.ALLOWED_RESOLUTIONS);
		resolutions.setSelectedIndex(0);
		resolutions.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				res = (String)((JComboBox<?>)e.getSource()).getSelectedItem();
				System.out.println("Resolution selected: "+res);
			}
			
		});
		
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
		
		fsCheck = new JCheckBox("Fullscreen?");
		fsCheck.setToolTipText("Overrides the resolution...");
		fsCheck.setMnemonic('F');
		
		play = new JButton("PLAY");
		play.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(fsCheck.isSelected()) {
					w.fullscreenExclusive(w.MAIN_SCREEN);
					w.getPanel().init();
				}else {
					if(res != null && res.contains("x")) {
						String[] temp = res.split("x");
						w.setWindowSize(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
						w.getPanel().init();
					}
				}
			}
			
		});
	}

	@Override
	public void init() {
		
		Dimension temp = resolutions.getPreferredSize();
		resolutions.setBounds(w.getHALF_W()-temp.width/2, (int)w.getH12()*2-temp.height/2, temp.width, temp.height);
		w.addComp(resolutions);
		
		temp = screens.getPreferredSize();
		screens.setBounds(w.getHALF_W()-temp.width/2, (int)w.getH12()-temp.height/2, temp.width, temp.height);
		w.addComp(screens);
		
		temp = fsCheck.getPreferredSize();
		fsCheck.setBounds(w.getHALF_W()-temp.width/2, w.getHALF_H()-temp.height/2, temp.width, temp.height);
		w.addComp(fsCheck);
		
		temp = fsCheck.getPreferredSize();
		play.setBounds(w.getHALF_W()-temp.width/2, (int)w.getH12()*10-temp.height/2, temp.width, temp.height);
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
	public void paint(Graphics2D g) {  }

	@Override
	public void update(double delta) {  }
	
}
