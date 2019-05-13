package clock;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Calendar;
import javax.swing.*;
import java.util.Observer;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;



public class View implements Observer {
    
    ClockPanel panel;
    JButton button;
    
    public View(final Model model) {
        final JFrame frame = new JFrame();
        panel = new ClockPanel(model);
        //frame.setContentPane(panel);
        frame.setTitle("Java Clock");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(WindowEvent winEvt) {
                //https://stackoverflow.com/questions/8689122/joptionpane-yes-no-options-confirm-dialog-box-issue
                int saveButton = JOptionPane.YES_NO_OPTION;
                

                //https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
                final int optionPane = JOptionPane.showConfirmDialog (null, "Would you like to save your alarms before closing?","Save",saveButton);
                
                if(optionPane==JOptionPane.YES_OPTION){
                    try {
                        model.save();
                    } catch (IOException ex) {
                        Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }                
            }
        });
        
        // Start of border layout code        
        Container pane = frame.getContentPane();
        
        final JButton nextButton = new JButton("Next Alarm: " + model.nextAlarm());
        
        if(!model.checkEmpty()){
            nextButton.setText(nextButton.getText()+". Click to remove");
        }
        
        pane.add(nextButton, BorderLayout.NORTH);
                 
        nextButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) 
            {
                model.removeHead();
                
                nextButton.setText("Next Alarm: " + model.nextAlarm());
                        
                if (!model.checkEmpty()) {
                    nextButton.setText(nextButton.getText() + ". Click to remove");
                }
            }
                
             
        });
        
        panel.setPreferredSize(new Dimension(200, 200));
        pane.add(panel, BorderLayout.CENTER);
         
        button = new JButton("Add Alarm");
        pane.add(button, BorderLayout.WEST);
        
        //https://www.geeksforgeeks.org/jradiobutton-java-swing/
        //https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html
        // Adding Listener to JButton for adding alarms. 
        button.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) 
            {                
                JTextField hours = new JTextField(2);
                JTextField minutes = new JTextField(2);
                JTextField seconds = new JTextField(2);
                
                JPanel alarmPanel = new JPanel();
                alarmPanel.add(new JLabel("Hour:"));
                alarmPanel.add(hours);
                alarmPanel.add(Box.createHorizontalStrut(10)); // a spacer
                alarmPanel.add(new JLabel("Minutes:"));
                alarmPanel.add(minutes);
                alarmPanel.add(Box.createHorizontalStrut(10)); // a spacer
                alarmPanel.add(new JLabel("Seconds:"));
                alarmPanel.add(seconds);

                int result = JOptionPane.showConfirmDialog(null, alarmPanel, "Enter an alarm time", JOptionPane.OK_CANCEL_OPTION);
                                
                try {
                    int hoursint = Integer.parseInt(hours.getText());
                    int minutesint = Integer.parseInt(minutes.getText());
                    int secondsint = Integer.parseInt(seconds.getText());
                    
                    if(hoursint > 23 || hoursint < 00){
                        JOptionPane.showMessageDialog(frame, "24 hours in a day", "Error", JOptionPane.WARNING_MESSAGE);
                    }else if(minutesint > 59 || hoursint < 00){
                        JOptionPane.showMessageDialog(frame, "60 minutes in an hour", "Error", JOptionPane.WARNING_MESSAGE);
                    }else if(secondsint > 59 || hoursint < 00){
                        JOptionPane.showMessageDialog(frame, "60 seconds in a minute", "Error", JOptionPane.WARNING_MESSAGE);
                    }else{
                        Calendar date = Calendar.getInstance();
                        
                        int currentHour = date.get(Calendar.HOUR_OF_DAY);
                        int currentMinute = date.get(Calendar.MINUTE);
                        int currentSecond = date.get(Calendar.SECOND);
                        
                        hoursint = Integer.parseInt(hours.getText());
                        minutesint = Integer.parseInt(minutes.getText());
                        secondsint = Integer.parseInt(seconds.getText());
                        
                        String hoursString = Integer.toString(hoursint); 
                        String minutesString = Integer.toString(minutesint);
                        String secondsString = Integer.toString(secondsint);
                        
                        if(hoursString.length()<2){
                            hoursString = "0"+hoursString;
                        }
                        if(minutesString.length()<2){
                            minutesString = "0"+minutesString;
                        }
                        if(secondsString.length()<2){
                            secondsString = "0"+secondsString;
                        }      
                        
                        String alarmInput = "";
                        
                        if(hoursint<currentHour || (hoursint==currentHour && minutesint < currentMinute) || (minutesint==currentMinute && secondsint<currentSecond)){
                            JOptionPane.showMessageDialog(frame, "Alarm has been set for tomorrow", "Error", JOptionPane.WARNING_MESSAGE);
                            alarmInput = 9999+":" +hoursString+":"+minutesString+":"+secondsString;
                        }else{
                            alarmInput = hoursString+":"+minutesString+":"+secondsString;     
                        }
                        
                        model.addAlarm(alarmInput);
                        
                        nextButton.setText("Next Alarm: " + model.nextAlarm());
                        
                        
                    }
                    if (!model.checkEmpty()) {
                        nextButton.setText("Next Alarm: " + model.nextAlarm() + ". Click to remove");
                    }else{
                        nextButton.setText("Next Alarm: There are no alarms set.");
                    }
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Alarm must be formatted as 00:00:00", "Error", JOptionPane.WARNING_MESSAGE);
                }
            } 
        });
        
        
        button = new JButton("View Alarms");
        pane.add(button, BorderLayout.EAST);
        
        button.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) 
            {                 
                model.viewAlarms();  
                if (!model.checkEmpty()) {
                    nextButton.setText("Next Alarm: " + model.nextAlarm() + ". Click to remove");
                } else {
                    nextButton.setText("Next Alarm: There are no alarms set");
                }
            } 
        });

        // End of borderlayout code
        frame.pack();
        frame.setVisible(true);
    }
    
    public void update(Observable o, Object arg) {
        panel.repaint();
    }
}
