package ch.zhaw.fundhive.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import ch.zhaw.fundhive.service.helpers.UserService;
import ch.zhaw.fundhive.service.helpers.CypressDeleteAllInDBService;

@WebMvcTest(CypressDeleteAllInDBController.class)
class CypressDeleteAllInDBControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private CypressDeleteAllInDBService service;

    @Test
    void deleteAllInDB_returnsOkIfAdmin() throws Exception {
        when(userService.userHasRole("admin")).thenReturn(true);

        mockMvc.perform(delete("/api/deleteAll")
                .with(jwt().authorities(() -> "admin")))
                .andExpect(status().isOk())
                .andExpect(content().string("DELETED"));

        verify(service).deleteAll();
    }

    @Test
    void deleteAllInDB_returnsForbiddenIfNotAdmin() throws Exception {
        when(userService.userHasRole("admin")).thenReturn(false);

        mockMvc.perform(delete("/api/deleteAll")
                .with(jwt()))
                .andExpect(status().isForbidden());

        verify(service, never()).deleteAll();
    }
}