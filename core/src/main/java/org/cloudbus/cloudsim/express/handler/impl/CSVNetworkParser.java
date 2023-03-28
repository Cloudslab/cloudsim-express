/*
 * cloudsim-express
 * Copyright (C) 2023 CLOUDS Lab
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

package org.cloudbus.cloudsim.express.handler.impl;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.cloudbus.cloudsim.express.constants.ErrorConstants.ErrorCode;
import org.cloudbus.cloudsim.express.exceptions.CloudSimExpressRuntimeException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The CSVNetworkParser class represents a parser that reads network link information from a CSV file, and then
 * builds {@link Link} objects to represent the network with its links.
 * <p/>
 * <p>CSV file format:</p>
 * <ul>
 *     <li>First two columns are node labels</li>
 *     <li>Third column is the link latency, in seconds</li>
 *     <li>Fourth column is the link bandwidth</li>
 * </ul>
 * <p>
 * Example Usage:
 * <pre>
 * {@code
 * CSVNetworkParser parser = new CSVNetworkParser(networkInfoCSVFile);
 * List<Link> = parser.getLinks();
 * }
 * </pre>
 * The {@link Link} class can be used as a DTO for a network link information.
 *
 * @see Link
 */
public class CSVNetworkParser {

    List<Link> links;

    public CSVNetworkParser(File networkInfoCSV) {

        try (Reader in = new FileReader(networkInfoCSV, StandardCharsets.UTF_8)) {
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
        } catch (IOException e) {
            throw new CloudSimExpressRuntimeException(ErrorCode.FILE_OPERATION_ERROR, "Could not read: "
                    + networkInfoCSV.getAbsolutePath(), e);
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

    /**
     * The Link class represents a network link.
     * <p/>
     * <p>Link Structure<p/>
     * <ul>
     *     <li>Node A</li>
     *     <li>Node B</li>
     *     <li>Latency of the link between A and B</li>
     *     <li>Bandwidth of the link between A and B</li>
     * </ul>
     */
    public static class Link {

        private final String nodeALabel;
        private final String nodeBLabel;
        private float latency;
        private float bandwidth;

        public Link(String nodeALabel, String nodeBLabel, float latency, float bandwidth) {
            this.nodeALabel = nodeALabel.trim();
            this.nodeBLabel = nodeBLabel.trim();
            this.latency = latency;
            this.bandwidth = bandwidth;
        }

        public void setLatency(float latency) {
            this.latency = latency;
        }

        public void setBandwidth(float bandwidth) {
            this.bandwidth = bandwidth;
        }

        public String getNodeALabel() {
            return nodeALabel;
        }

        public String getNodeBLabel() {
            return nodeBLabel;
        }

        public float getLatency() {
            return latency;
        }

        public float getBandwidth() {
            return bandwidth;
        }
    }
}
