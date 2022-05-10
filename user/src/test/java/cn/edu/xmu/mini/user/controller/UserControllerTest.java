package cn.edu.xmu.mini.user.controller;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @Test
    void getSessionByCode() throws Exception {
        String responseString=this.mvc.perform(get("/user/token")
                .param("code", "023um8000a2dON1GiZ300ALPEn2um80g")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andReturn().getResponse().getContentAsString();
//        String expectedResponse = "{\"errno\":0,\"data\":{\"session_key\":\"uQER4NDjMtLx90wnxMuRbg==\",\"openid\":\"o-tMy6JWvFAUp6l7NWHadbr9SlBA\"},\"errmsg\":\"成功\"}";
        String expectedResponse = "{\"errno\":0,\"errmsg\":\"成功\"}";
        System.out.println(expectedResponse);
        JSONAssert.assertEquals(expectedResponse, responseString, false);
    }
}