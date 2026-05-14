package com.matchpuff.matchingservice.matching_service.domain.model;

import com.matchpuff.matchingservice.matching_service.domain.exceptions.InvalidInputException;
import com.matchpuff.matchingservice.matching_service.domain.model.Category;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class CategoryTest {

    @Test
    void constructor_validName_normalizesAndSets() {
        Category cat = new Category("  Technology  ");
        assertThat(cat.getName()).isEqualTo("technology");
    }

    @Test
    void constructor_nullName_throwsInvalidInputException() {
        assertThatThrownBy(() -> new Category(null))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining("cannot be blank");
    }

    @Test
    void constructor_blankName_throwsInvalidInputException() {
        assertThatThrownBy(() -> new Category("   "))
                .isInstanceOf(InvalidInputException.class);
    }

    @Test
    void constructor_nameTooLong_throwsInvalidInputException() {
        String longName = "a".repeat(51);
        assertThatThrownBy(() -> new Category(longName))
                .isInstanceOf(InvalidInputException.class)
                .hasMessageContaining("50 characters");
    }

    @Test
    void constructor_nameExactly50Chars_doesNotThrow() {
        String name = "a".repeat(50);
        assertThatCode(() -> new Category(name)).doesNotThrowAnyException();
    }

    @Test
    void setId_andGetId_work() {
        Category cat = new Category("Tech");
        UUID id = UUID.randomUUID();
        cat.setId(id);
        assertThat(cat.getId()).isEqualTo(id);
    }
}
