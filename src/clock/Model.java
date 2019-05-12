package clock;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Observable;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
                    JOptionPane.showMessageDialog(null, "It is "+time,"Alarm", JOptionPane.OK_OPTION);
                    
                    alarm.pop();
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
    
    void viewAlarms(){
        if (checkEmpty()) {
            JFrame viewFrame = new JFrame();
            JOptionPane.showMessageDialog(viewFrame, "There are no alarms set", "Empty", JOptionPane.OK_OPTION);
        } else {
            Object[] allAlarms  = alarm.items.toArray();
            
            Arrays.sort(allAlarms);            
            
            JPanel viewPanel = new JPanel();
            
            int panelX = 210;
            int panelY = allAlarms.length * 35;
            
            if(allAlarms.length > 10){
                panelX = 500;
                panelY = allAlarms.length * 17;
            }
            
            
            viewPanel.setPreferredSize(new Dimension(panelX, panelY));
            
            for(int x = 0; x < allAlarms.length; x++){
                JTextField alarmEdit = new JTextField(8);
                
                alarmEdit.setText(allAlarms[x].toString());
                viewPanel.add(alarmEdit);
                
                JButton editButton = new JButton("Edit");
                viewPanel.add(editButton);
                
                JButton delButton = new JButton("Delete");
                viewPanel.add(delButton);
                
                if(allAlarms.length > 10){
                    viewPanel.add(Box.createHorizontalStrut(10));
                }
            }
             
            JOptionPane.showMessageDialog(null, viewPanel, "Alarms", JOptionPane.PLAIN_MESSAGE );
        }
    }
    
}
