package tech.builtrix.context;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import tech.builtrix.annotations.Limited;
import tech.builtrix.exceptions.MaximumRequestPerMinuteExceededException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author : pc`
 * @date : 09/06/2018
 */

@Component
public class RequestLimitInterceptor implements HandlerInterceptor {

	private HashMap<String, List<Date>> requests = new HashMap<>();

	@Override
	public synchronized boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws MaximumRequestPerMinuteExceededException {
		HandlerMethod handlerMethod;
		Method method;
		if (handler instanceof HandlerMethod) {
			handlerMethod = (HandlerMethod) handler;
			method = handlerMethod.getMethod();
		} else {
			return true;
		}
		if (method.isAnnotationPresent(Limited.class)
				|| method.getDeclaringClass().isAnnotationPresent(Limited.class)) {

			Limited limitedData = method.isAnnotationPresent(Limited.class) ? method.getAnnotation(Limited.class)
					: method.getDeclaringClass().getAnnotation(Limited.class);

			String ipAddress = request.getHeader("X-FORWARDED-FOR");
			if (ipAddress == null) {
				ipAddress = request.getRemoteAddr();
			}

			String params = String.join(",", Lists.newArrayList(method.getParameterTypes()).stream()
					.map(x -> x.getSimpleName()).collect(Collectors.toList()));
			String key = method.getDeclaringClass().getSimpleName() + "." + method.getName() + "(" + params + ")-"
					+ ipAddress;

			if (!requests.containsKey(key)) {
				requests.put(key, new ArrayList<>());
			}
			List<Date> list = requests.get(key);
			list.add(new Date());

			long now = new Date().getTime();
			Date start = new Date(now - now % 60000);
			long count = 0;
			try {
				count = list.stream().filter(x -> x.after(start)).count();
			} catch (NullPointerException e) {
				// do nothing
			}

			list.removeAll(list.stream().filter(x -> x.before(start)).collect(Collectors.toList()));

			if (count > limitedData.requestsPerMinutes()) {

				response.setStatus(429);
				return false;
			}
		}

		return true;
	}
}
