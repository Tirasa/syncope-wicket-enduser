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
package org.apache.syncope.client.ui.commons.panels;

import org.apache.wicket.markup.html.panel.Panel;

public abstract class BaseSSOLoginFormPanel extends Panel {

    private static final long serialVersionUID = -2371733568360773586L;

    public BaseSSOLoginFormPanel(final String id) {
        super(id);
    }
}
