package srv.utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.support.RequestContextUtils;

public class FlashUtil {

	private static Logger log = LoggerFactory.getLogger(FlashUtil.class);
	
	public static void addFlashAttr(HttpServletRequest request, String key, Object obj) {
		Map<String, Object> outputFlashMap = RequestContextUtils.getOutputFlashMap(request);
		if (outputFlashMap != null) {
			outputFlashMap.put(key,obj);
		} else {
			log.debug("null output flash map");
		}
	}
	
	public static Object getFlashAttr(HttpServletRequest request, String key ) {
	    Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
	    if (inputFlashMap != null) {
	        return inputFlashMap.get(key);
	    }
	    return null;
	}
	
	
}
