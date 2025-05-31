package pl.derleta.nebula.repository.filter;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;
import pl.derleta.nebula.domain.entity.UserAchievementEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
@SuppressWarnings("unchecked")
class UserAchievementsSpecificationsTest {

    @Test
    void hasNotEqualLevel_shouldCreateCorrectPredicate() {
        // Arrange
        int level = 3;
        Root<UserAchievementEntity> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        
        // Mock the path and notEqual operation
        var levelPath = Mockito.mock(jakarta.persistence.criteria.Path.class);
        when(root.get("level")).thenReturn(levelPath);
        
        Predicate expectedPredicate = mock(Predicate.class);
        when(cb.notEqual(levelPath, level)).thenReturn(expectedPredicate);
        
        // Act
        Specification<UserAchievementEntity> spec = UserAchievementsSpecifications.hasNotEqualLevel(level);
        Predicate result = spec.toPredicate(root, query, cb);
        
        // Assert
        assertEquals(expectedPredicate, result);
        verify(root).get("level");
        verify(cb).notEqual(levelPath, level);
    }
    
    @Test
    void hasEqualLevel_shouldCreateCorrectPredicate() {
        // Arrange
        int level = 3;
        Root<UserAchievementEntity> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        
        // Mock the path and equal operation
        var levelPath = Mockito.mock(jakarta.persistence.criteria.Path.class);
        when(root.get("level")).thenReturn(levelPath);
        
        Predicate expectedPredicate = mock(Predicate.class);
        when(cb.equal(levelPath, level)).thenReturn(expectedPredicate);
        
        // Act
        Specification<UserAchievementEntity> spec = UserAchievementsSpecifications.hasEqualLevel(level);
        Predicate result = spec.toPredicate(root, query, cb);
        
        // Assert
        assertEquals(expectedPredicate, result);
        verify(root).get("level");
        verify(cb).equal(levelPath, level);
    }
    
    @Test
    void hasGreaterLevel_shouldCreateCorrectPredicate() {
        // Arrange
        int level = 3;
        Root<UserAchievementEntity> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        
        // Mock the path and greaterThan operation
        var levelPath = Mockito.mock(jakarta.persistence.criteria.Path.class);
        when(root.get("level")).thenReturn(levelPath);
        
        Predicate expectedPredicate = mock(Predicate.class);
        when(cb.greaterThan(levelPath, level)).thenReturn(expectedPredicate);
        
        // Act
        Specification<UserAchievementEntity> spec = UserAchievementsSpecifications.hasGreaterLevel(level);
        Predicate result = spec.toPredicate(root, query, cb);
        
        // Assert
        assertEquals(expectedPredicate, result);
        verify(root).get("level");
        verify(cb).greaterThan(levelPath, level);
    }
    
    @Test
    void hasGreaterOrEqualLevel_shouldCreateCorrectPredicate() {
        // Arrange
        int level = 3;
        Root<UserAchievementEntity> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        
        // Mock the path and greaterThanOrEqualTo operation
        var levelPath = Mockito.mock(jakarta.persistence.criteria.Path.class);
        when(root.get("level")).thenReturn(levelPath);
        
        Predicate expectedPredicate = mock(Predicate.class);
        when(cb.greaterThanOrEqualTo(levelPath, level)).thenReturn(expectedPredicate);
        
        // Act
        Specification<UserAchievementEntity> spec = UserAchievementsSpecifications.hasGreaterOrEqualLevel(level);
        Predicate result = spec.toPredicate(root, query, cb);
        
        // Assert
        assertEquals(expectedPredicate, result);
        verify(root).get("level");
        verify(cb).greaterThanOrEqualTo(levelPath, level);
    }
    
    @Test
    void hasLessLevel_shouldCreateCorrectPredicate() {
        // Arrange
        int level = 3;
        Root<UserAchievementEntity> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        
        // Mock the path and lessThan operation
        var levelPath = Mockito.mock(jakarta.persistence.criteria.Path.class);
        when(root.get("level")).thenReturn(levelPath);
        
        Predicate expectedPredicate = mock(Predicate.class);
        when(cb.lessThan(levelPath, level)).thenReturn(expectedPredicate);
        
        // Act
        Specification<UserAchievementEntity> spec = UserAchievementsSpecifications.hasLessLevel(level);
        Predicate result = spec.toPredicate(root, query, cb);
        
        // Assert
        assertEquals(expectedPredicate, result);
        verify(root).get("level");
        verify(cb).lessThan(levelPath, level);
    }
    
    @Test
    void hasLessOrEqualLevel_shouldCreateCorrectPredicate() {
        // Arrange
        int level = 3;
        Root<UserAchievementEntity> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        
        // Mock the path and lessThanOrEqualTo operation
        var levelPath = Mockito.mock(jakarta.persistence.criteria.Path.class);
        when(root.get("level")).thenReturn(levelPath);
        
        Predicate expectedPredicate = mock(Predicate.class);
        when(cb.lessThanOrEqualTo(levelPath, level)).thenReturn(expectedPredicate);
        
        // Act
        Specification<UserAchievementEntity> spec = UserAchievementsSpecifications.hasLessOrEqualLevel(level);
        Predicate result = spec.toPredicate(root, query, cb);
        
        // Assert
        assertEquals(expectedPredicate, result);
        verify(root).get("level");
        verify(cb).lessThanOrEqualTo(levelPath, level);
    }
    
    @ParameterizedTest
    @CsvSource({
        "greater or equal, greaterThanOrEqualTo",
        "less or equal, lessThanOrEqualTo",
        "greater, greaterThan",
        "less, lessThan",
        "notequal, notEqual",
        "equal, equal",
        ", equal"
    })
    void hasAllFilters_shouldReturnCorrectSpecification(String filterType, String expectedMethod) {
        // Arrange
        int level = 3;
        Root<UserAchievementEntity> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        
        // Mock the path
        var levelPath = Mockito.mock(jakarta.persistence.criteria.Path.class);
        when(root.get("level")).thenReturn(levelPath);
        
        // Mock all possible predicates
        Predicate equalPredicate = mock(Predicate.class);
        Predicate notEqualPredicate = mock(Predicate.class);
        Predicate greaterPredicate = mock(Predicate.class);
        Predicate greaterOrEqualPredicate = mock(Predicate.class);
        Predicate lessPredicate = mock(Predicate.class);
        Predicate lessOrEqualPredicate = mock(Predicate.class);
        
        when(cb.equal(levelPath, level)).thenReturn(equalPredicate);
        when(cb.notEqual(levelPath, level)).thenReturn(notEqualPredicate);
        when(cb.greaterThan(levelPath, level)).thenReturn(greaterPredicate);
        when(cb.greaterThanOrEqualTo(levelPath, level)).thenReturn(greaterOrEqualPredicate);
        when(cb.lessThan(levelPath, level)).thenReturn(lessPredicate);
        when(cb.lessThanOrEqualTo(levelPath, level)).thenReturn(lessOrEqualPredicate);
        
        // Act
        Specification<UserAchievementEntity> spec = UserAchievementsSpecifications.hasAllFilters(level, filterType);
        Predicate result = spec.toPredicate(root, query, cb);
        
        // Assert
        assertNotNull(result);
        verify(root, atLeastOnce()).get("level");
        
        // Verify the correct method was called based on the filter type
        switch (expectedMethod) {
            case "equal" -> verify(cb).equal(levelPath, level);
            case "notEqual" -> verify(cb).notEqual(levelPath, level);
            case "greaterThan" -> verify(cb).greaterThan(levelPath, level);
            case "greaterThanOrEqualTo" -> verify(cb).greaterThanOrEqualTo(levelPath, level);
            case "lessThan" -> verify(cb).lessThan(levelPath, level);
            case "lessThanOrEqualTo" -> verify(cb).lessThanOrEqualTo(levelPath, level);
        }
    }
    
    @Test
    void hasAllFilters_shouldHandleNullFilterType() {
        // Arrange
        int level = 3;
        String filterType = null;
        Root<UserAchievementEntity> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        
        // Mock the path and equal operation (default for null filter type)
        var levelPath = Mockito.mock(jakarta.persistence.criteria.Path.class);
        when(root.get("level")).thenReturn(levelPath);
        
        Predicate expectedPredicate = mock(Predicate.class);
        when(cb.equal(levelPath, level)).thenReturn(expectedPredicate);
        
        // Act
        Specification<UserAchievementEntity> spec = UserAchievementsSpecifications.hasAllFilters(level, filterType);
        Predicate result = spec.toPredicate(root, query, cb);
        
        // Assert
        assertNotNull(result);
        verify(root).get("level");
        verify(cb).equal(levelPath, level);
    }
    
    @Test
    void hasAllFilters_shouldHandleUnknownFilterType() {
        // Arrange
        int level = 3;
        String filterType = "unknown";
        Root<UserAchievementEntity> root = mock(Root.class);
        CriteriaQuery<?> query = mock(CriteriaQuery.class);
        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        
        // Mock the path and equal operation (default for unknown filter type)
        var levelPath = Mockito.mock(jakarta.persistence.criteria.Path.class);
        when(root.get("level")).thenReturn(levelPath);
        
        Predicate expectedPredicate = mock(Predicate.class);
        when(cb.equal(levelPath, level)).thenReturn(expectedPredicate);
        
        // Act
        Specification<UserAchievementEntity> spec = UserAchievementsSpecifications.hasAllFilters(level, filterType);
        Predicate result = spec.toPredicate(root, query, cb);
        
        // Assert
        assertNotNull(result);
        verify(root).get("level");
        verify(cb).equal(levelPath, level);
    }

}
