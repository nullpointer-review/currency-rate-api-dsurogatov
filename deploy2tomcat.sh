#!/bin/bash

mvn -DtomcatUrl=http://localhost:8080/manager/text -DtomcatUser=tomcat -DtomcatPass=tomcat tomcat:redeploy
