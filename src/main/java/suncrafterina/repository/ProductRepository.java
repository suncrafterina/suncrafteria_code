package suncrafterina.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import suncrafterina.domain.Product;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query("From Product p where lower(p.name) = lower(?1) AND p.user.id=(?2)")
    public Optional<Product> findByTitle(String title,Long userId);

    @Query("From Product p where lower(p.name) = lower(?1) AND p.user.id=(?2) AND p.id <> (?3)")
    public Optional<Product> findByTitleAndId(String title,Long userId,Long id);

    @Query("From Product p where lower(p.sku) = lower(?1) AND p.user.id=(?2) AND p.id <> (?3)")
    public Optional<Product> findBySkuAndId(String title,Long userId,Long id);

    @Query(value="select p.id as id,p.name,p.image_file," +
        "p.sku,CONCAT(u.first_name,' ',u.last_name) as vendor,p.manufacturer,p.created_at,p.price," +
        "p.vendor_show_status," +
        "r.rated as rating,p.admin_show_status,p.image_file_thumb,p.is_sponsored,p.is_new_arrival from jhi_product p " +
        "LEFT JOIN ( " +
        "SELECT product_id,(CAST(SUM(rating) as float)/CAST(COUNT(1) as float)) as rated " +
        "FROM jhi_product_user_rating " +
        "GROUP BY product_id " +
        ") r " +
        "ON p.id = r.product_id " +
        "LEFT JOIN jhi_user u ON p.user_id=u.id " +
        "where  (" +
        "lower(p.name) LIKE lower(concat('%', ?1,'%')) OR " +
        "lower(p.sku) LIKE lower(concat('%', ?1,'%')) OR " +
        "lower(CONCAT(u.first_name,' ',u.last_name)) LIKE lower(concat('%', ?1,'%')) OR " +
        "lower(p.manufacturer) LIKE lower(concat('%', ?1,'%'))" +
        ")",nativeQuery = true)
    public Page<Object[]> findAllUserProduct(String search, Pageable pageable);

    @Query(value="select p.id as id,p.name as name,p.image_file," +
        "p.sku,c.title as category,p.manufacturer,p.created_at,p.price," +
        "p.vendor_show_status," +
        "r.rated as rating, p.image_file_thumb from jhi_product p " +
        "LEFT JOIN ( " +
        "SELECT product_id,(CAST(SUM(rating) as float)/CAST(COUNT(1) as float)) as rated " +
        "FROM jhi_product_user_rating " +
        "GROUP BY product_id " +
        ") r " +
        "ON p.id = r.product_id " +
        "LEFT JOIN jhi_category c ON p.category_id=c.id " +
        "where p.user_id= (?1) and (" +
        "lower(p.name) LIKE lower(concat('%', ?2,'%')) OR " +
        "lower(p.sku) LIKE lower(concat('%', ?2,'%')) OR " +
        "lower(c.title) LIKE lower(concat('%', ?2,'%')) OR " +
        "lower(p.manufacturer) LIKE lower(concat('%', ?2,'%'))" +
        ")",nativeQuery = true)
    public Page<Object[]> findAllProduct(Long userId, String search, Pageable pageable);


/*
    @Query(value = "SELECT product_id,SUM(rating) as sum_rating,COUNT(1) as total_count,(CAST(SUM(rating) as float)/CAST(COUNT(1) as float)) as rating " +
        "FROM jhi_product_user_rating " +
        "WHERE product_id=(?1) GROUP BY product_id",nativeQuery = true)
    public List<Object[]> findRating(Long productId);


    @Query("FROM Product p WHERE p.user=?1")
    public List<Product> getProductByUser(User user);

    @Query(value = "select count(*) from jhi_product p where p.user_id=?1 and vendor_show_status=true and admin_show_status=true",nativeQuery = true)
    BigInteger getCountOfProducts(Long user_id);

    @Query(value = "select parent_id from jhi_category where id = (select parent_id from jhi_category where id = ?1)",nativeQuery = true)
    BigInteger getLevelOneOfCategoryProduct(Long category_id);

 */
}
