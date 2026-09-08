package fr.fortytwo.repositories;

import fr.fortytwo.models.Product;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProductsRepositoryJdbcImplTest {

    // -----------------------------------------------------------------------
    // Pre-prepared model objects that match the seed data in data.sql
    // -----------------------------------------------------------------------
    final List<Product> EXPECTED_FIND_ALL_PRODUCTS = Arrays.asList(
            new Product(1L, "Apple",      0.99),
            new Product(2L, "Banana",     0.49),
            new Product(3L, "Cherry",     2.99),
            new Product(4L, "Date",       4.50),
            new Product(5L, "Elderberry", 7.99)
    );

    final Product EXPECTED_FIND_BY_ID_PRODUCT = new Product(3L, "Cherry", 2.99);

    final Product EXPECTED_UPDATED_PRODUCT = new Product(2L, "Banana Gold", 1.49);

    // -----------------------------------------------------------------------
    // Infrastructure — fresh DB per test
    // -----------------------------------------------------------------------
    private EmbeddedDatabase embeddedDatabase;
    private ProductsRepository repository;

    @BeforeEach
    void init() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();

        repository = new ProductsRepositoryJdbcImpl(embeddedDatabase);
    }

    @AfterEach
    void tearDown() {
        embeddedDatabase.shutdown();
    }

    // -----------------------------------------------------------------------
    // findAll
    // -----------------------------------------------------------------------

    @Test
    void testFindAllReturnsAllProducts() {
        List<Product> actual = repository.findAll();
        assertEquals(EXPECTED_FIND_ALL_PRODUCTS, actual,
                "findAll() must return all 5 seed products in id order");
    }

    @Test
    void testFindAllReturnsCorrectSize() {
        assertEquals(5, repository.findAll().size());
    }

    // -----------------------------------------------------------------------
    // findById
    // -----------------------------------------------------------------------

    @Test
    void testFindByIdReturnsCorrectProduct() {
        Optional<Product> actual = repository.findById(3L);
        assertTrue(actual.isPresent(), "Product with id=3 must exist");
        assertEquals(EXPECTED_FIND_BY_ID_PRODUCT, actual.get());
    }

    @Test
    void testFindByIdReturnsEmptyForUnknownId() {
        Optional<Product> actual = repository.findById(999L);
        assertFalse(actual.isPresent(), "findById with unknown id must return empty Optional");
    }

    // -----------------------------------------------------------------------
    // save
    // -----------------------------------------------------------------------

    @Test
    void testSaveInsertsNewProduct() {
        Product newProduct = new Product(6L, "Fig", 3.25);
        repository.save(newProduct);

        Optional<Product> saved = repository.findById(6L);
        assertTrue(saved.isPresent(), "Saved product must be retrievable by id");
        assertEquals(newProduct, saved.get());
    }

    @Test
    void testSaveIncreasesCount() {
        repository.save(new Product(6L, "Fig", 3.25));
        assertEquals(6, repository.findAll().size());
    }

    // -----------------------------------------------------------------------
    // update
    // -----------------------------------------------------------------------

    @Test
    void testUpdateChangesProductFields() {
        repository.update(EXPECTED_UPDATED_PRODUCT);

        Optional<Product> updated = repository.findById(2L);
        assertTrue(updated.isPresent());
        assertEquals(EXPECTED_UPDATED_PRODUCT, updated.get());
    }

    @Test
    void testUpdateDoesNotChangeRowCount() {
        repository.update(EXPECTED_UPDATED_PRODUCT);
        assertEquals(5, repository.findAll().size(),
                "update() must not insert or delete rows");
    }

    // -----------------------------------------------------------------------
    // delete
    // -----------------------------------------------------------------------

    @Test
    void testDeleteRemovesProduct() {
        repository.delete(4L);
        assertFalse(repository.findById(4L).isPresent(),
                "Product with id=4 must not exist after delete");
    }

    @Test
    void testDeleteDecreasesCount() {
        repository.delete(4L);
        assertEquals(4, repository.findAll().size());
    }

    @Test
    void testDeleteNonExistentIdDoesNotThrow() {
        assertDoesNotThrow(() -> repository.delete(999L),
                "Deleting a non-existent id must not throw");
    }
}
