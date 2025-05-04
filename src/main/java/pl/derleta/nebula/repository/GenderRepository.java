package pl.derleta.nebula.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.derleta.nebula.domain.entity.GenderEntity;

@Repository
public interface GenderRepository extends JpaRepository<GenderEntity, Integer> {

}
