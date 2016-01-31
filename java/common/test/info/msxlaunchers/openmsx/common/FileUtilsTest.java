package info.msxlaunchers.openmsx.common;

import java.io.File;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FileUtilsTest
{
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
}
