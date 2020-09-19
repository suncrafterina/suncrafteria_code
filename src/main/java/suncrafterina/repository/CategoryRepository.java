package suncrafterina.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import suncrafterina.domain.Category;
import suncrafterina.enums.CategoryLevelEnum;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {

    @Query("From Category c where c.parent_id = ?1")
    public List<Category> findCategoriesById(Long category_id);

    @Query("From Category c WHERE c.category_level=(:categoryLevel) AND (:search is null or lower(c.title) like lower(concat('%', :search,'%')))")
    public Page<Category> findCategoriesByLevel(@Param("categoryLevel") CategoryLevelEnum categoryLevel, @Param("search")  String search, Pageable pageable);

    @Query(value="SELECT a.id," +
        "a.title," +
        "a.parent_id," +
        "a.category_level," +
        "a.image_file," +
        "b.subcategory_count," +
        "a.image_file_thumb," +
        "a.icon_file," +
        "a.slug" +
        " FROM jhi_category a LEFT JOIN " +
        " (" +
        "SELECT parent_id, COUNT(1) as subcategory_count" +
        " FROM jhi_category" +
        " WHERE parent_id <> id" +
        " GROUP BY parent_id" +
        ") b" +
        " ON a.id = b.parent_id" +
        " WHERE a.category_level=0 AND a.parent_id=0 AND lower(a.title) like lower(concat('%', ?1,'%')) ",nativeQuery = true)
    public Page<Object[]> findCategoriesLevelOne(String search,Pageable pageable);

    @Query("From Category c where lower(c.title) = lower(?1)")
    public Optional<Category> findByTitle(String title);

//    @Query("From Category c where c.sorting_order = ?1 AND c.parent_id=?2")
 //   public Optional<Category> findBySortingOrder(Long sortingOrder,Long parentId);


    @Query("From Category c where lower(c.title) = lower(?2) AND id <> ?1")
    public Optional<Category> findByIdAndTitle(Long id,String title);

    //@Query("From Category c where c.sorting_order = ?2 AND c.parent_id=?3 AND id <> ?1")
    //public Optional<Category> findByIdAndSortingOrder(Long id, Long sortingOrder, Long parentId);


    @Query(value = "select c.id,c.title," +
        "c.parent_id," +
        "c.category_level," +
        "c.image_file," +
        "p.product_count,c.image_file_thumb,c.icon_file,c.slug from jhi_category c left join " +
        "(select category_id,COUNT(1) as product_count " +
        "from jhi_product group by category_id) p on c.id=p.category_id " +
        "where c.category_level=1 and c.parent_id=?1 and " +
        "lower(c.title) like lower(concat('%', ?2,'%'))",nativeQuery = true)
    public Page<Object[]> findCategoriesLevelTwo(Long category_id,String search,Pageable pageable);
/*
    @Query(value = "select c.id,c.title,c.sorting_order,c.parent_id,c.category_level,c.image_file " +
        "from jhi_category c where c.category_level=2 and c.parent_id=?1 " +
        "and lower(c.title) like lower(concat('%', ?2,'%'))",nativeQuery = true)
        public Page<Object[]> findCategoriesLevelThree(Long category_id,String search,Pageable pageable);
*/

//    @Query(value="select c.id,c.title from jhi_category c where " +
//        "c.category_level = 0 and lower(c.title) like lower(concat('%', ?1,'%')) LIMIT 10 ",nativeQuery=true)
//    List<Object[]> searchByCategoryName(String search);

    @Query(value="select c.id,c.title from jhi_category c where " +
        "lower(c.title) like lower(concat('%', ?1,'%')) LIMIT 10 ",nativeQuery=true)
    List<Object[]> searchByCategoryName(String search);
}
