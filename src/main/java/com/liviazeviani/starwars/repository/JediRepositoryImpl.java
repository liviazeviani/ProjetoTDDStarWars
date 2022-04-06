package com.liviazeviani.starwars.repository;

import com.liviazeviani.starwars.model.Jedi;
import com.liviazeviani.starwars.service.JediService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class JediRepositoryImpl implements JediRepository {

    private static final Logger logger = LogManager.getLogger(JediService.class);

    //para conseguir fazer as queries
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    //construtores que identificque e valide o JdbcTemplate
    //datasource também faz queries e schema sql
    public JediRepositoryImpl(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;

        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource) //datasource fará a query sql
                .withTableName("jedis") //nome da tabela
                .usingGeneratedKeyColumns("id"); //id da coluna será o id

    }

    @Override
    public Optional<Jedi> findById(int id) {
        try{
            Jedi jedi = jdbcTemplate.queryForObject("SELECT + FROM jedis WHERE id = ?",
                    new Object[]{id},
                    //estou mapeando um objeto fazendo um rowmapper. no rowmapper vou setar os objetos
                     (rs, rowNum) -> {
                        Jedi p = new Jedi();
                        p.setId(rs.getInt("id"));
                        p.setName(rs.getString("name"));
                        p.setStrength(rs.getInt("strength"));
                        p.setVersion(rs.getInt("version"));
                        return p;

                     });
            return Optional.of(jedi);
        } catch (EmptyResultDataAccessException e){
            return Optional.empty();
        }
    }

    @Override
    public List<Jedi> findAll() {
        return null;
    }

    @Override
    public boolean update(Jedi jedi) {
        return false;
    }

    @Override
    public Jedi save(Jedi jedi) {
        return null;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }
}
