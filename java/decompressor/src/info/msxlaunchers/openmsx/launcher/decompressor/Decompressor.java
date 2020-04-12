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
package info.msxlaunchers.openmsx.launcher.decompressor;

/**
 * Interface for decompressing files
 * 
 * @author Sam Elsharif
 * @since v1.14
 */
public interface Decompressor
{
	/**
	 * Decompress the given file into the given directory
	 * 
	 * @param fileToDecompress Full path to the file to decompress
	 * @param targetDirectory Full directory path to decompressed files. If null, the original file's directory will be written to
	 * @param decompressOnlyMsxFiles If true decompress only MSX files (ROMs, disks and tapes) and ignore other types
	 * @throws DecompressionException
	 */
	void decompress( String fileToDecompress, String targetDirectory, boolean decompressOnlyMsxFiles ) throws DecompressionException;
}
