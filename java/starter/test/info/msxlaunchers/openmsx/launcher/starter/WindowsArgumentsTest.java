package info.msxlaunchers.openmsx.launcher.starter;

import java.io.File;

import info.msxlaunchers.openmsx.launcher.data.game.Game;
import info.msxlaunchers.openmsx.launcher.data.game.constants.FDDMode;
import info.msxlaunchers.openmsx.launcher.data.settings.Settings;
import info.msxlaunchers.openmsx.launcher.starter.WindowsStarterArguments;
import info.msxlaunchers.platform.ArgumentsBuilder;

import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class WindowsArgumentsTest
{
	@Test
	public void testGetArgumentsWithScripts()
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
				.build();
		
		Settings settings = new Settings( "/openMSX dir/",
				"/openMSX/share/machines",
				null,
				null,
				null,
				false,
				false );

		ArgumentsBuilder argsBuilder = mock( ArgumentsBuilder.class );
		Mockito.doNothing().when( argsBuilder ).appendIfValueDefined( any( String.class ), any( String.class ) );

		WindowsStarterArguments windowsArguments = new WindowsStarterArguments( argsBuilder, "directory" );

		windowsArguments.getArguments( settings,  game );

		verify( argsBuilder, times( 1 ) ).append( new File( "/openMSX dir/openmsx.exe" ).getAbsolutePath() );

		//verify that only script was appended
		verify( argsBuilder, times( 1 ) ).appendIfValueDefined( eq( "-script" ), any( String.class ) );
		verify( argsBuilder, times( 1 ) ).appendIfValueDefined( any( String.class ), any( String.class ) );
	}

	@Test
	public void testGetArgumentsWithoutScripts()
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
		
		Settings settings = new Settings( "/openMSX dir/",
				"/openMSX/share/machines",
				null,
				null,
				null,
				false,
				false );

		ArgumentsBuilder argsBuilder = mock( ArgumentsBuilder.class );
		Mockito.doNothing().when( argsBuilder ).appendIfValueDefined( any( String.class ), any( String.class ) );

		WindowsStarterArguments windowsArguments = new WindowsStarterArguments( argsBuilder, "directory" );

		windowsArguments.getArguments( settings,  game );

		verify( argsBuilder, times( 1 ) ).append( new File( "/openMSX dir/openmsx.exe" ).getAbsolutePath() );

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

		verify( argsBuilder, times( 10 ) ).appendIfValueDefined( any( String.class ), any( String.class ) );
	}

	@Test
	public void testGetArgumentsWithoutScriptsButWithPressCtrlWhileBoot()
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
				.fddMode( FDDMode.DISABLE_SECOND )
				.build();
		
		Settings settings = new Settings( "/openMSX dir/",
				"/openMSX/share/machines",
				null,
				null,
				null,
				false,
				false );

		ArgumentsBuilder argsBuilder = mock( ArgumentsBuilder.class );
		Mockito.doNothing().when( argsBuilder ).appendIfValueDefined( any( String.class ), any( String.class ) );

		WindowsStarterArguments arguments = new WindowsStarterArguments( argsBuilder, "directory" );

		arguments.getArguments( settings,  game );

		verify( argsBuilder, times( 1 ) ).append( new File( "/openMSX dir/openmsx.exe" ).getAbsolutePath() );

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
		verify( argsBuilder, times( 1 ) ).appendIfValueDefined( eq( "-script" ), eq( new File( "directory", "pressctrl.tcl").toString() ) );

		verify( argsBuilder, times( 10 ) ).appendIfValueDefined( any( String.class ), any( String.class ) );
	}

	@Test
	public void testGetArgumentsWithoutScriptsButWithPressShiftWhileBoot()
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
				.fddMode( FDDMode.DISABLE_BOTH )
				.build();
		
		Settings settings = new Settings( "/openMSX dir/",
				"/openMSX/share/machines",
				null,
				null,
				null,
				false,
				false );

		ArgumentsBuilder argsBuilder = mock( ArgumentsBuilder.class );
		Mockito.doNothing().when( argsBuilder ).appendIfValueDefined( any( String.class ), any( String.class ) );

		WindowsStarterArguments arguments = new WindowsStarterArguments( argsBuilder, "directory" );

		arguments.getArguments( settings,  game );

		verify( argsBuilder, times( 1 ) ).append( new File( "/openMSX dir/openmsx.exe" ).getAbsolutePath() );

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
		verify( argsBuilder, times( 1 ) ).appendIfValueDefined( eq( "-script" ), eq( new File( "directory", "pressshift.tcl").toString() ) );

		verify( argsBuilder, times( 10 ) ).appendIfValueDefined( any( String.class ), any( String.class ) );
	}
}
