package moa.io.byteparse;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;

import moa.io.byteparse.config.ByteParse;
import moa.io.util.ByteUtil;

public class ByteParseConverter {
	public static void coverByteToDto(Object instence,List<MoaClassFieldInfo> field,byte []msg) throws IllegalArgumentException, IllegalAccessException, IOException {
		int index=0;
		for (int i = 0; i < field.size(); i++) {
			index+=coverByteToDto(instence,field.get(i),msg,index);
		}
	}
	public static int coverByteToDto(Object instence,MoaClassFieldInfo field,byte[] msg,int index) throws IllegalArgumentException, IllegalAccessException, IOException {
//		ByteParse parse=field.getParse();
//		System.out.print(field.getField().getName()+"\t");
//		System.out.print(parse.order()+"\t");
//		System.out.print(parse.byteSize()+"\t");
//		System.out.println(field.getField().getType());
		int size=field.getParse().byteSize();
		try {
			if(field.getField().getType().equals(String.class)) {
				field.getField().setAccessible(true);
				Object fileV=field.getField().get(instence);
				if(fileV==null) {
					byte []sprlitMsg=Arrays.copyOfRange(msg, index, index+ size);
					field.getField().set(instence, new String(sprlitMsg));
				}
			}else {
				throw new RuntimeException("not impl");
			}
		}finally {
		}
		return size;
	}
	
	public static void coverStringToDto(Object instence,List<MoaClassFieldInfo> field,String msg) throws IllegalArgumentException, IllegalAccessException, IOException {
		StringBuilder sb=new StringBuilder(msg);
		int index=0;
		for (int i = 0; i < field.size(); i++) {
			index+=coverStringToDto(instence,field.get(i),sb,index);
		}
	}
	public static int coverStringToDto(Object instence,MoaClassFieldInfo field,StringBuilder msg,int index) throws IllegalArgumentException, IllegalAccessException, IOException {
		ByteParse parse=field.getParse();
		int size=field.getParse().byteSize();
		try {
			if(field.getField().getType().equals(String.class)) {
				field.getField().setAccessible(true);
				Object fileV=field.getField().get(instence);
				if(fileV==null) {
					String sprlitMsg=msg.substring(index,index+ size);
					field.getField().set(instence, sprlitMsg);
				}
			}else {
				throw new RuntimeException("not impl");
			}
		}finally {
			index+=size;
		}
		return index;
	}
	
	public static void covertDtoToByte(Object instence,List<MoaClassFieldInfo> field,ByteArrayOutputStream bos) throws IllegalArgumentException, IllegalAccessException, IOException {
		for (int i = 0; i < field.size(); i++) {
			covertDtoToByte(instence,field.get(i),bos);
		}
	}
	
	public static void covertDtoToByte(Object instence,MoaClassFieldInfo field,ByteArrayOutputStream bos) throws IllegalArgumentException, IllegalAccessException, IOException {
		ByteParse parse=field.getParse();
		field.getField().setAccessible(true);
		Object fieldV=field.getField().get(instence);
		
		if(fieldV==null) {
			if(field.getParse().isDefaultVal()) {
				bos.write(field.getParse().defaultVal());
			}else {
				bos.write(field.getEmpryByte());
			}
			return;
		}
		
		if(field.getField().getType().equals(String.class)) {
			byte defaultByte=0;
			if(field.getParse().isDefaultVal()) {
				defaultByte=field.getParse().defaultVal()[0];
			}
			stringToByte(fieldV.toString(),field.getParse().byteSize(), bos,defaultByte);
		}else if(field.getField().getType().equals(int.class)) {
				intToByte(fieldV,field.getParse().byteSize(), bos);
		}else if(field.getField().getType().equals(List.class)) {
			List listFil=(List) fieldV;
			int size=listFil.size();
			byte defaultByte=0;
			if(field.getParse().isDefaultVal()) {
				defaultByte=field.getParse().defaultVal()[0];
			}
			stringToByteReverse(size+"",field.getParse().byteSize(), bos,defaultByte);
			for (int i = 0; i < size; i++) {
				Object o=listFil.get(i);
				covertDtoToByte(o, DtoHelper.getFieldList(o), bos);
			}
		}
	}

	public static void intToByte(Object msg,int size,ByteArrayOutputStream bos) throws IOException {
		byte[] intbyte=ByteUtil.convertByEndian(Integer.valueOf(msg.toString()),ByteOrder.BIG_ENDIAN,size);
		bos.write(intbyte);
	}
	public static void stringToByteReverse(String msg,int size,ByteArrayOutputStream bos,int defaultByte) throws IOException {
		int bytessize=msg.getBytes().length;
		int moreByte=size-bytessize;
		if(moreByte<0) {
			throw new IOException("out of range:: data size:"+bytessize+"\t but max Size:"+size+"\tmsg:["+msg+"]");
		}
		for (int i = 0; i < moreByte; i++) {
			bos.write(defaultByte);
		}
		bos.write(msg.getBytes());
	}
	public static void stringToByte(String msg,int size,ByteArrayOutputStream bos,int defaultByte) throws IOException {
		int bytessize=msg.getBytes().length;
		int moreByte=size-bytessize;
		if(moreByte<0) {
			throw new IOException("out of range:: data size:"+bytessize+"\t but max Size:"+size+"\tmsg:["+msg+"]");
		}
		bos.write(msg.getBytes());
		for (int i = 0; i < moreByte; i++) {
			bos.write(defaultByte);
		}
	}
}