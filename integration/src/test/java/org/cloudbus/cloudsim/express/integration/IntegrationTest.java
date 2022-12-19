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

package org.cloudbus.cloudsim.express.integration;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.cloudbus.cloudsim.express.integration.helper.IntegrationHelper;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IntegrationTest {

    @Test
    void executeDefaultScenario() throws URISyntaxException, IOException {

        var buildDirectory = Paths.get("").toAbsolutePath().toString();
        var integrationTestRootLocation = buildDirectory + getSeparator() + "target" + getSeparator() + "integration-tests";
        var cloudSimExpressToolLocation = new File(integrationTestRootLocation + getSeparator() + "cloudsim-express-0.1");

        prepareCloudSimExpressTool(integrationTestRootLocation);

        executeCloudSimExpressTool(cloudSimExpressToolLocation);

        var reference = Paths.get(this.getClass().getClassLoader().getResource("reference-cloudsim-successful.log").toURI()).toFile();

        Optional<Path> lastFilePath = Files.list(Paths.get(new URI(cloudSimExpressToolLocation + getSeparator() + "logs")))    // here we get the stream with full directory listing
                .filter(f -> !Files.isDirectory(f))  // exclude subdirectories from listing
                .max(Comparator.comparingLong(f -> f.toFile().lastModified()));  // finally get the last file using simple comparator by lastModified field
        lastFilePath.ifPresent(path -> {
            try {
                Reader reader1 = new BufferedReader(new FileReader(path.toFile()));
                Reader reader2 = new BufferedReader(new FileReader(reference));
                assertTrue(IOUtils.contentEqualsIgnoreEOL(reader1, reader2));
            } catch (Exception e) {
                throw new RuntimeException("content mismatch", e);
            }
        });
    }

    private static void executeCloudSimExpressTool(File cloudSimExpressToolLocation) {
        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "simulator.jar", "configs.properties").inheritIO();
            pb.directory(cloudSimExpressToolLocation);
            Process p = pb.start();
            p.waitFor();
        } catch (Exception e) {
            throw new RuntimeException("Failed during CloudSimExpress execution", e);
        }
    }

    private static String getSeparator() {
        return FileSystems.getDefault().getSeparator();
    }

    private static void prepareCloudSimExpressTool(String integrationTestRootLocation) {
        try {
            // Create integration test root.
            FileUtils.deleteDirectory(new File(integrationTestRootLocation));
            FileUtils.forceMkdir(new File(integrationTestRootLocation));

            // Copy and unzip CloudSimExpress.
            var releaseArtifactsLocation = Paths.get("").toAbsolutePath() + getSeparator() + ".." + getSeparator()
                    + "release-artifacts";
            var targetFile = new File(integrationTestRootLocation + getSeparator() + "cloudsim-express-0.1.zip");
            FileUtils.copyFile(
                    new File(releaseArtifactsLocation + getSeparator() + "cloudsim-express-0.1.zip"),
                    targetFile
            );
            IntegrationHelper.unzipFolder(Path.of(targetFile.toURI()), Path.of(integrationTestRootLocation));

        } catch (Exception e) {
            throw new RuntimeException("Could not handle file creation", e);
        }
    }
}
