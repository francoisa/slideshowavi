package org.jmf.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

/** class FormListeners.
*/
public class FormListeners extends FileFilter {
    private static final String newline = "\n";
    private JTextArea output;
    private JTextField source;
    private JTextField dest;
    private final static String jpeg = "jpeg";
    private final static String jpg = "jpg";
    private final static String gif = "gif";
    private final static String tiff = "tiff";
    private final static String tif = "tif";
    private final static String png = "png";

    /*
     * Get the extension of a file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }
   public FormListeners(JTextArea o, JTextField s, JTextField d, JSlider spi) {
      output = o;
      source = s;
      dest = d;
   }

    public String getDescription() {
        return "Images";
    }

    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = getExtension(f);
        if (extension != null) {
            if (extension.equals(tiff) ||
                extension.equals(tif) ||
                extension.equals(gif) ||
                extension.equals(jpeg) ||
                extension.equals(jpg) ||
                extension.equals(png)) {
                    return true;
            } else {
                return false;
            }
        }

        return false;
   }

   public void sourceHandler() {
      final JFileChooser fc = new JFileChooser();
      fc.setFileFilter(this);
      int returnVal = fc.showOpenDialog(source.getTopLevelAncestor());
      if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            source.setText(file.getAbsolutePath());
            //This is where a real application would open the file.
           logStatus("Opening: '" + source.getText() + "'");
           Preferences prefs = Preferences.userRoot().node(SlideshowAVI.nodeName);
	 	prefs.put(SlideshowAVI.SRC, source.getText());
        }
   }

   public void addSourceHandler(JButton sb) {
      sb.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent actionEvent) {
   			sourceHandler();
    		}
      });
   }

   public void destHandler() {
      final JFileChooser fc = new JFileChooser();
      fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      int returnVal = fc.showOpenDialog(dest.getTopLevelAncestor());
      if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            dest.setText(file.getAbsolutePath());
            //This is where a real application would open the file.
           logStatus("Destination: '" + dest.getText() + "'");
           Preferences prefs = Preferences.userRoot().node(SlideshowAVI.nodeName);
	 	prefs.put(SlideshowAVI.DEST, dest.getText());
        }
   }

   public void addDestHandler(JButton db) {
      db.addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent actionEvent) {
   			destHandler();
    		}
      });
   }

   public void logStatus(String s) {
        output.append(s + newline);
        output.setCaretPosition(output.getDocument().getLength());
    }
}