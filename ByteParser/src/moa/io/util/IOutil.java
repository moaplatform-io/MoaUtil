package moa.io.util;

import java.io.Closeable;
import java.net.Socket;

public class IOutil {
	public static void close(Closeable closeable) {
		if(closeable!=null) {
			try {
				closeable.close();
			}catch (Exception e) {
			}
		}
	}
	public static void close(Socket closeable) {
		if(closeable!=null) {
			try {
				closeable.close();
			}catch (Exception e) {
			}
		}
	}
	public static void printBytes(byte[] buff) {
		for(byte c : buff) {
		    System.out.format("%d ", c);
		}
		System.out.println();
	}
}
