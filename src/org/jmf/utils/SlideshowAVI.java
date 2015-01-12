package org.jmf.utils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.prefs.Preferences;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class SlideshowAVI {
	public static final String nodeName = "/org/jmf/utils";
   	public static final String SRC = "source";
   	public static final String DEST = "dest";
   	private static JButton sourceButton;
   	private static JButton destButton;
   	private static JTextField sourceField;
   	private static JTextField destField;
   	private static JSlider spiSlider;

   	private static JTextArea output;
   	private static JButton goButton;
   	private static JLabel status;

   	 private static void addComponentsToMenu(JMenuBar menubar) {
   	    MenuListeners ml = new MenuListeners(output);
   	    JMenu fileMenu = new JMenu("File");
   	    menubar.add(fileMenu);
   	    JMenuItem exit = new JMenuItem("Exit");
   	    exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, ActionEvent.ALT_MASK));
   	    ml.addExitHandler(exit);
   	    fileMenu.add(exit);

   	    JMenu helpMenu = new JMenu("Help");
   	    menubar.add(helpMenu);
   	    JMenuItem  about = new JMenuItem("About");
   	    ml.addAboutHandler(about);
   	    helpMenu.add(about);
   	 }

   	 private static void addComponentsToForm(JPanel formPanel) {
	 	final Preferences prefs = Preferences.userRoot().node(nodeName);
	 	final String sourceText = prefs.get(SRC, "");
	 	final String destText = prefs.get(DEST, "");
	 	sourceButton = new JButton("Source ,,,");
	 	destButton = new JButton("Dest      ,,,");
	 	final int min = 1, max = 5;
	 	spiSlider = new JSlider(JSlider.HORIZONTAL, min, max, min);
	 	spiSlider.setMajorTickSpacing(2);
	 	spiSlider.setMinorTickSpacing(1);
	 	spiSlider.setPaintTicks(true);
	 	spiSlider.setPaintLabels(true);

	 	sourceField = new JTextField(sourceText);
	 	sourceField.setEditable(false);
	 	sourceField.setOpaque(false);
	 	destField = new JTextField(destText);
	 	destField.setEditable(false);
	 	destField.setOpaque(false);
	 	GridBagConstraints gBC = new GridBagConstraints();

	 	// source file row
	 	gBC.gridx = 0;
	 	gBC.gridy = 0;
	 	gBC.fill = GridBagConstraints.NONE;
	 	gBC.anchor = GridBagConstraints.LINE_START;
	 	formPanel.add(new JLabel("Source: "), gBC);
	 	gBC.gridx = 1;
	 	gBC.weightx = 1;
	 	gBC.ipadx = 300;
	 	gBC.fill = GridBagConstraints.HORIZONTAL;
	 	gBC.anchor = GridBagConstraints.CENTER;
	 	formPanel.add(sourceField, gBC);
	 	gBC.gridx = 4;
	 	gBC.weightx = 0;
	 	gBC.ipadx = 0;
	 	gBC.fill = GridBagConstraints.NONE;
	 	gBC.anchor = GridBagConstraints.LINE_END;
	 	formPanel.add(sourceButton, gBC);

		// destination directory row
	 	gBC.gridx = 0;
	 	gBC.gridy = 1;
	 	gBC.fill = GridBagConstraints.NONE;
	 	gBC.anchor = GridBagConstraints.LINE_START;
	 	formPanel.add(new JLabel("Dest  : "), gBC);
	 	gBC.gridx = 1;
	 	gBC.weightx = 1;
	 	gBC.ipadx = 300;
	 	gBC.fill = GridBagConstraints.HORIZONTAL;
	 	gBC.anchor = GridBagConstraints.CENTER;
	 	formPanel.add(destField, gBC);
	 	gBC.gridx = 4;
	 	gBC.weightx = 0;
	 	gBC.ipadx = 0;
	 	gBC.fill = GridBagConstraints.NONE;
	 	gBC.anchor = GridBagConstraints.LINE_END;
	 	formPanel.add(destButton, gBC);

		// seconds per image row
	 	gBC.gridx = 0;
	 	gBC.gridy = 2;
	 	gBC.fill = GridBagConstraints.NONE;
	 	gBC.anchor = GridBagConstraints.LINE_START;
	 	formPanel.add(new JLabel("Secs/Image: "), gBC);
	 	gBC.gridx = 1;
	 	gBC.weightx = 0;
	 	gBC.ipadx = 3;
	 	gBC.fill = GridBagConstraints.NONE;
	 	gBC.anchor = GridBagConstraints.LINE_START;
	 	formPanel.add(spiSlider, gBC);

	 	FormListeners fl = new FormListeners(output, sourceField, destField, spiSlider);
	 	fl.addSourceHandler(sourceButton);
	 	fl.addDestHandler(destButton);
   	 }

       private static void addComponentsToPane(Container mainPane) {
	 	mainPane.setLayout(new BorderLayout());

	 	JPanel topPanel = new JPanel();
	 	topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
	 	mainPane.add(topPanel, BorderLayout.NORTH);

	 	// the source and destination form right below.
	 	JPanel formPanel = new JPanel(new GridBagLayout());
	 	addComponentsToForm(formPanel);
	 	topPanel.add(formPanel);


	 	JLabel outputLabel = new JLabel("Output");
	 	outputLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
	 	topPanel.add(outputLabel);

	 	output.setRows(6);
	 	output.setEditable(false);
	 	output.setOpaque(false);
	 	JScrollPane scrollPane = new JScrollPane(output);
		scrollPane.setBounds(0, 0, 600, 400);
	 	mainPane.add(scrollPane, BorderLayout.CENTER);

	 	JPanel bottomPanel = new JPanel();
	 	bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
	 	JPanel goButtonPanel = new JPanel();
	 	goButtonPanel.setLayout(new BorderLayout());
	 	goButton = new JButton("Go");
	 	goButtonPanel.add(goButton, BorderLayout.EAST);
	 	bottomPanel.add(goButtonPanel);
	 	goButton.addActionListener(new GoListener(output, sourceField, destField, spiSlider,
	 			sourceButton, destButton, goButton));

	 	status = new JLabel("Ready");
	 	status.setAlignmentX(Component.LEFT_ALIGNMENT);
	 	status.setHorizontalAlignment(SwingConstants.LEFT);
	 	JPanel statusPanel = new JPanel(new BorderLayout());
	 	statusPanel.add(status, BorderLayout.WEST);
	 	bottomPanel.add(statusPanel);
	 	mainPane.add(bottomPanel, BorderLayout.SOUTH);
       }

   private static void createAndShowGUI() {
	JFrame frame = new JFrame("Slideshow AVI");

	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setSize(400, 600);
	frame.setLocation(100, 30);
	output = new JTextArea();

       // menu bar at the top of the page
 	JMenuBar menubar = new JMenuBar();
	frame.setJMenuBar(menubar);
 	addComponentsToMenu(menubar);
	addComponentsToPane(frame.getContentPane());

	frame.pack();

	frame.setVisible(true);
   }

   public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });

  }


}