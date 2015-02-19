package com.bouncer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JApplet;


public class MonApplet extends JApplet {
    private Plateau plateau;
    private Image image;
    @Override
    public void init() {
        this.resize(500, 400);
        image = createImage(500,400);
        plateau = new Plateau(this, image);
        plateau.addListeners(this);
    }
    @Override
    public void paint(Graphics g) {
        plateau.paint();
        g.drawImage(image,0,0,this);
    }
}
