/*
 * Copyright 2016 Sam Elsharif
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
package info.msxlaunchers.openmsx.launcher.persistence.game;

import info.msxlaunchers.openmsx.common.Utils;
import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.persistence.LauncherPersistenceException;
import info.msxlaunchers.openmsx.launcher.persistence.TransactionalDatabaseOperation;

/**
 * Parent class to save and update game actions. It contains common validation
 * 
 * @since v1.7
 * @author Sam Elsharif
 *
 */
abstract class AbstractPersistGameAction extends TransactionalDatabaseOperation<Boolean>
{
	protected void validateGame( Game game ) throws LauncherPersistenceException
	{
		if( Utils.isEmpty( game.getName() ) )
		{
			throwEncapsulatingException( new GamePersistenceException( GamePersistenceExceptionIssue.GAME_WITH_NULL_NAME ) );
		}

		if( Utils.isEmpty( game.getRomA() ) &&  Utils.isEmpty( game.getRomB() ) &&
				Utils.isEmpty( game.getDiskA() ) &&  Utils.isEmpty( game.getDiskB() ) &&
				Utils.isEmpty( game.getTape() ) &&  Utils.isEmpty( game.getHarddisk() ) &&
				Utils.isEmpty( game.getLaserdisc() ) && Utils.isEmpty( game.getTclScript() ) )
		{
			throwEncapsulatingException( new GamePersistenceException( GamePersistenceExceptionIssue.GAME_WITH_MISSING_MEDIA ) );
		}
	}
}
