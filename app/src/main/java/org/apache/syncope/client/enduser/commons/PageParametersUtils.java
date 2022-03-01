/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.syncope.client.enduser.commons;

import org.apache.syncope.client.enduser.SyncopeEnduserApplication;
import org.apache.syncope.client.enduser.pages.Dashboard;
import org.apache.syncope.client.ui.commons.Constants;
import org.apache.syncope.common.lib.to.PropagationStatus;
import org.apache.wicket.Component;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import java.util.List;

public final class PageParametersUtils {

    private PageParametersUtils() {
    }

    public static PageParameters managePageParams(
            final Component component,
            final String section,
            final List<PropagationStatus> failingPropagations) {
        PageParameters parameters = new PageParameters();
        parameters.add(Constants.STATUS,
                failingPropagations.isEmpty()
                        ? Constants.OPERATION_SUCCEEDED
                        : Constants.OPERATION_ERROR);
        parameters.add(Constants.NOTIFICATION_TITLE_PARAM,
                failingPropagations.isEmpty()
                        ? component.getString("self." + section + ".success.msg")
                        : component.getString("self." + section + ".error.msg"));
        parameters.add(Constants.NOTIFICATION_MSG_PARAM,
                failingPropagations.isEmpty()
                        ? component.getString("self." + section + ".success")
                        : component.getString("self." + section + ".error"));
        parameters.add(Constants.LANDING_PAGE,
                SyncopeEnduserApplication.get().getPageClass("profile", Dashboard.class).getName());
        return parameters;
    }
}
