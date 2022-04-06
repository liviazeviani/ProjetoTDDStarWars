package com.liviazeviani.starwars.repository;

import com.liviazeviani.starwars.model.Jedi;

import java.util.List;
import java.util.Optional;

public interface JediRepository {

    //o optional é um tipo de retorno opcional - pode retornar lista, objeto...
    Optional<Jedi> findById(int id);

    //já aqui necessariamente vai me retornar uma lista
    List<Jedi> findAll();

    //para atualizar
    boolean update(Jedi jedi);

    //para salvar
    Jedi save(Jedi jedi);

    //para deletar
    boolean delete(int id);
}
