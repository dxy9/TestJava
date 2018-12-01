
/**  
 * @Title: TestList.java
 * @Package: org.person.dfw.collection
 * @author: 丰伟
 * @date: 2017年5月16日 下午8:49:42
 * @version: V1.0  
 */ 
package org.person.dfw.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.Assert;
import org.junit.Test;

/**
 * @moudle: TestList 
 * @version:v1.0
 * @author: 丰伟
 * @date: 2017年5月16日 下午8:49:42
 *
 */
public class TestList {

	@Test
	public void size() {
		List list = new ArrayList(100);
		Assert.assertEquals(0,list.size());

		List list1 = new CopyOnWriteArrayList(new ArrayList(200));
		Assert.assertEquals(0,list1.size());

	}


	/**查看ArrayList添加顺序:顺序添加的*/
	@Test public void listsort(){
		List list = new ArrayList();
		for( int i = 0; i < 1000; i++ ) {
			list.add(i);
		}
		System.out.println(list.toString());
		Collections.sort(list);
		System.out.println(list.toString());
	}
	
	/**不要在 foreach 循环里进行元素的 remove/add 操作。 remove 元素请使用 Iterator方式，如果并发操作，需要对 Iterator 对象加锁*/
	@Test public void foreach_remove1(){						//【报错】
		List<String> a = new ArrayList<String>();
		a.add("1");
		a.add("2");
		for (String temp : a) {
			if ("1".equals(temp)) {
				a.remove(temp);
			}
		}
	}
	
	@Test public void foreach_remove2(){						//【不报错】
		List<String> a = new ArrayList<String>();
		a.add("1");
		a.add("2");
		Iterator<String> it = a.iterator();
		while (it.hasNext()) {
			String temp = it.next();
			if ("2".equals(temp)) {
				it.remove();
			}
		}
	}
	
	/**初始化话大小和size不同*/
	@Test public void sizeAndInitalizeSize(){
		List list = new ArrayList<>(1000);
		System.out.println(list.size());
	}
}