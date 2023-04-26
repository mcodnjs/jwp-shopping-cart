package cart.dao;

import cart.controller.dto.ProductRequest;
import cart.domain.Product;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ProductDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<Product> productRowMapper = (resultSet, rowNum) -> new Product(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("image_url"),
            resultSet.getInt("price")
    );

    public ProductDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product")
                .usingGeneratedKeyColumns("id");
    }

    public Long save(final ProductRequest productRequest) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", productRequest.getName());
        params.put("price", productRequest.getPrice());
        params.put("image_url", productRequest.getImageUrl());

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    public List<Product> findAll() {
        String sql = "SELECT * FROM product";

        return jdbcTemplate.query(sql, productRowMapper);
    }

    public Optional<Product> findById(final Long productId) {
        String sql = "SELECT * from product where id = ?";

        try {
            Product product = jdbcTemplate.queryForObject(sql, productRowMapper, productId);
            return Optional.ofNullable(product);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void updateById(final Long id, final ProductRequest productRequest) {
        String sql = "UPDATE product SET name = ?, image_url = ?, price = ? WHERE id = ?";

        jdbcTemplate.update(sql, ps -> {
            ps.setString(1, productRequest.getName());
            ps.setString(2, productRequest.getImageUrl());
            ps.setInt(3, productRequest.getPrice());
            ps.setLong(4, id);
        });
    }

    public void deleteById(final Long id) {
        String sql = "DELETE FROM product WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }
}
