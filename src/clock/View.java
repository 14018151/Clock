package clock;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.util.Observer;
import java.util.Observable;
import java.util.Random;


public class View implements Observer {
    
    ClockPanel panel;
    JButton button;
    
    public View(Model model) {
        final Alarms alarm = new Alarms();
        
        JFrame frame = new JFrame();
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
                System.out.println(alarm.head());
            } 
        });
        
        panel.setPreferredSize(new Dimension(200, 200));
        pane.add(panel, BorderLayout.CENTER);
         
        button = new JButton("Add Alarm");
        pane.add(button, BorderLayout.LINE_START);
        
        //https://www.geeksforgeeks.org/jradiobutton-java-swing/
        // Adding Listener to JButton for adding alarms. 
        button.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) 
            {               
                Random r = new Random();
                String str = "";
                
                str+= r.nextInt(24) + ":";
                str+= r.nextInt(60) + ":";
                str+= r.nextInt(60);
                
                alarm.add(str);
            } 
        });
        
        button = new JButton("Remove Head");
        pane.add(button, BorderLayout.PAGE_END);
                 
        button.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) 
            {               
                alarm.pop();
            } 
        });
        
        
        button = new JButton("View Alarms");
        pane.add(button, BorderLayout.LINE_END);
        
        button.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) 
            {                 
                System.out.println(alarm.toString());
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
