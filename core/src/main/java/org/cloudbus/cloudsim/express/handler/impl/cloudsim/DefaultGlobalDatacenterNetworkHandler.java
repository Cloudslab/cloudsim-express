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

package org.cloudbus.cloudsim.express.handler.impl.cloudsim;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.NetworkTopology;
import org.cloudbus.cloudsim.express.constants.ErrorConstants.ErrorCode;
import org.cloudbus.cloudsim.express.exceptions.CloudSimExpressRuntimeException;
import org.cloudbus.cloudsim.express.handler.ElementHandler;
import org.cloudbus.cloudsim.express.handler.impl.BaseElementHandler;
import org.cloudbus.cloudsim.express.handler.impl.CSVNetworkParser;
import org.cloudbus.cloudsim.express.resolver.ExtensionsResolver;
import org.cloudbus.cloudsim.express.resolver.environment.definitions.model.GlobalDatacenterNetwork;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class handles the {@link GlobalDatacenterNetwork} DTO.
 */
public class DefaultGlobalDatacenterNetworkHandler extends BaseElementHandler {

    private static final Logger logger = LogManager.getLogger(DefaultGlobalDatacenterNetworkHandler.class);

    protected GlobalDatacenterNetwork networkDTO;
    protected List<ElementHandler> zoneHandlers;

    @Override
    public void init(Object componentDTO, ExtensionsResolver resolver) {

        super.init(componentDTO, resolver);
        this.networkDTO = (GlobalDatacenterNetwork) componentDTO;
    }

    @Override
    public void handle() {

        createZones();
        createInterZoneNetworkInCloudSim();
    }

    @Override
    public boolean isSimulated() {

        zoneHandlers.forEach(ElementHandler::isSimulated);
        return true;
    }

    @Override
    public boolean canHandle(Object componentDTO) {

        return componentDTO instanceof GlobalDatacenterNetwork;
    }

    @Override
    public Object getProperty(String key) {

        return null;
    }

    protected void createZones() {

        zoneHandlers = new ArrayList<>();
        networkDTO.getZones().forEach(zone -> {
            ElementHandler zoneHandler = getHandler(zone);
            zoneHandler.init(zone, getResolver());
            zoneHandler.handle();
            zoneHandlers.add(zoneHandler);
            logger.atInfo().log("Zone created: " + zoneHandler.getProperty(DefaultZoneHandler.KEY_ZONE_NAME));
        });
    }

    protected int getZoneId(String zoneName) {

        for (ElementHandler handler : zoneHandlers) {
            String zoneNameFromHandler = (String) handler.getProperty(DefaultZoneHandler.KEY_ZONE_NAME);
            if (StringUtils.equalsIgnoreCase(zoneName, zoneNameFromHandler)) {
                return (Integer) handler.getProperty(DefaultZoneHandler.KEY_ZONE_ID);
            }
        }
        throw new CloudSimExpressRuntimeException(ErrorCode.MISSING_HANDLER, "Zone handler missing for " + zoneName);
    }

    protected void createInterZoneNetworkInCloudSim() {

        logger.atInfo().log("Crating Inter Zone network...");
        File networkInfo = new File(networkDTO.getInterZoneNetworkDescriptionFilePath());
        CSVNetworkParser parser = new CSVNetworkParser(networkInfo);
        parser.getLinks().forEach(link -> {
                    NetworkTopology.addLink(
                            getZoneId(link.getNodeALabel()),
                            getZoneId(link.getNodeBLabel()),
                            link.getBandwidth(),
                            link.getLatency()
                    );
                    logger.atDebug().log(link.getNodeALabel() + " - " + link.getNodeBLabel() + " -> " + "Latency: "
                            + link.getLatency() + ", Bandwidth: " + link.getBandwidth());
                }
        );
        logger.atInfo().log("Inter Zone network created with " + parser.getLinks().size() + " links");
    }

    protected Map<String, List<Cloudlet>> getCompletedCloudlets() {

        Map<String, List<Cloudlet>> cloudlets = new HashMap<>();
        this.zoneHandlers.forEach(z -> {
            Object completedCloudlets = z.getProperty(DefaultZoneHandler.RECEIVED_CLOUDLETS);
            if (completedCloudlets == null) {
                return;
            }
            cloudlets.put(
                    (String) z.getProperty(DefaultZoneHandler.KEY_ZONE_NAME),
                    (List<Cloudlet>) completedCloudlets
            );
        });
        return cloudlets;
    }
}
