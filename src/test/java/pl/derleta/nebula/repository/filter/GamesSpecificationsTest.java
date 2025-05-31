package pl.derleta.nebula.repository.filter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import pl.derleta.nebula.domain.entity.GameEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
class GamesSpecificationsTest {

    @SuppressWarnings("unchecked")
    @Test
    void hasAllFilters_ValidNameAndEnable_ReturnsCombinedPredicate() {
        // Arrange
        String name = "test";
        boolean enable = true;
        Specification<GameEntity> specification = GamesSpecifications.hasAllFilters(name, enable);

        Root<GameEntity> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        Predicate namePredicate = mock(Predicate.class);
        Predicate enablePredicate = mock(Predicate.class);
        Predicate combinedPredicate = mock(Predicate.class);

        when(cb.like(root.get("name"), "%test%")).thenReturn(namePredicate);
        when(cb.equal(root.get("enable"), true)).thenReturn(enablePredicate);
        when(cb.and(namePredicate, enablePredicate)).thenReturn(combinedPredicate);

        // Act
        Predicate result = specification.toPredicate(root, query, cb);

        // Assert
        assertNotNull(result);
        assertEquals(combinedPredicate, result);
    }

    @SuppressWarnings("unchecked")
    @Test
    void hasAllFilters_NullName_ReturnsCombinedPredicateWithWildcard() {
        // Arrange
        String name = null;
        boolean enable = true;
        Specification<GameEntity> specification = GamesSpecifications.hasAllFilters(name, enable);

        Root<GameEntity> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        Predicate namePredicate = mock(Predicate.class);
        Predicate enablePredicate = mock(Predicate.class);
        Predicate combinedPredicate = mock(Predicate.class);

        when(cb.like(root.get("name"), "%%")).thenReturn(namePredicate);
        when(cb.equal(root.get("enable"), true)).thenReturn(enablePredicate);
        when(cb.and(namePredicate, enablePredicate)).thenReturn(combinedPredicate);

        // Act
        Predicate result = specification.toPredicate(root, query, cb);

        // Assert
        assertNotNull(result);
        assertEquals(combinedPredicate, result);
    }

    @SuppressWarnings("unchecked")
    @Test
    void hasAllFilters_EmptyName_ReturnsCombinedPredicateWithWildcard() {
        // Arrange
        String name = "";
        boolean enable = true;
        Specification<GameEntity> specification = GamesSpecifications.hasAllFilters(name, enable);

        Root<GameEntity> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        Predicate namePredicate = mock(Predicate.class);
        Predicate enablePredicate = mock(Predicate.class);
        Predicate combinedPredicate = mock(Predicate.class);

        when(cb.like(root.get("name"), "%%")).thenReturn(namePredicate);
        when(cb.equal(root.get("enable"), true)).thenReturn(enablePredicate);
        when(cb.and(namePredicate, enablePredicate)).thenReturn(combinedPredicate);

        // Act
        Predicate result = specification.toPredicate(root, query, cb);

        // Assert
        assertNotNull(result);
        assertEquals(combinedPredicate, result);
    }

    @SuppressWarnings("unchecked")
    @Test
    void hasAllFilters_EnableFalse_ReturnsCombinedPredicateWithDisabled() {
        // Arrange
        String name = "test";
        boolean enable = false;
        Specification<GameEntity> specification = GamesSpecifications.hasAllFilters(name, enable);

        Root<GameEntity> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        Predicate namePredicate = mock(Predicate.class);
        Predicate enablePredicate = mock(Predicate.class);
        Predicate combinedPredicate = mock(Predicate.class);

        when(cb.like(root.get("name"), "%test%")).thenReturn(namePredicate);
        when(cb.equal(root.get("enable"), false)).thenReturn(enablePredicate);
        when(cb.and(namePredicate, enablePredicate)).thenReturn(combinedPredicate);

        // Act
        Predicate result = specification.toPredicate(root, query, cb);

        // Assert
        assertNotNull(result);
        assertEquals(combinedPredicate, result);
    }

    @SuppressWarnings("unchecked")
    @Test
    void hasName_ValidName_ReturnsExpectedPredicate() {
        // Arrange
        String name = "test";
        Specification<GameEntity> specification = GamesSpecifications.hasName(name);

        Root<GameEntity> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        Predicate namePredicate = mock(Predicate.class);

        when(cb.like(root.get("name"), "%test%")).thenReturn(namePredicate);

        // Act
        Predicate result = specification.toPredicate(root, query, cb);

        // Assert
        assertNotNull(result);
        assertEquals(namePredicate, result);
    }

    @SuppressWarnings("unchecked")
    @Test
    void hasName_NullName_ReturnsWildcardPredicate() {
        // Arrange
        String name = null;
        Specification<GameEntity> specification = GamesSpecifications.hasName(name);

        Root<GameEntity> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        Predicate namePredicate = mock(Predicate.class);

        when(cb.like(root.get("name"), "%%")).thenReturn(namePredicate);

        // Act
        Predicate result = specification.toPredicate(root, query, cb);

        // Assert
        assertNotNull(result);
        assertEquals(namePredicate, result);
    }

    @SuppressWarnings("unchecked")
    @Test
    void hasName_EmptyName_ReturnsWildcardPredicate() {
        // Arrange
        String name = "";
        Specification<GameEntity> specification = GamesSpecifications.hasName(name);

        Root<GameEntity> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        Predicate namePredicate = mock(Predicate.class);

        when(cb.like(root.get("name"), "%%")).thenReturn(namePredicate);

        // Act
        Predicate result = specification.toPredicate(root, query, cb);

        // Assert
        assertNotNull(result);
        assertEquals(namePredicate, result);
    }

    @SuppressWarnings("unchecked")
    @Test
    void isEnable_EnableTrue_ReturnsExpectedPredicate() {
        // Arrange
        boolean enable = true;
        Specification<GameEntity> specification = GamesSpecifications.isEnable(enable);

        Root<GameEntity> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        Predicate enablePredicate = mock(Predicate.class);

        when(cb.equal(root.get("enable"), true)).thenReturn(enablePredicate);

        // Act
        Predicate result = specification.toPredicate(root, query, cb);

        // Assert
        assertNotNull(result);
        assertEquals(enablePredicate, result);
    }

    @SuppressWarnings("unchecked")
    @Test
    void isEnable_EnableFalse_ReturnsExpectedPredicate() {
        // Arrange
        boolean enable = false;
        Specification<GameEntity> specification = GamesSpecifications.isEnable(enable);

        Root<GameEntity> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);

        Predicate enablePredicate = mock(Predicate.class);

        when(cb.equal(root.get("enable"), false)).thenReturn(enablePredicate);

        // Act
        Predicate result = specification.toPredicate(root, query, cb);

        // Assert
        assertNotNull(result);
        assertEquals(enablePredicate, result);
    }

}
