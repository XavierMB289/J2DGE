package annotations;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Inherited;
import java.lang.annotation.Target;

@Inherited
@Target({ METHOD, CONSTRUCTOR, LOCAL_VARIABLE })
public @interface WIP {
	String method();
}
