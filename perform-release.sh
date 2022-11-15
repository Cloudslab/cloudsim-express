#!/bin/bash
#
# CloudSim Express
# Copyright (C) 2022  CloudsLab
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#

# Written and tested for linux


RELEASE_VERSION=0.1

rm -rf release-artifacts
mkdir release-artifacts
cd release-artifacts

RELEASE_FOLDER=cloudsim-express-$RELEASE_VERSION
mkdir $RELEASE_FOLDER
cd $RELEASE_FOLDER
cp ../../core/target/simulator-jar-with-dependencies.jar simulator.jar

mkdir scenarios
RESOURCE_DIRECTORY=core/src/main/resources
cp ../../$RESOURCE_DIRECTORY/scenarios/geo-distributed-datacenter-network.yaml ./scenarios

mkdir extensions
cp ../../extensions/target/cloudsim-express-extensions-$RELEASE_VERSION-SNAPSHOT.jar ./extensions/workload-generators-$RELEASE_VERSION.jar

mkdir logs

mkdir sample-data
cp ../../external-dependencies/data/network/aws-geo-distributed-datacenter-inter-network-data.csv sample-data

cp ../../$RESOURCE_DIRECTORY/cloudsim-express-configs.properties configs.properties

cp ../../$RESOURCE_DIRECTORY/log4j2.properties ./

cd ../

zip -r cloudsim-express-$RELEASE_VERSION.zip $RELEASE_FOLDER

rm -rf $RELEASE_FOLDER
