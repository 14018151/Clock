package clock;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Observable;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Model extends Observable {
    Alarms alarm = new Alarms();
    
    int hour = 0;
    int minute = 0;
    int second = 0;
    
    int oldSecond = 0;
    
    public Model() {
        update();
    }
    
    public void update() {
        Calendar date = Calendar.getInstance();
        hour = date.get(Calendar.HOUR_OF_DAY);
        minute = date.get(Calendar.MINUTE);
        oldSecond = second;
        second = date.get(Calendar.SECOND);
        if (oldSecond != second) {
            setChanged();
            notifyObservers();
            
            String hoursString = Integer.toString(hour);
            String minutesString = Integer.toString(minute);
            String secondsString = Integer.toString(second);
            
            if (hoursString.length() < 2) {
                hoursString = "0" + hoursString;
            }
            if (minutesString.length() < 2) {
                minutesString = "0" + secondsString;
            }
            if (secondsString.length() < 2) {
                secondsString = "0" + secondsString;
            }
            
            String time = hoursString +":"+minutesString+":"+secondsString;
            
            if(!alarm.isEmpty()){
                if(alarm.head().equals(time)){
                    alarm.pop();
                    if(!alarm.isEmpty()){
                        JOptionPane.showMessageDialog(null, "It is "+time+". Next alarm is: "+alarm.head()+". Click view alarms to update next alarm display.","Alarm", JOptionPane.OK_OPTION);           
                    }else{
                        JOptionPane.showMessageDialog(null, "It is "+time+". There is no next alarm. Click view alarms to update next alarm display.","Alarm", JOptionPane.OK_OPTION);
                    }
                } 
            }
        }
    }
    
    String nextAlarm() {
        if(!alarm.isEmpty()){
            return alarm.head();
        }else{
            return "There are no alarms set";
        }
    }

    void addAlarm(String str) {
        alarm.add(str);
    }

    void removeHead() {
        alarm.pop();
    }
    
    String printQueue(){
        return alarm.toString();
    }
    
    boolean checkEmpty(){
        return alarm.isEmpty();
    }
    
    void removeAlarm(String str){
        alarm.remove(str);
    }
    
    void viewAlarms(){
        if (checkEmpty()) {
            JFrame viewFrame = new JFrame();
            JOptionPane.showMessageDialog(viewFrame, "There are no alarms set", "Empty", JOptionPane.OK_OPTION);
        } else {
            Object[] allAlarms  = alarm.items.toArray();
            
            Arrays.sort(allAlarms);            
            
            final JPanel viewPanel = new JPanel();
            
            int panelX = 210;
            int panelY = allAlarms.length * 35;
            
            if(allAlarms.length > 10){
                panelX = 500;
                panelY = allAlarms.length * 17;
            }
            
            
            viewPanel.setPreferredSize(new Dimension(panelX, panelY));
            
            for(int x = 0; x < allAlarms.length; x++){
                final JTextField alarmEdit = new JTextField(8);
                
                final String alarm = allAlarms[x].toString();
                
                alarmEdit.setText(alarm);
                viewPanel.add(alarmEdit);
                
                JButton editButton = new JButton("Edit");
                viewPanel.add(editButton);
                
                editButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        String newAlarm = alarmEdit.getText();
                        JFrame frame = new JFrame();
                        try{
                            if(newAlarm.substring(0, 4).equals("9999")){
                                if (!newAlarm.substring(4, 5).equals(":") || !newAlarm.substring(7, 8).equals(":")||!newAlarm.substring(10,11).equals(":")||newAlarm.length()!=13) {
                                    JOptionPane.showMessageDialog(frame, "Alarm must be formatted as 9999:00:00:00", "Error", JOptionPane.WARNING_MESSAGE);
                                }else{
                                    int hoursint = Integer.parseInt(newAlarm.substring(5, 7));
                                    int minutesint = Integer.parseInt(newAlarm.substring(8, 10));
                                    int secondsint = Integer.parseInt(newAlarm.substring(11, 13));

                                    if (hoursint > 23 || hoursint < 00) {
                                        JOptionPane.showMessageDialog(frame, "24 hours in a day", "Error", JOptionPane.WARNING_MESSAGE);
                                    } else if (minutesint > 59 || hoursint < 00) {
                                        JOptionPane.showMessageDialog(frame, "60 minutes in an hour", "Error", JOptionPane.WARNING_MESSAGE);
                                    } else if (secondsint > 59 || hoursint < 00) {
                                        JOptionPane.showMessageDialog(frame, "60 seconds in a minute", "Error", JOptionPane.WARNING_MESSAGE);
                                    } else {
                                        Calendar date = Calendar.getInstance();

                                        int currentHour = date.get(Calendar.HOUR_OF_DAY);
                                        int currentMinute = date.get(Calendar.MINUTE);
                                        int currentSecond = date.get(Calendar.SECOND);

                                        String hoursString = Integer.toString(hoursint);
                                        String minutesString = Integer.toString(minutesint);
                                        String secondsString = Integer.toString(secondsint);
                                        
                                        if (hoursString.length() < 2) {
                                            hoursString = "0" + hoursString;
                                        }
                                        if (minutesString.length() < 2) {
                                            minutesString = "0" + minutesString;
                                        }
                                        if (secondsString.length() < 2) {
                                            secondsString = "0" + secondsString;
                                        }
                                        
                                        if (hoursint < currentHour || (hoursint == currentHour && minutesint < currentMinute) || (minutesint == currentMinute && secondsint < currentSecond)) {
                                            JOptionPane.showMessageDialog(frame, "Alarm has been set for tomorrow", "Error", JOptionPane.WARNING_MESSAGE);
                                            newAlarm = 9999 + ":" + hoursString + ":" + minutesString + ":" + secondsString;
                                        } else {
                                            newAlarm = hoursString + ":" + minutesString + ":" + secondsString;
                                        }
                                        removeAlarm(alarm);
                                        addAlarm(newAlarm);
                                        }

                                    }
                            }
                            else{
                                if (!newAlarm.substring(2, 3).equals(":") || !newAlarm.substring(5, 6).equals(":") || newAlarm.length()!=8) {
                                    JOptionPane.showMessageDialog(frame, "Alarm must be formatted as 00:00:00", "Error", JOptionPane.WARNING_MESSAGE);
                                } else {
                                    int hoursint = Integer.parseInt(newAlarm.substring(0, 2));
                                    int minutesint = Integer.parseInt(newAlarm.substring(3, 5));
                                    int secondsint = Integer.parseInt(newAlarm.substring(6, 8));

                                    if (hoursint > 23 || hoursint < 00) {
                                        JOptionPane.showMessageDialog(frame, "24 hours in a day", "Error", JOptionPane.WARNING_MESSAGE);
                                    } else if (minutesint > 59 || hoursint < 00) {
                                        JOptionPane.showMessageDialog(frame, "60 minutes in an hour", "Error", JOptionPane.WARNING_MESSAGE);
                                    } else if (secondsint > 59 || hoursint < 00) {
                                        JOptionPane.showMessageDialog(frame, "60 seconds in a minute", "Error", JOptionPane.WARNING_MESSAGE);
                                    } else {
                                        Calendar date = Calendar.getInstance();

                                        int currentHour = date.get(Calendar.HOUR_OF_DAY);
                                        int currentMinute = date.get(Calendar.MINUTE);
                                        int currentSecond = date.get(Calendar.SECOND);

                                        String hoursString = Integer.toString(hoursint);
                                        String minutesString = Integer.toString(minutesint);
                                        String secondsString = Integer.toString(secondsint);

                                        if (hoursString.length() < 2) {
                                            hoursString = "0" + hoursString;
                                        }
                                        if (minutesString.length() < 2) {
                                            minutesString = "0" + minutesString;
                                        }
                                        if (secondsString.length() < 2) {
                                            secondsString = "0" + secondsString;
                                        }
                                        
                                        if (hoursint < currentHour || (hoursint == currentHour && minutesint < currentMinute) || (minutesint == currentMinute && secondsint < currentSecond)) {
                                            JOptionPane.showMessageDialog(null, "Alarm has been set for tomorrow", "Error", JOptionPane.WARNING_MESSAGE);
                                            newAlarm = 9999 + ":" + hoursString + ":" + minutesString + ":" + secondsString;
                                        } else {
                                            newAlarm = hoursString + ":" + minutesString + ":" + secondsString;
                                        }

                                        removeAlarm(alarm);
                                        addAlarm(newAlarm);
                                    }

                                }
                            }
                            
  
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Alarm must be formatted as 00:00:00", "Error", JOptionPane.WARNING_MESSAGE);
                        }
                        
                        Window win = SwingUtilities.getWindowAncestor(viewPanel);
                        win.dispose();
                    }
                });
                
                
                JButton delButton = new JButton("Delete");
                viewPanel.add(delButton);
                
                delButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        removeAlarm(alarm);
                        //https://stackoverflow.com/questions/26762324/swing-how-to-close-jpanel-programmatically
                        Window win = SwingUtilities.getWindowAncestor(viewPanel);
                        win.dispose();
                    }
                });
                
                if(allAlarms.length > 10){
                    viewPanel.add(Box.createHorizontalStrut(10));
                }
            }
             
            JOptionPane.showMessageDialog(null, viewPanel, "Alarms", JOptionPane.PLAIN_MESSAGE );
        }
    }
    
    public void load() throws FileNotFoundException {
        
        JFileChooser fileChooser = new JFileChooser("FileSystemView.getFileSystemView().getHomeDirectory()");

        // set a title for the dialog 
        fileChooser.setDialogTitle("Select a .txt file");

        // only allow files of .txt extension 
        FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only .txt files", "txt");
        fileChooser.addChoosableFileFilter(restrict);

        fileChooser.setAcceptAllFileFilterUsed(false);

        // Open the choose file dialog 
        int fileChosen = fileChooser.showOpenDialog(null);

        // if the user selects a file 
        if (fileChosen == JFileChooser.APPROVE_OPTION) {
            //https://www.geeksforgeeks.org/different-ways-reading-text-file-java/
            File file = fileChooser.getSelectedFile();

            FileReader alarmLoad = new FileReader(file);

            Scanner loading = new Scanner(alarmLoad);

            while (loading.hasNextLine()) {
                String item = loading.nextLine();

                Calendar date = Calendar.getInstance();

                int currentHour = date.get(Calendar.HOUR_OF_DAY);
                int currentMinute = date.get(Calendar.MINUTE);
                int currentSecond = date.get(Calendar.SECOND);

                int hoursint, minutesint, secondsint;
                String newAlarm;

                if (item.substring(0, 4).equals("9999")) {
                    if (item.substring(4, 5).equals(":") && item.substring(7, 8).equals(":") && item.substring(10, 11).equals(":") && item.length() == 13) {
                        hoursint = Integer.parseInt(item.substring(5, 7));
                        minutesint = Integer.parseInt(item.substring(8, 10));
                        secondsint = Integer.parseInt(item.substring(11, 13));

                        if (hoursint < currentHour || (hoursint == currentHour && minutesint < currentMinute) || (minutesint == currentMinute && secondsint < currentSecond)) {
                            newAlarm = item;
                        } else {
                            newAlarm = item.substring(5);
                        }
                        addAlarm(newAlarm);
                    }
                } else {
                    if (item.substring(2, 3).equals(":") && item.substring(5, 6).equals(":") && item.length() == 8) {
                        hoursint = Integer.parseInt(item.substring(0, 2));
                        minutesint = Integer.parseInt(item.substring(3, 5));
                        secondsint = Integer.parseInt(item.substring(6, 8));

                        if (hoursint < currentHour || (hoursint == currentHour && minutesint < currentMinute) || (minutesint == currentMinute && secondsint < currentSecond)) {
                            newAlarm = 9999 + item;
                        } else {
                            newAlarm = item;
                        }
                        addAlarm(newAlarm);
                    }
                }
            }
        }        
    }
    
    //https://www.geeksforgeeks.org/java-swing-jfilechooser/
    //https://www.geeksforgeeks.org/file-handling-java-using-filewriter-filereader/
    public void save() throws IOException{
        // JFileChooser points to the mentioned path 
        JFileChooser fileChooser = new JFileChooser("FileSystemView.getFileSystemView().getHomeDirectory()");

        // set a title for the dialog 
        fileChooser.setDialogTitle("Select a .txt file");

        // only allow files of .txt extension 
        FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only .txt files", "txt");
        fileChooser.addChoosableFileFilter(restrict);
        
        fileChooser.setAcceptAllFileFilterUsed(false); 
        
        // Open the save dialog 
        int fileChosen = fileChooser.showSaveDialog(null);
        
        if (fileChosen == JFileChooser.APPROVE_OPTION) {

            File file = fileChooser.getSelectedFile();
            
            if(!file.toString().substring((file.toString().length()-4)).equals(".txt")){
                FileWriter alarmSave = new FileWriter(file+".txt");
                alarmSave.write(alarm.toString());

                alarmSave.close();
            }else{
                FileWriter alarmSave = new FileWriter(file);
                alarmSave.write(alarm.toString());

                alarmSave.close();
            }
        }
    }
}
