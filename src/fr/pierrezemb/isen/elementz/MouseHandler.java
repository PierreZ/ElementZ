package fr.pierrezemb.isen.elementz;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by pierrezemb on 17/02/15.
 */
public class MouseHandler extends MouseAdapter {

    // String to replace classical state
    public static String CLASSIC_STATE = "boule";
    public static String OVER_STATE="boule_o";
    public static String SELECTED_STATE = "boule_s";


    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);

        if (!Main_Window.gameOn){
            return;
        }

        if (!Main_Window.isWorking) {

            int x, y;

            x = ((JLabel) e.getSource()).getX();
            y = ((JLabel) e.getSource()).getY();

            if (x != 0) {
                x = x / Main_Window.IMAGE_SIZE;
            }
            if (y != 0) {
                y = y / Main_Window.IMAGE_SIZE;
            }

            if ((Main_Window.last_click_x != x ||
                    Main_Window.last_click_y != y) &&
                    Main_Window.isFirstClick) {


                Main_Window.model.permut(Main_Window.last_click_y, Main_Window.last_click_x, y, x);
                Main_Window.last_click_x = -1;
                Main_Window.last_click_y = -1;
                Main_Window.isFirstClick = !Main_Window.isFirstClick;
            } else {
                Main_Window.isFirstClick = !Main_Window.isFirstClick;
                Main_Window.last_click_x = x;
                Main_Window.last_click_y = y;

                String path = ((JLabel) e.getSource()).getIcon().toString();


                if (path.contains(SELECTED_STATE)) {
                    path = path.replace(SELECTED_STATE, CLASSIC_STATE);
                    ((JLabel) e.getSource()).setIcon(new ImageIcon(path));
                    Main_Window.last_click_x = -1;
                    Main_Window.last_click_y = -1;
                    return;
                }
                if (path.contains(OVER_STATE)) {
                    path = path.replace(OVER_STATE, SELECTED_STATE);
                    ((JLabel) e.getSource()).setIcon(new ImageIcon(path));
                    return;
                }
                // classic state as default
                path = path.replace(CLASSIC_STATE, SELECTED_STATE);
                ((JLabel) e.getSource()).setIcon(new ImageIcon(path));
            }

        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        //System.out.println("Released by"+((JLabel)e.getSource()).getX()+":"+((JLabel)e.getSource()).getY());
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e);
        if (!Main_Window.gameOn){
            return;
        }
        String path = ((JLabel)e.getSource()).getIcon().toString();

        // Si the ball is already selected, don't do anything
        if (!path.contains( SELECTED_STATE) && !path.contains( OVER_STATE)){
            path = path.replace(CLASSIC_STATE, OVER_STATE);
            ((JLabel)e.getSource()).setIcon(new ImageIcon(path));
        }


    }

    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e);
        if (!Main_Window.gameOn){
            return;
        }
        String path = ((JLabel)e.getSource()).getIcon().toString();
        path = path.replace(OVER_STATE, CLASSIC_STATE);
        ((JLabel) e.getSource()).setIcon(new ImageIcon(path));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
    }
}
