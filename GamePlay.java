import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePlay extends JPanel implements KeyListener, ActionListener {

    private boolean play = false;
    private int score = 0;

    private int totalBricks = 21;

    private Timer timer;
    private int delay = 5;

    private int playerX = 310;

    private int ballPosX = 120;
    private int ballPosY = 350;
    private int ballXdir = -1;
    private int ballYdir = -2;

    private MapGenerator map;
    
    public GamePlay() {
    	map = new MapGenerator(3,7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
    	        
        // Background
        g.setColor(Color.black);
        g.fillRect(0, 0, 700, 600);
        
        //map
    	map.draw((Graphics2D)g);


        // Borders
        g.setColor(Color.RED);
        g.fillRect(0, 0, 3, 592); // Left border
        g.fillRect(0, 0, 692, 4); // Top border
        g.fillRect(683, 0, 3, 592); // Right border
        
        //Scores
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD,25));
        g.drawString("Score: "+score, 580, 30);

        // Paddle
        g.setColor(Color.GREEN);
        g.fillRect(playerX, 550, 100, 8);

        // Ball
        g.setColor(Color.YELLOW);
        g.fillOval(ballPosX, ballPosY, 20, 20);

        if(totalBricks <= 0){
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Congo You Won the Game!", 260, 300);

            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Press Enter to Restart.", 230, 350);
        }

        if(ballPosY > 570){
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 45));
            g.drawString("Game Over, Scores: "+score, 180, 300);

            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Press Enter to Restart.", 230, 350);

        }
        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        
        if(play) {
        	if(new Rectangle(ballPosX,ballPosY,20,20).intersects(new Rectangle(playerX,550,100,8))) {
        		ballYdir = -ballYdir;
        	}
        	
          A: for( int i = 0; i<map.map.length;i++) {
        		for(int j = 0; j< map.map[0].length; j++) {
        			if(map.map[i][j] > 0) {
        				int brickX = j*map.brickWidth + 80;
        				int brickY = i*map.brickHeight + 50;
        				int brickWidth = map.brickWidth;
        				int brickHeight = map.brickHeight;
        				
        				Rectangle rect = new Rectangle(brickX,brickY,brickWidth,brickHeight);
        				Rectangle ballRect = new Rectangle(ballPosX,ballPosY,20,20);
        				Rectangle brickRect = rect;
        				
        				if(ballRect.intersects(brickRect)) {
        					map.setBrickValue(0,i,j);
        					totalBricks--;
        					score+=5;
        					
        					if(ballPosX + 19 <= brickRect.x || ballPosX+1 >= brickRect.x+brickRect.width) {
        						ballXdir = -ballXdir;
        					}
        					else {
        						ballYdir = -ballYdir;
        					}
        					
        					break A;
        				}
        			}
        		}
        	}
        	
        	ballPosX += ballXdir;
        	ballPosY += ballYdir;
        	
        	if(ballPosX < 0) {
        		ballXdir = -ballXdir;
        	}
        	if(ballPosY < 0) {
        		ballYdir = -ballYdir;
        	}
        	if(ballPosX > 670) {
        		ballXdir = -ballXdir;
        	}
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX > 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(!play){
                play = true;
                ballPosX = 120;
                ballPosY = 350;
                ballXdir = -1;
                ballYdir = -2;

                playerX = 310;
                score = 0;
                totalBricks = 21;
                map = new MapGenerator(3, 7);

                repaint();
            }
        }
    }

    public void moveRight() {
        play = true;
        playerX += 20;
    }

    public void moveLeft() {
        play = true;
        playerX -= 20;
    }
}