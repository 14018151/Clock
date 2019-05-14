package clock;

import java.awt.event.*;
import java.io.FileNotFoundException;
import javax.swing.Timer;

/**
 * 
 * @author Graham 
 */
public class Controller {
    
    ActionListener listener;
    Timer timer;
    
    Model model;
    View view;
    
    /**
     * Sets up the timer to update the clock every 100 milliseconds
     * @param m Setting up the model class/object
     * @param v Setting up the view class/object
     */
    public Controller(Model m, View v){
        model = m;
        view = v;
       
        
        listener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.update();
            }
        };
        
        timer = new Timer(100, listener);
        timer.start();
    }
}