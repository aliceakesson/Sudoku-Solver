
package SudokuSolver;

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import javax.swing.JTextField;

public class SudokuSolver extends Applet implements ActionListener, TextListener, KeyListener, FocusListener {
    
    int MARGIN_X = 50; 
    int MARGIN_Y = 50; 
    int length = 40; 
    
    Ruta [] rutaArray = new Ruta[81];
    JTextField [] textArray = new JTextField[81];
    
    Button done = new Button("Klar");
    
    String error = "";
    
    boolean hasClickedTextField = false; 
    int clickedTextfield = -1;
    
    boolean valuesHasBeenAdded = true; 
    int addedValues = 0; 
    int [][] valueArray = new int[81][9];
    
    public void init() {
        
        this.setLayout(null);
        this.setSize(500, 500);
        
        int x = 0, y = 0; 
        for(int i = 0; i < 81; i++) {
            rutaArray[i] = new Ruta(MARGIN_X+length*x, MARGIN_Y+length*y);
            
            textArray[i] = new JTextField();
            textArray[i].setBounds(MARGIN_X+length*x+1, MARGIN_Y+length*y+1, length-2, length-2);
            textArray[i].setBorder(null);
            textArray[i].addFocusListener(this);
            textArray[i].addKeyListener(this);
            
            textArray[i].setHorizontalAlignment(JTextField.CENTER);
            textArray[i].setFont(new Font("Arial", 0, 20));
            
            add(textArray[i]);
            
            x++; 
            if(x == 9) {
                x = 0; 
                y++; 
            }
        }
                
        done.setBounds(MARGIN_X+length*4, MARGIN_Y*2+length*9, 50, 30);
        done.addActionListener(this);
        add(done);
        
    }
    public void paint(Graphics g) {
        
        for(int i = 0; i < 81; i++) {
            rutaArray[i].show(g);
        }
                
        //feta linjer
        for(int i = 0; i < 4; i++) {
            g.drawLine(MARGIN_X, MARGIN_Y + (length*3*i) -1, MARGIN_X + length*9, MARGIN_Y + (length*3*i) -1);
        }
        for(int i = 0; i < 4; i++) {
            g.drawLine(MARGIN_X + (length*3*i) -1, MARGIN_Y, MARGIN_X + (length*3*i) -1, MARGIN_Y + length*9);
        }
        
        g.setColor(Color.red);
        g.setFont(new Font("Arial", 1, 40));
        g.drawString(error, MARGIN_X-length/2, MARGIN_Y+length*4+length/2);
        
    }
    public void actionPerformed(ActionEvent e) {
        
        /*
            1. checka fel 
            2. gå igenom möjliga värden för varje ruta (efter vad som redan är inmatat)
                2.1. för varje siffra 1-9: se om den går både vågrät, lodrät och kvadrat
            3. sök ensamma värden (ex bara en i första raden som kan ha värde 4) och tilldela dem
            4. steg 2 och 3 i loop tills att det inte går mer (= inget ändras,inga nya tilldelningar)
            5. pröv-strategin
                5.1. gå igenom varje vågrät rad för dubletter (ex. två rutor som kan ha värde 4 på en rad)
                    5.1.1. sök om ruta 1 kan ha värdet utan att det blir fel, annars ruta 2 (om inget fel ¨på båda: skippa)
                        5.1.1.1. kolla nya värden som kan tilläggas (våg, lod, kva.. generellt hela brädet)
                        5.1.1.2. så fort det kommer tvingad dublett = fel 
                5.2. lodräta rader
                5.3 kvadrater 
        */
        
        //checka fel (och gör om text till endast en eller ingen siffra)
        for(int i = 0; i < 81; i++) {
            if(textArray[i].getText().length() > 1) 
                textArray[i].setText(textArray[i].getText().charAt(0) + "");
            
            try {
                int x = 0; 
                
                if(textArray[i].getText().length() > 0)
                    x = Integer.parseInt(textArray[i].getText());
                
            } catch(NumberFormatException ne) {
            
                error = "Någonting gick snett!";
                repaint();
                
            };
        }
        
        //steg 2 och 3 (4) 
        while(valuesHasBeenAdded) {
            addedValues = 0; 
            
            //steg 2
            for(int i = 0; i < 81; i++) { //alla rutor nr 1-81
                if(textArray[i].getText().length() < 1) { //om den inte har något värde
                    getPossibleValues(i);
                }
            }
            
            //steg 3
            for(int i = 0; i < 81; i++) {
                if(textArray[i].getText().length() < 1) {
                    int k = 0; 
                    for(int j = 0; j < 9; j++) {
                        if(valueArray[i][j] > 0) {
                            //nås
                            k++; 
                        }
                    }
                    System.out.println("k: " + k); // nås
                    if(k == 1) {
                        for(int j = 0; j < 9; j++) {
                            if(valueArray[i][j] > 0) {
                                textArray[i].setText(valueArray[i][j] + "");
                                addedValues++; 
                                break; 
                            }
                        }
                    }
                }
            }
            
            if(addedValues > 0)
                valuesHasBeenAdded = true; 
            else
                valuesHasBeenAdded = false; 
                        
        }
                
        //steg 5
        boolean everythingFilled = true; 
        for(int i = 0; i < 81; i++) {
            if(textArray[i].getText().length() < 1) {
                everythingFilled = false; 
            }
        }
        if(everythingFilled) {
            System.out.println("första rutan: " + textArray[0].getText());
            System.out.println("näst sista rutan: " + textArray[79].getText());
            System.out.println("sista rutan: " + textArray[80].getText());
            System.out.println("done");
        }
        else {
            System.out.println("--------------else-del----------------");
            valuesHasBeenAdded = true; 
            while(valuesHasBeenAdded) {
                for(int i = 0; i < 81; i++) {
                    if(textArray[i].getText().length() < 1) {
                        int k = 0; 
                        for(int j = 0; j < 9; j++) {
                            if(valueArray[i][j] > 0) {
                                k++; 
                            }
                        }
                        if(k == 2) {
                            int a = 0, b = 0; 
                            for(int j = 0; j < 9; j++) {
                                if(valueArray[i][j] > 0) {
                                    if(a == 0) {
                                        a = valueArray[i][j];
                                    }
                                    else {
                                        b = valueArray[i][j];
                                    }
                                }
                            }
                            
                            int [] horizontal = getHorizontalNumbers(i);
                            for(int j = 0; j < 8; j++) {
                                if(textArray[horizontal[j]].getText().length() < 1) {
                                    getPossibleValues(horizontal[j]);
                                    int k2 = 0; 
                                    for(int j2 = 0; j2 < 9; j2++) {
                                        if(valueArray[horizontal[j]][j2] > 0) 
                                            k2++; 
                                    }
                                    if(k2 == 2) {
                                        
                                    }
                                }
                            }
                        }
                    }
                }
            } // while(valuesHasBeenAdded) {
        } //else {
    }
    public void getPossibleValues(int i) {
        
        int[] horizontalNumbers = new int[8];
        int[] verticalNumbers = new int[8];
        int[] squareNumbers = new int[8];

        horizontalNumbers = getHorizontalNumbers(i);
        verticalNumbers = getVerticalNumbers(i);
        squareNumbers = getSquareNumbers(i);

        //vågräta, lodräta, kvadrat möjliga
        for(int n = 1; n < 10; n++) { //möjliga värden 1-9
            boolean isPossibleHorizontal = true;
            boolean isPossibleVertical = true; 
            boolean isPossibleSquare = true; 

            for(int j = 0; j < 8; j++) {
                try {
                    int horizontalValue = Integer.parseInt(textArray[horizontalNumbers[j]].getText());
                    if(horizontalValue == n) {
                        isPossibleHorizontal = false;
                    }
                }
                catch(NumberFormatException ne) {}

                try {
                    int verticalValue = Integer.parseInt(textArray[verticalNumbers[j]].getText());
                    if(verticalValue == n) {
                        isPossibleVertical = false; 
                    }
                }
                catch(NumberFormatException ne) {}

                try {
                    int squareValue = Integer.parseInt(textArray[squareNumbers[j]].getText()); 
                    if(squareValue == n) {
                        isPossibleSquare = false; 
                    }
                }
                catch(NumberFormatException ne) {}

            }
            if(isPossibleHorizontal && isPossibleVertical && isPossibleSquare) {
                valueArray[i][n-1] = n; 
            }
            else {
                valueArray[i][n-1] = -1;  
            }
        }
        
    }
    public void textValueChanged(TextEvent e) {
        
        for(int i = 0; i < 81; i++) {
            if(textArray[i].getText().length() > 1) 
                textArray[i].setText(textArray[i].getText().charAt(0) + "");
        }
        
    }
    public int[] getHorizontalNumbers(int pos) { // pos = arraypos av ruta
        int[] horizontal = new int[8];
        for(int i = 0; i < 8; i++) {
            horizontal[i] = -1; 
        }
        int start = 0; 
        
        if(pos < 9) {
            start = 0; 
        }
        else if(pos < 18) {
            start = 9;
        }
        else if(pos < 27) {
            start = 18;
        }
        else if(pos < 36) {
            start = 27;
        }
        else if(pos < 45) {
            start = 36;
        }
        else if(pos < 54) {
            start = 45;
        }
        else if(pos < 63) {
            start = 54;
        }
        else if(pos < 72) {
            start = 63;
        }
        else {
            start = 72;
        }
        
        int i = 0; 
        while(i < 8) {
            if(start != pos) {
                horizontal[i] = start; 
                i++; 
            }
            start++; 
        }
            
        return horizontal; 
            
    }
    public int[] getVerticalNumbers(int pos) {
        int[] vertical = new int[8];
        for(int i = 0; i < 8; i++) {
            vertical[i] = -1; 
        }
        int before = 0; 
        
        if(pos < 9) {
            before = 0; 
        }
        else if(pos < 18) {
            before = 1; 
        }
        else if(pos < 27) {
            before = 2; 
        }
        else if(pos < 36) {
            before = 3; 
        }
        else if(pos < 45) {
            before = 4; 
        }
        else if(pos < 54) {
            before = 5; 
        }
        else if(pos < 63) {
            before = 6; 
        }
        else if(pos < 72) {
            before = 7; 
        }
        else {
            before = 8; 
        }
        
        //nummer före
        int start = 0; 
        if(before > 0) {
            for(int i = 0; i < before; i++) {
                vertical[i] = pos - (9*(i+1));
                start++; 
            }
        }   
        
        //nummer efter
        int k = 0; 
        for(int i = start; i < 8; i++) {
            vertical[k+start] = pos + (9*(k+1));
            k++; 
        }
        
        return vertical; 
    }
    public int[] getSquareNumbers(int pos) {
        int[] square = new int[8];
        for(int i = 0; i < 8; i++) {
            square[i] = -1; 
        }
        int start = 0; 
        
        if(pos == 0 || pos == 1 || pos == 2 
            || pos == 9 || pos == 10 || pos == 11
            ||pos == 18 || pos == 19 || pos == 20) {
            start = 0; 
        }
        else if(pos == 3 || pos == 4 || pos == 5
                || pos == 12 || pos == 13 || pos == 14
                || pos == 21 || pos == 22 || pos == 23) {
            start = 3; 
        }
        else if(pos == 6 || pos == 7 || pos == 8
                || pos == 15 || pos == 16 || pos == 17
                || pos == 24 || pos == 25 || pos == 26) {
            start = 6; 
        }
        else if(pos == 27 || pos == 28 || pos == 29
                || pos == 36 || pos == 37 || pos == 38
                || pos == 45 || pos == 46 || pos == 47) {
            start = 27; 
        }
        else if(pos == 30 || pos == 31 || pos == 32
                || pos == 39 || pos == 40 || pos == 41
                || pos == 48 || pos == 49 || pos == 50) {
            start = 30; 
        }
        else if(pos == 33 || pos == 34 || pos == 35
                || pos == 42 || pos == 43 || pos == 44
                || pos == 51 || pos == 52 || pos == 53) {
            start = 33; 
        }
        else if(pos == 54 || pos == 55 || pos == 56
                || pos == 63 || pos == 64 || pos == 65
                || pos == 72 || pos == 73 || pos == 74) {
            start = 54; 
        }
        else if(pos == 57 || pos == 58 || pos == 59
                || pos == 66 || pos == 67 || pos == 68
                || pos == 75 || pos == 76 || pos == 77) {
            start = 57; 
        }
        else {
            start = 60; 
        }
        
        int k = start; 
        for(int i = 0; i < 8; i++) {
            if(k != pos) {
                square[i] = k; 
            }
            else {
                i--;
            }
            if(k == start+2 || k == start+11) {
                k += 7; 
            }
            else {
                k++; 
            }
        }
        
        return square; 
    }
    
    public void keyPressed(KeyEvent ke) {
        if(hasClickedTextField) {
            if(ke.getKeyCode() == KeyEvent.VK_LEFT) {
                if(clickedTextfield == 0) {
                    textArray[80].requestFocus();
                }
                else {
                    textArray[clickedTextfield-1].requestFocus();
                }
            }
            if(ke.getKeyCode() == KeyEvent.VK_RIGHT) {
                if(clickedTextfield == 80) {
                    textArray[0].requestFocus();
                }
                else {
                    textArray[clickedTextfield+1].requestFocus();
                }
            }
            if(ke.getKeyCode() == KeyEvent.VK_UP) {
                if(clickedTextfield < 9) {
                    textArray[clickedTextfield+72].requestFocus();
                }
                else {
                    textArray[clickedTextfield-9].requestFocus();
                }
            }
            if(ke.getKeyCode() == KeyEvent.VK_DOWN) {
                if(clickedTextfield > 71) {
                    textArray[clickedTextfield-72].requestFocus();
                }
                else {
                    textArray[clickedTextfield+9].requestFocus();
                }
            }
        }
    }
    public void keyReleased(KeyEvent ke) {
        
    }
    public void keyTyped(KeyEvent ke) {
      
    }
    public void focusGained(FocusEvent fe) {
        hasClickedTextField = true; 
        
        for(int i = 0; i < 81; i++) {
            if(fe.getSource() == textArray[i]) {
                clickedTextfield = i; 
            }
        }
    }
    public void focusLost(FocusEvent fe) {
    }
}
class Ruta {
    
    int x = 0, y = 0; 
    int length = 40; 
    
    public Ruta(int xpos, int ypos) {
        
        x = xpos; 
        y = ypos; 
        
    }
    public void show(Graphics g) {
        
        g.drawLine(x, y, x + length, y);
        g.drawLine(x, y + length, x + length, y + length);
        g.drawLine(x, y, x, y + length);
        g.drawLine(x + length, y , x + length, y + length);
        
    }
    public void paintBlack(Graphics g) {
        
        g.fillRect(x, y, length, length);
        
    }
}