/*
 * Copyright (c) 2004, Antoine Ramponi
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer. 
 * Redistributions in binary form must reproduce the above copyright notice, this list 
 * of conditions and the following disclaimer in the documentation and/or other 
 * materials provided with the distribution. 
 * Neither the names of its contributors may be 
 * used to endorse or promote products derived from this software without specific 
 * prior written permission. 
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, 
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, 
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY 
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE 
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED 
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.infine.perfmeter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class TimeControl extends JPanel {

    final int gap = 5;

    private Graphics2D graphic;

    private BufferedImage image;

    int width;

    final int height;

    SimpleGraphConfigurator graphConfig;

    PluginConfigurator pluginConfig;

    private float scale = 1;

    static private Font font = new Font("Lucida Sans", Font.PLAIN, 11);

    private int graphWidth;

    private int cursorOffSet = 0;

    private int cursorSize = 0;

    private int mousePressedPosition;

    private boolean dragCursor;
    private boolean resizeCursor;

    private final int resizeWidthSurface = 5;

    private MainFrame mainFrane = null;

    private int drawCounter = 0;

    /**
     * This is the default constructor
     */
    public TimeControl(MainFrame mainFrane, int height) {
        super();
        initialize();
        this.height = height;
        this.mainFrane = mainFrane;
    }

    private void setCursorOffSet(int offset) {
        if (offset < 0)
            cursorOffSet = 0;
        else
            cursorOffSet = offset;
    }

    private int getCursorPosition() {
        int x = toX(cursorOffSet);
        return x > gap ? x : gap;
    }

    private void commitChanges() {
        invalidate();
        repaint();
        final int tmpDrawCounter = ++drawCounter;
        GraphModelManager.getInstance().setOffSet(cursorOffSet);
        //System.out.println(cursorOffSet);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if (tmpDrawCounter == TimeControl.this.drawCounter)
                    mainFrane.draw();
            }

            ;
        });
    }

    /**
     * This method initializes this
     */
    private void initialize() {
        this.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent e) {
                // System.out.println("mouseMoved()"); // TODO Auto-generated Event stub mouseMoved()
                mousePressedPosition = e.getX() - getCursorPosition();
                if (mousePressedPosition > -resizeWidthSurface && mousePressedPosition < resizeWidthSurface)
                    setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                else
                    setCursor(Cursor.getDefaultCursor());

            }

            public void mouseDragged(java.awt.event.MouseEvent e) {
                if (resizeCursor) {
                    // System.out.println("toto");
                    int x = e.getPoint().x;
                    if (x < gap + mousePressedPosition)
                        x = gap + mousePressedPosition;
                    setCursorOffSet(toInternal(x - mousePressedPosition));
                    //scale = (cursorSize + mousePressedPosition) / cursorSize; 

                    commitChanges();

                } else if (dragCursor) {
                    // int x = (width - e.getPoint().x) *
                    // pluginConfig.getSampleHistory() / graphWidth;
                    int x = e.getPoint().x;
                    if (x < gap + mousePressedPosition)
                        x = gap + mousePressedPosition;
                    else if (x > width - gap + mousePressedPosition - cursorSize)
                        x = width - gap + mousePressedPosition - cursorSize;
                    // x = x < gap ? gap : (x > width - gap ? width - gap : x);
                    setCursorOffSet(toInternal(x - mousePressedPosition));
                    // System.out.println("x=" + x + ",w=" + width + ",size=" + cursorSize + ",tot=" + (width - gap - cursorSize) + ",cursor=" + getCursorPosition());

                    // dragCursor = false;
                    // System.out.println("mouseDragged()" + x + "," + getCursorOffSet() + "," + mousePressedPosition);
                    commitChanges();
                }
            }
        });
        this.addMouseListener(new java.awt.event.MouseAdapter() {


            public void mouseReleased(java.awt.event.MouseEvent e) {
                if (dragCursor || resizeCursor) {
                    setCursor(Cursor.getDefaultCursor());
                    dragCursor = resizeCursor = false;
                    return;
                }
                int x = getCursorPosition();
                mousePressedPosition = e.getX() - x;


                if (mousePressedPosition < -resizeWidthSurface && !dragCursor) {
                    if (x < gap + cursorSize)
                        x = gap;
                    else
                        x -= cursorSize + 1;
                } else if (mousePressedPosition > cursorSize && !dragCursor) {
                    x += cursorSize - 1;
                } else
                    return;
                setCursorOffSet(toInternal(x));
                // System.out.println("x=" + x + ",w=" + width + ",size=" + cursorSize + ",tot=" + (width - gap - cursorSize) + ",cursor=" + getCursorPosition());
                commitChanges();
                // System.out.println("mouseReleased()" + mousePressedPosition + "x=" + x);
            }

            public void mousePressed(java.awt.event.MouseEvent e) {

                mousePressedPosition = e.getX() - getCursorPosition();
                if (mousePressedPosition >= -resizeWidthSurface && mousePressedPosition <= resizeWidthSurface)
                    resizeCursor = true;
                else if (mousePressedPosition > resizeWidthSurface && mousePressedPosition <= cursorSize)
                    dragCursor = true;
                else
                    resizeCursor = dragCursor = false;
                // System.out.println("mousePressed()" + mousePressedPosition);
            }
        });

    }

    private int toInternal(int x) {
        //System.out.println("f(" + x + ")=" + ((width - /*cursorSize -*/ gap - x) * pluginConfig.getSampleHistory() / (width-2*gap)));
        return (width - cursorSize - gap - x) * pluginConfig.getSampleHistory() / (width - 2 * gap);
    }

    private int toX(int pos) {
        return width - gap - cursorSize - pos * (width - 2 * gap) / pluginConfig.getSampleHistory();
    }

    void setConfig(SimpleGraphConfigurator graphConfig, PluginConfigurator pluginConfig) {
        this.graphConfig = graphConfig;
        this.pluginConfig = pluginConfig;
    }

    public void paint(Graphics g) {
        Dimension d = getSize();
        if (d.width != width || d.height != height) {
            width = d.width;
            image = (BufferedImage) createImage(width, height);
            graphic = image.createGraphics();
            setFont(font);
            setSize(width, height);
        }
        graphic.setBackground(graphConfig.getColors().background);
        graphic.clearRect(0, 0, width, height);
        cursorSize = (int) (scale * (width - 2 * gap) * graphWidth / pluginConfig.getSampleHistory());
        // scale = model.getScale(model.getSamples() > graphW ?
        // model.getSamples() - graphW - 1: 0, model.getSamples());
        // scale = model.getManager().getScale(model, graphW,
        // config.isShareScale());
        // drawText();
        drawGrid();
        drawCursor();
        g.drawImage(image, 0, 0, this);
    }

    private void drawCursor() {
        Composite originalComposite = graphic.getComposite();
        graphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        graphic.setPaint(graphConfig.colors.text);
        graphic.fill(new Rectangle(getCursorPosition(), gap, cursorSize, getHeight() - gap));
        graphic.setComposite(originalComposite);
        //System.out.println("cursorSize=" + cursorSize + "(" + (toInternal(width - gap - cursorSize)) + "),cursorPosition=" + getCursorPosition()+ ",cursorOffSet=" + cursorOffSet + ",width=" + (width - 2 * gap) + ",rapport=" + ((float)cursorSize / (width - 2 * gap)) + ",graphWidth=" + graphWidth + ",samples=" + pluginConfig.getSampleHistory());
    }

    private void drawGrid() {

        graphic.setColor(graphConfig.getColors().grid);
        int step = (width - 2 * gap) * graphWidth / pluginConfig.getSampleHistory();
        // System.out.println(graphWidth);
        for (int j = width - gap; j >= gap; j -= step)
            graphic.drawLine(j, height, j, gap);

    }

    public void setGraphWidth(int graphWidth) {
        this.graphWidth = graphWidth - 2 * gap;
    }

}
