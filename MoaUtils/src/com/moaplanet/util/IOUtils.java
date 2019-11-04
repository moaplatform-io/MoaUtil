package com.moaplanet.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class IOUtils {

	public static void closeStream(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public static byte[] toByteArray(InputStream is) throws RuntimeException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			int count = -1;
			byte[] buffer = new byte[1024 * 10];

			while ((count = is.read(buffer)) != -1) {
				bos.write(buffer);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return bos.toByteArray();
	}

	public static void closeStream(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	public static void closeStream(Statement ps) {
		if (ps != null) {
			try {
				ps.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
	}

	public static void closeStream(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
	}

}
