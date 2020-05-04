package srv.services.reason;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import srv.controllers.reason.ReasonController;
import srv.domain.reason.Reason;
import srv.domain.reason.ReasonDao;


/**
 * How to Test a Controller
 * 
 * @author mahiggs
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(ReasonController.class)
public class ReasonControllerTest {

    @Autowired
    private MockMvc mvc;
    
    @MockBean
    private ReasonDao dao;
 
    @MockBean
    private ReasonService service;
    
    
    @Test
    public void basicHtmlPageTest() throws Exception {
         
    	// train the service to return 2 when asked for count
       when(service.reasonCount()).thenReturn(2);
       
       // train the dao to ask for these when asked to listAll reasons.
		Reason r1 = new Reason().setRid(1).setReason("reason one");
		Reason r2 = new Reason().setRid(2).setReason("reason two");
		
		List<Reason> dummyList = new ArrayList<Reason>();
		dummyList.add(r1);
		dummyList.add(r2);
		
		Mockito.when(dao.listAll()).thenReturn(dummyList);
		
		// now perform the test
		
        mvc.perform(get("/test/reason")
          .contentType(MediaType.TEXT_HTML))
          .andExpect(status().isOk())
          .andExpect(content().string(containsString("We have 2 reasons.")))
          .andExpect(content().string(containsString("<li id=\"rid-1\" >reason one</li>")))
          .andExpect(content().string(containsString("reason two")));
      
        
    }
	
}
