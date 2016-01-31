/*
 * Copyright 2013 Sam Elsharif
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.msxlaunchers.openmsx.launcher.data.repository;

import java.util.Objects;

/**
 * Class to hold data about games from openMSX software database
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
public final class RepositoryGame
{
	public static final String MIRRORED_ROM = "Mirrored ROM";

	private final String title;
	private final String company;
	private final String year;
	private final String country;

	//the following can be null
	private final boolean original;
	private final String originalText;
	private final String mapper;
	private final String start;
	private final String remark;
	
	/**
	 * @param title
	 * @param company
	 * @param year
	 * @param country
	 */
	public RepositoryGame( String title, String company, String year, String country )
	{
		this( title, company, year, country, false, null, null, null, null );
	}

	/**
	 * @param title
	 * @param company
	 * @param year
	 * @param country
	 * @param original
	 * @param originalText Can be null
	 * @param mapper Can be null
	 * @param start Can be null
	 * @param remark Can be null
	 */
	public RepositoryGame( String title, String company, String year, String country,
			boolean original, String originalText, String mapper, String start, String remark )
	{
		this.title = Objects.requireNonNull( title );
		this.company = Objects.requireNonNull( company );
		this.year = Objects.requireNonNull( year );
		this.country = Objects.requireNonNull( country );
		
		this.original = original;
		this.originalText = originalText;
		this.mapper = mapper;
		this.start = start;
		this.remark = remark;
	}

	//--------
	// Getters
	//--------
	public String getTitle()	{ return title; }
	public String getCompany()	{ return company; }
	public String getYear()	{ return year; }
	public String getCountry()	{ return country; }
	public boolean isOriginal() { return original; }
	public String getOriginalText()	{ return originalText; }

	//Mapper has special treatment
	public String getMapper()
	{
		String mapperValue;
		
		if( mapper == null )
		{
			mapperValue = MIRRORED_ROM;
		}
		else
		{
			mapperValue = mapper;
		}

		return mapperValue;
	}

	public String getStart()	{ return start; }
	public String getRemark()	{ return remark; }
}
