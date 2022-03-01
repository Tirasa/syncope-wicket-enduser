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
package org.apache.syncope.client.enduser.panels;

import org.apache.syncope.client.enduser.SyncopeEnduserApplication;
import org.apache.syncope.client.enduser.SyncopeEnduserSession;
import org.apache.syncope.client.enduser.commons.PageParametersUtils;
import org.apache.syncope.client.enduser.commons.RESTUtils;
import org.apache.syncope.client.enduser.layout.UserFormLayoutInfo;
import org.apache.syncope.client.enduser.pages.BasePage;
import org.apache.syncope.client.enduser.pages.SelfResult;
import org.apache.syncope.client.enduser.panels.any.Details;
import org.apache.syncope.client.enduser.panels.any.UserDetails;
import org.apache.syncope.client.ui.commons.Constants;
import org.apache.syncope.client.ui.commons.layout.UserForm;
import org.apache.syncope.client.ui.commons.wizards.any.AnyWrapper;
import org.apache.syncope.client.ui.commons.wizards.any.UserWrapper;
import org.apache.syncope.common.lib.AnyOperations;
import org.apache.syncope.common.lib.SyncopeClientException;
import org.apache.syncope.common.lib.to.UserTO;
import org.apache.syncope.common.lib.types.ExecStatus;
import org.apache.wicket.PageReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import java.util.List;
import java.util.stream.Collectors;

public class UserFormPanel extends AnyFormPanel<UserTO> implements UserForm {

    private static final long serialVersionUID = 6763365006334514387L;

    public UserFormPanel(
            final String id,
            final UserTO userTO,
            final List<String> anyTypeClasses,
            final UserFormLayoutInfo formLayoutInfo,
            final PageReference pageReference) {
        super(id, new UserWrapper(null), anyTypeClasses, formLayoutInfo, pageReference);

        AnyWrapper<UserTO> modelObj = newModelObject();
        buildLayout(modelObj);
    }

    public UserFormPanel(
            final String id,
            final UserTO previousUserTO,
            final UserTO userTO,
            final List<String> anyTypeClasses,
            final UserFormLayoutInfo formLayoutInfo,
            final PageReference pageReference) {
        super(id, new UserWrapper(previousUserTO, userTO), anyTypeClasses, formLayoutInfo, pageReference);

        AnyWrapper<UserTO> modelObj = newModelObject();
        setFormModel(modelObj);
        buildLayout(modelObj);

    }

    @Override
    protected void buildLayout(final AnyWrapper<UserTO> wrapper) {
        super.buildLayout(wrapper);
    }

    @Override
    protected Details<UserTO> addOptionalDetailsPanel(final AnyWrapper<UserTO> modelObject) {
        return new UserDetails(
                Constants.CONTENT_PANEL,
                UserWrapper.class.cast(modelObject),
                false,
                false,
                pageReference);
    }

    @Override
    protected void onFormSubmit(final AjaxRequestTarget target) {
        // captcha check
        boolean checked = true;
        if (SyncopeEnduserApplication.get().isCaptchaEnabled()) {
            checked = captcha.check();
        }
        if (!checked) {
            SyncopeEnduserSession.get().error(getString(Constants.CAPTCHA_ERROR));
            ((BasePage) pageReference.getPage()).getNotificationPanel().refresh(target);
        } else {
            try {
                AnyWrapper<UserTO> updatedWrapper = form.getModelObject();
                UserTO userTO = updatedWrapper.getInnerObject();

                fixPlainAndVirAttrs(userTO, getOriginalItem().getInnerObject());
                // update and set page paramters according to provisioning result
                setResponsePage(SelfResult.class,
                        PageParametersUtils.managePageParams(UserFormPanel.this, "profile.change",
                                RESTUtils.update(AnyOperations.diff(userTO, getOriginalItem().getInnerObject(), false),
                                                getOriginalItem().getInnerObject().getETagValue())
                                        .getPropagationStatuses().stream()
                                        .filter(ps -> ExecStatus.SUCCESS != ps.getStatus())
                                        .collect(Collectors.toList())));
            } catch (SyncopeClientException e) {
                LOG.error("While changing password for {}",
                        SyncopeEnduserSession.get().getSelfTO().getUsername(), e);
                SyncopeEnduserSession.get().onException(e);
                ((BasePage) pageReference.getPage()).getNotificationPanel().refresh(target);
            }
        }
    }
}
