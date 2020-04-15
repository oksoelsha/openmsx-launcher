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
package info.msxlaunchers.openmsx.launcher.ui.presenter;

/**
 * Enum class to list all error codes
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
public enum LauncherExceptionCode
{
	ERR_CANNOT_SAVE_SETTINGS,
	ERR_CANNOT_START_OPENMSX,
	ERR_IO,
	ERR_DATABASE_NOT_FOUND,
	ERR_DATABASE_ALREADY_EXISTS,
	ERR_DATABASE_MAX_BACKUPS_REACHED,
	ERR_DATABASE_NULL_NAME,
	ERR_EMPTY_GAME_FIELDS,
	ERR_GAME_ALREADY_EXISTS,
	ERR_GAME_NOT_FOUND,
	ERR_GAME_WITH_NULL_NAME,
	ERR_INVALID_MACHINES_DIRECTORY,
	ERR_CANNOT_VIEW_GAMEINFO,
	ERR_CANNOT_VIEW_HELP_FILE,
	ERR_CANNOT_LOCATE_FILE,
	ERR_FILTER_ALREADY_EXISTS,
	ERR_CANNOT_CONTACT_SERVER,
	ERR_CANNOT_INSTALL_NEW_UPDATED_FILES,
	ERR_BACKUP_NOT_FOUND,
	ERR_INVALID_PATCH_FILE,
	ERR_FILE_TO_PATCH_NOT_PATCHABLE,
	ERR_SOURCE_FILE_CHECKSUM_NOT_MATCH,
	ERR_ZIP_SOURCE_FILE_CANNOT_BE_PATCHED_DIRECTLY,
	ERR_EMPTY_CHECKSUM,
	ERR_COMPRESSED_FILE_NOT_FOUND
}
