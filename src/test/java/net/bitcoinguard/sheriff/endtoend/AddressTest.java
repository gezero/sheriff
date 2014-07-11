package net.bitcoinguard.sheriff.endtoend;

import net.bitcoinguard.sheriff.ApplicationTests;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Created by Jiri on 11. 7. 2014.
 */
public class AddressTest extends ApplicationTests {
    @Autowired
    private WebApplicationContext context;

    MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void createNewAddress() throws Exception {
        mockMvc
                .perform(post("/rest/addresses")
                                .content("{}")
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print());
    }
}
