
package com.bookmygift.entity;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EnityClassJunitTest {

	@Test
	void testMyPOJO() throws Exception {

		Set<String> pojoClasses = new HashSet<>( Arrays.asList("com.bookmygift.entity.Properties","com.bookmygift.security.info.AuthRequest","com.bookmygift.security.info.AuthResponse"));

		for (String string : pojoClasses) {

			Class<?> cls = Class.forName(string);

			// Need to check why constructor instance is not getting created
			Constructor<?> constructor = cls.getConstructor();

			Object obj = constructor.newInstance();

			Method[] methods = cls.getDeclaredMethods();

			for (Method method : methods) {

				method.setAccessible(true);

				if (method.getName().startsWith("set")) {

					if (method.getParameters()[0].getType() == Integer.class) {

						method.invoke(obj, 100);

					} else if (method.getParameters()[0].getType() == String.class) {

						method.invoke(obj, new String("Sample String"));

					} else if (method.getParameters()[0].getType() == Double.class) {

						method.invoke(obj, 1000D);

					} else if (method.getParameters()[0].getType() == ObjectId.class) {

						method.invoke(obj, ObjectId.get());

					} else if (method.getParameters()[0].getType() == Set.class) {

						method.invoke(obj, new HashSet<>());

					} else if (method.getParameters()[0].getType() == Long.class) {

						method.invoke(obj, 1L);

					} else if (method.getParameters()[0].getType() == List.class) {

						method.invoke(obj, new ArrayList<>());

					} else {

						Class<?> associatedClass = Class
								.forName(method.getParameters()[0].getType().toString().replace("class ", ""));

						Constructor<?> associatedClassConstructpr = associatedClass.getConstructor();

						Object associatedObject = associatedClassConstructpr.newInstance();

						method.invoke(obj, associatedObject);

					}

				}

			}

			for (Method method : methods) {

				method.setAccessible(true);

				if (method.getName().startsWith("get")) {

					assertNotNull(method.invoke(obj));

				} else if (method.getName().startsWith("hashCode")) {

					assertNotNull(method.invoke(obj));

				} else if (method.getName().startsWith("equals")) {

					assertTrue((Boolean) method.invoke(obj, obj));

					assertFalse((Boolean) method.invoke(obj, "Non Movie Object"));

				}

			}

		}

	}

}
