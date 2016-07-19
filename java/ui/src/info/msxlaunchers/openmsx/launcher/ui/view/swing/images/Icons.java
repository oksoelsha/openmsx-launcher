/*
 * Copyright 2014 Sam Elsharif
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
package info.msxlaunchers.openmsx.launcher.ui.view.swing.images;

import java.awt.Image;

import javax.swing.ImageIcon;

/**
 * All icons images
 * 
 * @since v1.3
 * @author Sam Elsharif
 *
 */
public enum Icons
{
	APPLICATION_32( "icon-32" ),
	APPLICATION_64( "icon-64" ),
	BACKGROUND( "bkgrd" ),

	ERROR( "error" ),
	INFORMATION( "information" ),
	QUESTION( "question" ),

	LAUNCH( "mainscreen/launch" ),
	REMOVE( "mainscreen/delete" ),
	ADD( "mainscreen/add" ),
	EDIT( "mainscreen/edit" ),

	NO_SCREENSHOT( "noscrsht" ),

	FAVORITE( "favorite" ),
	SEARCH( "search" ),

	ADD_SMALL( "add_small" ),
	DELETE_SMALL( "delete_small" ),
	EDIT_SMALL( "edit_small" ),
	BACKUP_SMALL( "backup_small" ),
	RESTORE_SMALL( "restore_small" ),

	FOLDER( "folder" ),
	FILTER( "filter" ),
	DETECT( "detect" ),

	MEDIA_ROM( "media/rom" ),
	MEDIA_DISK( "media/disk" ),
	MEDIA_TAPE( "media/tape" ),
	MEDIA_HARDDISK( "media/harddisk" ),
	MEDIA_LASERDISC( "media/laserdisc" ),
	MEDIA_SCRIPT( "media/script" ),

	LEFT_ARROW( "leftArrow" ),
	RIGHT_ARROW( "rightArrow" ),

	FLAG_ar_KW( "flags/ar_KW" ),
	FLAG_ca_ES( "flags/ca_ES" ),
	FLAG_de_DE( "flags/de_DE" ),
	FLAG_en_US( "flags/en_US" ),
	FLAG_es_ES( "flags/es_ES" ),
	FLAG_fa_IR( "flags/fa_IR" ),
	FLAG_fi_FI( "flags/fi_FI" ),
	FLAG_fr_FR( "flags/fr_FR" ),
	FLAG_it_IT( "flags/it_IT" ),
	FLAG_ja_JP( "flags/ja_JP" ),
	FLAG_ko_KR( "flags/ko_KR" ),
	FLAG_nl_NL( "flags/nl_NL" ),
	FLAG_pt_BR( "flags/pt_BR" ),
	FLAG_ru_RU( "flags/ru_RU" ),
	FLAG_sv_SE( "flags/sv_SE" ),
	FLAG_zh_CN( "flags/zh_CN" ),
	FLAG_zh_TW( "flags/zh_TW" ),

	SOUND_PSG( "sound/psg" ),
	SOUND_SCC( "sound/scc" ),
	SOUND_SCC_I( "sound/scc_i" ),
	SOUND_PCM( "sound/pcm" ),
	SOUND_MSX_MUSIC( "sound/msx_music" ),
	SOUND_MSX_AUDIO( "sound/msx_audio" ),
	SOUND_MOONSOUND( "sound/moonsound" ),
	SOUND_MIDI( "sound/midi" ),

	GENERATION_MSX( "generation/msx" ),
	GENERATION_MSX2( "generation/msx2" ),
	GENERATION_MSX2P( "generation/msx2p" ),
	GENERATION_TURBO_R( "generation/msxturbor" )
	;

	private final ImageIcon imageIcon;

	Icons( String iconFile )
	{
		this.imageIcon = new ImageIcon(getClass().getResource(iconFile + ".png"));
	}

	public ImageIcon getImageIcon()
	{
		return imageIcon;
	}

	public Image getImage()
	{
		return imageIcon.getImage();
	}
}
