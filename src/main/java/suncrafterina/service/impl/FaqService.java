package suncrafterina.service.impl;

import io.github.jhipster.web.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import suncrafterina.domain.Faqs;
import suncrafterina.repository.FaqsRepository;


import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FaqService {

    @Autowired
    private FaqsRepository faqsRepository;

    public Faqs saveFaq(Faqs faqs){
        return faqsRepository.save(faqs);
    }

    public Faqs findByTitle(String title){
        Optional<Faqs> faqs = faqsRepository.findOneByTitleIgnoreCase(title);
        if(faqs.isPresent())
            return  faqs.get();
        return null;
    }

    public ResponseEntity<List<Faqs>> findAllFaqs(String search, Pageable pageable){

        Page<Faqs> faqsPage = faqsRepository.findAllFaqs(search,pageable);
        List<Faqs> faqsList = new LinkedList<>();
        for (Faqs faqs : faqsPage){

            faqsList.add(new Faqs(faqs.getId(),faqs.getTitle(),faqs.getAnswer(),faqs.getStatus(),faqs.getCreated_at(),
                faqs.getUpdated_at()));
        }
        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), faqsPage);
        return ResponseEntity.ok().headers(headers).body(faqsList);
    }

    public Faqs findFaqById(Long id){
        Optional<Faqs> faqs = faqsRepository.findOneById(id);
        if(faqs.isPresent())
            return faqs.get();
        return null;
    }
}
