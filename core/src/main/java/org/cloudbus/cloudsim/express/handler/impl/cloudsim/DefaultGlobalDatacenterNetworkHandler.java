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

package org.cloudbus.cloudsim.express.handler.impl.cloudsim;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.NetworkTopology;
import org.cloudbus.cloudsim.express.exceptions.CloudSimExpressRuntimeException;
import org.cloudbus.cloudsim.express.exceptions.constants.ErrorConstants.ErrorCode;
import org.cloudbus.cloudsim.express.handler.ElementHandler;
import org.cloudbus.cloudsim.express.handler.helper.InterZoneNetworkHelper;
import org.cloudbus.cloudsim.express.handler.impl.BaseElementHandler;
import org.cloudbus.cloudsim.express.resolver.ExtensionsResolver;
import org.cloudbus.cloudsim.express.resolver.environment.definitions.model.GlobalDatacenterNetwork;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Creates and manages a CloudSim processing element.
 */
public class DefaultGlobalDatacenterNetworkHandler extends BaseElementHandler {

    private static Logger logger = LogManager.getLogger(DefaultGlobalDatacenterNetworkHandler.class);

    protected GlobalDatacenterNetwork globalDatacenterNetworkDescription;
    protected List<ElementHandler> zoneHandlers;

    @Override
    public void init(Object elementDescription, ExtensionsResolver extensionsResolver) {

        super.init(elementDescription, extensionsResolver);
        this.globalDatacenterNetworkDescription = (GlobalDatacenterNetwork) elementDescription;
    }

    @Override
    public void handle() {

        try {
            CreateZones();
            createInterZoneNetworkInCloudSim();
        } catch (Exception e) {
            // TODO: 2022-03-28 handler error.
            throw new CloudSimExpressRuntimeException(ErrorCode.UNKNOWN_ERROR,
                    "Please refer to the stacktrace", e);
        }
    }

    @Override
    public boolean isSimulated() {

        try {
            zoneHandlers.forEach(ElementHandler::isSimulated);
            getReturnedCloudletsInEachZone().forEach(this::performPostSimulationAnalysis);
            return true;
        } catch (Exception e) {
            // TODO: 2022-03-29 handle exception
            throw new CloudSimExpressRuntimeException(ErrorCode.UNKNOWN_ERROR,
                    "Please refer to the stacktrace", e);
        }
    }

    @Override
    public boolean canHandle(Object elementDescription) {

        return elementDescription instanceof GlobalDatacenterNetwork;
    }

    @Override
    public Object getProperty(String key) {

        return null;
    }

    protected void CreateZones() {
        zoneHandlers = new ArrayList<>();
        globalDatacenterNetworkDescription.getZones().forEach(zone -> {
            ElementHandler zoneHandler = getHandler(zone);
            zoneHandler.init(zone, getExtensionsResolver());
            zoneHandler.handle();
            zoneHandlers.add(zoneHandler);
            logger.atInfo()
                    .log("Zone created: " + zoneHandler.getProperty(DefaultZoneHandler.KEY_ZONE_NAME));
        });
    }

    protected int getZoneId(String zoneName) {

        for (ElementHandler handler : zoneHandlers) {
            String zoneNameOfHandler = (String) handler.getProperty(DefaultZoneHandler.KEY_ZONE_NAME);
            if (StringUtils.equalsIgnoreCase(zoneName, zoneNameOfHandler)) {
                return (Integer) handler.getProperty(DefaultZoneHandler.KEY_ZONE_ID);
            }
        }
        throw new CloudSimExpressRuntimeException(ErrorCode.UNKNOWN_ERROR, "Could not find a matching zone handler for " +
                "the zone: " + zoneName);
    }

    protected void createInterZoneNetworkInCloudSim() {

        logger.atInfo().log("Crating Inter Zone network...");
        File interZoneNetworkDescription = new File(
                globalDatacenterNetworkDescription.getInterZoneNetworkDescriptionFilePath());
        InterZoneNetworkHelper interZoneNetworkHelper = new InterZoneNetworkHelper(
                interZoneNetworkDescription);
        interZoneNetworkHelper.getLinks().forEach(link -> {
                    NetworkTopology.addLink(
                            getZoneId(link.getZoneALabel()),
                            getZoneId(link.getZoneBLabel()),
                            link.getBandwidth(),
                            link.getLatency()
                    );
                    logger.atDebug()
                            .log(link.getZoneALabel() + " - " + link.getZoneBLabel() + " -> " + "Latency: "
                                    + link.getLatency() + ", Bandwidth: " + link.getBandwidth());
                }
        );
        logger.atInfo().log(
                "Inter Zone network created with " + interZoneNetworkHelper.getLinks().size() + " links");
    }

    protected Map<String, List<Cloudlet>> getReturnedCloudletsInEachZone() {

        Map<String, List<Cloudlet>> cloudlets = new HashMap<>();
        this.zoneHandlers.forEach(z -> {
            Object returnedCloudletsProperty = z.getProperty(DefaultZoneHandler.RECEIVED_CLOUDLETS);
            if (returnedCloudletsProperty == null) {
                return;
            }
            cloudlets.put(
                    (String) z.getProperty(DefaultZoneHandler.KEY_ZONE_NAME),
                    (List<Cloudlet>) returnedCloudletsProperty
            );
        });
        return cloudlets;
    }

    protected void performPostSimulationAnalysis(String zoneName,
                                                 List<Cloudlet> returnedCloudletList) {

        // This method is available to perform any simulation specific analysis.
    }
}
