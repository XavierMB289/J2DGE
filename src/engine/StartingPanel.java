package engine;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import backends.AppPage;

public class StartingPanel extends AppPage{

	private static final long serialVersionUID = 839804302267614473L;
	
	JComboBox<String> resolutions;
	String res;
	JCheckBox fsCheck;
	JButton play;

	public StartingPanel(Window win) {
		super(win);
	}
	
	@SuppressWarnings("static-access")
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
		
		fsCheck = new JCheckBox("Fullscreen?");
		fsCheck.setToolTipText("Overrides the resolution...");
		fsCheck.setMnemonic('F');
		
		play = new JButton("PLAY");
		play.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if(fsCheck.isSelected()) {
					w.fullscreenExclusive(0);
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
		resolutions.setBounds(w.HALF_W-temp.width/2, (int)w.H12*2-temp.height/2, temp.width, temp.height);
		w.addComp(resolutions);
		
		temp = fsCheck.getPreferredSize();
		fsCheck.setBounds(w.HALF_W-temp.width/2, w.HALF_H-temp.height/2, temp.width, temp.height);
		w.addComp(fsCheck);
		
		temp = fsCheck.getPreferredSize();
		play.setBounds(w.HALF_W-temp.width/2, (int)w.H12*10-temp.height/2, temp.width, temp.height);
		w.addComp(play);
	}

	@Override
	public void onChange() {
		w.removeComp(resolutions);
		w.removeComp(fsCheck);
		w.removeComp(play);
	}

	@Override
	public void paint(Graphics2D g) {  }

	@Override
	public void update(double delta) {  }
	
}
