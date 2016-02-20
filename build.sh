#!/bin/bash
targets="${@:-clean test build}"
gradle -DteamcityVersion=SNAPSHOT ${targets}
