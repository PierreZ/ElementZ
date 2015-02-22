package fr.pierrezemb.isen.elementz;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by pierrezemb on 17/02/15.
 */
public class MouseHandler extends MouseAdapter {


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


                if (path.contains(Main_Window.SELECTED_STATE)) {
                    ((JLabel)e.getSource()).setIcon(changeSprite(path, Main_Window.CLASSIC_STATE));
                    Main_Window.last_click_x = -1;
                    Main_Window.last_click_y = -1;
                    return;
                }
                if (path.contains(Main_Window.OVER_STATE)) {
                    ((JLabel)e.getSource()).setIcon(changeSprite(path, Main_Window.SELECTED_STATE));
                    return;
                }
                // classic state as default
                ((JLabel)e.getSource()).setIcon(changeSprite(path, Main_Window.SELECTED_STATE));
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
        if (!path.contains( Main_Window.SELECTED_STATE) && !path.contains( Main_Window.OVER_STATE)){
            ((JLabel)e.getSource()).setIcon(changeSprite(path, Main_Window.OVER_STATE));

        }


    }

    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e);
        if (!Main_Window.gameOn){
            return;
        }

        String path = ((JLabel)e.getSource()).getIcon().toString();
        if (!path.contains( Main_Window.SELECTED_STATE)){
            ((JLabel)e.getSource()).setIcon(changeSprite(path, Main_Window.CLASSIC_STATE));
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        super.mouseDragged(e);
    }


public ImageIcon changeSprite(String path, String wanted){
    String ball_color = path.substring(path.length() - Main_Window.EXAMPLE.length());
    return new ImageIcon(Main_Window.class.getClassLoader().getResource(Main_Window.IMAGE_ROOT+wanted+ball_color));
}

}