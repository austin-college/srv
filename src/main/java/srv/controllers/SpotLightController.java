package srv.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import srv.domain.spotlight.SpotLight;
import srv.domain.spotlight.SpotLightDao;

@Controller
@EnableWebSecurity
public class SpotLightController {
	
	private static Logger log = LoggerFactory.getLogger(SpotLightController.class);

	
	@Autowired
	SpotLightDao spotLightDao;
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/spotlight")
	public ModelAndView basePageAction(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ModelAndView mav = new ModelAndView("spotlight/basepage");
		
		
		SpotLight s = spotLightDao.spotLightById(1);
		
		mav.addObject("spotId", s.getSid());
		mav.addObject("spotTxt", s.getSpotText());
		
		return mav;
	}
	
	
    @RequestMapping(value = "/spotlight/img/{id}", method = RequestMethod.GET)
    public void getImage(HttpServletResponse response, @PathVariable Integer id) throws Exception {

    	SpotLight s = spotLightDao.spotLightById(id);
    	
    	/*
    	 * spotlight image missing;  use default 
    	 */
    	if (s.getImg() == null) {
    		log.debug("using default image for spot light");
	        ClassPathResource imgFile = new ClassPathResource("/static/images/defaultSpotlight.jpg", this.getClass().getClassLoader());
	        response.setContentType(MediaType.IMAGE_PNG_VALUE);
	        StreamUtils.copy(imgFile.getInputStream(), response.getOutputStream());
    	}
    	else {
    		log.debug("using db image for spot light");
    		
    		response.setContentLength(s.getImgSize());
    		response.setContentType(s.getImgType());
    		ByteArrayInputStream sin = new ByteArrayInputStream(s.getImg());
    		StreamUtils.copy(sin, response.getOutputStream());
    	}
        
    }
    
    @RequestMapping(value = "/spotlight/img/uploadFile", method = RequestMethod.POST)
    public RedirectView submitImage(@RequestParam("file") MultipartFile file, ModelMap modelMap) throws Exception {

    	/*
    	 * if no file or empty....do nothing and return to base page
    	 */
    	if (file.getSize()==0 || file.getBytes() == null || file.getBytes().length == 0)
    		return new RedirectView("/srv/spotlight");

    	/*
    	 * otherwise, looks like we have a file.  is the image too big for our db?
    	 */
    	
    	if (file.getSize()>Integer.MAX_VALUE-1) {
    		
    		return new RedirectView("/srv/spotlight");
    	}

    	/*
    	 * ok to proceed....
    	 */
    	log.debug(file.getContentType());
    	log.debug("size: {}",file.getSize());
    	
    	/*
    	 * get the singleton splotlight object...always id 1
    	 */
    	SpotLight s = spotLightDao.spotLightById(1);
    	
    	/*
    	 * change essential fields and update the db.
    	 */
    	s.setImgType(file.getContentType());
    	s.setImgSize((int)file.getSize());
    	s.setImg(file.getBytes());
    	
    	spotLightDao.update(s);
    	
        return new RedirectView("/srv/spotlight");
    }
    
    
    
    
    @RequestMapping(value = "/spotlight/txt", method = RequestMethod.POST)
    public RedirectView submitText(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    	SpotLight s = spotLightDao.spotLightById(1);
    	
    	String htmlStr = request.getParameter("spotTxt");
    	log.debug(htmlStr);
    	
    	s.setSpotText(htmlStr);
    	
    	spotLightDao.update(s);
    	
        return new RedirectView("/srv/spotlight");
    }
	
}
