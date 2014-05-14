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

package com.infine.perfmeter.rstat;

import com.infine.perfmeter.DefaultGraphModel;
import com.infine.perfmeter.GraphModelManager;

abstract public class RstatGraphModel extends DefaultGraphModel {
    String hostName;

    RstatGraphModel(GraphModelManager manager, String hostName, String category, int series, int capacity, float minValue, float maxValue) {
        super(manager, category, series, capacity, minValue, maxValue);
        manager.register(hostName, this);
        this.hostName = hostName;
    }

    RstatGraphModel(GraphModelManager manager, String hostName, String category, int series, int capacity, float scales[]) {
        super(manager, category, series, capacity, scales);
        manager.register(hostName, this);
        this.hostName = hostName;
    }

    RstatGraphModel(GraphModelManager manager, String hostName, String category, int series, int capacity, ScaleComputer sc) {
        super(manager, category, series, capacity, sc);
        manager.register(hostName, this);
        this.hostName = hostName;
    }

    public String toString() {
        return "[" + hostName + "," + getCategory() + "," + getSamples() + "]";
    }

    abstract public void process(Statscommon rs, int havedisk);

    public String getHostName() {
        return hostName;
    }
}
