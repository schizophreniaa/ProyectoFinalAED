package interfac;

import javax.swing.*;
import java.awt.*;
import btree.*;
import estructura.*;

public class BTreeDrawPanel extends JPanel {

    private BTree<?> arbol;

    public BTreeDrawPanel(BTree<?> arbol) {
        this.arbol = arbol;
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(1200, 800)); // 👈 área grande de dibujo
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (arbol != null && arbol.root != null) {
            drawNode(g, arbol.root, getWidth() / 2, 50, getWidth() / 4);
        }
    }

    private void drawNode(Graphics g, NodeBTree<?> node, int x, int y, int hGap) {
        int boxWidth = 40;
        int boxHeight = 30;
        int totalWidth = node.count * boxWidth;

        // Dibujar claves
        for (int i = 0; i < node.count; i++) {
            int boxX = x - totalWidth / 2 + i * boxWidth;
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(boxX, y, boxWidth, boxHeight);
            g.setColor(Color.BLACK);
            g.drawRect(boxX, y, boxWidth, boxHeight);

            Object key = node.keys.get(i);
            g.drawString(String.valueOf(key), boxX + 10, y + 20);
        }

        // Dibujar hijos si no es hoja
        if (!node.isLeaf()) {
            for (int i = 0; i <= node.count; i++) {
                NodeBTree<?> child = node.childs.get(i);
                if (child != null) {
                    int childX;
                    if (node.count == 1) {
                        childX = (i == 0) ? x - hGap : x + hGap;
                    } else {
                        childX = x - hGap + (2 * hGap * i / node.count);
                    }
                    int childY = y + 70;

                    g.setColor(Color.BLACK);
                    g.drawLine(x, y + boxHeight, childX, childY);

                    drawNode(g, child, childX, childY, hGap / 2);
                }
            }
        }

        // Dibujar puntero `next` en nodos hoja
        if (node.isLeaf() && node.getNext() != null) {
            int fromX = x + totalWidth / 2;
            int fromY = y + boxHeight / 2;
            int toX = fromX + 50;
            int toY = fromY;

            g.setColor(Color.BLUE);
            g.drawLine(fromX, fromY, toX, toY);
            g.drawString("→", fromX + 20, fromY - 5);
        }
    }
}
