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

import info.msxlaunchers.openmsx.common.ActionDecider;

/**
 * Interface for extracting from compressed files
 * 
 * @author Sam Elsharif
 * @since v1.14
 */
public interface Extractor
{
	/**
	 * Extract the given file into the given directory
	 * 
	 * @param fileToExtract Full path to the file to extract
	 * @param targetDirectory Full directory path to extracted files. If null, the original file's directory will be written to
	 * @param extractOnlyMsxImages If true extract only MSX images (ROMs, disks and tapes) and ignore other types
	 * @param actionDecider Reference to an ActionDecider implementation that prompts the caller for actions when an extracted file exists in target directory
	 * @return data about the extraction operation
	 * @throws ExtractionException
	 */
	ExtractorData extract( String fileToExtract, String targetDirectory, boolean extractOnlyMsxImages, ActionDecider actionDecider ) throws ExtractionException;
}
