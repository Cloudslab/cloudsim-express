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

package org.cloudbus.cloudsim.express.handler.impl;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.cloudbus.cloudsim.express.handler.ElementHandler;
import org.cloudbus.cloudsim.express.resolver.ExtensionsResolver;
import org.cloudbus.cloudsim.express.resolver.environment.definitions.model.Extension;
import org.cloudbus.cloudsim.express.resolver.environment.definitions.model.Property;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implement common functionalities of most elements.
 */
public abstract class BaseElementHandler implements ElementHandler {

    public static final String AUTOGENERATE_ID_SYMBOL = "-1";

    protected ExtensionsResolver extensionsResolver;

    @Override
    public void init(Object elementDescription, ExtensionsResolver extensionsResolver) {

        this.extensionsResolver = extensionsResolver;
    }

    protected ElementHandler getHandler(Object elementDescription) {

        return extensionsResolver.getElementHandlers().stream()
                .filter(handler -> handler.canHandle(elementDescription)).findFirst().get();
    }

    protected ExtensionsResolver getExtensionsResolver() {

        return this.extensionsResolver;
    }

    protected String getPropertyValue(List<Property> props, String key) {

        return props.stream().filter(p -> key.equals(p.getKey())).findFirst().get().getValue();
    }

    protected boolean isIdIncrementing(String id) {
        return AUTOGENERATE_ID_SYMBOL.equals(id);
    }

    protected List<Pair<String, String>> getExtensionProperties(Extension extension) {
        if (extension.getExtensionProperties() == null
                || extension.getExtensionProperties().size() == 0) {
            return null;
        }
        return extension.getExtensionProperties().stream()
                .map(p -> new ImmutablePair<>(p.getKey(), p.getValue())).collect(
                        Collectors.toList());
    }
}
