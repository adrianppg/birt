/*******************************************************************************
 * Copyright (c) 2006 Inetsoft Technology Corp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Inetsoft Technology Corp  - Implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.excel;

import java.util.Hashtable;
import java.util.Map;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.emitter.excel.layout.ExcelLayoutEngine;
import org.eclipse.birt.report.engine.emitter.excel.layout.ContainerSizeInfo;
import org.eclipse.birt.report.engine.emitter.excel.layout.XlsContainer;

/**
 * This class is used to caculate styles for Excel.
 * 
 * 
 */
public class StyleEngine
{
	public static final int DEFAULT_DATE_STYLE = 1;
	
	public static final int RESERVE_STYLE_ID = 20;

	private int styleID = RESERVE_STYLE_ID;	
	private Hashtable<StyleEntry,Integer> style2id = new Hashtable<StyleEntry,Integer>( );
	private ExcelLayoutEngine engine;

	/**
	 * 
	 * @param dataMap
	 *            layout data
	 * @return a StyleEngine instance
	 */
	public StyleEngine( ExcelLayoutEngine engine )
	{
		this.engine = engine;

		style2id.put( getDefaultEntry( DEFAULT_DATE_STYLE ), new Integer(
				DEFAULT_DATE_STYLE ) );
	}
	
	public StyleEntry getDefaultEntry( int id )
	{
		StyleEntry entry = new StyleEntry( );
		if ( id == DEFAULT_DATE_STYLE )
		{
			entry.setProperty( StyleConstant.DATE_FORMAT_PROP,
					"yyyy-M-d HH:mm:ss AM/PM" );
			entry.setProperty( StyleConstant.DATA_TYPE_PROP, Data.DATE );
		}
		return entry;
	}

	public StyleEntry createEntry(ContainerSizeInfo sizeInfo, IStyle style)
	{
		if ( style == null )
		{
			return StyleBuilder.createEmptyStyleEntry( );
		}	
		
		StyleEntry entry = initStyle( style, sizeInfo );
		entry.setStart( true );
		return entry;
	}

	public void calculateTopStyles( )
	{
		if ( engine.getContainers( ).size( ) > 0 )
		{
			XlsContainer container = engine.getCurrentContainer( );
			StyleEntry style = container.getStyle( );
			boolean first = style.isStart( );

			if ( first )
			{
				ContainerSizeInfo rule = container.getSizeInfo( );
				int start = container.getStartRowId( );
				applyContainerTopBorder( rule, start );
				style.setStart( false );
			}
		}
	}

	public StyleEntry createHorizontalStyle( ContainerSizeInfo rule )
	{
		StyleEntry entry = StyleBuilder.createEmptyStyleEntry( );
		
		if(engine.getContainers( ).size( ) > 0)
		{
			XlsContainer container = engine.getCurrentContainer( );
			ContainerSizeInfo crule = container.getSizeInfo( );
			StyleEntry cEntry = container.getStyle( );

			StyleBuilder.mergeInheritableProp( cEntry, entry );

			if ( rule.getStartCoordinate( ) == crule.getStartCoordinate( ) )
			{
				StyleBuilder.applyLeftBorder( cEntry, entry );
			}

			if ( rule.getEndCoordinate( ) == crule.getEndCoordinate( ) )
			{
				StyleBuilder.applyRightBorder( cEntry, entry );
			}
		}

		return entry;
	}

	public void removeContainerStyle( )
	{
		calculateBottomStyles( );
	}

	public void calculateBottomStyles( )
	{
		if(engine.getContainers( ).size() == 0)
		{
			return;
		}	
		
		XlsContainer container = engine.getCurrentContainer( );
		ContainerSizeInfo rule = container.getSizeInfo( );
		StyleEntry entry = container.getStyle( );

		if ( entry.isStart( ) )
		{
			calculateTopStyles( );
		}

		int start = rule.getStartCoordinate( );
		int col = engine.getAxis().getColumnIndexByCoordinate( start );
		int span = engine.getAxis().getColumnIndexByCoordinate( rule.getEndCoordinate( ) ) - col;
		int cp = engine.getColumnSize( col );

		cp = cp > 0 ? cp - 1 : 0;

		for ( int i = 0; i < span; i++ )
		{
			Data data = engine.getData( i + col, cp );
			
			if(data == null)
			{
				continue;
			}	
			
			StyleBuilder.applyBottomBorder( entry, data.style );
		}		
	}	

	public StyleEntry getStyle( IStyle style, ContainerSizeInfo rule )
	{
		// This style associated element is not in any container.
		return initStyle( style, rule );
	}

	public int getStyleID( StyleEntry entry )
	{
		if ( style2id.get( entry ) != null )
		{
			return style2id.get( entry ).intValue( );
		}
		else
		{
			int styleId = styleID;
			style2id.put( entry, new Integer( styleId ) );
			styleID++;
			return styleId;
		}
	}	

	public Map<StyleEntry,Integer> getStyleIDMap( )
	{
		return style2id;
	}

	private void applyContainerTopBorder( ContainerSizeInfo rule, int pos )
	{
		if(engine.getContainers( ).size( ) == 0)
		{
			return;
		}	
		
		XlsContainer container = engine.getCurrentContainer( );
		StyleEntry entry = container.getStyle( );
		int col = engine.getAxis( ).getColumnIndexByCoordinate( rule.getStartCoordinate( ) );
		int span = engine.getAxis( ).getColumnIndexByCoordinate( rule.getEndCoordinate( ) ) - col;

		for ( int i = col; i < span + col; i++ )
		{
			Data data = engine.getData( i, pos );			
			
			if(data == null || data.isBlank( ) )
			{
				continue;
			}	
			StyleBuilder.applyTopBorder( entry,	data.style );
		}
	}

	private void applyHBorders( StyleEntry centry, StyleEntry entry,
			ContainerSizeInfo crule, ContainerSizeInfo rule )
	{
		if ( crule == null || rule == null )
		{
			return;
		}
		if ( crule.getStartCoordinate( ) == rule.getStartCoordinate( ) )
		{
			StyleBuilder.applyLeftBorder( centry, entry );
		}

		if ( crule.getEndCoordinate( ) == rule.getEndCoordinate( ) )
		{
			StyleBuilder.applyRightBorder( centry, entry );
		}
	}

	private StyleEntry initStyle( IStyle style, ContainerSizeInfo rule )
	{
		StyleEntry entry = StyleBuilder.createStyleEntry( style );
		
		if(engine.getContainers( ).size( ) > 0)		
		{
			XlsContainer container = engine.getCurrentContainer( );
			StyleEntry cEntry = container.getStyle( );
			StyleBuilder.mergeInheritableProp( cEntry, entry );
			applyHBorders( cEntry, entry, container.getSizeInfo( ), rule );
		}

		return entry;
	}
}
