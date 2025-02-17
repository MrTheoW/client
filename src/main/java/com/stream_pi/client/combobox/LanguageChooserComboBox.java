/*
 * Stream-Pi - Free & Open-Source Modular Cross-Platform Programmable Macro Pad
 * Copyright (C) 2019-2021  Debayan Sutradhar (rnayabed),  Samuel Quiñones (SamuelQuinones)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package com.stream_pi.client.combobox;

import com.stream_pi.client.i18n.I18N;
import com.stream_pi.util.combobox.StreamPiComboBox;
import com.stream_pi.util.combobox.StreamPiComboBoxFactory;
import com.stream_pi.util.i18n.language.Language;

import java.util.Locale;

public class LanguageChooserComboBox extends StreamPiComboBox<Language>
{
    public LanguageChooserComboBox()
    {
        setStreamPiComboBoxFactory(new StreamPiComboBoxFactory<>() {
            @Override
            public String getOptionDisplayText(Language language) {
                return language.getDisplayName();
            }
        });
        setOptions(I18N.getLanguages());
    }

    public Locale getSelectedLocale()
    {
        return getCurrentSelectedItem().getLocale();
    }
}
