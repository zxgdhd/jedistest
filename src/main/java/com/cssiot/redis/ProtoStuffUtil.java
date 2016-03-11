package com.cssiot.redis;

import java.util.HashMap;

import com.dyuproject.protostuff.GraphIOUtil;
import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

/**
 * @author woow
 *
 * @param <T>
 *            在序列化的过程中会用到LinkedBuffer,可以建立ＬｉｎｋｅｄＢｕｆｆｅｒ对象池应对并发
 *            对于每个类的Ｓｃｈｅｍａ对象可以建立缓存避免重复创建Ｓｃｈｅｍａ对象影响性能
 *            类的Ｓｃｈｅｍａ对象不可使用单例模式作为类的成员变量，因为序列化会破坏单例模式 
 *            
 *            protostuff支持序列化的形式：(本版本支持前三种)
 *            		protobuf 
 *            		protostuff (native) 
 *            		graph 	(protostuff with support for cyclic references. See Serializing Object Graphs) 
 *            		json 
 *            		smile		(binary json useable from the protostuff-json module) 
 *            		xml 
 *            		yaml		(serialization only) kvp (binary uwsgi header)
 * 
 */
public class ProtoStuffUtil<T> {
	
	private static final HashMap<Class, Schema> cachedSchema=new HashMap<Class, Schema>();

	/**
	 * @param obj
	 *            需要序列化的实体
	 * @return 序列化后生成的byte数组 serializeObjectToByteArray byte[] 返回字节数组 woow TODO
	 *         将实体类序列化为字节数组,适合单一对象序列化，多个对象序列化可复用Class,LinkedBuffer,Schema对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> byte[] serializeObjectToByteArray(T obj) {
		if (obj == null)// 若参数对象为null，返回null
			return null;
		Class<T> objClass = (Class<T>) obj.getClass(); // 获取实体的Class对象
		LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);// 获取LinkedBuffer对象
		Schema<T> schema;
		try {
			if((schema=cachedSchema.get(objClass))==null){//从缓存中取Ｓｃｈｅｍａ对象
				schema = RuntimeSchema.createFrom(objClass);// 缓存中不存Ｓｃｈｅｍａ对象，在通过实体创建该类的Scheme对象
				cachedSchema.put(objClass, schema);
			}
			return ProtostuffIOUtil.toByteArray(obj, schema, buffer);// 返回序列化后的字节数组
		}catch(Exception exception){
			throw new IllegalStateException(exception.getMessage(),exception);
		}/*finally {
			 buffer.clear();//初始化并返回当前LinkedBuffer对象，复用对象,此处buffer不需复用
		}*/
	}

	/**
	 * @param obj
	 * @param tClass
	 * @param schema
	 * @param buffer
	 * @return
	 * serializeObjectToByteArray
	 * byte[]
	 * woow
	 * TODO			使用缓存的参数序列化对象
	 */
	public static<T> byte[] serializeObjectToByteArray(T obj,Class<T> tClass,Schema<T> schema,LinkedBuffer buffer){
		try {
			return ProtostuffIOUtil.toByteArray(obj, schema, buffer);// 返回序列化后的字节数组
		}catch(Exception exception){
			throw new IllegalStateException(exception.getMessage(),exception);
		}finally {
			buffer.clear();//初始化buffer对象以供复用
		}
	}
	
	
	/**
	 * @param objBytes		对象被序列化后的byte数组
	 * @param objClass		对象的class对象
	 * @return				返回指定类型的对象
	 * deserializeObjectFromByteArray
	 * T
	 * woow
	 * TODO					反序列化对象
	 */
	public static<T> T deserializeObjectFromByteArray(byte[] objBytes,Class<T> objClass){
		Schema<T> schema;
		try {
			if((schema=cachedSchema.get(objClass))==null){//从缓存中取Ｓｃｈｅｍａ对象
				schema = RuntimeSchema.createFrom(objClass);// 缓存中不存Ｓｃｈｅｍａ对象，在通过实体创建该类的Scheme对象
				cachedSchema.put(objClass, schema);
			}
			T obj=schema.newMessage();//实例化一个空的对象，以供反序列化
			ProtostuffIOUtil.mergeFrom(objBytes, obj, schema);//反序列化
			return obj;
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}
	
	
	/**
	 * @param objBytes
	 * @param objClass
	 * @param schema				类Ｔ对应的Ｓｃｈｅｍａ对象
	 * @return
	 * deserializeObjectFromByteArray
	 * T
	 * woow
	 * TODO
	 */
	public static<T> T deserializeObjectFromByteArray(byte[] objBytes,Class<T> objClass,Schema<T> schema){
		try {
			T obj=schema.newMessage();
			ProtostuffIOUtil.mergeFrom(objBytes, obj, schema);//反序列化
			return obj;
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}
	
	
	/**
	 * Serialization and de-serialization for deep object graphs (references and
	 * cyclic dependencies).
	 * 
	 * Features:
	 * 
	 * high-performance graph serialization handles cyclic dependencies and
	 * polymorphic pojos (interface and abstract classes)
	 * 
	 * Limitations:
	 * 
	 * When a root message is serialized/deserialized, its nested messages
	 * should not contain references to it.
	 */
	@SuppressWarnings("unchecked")
	public static <T> byte[] serializeObjectToGraph(T obj) {
		if (obj == null)// 若参数对象为null，返回null
			return null;
		Class<T> objClass = (Class<T>) obj.getClass(); // 获取实体的Class对象
		LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);// 获取LinkedBuffer对象
		Schema<T> schema;
		try {
			if((schema=cachedSchema.get(objClass))==null){//从缓存中取Ｓｃｈｅｍａ对象
				schema = RuntimeSchema.createFrom(objClass);// 缓存中不存Ｓｃｈｅｍａ对象，在通过实体创建该类的Scheme对象
				cachedSchema.put(objClass, schema);
			}
			return GraphIOUtil.toByteArray(obj, schema, buffer);// 返回序列化后的字节数组
		}catch(Exception exception){
			throw new IllegalStateException(exception.getMessage(),exception);
		}/*finally {
			 buffer.clear();//初始化并返回当前LinkedBuffer对象，复用对象,此处buffer不需复用
		}*/
	}

	/**
	 * @param obj
	 * @param tClass
	 * @param schema
	 * @param buffer
	 * @return
	 * deserializeObjectFromGraph
	 * byte[]
	 * woow
	 * TODO			使用缓存的参数序列化对象
	 */
	public static<T> byte[] serializeObjectToGraph(T obj,Class<T> tClass,Schema<T> schema,LinkedBuffer buffer){
		try {
			return GraphIOUtil.toByteArray(obj, schema, buffer);// 返回序列化后的字节数组
		}catch(Exception exception){
			throw new IllegalStateException(exception.getMessage(),exception);
		}finally {
			buffer.clear();//初始化buffer对象以供复用
		}
	}
	
	
	/**
	 * @param objBytes		对象被序列化后的byte数组
	 * @param objClass		对象的class对象
	 * @return				返回指定类型的对象
	 * deserializeObjectFromGraph
	 * T
	 * woow
	 * TODO					反序列化对象
	 */
	public static<T> T deserializeObjectFromGraph(byte[] objBytes,Class<T> objClass){
		Schema<T> schema;
		try {
			if((schema=cachedSchema.get(objClass))==null){//从缓存中取Ｓｃｈｅｍａ对象
				schema = RuntimeSchema.createFrom(objClass);// 缓存中不存Ｓｃｈｅｍａ对象，在通过实体创建该类的Scheme对象
				cachedSchema.put(objClass, schema);
			}
			T obj=schema.newMessage();//实例化一个空的对象，以供反序列化
			GraphIOUtil.mergeFrom(objBytes, obj, schema);//反序列化
			return obj;
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}
	
	
	/**
	 * @param objBytes
	 * @param objClass
	 * @param schema				类Ｔ对应的Ｓｃｈｅｍａ对象
	 * @return
	 * deserializeObjectFromGraph
	 * T
	 * woow
	 * TODO
	 */
	public static<T> T deserializeObjectFromGraph(byte[] objBytes,Class<T> objClass,Schema<T> schema){
		try {
			T obj=schema.newMessage();
			GraphIOUtil.mergeFrom(objBytes, obj, schema);//反序列化
			return obj;
		} catch (Exception e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}
	


}
