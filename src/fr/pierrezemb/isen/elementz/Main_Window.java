package fr.pierrezemb.isen.elementz;

import com.bouncer.ElementZ_Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by pierrezemb on 26/01/15.
 */
public class Main_Window extends JFrame implements ElementZ_Model_Listener{

    public static int SIZE_GRID = 8;
    public static int GRID_GAP = 10;
    public static int IMAGE_SIZE = 50;

    // 60 sec for a game
    public static int TIME_GAME = 60;

    public static final int SLEEP_TIMER = 10;
    public static boolean isWorking;

    public Timer timer;
    public Thread threadProgressbar;

    public static boolean gameOn;

    public static HTTPFetcher netscape;

    // String to replace classical state
    public static String CLASSIC_STATE = "boule";
    public static String OVER_STATE="boule_o";
    public static String SELECTED_STATE = "boule_s";

    // Paths for the ball's sprite
    // https://stackoverflow.com/questions/16924347/java-path-imageicon-url-jar
    public static String IMAGE_ROOT = "images/";
    // Usecase for MouseHandler calculator
    public static String EXAMPLE= "_0.jpg";

    // Classical paths
    public static String BLACKBALL_PATH= IMAGE_ROOT+CLASSIC_STATE+"_0.jpg";
    public static String REDBALL_PATH= IMAGE_ROOT+CLASSIC_STATE+"_1.jpg";
    public static String GREENBALL_PATH=IMAGE_ROOT+CLASSIC_STATE+"_2.jpg";
    public static String BLUEBALL_PATH= IMAGE_ROOT+CLASSIC_STATE+"_3.jpg";
    public static String PURPLEBALL_PATH= IMAGE_ROOT+CLASSIC_STATE+"_4.jpg";
    public static String YELLOWBALL_PATH=IMAGE_ROOT+CLASSIC_STATE+"_5.jpg";
    public static String GOLDBALL_PATH= IMAGE_ROOT+CLASSIC_STATE+"_6.jpg";

    public static ImageIcon RED = new ImageIcon(Main_Window.class.getClassLoader().getResource(REDBALL_PATH));
    public static ImageIcon GREEN = new ImageIcon(Main_Window.class.getClassLoader().getResource(GREENBALL_PATH));
    public static ImageIcon BLUE = new ImageIcon(Main_Window.class.getClassLoader().getResource(BLUEBALL_PATH));
    public static ImageIcon PURPLE = new ImageIcon(Main_Window.class.getClassLoader().getResource(PURPLEBALL_PATH));
    public static ImageIcon YELLOW= new ImageIcon(Main_Window.class.getClassLoader().getResource(YELLOWBALL_PATH));
    public static ImageIcon GOLD= new ImageIcon(Main_Window.class.getClassLoader().getResource(GOLDBALL_PATH));




    // last click
    public static int last_click_x;
    public static int last_click_y;

    public static boolean isFirstClick;

    public static String userName;

    // model
    public static ElementZ_Model model;

    private JPanel JPanelRoot;
    private JPanel LeftPanel;
    private JPanel RightPanel;
    private JButton playButton;
    private JLabel ScoreLabel;
    private JList ScoreList;
    private JProgressBar progressBar;

    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem openMenuItem;
    private JMenuItem editMenu;
    private JMenuItem splashItem;


    public Main_Window() {

        super("ElementZ");

        userName = "";
        netscape = new HTTPFetcher();

        // -1 is impossible for a value !
        last_click_x = -1;
        last_click_y = -1;
        isFirstClick = false;
        isWorking = false;

        // starting a new model
        this.model = new ElementZ_Model();

        // Creates the UI
        createUI();

        // Start timer
        startTimerThread();

        // We register as an listener of the model
        model.addModelListener(this);
    }

    private void createUI() {
        setContentPane(JPanelRoot);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setSize(650,450);

        createMenu();

        this.playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Main_Window.model.newGame();
                timer.stop();
                threadProgressbar.stop();
                startTimerThread();
            }
        });

        this.LeftPanel.setLayout(new GridLayout(SIZE_GRID,SIZE_GRID));
        this.generateGrid();
        addListeners();
        setVisible(true);
    }
    // Start timer
    private void startTimerThread() {
        this.gameOn = true;

        showTop();

        playButton.setText("Restart");

        progressBar.setValue(60);
        ActionListener EndPopup = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main_Window.gameOn = false;
                Main_Window.userName = JOptionPane.showInputDialog(menuBar,"<html><h2>Time's up!</h2>"+
                        "You've made <b>"+Main_Window.model.getScore()+"</b> in a minute!<br>"+
                        "What's your name?<br></html>");
                if (Main_Window.userName.length() !=0) {
                    push2Cloud();
                    showTop();
                }
            }
        };
        timer = new Timer(TIME_GAME*1000, EndPopup);
        timer.start();
        threadProgressbar = new Thread(new Runnable() { public void run() {
            try {
                for (int i = 0;i<TIME_GAME;i++){
                    Thread.sleep(1000);
                    progressBar.setValue(progressBar.getValue()-1);
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }});
        threadProgressbar.start();

    }
    // push data on my server
    private void push2Cloud() {
        try {
            netscape.sendPost(this.userName,this.model.getScore());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // build the menu
    private void createMenu() {
        menuBar = new JMenuBar();

        fileMenu = new JMenu("ElementZ");
        openMenuItem = new JMenuItem("Quit");
        openMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false); //you can't see me!
                dispose(); //Destroy the JFrame object
            }
        });
        fileMenu.add(openMenuItem);

        // build the Edit menu
        editMenu = new JMenu("Help");
        splashItem = new JMenuItem("Credits");

        editMenu.add(splashItem);

        splashItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                JOptionPane
                        .showMessageDialog(
                                menuBar,
                                "<html><h2>Element Z</h2>"
                                        + "The goal is to align 3 or more balls. Try to make the best score in one minute!<br>" +
                                        "Made by Pierre Zemb<br>" +
                                        "<a href=\"https://pierrezemb.fr\">https://pierrezemb.fr</a></html>",
                                "About ElementZ",
                                JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // add menus to menubar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        // put the menubar on the frame
        setJMenuBar(menuBar);
    }


    /**
     * This method's handling the generation of the Left Panel
     * according to the model
     */
    private void generateGrid() {
        int i,j;

        for (i = 0;i<SIZE_GRID;i++) {
            for (j = 0;j<SIZE_GRID;j++){

                switch (this.model.getXY(i,j)){
                    case 1:
                        this.LeftPanel.add(new JLabel(RED, JLabel.CENTER));
                        break;
                    case 2:
                        this.LeftPanel.add(new JLabel(GREEN, JLabel.CENTER));
                        break;
                    case 3:
                        this.LeftPanel.add(new JLabel(BLUE, JLabel.CENTER));
                        break;
                    case 4:
                        this.LeftPanel.add(new JLabel(PURPLE, JLabel.CENTER));
                        break;
                    case 5:
                        this.LeftPanel.add(new JLabel(YELLOW, JLabel.CENTER));
                        break;
                    case 6:
                        this.LeftPanel.add(new JLabel(GOLD, JLabel.CENTER));
                        break;
                }
            }
        }
    }

    private void addListeners() {
        int i, j;
        for (i = 0; i < SIZE_GRID; i++) {
            for (j = 0; j < SIZE_GRID; j++) {
                this.getElement(i, j).addMouseListener(new MouseHandler());
            }
        }
    }

    // Return a ball
    public Component getElement(int x, int y){
        return LeftPanel.getComponent(x * 8 + y);
    }

    // Repaint the balls, obviously
    public void repaintBalls(){
        if ( SwingUtilities.isEventDispatchThread () ) {
            int i,j;
            for (i = 0; i < SIZE_GRID; i++) {
                for (j = 0; j < SIZE_GRID; j++) {

                    switch (Main_Window.model.getXY(i, j)) {
                        case 1:
                            ((JLabel) getElement(i, j)).setIcon(RED);
                            break;
                        case 2:
                            ((JLabel) getElement(i, j)).setIcon(GREEN);
                            break;
                        case 3:
                            ((JLabel) getElement(i, j)).setIcon(BLUE);
                            break;
                        case 4:
                            ((JLabel) getElement(i, j)).setIcon(PURPLE);
                            break;
                        case 5:
                            ((JLabel) getElement(i, j)).setIcon(YELLOW);
                            break;
                        case 6:
                            ((JLabel) getElement(i, j)).setIcon(GOLD);
                            break;
                    }
                }
            }

        }
        else {
            Runnable callMAJ = new Runnable () {
                public void run () {
                    repaintBalls();
                }
            };
            SwingUtilities.invokeLater (callMAJ);
        }

    }

    // Method called on there's a change on the model
    @Override
    public void modelChanged(ModelChangedEvent event) {
        new Thread() {
            public void run() {
                refresh();
            }
        }.run();
    }

    // Same method than model.play but with thread sleep...
    public void refresh(){

        this.isWorking = true;
        repaintBalls();

                while(model.notReadyToPlay())
                {

                    // adding score
                    this.model.addScore();

                    try{Thread.sleep(SLEEP_TIMER);}catch(InterruptedException e){};

                    model.deleteTheSame();

                    repaintBalls();

                    try{Thread.sleep(SLEEP_TIMER);}catch(InterruptedException e){};

                    model.gravity();

                    repaintBalls();

                    try{Thread.sleep(SLEEP_TIMER);}catch(InterruptedException e){};

                    model.fillGaps();

                    repaintBalls();
                }

        this.isWorking = false;
        pushScore();

    }

    // print score on the window
    public void pushScore(){
        System.out.println(model.getScore());
        ScoreLabel.setText(String.valueOf(this.model.getScore()));
    }

    // Refresh JList with data on the backend
    public void showTop(){
        try {
            ScoreList.setListData(netscape.sendGet().toArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

