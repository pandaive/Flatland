import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SpeedSlider extends JPanel implements ChangeListener {

	static final int VALUE_MIN = 0;
	static final int VALUE_MAX = 10;
	static final int STEP = 1;
	
	static final String ANIMATION_SPEED = "Animation speed";
	
	public SpeedSlider(int animSpeed) {
		super(new FlowLayout());
		initIntSlider(animSpeed, ANIMATION_SPEED);
		
		JButton saveParameters = new JButton("Start");
        saveParameters.setVerticalTextPosition(AbstractButton.CENTER);
        saveParameters.setHorizontalAlignment(AbstractButton.CENTER);
        add(saveParameters);
        saveParameters.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Main.start = true;
			}
		});
	}
	
	private void initIntSlider(int init, String param) {
		JSlider slider;
		int max = VALUE_MAX;
		slider = new JSlider(JSlider.HORIZONTAL, VALUE_MIN, max, init);
		slider.setName(param);
		slider.addChangeListener(this);
		slider.setMajorTickSpacing(50);
		slider.setMinorTickSpacing(10);
		slider.setPaintTicks(true);
		
		Hashtable<Integer, JLabel> labels = new Hashtable<Integer, JLabel>();
		for (int i = 0; i < VALUE_MAX; i++) {
			labels.put(i+1, new JLabel((i+1) + ""));
		}
		slider.setLabelTable(labels);
		slider.setPaintLabels(true);
		
		add(new JLabel(param));
        add(slider);
	}
	
	@Override
	public void stateChanged(ChangeEvent arg0) {
		JSlider source = (JSlider)arg0.getSource();
        if (!source.getValueIsAdjusting()) {
        	double value = (double)source.getValue();
            if (source.getName() == ANIMATION_SPEED)
            	Main.animationSpeed = (int) (value);
        }
	}

}
