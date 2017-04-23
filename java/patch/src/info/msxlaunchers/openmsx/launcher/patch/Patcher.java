/*
 * Copyright 2017 Sam Elsharif
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
package info.msxlaunchers.openmsx.launcher.patch;

import java.nio.file.Path;

/**
 * Interface for file patching
 * 
 * @author Sam Elsharif
 * @since v1.9
 */
public interface Patcher
{
	/**
	 * Patch the given file with the given patch
	 * 
	 * @param fileToPatch Full path to the file to patch
	 * @param patchFile Full path to the patch file
	 * @param targetFile Full path to the target file. If null, the original file will be patched directly. If targetFile exists
	 *        then it will be overwritten
	 * @param checksum Checksum of the source file to check before patching. Skip if null
	 * @throws PatchException
	 */
	void patch( Path fileToPatch, Path patchFile, Path targetFile, String checksum ) throws PatchException;
}
