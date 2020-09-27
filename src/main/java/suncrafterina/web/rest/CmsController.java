package suncrafterina.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.zalando.problem.Status;
import suncrafterina.domain.Cms;
import suncrafterina.domain.Faqs;
import suncrafterina.security.AuthoritiesConstants;
import suncrafterina.service.HelperService;
import suncrafterina.service.dto.CmsDTO;
import suncrafterina.service.dto.FaqsDTO;
import suncrafterina.service.dto.ResponseStatusDTO;
import suncrafterina.service.impl.CmsService;
import suncrafterina.web.rest.errors.CustomException;
import suncrafterina.web.rest.errors.SunCraftStatusCode;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.SUB_ADMIN + "\")")
public class CmsController {

    @Autowired
    private CmsService cmsService;

    @Autowired
    private HelperService helperService;

    @PostMapping("/cms")
    public ResponseEntity<ResponseStatusDTO> addFAQs(@Valid @RequestBody CmsDTO cmsDTO)
    {
        Cms cms1 = cmsService.findByTitle(cmsDTO.getTitle());
        if(cms1 != null && cms1.getStatus()){
            throw new CustomException(Status.BAD_REQUEST, SunCraftStatusCode.ALREADY_EXISTS,null);
        }

        Cms cms = new Cms();
        cms.setTitle(cmsDTO.getTitle().trim());
        cms.setDescription(cmsDTO.getDescription().trim());
        String slug=helperService.getSlug(cmsDTO.getTitle().trim());
        cms.setSlug(slug);
        cms.setStatus(true);
        cms.setCreated_at(LocalDateTime.now());
        cms = this.cmsService.saveCms(cms);
        ResponseStatusDTO status = new ResponseStatusDTO("CMS page added", HttpStatus.CREATED.value(),System.currentTimeMillis());
        return new ResponseEntity<ResponseStatusDTO>(status,HttpStatus.CREATED);
    }

    @GetMapping("/cms")
    public ResponseEntity<List<Cms>> getAllFAQs(@RequestParam(required = false) String search, Pageable pageable)
    {
        if(search==null)
            search="_";
        return cmsService.findAllCmsPages(search,pageable);
    }

    @GetMapping("/cms/{id}")
    public ResponseEntity<Cms> getCmsById(@PathVariable("id") Long id)
    {
        Cms cms = cmsService.findCmsById(id);
        if(cms == null || !cms.getStatus())
            throw new CustomException(Status.NOT_FOUND,SunCraftStatusCode.NOT_FOUND,null);
        else
        return new ResponseEntity<>(cms,HttpStatus.OK);
    }

    @PutMapping("/cms")
    public ResponseEntity<ResponseStatusDTO> updateCms(@RequestBody CmsDTO cmsDTO)
    {
        Cms cms = cmsService.findCmsById(cmsDTO.getId());
        if(cms==null || !cms.getStatus())
            throw new CustomException(Status.NOT_FOUND,SunCraftStatusCode.NOT_FOUND,null);
        Cms cms1  = cmsService.findByTitle(cmsDTO.getTitle());

        if(cms1 != null)
        if(cms.getId() != cms1.getId() && cms1.getStatus()){
            throw new CustomException(Status.BAD_REQUEST,SunCraftStatusCode.ALREADY_EXISTS,null);
        }
        cms.setTitle(cmsDTO.getTitle().trim());
        cms.setDescription(cmsDTO.getDescription().trim());
        cms.setUpdated_at(LocalDateTime.now());
        String slug=helperService.getSlug(cmsDTO.getTitle().trim());
        cms.setSlug(slug);
        cmsService.saveCms(cms);
        ResponseStatusDTO status = new ResponseStatusDTO("Cms page updated", HttpStatus.CREATED.value(),System.currentTimeMillis());
        return new ResponseEntity<ResponseStatusDTO>(status,HttpStatus.OK);
    }

    @DeleteMapping("/cms/{id}")
    public ResponseEntity<ResponseStatusDTO> deleteFAQs(@PathVariable("id") Long id)
    {
        Cms cms = cmsService.findCmsById(id);
        if(cms==null)
            throw new CustomException(Status.NOT_FOUND,SunCraftStatusCode.NOT_FOUND,null);

        cms.setStatus(false);
        cmsService.saveCms(cms);
        ResponseStatusDTO status = new ResponseStatusDTO("Cms page deleted", HttpStatus.CREATED.value(),System.currentTimeMillis());
        return new ResponseEntity<ResponseStatusDTO>(status,HttpStatus.OK);
    }


}
