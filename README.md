# TeamCity Digital Ocean Integration Plugin
**`Project is in deep dev and I don't recommend anyone to to use it before the first major version is released.`**

## Introduction
It is a plugin for TeamCity CI to provide Digital Ocean cloud integration support. The idea is to make TeamCity able to request from digital ocean new unix blade and automatically configure it only when it is required to create a new build. Once build is created successfully, or with some failure, the requested unix blade will be destroyed to avoid any non-expected charges.

## Build details
state  | version       | binaries link | sources link
--- | --- | --- | ---
`released`| 0.1           | [tc-digitalocean-plugin-0.1.zip](http://nexus.beolnix.com/content/repositories/releases/io/cyberstock/tc-digitalocean-plugin/0.1/tc-digitalocean-plugin-0.1.zip) | [0.1-release](https://github.com/beolnix/tcdop/releases/tag/0.1-release)
`in dev`  | 0.2           |        | [master](https://github.com/beolnix/tcdop)



## Roadmap
* Integration with DO based on prepared snapshots - Done in [0.1](http://nexus.beolnix.com/content/repositories/releases/io/cyberstock/tc-digitalocean-plugin/0.1/tc-digitalocean-plugin-0.1.zip)
* Docker based, agent push support - planned to be done in **0.2
* Documentation - in focus

## Demo
Here the demo of 0.1 version usage
[![IMAGE ALT TEXT HERE](http://img.youtube.com/vi/h6m3OtviBmM/0.jpg)](http://www.youtube.com/watch?v=h6m3OtviBmM)

