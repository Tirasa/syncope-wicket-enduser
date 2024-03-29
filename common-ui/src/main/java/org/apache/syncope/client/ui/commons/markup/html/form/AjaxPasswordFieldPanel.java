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
package org.apache.syncope.client.ui.commons.markup.html.form;

import de.agilecoders.wicket.extensions.markup.html.bootstrap.form.password.strength.PasswordStrengthBehavior;
import org.apache.syncope.client.ui.commons.Constants;
import org.apache.syncope.client.ui.commons.ajax.form.IndicatorAjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;

import java.util.Optional;

public class AjaxPasswordFieldPanel extends FieldPanel<String> {

    private static final long serialVersionUID = -5490115280336667460L;

    public AjaxPasswordFieldPanel(final String id, final String name, final IModel<String> model) {
        this(id, name, model, true, null);
    }

    public AjaxPasswordFieldPanel(final String id, final String name, final IModel<String> model,
            final boolean enableOnChange) {
        this(id, name, model, enableOnChange, null);
    }

    public AjaxPasswordFieldPanel(
            final String id,
            final String name,
            final IModel<String> model,
            final boolean enableOnChange,
            final PasswordStrengthBehavior passwordStrengthBehavior) {

        super(id, name, model);

        field = new PasswordTextField("passwordField", model);

        Button togglePassword = new Button("togglePassword");
        add(togglePassword);
        
        Optional.ofNullable(passwordStrengthBehavior).ifPresent(field::add);
        add(field.setLabel(new ResourceModel(name, name)).setRequired(false).setOutputMarkupId(true));

        if (enableOnChange && !isReadOnly()) {
            field.add(new IndicatorAjaxFormComponentUpdatingBehavior(Constants.ON_CHANGE) {

                private static final long serialVersionUID = -1107858522700306810L;

                @Override
                protected void onUpdate(final AjaxRequestTarget art) {
                    // nothing to do
                }
            });
        }

    }

    @Override
    public FieldPanel<String> addRequiredLabel() {
        if (!isRequired()) {
            setRequired(true);
        }

        this.isRequiredLabelAdded = true;
        return this;
    }
}
