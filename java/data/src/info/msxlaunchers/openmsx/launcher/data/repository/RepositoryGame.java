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
	private final String system;
	private final String company;
	private final String year;
	private final String country;

	//the following can be null
	private final boolean original;
	private final String originalText;
	private final String mapper;
	private final String start;
	private final String remark;

	public static class RepositoryGameParam
	{
		private String title;
		private String system;
		private String company;
		private String year;
		private String country;
		private boolean original;
		private String originalText;
		private String mapper;
		private String start;
		private String remark;

		public RepositoryGameParam title( String title ) { this.title = Objects.requireNonNull( title ); return this; }
		public RepositoryGameParam system( String system ) { this.system = Objects.requireNonNull( system ); return this; }
		public RepositoryGameParam company( String company ) { this.company = Objects.requireNonNull( company ); return this; }
		public RepositoryGameParam year( String year ) { this.year = Objects.requireNonNull( year ); return this; }
		public RepositoryGameParam country( String country ) { this.country = Objects.requireNonNull( country ); return this; }
		public RepositoryGameParam isOriginal( boolean original ) { this.original = original; return this; }
		public RepositoryGameParam originalText( String originalText ) { this.originalText = originalText; return this; }
		public RepositoryGameParam mapper( String mapper ) { this.mapper = mapper; return this; }
		public RepositoryGameParam start( String start ) { this.start = start; return this; }
		public RepositoryGameParam remark( String remark ) { this.remark = remark; return this; }

		public RepositoryGame build()
		{
			if( title == null || system == null || company == null || year == null || country == null )
			{
				throw new IllegalArgumentException( "Title, System, Company, Year and Country must be set" );
			}

			return new RepositoryGame( this );
        }
	}

	public static RepositoryGameParam title( String title ) { return new RepositoryGameParam().title( title ); }
	public static RepositoryGameParam system( String system ) { return new RepositoryGameParam().system( system ); }
	public static RepositoryGameParam company( String company ) { return new RepositoryGameParam().company( company ); }
	public static RepositoryGameParam year( String year ) { return new RepositoryGameParam().year( year ); }
	public static RepositoryGameParam country( String country ) { return new RepositoryGameParam().country( country ); }
	public static RepositoryGameParam isOriginal( boolean original ) { return new RepositoryGameParam().isOriginal( original ); }
	public static RepositoryGameParam originalText( String originalText ) { return new RepositoryGameParam().originalText( originalText ); }
	public static RepositoryGameParam mapper( String mapper ) { return new RepositoryGameParam().mapper( mapper ); }
	public static RepositoryGameParam start( String start ) { return new RepositoryGameParam().start( start ); }
	public static RepositoryGameParam remark( String remark ) { return new RepositoryGameParam().remark( remark ); }

	private RepositoryGame( RepositoryGameParam param )
	{
		this.title = param.title;
		this.system = param.system;
		this.company = param.company;
		this.year = param.year;
		this.country = param.country;
		
		this.original = param.original;
		this.originalText = param.originalText;
		this.mapper = param.mapper;
		this.start = param.start;
		this.remark = param.remark;
	}

	//--------
	// Getters
	//--------
	public String getTitle()	{ return title; }
	public String getSystem()	{ return system; }
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

	@Override
	public boolean equals( Object obj )
	{
		if( obj == null || !(obj instanceof RepositoryGame) )
		{
			return false;
		}
		else if( this == obj )
		{
			return true;
		}
		else
		{
			RepositoryGame other = (RepositoryGame)obj;
			return other.getTitle().equals( title ) && other.getSystem().equals( system ) &&
					other.getCompany().equals( company ) && other.getYear().equals( year ) &&
					other.getCountry().equals( country );
		}
	}

	@Override
	public int hashCode()
	{
		return Objects.hash( title, system, company, year, country );
	}
}
