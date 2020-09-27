package suncrafterina.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import suncrafterina.domain.Cms;
import suncrafterina.domain.Faqs;

import java.util.Optional;

@Repository
public interface CmsRepository extends JpaRepository<Cms,Long>{

    Optional<Cms> findOneByTitleIgnoreCase(String title);

    @Query("FROM Cms c WHERE c.status = true AND lower(c.title) LIKE lower(concat('%', ?1,'%'))")
    Page<Cms> findAllCms(String search,Pageable pageable);

    Optional<Cms> findOneById(Long id);
}
