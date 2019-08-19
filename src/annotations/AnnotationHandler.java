package annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class AnnotationHandler {

	public void getAnnoted(Object obj, Class<? extends Annotation> annotation) {
		Class<?> object = obj.getClass();
		ArrayList<String> elm = new ArrayList<>();
		for(Method m : object.getMethods()) {
			m.setAccessible(true);
			if(m.isAnnotationPresent(annotation)) {
				elm.add(getSerializedKey(m, annotation));
			}
		}
		for(String out : elm) {
			System.err.println(out+" you are using in "+obj.getClass().getSimpleName()+", is still a Work In Progress");
		}
	}
	
	private String getSerializedKey(Method m, Class<? extends Annotation> annotation) {
		return ((Member)m.getAnnotation(annotation)).getName();
	}

}
