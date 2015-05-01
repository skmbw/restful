//package com.vteba.web.servlet.bind;
//
//import com.vteba.lang.bytecode.MethodAccess;
//import com.vteba.utils.reflection.AsmUtils;
//
//public class ParameterBinder {
//
//	public static <T> void binder(String parameters, Class<T> beanType) {
//		if (parameters != null && beanType != null) {
//			String[] forms = parameters.split("&");
//			if (forms != null) {
//				MethodAccess methodAccess = AsmUtils.get().createMethodAccess(beanType);
//				for (String form : forms) {
//					if (form != null) {
//						String[] args = form.split("=");
//						if (args != null && args.length == 2) {
//							String key = args[0];
//							String value = args[1];
//							
//							// 回来修改下MethodAccess，能直接返回属性和对应的Class
//							methodAccess.getMethodNames();
//							
//							if (key.equals("id")) {
//								
//								continue;
//							} else if (key.equals("name")) {
//								continue;
//							}
//						}
//					}
//				}
//			}
//		}
//	}
//}
