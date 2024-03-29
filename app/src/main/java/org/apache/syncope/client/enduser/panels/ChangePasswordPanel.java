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

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.password.strength.PasswordStrengthBehavior;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.password.strength.PasswordStrengthConfig;
import org.apache.syncope.client.ui.commons.markup.html.form.AbstractFieldPanel;
import org.apache.syncope.client.ui.commons.markup.html.form.AjaxPasswordFieldPanel;
import org.apache.syncope.client.ui.commons.panels.NotificationPanel;
import org.apache.syncope.common.lib.to.UserTO;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.core.util.string.CssUtils;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.AppendingStringBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ChangePasswordPanel extends Panel {

    protected static final Logger LOG = LoggerFactory.getLogger(ChangePasswordPanel.class);

    private static final long serialVersionUID = -8937593602426944714L;

    protected static final String FORM_SUFFIX = "form_";

    protected StatelessForm<Void> form;

    protected AjaxPasswordFieldPanel passwordField;

    protected AjaxPasswordFieldPanel confirmPasswordField;

    public ChangePasswordPanel(final String id, final NotificationPanel notificationPanel) {
        super(id);
        form = new StatelessForm<Void>("changePassword") {

            private static final long serialVersionUID = 418292023846536149L;

            @Override
            protected void appendDefaultButtonField() {
                AppendingStringBuffer buffer = new AppendingStringBuffer();

                String cssClass = getString(CssUtils.key(Form.class, "hidden-fields"));

                // div that is not visible (but not display:none either)
                buffer.append(String.format(
                        "<div style=\"width:0px;height:0px;position:absolute;"
                        + "left:-100px;top:-100px;overflow:hidden\" class=\"%s\">",
                        cssClass));

                // add an empty textfield (otherwise IE doesn't work)
                buffer.append("<input title=\"text_hidden\" "
                        + "aria-label=\"text_hidden\" type=\"text\" "
                        + "tabindex=\"-1\" autocomplete=\"off\"/>");

                // add the submitting component
                final Component submittingComponent = (Component) getDefaultButton();
                buffer.append("<input title=\"submit_hidden\" aria-label=\"submit_hidden\" "
                        + "type=\"submit\" tabindex=\"-1\" name=\"");
                buffer.append(getDefaultButton().getInputName());
                buffer.append("\" onclick=\" var b=document.getElementById('");
                buffer.append(submittingComponent.getMarkupId());
                buffer.append(
                        "'); if (b!=null&amp;&amp;b.onclick!=null&amp;&amp;typeof(b.onclick) != 'undefined') "
                        + "{  var r = Wicket.bind(b.onclick, b)(); if (r != false) b.click(); } "
                        + "else { b.click(); };  return false;\" ");
                buffer.append(" />");

                // close div
                buffer.append("</div>");

                getResponse().write(buffer);
            }
        };
        form.setOutputMarkupId(true);
        add(form);

        passwordField = new AjaxPasswordFieldPanel(
                "password",
                getString("password"),
                new Model<>(),
                false,
                new PasswordStrengthBehavior(
                        new PasswordStrengthConfig()
                                .withDebug(true)
                                .withShowVerdictsInsideProgressBar(true)
                                .withShowProgressBar(true)));
        passwordField.setRequired(true);
        passwordField.setMarkupId("password");
        passwordField.setPlaceholder("password");

        passwordField.setInputTitle(getString("password"));
        passwordField.setFormComponentId("form_password");
        Label passwordLabel = (Label) passwordField.get(AbstractFieldPanel.LABEL);
        passwordLabel.add(new AttributeModifier("for", FORM_SUFFIX + "password"));

        ((PasswordTextField) passwordField.getField()).setResetPassword(true);
        form.add(passwordField);

        confirmPasswordField = new AjaxPasswordFieldPanel("confirmPassword",
                getString("confirmPassword"), new Model<>());
        confirmPasswordField.setRequired(true);
        confirmPasswordField.setMarkupId("confirmPassword");
        confirmPasswordField.setPlaceholder("confirmPassword");

        confirmPasswordField.setInputTitle(getString("confirmPassword"));
        confirmPasswordField.setFormComponentId(FORM_SUFFIX + "confirmPassword");
        Label confirmPasswordLabel = (Label) confirmPasswordField.get(AbstractFieldPanel.LABEL);
        confirmPasswordLabel.add(new AttributeModifier("for", FORM_SUFFIX + "confirmPassword"));

        ((PasswordTextField) confirmPasswordField.getField()).setResetPassword(true);
        form.add(confirmPasswordField);

        form.add(new EqualPasswordInputValidator(passwordField.getField(), confirmPasswordField.getField()));

        AjaxButton submitButton = new AjaxButton("submit", new Model<>(getString("submit"))) {

            private static final long serialVersionUID = 429178684321093953L;

            @Override
            protected void onSubmit(final AjaxRequestTarget target) {
                doSubmit(target, passwordField);
            }

            @Override
            protected void onError(final AjaxRequestTarget target) {
                notificationPanel.refresh(target);
            }
        };
        form.add(submitButton);
        form.setDefaultButton(submitButton);

        Button cancel = new Button("cancel") {

            private static final long serialVersionUID = 3669569969172391336L;

            @Override
            public void onSubmit() {
                doCancel();
            }
        };
        cancel.setOutputMarkupId(true);
        cancel.setDefaultFormProcessing(false);
        form.add(cancel);
    }

    public StatelessForm<Void> getForm() {
        return form;
    }

    public AjaxPasswordFieldPanel getPasswordField() {
        return passwordField;
    }

    public AjaxPasswordFieldPanel getConfirmPasswordField() {
        return confirmPasswordField;
    }

    protected abstract void doSubmit(AjaxRequestTarget target, AjaxPasswordFieldPanel passwordField);

    protected abstract void doCancel();

    protected abstract UserTO getLoggedUser();
}
