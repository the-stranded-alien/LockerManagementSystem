package lockermanagement.system.ControllerTests;

import lockermanagement.system.models.LockerRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;

public class LockerControllerTest extends AbstractTest{

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void addLockerHappyTest() throws Exception {
        String uri = "/lockers";
        LockerRequest lockerRequest = new LockerRequest(7249999056L, 251001);
        String inputJson = super.mapToJson(lockerRequest);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("Locker Added !", content);
    }

    @Test
    public void addLockerSadTest() throws Exception {
        String uri = "/lockers";
        LockerRequest lockerRequest = new LockerRequest(7249999056L, 100);
        String inputJson = super.mapToJson(lockerRequest);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("Invalid Pin Entered !", content);
    }

    @Test
    public void bookLockerHappyTest() throws Exception {
        String uri = "/lockers/book";
        LockerRequest lockerRequest = new LockerRequest(7249999056L, 251001);
        String inputJson = super.mapToJson(lockerRequest);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals("Locker Booked Successfully !", content);
    }

}
