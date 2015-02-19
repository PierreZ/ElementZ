package com.bouncer;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JApplet;


public class Objet_Move implements MouseListener, MouseMotionListener, KeyListener {
    // Attributs
    private int x, dx;
    private int y, dy;
    private int longueur;
    private int largeur;
    private Color couleur;
    private String nom;
    private boolean surObjet = false;
    private boolean selectionne = false;
    private boolean presse = false;
    private JApplet jap;
    private Image image;
    Graphics g;
    // Constructeur
    public Objet_Move(int x, int y, int longueur, int largeur, Color couleur, String nom, JApplet jap, Image image) {
        this.image = image;
        this.jap = jap;
        g = image.getGraphics();
        setX(x);
        setY(y);
        setLongueur(longueur);
        setLargeur(largeur);
        setCouleur(couleur);
        setNom(nom);
    }
    public Objet_Move(JApplet jap, Image image) {this(0,0,100,80,Color.red,"",jap, image);}
    // Accesseurs
    public void setX(int x) {this.x = x;}
    public void setY(int y) {this.y = y;}
    public void setLongueur(int longueur) {this.longueur = longueur;}
    public void setLargeur(int largeur) {this.largeur = largeur;}
    public void setCouleur(Color couleur) {this.couleur = couleur;}
    public void setNom(String nom) {this.nom = nom;}
    //-----
    public int getX() {return x;}
    public int getY() {return y;}
    public int getLongueur() {return longueur;}
    public int getLargeur() {return largeur;}
    public Color getCouleur() {return couleur;}
    public String getNom() {return nom;}
    // Les Méthodes
    public void draw() {
        g.setColor(couleur);
        g.fillRect(x, y, longueur, largeur);
        g.setColor(Color.black);
        g.drawRect(x, y, longueur, largeur);
        g.drawString(nom+" : ("+x+","+y+")", x+4, y+14);

        if (surObjet) {
            g.setColor(Color.black);
            g.drawLine(x-4, y-4, x+4, y-4);
            g.drawLine(x-4, y-4, x-4, y+4);
            g.drawLine(x-4, y+largeur+4, x+4, y+largeur+4);
            g.drawLine(x-4, y+largeur+4, x-4, y+largeur-4);
            g.drawLine(x+longueur+4, y-4, x+longueur-4, y-4);
            g.drawLine(x+longueur+4, y-4, x+longueur+4, y+4);
            g.drawLine(x+longueur+4, y+largeur+4, x+longueur+4, y+largeur-4);
            g.drawLine(x+longueur+4, y+largeur+4, x+longueur-4, y+largeur+4);
        }
        if (selectionne) {
            g.setColor(Color.red);
            g.drawRect(x-2, y-2, longueur+4, largeur+4);
        }
    }
    public boolean dansObjet(int x, int y) {
        if (x>this.x && x <(this.x+longueur) && y>this.y && y<(this.y+largeur))
            return true;
        return false;
    }
//

    public void mouseClicked(MouseEvent e) {
        if (dansObjet(e.getX(), e.getY())) {
            selectionne = !selectionne;
            jap.repaint();
        }
    }
    public void mousePressed(MouseEvent e) {
        if (dansObjet(e.getX(), e.getY())) {
            presse = true;
            dx = e.getX()-x;
            dy = e.getY()-y;
        }
    }
    public void mouseReleased(MouseEvent e) {
        if (dansObjet(e.getX(), e.getY())) {
            presse = false;
        }
    }
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public void mouseDragged(MouseEvent e) {
        if (dansObjet(e.getX(), e.getY()) && presse) {
            x = e.getX()-dx;
            y = e.getY()-dy;
            jap.repaint();
        }
    }

    public void mouseMoved(MouseEvent e) {
        surObjet = false;
        if (dansObjet(e.getX(), e.getY()))
            surObjet = true;
        jap.repaint();
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar()=='s') {
            save(image,"objet.png");
        }
        if(this.selectionne) {
            if (e.getKeyCode() == 37) /*Gauche*/ x--;
            if (e.getKeyCode() == 38) /*Haut*/ y--;
            if (e.getKeyCode() == 39) /*Droite*/ x++;
            if (e.getKeyCode() == 40) /*Bas*/ y++;
            jap.repaint();
        }
    }

    public void keyReleased(KeyEvent e) {}

    public void save(Image im, String nom_fichier) {
		FileOutputStream flux;
		BufferedOutputStream fluxBuf;
		JPEGImageEncoder codec;
		BufferedImage imageCourante = (BufferedImage) im;
		try {
			flux = new FileOutputStream(nom_fichier);
			fluxBuf = new BufferedOutputStream(flux);
			codec = JPEGCodec.createJPEGEncoder(fluxBuf,
				JPEGCodec.getDefaultJPEGEncodeParam(
							imageCourante));
			codec.encode(imageCourante);
			fluxBuf.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}

}
