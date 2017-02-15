#!/bin/bash

arg="$(echo $1 | tr '[A-Z]' '[a-z]')"

case "$arg" in
  "snapshot")
     describe=`git describe --match *SNAPSHOT*`
     IFS="-"
     tags=($describe)
     git_tag="${tags[0]}-${tags[1]}.${tags[2]}"
     ;;
  "rc")
     describe=`git describe --abbrev=0 2> /dev/null`
     if [[ $describe == *"rc"* ]]; then
       version=`echo $describe | cut -d- -f1`
       rc=`echo $describe | cut -d- -f2 | cut -d. -f2`
       git_tag="${version}-rc.$((rc+1))"
     else
       describe=`git describe --match *SNAPSHOT*`
       IFS="-"
       tags=($describe)
       git_tag="${tags[0]}-rc.1"
     fi
     ;;
  "release")
     describe=`git describe --abbrev=0 2> /dev/null`
     IFS="-"
     tags=($describe)
     git_tag="${tags[0]}"
     ;;
  *)
     echo $"Usage: `basename $0` {snapshot|rc|release}"
     exit 1
esac

printf "$git_tag"