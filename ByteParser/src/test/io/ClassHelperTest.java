package test.io;

import java.util.List;

import moa.io.byteparse.ClassHelper;
import test.dto.HelloDTO;

public class ClassHelperTest {
	public static void main(String[] args) throws Exception {
		getClassesTest(args);
		parseClassTest(args);
	}
	public static void getClassesTest(String[] args) throws Exception {
		List<Class> classList = ClassHelper.getClasses("test.dto");
		for (int i = 0; i < classList.size(); i++) {
			System.out.println(classList.get(i));
			ClassHelper.parseClass(classList.get(i));
		}
	}
	public static void parseClassTest(String[] args) throws Exception {
		String cmd=ClassHelper.parseClass(HelloDTO.class);
		System.out.println(cmd);
	}
	
}
