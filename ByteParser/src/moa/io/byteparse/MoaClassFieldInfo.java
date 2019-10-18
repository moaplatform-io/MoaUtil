package moa.io.byteparse;

import java.lang.reflect.Field;

import moa.io.byteparse.config.ByteParse;

public class MoaClassFieldInfo {
	private ByteParse parse;
	private Field field;
	private int orderCnt;
	private byte[] empryByte;

	public ByteParse getParse() {
		return parse;
	}

	public byte[] getEmpryByte() {
		return empryByte;
	}

	public void setEmpryByte(byte[] empryByte) {
		this.empryByte = empryByte;
	}

	public void setParse(ByteParse parse) {
		this.empryByte = new byte[parse.byteSize()];
		this.parse = parse;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public int getOrderCnt() {
		return orderCnt;
	}

	public void setOrderCnt(int orderCnt) {
		this.orderCnt = orderCnt;
	}

}
