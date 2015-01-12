package org.jmf.utils;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/** class MenuListeners.
*/
public final class MenuListeners {
   private Container frame;

   /** Constructor. */
   public MenuListeners(JTextArea o) {
   }

   public void addExitHandler(JMenuItem exit) {
      exit.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent actionEvent) {
      		System.exit(0);
    		}
      });
   }

   public void addAboutHandler(JMenuItem about) {
      frame = about.getTopLevelAncestor();
      about.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent actionEvent) {
      		JOptionPane.showMessageDialog(frame,
   				"Convert a sequential list of jpegs into an avi..",
   				"About",
   				JOptionPane.INFORMATION_MESSAGE);
    		}
    	});
   }
}