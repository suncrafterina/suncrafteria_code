package suncrafterina.service.impl;

import io.github.jhipster.web.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import suncrafterina.domain.Cms;
import suncrafterina.domain.Faqs;
import suncrafterina.repository.CmsRepository;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CmsService {

    @Autowired
    private CmsRepository cmsRepository;

    public Cms saveCms(Cms cms){
        return cmsRepository.save(cms);
    }

    public Cms findByTitle(String title){
        Optional<Cms> cms = cmsRepository.findOneByTitleIgnoreCase(title);
        if(cms.isPresent())
            return  cms.get();
        return null;
    }

    public ResponseEntity<List<Cms>> findAllCmsPages(String search, Pageable pageable){

        Page<Cms> cmsPage = cmsRepository.findAllCms(search,pageable);
        List<Cms> cmsList = new LinkedList<>();
        for (Cms cms : cmsPage){

            cmsList.add(new Cms(cms.getId(),cms.getTitle(),cms.getDescription(),cms.getSlug(),cms.getStatus(),cms.getCreated_at(),
                cms.getUpdated_at()));
        }
        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), cmsPage);
        return ResponseEntity.ok().headers(headers).body(cmsList);
    }

    public Cms findCmsById(Long id){
        Optional<Cms> cms = cmsRepository.findOneById(id);
        if(cms.isPresent())
            return cms.get();
        return null;
    }
}
