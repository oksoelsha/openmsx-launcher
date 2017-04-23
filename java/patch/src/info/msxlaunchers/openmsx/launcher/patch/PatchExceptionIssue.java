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

/**
 * Enum class to list all Patcher exception issues
 * 
 * @author Sam Elsharif
 * @since v1.9
 */
public enum PatchExceptionIssue
{
	INVALID_PATCH_FILE,
	FILE_TO_PATCH_NOT_PATCHABLE,
	TARGET_FILE_CANNOT_WRITE,
	IO,
	SOURCE_FILE_CHECKSUM_NOT_MATCH,
	ZIP_SOURCE_FILE_CANNOT_BE_PATCHED_DIRECTLY
}
