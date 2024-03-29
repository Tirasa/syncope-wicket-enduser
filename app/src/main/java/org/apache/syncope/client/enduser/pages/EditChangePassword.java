/*
 *  Copyright (C) 2020 Tirasa (info@tirasa.net)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.syncope.client.enduser.pages;

import org.apache.syncope.client.enduser.SyncopeEnduserSession;
import org.apache.syncope.client.enduser.commons.PageParametersUtils;
import org.apache.syncope.client.enduser.commons.RESTUtils;
import org.apache.syncope.client.ui.commons.markup.html.form.AjaxPasswordFieldPanel;
import org.apache.syncope.common.lib.patch.PasswordPatch;
import org.apache.syncope.common.lib.patch.UserPatch;
import org.apache.syncope.common.lib.to.UserTO;
import org.apache.syncope.common.lib.types.ExecStatus;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import java.util.stream.Collectors;

public class EditChangePassword extends AbstractChangePassword {

    private static final long serialVersionUID = -537205681762708502L;

    public EditChangePassword(final PageParameters parameters) {
        super(parameters);
    }

    @Override
    protected void doPwdSubmit(final AjaxRequestTarget target, final AjaxPasswordFieldPanel passwordField) {
        try {
            UserTO userTO = getPwdLoggedUser();
            PasswordPatch passwordPatch = new PasswordPatch.Builder().
                    value(passwordField.getModelObject()).onSyncope(true).resources(userTO.getResources()).build();

            UserPatch userPatch = new UserPatch();
            userPatch.setKey(getPwdLoggedUser().getKey());
            userPatch.setPassword(passwordPatch);
            // update and set page paramters according to provisioning result
            setResponsePage(SelfResult.class,
                    PageParametersUtils.managePageParams(EditChangePassword.this, "pwd.change",
                            RESTUtils.update(userPatch, userTO.getETagValue())
                                    .getPropagationStatuses().stream()
                                    .filter(ps -> ExecStatus.SUCCESS != ps.getStatus())
                                    .collect(Collectors.toList())));
        } catch (Exception e) {
            LOG.error("While changing password for {}",
                    SyncopeEnduserSession.get().getSelfTO().getUsername(), e);
            SyncopeEnduserSession.get().onException(e);
            notificationPanel.refresh(target);
        }
    }

    @Override
    protected UserTO getPwdLoggedUser() {
        return SyncopeEnduserSession.get().getSelfTO();
    }

    @Override
    protected void doPwdCancel() {
        setResponsePage(getApplication().getHomePage());
    }
}
