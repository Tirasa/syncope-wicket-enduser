#
#  Copyright (C) 2020 Tirasa (info@tirasa.net)
# 
#  Licensed under the Apache License, Version 2.0 (the "License");
#  you may not use this file except in compliance with the License.
#  You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#

FROM tomcat:9-jdk8-adoptopenjdk-openj9
MAINTAINER syncope@tirasa.net

RUN set -x

RUN mkdir -p /opt/syncope/conf

COPY syncope-wicket-enduser-*.war $CATALINA_HOME/webapps/syncope-enduser.war

COPY *.template /tmp/

COPY *.xml /opt/syncope/conf/
COPY *.json /opt/syncope/conf/

COPY startup.sh /sbin
RUN chmod 755 /sbin/startup.sh
CMD ["/sbin/startup.sh"]

RUN rm -rf $CATALINA_HOME/webapps/ROOT/*
RUN rm -rf $CATALINA_HOME/webapps/manager/
RUN rm -rf $CATALINA_HOME/webapps/examples/
RUN rm -rf $CATALINA_HOME/webapps/docs/

RUN sed -i 's/<\/Host>/<Valve className="org.apache.catalina.valves.RemoteIpValve"\/><\/Host>/' $CATALINA_HOME/conf/server.xml
