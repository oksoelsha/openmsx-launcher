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
package info.msxlaunchers.openmsx.game.repository;

import info.msxlaunchers.openmsx.game.repository.processor.XMLProcessorModule;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;

/**
 * @since v1.0
 * @author Sam Elsharif
 *
 */
public class RepositoryDataModule extends AbstractModule
{
	@Override 
	protected void configure()
	{
		bind( RepositoryData.class ).to( XMLRepositoryData.class );

		install( new XMLProcessorModule() );

		Multibinder<XMLFileGetter> multibinder = Multibinder.newSetBinder( binder(), XMLFileGetter.class );
		multibinder.addBinding().to( ROMXMLFileGetter.class );
		multibinder.addBinding().to( DiskXMLFileGetter.class );
		multibinder.addBinding().to( TapeXMLFileGetter.class );
	}
}