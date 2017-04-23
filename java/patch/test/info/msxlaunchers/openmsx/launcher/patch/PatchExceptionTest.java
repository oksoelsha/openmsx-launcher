package info.msxlaunchers.openmsx.launcher.patch;

import org.junit.Assert;
import org.junit.Test;

public class PatchExceptionTest
{
	@Test
	public void givenExceptionIssue_whenContruct_thenGetIssueReturnsIssue()
	{
		PatchException exception = new PatchException( PatchExceptionIssue.FILE_TO_PATCH_NOT_PATCHABLE );
		Assert.assertEquals( PatchExceptionIssue.FILE_TO_PATCH_NOT_PATCHABLE, exception.getIssue() );
	}
}
