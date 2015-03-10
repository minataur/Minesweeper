import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import de.bezier.guido.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Minesweeper extends PApplet {




int NUM_COLS = 20;
int NUM_ROWS = 20;
private MSButton[][] buttons; //2d array of minesweeper buttons
private ArrayList <MSButton> bombs; //ArrayList of just the minesweeper buttons that are mined

public void setup ()
{
    size(400, 400);
    textAlign(CENTER,CENTER);
    
    // make the manager
    Interactive.make( this );
    
    
    buttons = new MSButton[NUM_ROWS][NUM_COLS];
    for (int y = 0; y<NUM_ROWS;y++) {
        for (int x=0;x<NUM_COLS;x++) {
            buttons[y][x] = new MSButton(y,x);
        }
    }
    bombs = new ArrayList<MSButton>();
    setBombs();
}
public void setBombs()
{
    int numBombs = 30;
    for (int i = 0; i<numBombs; i++) {
        int row = (int)(Math.random()*NUM_ROWS);
        int col = (int)(Math.random()*NUM_COLS);
        if (! bombs.contains(buttons[row][col])) {
            bombs.add(buttons[row][col]);
        }
    }
}

public void draw ()
{
    background( 0 );
    if(isWon())
        displayWinningMessage();
}
public boolean isWon()
{
    for (int i = 0; i<bombs.size();i++) {
        if (bombs.get(i).isMarked() == false) {
            return false;
        }
    }
    return true;
}
public void displayLosingMessage()
{
    buttons[9][6].setLabel("Y");
    buttons[9][7].setLabel("O");
    buttons[9][8].setLabel("U");
    buttons[9][9].setLabel(" ");
    buttons[9][10].setLabel("L");
    buttons[9][11].setLabel("O");
    buttons[9][12].setLabel("S");
    buttons[9][13].setLabel("E");
    for (int i =0; i < bombs.size (); i++) {
        bombs.get(i).marked = false;
        bombs.get(i).clicked = true;
    }

}
public void displayWinningMessage()
{
    buttons[9][6].setLabel("Y");
    buttons[9][7].setLabel("O");
    buttons[9][8].setLabel("U");
    buttons[9][9].setLabel(" ");
    buttons[9][10].setLabel("W");
    buttons[9][11].setLabel("I");
    buttons[9][12].setLabel("N");
}

public class MSButton
{
    private int r, c;
    private float x,y, width, height;
    private boolean clicked, marked;
    private String label;
    
    public MSButton ( int rr, int cc )
    {
        width = 400/NUM_COLS;
        height = 400/NUM_ROWS;
        r = rr;
        c = cc; 
        x = c*width;
        y = r*height;
        label = "";
        marked = clicked = false;
        Interactive.add( this ); // register it with the manager
    }
    public boolean isMarked()
    {
        return marked;
    }
    public boolean isClicked()
    {
        return clicked;
    }
    // called by manager
    
    public void mousePressed () 
    {
        clicked = true;
        
        if (mouseButton == RIGHT) {
            marked = !marked;
        } else if (bombs.contains(this)) {
            displayLosingMessage();
        } else if (countBombs(r,c)>0) {
            fill(0);
            setLabel(str(countBombs(r,c)));
        } else {
            if (isValid(r-1,c) && buttons[r-1][c].isClicked()==false) {
                buttons[r-1][c].mousePressed();
            }
            if (isValid(r-1,c+1) && buttons[r-1][c+1].isClicked()==false) {
                buttons[r-1][c+1].mousePressed();
            }
            if (isValid(r-1,c-1) && buttons[r-1][c-1].isClicked()==false) {
                buttons[r-1][c-1].mousePressed();
            }
            if (isValid(r,c+1) && buttons[r][c+1].isClicked()==false) {
                buttons[r][c+1].mousePressed();
            }
            if (isValid(r,c-1) && buttons[r][c-1].isClicked()==false) {
                buttons[r][c-1].mousePressed();
            }
            if (isValid(r+1,c) && buttons[r+1][c].isClicked()==false) {
                buttons[r+1][c].mousePressed();
            }
            if (isValid(r+1,c) && buttons[r+1][c].isClicked()==false) {
                buttons[r-1][c].mousePressed();
            }
            if (isValid(r+1,c-1) && buttons[r+1][c-1].isClicked()==false) {
                buttons[r+1][c-1].mousePressed();
            }
        }
    }

    public void draw () 
    {    
        if (marked)
            fill(0);
        else if( clicked && bombs.contains(this) ) 
             fill(255,0,0);
        else if(clicked)
            fill( 200 );
        else 
            fill( 100 );

        rect(x, y, width, height);
        fill(0);
        text(label,x+width/2,y+height/2);
    }
    public void setLabel(String newLabel)
    {
        label = newLabel;
    }
    public boolean isValid(int r, int c)
    {
        if (r>0 && r<NUM_ROWS) {
            if (c>0 && c<NUM_COLS) {
                return true;
            }
        }
        return false;
    }
    public int countBombs(int row, int col)
    {
        int numBombs = 0;
        
        if (isValid(r-1,c) && bombs.contains(buttons[r-1][c])) {
            numBombs++;
        }
        if (isValid(r-1,c-1) && bombs.contains(buttons[r-1][c-1])) {
            numBombs++;
        }
        if (isValid(r-1,c+1) && bombs.contains(buttons[r-1][c+1])) {
            numBombs++;
        }
        if (isValid(r,c-1) && bombs.contains(buttons[r][c-1])) {
            numBombs++;
        }
        if (isValid(r,c+1) && bombs.contains(buttons[r][c+1])) {
            numBombs++;
        }
        if (isValid(r+1,c+1) && bombs.contains(buttons[r+1][c+1])){
            numBombs++;
        }
        if (isValid(r+1,c-1) && bombs.contains(buttons[r+1][c-1])){
            numBombs++;
        }
        if (isValid(r+1,c) && bombs.contains(buttons[r+1][c])){
            numBombs++;
        }

        return numBombs;
    }
}



  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Minesweeper" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
