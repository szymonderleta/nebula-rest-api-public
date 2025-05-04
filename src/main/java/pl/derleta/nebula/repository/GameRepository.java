package pl.derleta.nebula.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.derleta.nebula.domain.entity.GameEntity;
import pl.derleta.nebula.domain.model.Game;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, Integer> {

    /**
     * Retrieves the next available ID for a GameEntity by incrementing the maximum current ID.
     *
     * @return the next available ID as an integer
     */
    @Query("""
            SELECT MAX(ge.id) + 1 AS next_id FROM GameEntity ge
            """)
    int getNextId();

    /**
     * Retrieves a paginated list of GameEntity objects based on the provided specification and pagination parameters.
     *
     * @param spec     a Specification object defining the criteria for filtering the GameEntity objects
     * @param pageable a Pageable object defining the pagination and sorting information
     * @return a Page containing GameEntity objects that match the specified criteria
     */
    Page<GameEntity> findAll(Specification<GameEntity> spec, Pageable pageable);

    /**
     * Retrieves a GameEntity based on its name.
     *
     * @param name the name of the game to retrieve
     * @return an Optional containing the GameEntity if found, or an empty Optional if not found
     */
    @Query("""
            SELECT g FROM GameEntity g
            WHERE g.name = :name
            """)
    Optional<GameEntity> findByName(@Param("name") String name);

    @Query("""
            SELECT g FROM GameEntity g
            WHERE g.name = :name AND g.id <> :selfId
            """)
    Optional<GameEntity> findByNameOtherThanSelfId(@Param("selfId") Integer selfId, @Param("name") String name);


    /**
     * Retrieves a list of enabled GameEntity objects.
     *
     * @return a list of GameEntity instances where the enable field is true
     */
    @Query("""
            SELECT g FROM GameEntity g
            WHERE g.enable = true
            """)
    List<GameEntity> getEnabled();

}
