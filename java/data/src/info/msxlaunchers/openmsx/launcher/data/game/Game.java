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
package info.msxlaunchers.openmsx.launcher.data.game;

import java.io.Serializable;

import info.msxlaunchers.openmsx.common.Utils;
import info.msxlaunchers.openmsx.launcher.data.game.constants.FDDMode;
import info.msxlaunchers.openmsx.launcher.data.game.constants.Genre;

/**
 * This class represents a game with all relevant fields. It can only be instantiated through the fluid builder pattern
 * and not through a constructor.
 * You need to call the build() method in the end to get an object. An <code>IllegalArgumentException</code> will be thrown
 * when calling the build() method if all the following fields are null:
 * ROM A, ROM B, Disk A, Disk B, Tape, Laserdisc, Hard disk and Script 
 * 
 * @since v1.0
 * @author Sam Elsharif
 *
 */
public final class Game implements Serializable
{
	private static final long serialVersionUID = 6233080612490194173L;

	private final String name;
	private final String info;
	private final String machine;
	private final String romA, romB;
	private final String extensionRom;
	private final String diskA, diskB;
	private final String tape;
	private final String harddisk;
	private final String laserdisc;
	private final boolean isMSX, isMSX2, isMSX2Plus, isTurboR;
	private final boolean isPSG, isSCC, isSCCI, isPCM, isMSXMUSIC, isMSXAUDIO, isMoonsound, isMIDI;
	private final Genre genre1, genre2;
	private final int msxGenID;
	private final String sha1Code;
	private final long size;
	private final String screenshotSuffix;
	private final String tclScript;
	private final boolean tclScriptOverride;
	private final FDDMode fddMode;

	public static class GameParam
	{
		private String name;
		private String info;
		private String machine;
		private String romA, romB;
		private String extensionRom;
		private String diskA, diskB;
		private String tape;
		private String harddisk;
		private String laserdisc;
		private boolean isMSX, isMSX2, isMSX2Plus, isTurboR;
		private boolean isPSG, isSCC, isSCCI, isPCM, isMSXMUSIC, isMSXAUDIO, isMoonsound, isMIDI;
		private Genre genre1, genre2;
		private int msxGenID;
		private String sha1Code;
		private long size;
		private String screenshotSuffix;
		private String tclScript;
		private boolean tclScriptOverride;
		private FDDMode fddMode;

		public GameParam machine( String machine ) { this.machine = Utils.resetIfEmpty( machine ); return this; }
		public GameParam romA( String romA ) { this.romA = Utils.resetIfEmpty( romA ); return this; }
		public GameParam romB( String romB ) { this.romB = Utils.resetIfEmpty( romB ); return this; }
		public GameParam extensionRom( String extensionRom ) { this.extensionRom = Utils.resetIfEmpty( extensionRom ); return this; }
		public GameParam diskA( String diskA ) { this.diskA = Utils.resetIfEmpty( diskA ); return this; }
		public GameParam diskB( String diskB ) { this.diskB = Utils.resetIfEmpty( diskB ); return this; }
		public GameParam tape( String tape ) { this.tape = Utils.resetIfEmpty( tape ); return this; }
		public GameParam harddisk( String harddisk ) { this.harddisk = Utils.resetIfEmpty( harddisk ); return this; }
		public GameParam laserdisc( String laserdisc ) { this.laserdisc = Utils.resetIfEmpty( laserdisc ); return this; }
		public GameParam name( String name ) { this.name = Utils.resetIfEmpty( name ); return this; }
		public GameParam info( String info ) { this.info = Utils.resetIfEmpty( info ); return this; }
		public GameParam isMSX( boolean isMSX ) { this.isMSX = isMSX; return this; }
		public GameParam isMSX2( boolean isMSX2 ) { this.isMSX2 = isMSX2; return this; }
		public GameParam isMSX2Plus( boolean isMSX2Plus ) { this.isMSX2Plus = isMSX2Plus; return this; }
		public GameParam isTurboR( boolean isTurboR ) { this.isTurboR = isTurboR; return this; }
		public GameParam isPSG( boolean isPSG ) { this.isPSG = isPSG; return this; }
		public GameParam isSCC( boolean isSCC ) { this.isSCC = isSCC; return this; }
		public GameParam isSCCI( boolean isSCCI ) { this.isSCCI = isSCCI; return this; }
		public GameParam isPCM( boolean isPCM ) { this.isPCM = isPCM; return this; }
		public GameParam isMSXMUSIC( boolean isMSXMUSIC ) { this.isMSXMUSIC = isMSXMUSIC; return this; }
		public GameParam isMSXAUDIO( boolean isMSXAUDIO ) { this.isMSXAUDIO = isMSXAUDIO; return this; }
		public GameParam isMoonsound( boolean isMoonsound ) { this.isMoonsound = isMoonsound; return this; }
		public GameParam isMIDI( boolean isMIDI ) { this.isMIDI = isMIDI; return this; }
		public GameParam genre1( Genre genre1 ) { this.genre1 = genre1; return this; }
		public GameParam genre2( Genre genre2 ) { this.genre2 = genre2; return this; }
		public GameParam msxGenID( int msxGenID ) { this.msxGenID = msxGenID; return this; }
		public GameParam sha1Code( String sha1Code ) { this.sha1Code = Utils.resetIfEmpty( sha1Code ); return this; }
		public GameParam size( long size ) { this.size = size; return this; }
		public GameParam screenshotSuffix( String screenshotSuffix ) { this.screenshotSuffix = Utils.resetIfEmpty( screenshotSuffix ); return this; }
		public GameParam tclScript( String tclScript ) { this.tclScript = Utils.resetIfEmpty( tclScript ); return this; }
		public GameParam tclScriptOverride( boolean tclScriptOverride ) { this.tclScriptOverride = tclScriptOverride; return this; }
		public GameParam fddMode( FDDMode fddMode ) { this.fddMode = fddMode; return this; }

		public Game build()
		{
			//at least one of the following fields must be set
			if( name == null &&
					romA == null && romB == null
					&& diskA == null && diskB == null
					&& tape == null && harddisk == null && laserdisc == null && tclScript == null )
			{
				throw new IllegalArgumentException( "Must set at least one field" );
			}

			return new Game( this );
        }
	}

	public static GameParam machine( String machine ) { return new GameParam().machine( machine ); }
	public static GameParam romA( String romA ) { return new GameParam().romA( romA ); }
	public static GameParam romB( String romB ) { return new GameParam().romB( romB ); }
	public static GameParam extensionRom( String extensionRom ) { return new GameParam().extensionRom( extensionRom ); }
	public static GameParam diskA( String diskA ) { return new GameParam().diskA( diskA ); }
	public static GameParam diskB( String diskB ) { return new GameParam().diskB( diskB ); }
	public static GameParam tape( String tape ) { return new GameParam().tape( tape ); }
	public static GameParam harddisk( String harddisk ) { return new GameParam().harddisk( harddisk ); }
	public static GameParam laserdisc( String laserdisc ) { return new GameParam().laserdisc( laserdisc ); }
	public static GameParam name( String name ) { return new GameParam().name( name ); }
	public static GameParam info( String info ) { return new GameParam().info( info ); }
	public static GameParam isMSX( boolean isMSX ) { return new GameParam().isMSX( isMSX ); }
	public static GameParam isMSX2( boolean isMSX2 ) { return new GameParam().isMSX2( isMSX2 ); }
	public static GameParam isMSX2Plus( boolean isMSX2Plus ) { return new GameParam().isMSX2Plus( isMSX2Plus ); }
	public static GameParam isTurboR( boolean isTurboR ) { return new GameParam().isTurboR( isTurboR ); }
	public static GameParam isPSG( boolean isPSG ) { return new GameParam().isPSG( isPSG ); }
	public static GameParam isSCC( boolean isSCC ) { return new GameParam().isSCC( isSCC ); }
	public static GameParam isSCCI( boolean isSCCI ) { return new GameParam().isSCCI( isSCCI ); }
	public static GameParam isPCM( boolean isPCM ) { return new GameParam().isPCM( isPCM ); }
	public static GameParam isMSXMUSIC( boolean isMSXMUSIC ) { return new GameParam().isMSXMUSIC( isMSXMUSIC ); }
	public static GameParam isMSXAUDIO( boolean isMSXAUDIO ) { return new GameParam().isMSXAUDIO( isMSXAUDIO ); }
	public static GameParam isMoonsound( boolean isMoonsound ) { return new GameParam().isMoonsound( isMoonsound ); }
	public static GameParam isMIDI( boolean isMIDI ) { return new GameParam().isMIDI( isMIDI ); }
	public static GameParam genre1( Genre genre1 ) { return new GameParam().genre1( genre1 ); }
	public static GameParam genre2( Genre genre2 ) { return new GameParam().genre2( genre2 ); }
	public static GameParam msxGenID( int msxGenID ) { return new GameParam().msxGenID( msxGenID ); }
	public static GameParam sha1Code( String sha1Code ) { return new GameParam().sha1Code( sha1Code ); }
	public static GameParam size( long size ) { return new GameParam().size( size ); }
	public static GameParam screenshotSuffix( String screenshotSuffix ) { return new GameParam().screenshotSuffix( screenshotSuffix ); }
	public static GameParam tclScript( String tclScript ) { return new GameParam().tclScript( tclScript ); }
	public static GameParam tclScriptOverride( boolean tclScriptOverride ) { return new GameParam().tclScriptOverride( tclScriptOverride ); }
	public static GameParam fddMode( FDDMode fddMode ) { return new GameParam().fddMode( fddMode ); }

	private Game( GameParam param )
	{
		this.machine = param.machine;
		this.romA = param.romA;
		this.romB = param.romB;
		this.extensionRom = param.extensionRom;
		this.diskA = param.diskA;
		this.diskB = param.diskB;
		this.tape = param.tape;
		this.harddisk = param.harddisk;
		this.laserdisc = param.laserdisc;
		this.name = param.name;
		this.info = param.info;
		
		this.isMSX = param.isMSX;
		this.isMSX2 = param.isMSX2;
		this.isMSX2Plus = param.isMSX2Plus;
		this.isTurboR = param.isTurboR;
		
		this.isPSG = param.isPSG;
		this.isSCC = param.isSCC;
		this.isSCCI = param.isSCCI;
		this.isPCM = param.isPCM;
		this.isMSXMUSIC = param.isMSXMUSIC;
		this.isMSXAUDIO = param.isMSXAUDIO;
		this.isMoonsound = param.isMoonsound;
		this.isMIDI = param.isMIDI;
		
		this.genre1 = param.genre1;
		this.genre2 = param.genre2;

		this.msxGenID = param.msxGenID;
		
		this.sha1Code = param.sha1Code;

		this.size = param.size;
		this.screenshotSuffix = param.screenshotSuffix;
		this.tclScript = param.tclScript;
		this.tclScriptOverride = param.tclScriptOverride;

		this.fddMode = param.fddMode;
	}

	@Override
	public boolean equals( Object game )
	{
		boolean isSame;
		
		if( name == null || game == null || !(game instanceof Game) )
		{
			isSame = false;
		}
		else
		{
			isSame = name.equals( ((Game)game).getName() );
		}

		return isSame;
	}

	@Override
	 public int hashCode()
	{
		   return name == null? super.hashCode():name.hashCode();
	}
	
	//--------
	// Getters
	//--------
	public String getName()	{ return name; }
	public String getMachine() { return machine; }
	public String getRomA() { return romA; }
	public String getRomB() { return romB; }
	public String getExtensionRom() { return extensionRom; }
	public String getDiskA() { return diskA; }
	public String getDiskB() { return diskB; }
	public String getTape() { return tape; }
	public String getInfo() { return info; }
	public String getHarddisk() { return harddisk; }
	public String getLaserdisc() { return laserdisc; }
	public boolean isMSX() { return isMSX; }
	public boolean isMSX2() { return isMSX2; }
	public boolean isMSX2Plus() { return isMSX2Plus; }
	public boolean isTurboR() { return isTurboR; }
	public boolean isPSG() { return isPSG; }
	public boolean isSCC() { return isSCC; }
	public boolean isSCCI() { return isSCCI; }
	public boolean isPCM() { return isPCM; }
	public boolean isMSXMUSIC() { return isMSXMUSIC; }
	public boolean isMSXAUDIO() { return isMSXAUDIO;	}
	public boolean isMoonsound() { return isMoonsound; }
	public boolean isMIDI() { return isMIDI; }
	public Genre getGenre1() { return genre1; }
	public Genre getGenre2() { return genre2; }
	public int getMsxGenID() { return msxGenID; }
	public String getSha1Code() { return sha1Code; }
	public long getSize() { return size; }
	public String getScreenshotSuffix() { return screenshotSuffix; }
	public String getTclScript() { return tclScript; }
	public FDDMode getFDDMode() { return fddMode; };
	public boolean isTclScriptOverride() { return tclScriptOverride; };

	//the following are to determine what kind of media the game is.
	//the order of check is ROM, Disk, Tape, Laserdisc
	
	public boolean isROM() { return romA != null; }
	public boolean isDisk() { return !isROM() && diskA != null; }
	public boolean isTape() { return !isDisk() && tape != null; }
	public boolean isHarddisk() { return !isTape() && harddisk != null; }
	public boolean isLaserdisc() { return !isHarddisk() && laserdisc != null; }
	public boolean isScript() { return !isLaserdisc() && tclScript != null; }

	/* Compares extra data fields and return true or false accordingly. This method only looks at the extra data fields and ignores others.
	 * 
	 * @param game Game object to compare to
	 * @return True if extra data fields are equal, false otherwise or if passed game object is null
	 */
	public boolean isExtraDataEqual( Game game )
	{
		boolean isEqual = false;

		if( game != null )
		{
			isEqual = (isMSX() == game.isMSX()) && (isMSX2() == game.isMSX2()) && (isMSX2Plus() == game.isMSX2Plus()) && (isTurboR() == game.isTurboR()) &&
					(isPSG() == game.isPSG()) && (isSCC() == game.isSCC()) && (isSCCI() == game.isSCCI()) && (isPCM() == game.isPCM()) &&
					(isMSXMUSIC() == game.isMSXMUSIC()) && (isMSXAUDIO() == game.isMSXAUDIO()) && (isMoonsound() == game.isMoonsound()) && (isMIDI() == game.isMIDI()) &&
					(getGenre1() == game.getGenre1()) && (getGenre2() == game.getGenre2()) && (Utils.equalStrings(getScreenshotSuffix(), game.getScreenshotSuffix()) &&
					(getMsxGenID() == game.getMsxGenID()));
		}

		return isEqual;
	}
}
