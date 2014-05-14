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

import com.infine.perfmeter.*;
import org.acplt.oncrpc.OncRpcException;
import org.acplt.oncrpc.OncRpcProtocols;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class RstatPlugin implements Plugin {

    rstatClient clnt[] = null;
    ArrayList runners = new ArrayList();
    ArrayList rstats = new ArrayList();
    HashSet addresses = new HashSet();
    PerfmeterConfig perfmeterConfigurator = null;
    static ScaleComputer defaultScale = null;
    final GraphModelManager manager = GraphModelManager.getInstance();

    public void configurationChange() {
        String[] newHosts = perfmeterConfigurator.getHosts();
        ArrayList newAddresses = new ArrayList();
        for (int i = 0; i < newHosts.length; i++)
            try {
                newAddresses.add(InetAddress.getByName(newHosts[i]));
            } catch (Exception e) {
            }
        retainAll(newAddresses);
        // new capacity ?
        for (int i = 0; i < runners.size(); i++) {
            Runner runner = (Runner) runners.get(i);
            for (int j = 0; j < runner.getModels().size(); j++)
                runner.getModel(j).changeCapacity(perfmeterConfigurator.getSampleHistory());
        }
        // add new hosts
        rstatClient newClient = null;
        for (int i = 0; i < newHosts.length; i++)
            if ((newClient = addHost(newHosts[i])) != null)
                start(init(newClient));

        // sort hosts
        ArrayList newRunners = (ArrayList) runners.clone();
        for (int i = 0; i < runners.size(); i++) {
            Runner runner = (Runner) runners.get(i);
            newRunners.set(newAddresses.indexOf(runner.getClient().getClient().getClient().getHost()), runner);
        }
        runners = newRunners;
    }

    class Runner implements Runnable {
        ArrayList models = new ArrayList();
        ClientCommon clnt = null;
        int havedisk;
        boolean stop = false;
        boolean running = false;
        RstatTimeStampGraphModel timestampModel = null;

        Runner(rstatClient clnt) {
            this.clnt = new ClientCommon(clnt);
        }

        public void addModel(DefaultGraphModel model) {
            models.add(model);
        }

        ArrayList getModels() {
            return models;
        }

        public DefaultGraphModel getModel(int i) {
            return (DefaultGraphModel) models.get(i);
        }

        public RstatTimeStampGraphModel getTimestampModel() {
            return timestampModel;
        }

        public void setTimestampModel(RstatTimeStampGraphModel timestampModel) {
            this.timestampModel = timestampModel;
        }

        public void stop() {
            stop = true;
            //GraphModelManager manager = GraphModelManager.getInstance();

            for (int i = 0; i < models.size(); i++) {
                manager.remove((GraphModel) models.get(i));
            }
        }

        public ClientCommon getClient() {
            return clnt;
        }

        public void run() {
            if (running == true) {
                System.err.println("runner already running");
                return;
            }
            running = true;
            for (long start = System.currentTimeMillis(), inc = 0; !stop; inc++) {
                int sampleTime = (getPluginConfigurator().getSampleTime() * 1000);
                try {
                    clnt.setTimeOut(sampleTime);
                    Statscommon rs;
                    rs = clnt.RSTATPROC_STATS();
                    timestampModel.process(rs, havedisk);
                    for (int i = 0; i < models.size(); i++) {
                        RstatGraphModel model = (RstatGraphModel) models.get(i);
                        model.process(rs, havedisk);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    long end = sampleTime - (System.currentTimeMillis() - start) % sampleTime;
                    if (end < sampleTime / 2)
                        end += sampleTime;
                    if (end > 0)
                        try {
                            Thread.sleep(end);
                        } catch (InterruptedException e) {
                        }
                }

            }
            models = null;
            running = false;
        }
    }

    public RstatPlugin() {
    }

//  public RstatPlugin(String hosts[]) {
//    rstatClient clnt = null;
//    for (int i = 0; i < hosts.length; i++) {
//      if ((clnt = addHost(hosts[i])) != null)
//        init(clnt);
//    }
//  }

    private ScaleComputer getDefaultScaleComputer() {
        if (defaultScale == null) {
            synchronized (ScaleComputer.class) {
                if (defaultScale == null)
                    defaultScale = new ScaleComputer() {
                        public float getScale(float value) {
                            if (value <= 1)
                                return 1;
                            int tmp = (int) value;
                            int res = 0;
                            for (; tmp > 1; res++)
                                tmp = tmp >> 1;
                            return 2 << res;
                        }
                    };

            }
        }
        return defaultScale;
    }

    private Runner init(rstatClient clnt) {
        Runner runner = new Runner(clnt);
        String tmpHostName = clnt.getClient().getHost().getHostName();
        RstatGraphModel model = new RstatGraphModel(manager, tmpHostName, "cpu", 3, perfmeterConfigurator.getSampleHistory(), 0, 100) {
            Statscommon last = null;
            int maxs;
            final String footer1[] = new String[]{"", "sys", "usr", "iowait"};
            final String footer2[] = new String[]{"", "sys", "usr", "idle"};
            String[] footer = null;

            public String[] getFooter() {
                return footer;
            }

            public String[] getHeader() {
                int cpus = (int) Math.round(maxs / (100.0 * getDelay()));
                //System.out.println(maxs + "," + maxs / (100.0 * getDelay()));

                return new String[]{cpus + (cpus > 1 ? " cpus on " : " cpu on ") + hostName};

            }

            public void process(Statscommon rs, int havedisk) {
                if (rs == null) {
                    addValues(new double[]{0, 0, 0});
                    return;
                }
                if (footer == null)
                    footer = rs.version == 4 ? footer1 : footer2;
                if (last != null) {
                    int usr = rs.cp_time[0] - last.cp_time[0];
                    int sys = rs.cp_time[2] - last.cp_time[2];
                    int iow = rs.cp_time[1] - last.cp_time[1];
                    int idl = rs.cp_time[3] - last.cp_time[3];

                    maxs = usr + sys + iow + idl;

                    usr = (int) (usr * 100.0 / maxs);
                    sys = (int) (sys * 100.0 / maxs);
                    iow = (int) (iow * 100.0 / maxs);
                    addValues(new double[]{sys, usr, iow});

                }
                last = rs;
            }
        };
        runner.addModel(model);

        model = new RstatGraphModel(manager, tmpHostName, "load", 1, perfmeterConfigurator.getSampleHistory(), getDefaultScaleComputer()) {
            Statscommon last = null;
            String footer[] = new String[]{"load"};

            public String[] getFooter() {
                return footer;
            }

            public void process(Statscommon rs, int havedisk) {
                if (rs == null) {
                    addValues(new double[]{0});
                    return;
                }
                if (last != null) {
                    float load = rs.avenrun[0] / 256.0f;
                    addValues(new double[]{load});
                }
                last = rs;
            }
        };
        runner.addModel(model);

        model = new RstatGraphModel(manager, tmpHostName, "disk", 1, perfmeterConfigurator.getSampleHistory(), getDefaultScaleComputer()) {
            Statscommon last = null;
            int totalDisks = 0;
            int version = 0;
            final String[] footer = {"disk"};

            public String[] getFooter() {
                return version == 3 ? footer : new String[]{totalDisks + " disk" + (totalDisks > 1 ? "s" : "")};

            }

            public void process(Statscommon rs, int havedisk) {
                if (rs == null) {
                    addValues(new double[]{0});
                    return;
                }
                version = rs.version;
                if (last != null) {
                    int total = 0;
                    totalDisks = rs.dk_xfer.length;
                    for (int i = 0; i < rs.dk_xfer.length; i++) {
                        int diff = rs.dk_xfer[i] - last.dk_xfer[i];
                        total += diff;
                    }
                    addValues(new double[]{total / getDelay()});
                }
                last = rs;
            }
        };
        runner.addModel(model);

        model = new RstatGraphModel(manager, tmpHostName, "page", 2, perfmeterConfigurator.getSampleHistory(), getDefaultScaleComputer()) {
            Statscommon last = null;
            String footer[] = new String[]{"page", "out", "in"};

            public String[] getFooter() {
                return footer;
            }

            public void process(Statscommon rs, int havedisk) {
                if (rs == null) {
                    addValues(new double[]{0, 0});
                    return;
                }

                if (last != null) {

                    int vpgin = (rs.v_pgpgin - last.v_pgpgin) / getDelay();
                    int vpgout = (rs.v_pgpgout - last.v_pgpgout) / getDelay();
                    addValues(new double[]{vpgout, vpgin});
                }
                last = rs;
            }
        };
        runner.addModel(model);

        model = new RstatGraphModel(manager, tmpHostName, "context", 1, perfmeterConfigurator.getSampleHistory(), getDefaultScaleComputer()) {
            Statscommon last = null;
            String footer[] = new String[]{"context"};

            public String[] getFooter() {
                return footer;
            }

            public void process(Statscommon rs, int havedisk) {
                if (rs == null) {
                    addValues(new double[]{0});
                    return;
                }
                if (last != null) {

                    int v_swtch = (rs.v_swtch - last.v_swtch) / getDelay();
                    addValues(new double[]{v_swtch});
                }
                last = rs;
            }
        };
        runner.addModel(model);

        model = new RstatGraphModel(manager, tmpHostName, "swap", 2, perfmeterConfigurator.getSampleHistory(), getDefaultScaleComputer()) {
            Statscommon last = null;
            String footer[] = new String[]{"swap", "out", "in"};

            public String[] getFooter() {
                return footer;
            }

            public void process(Statscommon rs, int havedisk) {
                if (rs == null) {
                    addValues(new double[]{0, 0});
                    return;
                }

                if (last != null) {

                    int v_pswpin = (rs.v_pswpin - last.v_pswpin) / getDelay();
                    int v_pswpout = (rs.v_pswpout - last.v_pswpout) / getDelay();
                    addValues(new double[]{v_pswpout, v_pswpin});
                }
                last = rs;
            }
        };
        runner.addModel(model);

        model = new RstatGraphModel(manager, tmpHostName, "interrupts", 1, perfmeterConfigurator.getSampleHistory(), getDefaultScaleComputer()) {
            Statscommon last = null;
            String footer[] = new String[]{"interrupts"};

            public String[] getFooter() {
                return footer;
            }

            public void process(Statscommon rs, int havedisk) {
                if (rs == null) {
                    addValues(new double[]{0});
                    return;
                }
                if (last != null) {

                    int v_intr = rs.v_intr - last.v_intr;
                    addValues(new double[]{v_intr});
                }
                last = rs;
            }
        };
        runner.addModel(model);

        model = new RstatGraphModel(manager, tmpHostName, "packets", 2, perfmeterConfigurator.getSampleHistory(), getDefaultScaleComputer()) {
            Statscommon last = null;
            String footer[] = new String[]{"packets", "out", "in"};

            public String[] getFooter() {
                return footer;
            }

            public void process(Statscommon rs, int havedisk) {
                if (rs == null) {
                    addValues(new double[]{0, 0});
                    return;
                }

                if (last != null) {

                    int in = (rs.if_ipackets - last.if_ipackets) / getDelay();
                    int out = (rs.if_opackets - last.if_opackets) / getDelay();
                    addValues(new double[]{out, in});
                }
                last = rs;
            }
        };
        runner.addModel(model);

        model = new RstatGraphModel(manager, tmpHostName, "collisions", 1, perfmeterConfigurator.getSampleHistory(), getDefaultScaleComputer()) {
            Statscommon last = null;
            String footer[] = new String[]{"collisions"};

            public String[] getFooter() {
                return footer;
            }

            public void process(Statscommon rs, int havedisk) {
                if (rs == null) {
                    addValues(new double[]{0, 0});
                    return;
                }
                if (last != null) {

                    int if_collisions = (rs.if_collisions - last.if_collisions) / getDelay();
                    addValues(new double[]{if_collisions});
                }
                last = rs;
            }
        };
        runner.addModel(model);

        model = new RstatGraphModel(manager, tmpHostName, "errors", 2, perfmeterConfigurator.getSampleHistory(), getDefaultScaleComputer()) {
            Statscommon last = null;
            String footer[] = new String[]{"errors", "out", "in"};

            public String[] getFooter() {
                return footer;
            }

            public void process(Statscommon rs, int havedisk) {
                if (rs == null) {
                    addValues(new double[]{0, 0});
                    return;
                }
                if (last != null) {

                    int if_ierrors = (rs.if_ierrors - last.if_ierrors) / getDelay();
                    int if_oerrors = (rs.if_oerrors - last.if_oerrors) / getDelay();
                    addValues(new double[]{if_oerrors, if_ierrors});
                }
                last = rs;
            }
        };
        runner.addModel(model);
        runner.setTimestampModel(new RstatTimeStampGraphModel(manager, tmpHostName, perfmeterConfigurator.getSampleHistory()));
        runners.add(runner);
        return runner;
    }

    private void retainAll(ArrayList newAddresses) {
        addresses.retainAll(newAddresses);
        for (int i = rstats.size() - 1; i >= 0; i--) {
            rstatClient clnt = (rstatClient) rstats.get(i);
            if (!addresses.contains(clnt.getClient().getHost())) {
                rstats.remove(i);
                //hosts.remove(clnt);
                for (int j = runners.size() - 1; j >= 0; j--)
                    if (clnt.equals(((Runner) runners.get(j)).getClient().getClient()))
                        ((Runner) runners.remove(j)).stop();
            }
        }
    }

    public rstatClient addHost(String hostName) {
        rstatClient clnt = null;
        try {
            InetAddress address = InetAddress.getByName(hostName);
            if (!addresses.contains(address)) {
                rstatClient tmp = new rstatClient(address, OncRpcProtocols.ONCRPC_UDP);
                clnt = tmp;
                rstats.add(clnt);
                addresses.add(address);
                //hosts.put(clnt, hostName);
            }
        } catch (UnknownHostException e) {
        } catch (OncRpcException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }
        return clnt;
    }

    public Collection getHosts() {
        ArrayList tmpHosts = new ArrayList();
        for (int i = 0; i < runners.size(); i++)
            tmpHosts.add(((Runner) runners.get(i)).getClient().getClient().getClient().getHost().getHostName());
        return tmpHosts;
    }

    public void start() {
        for (int i = 0; i < runners.size(); i++)
            start((Runner) runners.get(i));

    }

    public void stop() {
        for (int i = 0; i < runners.size(); i++)
            ((Runner) runners.get(i)).stop();

    }

    private void start(Runner runner) {
        new Thread(runner).start();
    }

    public int getModelGroups() {
        return runners.size();
    }

    public int getModels(int group) {
        return ((Runner) runners.get(group)).models.size();
    }

    public GraphModel getModel(int group, int model) {
        return (GraphModel) ((Runner) runners.get(group)).models.get(model);
    }

    public int getDelay() {
        return getPluginConfigurator().getSampleTime();
    }

    public PluginConfigurator getPluginConfigurator() {
        if (perfmeterConfigurator == null) {
            perfmeterConfigurator = new PerfmeterConfig(this);
        }
        return perfmeterConfigurator;
    }

//  public RstatTimeStampGraphModel getTimestampModel() {
//    return timestampModel;
//  }

    public void setPluginConfigurator(PluginConfigurator pc) {
        if (pc instanceof PerfmeterConfig) {
            perfmeterConfigurator = (PerfmeterConfig) pc;
            perfmeterConfigurator.setPlugin(this);
        }
    }

    protected GraphModelManager getManager() {
        return manager;
    }

    public TimeStampGraphModel getTimeStamp(int group) {
        return (TimeStampGraphModel) ((Runner) runners.get(group)).getTimestampModel();
    }

}
