package tech.ldxy.json;

/**
 * 序列化策略
 * 
 * @author hxulin
 *
 */
@SuppressWarnings("rawtypes")
public class SerializationStrategy {
	
	private static final String[] TEMP_NO_LENGTH_ARRAY = new String[0];
	
	private Class type;
	
	private String[] include;
	
	private String[] exclude;

	private SerializationStrategy(Class type, String[] include, String[] exclude) {
		this.type = type;
		this.include = include;
		this.exclude = exclude;
	}

	public Class getType() {
		return type;
	}

	public String[] getInclude() {
		return include;
	}

	public String[] getExclude() {
		return exclude;
	}

	public static SerializationStrategy include(Class type, String... include) {
		return new SerializationStrategy(type, include, TEMP_NO_LENGTH_ARRAY);
	}

	public static SerializationStrategy exclude(Class type, String... exclude) {
		return new SerializationStrategy(type, TEMP_NO_LENGTH_ARRAY, exclude);
	}
}
