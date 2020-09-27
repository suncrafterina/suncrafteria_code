package suncrafterina.web.rest;

import io.github.jhipster.web.util.PaginationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.zalando.problem.Status;
import suncrafterina.domain.Client;
import suncrafterina.security.AuthoritiesConstants;
import suncrafterina.service.AmazonClient;
import suncrafterina.service.HelperService;
import suncrafterina.service.dto.ClientDTO;
import suncrafterina.service.dto.ClientListDTO;
import suncrafterina.service.dto.ClientUpdateDTO;
import suncrafterina.service.dto.ResponseStatusDTO;
import suncrafterina.service.impl.ClientService;
import suncrafterina.web.rest.errors.CustomException;
import suncrafterina.web.rest.errors.SunCraftStatusCode;

import javax.validation.Valid;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.SUB_ADMIN + "\")")
public class ClientController {

    @Autowired
    ClientService clientService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    private HelperService helperService;

    private AmazonClient amazonClient;

    @Autowired
    public ClientController(AmazonClient amazonClient) {
        this.amazonClient = amazonClient;
    }

    @GetMapping("/client")
    public ResponseEntity<List<ClientListDTO>> getAllClient(@RequestParam(required = false) String search, Pageable pageable){
      if(search==null)
          search="_";
        Page<Object[]> page = clientService.findAllClient(search,pageable);
        List<ClientListDTO> clients = new LinkedList<>();
        for(Object[] obj : page){
            BigInteger id = (BigInteger) obj[0];
            String title = (String) obj[1];
            String image_file = (String) obj[2];
            String image_file_thumb = (String) obj[3];
            Boolean is_active = (Boolean) obj[4];
            Timestamp created_at = (Timestamp) obj[5];
            clients.add(new ClientListDTO(id,title,image_file,image_file_thumb,
                is_active,created_at.toLocalDateTime().toLocalDate()));
        }
        HttpHeaders headers = PaginationUtil
            .generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(clients);

    }

    @PostMapping("/client")
    public ResponseEntity<ResponseStatusDTO> addNewClient(@Valid @ModelAttribute ClientDTO clientDTO, Locale locale){
        Client client1 = clientService.findClientByTitle(clientDTO.getTitle().trim());
        if(client1!=null){
            throw new CustomException(Status.BAD_REQUEST, SunCraftStatusCode.ALREADY_EXISTS,null);
        }
        Client client = new Client();
        client.setTitle(clientDTO.getTitle().trim());
        if(clientDTO.getImage_file()!=null) {
            String image_file = this.amazonClient.uploadFile(clientDTO.getImage_file(),"suncraft_client");
            client.setImage_file(image_file);
            MultipartFile multipartFile=null;
            try{
                multipartFile = helperService.createThumbnail(clientDTO.getImage_file());
                String thumbnail= this.amazonClient.uploadFile(multipartFile,"suncraft_client");
                client.setImage_file_thumb(thumbnail);
            }catch(Exception ex){
                throw new CustomException(Status.BAD_REQUEST, SunCraftStatusCode.IMAGE_NOT_UPLOADED, null);
            }
        }
        client.setIs_active(true);
        client.setCreated_at(LocalDateTime.now());
        clientService.saveClient(client);
        ResponseStatusDTO status = new ResponseStatusDTO("Client added.", HttpStatus.CREATED.value(),System.currentTimeMillis());
        ResponseEntity responseEntity = new ResponseEntity<ResponseStatusDTO>(status,HttpStatus.CREATED);
        return responseEntity;
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id){
        Client client = clientService.getClientById(id);
        if(client == null)
            throw new CustomException(Status.BAD_REQUEST,SunCraftStatusCode.NOT_FOUND,null);
        return new ResponseEntity<>(client,HttpStatus.OK);
    }

    @PutMapping("/client")
    public ResponseEntity<ResponseStatusDTO> addNewClient(@Valid @ModelAttribute ClientUpdateDTO clientUpdateDTO, Locale locale){
        Client client = clientService.getClientById(clientUpdateDTO.getId());
        if(client == null)
            throw new CustomException(Status.BAD_REQUEST,SunCraftStatusCode.NOT_FOUND,null);
        //Client client1 = clientService.findClientByTitle(clientUpdateDTO.getTitle().trim());
        Client client1 = clientService.findByTitleAlreadyExist(clientUpdateDTO.getId(),clientUpdateDTO.getTitle());
        if(client1!=null){
            throw new CustomException(Status.BAD_REQUEST, SunCraftStatusCode.ALREADY_EXISTS,null);
        }
        client.setTitle(clientUpdateDTO.getTitle().trim());
        if(clientUpdateDTO.getImage_file()!=null) {
            String image_file = this.amazonClient.uploadFile(clientUpdateDTO.getImage_file(),"suncraft_client");
            if(client.getImage_file()!=null){
                this.amazonClient.deleteFileFromS3Bucket(client.getImage_file(),"suncraft_client");
            }
            client.setImage_file(image_file);
            MultipartFile multipartFile=null;
            try{
                multipartFile = helperService.createThumbnail(clientUpdateDTO.getImage_file());
                String thumbnail= this.amazonClient.uploadFile(multipartFile,"suncraft_client");
                if(client.getImage_file_thumb()!=null){
                    this.amazonClient.deleteFileFromS3Bucket(client.getImage_file_thumb(),"suncraft_client");
                }
                client.setImage_file_thumb(thumbnail);
            }catch(Exception ex){
                throw new CustomException(Status.BAD_REQUEST, SunCraftStatusCode.IMAGE_NOT_UPLOADED, null);
            }
        }
        client.setUpdated_at(LocalDateTime.now());
        clientService.saveClient(client);
        ResponseStatusDTO status = new ResponseStatusDTO("Client updated", HttpStatus.CREATED.value(),System.currentTimeMillis());
        ResponseEntity responseEntity = new ResponseEntity<ResponseStatusDTO>(status,HttpStatus.CREATED);
        return responseEntity;
    }

    @DeleteMapping("/client/{id}")
    public ResponseEntity<ResponseStatusDTO> deleteBanner(@PathVariable Long id,Locale locale){
        Client client = clientService.getClientById(id);
        if(client==null){
            throw new CustomException(Status.BAD_REQUEST,SunCraftStatusCode.NOT_FOUND,null);
        }
        clientService.deleteClient(client);
        ResponseStatusDTO status = new ResponseStatusDTO("Client deleted", HttpStatus.CREATED.value(),System.currentTimeMillis());
        ResponseEntity responseEntity = new ResponseEntity<ResponseStatusDTO>(status,HttpStatus.CREATED);
        return responseEntity;
    }

    @PutMapping("/client/status/{id}/{status}")
    public ResponseEntity<ResponseStatusDTO> updateBannerStatus(@PathVariable Long id,@PathVariable Boolean status,Locale locale){
        Client client = clientService.getClientById(id);
        if(client==null){
            throw new CustomException(Status.BAD_REQUEST,SunCraftStatusCode.NOT_FOUND,null);
        }
        client.setIs_active(status);
        clientService.saveClient(client);
        ResponseStatusDTO status1 = new ResponseStatusDTO("Client status changed", HttpStatus.CREATED.value(),System.currentTimeMillis());
        ResponseEntity responseEntity = new ResponseEntity<ResponseStatusDTO>(status1,HttpStatus.CREATED);
        return responseEntity;
    }
}
