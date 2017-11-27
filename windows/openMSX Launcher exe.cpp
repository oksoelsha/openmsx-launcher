/*
 * Windows launcher for openMSX Launcher v1.6 and later
 *
 * Author - Sam Elsharif
 *
 * Nov 2017 - accept optional jre location as a command line argument
 */
#include "stdafx.h"
#include "openMSX Launcher exe.h"
#include <string>

#define JAVA_REGISTRY_PATH L"SOFTWARE\\JavaSoft\\Java Runtime Environment\\"
#define JAVA_REGISTRY_CURRENT_VERSION L"CurrentVersion"
#define JAVA_REGISTRY_JAVA_HOME L"JavaHome"
#define JAVA_EXE L"\\bin\\java.exe"
#define JAVA_ARGS TEXT(" -client -cp \"lib\\*\" info.msxlaunchers.openmsx.launcher.ui.Launcher")
#define JAVA_NOT_INSTALLED_MSG L"Java is not installed on your system or is not the right version. Please install Java Runtime Environment 8 or later"
#define LAUNCHER_CANNOT_START_MSG L"Could not start openMSX Launcher"
#define ERROR_TITLE L"Error"
#define BUFFER_SIZE 256
#define MAX_FILES 100
#define JAR_FILE_EXT L".jar"
#define UPDATED_JAR_FILE_EXT L".jar_update"
#define UPDATED_JAR_FILE_EXT_LENGTH _tcslen(UPDATED_JAR_FILE_EXT)
#define UPDATE_IDENTIFIER_LENGTH _tcslen(L"_update")
#define LEAST_VALID_JAVA_VERSION 8
#define LAUNCHER_OLD_NAME L"\\openMSX Launcher.exe.old"

using namespace std;
typedef basic_string<TCHAR> tstring;

/*
value: pre-allocated buffer that will hold the registry key value upon return
Return: True if value was found, FALSE otherwise
*/
BOOL GetRegistryValue(TCHAR *keyPath, TCHAR *key, TCHAR *value)
{
	DWORD dwType;
	DWORD dwDataSize = BUFFER_SIZE;
	memset(value, 0, BUFFER_SIZE);

	// open the key for reading.
	HKEY hkeyDXVer;
	long lResult = RegOpenKeyEx(HKEY_LOCAL_MACHINE, keyPath, 0, KEY_READ | KEY_WOW64_64KEY, &hkeyDXVer);

	if (ERROR_SUCCESS == lResult)
	{
		lResult = RegQueryValueEx(hkeyDXVer, key, NULL, &dwType, (LPBYTE)value, &dwDataSize);
		RegCloseKey(hkeyDXVer);

		if (ERROR_SUCCESS != lResult)
		{
			return FALSE;
		}
	}
	else
	{
		return FALSE;
	}

	return TRUE;
}

/*
javaVersionPath: pre-allocated buffer that will hold the JRE full path upon return
lpCmdLine: command line argument. Windows passes an empty string if one wasn't specified
Return: True if command line argument was a non-zero length string, FALSE otherwise
*/
BOOL GetJavaPathFromCommandLine(TCHAR *javaVersionPath, LPTSTR lpCmdLine)
{
	if (__argc == 1)
	{
		//this means no arguments were passed
		return FALSE;
	}

	return !_tcscpy_s(javaVersionPath, BUFFER_SIZE, lpCmdLine);
}

/*
javaVersionPath: pre-allocated buffer that will hold the JRE full path upon return
Return: True if JRE was found and is version 1.8 or later, FALSE otherwise
*/
BOOL GetJavaPathFromRegistry(TCHAR *javaVersionPath)
{
	//get the Java version from the registry
	TCHAR  value[BUFFER_SIZE];
	if (!GetRegistryValue(JAVA_REGISTRY_PATH, JAVA_REGISTRY_CURRENT_VERSION, value))
	{
		return FALSE;
	}

	//check if the version is 1.8 or later
	tstring versionString = value;
	tstring majorVersionString = versionString.substr(2, tstring::npos);
	int majorVersionInt = (int)_ttoi(majorVersionString.c_str());
	if (majorVersionInt < LEAST_VALID_JAVA_VERSION)
	{
		return FALSE;
	}

	//get the Java JRE location based on the version
	_tcscpy_s(javaVersionPath, BUFFER_SIZE, JAVA_REGISTRY_PATH);
	_tcscat_s(javaVersionPath, BUFFER_SIZE, value);

	//get the JRE path
	if (!GetRegistryValue(javaVersionPath, JAVA_REGISTRY_JAVA_HOME, value))
	{
		return FALSE;
	}

	_tcscpy_s(javaVersionPath, BUFFER_SIZE, value);
	_tcscat_s(javaVersionPath, BUFFER_SIZE, JAVA_EXE);

	return TRUE;
}

BOOL StartJavaProcess(const TCHAR* javaFullPath)
{
	STARTUPINFO si;
	ZeroMemory(&si, sizeof(si));
	si.cb = sizeof(STARTUPINFOA);
	si.lpReserved = NULL;
	si.lpReserved2 = NULL;
	si.cbReserved2 = 0;
	si.lpDesktop = NULL;
	si.lpTitle = NULL;
	si.dwFlags = STARTF_USESHOWWINDOW;
	si.dwX = 0;
	si.dwY = 0;
	si.dwFillAttribute = 0;
	si.wShowWindow = SW_HIDE;

	PROCESS_INFORMATION pi;
	ZeroMemory(&pi, sizeof(pi));

	if (!CreateProcess(javaFullPath, JAVA_ARGS, NULL, FALSE, STARTF_USESHOWWINDOW, NULL, NULL, NULL, &si, &pi))
	{
		return FALSE;
	}

	WaitForSingleObject(pi.hProcess, INFINITE);

	CloseHandle(pi.hProcess);
	CloseHandle(pi.hThread);

	return TRUE;
}

BOOL EndsWith(const TCHAR *fullString, const TCHAR *ending)
{
	tstring str = fullString;
	tstring end = ending;

	if (str.length() >= end.length())
	{
		return (0 == str.compare(str.length() - end.length(), end.length(), end));
	}
	else
	{
		return FALSE;
	}
}

void PrepareForUpdate()
{
	WIN32_FIND_DATA ffd;
	TCHAR exeDirectory[MAX_PATH];
	TCHAR libDirectory[MAX_PATH];
	TCHAR libDirectoryAllFiles[MAX_PATH];
	int totalFile = 0;
	BOOL foundFilesTEndingWithNewString = FALSE;
	TCHAR currentJarFiles[MAX_FILES][MAX_PATH];
	TCHAR updatedJarFiles[MAX_FILES][MAX_PATH];
	TCHAR oldExecutable[MAX_PATH];

	GetCurrentDirectory(MAX_PATH, exeDirectory);

	_tcscpy_s(libDirectory, exeDirectory);
	_tcscat_s(libDirectory, MAX_PATH, L"\\lib\\");

	_tcscpy_s(libDirectoryAllFiles, libDirectory);
	_tcscat_s(libDirectoryAllFiles, MAX_PATH, L"\\*.*");

	_tcscpy_s(oldExecutable, exeDirectory);
	_tcscat_s(oldExecutable, MAX_PATH, LAUNCHER_OLD_NAME);

	int jarFileCount = 0;
	int updatedJarFileCount = 0;

	HANDLE hFind = FindFirstFile(libDirectoryAllFiles, &ffd);
	if (INVALID_HANDLE_VALUE != hFind)
	{
		do
		{
			if (!(ffd.dwFileAttributes & FILE_ATTRIBUTE_DIRECTORY))
			{
				TCHAR fullFileName[MAX_PATH];
				_tcscpy_s(fullFileName, libDirectory);
				_tcscat_s(fullFileName, MAX_PATH, ffd.cFileName);

				if (EndsWith(ffd.cFileName, JAR_FILE_EXT))
				{
					_tcscpy_s(currentJarFiles[jarFileCount++], MAX_PATH, fullFileName);
				}
				else if (EndsWith(ffd.cFileName, UPDATED_JAR_FILE_EXT))
				{
					_tcscpy_s(updatedJarFiles[updatedJarFileCount++], MAX_PATH, fullFileName);
				}
			}
		} while ((FindNextFile(hFind, &ffd) != 0) && ((jarFileCount + updatedJarFileCount) < MAX_FILES));
	}

	FindClose(hFind);

	//process the update if the new updated jars exist
	if (updatedJarFileCount > 0)
	{
		//delete the current JAR files
		for (int counter = 0; counter < jarFileCount; counter++)
		{
			DeleteFile(currentJarFiles[counter]);
		}

		//remove the '_update' extendsion from the remaining update JAR files
		for (int counter = 0; counter < updatedJarFileCount; counter++)
		{
			TCHAR newJarFileName[MAX_PATH];
			_tcscpy_s(newJarFileName, updatedJarFiles[counter]);
			newJarFileName[_tcslen(updatedJarFiles[counter]) - UPDATE_IDENTIFIER_LENGTH] = L'\0';

			MoveFile(updatedJarFiles[counter], newJarFileName);
		}
	}

	//also delete the old executable file
	DeleteFile(oldExecutable);
}

void BringOpenMSXLauncherToForeground(HWND hWnd)
{
	SetForegroundWindow(hWnd);

	if (IsIconic(hWnd))
	{
		ShowWindow(hWnd, SW_RESTORE);
	}
	else
	{
		ShowWindow(hWnd, SW_SHOW);
	}
}

BOOL CALLBACK EnumWindowsProc(HWND hwnd, LPARAM lParam)
{
	TCHAR title[80];

	GetWindowText(hwnd, title, sizeof(title));

	if (!_tcscmp(title, _T("openMSX Launcher")))
	{
		BringOpenMSXLauncherToForeground(hwnd);
	}

	return TRUE;
}

BOOL IsLauncherAlreadyRunning()
{
	HANDLE mutex = ::CreateMutex(NULL, FALSE, _T("OPENMSX_LAUNCHER_MUTEX"));

	if ((mutex != NULL) && (GetLastError() != ERROR_ALREADY_EXISTS))
	{
		//Launcher is not running
		return FALSE;
	}
	else
	{
		//Launcher (Java process) is running already
		//find original instance and bring it to foreground
		EnumWindows(EnumWindowsProc, NULL);
		return TRUE;
	}
}

// Since the dialog has been closed, return FALSE so that we exit the
//  application, rather than start the application's message pump.

int APIENTRY _tWinMain(_In_ HINSTANCE hInstance,
                     _In_opt_ HINSTANCE hPrevInstance,
                     _In_ LPTSTR    lpCmdLine,
                     _In_ int       nCmdShow)
{
	//check if another instance is already running
	if (!IsLauncherAlreadyRunning())
	{
		//check if we need to prepare for update
		PrepareForUpdate();

		//get the JRE full path
		TCHAR javaVersionPath[BUFFER_SIZE];
		if (!GetJavaPathFromCommandLine(javaVersionPath, lpCmdLine) && !GetJavaPathFromRegistry(javaVersionPath))
		{
			MessageBox(0, JAVA_NOT_INSTALLED_MSG, ERROR_TITLE, MB_ICONERROR | MB_OK);
		}
		else
		{
			//start the Java process
			if (!StartJavaProcess(javaVersionPath))
			{
				MessageBox(0, LAUNCHER_CANNOT_START_MSG, ERROR_TITLE, MB_ICONERROR | MB_OK);
			}
		}
	}

	return 0;
}
