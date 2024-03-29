package clock;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import javax.swing.*;
import java.util.Observer;
import java.util.Observable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Has functionality but also some pointers to model class
 * @author 14018151 Joseph Kelly
 */
public class View implements Observer {
    
    ClockPanel panel;
    JButton button;
    
    /**
     * Sets up the view class linking to model and handles all the buttons
     * @param model Sets up a model object to be linked and referenced throughout
     */
    public View(final Model model) {
        final JFrame frame = new JFrame();
        panel = new ClockPanel(model);
        final JButton nextButton = new JButton("Next Alarm: " + model.nextAlarm());
        
        //frame.setContentPane(panel);
        frame.setTitle("Java Clock");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            /**
             * Asks to save a file on close
             */
            public void windowClosing(WindowEvent e) {
                /* Code used from:
                    Stack Overflow. (2011). JOptionPane YES/No Options Confirm Dialog Box Issue. [online] 
                    Available at: https://stackoverflow.com/questions/8689122/joptionpane-yes-no-options-confirm-dialog-box-issue [Accessed 13 May 2019].
                */
                int saveButton = JOptionPane.YES_NO_OPTION;
                
                /*
                    Docs.oracle.com. (n.d.). How to Make Dialogs (The Java™ Tutorials > Creating a GUI With JFC/Swing > Using Swing Components). [online] 
                    Available at: https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html [Accessed 13 May 2019].
                */
                final int optionPane = JOptionPane.showConfirmDialog (null, "Would you like to save your alarms before closing?","Save",saveButton);
                
                if(optionPane==JOptionPane.YES_OPTION){
                    try {
                        model.save();
                    } catch (IOException ex) {
                        Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }                
            }
            /**
             * Asks to load a saved file on open
             */
            public void windowOpened(WindowEvent e){
                int loadButton = JOptionPane.YES_NO_OPTION;

                final int optionPane = JOptionPane.showConfirmDialog(null, "Would you like to load alarms before opening?", "Save", loadButton);

                if (optionPane == JOptionPane.YES_OPTION) {
                    try {
                        model.load();
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(View.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    nextButton.setText("Next Alarm: " + model.nextAlarm());
                }
            }
        });
        
        // Start of border layout code        
        Container pane = frame.getContentPane();
        
        if(!model.checkEmpty()){
            nextButton.setText(nextButton.getText()+". Click to remove");
        }
        
        //Sets up the button that displays and removes the head of the queue
        pane.add(nextButton, BorderLayout.NORTH);
                 
        /**
         * Button to remove the head of the queue and update the display
         */
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
         
        
        //Sets up button to add alarms
        button = new JButton("Add Alarm");
        pane.add(button, BorderLayout.WEST);
        
        
        /* Code used from: 
            Docs.oracle.com. (n.d.). How to Make Dialogs (The Java™ Tutorials > Creating a GUI With JFC/Swing > Using Swing Components). [online] 
            Available at: https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html [Accessed 19 Apr. 2019].
        */
        /**
         * Displays a pop-up letting users input hours, minutes, and seconds of the alarm
         * Validates their entries and if all goes well adds the alarm to the queue
         */ 
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
                                
                //Validates the user's input for the alarm
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
                        
                        //Adds 9999 to start of alarm to show that it's due for tomorrow
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
        
        //Sets up and adds button to display the alarms.
        button = new JButton("View Alarms");
        pane.add(button, BorderLayout.EAST);
        
        /**
         * Button to display all the alarms set
         */
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
    
    /**
     * Repaints the clock from ClockPanel
     * @param o Observing model so that every time it updates (every 100 milliseconds) it repaints the clock
     */
    public void update(Observable o, Object arg) {
        panel.repaint();
    }
}
