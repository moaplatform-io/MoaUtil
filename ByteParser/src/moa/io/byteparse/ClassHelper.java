package moa.io.byteparse;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import moa.io.byteparse.config.ByteParseReqCommand;

public class ClassHelper {
	private static Map<String,List<Class>> classesList=new HashMap<String,List<Class>>();
	private static Map<String,Class> cmdClassList;

	public static Object getCommandObjByByteArray(byte[] cmdCode,String searchPackage) throws Exception {
		String command=new String(Arrays.copyOfRange(cmdCode, 0, 8));
		Object o=null;
		if(cmdClassList==null) {
			 cmdClassList=new HashMap<String,Class>();
			 List<Class> classList=getClasses(searchPackage);
			 for (int i = 0; i < classList.size(); i++) {
				 String inneerCmdCode=parseClass(classList.get(i));
				 cmdClassList.put(inneerCmdCode, classList.get(i));
			 }
		}
		System.out.println(cmdClassList);
		Class callClass=cmdClassList.get(command);
		if(callClass!=null) {
			o=callClass.newInstance();
			ByteToDto.byteToDto(o, cmdCode);
		}
		
		return o;
	}

	public static Object getCommandObjByCmdCode(String cmdCode,String searchPackage) throws InstantiationException, IllegalAccessException {
		Object o=null;
		if(cmdClassList==null) {
			 cmdClassList=new HashMap<String,Class>();
			 List<Class> classList=getClasses(searchPackage);
			 for (int i = 0; i < classList.size(); i++) {
				 String inneerCmdCode=parseClass(classList.get(i));
				 cmdClassList.put(inneerCmdCode, classList.get(i));
			 }
		}
		Class callClass=cmdClassList.get(cmdCode);
		if(callClass!=null) {
			o=callClass.newInstance();
		}
		
		return o;
	}
	
	public static String parseClass(Class checkCLass) {
		String cmdCode=null;
		ByteParseReqCommand command=(ByteParseReqCommand) checkCLass.getAnnotation(ByteParseReqCommand.class);
		if(command!=null)
			cmdCode=command.CMD_TYPE();
		return cmdCode;	
	}
	
	public static List<Class> getClasses(String packageName) {
		List<Class> arrayli=classesList.get(packageName);
		if(arrayli==null) {
			arrayli=new ArrayList<Class>();
			
			Set<Class> classes = new HashSet<Class>();
			String packageNameSlash = "./" + packageName.replace(".", "/");
			URL directoryURL = Thread.currentThread().getContextClassLoader().getResource(packageNameSlash);
			if (directoryURL == null) {
				System.err.println("Could not retrive URL resource : " + packageNameSlash);
				return null;
			}
	
			String directoryString = directoryURL.getFile();
			try {
				directoryString=URLDecoder.decode(directoryString, "utf-8");
			}catch (Exception e) {
			}

			if (directoryString == null) {
				System.err.println("Could not find directory for URL resource : " + packageNameSlash);
				return null;
			}
	
			File directory = new File(directoryString);
			if (directory.exists()) {
				String[] files = directory.list();
				for (String fileName : files) {
					if (fileName.endsWith(".class")) {
						fileName = fileName.substring(0, fileName.length() - 6); // remove .class
						try {
							Class c = Class.forName(packageName + "." + fileName);
							if (!Modifier.isAbstract(c.getModifiers())) // add a class which is not abstract
								classes.add(c);
						} catch (ClassNotFoundException e) {
							System.err.println(packageName + "." + fileName + " does not appear to be a valid class");
							e.printStackTrace();
						}
					}
				}
			} else {
				System.err.println(packageName + " does not appear to exist as a valid package on the file system.");
			}
			if(classes!=null) {
				arrayli.addAll(classes);
				classesList.put(packageName,arrayli);
			}
		}
			
		return arrayli;
	}
}