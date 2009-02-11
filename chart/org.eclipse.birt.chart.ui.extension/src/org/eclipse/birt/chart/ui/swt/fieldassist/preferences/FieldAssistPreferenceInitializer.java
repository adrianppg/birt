/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.fieldassist.preferences;

import org.eclipse.birt.chart.ui.plugin.ChartUIExtensionPlugin;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Class used to initialize default preference values.
 * 
 * @since 2.5
 */
public class FieldAssistPreferenceInitializer extends
		AbstractPreferenceInitializer
{

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
	 * initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences( )
	{
		IPreferenceStore store = ChartUIExtensionPlugin.getDefault( )
				.getPreferenceStore( );
		store.setDefault( PreferenceConstants.PREF_DECORATOR_HORIZONTALLOCATION,
				PreferenceConstants.PREF_DECORATOR_HORIZONTALLOCATION_LEFT );
		store.setDefault( PreferenceConstants.PREF_DECORATOR_VERTICALLOCATION,
				PreferenceConstants.PREF_DECORATOR_VERTICALLOCATION_CENTER );
		store.setDefault( PreferenceConstants.PREF_DECORATOR_MARGINWIDTH, 0 );
		store.setDefault( PreferenceConstants.PREF_CONTENTASSISTKEY,
				PreferenceConstants.PREF_CONTENTASSISTKEY1 );
		store.setDefault( PreferenceConstants.PREF_CONTENTASSISTKEYCUSTOMKEY,
				"" ); //$NON-NLS-1$
		store.setDefault( PreferenceConstants.PREF_CONTENTASSISTKEY_PROPAGATE,
				false );
		store.setDefault( PreferenceConstants.PREF_CONTENTASSISTDELAY, 1000 );
		store.setDefault( PreferenceConstants.PREF_CONTENTASSISTRESULT,
				PreferenceConstants.PREF_CONTENTASSISTRESULT_REPLACE );
		store.setDefault( PreferenceConstants.PREF_CONTENTASSISTFILTER,
				PreferenceConstants.PREF_CONTENTASSISTFILTER_CHAR );
	}
}
