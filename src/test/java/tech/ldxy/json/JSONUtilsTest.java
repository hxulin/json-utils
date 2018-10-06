package tech.ldxy.json;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import tech.ldxy.json.bean.Address;
import tech.ldxy.json.bean.User;

public class JSONUtilsTest {

	@SerializeField(type = User.class, include = {"name", "id", "password"})
	@Test
	public void test1() {
		User user = new User(1L, "hxulin", "123456");
		List<Address> addresses = new ArrayList<Address>();
		Address a1 = new Address("hxulin's home", "hxulin's school", user);
		Address a2 = new Address("hxulin's home2", "hxulin's school2", user);
		addresses.add(a1);
		addresses.add(a2);
		user.setAddresses(addresses);
		
		// 使用方法传参的方式添加过滤条件
		SerializationStrategy strategy = SerializationStrategy.include(User.class, "name", "id", "password");
		System.out.println("test1 - JSON动态过滤方案一:[方法传参] - " + JSONUtils.toJson(user, strategy));
		
		// 检查本方法(test1)上的注解, 添加过滤条件
		System.out.println("test1 - JSON动态过滤方案二:[注解传参] - " + JSONUtils.toJsonUseMetadata(user));
	}
	
	@SerializeField(type = User.class, include = {"name", "id", "addresses"})
	@MultiSerializeField(type = Address.class, exclude = "user")
	@Test
	public void test2() {
		User user = new User(1L, "hxulin", "123456");
		List<Address> addresses = new ArrayList<Address>();
		Address a1 = new Address("hxulin's home", "hxulin's school", user);
		Address a2 = new Address("hxulin's home2", "hxulin's school2", user);
		addresses.add(a1);
		addresses.add(a2);
		user.setAddresses(addresses);
		
		// 使用方法传参的方式添加序列化策略
		SerializationStrategy[] strategies = new SerializationStrategy[] {
			SerializationStrategy.include(User.class, "name", "id", "addresses"),
			SerializationStrategy.exclude(Address.class, "user")
		};
		System.out.println("test2 - JSON动态过滤方案一:[方法传参] - " + JSONUtils.toJson(user, strategies));
		
		// 检查本方法(test2)上的注解, 添加序列化策略
		System.out.println("test2 - JSON动态过滤方案二:[注解传参] - " + JSONUtils.toJsonUseMetadata(user));
	}

	@SerializeField(type = Address.class, include = {"school", "home", "user"})
	@MultiSerializeField(type = User.class, exclude = {"addresses", "password"})
	@Test
	public void test3() {
		User user = new User(1L, "hxulin", "123456");
		List<Address> addresses = new ArrayList<Address>();
		Address a1 = new Address("hxulin's home", "hxulin's school", user);
		Address a2 = new Address("hxulin's home2", "hxulin's school2", user);
		addresses.add(a1);
		addresses.add(a2);
		user.setAddresses(addresses);

		// 使用方法传参的方式添加序列化策略
		SerializationStrategy[] strategies = new SerializationStrategy[] {
			SerializationStrategy.include(Address.class, "school", "home", "user"),
			SerializationStrategy.exclude(User.class, "addresses", "password")
		};
		System.out.println("test3 - JSON动态过滤方案一:[方法传参] - " + JSONUtils.toJson(a1, strategies));
		
		// 检查本方法(test3)上的注解, 添加序列化策略
		System.out.println("test3 - JSON动态过滤方案二:[注解传参] - " + JSONUtils.toJsonUseMetadata(a1));
	}

	@MoreSerializeField({
		@SerializeField(type = Address.class, include = {"school", "home", "user"}),
		@SerializeField(type = User.class, include = {"id", "name"})
	})
	@Test
	public void test4() {
		User user = new User(1L, "hxulin", "123456");
		List<Address> addresses = new ArrayList<Address>();
		Address a1 = new Address("hxulin's home", "hxulin's school", user);
		Address a2 = new Address("hxulin's home2", "hxulin's school2", user);
		addresses.add(a1);
		addresses.add(a2);
		user.setAddresses(addresses);
		
		// 使用方法传参的方式添加序列化策略
		SerializationStrategy[] strategies = new SerializationStrategy[] {
			SerializationStrategy.include(Address.class, "school", "home", "user"),
			SerializationStrategy.include(User.class, "id", "name")
		};
		System.out.println("test4 - JSON动态过滤方案一:[方法传参] - " + JSONUtils.toJson(a1, strategies));
		
		// 检查本方法(test4)上的注解, 添加序列化策略
		System.out.println("test4 - JSON动态过滤方案二:[注解传参] - " + JSONUtils.toJsonUseMetadata(a1));
	}
	
	/**
	 * 此方法与无参的 test4 方法重载
	 *   注: 类中的重载方法如果要做 JSON 序列化, 不能使用注解的方式添加序列化策略
	 *        否则会抛出方法重名的异常
	 *        
	 *   有重载方法时, 请使用方法传参的方式添加序列化策略
	 */
	public void test4(int x) {

	}
	
	public static void main(String[] args) {
		
		JSONUtilsTest instance = new JSONUtilsTest();
		
		instance.test1();
		System.out.println();
		instance.test2();
		System.out.println();
		instance.test3();
		System.out.println();
		
		/**
		 * 注: 由于 Test 类中存在两个重载的test4方法,
		 *      在使用注解添加序列化策略时, 会抛出方法重名的异常
		 *   
		 *  当类中的重载方法需要调用 JSONUtils.toJson 的方法做序列化时,
		 *      请使用方法传参的方式添加序列化策略
		 *      
		 *  两种解决方法: 1、类中不使用重载的方法, 此处可以将 public void test4(int x) 方法注释掉
		 *             2、类中存在重载方法, 在做 JSON 序列化时,
		 *                 使用 JSONUtils.toJson(Object object, SerializationStrategy... strategies) 方法,
		 *                 不使用 JSONUtils.toJsonUseMetadata(Object object) 的方式
		 */
		instance.test4();
	}
}
