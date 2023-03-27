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

package org.cloudbus.cloudsim.express.exceptions;

import org.cloudbus.cloudsim.express.exceptions.constants.ErrorConstants.ErrorCode;

public class CloudSimExpressRuntimeException extends RuntimeException {

    private ErrorCode code;

    public CloudSimExpressRuntimeException(ErrorCode code, String message, Throwable cause) {

        super(message, cause);

        // SpotBugs - EI_EXPOSE_REP: Fix exposing internal error code.
        this.code = getErrorCode(code);
    }

    public CloudSimExpressRuntimeException(ErrorCode code, String message) {
        super(message);

        // SpotBugs - EI_EXPOSE_REP: Fix exposing internal error code.
        this.code = getErrorCode(code);
    }

    public ErrorCode getCode() {

        // SpotBugs - EI_EXPOSE_REP: Fix exposing internal error code.
        return getErrorCode(this.code);
    }

    private ErrorCode getErrorCode(ErrorCode code) {
        return ErrorCode.valueOf(code.name());
    }
}
