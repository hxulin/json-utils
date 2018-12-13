# json-utils
### 基于fastjson封装的JSON序列化工具类，可动态过滤序列化的字段。

> 此工具类是从 Liuyis 的 jsonfilter 库借鉴而来, 地址: [https://github.com/Liuyis/jsonfilter](https://github.com/Liuyis/jsonfilter)
>
> 在此特别感谢 Liuyis 提供的优秀的思路和方法。
### Example1
```java
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
```
输出：

test1 - JSON动态过滤方案一:[方法传参] - {"id":1,"name":"hxulin","password":"123456"}

test1 - JSON动态过滤方案二:[注解传参] - {"id":1,"name":"hxulin","password":"123456"}

### Example2

```java
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
```
输出：

test2 - JSON动态过滤方案一:[方法传参] - {"addresses":[{"home":"hxulin's home","school":"hxulin's school"},{"home":"hxulin's home2","school":"hxulin's school2"}],"id":1,"name":"hxulin"}

test2 - JSON动态过滤方案二:[注解传参] - {"addresses":[{"home":"hxulin's home","school":"hxulin's school"},{"home":"hxulin's home2","school":"hxulin's school2"}],"id":1,"name":"hxulin"}

### Example3

```java
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
```
输出：

test3 - JSON动态过滤方案一:[方法传参] - {"home":"hxulin's home","school":"hxulin's school","user":{"id":1,"name":"hxulin"}}

test3 - JSON动态过滤方案二:[注解传参] - {"home":"hxulin's home","school":"hxulin's school","user":{"id":1,"name":"hxulin"}}

### Example4

```java
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
```
输出：

test4 - JSON动态过滤方案一:[方法传参] - {"home":"hxulin's home","school":"hxulin's school","user":{"id":1,"name":"hxulin"}}

test4 - JSON动态过滤方案二:[注解传参] - {"home":"hxulin's home","school":"hxulin's school","user":{"id":1,"name":"hxulin"}}

