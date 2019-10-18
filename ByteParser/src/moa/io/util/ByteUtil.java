package moa.io.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteUtil {

	public static byte[] convertCharToByte(char[] charList) {
		byte[] b = new byte[charList.length];
		for (int i = 0; i < charList.length; i++) {
			b[i] = (byte) charList[i];
		}
		return b;
	}

	public static byte[] convertByEndian(int covnert, ByteOrder order, int size) {
		byte inputbytes[] = intToByteArray(covnert);
		ByteBuffer byteBuffer = ByteBuffer.allocate(size);
		byteBuffer.order(order);
		for (int i = 0; i < size; i++) {
			byteBuffer.put(inputbytes[i]);
		}
		byte[] result = byteBuffer.array();
		return result;
	}

	public static byte[] convertLittleEndian(int covnert) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(4);
		byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		byteBuffer.putInt(covnert);
		byte[] result = byteBuffer.array();
		return result;
	}

	public static final byte[] intToByteArray(int value) {
		return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value };
	}

	static short swap(short x) {
		return (short) ((x << 8) | ((x >> 8) & 0xff));
	}

	static char swap(char x) {
		return (char) ((x << 8) | ((x >> 8) & 0xff));
	}

	static int swap(int x) {
		return (int) ((swap((short) x) << 16) | (swap((short) (x >> 16)) & 0xffff));
	}

	static long swap(long x) {
		return (long) (((long) swap((int) (x)) << 32) | ((long) swap((int) (x >> 32)) & 0xffffffffL));
	}

	static float swap(float x) {
		return Float.intBitsToFloat(swap(Float.floatToRawIntBits(x)));
	}

	static double swap(double x) {
		return Double.longBitsToDouble(swap(Double.doubleToRawLongBits(x)));
	}

	public static int byteToint(byte[] arr) {
		return (arr[0] & 0xff) << 24 | (arr[1] & 0xff) << 16 | (arr[2] & 0xff) << 8 | (arr[3] & 0xff);
	}

	public static String byteArrayToHex(byte[] a) {
		return byteArrayToHex(a, 0, a.length);
	}

	public static String byteArrayToHex(byte[] a, int startIndex, int endIndex) {
		StringBuilder sb = new StringBuilder();
		for (int i = startIndex; i < endIndex; i++) {
			sb.append(String.format("%02x", a[i] & 0xff));
		}
		return sb.toString();
	}
}
