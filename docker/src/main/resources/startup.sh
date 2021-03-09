#!/bin/bash

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

cd /tmp

sed "s/\${CORE_SCHEME}/$CORE_SCHEME/" enduser.properties.template |
sed "s/\${CORE_HOST}/$CORE_HOST/" | 
sed "s/\${CORE_PORT}/$CORE_PORT/" |
sed "s/\${ADMIN_USER}/$ADMIN_USER/" > /opt/syncope/conf/enduser.properties

sed "s/\${CORE_SCHEME}/$CORE_SCHEME/" saml2sp-agent.properties.template |
sed "s/\${CORE_HOST}/$CORE_HOST/" | 
sed "s/\${CORE_PORT}/$CORE_PORT/" > /opt/syncope/conf/saml2sp-agent.properties

sed "s/\${CORE_SCHEME}/$CORE_SCHEME/" saml2sp-agent.properties.template |
sed "s/\${CORE_HOST}/$CORE_HOST/" | 
sed "s/\${CORE_PORT}/$CORE_PORT/" > /opt/syncope/conf/oidcclient-agent.properties

catalina.sh run
