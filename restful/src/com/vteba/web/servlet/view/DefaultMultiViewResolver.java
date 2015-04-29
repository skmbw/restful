package com.vteba.web.servlet.view;

import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

/**
 * 默认的多视图处理器。根据返回的逻辑视图的扩展名进行判断。
 * 
 * @author yinlei
 * @see
 * @since 2015年4月29日 下午12:22:44
 */
public class DefaultMultiViewResolver implements ViewResolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultMultiViewResolver.class);
	
	private Map<String, ViewResolver> viewResolverMap;
	private ViewResolver defaultViewResolver;

	public View resolveViewName(String viewName, Locale locale)
			throws Exception {
		for (Entry<String, ViewResolver> map : viewResolverMap.entrySet()) {
			if (viewName.endsWith(map.getKey())) {
				ViewResolver viewResolver = map.getValue();
				if (viewResolver != null) {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("找到了viewResolver [" + viewResolver
								+ "] for viewName [" + viewName + "]");
					}
					return viewResolver.resolveViewName(viewName, locale);
				} else {
					if (LOGGER.isDebugEnabled()) {
						LOGGER.debug("没有找到viewResolver [" + viewResolver
								+ "] for viewName [" + viewName + "]。将使用默认的视图处理器处理。");
					}
				}
			}
		}

		if (defaultViewResolver != null) {
			return defaultViewResolver.resolveViewName(viewName, locale);
		}
		// 允许剩下的ViewResolver链式处理
		return null;
	}

	public void setViewResolverMap(Map<String, ViewResolver> viewResolverMap) {
		this.viewResolverMap = viewResolverMap;
	}

	public void setDefaultViewResolver(ViewResolver defaultViewResolver) {
		this.defaultViewResolver = defaultViewResolver;
	}

}
