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

import com.infine.perfmeter.rstat.ScaleComputer;

public class DefaultGraphModel extends AbstractGraphModel {

    float scales[] = null;
    String header[] = null;
    String footer[] = null;
    double data[][] = null;
    double max[] = null;
    float globalMax;
    String legend[] = null;
    int series;
    int capacity;
    int size;
    int total;
    float minValue = -1.0f;
    float maxValue = -1.0f;
    //boolean computeMinMax = true;
    int lastFrom;
    int lastTo;
    float lastScale;
    ScaleComputer sc = null;
    boolean isScaleCached = false;

    public DefaultGraphModel(GraphModelManager manager, String category, int series, int capacity) {
        super(manager, category);
        data = new double[series][capacity];
        this.series = series;
        this.capacity = capacity;
    }

    public DefaultGraphModel(GraphModelManager manager, String category, int series, int capacity, float minValue, float maxValue) {
        this(manager, category, series, capacity);
        setScale(minValue, maxValue);
        //computeMinMax = false;
    }

    public DefaultGraphModel(GraphModelManager manager, String category, int series, int capacity, float scales[]) {
        this(manager, category, series, capacity);
        this.scales = scales;
        //computeMinMax = false;
    }

    public DefaultGraphModel(GraphModelManager manager, String category, int series, int capacity, ScaleComputer sc) {
        this(manager, category, series, capacity);
        this.sc = sc;
    }

    public synchronized void changeCapacity(int newCapacity) {
        if (newCapacity == capacity)
            return;
        for (int i = 0; i < series; i++) {
            double tmp[] = new double[newCapacity];
            System.arraycopy(data[i], Math.max(0, size - newCapacity), tmp, 0, Math.min(size, newCapacity));
            data[i] = tmp;
        }
        capacity = newCapacity;
        if (size >= capacity)
            size = capacity - 1;
    }

    public synchronized void addValues(double values[]) {
        if (size == capacity - 1) {
            for (int i = 0; i < series; i++) {
                System.arraycopy(data[i], 1, data[i], 0, size);
                data[i][size] = values[i];
            }
        } else {
            //System.out.println(last);
            for (int i = 0; i < series; i++) {
                data[i][size] = values[i];
            }
            size++;
        }
        total++;
        isScaleCached = false;
        fireGraphDataChanged();
    }

    public String[] getHeader() {
        return header;
    }

    public String[] getFooter() {
        return footer;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getSamples() {
        return size;
    }

    public int getSeries() {
        return series;
    }

    public String getSerieLegend(int serieIndex) {
        return legend != null ? legend[serieIndex] : null;
    }

    public double[] getSerie(int serieIndex) {
        return data[serieIndex];
    }

    public double getMaxSample(int serieIndex) {
        return max[serieIndex];
    }

    public float getMax() {
        return globalMax;
    }

    public void setFooter(String string[]) {
        footer = string;
    }

    public void setHeader(String string[]) {
        header = string;
    }

    public void setLegend(int serieIndex, String legend) {
        this.legend[serieIndex] = legend;
    }

    public void setScale(float minValue, float maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public long getTotal() {
        return total;
    }

    public float getScale() {
        return maxValue - minValue;
    }

    public float getScale(int from, int to) {
        float localMax = getScale();
        float tmp;
        if (localMax != 0.0f)
            return localMax;
        if (isScaleCached && from == lastFrom && to == lastTo)
            return lastScale;
        for (int i = from; i < to; i++) {
            tmp = 0.0f;
            for (int j = 0; j < series; j++)
                tmp += data[j][i];
            if (tmp > localMax)
                localMax = tmp;
        }
        if (sc != null)
            localMax = sc.getScale(localMax);
        else if (scales != null)
            for (int i = 0; i < scales.length; i++)
                if (localMax > scales[i]) {
                    localMax = i == 0 ? localMax : scales[i - 1];
                    break;
                }
        isScaleCached = true;
        lastFrom = from;
        lastTo = to;
        return lastScale = localMax;
    }

}
