package org.jmf.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public final class GoListener implements ActionListener {
   private static final String newline = "\n";
   private JTextArea output;
   private JTextField source;
   private JTextField dest;
   private JSlider spiSlider;
   private JButton sourceButton;
   private JButton destButton;
   private JButton goButton;
  	
   public GoListener(JTextArea o, JTextField s, JTextField d, JSlider spi, 
		   			 JButton sourceBtn, JButton destBtn, JButton goBtn) {
      output = o;
      source = s;
      dest = d;
      spiSlider = spi;
      sourceButton = sourceBtn;
      destButton = destBtn;
      goButton = goBtn;
   }

   private static boolean isDirEmpty(final Path directory) {
   	boolean isEmpty = false;
	try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory)) {
		isEmpty = !dirStream.iterator().hasNext();
	}
	catch (IOException x) {
    		// IOException can never be thrown by the iteration.
    		// In this snippet, it can // only be thrown by newDirectoryStream.
    		System.err.println(x);
	}
	return isEmpty;
   }
   
   public void actionPerformed(ActionEvent actionEvent) {
      StringBuilder sb = new StringBuilder();
      if (source.getText().isEmpty()) {
         sb.append("Source file missing." + newline);
      }
      if (dest.getText().isEmpty()) {
         sb.append("Destination directory missing." + newline);
      }
      else {
         Path destPath = Paths.get(dest.getText());
         if (!isDirEmpty(destPath)) {
            sb.append("The destination directory '" + dest.getText() + "' is not empty.");
         }
      }
      if (sb.length() > 0) {
      	JOptionPane.showMessageDialog(source,
   			sb.toString(),
   			"Error",
   			JOptionPane.ERROR_MESSAGE);
      }
      else {
    	  SlideShowMaker ssm = new SlideShowMaker(output, source.getText(), dest.getText(), spiSlider.getValue(),
    			  sourceButton, destButton, goButton);
    	  ssm.execute();
      }
   }
}