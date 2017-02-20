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

import com.naughter.aaplus.CAARiseTransitSetDetails;

public class RiseSet {

    final CAARiseTransitSetDetails rtsDetails;
    final double jd0;

    public RiseSet(CAARiseTransitSetDetails rtsDetails, double jd0) {
        this.rtsDetails = rtsDetails;
        this.jd0 = jd0;
    }

    public boolean isAlwaysAbove() {
        return (!isSetting() && !isRising()) && rtsDetails.getBTransitAboveHorizon();
    }
    public boolean isAbove() {
        return rtsDetails.getBTransitAboveHorizon();
    }
    public boolean isAlwaysBelow() {
        return (!isSetting() && !isRising()) && !rtsDetails.getBTransitAboveHorizon();
    }

    public boolean isSetting() {
        return rtsDetails.getBSetValid();
    }
    public boolean isRising() {
        return rtsDetails.getBRiseValid();
    }

    public double getRiseNum() {
        return jd0 + getRiseNum1d();
    }

    public double getSetNum() {
        return jd0 + getSetNum1d();
    }

    public void normalize() {
    }

    public double getTransitNum() {
        return jd0 + rtsDetails.getTransit() / 24.0;
    }

    public double getTransitNum1d() {
        return rtsDetails.getTransit() / 24.0;
    }

    public double getRiseNum1d() {
        return rtsDetails.getRise() / 24.0;
    }

    public double getSetNum1d() {
        return rtsDetails.getSet() / 24.0;
    }
}