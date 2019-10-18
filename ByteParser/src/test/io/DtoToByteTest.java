package test.io;

import java.lang.reflect.Array;
import java.util.Arrays;

import moa.io.byteparse.DtoToByte;
import test.dto.HelloDTO;

public class DtoToByteTest {

	public static void main(String[] args) throws Exception{
		dtoToByteTest();
	}

	public static boolean  dtoToByteTest() throws Exception{

		HelloDTO helloR = new HelloDTO();
		helloR.setHelloMsg("helloMsg");
		helloR.setMsgBody("body");
		byte[] bytes = DtoToByte.dtoToByte(helloR);
		byte[] arrays = new byte[] { 104, 101, 108, 108, 111, 77, 115, 103, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 98, 111,
				100, 121, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

		boolean isT = Arrays.equals(bytes, arrays);
		if(!isT) {
			throw new Exception("not valid "+Arrays.toString(bytes));
		}
		return isT;

	}

}
