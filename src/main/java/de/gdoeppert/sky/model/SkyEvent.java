package de.gdoeppert.sky.model;

/* Android App Sky 
 * 
 * (c) Gerhard Döppert
 * 
 * SkyReigen.apk is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * SkyReigen.apk is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */

import java.util.Calendar;

public class SkyEvent {

    public String event;
    public Calendar date;
    public String info;
    double jd;

    public SkyEvent(String ev, Calendar cal, double jd) {
        event = ev;
        date = cal;
        this.jd = jd;
        info = null;
    }

    public SkyEvent(String ev, Calendar d, double jd, String inf) {
        event = ev;
        date = d;
        this.jd = jd;
        info = inf;
    }
}
