package org.jmf.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import javax.swing.*;

public final class SlideShowMaker extends SwingWorker<Void, String> {
   private JTextArea output;
   private Path sourceFile;
   private Path destDir;
   private int interval;
   private JButton goBtn, srcBtn, destBtn;

   public SlideShowMaker(JTextArea o, String source, String dest, int i, 
		   				 JButton gBtn, JButton sBtn, JButton dBtn) {
      output = o;
      sourceFile = Paths.get(source);
      destDir = Paths.get(dest);
      interval = i*60;
      goBtn = gBtn;
      srcBtn = sBtn;
      destBtn = dBtn;
   }

   @Override
   public Void doInBackground() {
	   goBtn.setEnabled(false);
	   srcBtn.setEnabled(false);
	   destBtn.setEnabled(false);
   	   makeSlides();
   	   return null;
   }
   
   public void makeSlides() {
       copyFiles();
       createDuplicates();
   }

   private static NumberFormat FIVEZEROS = new DecimalFormat("00000");; 
   private String rename(String ext, String name, Integer i) {
	   StringBuilder sb = new StringBuilder(name);
	   int end = sb.length();
	   for (int start = end - ext.length() - 1; start != 0; --start) {
		   char c = sb.charAt(start);
		   if (!Character.isDigit(c)) {
			   if (start == end) {
				   throw new RuntimeException(name + " does not end in an integer!");
			   }
			   sb.delete(start+1, end);
			   sb.append(FIVEZEROS.format(i)).append(ext);
			   break;
		   }
		   else if (start == 0) {
			   throw new RuntimeException(name + " Only contains integers!");
		   }
	   }
	   return sb.toString();
   }
   
   private void copyFiles() {
	   String ext = sourceFile.getFileName().toString();
	   int dotPos = ext.lastIndexOf('.');
	   ext = ext.substring(dotPos, ext.length());
       Path sourceDir = sourceFile.getParent();
       try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(sourceDir)) {
    	   boolean isFirst = false;
    	   int i = 0;
           for (Path file: dirStream) {
               if (file.equals(sourceFile)) {
            	   isFirst = true;
                }
                if (isFirst && file.getFileName().toString().endsWith(ext)) {
             	   String newName = rename(ext, file.getFileName().toString(), i*interval);
             	   String newPath = destDir.toString() + File.separator + newName; 
             	   publish(file.getFileName().toString() + " - " +  newPath);
                   Files.copy(file,  Paths.get(newPath));
                   ++i;
                }                    
           }
       }
       catch (IOException x) {
           // IOException can never be thrown by the iteration.
           // In this snippet, it can // only be thrown by newDirectoryStream.
           System.err.println(x);
       }
    }

   private void createDuplicates() {
	   String ext = sourceFile.getFileName().toString();
	   int dotPos = ext.lastIndexOf('.');
	   ext = ext.substring(dotPos, ext.length());
       try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(destDir)) {
    	   int newFile = 1;
    	   int imageFile = 1;
           for (Path file: dirStream) {
        	   while (newFile != imageFile*interval) {
	         	   String newName = rename(ext, file.getFileName().toString(), newFile);
	         	   String newPath = destDir.toString() + File.separator + newName; 
	               publish(file.getFileName().toString() + " - " +  newPath);
	               if (!file.equals(newPath)) Files.copy(file,  Paths.get(newPath));
	               ++newFile;
        	   }
        	   ++imageFile;
           }
       }
       catch (IOException x) {
           // IOException can never be thrown by the iteration.
           // In this snippet, it can // only be thrown by newDirectoryStream.
           System.err.println(x);
       }
   }

   @Override
   protected void process(List<String> msgs) {
	   for (String msg : msgs) {
		   output.append(msg + '\n');
	   }
       output.setCaretPosition(output.getDocument().getLength());
   }
   
   protected void done() {
	   goBtn.setEnabled(true);
	   srcBtn.setEnabled(true);
	   destBtn.setEnabled(true);	   
   }
}