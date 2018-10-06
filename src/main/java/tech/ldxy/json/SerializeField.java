package tech.ldxy.json;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SerializeField {

	@SuppressWarnings("rawtypes")
	Class type();

	// JSON 解析时需要保留的字段
	String[] include() default {};

	// JSON 解析时需要去除的字段
	String[] exclude() default {};

}
