package clock;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.Observer;
import java.util.Observable;



public class View implements Observer {
    
    ClockPanel panel;
    JButton button;
    
    public View(final Model model) {
        
        final JFrame frame = new JFrame();
        panel = new ClockPanel(model);
        //frame.setContentPane(panel);
        frame.setTitle("Java Clock");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Start of border layout code
        
        // I've just put a single button in each of the border positions:
        // PAGE_START (i.e. top), PAGE_END (bottom), LINE_START (left) and
        // LINE_END (right). You can omit any of these, or replace the button
        // with something else like a label or a menu bar. Or maybe you can
        // figure out how to pack more than one thing into one of those
        // positions. This is the very simplest border layout possible, just
        // to help you get started.
        
        Container pane = frame.getContentPane();
        
        button = new JButton("Show Head");
        pane.add(button, BorderLayout.PAGE_START);
         
        button.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) 
            {               
                model.nextAlarm();
            } 
        });
        
        panel.setPreferredSize(new Dimension(200, 200));
        pane.add(panel, BorderLayout.CENTER);
         
        button = new JButton("Add Alarm");
        pane.add(button, BorderLayout.LINE_START);
        
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
                alarmPanel.add(Box.createHorizontalStrut(15)); // a spacer
                alarmPanel.add(new JLabel("Minutes:"));
                alarmPanel.add(minutes);
                alarmPanel.add(Box.createHorizontalStrut(15)); // a spacer
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
                        String alarmInput = hours.getText() + ":" + minutes.getText() + ":" + seconds.getText();

                        model.addAlarm(alarmInput);
                    }
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Alarm must be formatted as 00:00:00", "Error", JOptionPane.WARNING_MESSAGE);
                }
                
               
            } 
        });
        
        button = new JButton("Remove Head");
        pane.add(button, BorderLayout.PAGE_END);
                 
        button.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) 
            {               
                model.removeHead();
            } 
        });
        
        
        button = new JButton("View Alarms");
        pane.add(button, BorderLayout.LINE_END);
        
        button.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) 
            {                 
                model.printQueue();
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
