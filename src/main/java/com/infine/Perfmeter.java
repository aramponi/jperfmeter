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

package com.infine;

import com.infine.perfmeter.MainFrame;
import com.infine.perfmeter.PluginConfigurator;
import com.infine.perfmeter.SimpleGraphConfigurator;
import com.infine.perfmeter.Util;
import com.infine.perfmeter.csv.StreamWriterConfigurator;
import com.infine.perfmeter.rstat.PerfmeterConfig;
import com.infine.perfmeter.rstat.RstatPlugin;

import java.io.File;
import java.io.IOException;


public class Perfmeter {

    public static void main(String[] args) {
        /* parsing command line */
        RstatPlugin plugin = new RstatPlugin();
        SimpleGraphConfigurator graphConfig = (SimpleGraphConfigurator) Util.newInstance(SimpleGraphConfigurator.class);
        StreamWriterConfigurator streamConfig = (StreamWriterConfigurator) Util.newInstance(StreamWriterConfigurator.class);
        boolean initOk = false;
        MainFrame frame = null;
        for (int i = 0; i < args.length; i++) {
            if (args.length > i + 1 && ("-f".equals(args[i]) || "--file".equals(args[i])) && args[i + 1] != null) {
                frame = new MainFrame(plugin, graphConfig, streamConfig);
                try {
                    File file = new File(args[i + 1]);
                    frame.loadConfiguration(file);
                } catch (Exception e) {
                    try {
                        File file = new File(Util.getPreferenceDir(), args[i + 1]);
                        frame.loadConfiguration(file);
                        initOk = true;
                    } catch (IOException e1) {
                        printUsage("Can't open configuration file " + args[i + 1]);
                        System.exit(0);
                    } catch (ClassNotFoundException e1) {
                        e1.printStackTrace();
                    }
                }
            } else if ("--help".equals(args[i]) || "-h".equals(args[i])) {
                printUsage();
                System.exit(1);
            } else if ("--version".equals(args[i]) || "-v".equals(args[i])) {
                printVersion();
                System.exit(1);
            }
        }
        if (!initOk) {
            PluginConfigurator pc = (PluginConfigurator) Util.newInstance(PerfmeterConfig.class);
            if (args.length > 0)
                pc.initFromArgs(args);
            plugin.setPluginConfigurator(pc);
            frame = new MainFrame(plugin, graphConfig, streamConfig);
        }

        frame.setVisible(true);
        if (plugin.getHosts().size() == 0)
            frame.openCongifurator();

    }

    private static void printVersion() {
        System.out.println("JPerfmeter version " + Util.getVersion());
    }

    private static void printUsage() {
        printUsage(null);
    }

    private static void printUsage(String mess) {
        if (mess != null)
            System.out.println(mess);
        printVersion();
        System.out.println("jperfmeter.[bat|sh] [options] [hosts] ");
        System.out.println("\t-h | --help                 : display this message");
        System.out.println("\t-f | --file <config file>   : read configuration from file");
    }
}
