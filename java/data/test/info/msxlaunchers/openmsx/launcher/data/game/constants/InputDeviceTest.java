package info.msxlaunchers.openmsx.launcher.data.game.constants;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class InputDeviceTest
{
	@Test
	public void testInputDeviceValues()
	{
		InputDevice[] inputDevices = InputDevice.values();

		for( InputDevice inputDevice: inputDevices )
		{
			assertEquals( InputDevice.fromValue( inputDevice.getValue() ), inputDevice );
		}
	}

	@Test
	public void testFromValueBoundaries()
	{
		assertEquals( InputDevice.NONE, InputDevice.fromValue( -1 ) );
		assertEquals( InputDevice.NONE, InputDevice.fromValue( 0 ) );
		assertEquals( InputDevice.JOYSTICK, InputDevice.fromValue( 1 ) );
		assertEquals( InputDevice.JOYSTICK_KEYBOARD, InputDevice.fromValue( 2 ) );
		assertEquals( InputDevice.MOUSE, InputDevice.fromValue( 3 ) );
		assertEquals( InputDevice.ARKANOID_PAD, InputDevice.fromValue( 4 ) );
		assertEquals( InputDevice.TRACKBALL, InputDevice.fromValue( 5 ) );
		assertEquals( InputDevice.TOUCHPAD, InputDevice.fromValue( 6 ) );
	}
}
