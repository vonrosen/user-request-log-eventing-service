#!/bin/bash

JARFILE=`ls target/*jar`

java -Dspring.profiles.active=${PROFILE} -jar ${JARFILE}