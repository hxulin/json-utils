package tech.ldxy.json;

import java.lang.reflect.Method;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * 基于fastjson的JSON序列化和解析的工具类
 * 
 * @author hxulin
 *
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public final class JSONUtils {

	/**
	 * JSON解析参数配置:
	 *   DisableCircularReferenceDetect: 禁止循环引用的检测
	 *   WriteDateUseDateFormat: 设置Date类型格式化为 yyyy-MM-dd HH:mm:ss
	 */
	private static final SerializerFeature[] SERIALIZE_FEATURES = {
			SerializerFeature.DisableCircularReferenceDetect,
			SerializerFeature.WriteDateUseDateFormat
	};

	private JSONUtils() {

	}

	private static void addFilterRule(Class entryClazz, String[] includes, String[] excludes, SimpleSerializerFilter filter) {
		if (includes.length > 0 && excludes.length > 0) {
			throw new IncludeAndExcludeConflictException("Can not use both include field and exclude field in an annotation.");
		} else if (includes.length > 0) {
			filter.include(entryClazz, includes);
		} else if (excludes.length > 0) {
			filter.exclude(entryClazz, excludes);
		}
	}

	/**
	 * 将Java对象序列化为JSON字符串
	 * 
	 * @param object 需要序列化的对象
	 * @return 序列化后的JSON字符串
	 */
	public static String toJson(Object object) {
		return JSON.toJSONString(object, SERIALIZE_FEATURES);
	}

	/**
	 * 根据指定的序列化策略, 将Java对象序列化为JSON字符串
	 * 
	 * @param object 需要序列化的对象
	 * @param strategies 序列化策略, 在序列化时指定包含或排除哪些属性
	 * @return 序列化后的JSON字符串
	 */
	public static String toJson(Object object, SerializationStrategy... strategies) {
		SimpleSerializerFilter filter = new SimpleSerializerFilter();
		if (strategies != null) {
			for (SerializationStrategy strategy : strategies) {
				addFilterRule(strategy.getType(), strategy.getInclude(), strategy.getExclude(), filter);
			}
			return JSON.toJSONString(object, filter, SERIALIZE_FEATURES);
		}
		return toJson(object);
	}
	
	/**
	 * 将Java对象序列化为JSON字符串
	 *   此方法会检测调用处方法上的 SerializeField, MultiSerializeField, MoreSerializeField 注解,
	 *   并解析注解中的序列化策略, 在序列化时动态地包含或排除注解中指定的属性
	 * 
	 * @param object 需要序列化的对象
	 * @return 序列化后的JSON字符串
	 */
	public static String toJsonUseMetadata(Object object) {
		// 记录被序列化注解标注的同名方法的个数，如果超过一个则抛出异常
		int methodCount = 0;
		Method method = null;  // 调用本方法的方法
		try {
			Class clazz = Class.forName(Thread.currentThread().getStackTrace()[2].getClassName());  // 调用本方法的类
			String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
			Method[] methods = clazz.getDeclaredMethods();
			for (Method tempMethod : methods) {
				if (tempMethod.getName().equals(methodName)) {
					method = clazz.getDeclaredMethod(methodName, tempMethod.getParameterTypes());
					methodCount++;
				}
			}
			if (methodCount > 1) {
				throw new SameMethodNameException("Methods that use the SerializeField, MultiSerializeField or MoreSerializeField annotations cannot be overrode, there are " + methodCount + " methods named \"" + methodName + "\" in \"" + clazz.getName() + "\".");
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		if (method.isAnnotationPresent(SerializeField.class) || method.isAnnotationPresent(MultiSerializeField.class)
				|| method.isAnnotationPresent(MoreSerializeField.class)) {
			SimpleSerializerFilter filter = new SimpleSerializerFilter();
			if (method.isAnnotationPresent(SerializeField.class)) {
				SerializeField anno = method.getAnnotation(SerializeField.class);
				addFilterRule(anno.type(), anno.include(), anno.exclude(), filter);
			}
			if (method.isAnnotationPresent(MultiSerializeField.class)) {
				MultiSerializeField anno = method.getAnnotation(MultiSerializeField.class);
				addFilterRule(anno.type(), anno.include(), anno.exclude(), filter);
			}
			if (method.isAnnotationPresent(MoreSerializeField.class)) {
				MoreSerializeField moreSerializeField = method.getAnnotation(MoreSerializeField.class);
				SerializeField[] serializeFields = moreSerializeField.value();
				for (SerializeField anno : serializeFields) {
					addFilterRule(anno.type(), anno.include(), anno.exclude(), filter);
				}
			}
			return JSON.toJSONString(object, filter, SERIALIZE_FEATURES);
		}
		return toJson(object);
	}

	/**
	 * 将JSON字符串解析为Java对象
	 * 
	 * @param text 待解析的JSON字符串
	 * @param clazz 解析后的数据类型
	 * @return 解析后的Java对象
	 */
	public static <T> T fromJson(String text, Class<T> clazz) {
		return JSON.parseObject(text, clazz);
	}

}
