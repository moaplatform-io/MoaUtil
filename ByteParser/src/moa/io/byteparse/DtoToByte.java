package moa.io.byteparse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


public class DtoToByte {

	public static byte[] dtoToByte(Object ins) throws IllegalArgumentException, IllegalAccessException, IOException {
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		List<MoaClassFieldInfo> filedList=DtoHelper.getFieldList(ins);
		ByteParseConverter.covertDtoToByte(ins, filedList,bos);
		return bos.toByteArray();
	}
}
