package com.bouncer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JApplet;


/*
 * and open the template in the editor.
 */

/**
 *
 * @author bounceur
 */
public class Plateau {
    private Image image;
    private Graphics g;
    private Objet_Move objet1;
    private Objet_Move objet2;
    private Objet_Move objet3;
    public Plateau(JApplet jap, Image image) {
        this.image = image;
        g = image.getGraphics();
        objet1 = new Objet_Move(10,10,100,80,Color.red,"Rec1",jap,image);
        objet2 = new Objet_Move(80,100,150,40,Color.green,"Rec2",jap,image);
        objet3 = new Objet_Move(200,250,110,110,Color.orange,"Rec2",jap,image);
    }
    public void addListeners(JApplet jap) {
        jap.addMouseMotionListener(objet1);
        jap.addMouseMotionListener(objet2);
        jap.addMouseMotionListener(objet3);
        jap.addMouseListener(objet1);
        jap.addMouseListener(objet2);
        jap.addMouseListener(objet3);
        jap.addKeyListener(objet1);
        jap.addKeyListener(objet2);
        jap.addKeyListener(objet3);
    }
    public void paint() {
        g.setColor(Color.white);
        g.fillRect(0, 0, 500, 500);
        objet1.draw();
        objet2.draw();
        objet3.draw();
    }
}
