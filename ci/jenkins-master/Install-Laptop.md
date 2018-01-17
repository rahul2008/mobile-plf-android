Laptop Build Agent installation
===============================

## BIOS changes
These laptops have multiple graphics cards which causes issues in linux.
Therefore this setting needs to be disabled. Futhermore it's best practice
to enable the VtX bit in the BIOS to allow virtualization of 64BIT OSes.

```BIOS
Graphics: Discrete
VtX Bit: Enabled
VtD Bit: Enabled
Built-in WIFI: Disabled --> otherwise the docker bridge is linked to WIFI
```

## Ubuntu
* Install ubuntu 16.04
* Update packages
```BASH
sudo apt-get update
sudo apt-get upgrade
```
### Install SSH
```BASH
sudo apt-get install ssh
```
Check that the service is running using

```BASH
sudo service ssh status
```

### Add TFS server as host
```BASH
vi /home/jenkins/.ssh/config
```

## Install Docker
Follow guide on [Docker Ubuntu Guide](https://docs.docker.com/engine/installation/linux/docker-ce/ubuntu/)

And followup guide to add rights to run [docker for non root users](https://docs.docker.com/engine/installation/linux/linux-postinstall/)

Allow docker to connect to non-secure repositories (artifactory without https).
Create a file /etc/docker/daemon.json
add the following content as indicated on [docker instructions](https://docs.docker.com/registry/insecure/):
```json
{
    "insecure-registries": ["artifactory-ehv.ta.philips.com:6556"]
}
```

Restart docker daemon to apply changes
```BASH
sudo service docker restart
```
## Fetch the android build image from Artifactory
```BASH
docker pull artifactory-ehv.ta.philips.com:6556/cdp2/android:26.0.2-manual
```

## Run the docker images
```BASH
lsusb
docker run -p [port]:22 -v [usb-link] artifactory-ehv.ta.philips.com:6556/cdp2/android:26.0.2-manual
```

Pass the correct usb ids to docker to use (example for container 42)
```BASH
docker run -d -v /opt/docker/ssh:/host/ssh --device /dev/bus/usb/[bus]/[port] -p 9042:22 --cap-add SYS_ADMIN --security-opt apparmor:unconfirmed --name android_calab_42 artificatory-ehv.ta.philips.com:6556/cdp2/android:26.0.2-manual /opt/run 
```

## Open items
* Time server does not sync (currently solved by manually setting time)
* SSH key installation