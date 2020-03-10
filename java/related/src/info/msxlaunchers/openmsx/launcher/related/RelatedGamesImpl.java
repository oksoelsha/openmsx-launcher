/*
 * Copyright 2020 Sam Elsharif
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
package info.msxlaunchers.openmsx.launcher.related;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;

import info.msxlaunchers.openmsx.common.Utils;
import info.msxlaunchers.openmsx.launcher.data.extra.ExtraData;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.game.RelatedGame;
import info.msxlaunchers.openmsx.launcher.data.game.constants.Genre;
import info.msxlaunchers.openmsx.launcher.data.repository.RepositoryGame;
import info.msxlaunchers.openmsx.launcher.extra.ExtraDataGetter;

/**
 * 
 * Implementation of the interface <code>RelatedGames</code> that returns a maximum of 15 games related to the given name
 * based on name, company and genre.
 * 
 * @since v1.13
 * @author Sam Elsharif
 */
class RelatedGamesImpl implements RelatedGames
{
	private static final int NAME_MATCH_ONE_WORD_GAME_SCORE = 5;
	private static final int NAME_MATCH_ONE_IN_MANY_WORDS_SCORE = 3;
	private static final int NAME_MATCH_TWO_OR_MORE_IN_MANY_WORDS_SCORE = 5;
	private static final int GENRE_MATCH_SCORE = 4;
	private static final int COMPANY_MATCH_SCORE = 2;
	private static final int MAX_SIZE_RESULTS = 15;
	private static final Set<String> excludedStrings;
	static
	{
		excludedStrings = new HashSet<>();

		excludedStrings.add( "-" );
		excludedStrings.add( "i" );
		excludedStrings.add( "ii" );

		excludedStrings.add( "the" );
		excludedStrings.add( "of" );
		excludedStrings.add( "and" );
		excludedStrings.add( "in" );
		excludedStrings.add( "on" );
		excludedStrings.add( "at" );
		excludedStrings.add( "to" );

		excludedStrings.add( "de" );
		excludedStrings.add( "el" );
		excludedStrings.add( "los" );
		excludedStrings.add( "en" );
	}

	private final ExtraDataGetter extraDataGetter;
	Map<String,RepositoryGame> repositoryInfoMap;

	@Inject
	RelatedGamesImpl( ExtraDataGetter extraDataGetter, @Assisted Map<String,RepositoryGame> repositoryInfoMap )
	{
		this.extraDataGetter = extraDataGetter;
		this.repositoryInfoMap = repositoryInfoMap;
	}

	/* (non-Javadoc)
	 * @see info.msxlaunchers.openmsx.launcher.related.RelatedGames#findRelated(info.msxlaunchers.openmsx.launcher.data.game.Game)
	 */
	@Override
	public List<RelatedGame> findRelated( Game game )
	{
		Map<String, ExtraData> extraDataMap = null;

		try
		{
			extraDataMap = extraDataGetter.getExtraData();
		}
		catch( IOException ioe )
		{
			//TODO - why do we reload this all the time?
			throw new RuntimeException();
		}

		Set<SimilarGame> similarGames = new HashSet<>();

		RepositoryGame repositoryGame = repositoryInfoMap.get( game.getSha1Code() );

		String selectedGameTitleFromRepository;
		String companyOfSelectedGame;
		Set<String> gameNameParts;
		if( repositoryGame == null )
		{
			selectedGameTitleFromRepository = "";
			companyOfSelectedGame = "";
			gameNameParts = getNormalizedStrings( game.getName() );
		}
		else
		{
			selectedGameTitleFromRepository = repositoryGame.getTitle();
			companyOfSelectedGame = repositoryGame.getCompany();
			gameNameParts = getNormalizedStrings( repositoryGame.getTitle() );
		}

		Set<RepositoryGame> repeatedRepositoryGame = new HashSet<>();

		for( Map.Entry<String,RepositoryGame> entry: repositoryInfoMap.entrySet() )
		{
			String companyOfRepositoryGame = entry.getValue().getCompany();
			String repositoryTitle = entry.getValue().getTitle();

			//skip the score if both name and company are the same
			if( !( selectedGameTitleFromRepository.equals( repositoryTitle ) && companyOfRepositoryGame.equals( companyOfSelectedGame ) ) )
			{
				if( repeatedRepositoryGame.add( entry.getValue() ) )
				{
					int score = 0;
					String sha1CodeOfRepositoryGame = entry.getKey();
					ExtraData extraData = extraDataMap.get( sha1CodeOfRepositoryGame );

					score += getNameScore( repositoryTitle, gameNameParts );
					score += getGenreScore( extraData, game );
					score += getCompanyScore( companyOfRepositoryGame, companyOfSelectedGame );
		
					if( score > 0 )
					{
						similarGames.add( new SimilarGame( new RelatedGame( repositoryTitle, entry.getValue().getCompany(), entry.getValue().getYear(),
							extraData != null? extraData.getMSXGenerationsID() : 0 ), score ) );
					}
				}
			}
		}

		//order by score and get top 15
		return similarGames.stream()
				.sorted( Comparator.comparingInt( SimilarGame::getScore ).reversed() )
				.map( g -> g.relatedGame )
				.limit( MAX_SIZE_RESULTS )
				.collect( Collectors.toList() );
	}

	private int getNameScore( String repositoryTitle, Set<String> gameNameParts )
	{
		Set<String> repositoryTitleParts = getNormalizedStrings( repositoryTitle );
		int score = 0;

		int matches = 0;
		for( String part: gameNameParts )
		{
			if( repositoryTitleParts.contains( part ) )
			{
				if( repositoryTitleParts.size() == 1 || gameNameParts.size() == 1 )
				{
					score += NAME_MATCH_ONE_WORD_GAME_SCORE;
				}
				else
				{
					matches++;
					score += (matches == 1) ? NAME_MATCH_ONE_IN_MANY_WORDS_SCORE : NAME_MATCH_TWO_OR_MORE_IN_MANY_WORDS_SCORE;
				}
			}
		}

		return score;
	}

	private int getGenreScore( ExtraData extraData, Game game )
	{
		int score = 0;

		if( extraData != null )
		{
			Genre selectedGameGenre1 = game.getGenre1();
			Genre selectedGameGenre2 = game.getGenre2();
			Genre genre1OfRepositoryGame = Genre.fromValue( extraData.getGenre1() );
			Genre genre2OfRepositoryGame = Genre.fromValue( extraData.getGenre2() );

			if( (!selectedGameGenre1.equals( Genre.UNKNOWN ) && (selectedGameGenre1.equals( genre1OfRepositoryGame ) || selectedGameGenre1.equals( genre2OfRepositoryGame ))) ||
					(!selectedGameGenre2.equals( Genre.UNKNOWN ) && (selectedGameGenre2.equals( genre1OfRepositoryGame ) || selectedGameGenre2.equals( genre2OfRepositoryGame ))) )
			{
				score = GENRE_MATCH_SCORE;
			}
		}

		return score;
	}

	private int getCompanyScore( String companyOfRepositoryGame, String companyOfSelectedGame )
	{
		if( !companyOfRepositoryGame.isEmpty() && companyOfRepositoryGame.equals( companyOfSelectedGame ) )
		{
			return COMPANY_MATCH_SCORE;
		}
		else
		{
			return 0;
		}
	}

	private class SimilarGame
	{
		private final RelatedGame relatedGame;
		private final int score;

		SimilarGame( RelatedGame relatedGame, int score )
		{
			this.relatedGame = relatedGame;
			this.score = score;
		}

		int getScore() { return score; }

		@Override
		public boolean equals( Object other )
		{
			if( other == null || !(other instanceof SimilarGame) )
			{
				return false;
			}
			else
			{
				return relatedGame.equals( ((SimilarGame)other).relatedGame );
			}
		}

		@Override
		public int hashCode()
		{
			return relatedGame.hashCode();
		}
	}

	private Set<String> getNormalizedStrings( String string )
	{
		String[] parts = string.split( " " );

		return Stream.of( parts )
				.map( String::toLowerCase )
				.map( s -> s.replace( ",", "" ) )
				.filter( s -> !excludedStrings.contains( s ) )
				.filter( s -> !Utils.isNumber( s ) )
				.collect( Collectors.toSet() );
	}
}
