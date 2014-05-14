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

import java.awt.*;
import java.awt.image.BufferedImage;

public class SimpleGraph extends Component implements GraphModelListener {


    private int graphColumn = 10;
    private int graphRow = 10;

    private int graphH;
    private int graphW;
    private int graphY;

    final private int graphX = 5;
    private int descent;
    private float ascent;
    private Graphics2D graphic;
    private BufferedImage image;
    int width, height;
    private float scale = 0;
    static private Font font = new Font("Lucida Sans", Font.PLAIN, 11);

    private GraphModel model = null;
    private SimpleGraphConfigurator config = null;
    private TimeStampGraphModel timestamp = null;

    private FontMetrics fm;

    public SimpleGraph() {
        this(null, null, null);
    }

    public SimpleGraph(GraphModel model, TimeStampGraphModel timestamp, SimpleGraphConfigurator config) {
        this.model = model;
        this.config = config;
        this.timestamp = timestamp;
        model.addTableModelListener(this);
    }

    public void stop() {
        model.removeTableModelListener(this);
    }

    private void drawText() {
        graphic.setColor(config.getColors().text);

        String str;

        if (model.getHeader() != null && model.getHeader().length != 0) {
            str = "";
            for (int i = 0; i < model.getHeader().length; i++)
                str += model.getHeader()[i];
            graphic.drawString(str, 4.0f, ascent + 0.5f);
        }
        int lastValue = 0;
        for (int i = 0; i < model.getSeries(); i++)
            lastValue += model.getSamples() > 0 ? model.getSerie(i)[model.getSamples() - 1] : 0;

        if (model.getFooter() != null && model.getFooter().length != 0) {
            int offset = 4;
            for (int i = 0; i < model.getFooter().length; i++) {
                if (i == 0)
                    graphic.setColor(config.getColors().text);
                else
                    graphic.setColor(config.getColors().getColorsAsArray()[model.getFooter().length - 1][i - 1]);
                graphic.drawString(model.getFooter()[i], offset, height - descent);
                offset += fm.charsWidth(model.getFooter()[i].toCharArray(), 0, model.getFooter()[i].length()) + 2;
            }
        }
        str = Integer.toString(lastValue) + "/" + (scale % 1024 == 0 ? (int) (scale / 1024) + "k" : Integer.toString((int) scale));
        int length = fm.charsWidth(str.toCharArray(), 0, str.length());
        graphic.clearRect(graphW - length, graphH, graphW, height);
        graphic.setColor(config.getColors().text);
        graphic.drawString(str, graphW - length, height - descent);

    }

    private void computeSize() {
        int factor = 1;
        if (model.getHeader() != null && model.getHeader().length != 0)
            factor++;
        fm = graphic.getFontMetrics(font);
        ascent = fm.getAscent();
        descent = fm.getDescent();

        float ssH = ascent + descent;
        float remainingHeight = height - (ssH * factor) - 0.5f;

        graphY = (int) (model.getHeader() != null && model.getHeader().length != 0 ? ssH : 0);
        graphW = width - 2 * graphX;
        graphH = (int) remainingHeight;
    }

    private void drawGrid() {
        graphic.setColor(config.getColors().grid);
        int offset = model.getManager().getOffSet();
        for (int j = graphY; j <= graphH + graphY; j += graphRow)
            graphic.drawLine(graphX, j, graphX + graphW, j);

        int columnInc = (int) (model.getTotal() - offset) % graphColumn;

        for (int j = graphX + graphW - columnInc; j >= graphX; j -= graphColumn)
            graphic.drawLine(j, graphY, j, graphY + graphH);
    }

    private void drawGraph() {
        int offset = model.getManager().getOffSet();
        int ptNum = model.getSamples() > offset ? model.getSamples() - offset : 0;
        int series = model.getSeries();
        double[] timestampSeries = timestamp.getSerie(1);
        for (int j = graphX + graphW, k = ptNum - 1; j >= graphX && k >= 0; k--, j--) {
            // no data to display => maybe data were not available
            if (timestampSeries[k] == 0) {
                graphic.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.80f));
                graphic.setColor(Color.white);
                graphic.drawLine(j, graphY + graphH, j, graphY);
                graphic.setComposite(AlphaComposite.SrcOver);
                continue;
            }
            int from = graphY + graphH;
            int to = from;
            for (int z = 0; z < series; z++) {
                to = from - (int) (graphH * (model.getSerie(z)[k] / scale));
                if (from > to) {
                    graphic.setColor(config.getColors().getColorsAsArray()[series][z]);
                    graphic.drawLine(j, from, j, to);
                    from -= from - to;
                }
            }
        }

    }

    public void setFont(Font font) {
        SimpleGraph.font = font;
        graphic.setFont(font);
    }

    public void paint(Graphics g) {
        Dimension d = getSize();
        if (d.width != width || d.height != height) {
            width = d.width;
            height = d.height;
            image = (BufferedImage) createImage(width, height);
            graphic = image.createGraphics();
            setFont(font);
            computeSize();
        }
        graphic.setBackground(config.getColors().background);
        graphic.clearRect(0, 0, width, height);
        //scale = model.getScale(model.getSamples() > graphW ? model.getSamples() - graphW - 1: 0, model.getSamples());
        scale = model.getManager().getScale(model, graphW, config.isShareScale());
        drawText();
        drawGrid();
        drawGraph();
        g.drawImage(image, 0, 0, this);
    }

    public void graphChanged(GraphModelEvent e) {
        invalidate();
        repaint();
    }

}
