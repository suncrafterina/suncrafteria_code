package suncrafterina.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import suncrafterina.domain.Banner;

import java.util.Optional;

@Repository
public interface BannerRepository extends JpaRepository<Banner,Long> {

    Optional<Banner> findBannerById(Long id);

    @Query(value = "select * from jhi_banner b" , nativeQuery = true)
    Page<Object[]> findALlBanner(Pageable pageable);

    @Query(value = "select b.id,p.name as name," +
        "b.product_id,b.image_file,b.image_file_thumb," +
        "b.is_active,b.created_at " +
        "from jhi_banner b left join jhi_product p on p.id=b.product_id " +
        "where (p.name is not  null and lower(p.name) like lower(concat('%',?1,'%'))) ", nativeQuery = true)
    Page<Object[]> findAllBannerList(String search, Pageable pageable);
}
