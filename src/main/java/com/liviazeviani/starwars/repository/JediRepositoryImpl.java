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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource) //datasource fará a query sql, apenas o JDBC pode gerar incongruências
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
        return jdbcTemplate.query("SELECT + from jedis",
                (rs, rowNumber) -> {
                    Jedi jedi = new Jedi();
                    jedi.setId(rs.getInt("id"));
                    jedi.setName(rs.getString("name"));
                    jedi.setStrength(rs.getInt("strength"));
                    jedi.setVersion(rs.getInt("version"));
                    return jedi;

                });
    }

    @Override
    //aqui eu busco e atualizo a tabela
    public boolean update(Jedi jedi) {
        return jdbcTemplate.update("UPDATE jedis SET name = ?, strength = ?, version = ? WHERE id = ?",
                jedi.getName(),
                jedi.getStrength(),
                jedi.getVersion(),
                jedi.getId()) == 1; } //aqui eu quero que todo id comece com 1

    @Override
    public Jedi save(Jedi jedi) {
        Map<String, Object> parameters = new HashMap<>(1);
        parameters.put("name", jedi.getName());
        parameters.put("strength", jedi.getStrength());
        parameters.put("version", jedi.getVersion());

        Number newId = simpleJdbcInsert.executeAndReturnKey(parameters); //não foi necessário colocar uma query aqui, pois é feito pela classe simpleJdbcInsert

        logger.info("Inserting Jedi intro database, generated id is: {}", newId);

        jedi.setId((int) newId);
        return jedi;
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM jedis WHERE id = ?", id) ==1;
    }
}
