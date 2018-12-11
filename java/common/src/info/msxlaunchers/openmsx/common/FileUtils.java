/*
 * Copyright 2014 Sam Elsharif
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
package info.msxlaunchers.openmsx.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Utility class that contains static file utility methods 
 * 
 * @since v1.3
 * @author Sam Elsharif
 */
public final class FileUtils
{
	/**
	 * Returns file name without extension
	 * 
	 * @param filename File or directory name
	 * @return File or directory name without extension, or null if given null
	 */
	public static String getFileNameWithoutExtension( String filename )
	{
		String filenameWithoutExt = null;

		if( filename != null )
		{
			String filePortion = new File( filename ).getName();
			int pos = filePortion.lastIndexOf( "." );

	        if( pos == -1 )
	        {
	        	filenameWithoutExt = filePortion;
	        }
	        else
	        {
	        	filenameWithoutExt = filePortion.substring( 0, pos );
	        }
		}

		return filenameWithoutExt;
	}

	/**
	 * Returns file or directory name without extension
	 * 
	 * @param file File that represents a file or directory name
	 * @return File or directory name without extension, or null if given null
	 */
	public static String getFileNameWithoutExtension( File file )
	{
		String filename = null;

		if( file != null )
		{
			return getFileNameWithoutExtension( file.getName() );
		}

		return filename;
	}

	/**
	 * Returns a list of filenames without full path of all files that belong to the same group as the given file
	 * 
	 * @param file Full path of the disk or tape file
	 * @return List of file names without the path (e.g. disk_1.dsk, disk_2.dsk, disk_3.dsk)
	 * @throws IOException
	 */
	public static List<String> getFileGroup( Path file ) throws IOException
	{
		if( !file.toFile().exists() )
		{
			return Collections.emptyList();
		}

		Path folder = file.getParent();
		List<String> files = Files.list( folder ).filter( Files::isRegularFile ).map( Path::getFileName ).map( Object::toString ).sorted().collect( Collectors.toList() );

		String filename = file.getFileName().toString();

		//at this point check the file naming convention - there are two:
		//1- nameX.ext where X is a sequence of consecutive digits of characters (e.g. 1,2,3 or a,b,c), or
		//2- name (Disk x of y).ext where x and y are digits (e.g. Disk 1 of 3)
		//code for handling each case is different

		int diskNumberFormat = filename.indexOf( "(Disk " );
		if( diskNumberFormat > -1 &&
				Character.isDigit( filename.charAt( diskNumberFormat + 6 ) ) &&
				filename.substring( diskNumberFormat + 8, diskNumberFormat + 11 ).equals( "of " ) &&
				Character.isDigit( filename.charAt( diskNumberFormat + 11 ) ) &&
				filename.charAt( diskNumberFormat + 12 ) == ')' )
		{
			return getFileGroupForDiskXofYFormat( files, filename );
		}
		else
		{
			return getFileGroupForSimpleSequentialFormat( files, filename );
		}
	}

	private static List<String> getFileGroupForDiskXofYFormat( List<String> files, String filename )
	{
		String escapedFilename = filename.replaceAll( "[\\W]", "\\\\$0" );

		int diskNumberFormat = escapedFilename.indexOf( "(Disk\\ " );
		StringBuilder builder = new StringBuilder( escapedFilename );
		builder.setCharAt( diskNumberFormat + 7, '.' );
		escapedFilename = builder.toString();

		Pattern pattern = Pattern.compile( escapedFilename );
		List<String> potentialMatches = files.stream().filter( f -> pattern.matcher( f ).matches() ).sorted().collect( Collectors.toList() );

		//get a list of the characters that we need to examine to determine if they are a sequence
		int indexOfCharacterToMatch = filename.indexOf( "(Disk " ) + 6;
		List<Character> charachtersToMatch = potentialMatches.stream().map( s -> s.charAt( indexOfCharacterToMatch ) ).collect( Collectors.toList() );

		//need to find all sequences before and after the index of the match
		//start going backwards to locate the starting index of the first element in the group
		int index =  potentialMatches.indexOf( filename );
		char characterToMatch = charachtersToMatch.get( index );
		boolean done = false;
		while( index > 0 && !done )
		{
			if( characterToMatch == charachtersToMatch.get( index - 1 ) + 1 )
			{
				characterToMatch--;
				index--;
			}
			else
			{
				done = true;
			}
		}

		//now go up the list and find the group files
		List<String> matches = new ArrayList<>( potentialMatches.size() );

		characterToMatch = charachtersToMatch.get( index );
		done = false;
		while( index < charachtersToMatch.size() && !done )
		{
			if( characterToMatch == charachtersToMatch.get( index ) )
			{
				matches.add( potentialMatches.get( index++ ) );
				characterToMatch++;
			}
			else
			{
				done = true;
			}
		}

		return matches;
	}

	private static List<String> getFileGroupForSimpleSequentialFormat( List<String> files, String filename )
	{
		//get a list of the files that can be potentially in the same group - they will have the same length
		String escapedFilename = filename.replaceAll( "[\\W]", "\\\\$0" );

		int firstAlphanumericIndex = escapedFilename.lastIndexOf( '.' );
		for( ;!Character.isLetterOrDigit( escapedFilename.charAt( firstAlphanumericIndex ) ); firstAlphanumericIndex-- ) {}
		StringBuilder builder = new StringBuilder( escapedFilename );

		builder.setCharAt( firstAlphanumericIndex, '.' );
		escapedFilename = builder.toString();

		Pattern pattern = Pattern.compile( escapedFilename );
		List<String> potentialMatches = files.stream().filter( f -> pattern.matcher( f ).matches() ).sorted().collect( Collectors.toList() );

		if( potentialMatches.size() <= 1 )
		{
			//if its a single element it means there's no group -> just return a list of this one element
			//if file doesn't exist (which is not supposed to happen) return an empty list
			return potentialMatches;
		}

		//get a list of the characters that we need to examine to determine if they are a sequence
		int indexOfCharacterToMatch = filename.lastIndexOf( '.' ) - 1;
		List<Character> charachtersToMatch = potentialMatches.stream().map( s -> s.charAt( indexOfCharacterToMatch ) ).collect( Collectors.toList() );

		//need to find all sequences before and after the index of the match
		//start going backwards to locate the starting index of the first element in the group
		int index =  potentialMatches.indexOf( filename );
		char characterToMatch = charachtersToMatch.get( index );
		boolean done = false;
		while( index > 0 && !done )
		{
			if( characterToMatch == charachtersToMatch.get( index - 1 ) + 1 )
			{
				characterToMatch--;
				index--;
			}
			else
			{
				done = true;
			}
		}

		//now go up the list and find the group files
		List<String> matches = new ArrayList<>( potentialMatches.size() );

		characterToMatch = charachtersToMatch.get( index );
		done = false;
		while( index < charachtersToMatch.size() && !done )
		{
			if( characterToMatch == charachtersToMatch.get( index ) )
			{
				matches.add( potentialMatches.get( index++ ) );
				characterToMatch++;
			}
			else
			{
				done = true;
			}
		}

		return matches;
	}
}
