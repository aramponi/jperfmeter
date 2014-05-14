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

import java.util.Hashtable;
import java.util.Vector;

public class GraphModelManager {

    private static GraphModelManager graphModelManager = new GraphModelManager();

    private Hashtable categories = new Hashtable();
    private Vector models = new Vector();

    private int offset;

    public static GraphModelManager getInstance() {
        return graphModelManager;
    }

    public void register(String category, GraphModel graphModel) {
        if (category == null)
            return;
        Vector tmpModels = (Vector) categories.get(category);
        if (tmpModels == null)
            categories.put(category, tmpModels = new Vector());
        if (!tmpModels.contains(graphModel))
            tmpModels.add(graphModel);
        if (!models.contains(graphModel))
            models.add(graphModel);
    }

    public Vector getModels(String category) {
        return (Vector) categories.get(category);
    }

    public Vector getModels() {
        return models;
    }

    public void remove(GraphModel model) {
        Vector tmpModels = (Vector) categories.get(model.getCategory());
        if (tmpModels != null)
            tmpModels.remove(model);
        models.remove(model);
    }

    public int getOffSet() {
        return offset;
    }

    public void setOffSet(int offset) {
        this.offset = offset;
    }

    public float getScale(GraphModel model, int graphW, boolean useCategoryScale) {
        Vector tmpModels = (Vector) categories.get(model.getCategory());
        int samples = (model.getSamples() > offset ? model.getSamples() - offset : 0);
        if (!useCategoryScale || tmpModels == null)
            return model.getScale(samples > graphW ? samples - graphW - 1 : 0, samples);
        float max = 0;
        for (int i = 0; i < tmpModels.size(); i++) {
            GraphModel tmpModel = ((GraphModel) tmpModels.get(i));
            samples = (tmpModel.getSamples() > offset ? tmpModel.getSamples() - offset : 0);
            float tmp = tmpModel.getScale(samples > graphW ? samples - graphW - 1 : 0, samples);
            if (tmp > max)
                max = tmp;
        }
        return max;
    }

}
