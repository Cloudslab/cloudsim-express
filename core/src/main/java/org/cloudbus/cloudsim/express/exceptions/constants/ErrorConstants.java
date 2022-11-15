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

package org.cloudbus.cloudsim.express.exceptions.constants;

public class ErrorConstants {

    public static final String ERROR_CODE_PREFIX = "LS-";

    /**
     * Error codes start with the prefix, 'LS' which is to represent 'Low-code Simulation'.
     */
    public enum ErrorCode {

        ELEMENT_NOT_AWARE_OF_SIMULATION(ERROR_CODE_PREFIX + "001",
                "This element is not aware about being simulated"),
        UNKNOWN_ERROR(ERROR_CODE_PREFIX + "002", "Unknown error occurred");

        private String errorCode;
        private String errorDescription;

        ErrorCode(String errorCode, String errorDescription) {

            this.errorCode = errorCode;
            this.errorDescription = errorDescription;
        }

        public String getErrorCode() {

            return errorCode;
        }

        private void setErrorCode(String errorCode) {

            this.errorCode = errorCode;
        }

        public String getErrorDescription() {

            return errorDescription;
        }

        private void setErrorDescription(String errorDescription) {

            this.errorDescription = errorDescription;
        }
    }
}
