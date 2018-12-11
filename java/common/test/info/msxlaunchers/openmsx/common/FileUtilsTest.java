package info.msxlaunchers.openmsx.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class FileUtilsTest
{
	@Rule
	public TemporaryFolder temporaryFolder = new TemporaryFolder();

	@Test
	public void test_whenInstantiateClass_thenGetAnInstance()
	{
		//this is not a useful test. It's just meant to get code coverage
		new FileUtils();
	}

	@Test
	public void test_GivenNullFile_WhenCallingGetFileNameWithoutExtension_ThenReturnNull()
	{
		File file = null;
		assertNull( FileUtils.getFileNameWithoutExtension( file ) );

		String filename = null;
		assertNull( FileUtils.getFileNameWithoutExtension( filename ) );
	}

	@Test
	public void test_GivenFilename_WhenCallingGetFileNameWithoutExtension_ThenReturnNameWithoutExtension()
	{
		assertEquals( "filename1", FileUtils.getFileNameWithoutExtension( "dir/filename1.abc" ) );
		assertEquals( "filename2", FileUtils.getFileNameWithoutExtension( "dir/filename2" ) );
		assertEquals( "filename3", FileUtils.getFileNameWithoutExtension( "dir1/dir2.2/filename3.x" ) );

		assertEquals( "filename4", FileUtils.getFileNameWithoutExtension( new File( "dir/filename4.er" ) ) );
		assertEquals( "filename5", FileUtils.getFileNameWithoutExtension( new File( "dir/filename5" ) ) );
		assertEquals( "filename6", FileUtils.getFileNameWithoutExtension( new File( "dir1/dir2.2/filename6.y" ) ) );
	}

	@Test
	public void test_whenGetFileGroupForSingleFile_thenReturnListOfOneFile() throws IOException
	{
		Path disk = temporaryFolder.newFile( "disk.dsk" ).toPath();

		//add an unrelated disk to the folder
		temporaryFolder.newFile( "tape.cas" ).toPath();

		List<String> files = FileUtils.getFileGroup( disk );

		assertEquals( Collections.singletonList( "disk.dsk" ), files );
	}

	@Test
	public void test_whenGetFileGroupForManyFilesInSingleFormatWithDigits_thenReturnListOfManyFiles() throws IOException
	{
		Path disk1 = temporaryFolder.newFile( "disk_1.dsk" ).toPath();
		Path disk2 = temporaryFolder.newFile( "disk_2.dsk" ).toPath();
		Path disk3 = temporaryFolder.newFile( "disk_3.dsk" ).toPath();

		//add an unrelated disk to the folder
		temporaryFolder.newFile( "disk_5.dsk" ).toPath();

		List<String> expectedList = Arrays.asList( "disk_1.dsk", "disk_2.dsk", "disk_3.dsk" );

		List<String> files = FileUtils.getFileGroup( disk1 );
		assertEquals( expectedList, files );

		files = FileUtils.getFileGroup( disk2 );
		assertEquals( expectedList, files );

		files = FileUtils.getFileGroup( disk3 );
		assertEquals( expectedList, files );
	}

	@Test
	public void test_whenGetFileGroupForManyFilesInSingleFormatWithChars_thenReturnListOfManyFiles() throws IOException
	{
		Path diska = temporaryFolder.newFile( "disk_a.dsk" ).toPath();
		Path diskb = temporaryFolder.newFile( "disk_b.dsk" ).toPath();
		Path diskc = temporaryFolder.newFile( "disk_c.dsk" ).toPath();

		//add an unrelated disk to the folder
		Path diske = temporaryFolder.newFile( "disk_e.dsk" ).toPath();

		List<String> expectedList = Arrays.asList( "disk_a.dsk", "disk_b.dsk", "disk_c.dsk" );

		List<String> files = FileUtils.getFileGroup( diska );
		assertEquals( expectedList, files );

		files = FileUtils.getFileGroup( diskb );
		assertEquals( expectedList, files );

		files = FileUtils.getFileGroup( diskc );
		assertEquals( expectedList, files );

		files = FileUtils.getFileGroup( diske );
		assertEquals( Collections.singletonList( "disk_e.dsk" ), files );
	}

	@Test
	public void test_whenGetFileGroupForManyFilesInDiskOfFormat_thenReturnListOfManyFiles() throws IOException
	{
		Path disk1 = temporaryFolder.newFile( "disk (Disk 1 of 3).di1" ).toPath();
		Path disk2 = temporaryFolder.newFile( "disk (Disk 2 of 3).di1" ).toPath();
		Path disk3 = temporaryFolder.newFile( "disk (Disk 3 of 3).di1" ).toPath();

		//add an unrelated disk to the folder
		Path disk4 = temporaryFolder.newFile( "disk (Disk 5 of 3).di1" ).toPath();

		//add an unrelated disk to the folder
		temporaryFolder.newFile( "disk (Disk 1 of 3).di2" ).toPath();

		List<String> expectedList = Arrays.asList( "disk (Disk 1 of 3).di1", "disk (Disk 2 of 3).di1", "disk (Disk 3 of 3).di1" );

		List<String> files = FileUtils.getFileGroup( disk1 );
		assertEquals( expectedList, files );

		files = FileUtils.getFileGroup( disk2 );
		assertEquals( expectedList, files );

		files = FileUtils.getFileGroup( disk3 );
		assertEquals( expectedList, files );

		files = FileUtils.getFileGroup( disk4 );
		assertEquals( Collections.singletonList( "disk (Disk 5 of 3).di1" ), files );
	}

	@Test
	public void test_whenGetFileGroupForManyFilesInMalformedDiskOfFormat_thenReturnListOfOneFile() throws IOException
	{
		Path disk1 = temporaryFolder.newFile( "disk (Disk a of 3).di1" ).toPath();
		Path disk2 = temporaryFolder.newFile( "disk (Disk 2 in 3).di1" ).toPath();
		Path disk3 = temporaryFolder.newFile( "disk (Disk 3 of3).di1" ).toPath();
		Path disk4 = temporaryFolder.newFile( "disk (Disk 3 of c).di1" ).toPath();

		Path disk5 = temporaryFolder.newFile( "disk (Disk 1 of 2.di1" ).toPath();
		Path disk6 = temporaryFolder.newFile( "disk (Disk 2 of 2.di1" ).toPath();

		List<String> files = FileUtils.getFileGroup( disk1 );
		assertEquals( Collections.singletonList( "disk (Disk a of 3).di1" ), files );

		files = FileUtils.getFileGroup( disk2 );
		assertEquals( Collections.singletonList( "disk (Disk 2 in 3).di1" ), files );

		files = FileUtils.getFileGroup( disk3 );
		assertEquals( Collections.singletonList( "disk (Disk 3 of3).di1" ), files );

		files = FileUtils.getFileGroup( disk4 );
		assertEquals( Collections.singletonList( "disk (Disk 3 of c).di1" ), files );

		files = FileUtils.getFileGroup( disk5 );
		assertEquals( Collections.singletonList( "disk (Disk 1 of 2.di1" ), files );

		files = FileUtils.getFileGroup( disk6 );
		assertEquals( Collections.singletonList( "disk (Disk 2 of 2.di1" ), files );
	}

	@Test
	public void test_whenGetFileGroupForNonExistentFile_thenReturnEmptyList() throws IOException
	{
		Path disk = Paths.get( "nonExistent" );

		List<String> files = FileUtils.getFileGroup( disk );

		assertTrue( files.isEmpty() );
	}
}
