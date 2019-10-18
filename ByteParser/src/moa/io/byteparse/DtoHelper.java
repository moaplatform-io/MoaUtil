package moa.io.byteparse;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import moa.io.byteparse.config.ByteParse;

public class DtoHelper {

	static Map<Class,List<MoaClassFieldInfo>> invokemap=new HashMap<Class,List<MoaClassFieldInfo>>();
	
	public static List<MoaClassFieldInfo> getFieldList(Object ins) {
		List<MoaClassFieldInfo> filedList=null;
		filedList=invokemap.get(ins.getClass());
		if(filedList==null) {
			
			Field field[] = ins.getClass().getDeclaredFields();
			filedList=new ArrayList<MoaClassFieldInfo>();
			
			for (int i = 0; i < field.length; i++) {
				if (field[i].isAnnotationPresent(ByteParse.class)) {
					ByteParse parse=field[i].getAnnotation(ByteParse.class);
					int count=parse.order();
					if(count==0) {
						continue;
					}
					MoaClassFieldInfo cwFieldInfo=new MoaClassFieldInfo();
					cwFieldInfo.setField(field[i]);
					cwFieldInfo.setParse(parse);
					filedList.add( cwFieldInfo);
				}
			}
			if(filedList.size()>0) {
				Collections.sort(filedList, new Comparator<MoaClassFieldInfo>() {
					@Override
					public int compare(MoaClassFieldInfo o1, MoaClassFieldInfo o2) {
						int o1cnt=o1.getParse().order();
						int o2cnt=o2.getParse().order();
						return o1cnt-o2cnt;
					}
				});
				invokemap.put(ins.getClass(), filedList);
			}
		}
		return filedList;
	}
}
