package suncrafterina.web.rest;

import io.github.jhipster.web.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.zalando.problem.Status;
import suncrafterina.domain.Faqs;
import suncrafterina.security.AuthoritiesConstants;
import suncrafterina.service.dto.FaqsDTO;
import suncrafterina.service.dto.ResponseStatusDTO;
import suncrafterina.service.impl.FaqService;
import suncrafterina.web.rest.errors.CustomException;
import suncrafterina.web.rest.errors.SunCraftStatusCode;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.SUB_ADMIN + "\")")
public class FaqController {

    @Autowired
    private FaqService faqService;

    @PostMapping("/faq")
    public ResponseEntity<ResponseStatusDTO> addFAQs(@Valid @RequestBody FaqsDTO faqDto)
    {
        Faqs faqs1 = faqService.findByTitle(faqDto.getTitle());
        if(faqs1 != null && faqs1.getStatus()){
            throw new CustomException(Status.BAD_REQUEST, SunCraftStatusCode.ALREADY_EXISTS,null);
        }

        Faqs faq = new Faqs();
        faq.setTitle(faqDto.getTitle().trim());
        faq.setAnswer(faqDto.getAnswer().trim());
        faq.setStatus(true);
        faq.setCreated_at(LocalDateTime.now());
        faq = this.faqService.saveFaq(faq);

        ResponseStatusDTO status = new ResponseStatusDTO("FAQ added", HttpStatus.CREATED.value(),System.currentTimeMillis());
        return new ResponseEntity<ResponseStatusDTO>(status,HttpStatus.CREATED);
    }

    @GetMapping("/faq")
    public ResponseEntity<List<Faqs>> getAllFAQs(@RequestParam(required = false) String search,Pageable pageable)
    {
        if(search==null)
            search="_";
        return faqService.findAllFaqs(search,pageable);
    }

    @GetMapping("/faq/{id}")
    public ResponseEntity<Faqs> getFAQsById(@PathVariable("id") Long id)
    {
        Faqs faqs = faqService.findFaqById(id);
        if(faqs==null || !faqs.getStatus())
            throw new CustomException(Status.NOT_FOUND,SunCraftStatusCode.NOT_FOUND,null);

        return new ResponseEntity<>(faqs,HttpStatus.OK);
    }

    @PutMapping("/faq")
    public ResponseEntity<ResponseStatusDTO> updateFAQs(@RequestBody FaqsDTO faqDto)
    {
        Faqs faqs = faqService.findFaqById(faqDto.getId());
        if(faqs==null || !faqs.getStatus())
            throw new CustomException(Status.NOT_FOUND,SunCraftStatusCode.NOT_FOUND,null);
        Faqs faqs1  = faqService.findByTitle(faqDto.getTitle());
        if(faqs1 != null)
        if(faqs.getId() != faqs1.getId() && faqs1.getStatus()){
            throw new CustomException(Status.BAD_REQUEST,SunCraftStatusCode.ALREADY_EXISTS,null);
        }
        faqs.setTitle(faqDto.getTitle().trim());
        faqs.setAnswer(faqDto.getAnswer().trim());
        faqs.setUpdated_at(LocalDateTime.now());
        faqService.saveFaq(faqs);
        ResponseStatusDTO status = new ResponseStatusDTO("FAQ updated", HttpStatus.CREATED.value(),System.currentTimeMillis());
        return new ResponseEntity<ResponseStatusDTO>(status,HttpStatus.OK);
    }

    @DeleteMapping("/faq/{id}")
    public ResponseEntity<ResponseStatusDTO> deleteFAQs(@PathVariable("id") Long id)
    {
        Faqs faqs = faqService.findFaqById(id);
        if(faqs==null)
            throw new CustomException(Status.NOT_FOUND,SunCraftStatusCode.NOT_FOUND,null);

        faqs.setStatus(false);
        faqService.saveFaq(faqs);
        ResponseStatusDTO status = new ResponseStatusDTO("FAQ deleted", HttpStatus.CREATED.value(),System.currentTimeMillis());
        return new ResponseEntity<ResponseStatusDTO>(status,HttpStatus.OK);
    }



}
