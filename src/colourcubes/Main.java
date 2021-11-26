package colourcubes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Main implements KeyListener {
    static final int SIZE = 40;
    static JFrame frame;
    static JPanel panel;
    static Timer timer;
    static ActionListener tick;
    static GridSquare player;
    static boolean left, right, up, down, folding, inGame = false, ready = true;

    static double gridX, gridY, cntX = 90, cntY, time, bestTime = 1e9;
    static int rows, cols, loc, rowPos, colPos, delay, moveX, moveY, mvRow, mvCol, colY, rowX, foldState, foldSize, fr = -1, fc = -1, foldDelay, lastSize;
    static ArrayList<ArrayList<GridSquare>> grid;

    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        init();
    }

    public void init() {
        grid = new ArrayList<ArrayList<GridSquare>>();
        reset();
        // Create JFrame
        frame = new JFrame("Colour Cubes");
        frame.setSize(new Dimension(700, 700));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        // Create JPanel
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // every time JPanel redraws it calls drag function
                draw(g);
            }
        };
        panel.addKeyListener(this);
        panel.setFocusable(true);
        panel.setDoubleBuffered(true);
        panel.grabFocus();
        tick = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // this will happen every time the timer goes off
                update();
                panel.repaint();
            }
        };
        timer = new Timer(16, tick);
        timer.start();
        frame.add(panel);
        frame.setVisible(true);
    }

    public void update() {
        if(inGame) {
            // increase timer
            time += 0.016;
            // delay == 0 means you have waited long enough to move again
            if(delay == 0) {
                if(foldDelay == 0 && delay == 0) {
                    collapseLines();
                } else {
                    foldDelay--; return;
                } if(rows == 0 || cols == 0) return; 
                if(delay > 0) return;
                if(up) {
                    if(loc == 4) {
                        rowPos--; delay = 6;
                        if(rowPos == -1) loc = 1;
                        moveY += SIZE;
                    }
                    if(loc == 5) {
                        rowPos--; delay = 6;
                        if(rowPos == -1) loc = 3;
                        moveY += SIZE;
                    }
                    if(loc == 6) {
                        rowPos = rows - 1;
                        loc = 4; delay = 6;
                        moveY += SIZE;
                    }
                    if(loc == 8) {
                        rowPos = rows - 1;
                        loc = 5; delay = 6;
                        moveY += SIZE;
                    } // push
                    if(loc == 7) {
                        GridSquare temp = grid.get(0).get(colPos);
                        for(int i = 0; i < rows - 1; i++) {
                            grid.get(i).set(colPos, grid.get(i+1).get(colPos));
                        } grid.get(rows - 1).set(colPos, player);
                        player = temp;
                        loc = 2; delay = 6;
                        colY += SIZE; moveY += SIZE;
                        mvCol = colPos;
                    }
            } else if(down) {
                    if(loc == 4) {
                        rowPos++; delay = 6;
                        if(rowPos >= rows) loc = 6;
                        moveY -= SIZE;
                    }
                    if(loc == 5) {
                        rowPos++; delay = 6;
                        if(rowPos >= rows) loc = 8;
                        moveY -= SIZE;
                    }
                    if(loc == 1) {
                        rowPos = 0;
                        loc = 4; delay = 6;
                        moveY -= SIZE;
                    }
                    if(loc == 3) {
                        rowPos = 0;
                        loc = 5; delay = 6;
                        moveY -= SIZE;
                    } // push
                    if(loc == 2) {
                        GridSquare temp = grid.get(rows - 1).get(colPos);
                        for(int i = rows - 1; i >= 1; i--) {
                            grid.get(i).set(colPos, grid.get(i-1).get(colPos));
                        } grid.get(0).set(colPos, player);
                        player = temp;
                        loc = 7; delay = 6;
                        colY -= SIZE; moveY -= SIZE;
                        mvCol = colPos;
                    }
            } else if(left) {
                    if(loc == 2) {
                        colPos--; delay = 6;
                        if(colPos == -1) loc = 1;
                        moveX += SIZE;
                    }
                    if(loc == 7) {
                        colPos--; delay = 6;
                        if(colPos == -1) loc = 6;
                        moveX += SIZE;
                    }
                    if(loc == 3) {
                        colPos = cols - 1;
                        loc = 2; delay = 6;
                        moveX += SIZE;
                    }
                    if(loc == 8) {
                        colPos = cols - 1;
                        loc = 7; delay = 6;
                        moveX += SIZE;
                    } // push
                    if(loc == 5) {
                        grid.get(rowPos).add(player);
                        player = grid.get(rowPos).get(0);
                        grid.get(rowPos).remove(0);
                        loc = 4; delay = 6;
                        rowX += SIZE; moveX += SIZE;
                        mvRow = rowPos;
                    }
            } else if(right) {
                    if(loc == 2) {
                        colPos++; delay = 6;
                        if(colPos >= cols) loc = 3;
                        moveX -= SIZE;
                    }
                    if(loc == 7) {
                        colPos++; delay = 6;
                        if(colPos >= cols) loc = 8;
                        moveX -= SIZE;
                    }
                    if(loc == 1) {
                        colPos = 0;
                        loc = 2; delay = 6;
                        moveX -= SIZE;
                    }
                    if(loc == 6) {
                        colPos = 0;
                        loc = 7; delay = 6;
                        moveX -= SIZE;
                    } // push
                    if(loc == 4) {
                        grid.get(rowPos).add(0, player);;
                        player = grid.get(rowPos).get(cols);
                        grid.get(rowPos).remove(cols);
                        loc = 5; delay = 6;
                        rowX -= SIZE; moveX -= SIZE;
                        mvRow = rowPos;
                    }
                } 
            } else delay--;
        } 
    }

    // used to draw the text at certain x and y with size and color
    public void drawText(Graphics g, int x, int y, int size, String txt, Color c1, Color c2) {
        g.setColor(c1);
        g.setFont(new Font("Sans", Font.BOLD, size)); 
        g.drawString(txt, x, y + 2);
        g.setColor(c2);
        g.setFont(new Font("Sans", Font.BOLD, size)); 
        g.drawString(txt, x + 2, y);
    }

    public void draw(Graphics g) {
        drawGrid(g);

        int textOffset = 0, bestTextOffset;
        // calculate how much to shift the timer based on number of digits it has
        if(time != 0) textOffset = Math.max(0, ((int) Math.log10(time)) * 27);
        // draw timer value rounded to 2 decimal places
        drawText(g, 290 - textOffset, 102, 45, String.valueOf(Math.round(time * 100.0) / 100.0), Color.LIGHT_GRAY, Color.BLACK);
        if(ready && !inGame) {
            time = 0;
            drawText(g, 150, 550, 30, "Press Any Key To Start", Color.LIGHT_GRAY, Color.BLACK);
        } else if(!inGame) {
            bestTime = Math.min(time, bestTime);
            bestTextOffset = Math.max(0, ((int) Math.log10(bestTime)) * 27);
            drawText(g, 120, 550, 30, "Press Any Key To Continue", Color.LIGHT_GRAY, Color.BLACK);
            drawText(g, 220, 300, 30, "Your Best Time:", Color.LIGHT_GRAY, Color.BLACK);
            drawText(g, 290 - bestTextOffset, 350, 45, String.valueOf(Math.round(bestTime * 100.0) / 100.0), Color.LIGHT_GRAY, Color.YELLOW.darker());
        }

        int offsetX, offsetY, widthOffset = 0, heightOffset = 0;
        offsetX = (frame.getWidth() / 2) - ((cols * SIZE) / 2) - (SIZE / 2);
        offsetY = (frame.getHeight() / 2) - ((rows * SIZE) / 2) - (SIZE / 2);

        if(foldSize > 0) foldSize -= SIZE / 4;

        if(rowX == 0) mvRow = -1;
        if(colY == 0) mvCol = -1;

        if(rowX > 0) rowX -= SIZE / 4;
        if(colY > 0) colY -= SIZE / 4;
        if(rowX < 0) rowX += SIZE / 4;
        if(colY < 0) colY += SIZE / 4;

        if(foldState == 1) offsetY += (SIZE - foldSize) / 2;
        if(foldState == 2) offsetX += (SIZE - foldSize) / 2;
        // draws the squares by looping over rows and columns
        for(int i = 0; i < grid.size(); i++) {
            // height change if row is folding
            if(fr == i) heightOffset = (SIZE - foldSize);
            for(int j = 0; j < grid.get(i).size(); j++) {
                // width change if column is folding
                if(fc == j) widthOffset = (SIZE - foldSize); 
                // if row is moving or col is moving we add an offset
                if(i == mvRow) offsetX += rowX;
                if(j == mvCol) offsetY += colY;

                grid.get(i).get(j).draw(g, (j * SIZE) + offsetX, (i * SIZE) + offsetY, widthOffset, heightOffset);

                if(i == mvRow) offsetX -= rowX;
                if(j == mvCol) offsetY -= colY;
                if(fc == j) offsetX -= (SIZE - foldSize);
                widthOffset = 0;
            }
            // if row is folding, all rows after must be shifted by a bit
            if(fr == i) offsetY -= (SIZE - foldSize);
            if(fc != -1) offsetX += (SIZE - foldSize);
            heightOffset = 0;
        }
        if(moveX > 0) moveX -= SIZE / 4;
        if(moveY > 0) moveY -= SIZE / 4;

        if(moveX < 0) moveX += SIZE / 4;
        if(moveY < 0) moveY += SIZE / 4;

        offsetX += moveX;
        offsetY += moveY;

        // edge cases
        if(fr != -1 && (loc == 1 || loc == 2 || loc == 3 || (loc == 4 && rowPos <= fr) || (loc == 5 && rowPos <= fr)) && !(rowPos == rows - 1)) offsetY += (SIZE - foldSize);
        if(fc != -1 && (loc == 3 || loc == 5 || loc == 8 || (loc == 2 && colPos > fc) || (loc == 7 && colPos > fc) || colPos == cols - 1)) offsetX -= (SIZE - foldSize); 

        if(rows == 0 || cols == 0) {
            if(lastSize > 0) lastSize -= 2;
            else {
                ready = false;
                inGame = false;
            }
        } else lastSize = SIZE;
        // draws the player in the 8 locations of the pos variable
        //
        // 1 2 2 2 3
        // 4 X X X 5
        // 4 X X X 5
        // 4 X X X 5
        // 6 7 7 7 8
        //
        if(lastSize > 0) {
            if(loc == 1) {
                player.draw(g, offsetX - SIZE + (SIZE - lastSize) / 2, offsetY - SIZE + (SIZE - lastSize) / 2, SIZE - lastSize, SIZE - lastSize);
            } else if(loc == 2) {
                player.draw(g, offsetX + (colPos * SIZE) + (SIZE - lastSize) / 2, offsetY - SIZE + (SIZE - lastSize) / 2, SIZE - lastSize, SIZE - lastSize);
            } else if(loc == 3) {
                player.draw(g, offsetX + (cols * SIZE) + (SIZE - lastSize) / 2, offsetY - SIZE + (SIZE - lastSize) / 2, SIZE - lastSize, SIZE - lastSize);
            } else if(loc == 4) {
                player.draw(g, offsetX - SIZE + (SIZE - lastSize) / 2, offsetY + (rowPos * SIZE) + (SIZE - lastSize) / 2, SIZE - lastSize, SIZE - lastSize);
            } else if(loc == 5) {
                player.draw(g, offsetX + (cols * SIZE) + (SIZE - lastSize) / 2, offsetY + (rowPos * SIZE) + (SIZE - lastSize) / 2, SIZE - lastSize, SIZE - lastSize);
            } else if(loc == 6) {
                player.draw(g, offsetX - SIZE + (SIZE - lastSize) / 2, offsetY + (rows * SIZE) + (SIZE - lastSize) / 2, SIZE - lastSize, SIZE - lastSize);
            } else if(loc == 7) {
                player.draw(g, offsetX + (colPos * SIZE) + (SIZE - lastSize) / 2, offsetY + (rows * SIZE) + (SIZE - lastSize) / 2, SIZE - lastSize, SIZE - lastSize);
            } else {
                player.draw(g, offsetX + (cols * SIZE) + (SIZE - lastSize) / 2, offsetY + (rows * SIZE) + (SIZE - lastSize) / 2, SIZE - lastSize, SIZE - lastSize);
            } if(foldSize == 0) {
                if(fr != -1) removeRow(fr);
                if(fc != -1) removeCol(fc);
            }
        }

    }

    // deletes a row
    public void removeRow(int idx) {
        grid.remove(idx); 
        rows--; folding = false; foldState = 0;
        if(rowPos > idx || rowPos == rows) rowPos--; fr = -1;
    }

    // deletes a column
    public void removeCol(int idx) {
        for(int i = 0; i < rows; i++) {
            grid.get(i).remove(idx); 
        } cols--; folding = false; fc = -1;
        if(colPos > idx || colPos == cols) colPos--; foldState = 0;
    }

    // checks rows and columns to see if any of them are all the same color
    public void collapseLines() {
        if(cols < 1 || rows < 1) return;
        // rows
        for(int i = 0; i < rows; i++) {
            if(cols < 1 || rows < 1) return;
            Color c = grid.get(i).get(0).color; boolean valid = true;
            for(int j = 0; j < cols; j++) {
                // check if an entire row is same color
                if(grid.get(i).get(j).color != c) {
                    valid = false;
                    break;
                }
            } if(valid) {
                // make sure only 1 col/row can fold at a time
                if(!folding) {
                    // call the foldRow method
                    foldRow(i); foldDelay = 10; delay = 10;
                    folding = true; foldState = 1;
                } 
            }
        } // cols
        for(int i = 0; i < cols; i++) {
            if(cols < 1 || rows < 1) return;
            Color c = grid.get(0).get(i).color; boolean valid = true;
            // check if an entire column is the same color
            for(int j = 0; j < rows; j++) {
                if(grid.get(j).get(i).color != c) {
                    valid = false;
                    break;
                }
            } if(valid) {
                // make sure only 1 col/row can fold at a time
                if(!folding) {
                    // call foldCol method
                    foldCol(i); foldDelay = 10; delay = 10;
                    folding = true; foldState = 2;
                }
            }
        }
    }

    // sets up the player and gives them a random position
    public void reset() {
        for(int i = 1; i <= 6; i++) {
            addRow(); addCol();
        } player = new GridSquare(); 
        // start the player off at random location
        int ran = (int)(8 * Math.random() + 1);
        int pos = (int)(5 * Math.random());
        loc = ran;
        if(ran == 2 || ran == 7) colPos = pos;
        if(ran == 4 || ran == 5) rowPos = pos;
    }

    // function used to indicate a row is collapsing
    public void foldRow(int idx) {
        if(fr != -1) return;
        fr = idx;
        foldSize = SIZE;
    }

    // function used to indicate a column is collapsing
    public void foldCol(int idx) {
        if(fc != -1) return;
        fc = idx;
        foldSize = SIZE;
    }

    // draws the background grid using for loops and modulus
    public void drawGrid(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        for(double i = gridX; i <= frame.getWidth(); i += SIZE) {
            g.drawLine((int) i, 0, (int) i, frame.getHeight());
        } gridX = (gridX + Math.sin(Math.toRadians(cntX))) % SIZE;
        for(double i = gridY; i <= frame.getHeight(); i += SIZE) {
            g.drawLine(0, (int) i, frame.getWidth(), (int) i);
        } gridY = (gridY + Math.sin(Math.toRadians(cntY))) % SIZE;
        cntY = (cntY + 0.1) % 360;
        cntX = (cntX + 0.1) % 360;
    }

    // adds a row by generating a row of new squares
    public void addRow() {
        ArrayList<GridSquare> rw = new ArrayList<GridSquare>();
        for(int i = 0; i < cols; i++) {
                rw.add(new GridSquare());
        } grid.add(rw);
        rows++;
    }

    // adds a column by generating a column of new squares
    public void addCol() {
        for(int i = 0; i < grid.size(); i++) {
                grid.get(i).add(new GridSquare());
        } cols++;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if(ready) inGame = true;
        if(!inGame) {
            ready = true; rows = 0; cols = 0;
            for(int i = 1; i <= 6; i++) {
                addRow(); addCol();
            } player = new GridSquare(); loc = 1;
        } // direction keys
        if(e.getKeyCode() == KeyEvent.VK_UP) {
                up = true;
        } else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                down = true;
        } else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
                right = true;
        } else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
                left = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // reset arrow key booleans
        if(e.getKeyCode() == KeyEvent.VK_UP) {
                up = false;
        } else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                down = false;
        } else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
                right = false;
        } else if(e.getKeyCode() == KeyEvent.VK_LEFT) {
                left = false;
        }
    }
}