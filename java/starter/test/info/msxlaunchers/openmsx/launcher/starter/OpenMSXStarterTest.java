package info.msxlaunchers.openmsx.launcher.starter;

import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.settings.Settings;
import info.msxlaunchers.openmsx.launcher.starter.EmulatorStarter;
import info.msxlaunchers.openmsx.launcher.starter.OpenMSXStarter;
import info.msxlaunchers.openmsx.launcher.starter.StarterPlatformArguments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith( MockitoJUnitRunner.class )
public class OpenMSXStarterTest
{
	@Mock StarterPlatformArguments starterPlatformArguments;

	@Test( expected = NullPointerException.class )
	public void testForNullConditionsConstructorArg() throws IOException
	{
		new OpenMSXStarter( null );
	}

	@Test( expected = NullPointerException.class )
	public void testForNullConditionsStartArg1() throws IOException
	{
		OpenMSXStarter openMSXStarter = new OpenMSXStarter( starterPlatformArguments );

		Game game = Game.machine( "Boosted_MSX2_EN" )
				.diskA( "/Games/MSX System/Software/DSK/Army-En.zip" )
				.build();

		openMSXStarter.start( null, game );
	}

	@Test( expected = NullPointerException.class )
	public void testForNullConditionsStartArg2() throws IOException
	{
		OpenMSXStarter openMSXStarter = new OpenMSXStarter( starterPlatformArguments );

		Settings settings = new Settings( null, null, null, null, null );

		openMSXStarter.start( settings, null );
	}

	@Test @Ignore
	public void testStarting()
	{
		Game game = Game.machine( "ignore" )
				.diskA( "ignore" )
				.build();
		
		Settings settings = new Settings( null,
				null,
				null,
				null,
				null );

		//this unit test will only work on Windows
		List<String> args = new ArrayList<String>();
		args.add( "/Games/MSX System/openMSX/0.10.1 64bit/openmsx.exe" );

		when( starterPlatformArguments.getArguments( any( Settings.class ), any( Game.class ) ) ).thenReturn( args );

		EmulatorStarter starter = new OpenMSXStarter( starterPlatformArguments );
		Process process = null;

		try
		{
			process = starter.start( settings, game );
		}
		catch( IOException ioex )
		{
			assertTrue( false );
		}
		finally
		{
			if( process != null )
			{
				process.destroy();

				try
				{
					process.waitFor();
				}
				catch( InterruptedException ie )
				{
					//Need to review what to do in this case
				}

				assertEquals( process.exitValue(), 1 );
			}
			else
			{
				assertTrue( false );
			}
		}
	}

	@Test( expected = IOException.class )
	public void testWrongEmulatorPath() throws IOException
	{
		Game game = Game.machine( "ignore" )
				.diskA( "ignore" )
				.build();
		
		Settings settings = new Settings( null,
				null,
				null,
				null,
				null );

		//this unit test will only work on Windows
		List<String> args = new ArrayList<String>();
		args.add( "/wrong path/openmsx.exe" );

		when( starterPlatformArguments.getArguments( any( Settings.class ), any( Game.class ) ) ).thenReturn( args );

		EmulatorStarter starter = new OpenMSXStarter( starterPlatformArguments );

		starter.start( settings, game );
	}

}
