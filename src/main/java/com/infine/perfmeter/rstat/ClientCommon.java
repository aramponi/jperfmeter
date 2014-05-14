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

import org.acplt.oncrpc.OncRpcException;
import org.acplt.oncrpc.OncRpcTimeoutException;

public class ClientCommon {
    boolean version4 = true;
    rstatClient client = null;

    public ClientCommon(rstatClient client) {
        this.client = client;
    }

    public int getTimeOut() {
        return client.getTimeOut();
    }

    public void setTimeOut(int timeout) {
        client.setTimeOut(timeout);
    }

    public Statscommon RSTATPROC_STATS() {
        Statscommon rs = null;
        if (version4) {
            try {
                rs = new Statscommon(client.RSTATPROC_STATS_4());
            } catch (OncRpcException e) {
                //e.printStackTrace();
                if (e instanceof OncRpcTimeoutException) {
                    version4 = true;
                } else
                    version4 = false;
            }
        }
        if (!version4) {
            try {
                rs = new Statscommon(client.RSTATPROC_STATS_3());
            } catch (OncRpcException e) {
                //e.printStackTrace();                
            }
        }
        return rs;
    }

    public rstatClient getClient() {
        return client;
    }

}
