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

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.cloudbus.cloudsim.express.constants.ScriptConstants;
import org.cloudbus.cloudsim.express.handler.ElementHandler;
import org.cloudbus.cloudsim.express.resolver.ExtensionsResolver;
import org.cloudbus.cloudsim.express.resolver.environment.definitions.model.Extension;
import org.cloudbus.cloudsim.express.resolver.environment.definitions.model.Property;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The BaseElementHandler is an abstract class that implements common functionalities used across most classes that
 * implements {@link ElementHandler}.
 * <p>
 * Such as storing the {@link ExtensionsResolver}, Utility methods to validate IDs, and managing properties.
 * <p>
 * <p/>
 * <p>
 * Usage:
 * Any new implementation of {@link ElementHandler} is advised to extend the {@link BaseElementHandler}.
 */
public abstract class BaseElementHandler implements ElementHandler {

    protected ExtensionsResolver resolver;

    @Override
    public void init(Object componentDTO, ExtensionsResolver resolver) {

        this.resolver = resolver;
    }

    protected ElementHandler getHandler(Object componentDTO) {

        return resolver.getElementHandlers().stream()
                .filter(handler -> handler.canHandle(componentDTO)).findFirst().get();
    }

    protected ExtensionsResolver getResolver() {

        return this.resolver;
    }

    protected String getPropertyValue(List<Property> props, String key) {

        return props.stream().filter(p -> key.equals(p.getKey())).findFirst().get().getValue();
    }

    protected boolean isIdIncrementing(String id) {
        return ScriptConstants.AUTOGENERATE_ID_SYMBOL.equals(id);
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
