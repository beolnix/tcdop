# TeamCity Digital Ocean Integration Plugin
**`Project is in deep dev and I don't recommend anyone to to use it before the first major version is released.`**

## Project description
It is a plugin for TeamCity CI to provide Digital Ocean cloud integration support. 

## Build details
state  | version       | binaries link | sources link
--- | --- | --- | ---
`released`| 0.2           | [tc-digitalocean-plugin-0.2.zip](http://nexus.beolnix.com/content/repositories/releases/io/cyberstock/tc-digitalocean-plugin/0.2/tc-digitalocean-plugin-0.1.zip) | [0.1-release](https://github.com/beolnix/tcdop/releases/tag/0.2-release)
`in dev`  | 0.3-SNAPSHOT  |        | [master](https://github.com/beolnix/tcdop)

## Usage
### Plugin installation
Copy [tc-digitalocean-plugin-0.2.zip](http://nexus.beolnix.com/content/repositories/releases/io/cyberstock/tc-digitalocean-plugin/0.2/tc-digitalocean-plugin-0.1.zip) to the ~/.BuildServer/plugins and restart the teamcity.

### Integration configuration
You need to prepare droplet image to be used by the plugin for on demand droplets creation.
There are three requirements for the image:

1. Teamcity agent must be installed and configured to be autostarted once operation system is loaded.
2. Teamcity server must be specified as the value of **serverUrl** property in **buildAgent/conf/buildAgent.properties**
3. The value of the **authorizationToken** in **buildAgent/conf/buildAgent.properties** must be empty
  
### Plugin configuraiton
1. Open Administration -> Agent Cloud
2. Press **Create new profile**
3. Choose cloud type **Digital Ocean**

## Plugin development
### Requirements
1. Java 5
2. Maven
3. Teamcity 9

### Build instruction
To build the project you need to setup **TEAMCITY_HOME** env variable. Please provide a path to the teamcity installation as the value for the variable.
Once variable is configured just execute the following command
```bash
mvn clean install
```
If everything is fine, the plugin will be packed to **tcdop/tc-digitalocean-plugin/target/tc-digitalocean-plugin-${version}.zip**

### Project modules
#### Main modules
* **tcdop-server** - main project module. Server's part of the module
* **tcdop-model**  - project's domain model. Used by server's and agent's parts of the plugin
* **tcdop-agent**  - agent's part of the plugin. It isn't used currently

#### Other modules
* **int-tests**    - integration test for digital ocean client
* **tcdop-ssh-client** - module is used by the integration test
