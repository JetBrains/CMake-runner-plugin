#!/bin/bash
targets="${@:-clean test package}"
mvn -DteamcityVersion=SNAPSHOT $targets
