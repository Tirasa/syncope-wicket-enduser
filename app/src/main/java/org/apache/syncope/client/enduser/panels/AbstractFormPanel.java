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

import java.io.Serializable;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.wicket.PageReference;
import org.apache.wicket.markup.html.panel.Panel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractFormPanel<T extends Serializable> extends Panel {

    private static final long serialVersionUID = 6650311507433421554L;

    protected static final Logger LOG = LoggerFactory.getLogger(AbstractFormPanel.class);

    protected final PageReference pageReference;

    private final T defaultItem;

    private T item;

    public AbstractFormPanel(final String id, final T defaultItem, final PageReference pageReference) {
        super(id);
        this.defaultItem = defaultItem;
        this.pageReference = pageReference;
    }

    protected T getOriginalItem() {
        return item;
    }

    public T getDefaultItem() {
        return defaultItem;
    }

    protected T newModelObject() {
        if (item == null) {
            // keep the original item: the which one before the changes performed during wizard browsing
            item = SerializationUtils.clone(defaultItem);
        }

        // instantiate a new model object and return it
        return SerializationUtils.clone(item);
    }

    public void setItem(final T item) {
        this.item = item;
    }

    public PageReference getPageReference() {
        return pageReference;
    }

}
