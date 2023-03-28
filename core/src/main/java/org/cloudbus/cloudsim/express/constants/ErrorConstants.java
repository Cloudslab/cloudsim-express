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

package org.cloudbus.cloudsim.express.constants;

/**
 * Provide various constants support for error handling.
 */
public class ErrorConstants {

    public static final String ERROR_CODE_PREFIX = "CE-";

    /**
     * Error segregation via error codes. CloudSim Express error codes starts with CE (i.e. [C]loudSim [E]xpress)
     */
    public enum ErrorCode {

        ELEMENT_NOT_AWARE_OF_SIMULATION(ERROR_CODE_PREFIX + "001", "This element is not aware about being simulated"),
        GENERIC_ERROR(ERROR_CODE_PREFIX + "002", "Generic error occurred. Please refer to the error message and stacktrace"),
        FILE_OPERATION_ERROR(ERROR_CODE_PREFIX + "003", "An error occurred while working with the file"),
        MISSING_HANDLER(ERROR_CODE_PREFIX + "004", "Could not find the handler"),
        EXTENSION_ERROR(ERROR_CODE_PREFIX + "005", "An error occurred with the extension"),
        SCRIPT_ERROR(ERROR_CODE_PREFIX + "006", "An error occurred while working with the script");

        private final String code;
        private final String description;

        ErrorCode(String code, String description) {

            this.code = code;
            this.description = description;
        }

        public String getCode() {

            return code;
        }

        public String getDescription() {

            return description;
        }
    }
}
