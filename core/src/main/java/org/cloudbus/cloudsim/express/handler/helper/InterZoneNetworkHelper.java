/*
 * CloudSim Express
 * Copyright (C) 2022  CloudsLab
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.cloudbus.cloudsim.express.handler.helper;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.cloudbus.cloudsim.express.exceptions.CloudSimExpressRuntimeException;
import org.cloudbus.cloudsim.express.exceptions.constants.ErrorConstants.ErrorCode;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Assist element handler to create the inter-zone network.
 */
public class InterZoneNetworkHelper {

    List<Link> links;

    public InterZoneNetworkHelper(File interZoneNetworkDetails) {

        try (Reader in = new FileReader(interZoneNetworkDetails, StandardCharsets.UTF_8)){
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);

            links = new ArrayList<>();
            for (CSVRecord record : records) {
                if (record.get(0).startsWith("#")) {
                    continue;
                }
                links.add(new Link(
                        getCSVColumnValue(record.get(0)),
                        getCSVColumnValue(record.get(1)),
                        Float.parseFloat(getCSVColumnValue(record.get(2))),
                        Float.parseFloat(getCSVColumnValue(record.get(3)))
                ));
            }
        } catch (Exception e) {
            // TODO: 2022-03-29 Handler error
            throw new CloudSimExpressRuntimeException(ErrorCode.UNKNOWN_ERROR,
                    "Please refer to the stacktrace", e);
        }
    }

    private String getCSVColumnValue(String columnValue) {

        // Need to remove BOM if present. Windows based tools may have added this character.
        String UT8ConvertedValue = new String(columnValue.strip().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        return UT8ConvertedValue.startsWith("\uFEFF") ? UT8ConvertedValue.substring(1) : UT8ConvertedValue;
    }

    public List<Link> getLinks() {
        return Collections.unmodifiableList(links);
    }

    public static class Link {

        private String zoneALabel;
        private String zoneBLabel;
        private float latency;
        private float bandwidth;

        public Link(String zoneALabel, String zoneBLabel, float latency, float bandwidth) {
            this.zoneALabel = zoneALabel.trim();
            this.zoneBLabel = zoneBLabel.trim();
            this.latency = latency;
            this.bandwidth = bandwidth;
        }

        public String getZoneALabel() {
            return zoneALabel;
        }

        public String getZoneBLabel() {
            return zoneBLabel;
        }

        public float getLatency() {
            return latency;
        }

        public float getBandwidth() {
            return bandwidth;
        }
    }
}
