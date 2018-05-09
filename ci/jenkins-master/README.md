Scripted creation of Jenkins master docker image
================================================

## Findings
### Jenkins does not support updating single components only
* All dependencies are updated.
* However they are updated to the latest version, NOT the required version!
* This prevents installing plugins on old-jenkins instances. *Therefore it is vital to keep Jenkins master up2date!*

### Build time issue addressed
[SCM API 2.0](https://jenkins.io/blog/2017/01/17/scm-api-2/)

[Multibranch Pipeline 2.13](https://plugins.jenkins.io/workflow-multibranch)
```MD
JENKINS-33273 Load Jenkinsfile (or any file requested in loadTrusted) directly from the SCM rather than doing a checkout. Requires a compatible SCM (currently Git or GitHub).
```
This resolves the git clones on master issue observed in Jenkins 2.19.1! 

## How to create an up-2-date Jenkins Master
fully scripted approach
### Get plugins currently installed:

Information source: [Jenkins Github](https://github.com/jenkinsci/docker#preinstalling-plugins)

```bash
curl -u "user:pass" -sSL "http://localhost:8080/pluginManager/api/xml?depth=1&xpath=/*/*/shortName|/*/*/version&wrapper=plugins" | perl -pe 's/.*?<shortName>([\w-]+).*?<version>([^<]+)()(<\/\w+>)+/\1 \2\n/g'|sed 's/ /:/' > plugins.txt
```
Result: [plugins.txt](./plugins.txt)

*NOTE*: Save plugins.txt with LF line endings!
*NOTE*: Remove custom plugins which are not stored in the jenkins repository from plugins.txt. they should be placed in artifactory and downloaded inside of the [Dockerfile](./Dockerfile)

### Build the image
```BASH
docker build -t cdp2/jenkins-master:2.89.2 .
```
## Run the image

### Deploy jenkins on a docker stack
The secrets to be passed to docker before deploying can be found in [Confluence](https://confluence.atlas.philips.com/pages/viewpage.action?spaceKey=CDPCON&title=CM+Accounts+List)
* Functional user / password: The account & password for platform.infrastructure@philips.com
* SSH username - Sys90 account
* SSH key - id_rsa file stored in attachments (the key for the Sys90 account)

```BASH
docker swarm init

echo "[functional-user]" | docker secret create functional-user -
echo "[functional-password]" | docker secret create functional-pass -
docker secret create ssh-key id_rsa
echo "[ssh-username]" | docker secret create ssh-user -
echo "http://[ip]:8080/" | docker secret create master-url -

docker stack deploy -c jenkins.yml jenkins
```

## Upgrade scenario's

### Remove an existing volume if needed (staging only)
*NOT SUITABLE FOR PRODUCTION*
*Note:* If you use an existing volume, then jenkins will not start fully clean!

Also note that all your existing configuration and build output is stored in this volume.
Therefore you are in risk of losing data if you remove this volume! 
```BASH
docker volume rm jenkins_jenkins_home_vol
```

### Remove files from the jenkins docker volume
Find the location of the jenkins docker volume
```BASH
docker volume inspect jenkins_jenkins_home_vol
```
The `Mountpoint` is the location where the volume is stored.

#### Remove existing jobs
Navigate to the `jobs/` folder in the volume and remove all `config.xml` files.
```BASH
rm **\config.xml
```
#### Remove jenkins config
Navigate to the root folder in the volume and remove the all files from this folder
*DO NOT REMOVE SUB FOLDERS!*
```BASH
rm *.*
```
### Rollout the updated docker container


## Push docker image to artifactory
### Login
```BASH 
 docker login artifactory-ehv.ta.philips.com:6556
```
### Tag
```BASH
 docker tag cdp2/jenkins-master:2.89.2 artifactory-ehv.ta.philips.com:6556:/cdp2/cdp2/jenkins-master:2.89.2
```

### Push
```BASH
 docker push artifactory-ehv.ta.philips.com:6556:/cdp2/cdp2/jenkins-master:2.89.2
```

## [Helpful commands](https://docs.docker.com/engine/swarm/)

### Check the status of the stack
```BASH
docker stack ps jenkins
```
### Stop the stack
```BASH
docker stack rm jenkins
```
### Check console logging
```BASH
docker service logs jenkins_master
```

### [Scaling the services](https://docs.docker.com/engine/swarm/swarm-tutorial/scale-service/)
```BASH
docker service scale jenkins_slave=4
```
