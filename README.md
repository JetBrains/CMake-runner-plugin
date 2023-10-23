[![official JetBrains project](https://jb.gg/badges/official-flat-square.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)

TeamCity CMake & Make Support Plugin
=============

https://plugins.jetbrains.com/plugin/20316-cmake-and-gnu-make-tools-support

Current status
--------------
[![Status](http://teamcity.jetbrains.com/app/rest/builds/buildType:\(id:TeamCityPluginsByJetBrains_CMakePlugin_BuildAgainstTeamCity80x\)/statusIcon)](http://teamcity.jetbrains.com/viewType.html?buildTypeId=TeamCityPluginsByJetBrains_CMakePlugin_BuildAgainstTeamCity80x)

Building
--------
- Download and install TeamCity distribution.
- Open project with IntelliJ IDEA.
- Set "TeamCityDistribution" path variable in IDEA Preferences as path to your TeamCity installation directory.
- Build 'plugin-zip' artifact

Sources
-------
```
 |- root
 |--- [cmake-runner-agent] - Agent's part of Teamcity CMake & Make Runners, JAVA sources
 |--- [cmake-runner-common] - Common code for Agent's and Server's parts or Teamcity CMake & Make Runners, JAVA
 |--- [cmake-runner-server] - Server's part of Teamcity CMake & Make Runners, JAVA
 |--- [cmake-runner-test] - Tests for cmake-runner. TeamCity tests API is required for running tests
 |--- [regex-parser] - Special xml-configurable parser module for per-line parsing, JAVA
 |--- [out] - Compilation output directory
 |--- [lib] - Libraries
 |--- build-hooks.xml - Special ant script for building plugin
 |--- copyright.txt - Copyright for sources
 |--- README.md - this file
```

Installation
------------
Just follow standard TeamCity plugin installation instructions.
Plugin file is "cmake-runner.zip" at "out/artifacts/plugin_zip" directory.
