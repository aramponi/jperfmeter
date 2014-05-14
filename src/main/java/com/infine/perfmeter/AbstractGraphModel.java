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

import javax.swing.event.EventListenerList;

public abstract class AbstractGraphModel implements GraphModel {

    protected EventListenerList listenerList = new EventListenerList();
    private final String category;
    private final GraphModelManager manager;

    protected AbstractGraphModel(GraphModelManager manager, String category) {
        this.category = category;
        this.manager = manager;
        manager.register(category, this);
    }

    public void addTableModelListener(GraphModelListener l) {
        listenerList.add(GraphModelListener.class, l);
    }

    public void removeTableModelListener(GraphModelListener l) {
        listenerList.remove(GraphModelListener.class, l);
    }

    public void fireGraphDataChanged() {
        fireGraphChanged(new GraphModelEvent(this));
    }

    public void fireGraphChanged(GraphModelEvent e) {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == GraphModelListener.class) {
                ((GraphModelListener) listeners[i + 1]).graphChanged(e);
            }
        }
    }

    public String getCategory() {
        return category;
    }

    public GraphModelManager getManager() {
        return manager;
    }

}
