package com.liviazeviani.starwars.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.liviazeviani.starwars.model.Jedi;
import com.liviazeviani.starwars.service.JediService;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


import java.util.Optional;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JediControllerTest {

    @MockBean
    private JediService jediService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /jedi/1 - SUCESS")
    public void TestGetJediByIdWithSucess() throws Exception{

        //cenário
        Jedi mockJedi = new Jedi(1, "Han Solo", 10, 1);
        Mockito.doReturn(Optional.of(mockJedi)).when(jediService).findById(1);

        //execução
        mockMvc.perform(get("/jedi/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
                .andExpect(header().string(HttpHeaders.LOCATION, "/jedi/1"))

                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.id", is("Han Solo")))
                .andExpect(jsonPath("$.id", is(10)))
                .andExpect(jsonPath("$.id", is(1)));

    }

    @Test
    @DisplayName("GET /jedi/1 - NOT FOUND")
    public void TestGetJediByIdNotFound() throws Exception{

        Mockito.doReturn(Optional.empty()).when(jediService).findById(1);

        mockMvc.perform(get("/jedi/{1}", 1))
                .andExpect(status().isNotFound());
    }

    // TODO teste do POST com sucesso
    // TODO teste do PUT com sucesso
    // TODO teste do PUT com versão já existinte - retorna conflito
    // TODO teste do PUT com erro - not found
    // TODO teste do DELETE com sucesso
    // TODO teste do DELETE com erro - com id já deletado
    // TODO teste do DELETE com erro - com internal server error


    //metodo auxiliar que constrói objeto (json) como string
    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
