package suncrafterina.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import suncrafterina.domain.Faqs;

import java.util.Optional;

@Repository
public interface FaqsRepository extends JpaRepository<Faqs,Long> {

    Optional<Faqs> findOneByTitleIgnoreCase(String title);

    @Query("FROM Faqs f WHERE f.status = true AND lower(f.title) LIKE lower(concat('%', ?1,'%'))")
    Page<Faqs> findAllFaqs(String search,Pageable pageable);

    Optional<Faqs> findOneById(Long id);

}
