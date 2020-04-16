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
package info.msxlaunchers.openmsx.launcher.extractor;

/**
 * Container for extraction operation results
 * 
 * @since v1.14
 * @author Sam Elsharif
 *
 */
public final class ExtractorData
{
	private final int totalFiles;
	private final int totalMSXImages;
	private final int totalExtractedFiles;

	public ExtractorData( int totalFiles, int totalMSXImages, int totalExtractedFiles )
	{
		this.totalFiles = totalFiles;
		this.totalMSXImages = totalMSXImages;
		this.totalExtractedFiles = totalExtractedFiles;
	}

	public int getTotalFiles()
	{
		return totalFiles;
	}

	public int getTotalMSXImages()
	{
		return totalMSXImages;
	}

	public int getTotalExtractedFiles()
	{
		return totalExtractedFiles;
	}
}
