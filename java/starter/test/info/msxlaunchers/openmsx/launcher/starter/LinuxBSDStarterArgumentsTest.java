package info.msxlaunchers.openmsx.launcher.starter;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.game.constants.FDDMode;
import info.msxlaunchers.openmsx.launcher.data.game.constants.InputDevice;
import info.msxlaunchers.openmsx.launcher.starter.LinuxBSDStarterArguments;

import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class LinuxBSDStarterArgumentsTest extends AbstractStarterArgumentTest
{
	@Test
	public void testGetArgumentsWithScriptsAndOverride() throws IOException
	{
		Game game = Game.machine( "Boosted_MSX2_EN" )
				.romA( "romA" )
				.romB( "romB" )
				.extensionRom( "extensionRom" )
				.diskA( "diskA" )
				.diskB( "diskB" )
				.tape( "tape" )
				.harddisk( "harddisk" )
				.machine( "machine" )
				.laserdisc( "laserdisc" )
				.tclScript( "tclScript" )
				.tclScriptOverride( true )
				.build();

		LinuxBSDStarterArguments arguments = new LinuxBSDStarterArguments( argsBuilder );

		arguments.getArguments( settings,  game );

		verify( argsBuilder, times( 1 ) ).append( new File( "/openMSX dir/openmsx" ).getAbsolutePath() );

		//verify that only script was appended
		verify( argsBuilder, times( 1 ) ).appendIfValueDefined( eq( "-script" ), any( String.class ) );
		verify( argsBuilder, times( 1 ) ).appendIfValueDefined( any( String.class ), any( String.class ) );
	}

	@Test
	public void testGetArgumentsWithoutScriptsOverride() throws IOException
	{
		Game game = Game.machine( "Boosted_MSX2_EN" )
				.romA( "romA" )
				.romB( "romB" )
				.extensionRom( "extensionRom" )
				.diskA( "diskA" )
				.diskB( "diskB" )
				.tape( "tape" )
				.harddisk( "harddisk" )
				.machine( "machine" )
				.laserdisc( "laserdisc" )
				.build();
		
		LinuxBSDStarterArguments arguments = new LinuxBSDStarterArguments( argsBuilder );

		arguments.getArguments( settings,  game );

		verify( argsBuilder, times( 1 ) ).append( new File( "/openMSX dir/openmsx" ).getAbsolutePath() );

		//verify that all media were appended
		verify( argsBuilder, times( 1 ) ).appendIfValueDefined( eq( "-carta" ), any( String.class ) );
		verify( argsBuilder, times( 1 ) ).appendIfValueDefined( eq( "-cartb" ), any( String.class ) );
		verify( argsBuilder, times( 1 ) ).appendIfValueDefined( eq( "-ext" ), any( String.class ) );
		verify( argsBuilder, times( 1 ) ).appendIfValueDefined( eq( "-diska" ), any( String.class ) );
		verify( argsBuilder, times( 1 ) ).appendIfValueDefined( eq( "-diskb" ), any( String.class ) );
		verify( argsBuilder, times( 1 ) ).appendIfValueDefined( eq( "-cassetteplayer" ), any( String.class ) );
		verify( argsBuilder, times( 1 ) ).appendIfValueDefined( eq( "-hda" ), any( String.class ) );
		verify( argsBuilder, times( 1 ) ).appendIfValueDefined( eq( "-machine" ), any( String.class ) );
		verify( argsBuilder, times( 1 ) ).appendIfValueDefined( eq( "-laserdisc" ), any( String.class ) );
		verify( argsBuilder, times( 1 ) ).appendIfValueDefined( eq( "-script" ), eq( null ) );

		verify( argsBuilder, times( 11 ) ).appendIfValueDefined( any( String.class ), any( String.class ) );
	}

	@Test
	public void testGetArgumentsWithInputDevice() throws IOException
	{
		Game game = Game.machine( "Boosted_MSX2_EN" )
				.romA( "romA" )
				.inputDevice( InputDevice.JOYSTICK )
				.build();

		LinuxBSDStarterArguments arguments = new LinuxBSDStarterArguments( argsBuilder );

		arguments.getArguments( settings,  game );

		verify( argsBuilder, times( 2 ) ).appendIfValueDefined( eq( "-script" ), any( String.class ) );
	}

	@Test
	public void testGetArgumentsWithFDDMode() throws IOException
	{
		Game game = Game.machine( "Boosted_MSX2_EN" )
				.romA( "romA" )
				.fddMode( FDDMode.DISABLE_SECOND )
				.build();

		LinuxBSDStarterArguments arguments = new LinuxBSDStarterArguments( argsBuilder );

		arguments.getArguments( settings,  game );

		verify( argsBuilder, times( 2 ) ).appendIfValueDefined( eq( "-script" ), any( String.class ) );
	}

	@Test
	public void testGetArgumentsWithConnectGFX9000() throws IOException
	{
		Game game = Game.machine( "Boosted_MSX2_EN" )
				.romA( "romA" )
				.connectGFX9000( true )
				.build();

		LinuxBSDStarterArguments arguments = new LinuxBSDStarterArguments( argsBuilder );

		arguments.getArguments( settings,  game );

		verify( argsBuilder, times( 2 ) ).appendIfValueDefined( eq( "-script" ), any( String.class ) );
	}

	@Test
	public void testHandlingOfTempScriptFiles() throws IOException
	{
		Path tempDirectory = Paths.get( System.getProperty( "java.io.tmpdir" ) );
		String fileMatch = "openmsx-launcher-script*.tmp";

		List<Path> matches = new ArrayList<>();

		try ( DirectoryStream<Path> newDirectoryStream = Files.newDirectoryStream( tempDirectory, fileMatch ) )
		{
			for( Path newDirectoryStreamItem : newDirectoryStream )
			{
				matches.add( newDirectoryStreamItem );
			}
		}

		//there may or may not be a temporary file from a previous run but it should be no more than
		assertTrue( matches.size() == 1 || matches.isEmpty() );

		String currentTempraryFile = null;
		if( matches.size() == 1 )
		{
			currentTempraryFile = matches.get( 0 ).toString();
		}

		Game game = Game.machine( "Boosted_MSX2_EN" )
				.romA( "romA" )
				.connectGFX9000( true )
				.build();

		LinuxBSDStarterArguments arguments = new LinuxBSDStarterArguments( argsBuilder );

		arguments.getArguments( settings,  game );

		matches.clear();

		try ( DirectoryStream<Path> newDirectoryStream = Files.newDirectoryStream( tempDirectory, fileMatch ) )
		{
			for( Path newDirectoryStreamItem : newDirectoryStream )
			{
				matches.add( newDirectoryStreamItem );
			}
		}

		//there should be one temporary file now and it is different than the previous one if it existed
		assertTrue( matches.size() == 1 );
		assertTrue( !matches.get( 0 ).toString().equals( currentTempraryFile ) );
	}
}
