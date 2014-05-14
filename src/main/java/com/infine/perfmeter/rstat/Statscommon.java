
package com.infine.perfmeter.rstat;

public class Statscommon {

    public Statscommon(statstime stat) {
        cp_time = stat.cp_time;
        dk_xfer = stat.dk_xfer;
        v_pgpgin = stat.v_pgpgin;
        v_pgpgout = stat.v_pgpgout;
        if_ipackets = stat.if_ipackets;
        if_opackets = stat.if_opackets;
        avenrun = stat.avenrun;
        v_swtch = stat.v_swtch;
        v_intr = stat.v_intr;
        if_collisions = stat.if_collisions;
        version = 3;
    }

    public Statscommon(statsvar stat) {
        cp_time = stat.cp_time;
        dk_xfer = stat.dk_xfer;
        v_pgpgin = stat.v_pgpgin;
        v_pgpgout = stat.v_pgpgout;
        if_ipackets = stat.if_ipackets;
        if_opackets = stat.if_opackets;
        if_ierrors = stat.if_ierrors;
        if_oerrors = stat.if_oerrors;
        avenrun = stat.avenrun;
        v_swtch = stat.v_swtch;
        v_intr = stat.v_intr;
        v_pswpin = stat.v_pswpin;
        v_pswpout = stat.v_pswpout;
        if_collisions = stat.if_collisions;
        version = 4;
    }

    public int[] cp_time;
    public int[] dk_xfer;
    public int v_pgpgin;
    public int v_pgpgout;
    public int v_pswpin;
    public int v_pswpout;
    public int v_intr;
    public int if_ipackets;
    public int if_ierrors;
    public int if_opackets;
    public int if_oerrors;
    public int if_collisions;
    public int v_swtch;
    public int[] avenrun;
    public rstat_timeval boottime;
    public rstat_timeval curtime;
    public int version;

}