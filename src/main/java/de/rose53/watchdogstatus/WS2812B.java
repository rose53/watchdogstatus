package de.rose53.watchdogstatus;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.util.Objects;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.io.i2c.I2CFactory.UnsupportedBusNumberException;

public class WS2812B implements AutoCloseable {

	public static final int ROWS = 8;
	public static final int COLS = 8;

	public static final int WS2812B_ADDR = 0x20;

	public static final int SET_ALL_LEDS_CMD = 0xfe;
	public static final int ALL_LEDS_OFF_CMD = 0xff;

	private static WS2812B ws2812B = null;

	public static class LEDField {

		private Color[][] field = new Color[ROWS][COLS];

		public LEDField(Color[][] field) {
			this.field = field;
		}

		byte[] getColorField() {
			byte[] retVal = new byte[3*ROWS*COLS];
			int idx = 0;
			for (int i = 0; i < ROWS; i++) {
				for (int j = 0; j < COLS; j++) {
					retVal[idx++] = (byte)Math.min(field[j][i].getRed(),0xfd);
					retVal[idx++] = (byte)Math.min(field[j][i].getGreen(),0xfd);
					retVal[idx++] = (byte)Math.min(field[j][i].getBlue(),0xfd);
				}
			}
			return retVal;
		}
	}

	private I2CBus i2c;
	private I2CDevice device;

	private WS2812B() throws UnsupportedBusNumberException, IOException {
		if (!isAvailable()) {
			return;
		}
		i2c = I2CFactory.getInstance(I2CBus.BUS_1);

		device = i2c.getDevice(WS2812B_ADDR);
	}

	public static WS2812B instance() {

		if (ws2812B == null) {
			try {
				ws2812B = new WS2812B();
			} catch (UnsupportedBusNumberException | IOException e) {
				e.printStackTrace();
			}
		}
		return ws2812B;
	}

	private boolean isAvailable() {

		String os = System.getProperty("os.name").toLowerCase();

		return (os.indexOf("win") >= 0)?false:true;
	}

	public void setLED(Point pos, Color color) throws IOException {
		if (!isAvailable()) {
			return;
		}
		device.write(pos.x + ROWS * pos.y, new byte[]{(byte)color.getRed(),(byte)color.getGreen(),(byte)color.getBlue()});
	}

	public void setLED(LEDField field) throws IOException {
		if (!isAvailable()) {
			return;
		}
		Objects.requireNonNull(field, "LEDField must not be null");
		byte[] colorField = field.getColorField();

		int offset = 0;
		int size = Math.min(offset + 30,colorField.length);
		device.write(SET_ALL_LEDS_CMD, colorField,offset,size);

		while (offset + size < colorField.length) {
			offset = offset + size;
			size = Math.min(30,colorField.length - offset);
			device.write(colorField,offset,size);
		}
	}

	public void clear() throws IOException {
		if (!isAvailable()) {
			return;
		}
		device.write(ALL_LEDS_OFF_CMD,new byte[0]);
	}

	@Override
	public void close() throws IOException {
		if (!isAvailable()) {
			return;
		}
		if (i2c != null) {
			clear();
			i2c.close();
		}
	}
}
