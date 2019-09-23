//https://www.callicoder.com/spring-boot-rest-api-tutorial-with-mysql-jpa-hibernate/
package com.magneto.mutant.repository;

import java.util.List;
import com.magneto.mutant.model.Mutant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface mutantRepository extends JpaRepository<Mutant, String>{
    public List<Mutant> findBymutante(int mutante);
    public Mutant findByAdn(String adn);
}


