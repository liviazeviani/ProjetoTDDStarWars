package com.liviazeviani.starwars.service;


import com.liviazeviani.starwars.model.Jedi;
import com.liviazeviani.starwars.repository.JediRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class JediTestService {

    @Autowired
    private JediService jediService;

    @MockBean //mocka resultados e comportamentos da classe
    private JediRepositoryImpl jediRepository;

    @Test
    @DisplayName("Should return Jedi with sucess")
    public void testFindBySucess(){

        //cenário
        Jedi mockJedi = new Jedi(1, "Jedi name", 10, 1);
        Mockito.doReturn(Optional.of(mockJedi)).when(jediRepository).findById(1); //retorne um mock quando id for 1

        //execução
        Optional<Jedi> returnedJedi = jediService.findById(1);

        //assert
        Assertions.assertTrue(returnedJedi.isPresent(), "Jedi not found");
        Assertions.assertSame(returnedJedi.get(), mockJedi, "Jedi must be the same");
    }
    //TODO: Criar teste de erro NOT FOUND

    //TODO: Criar um teste para o findAll();
}
