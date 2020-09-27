package suncrafterina.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import suncrafterina.domain.Client;
import suncrafterina.repository.ClientRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class ClientService {

    @Autowired
    ClientRepository clientRepository;

    public Client saveClient(Client client){
        return clientRepository.save(client);
    }

    public Boolean deleteClient(Client client){
        clientRepository.delete(client);
        return true;
    }

    public Client getClientById(Long id){
        Optional<Client> client = clientRepository.findById(id);
        if(client.isPresent())
            return client.get();
        return null;
    }

    public Client findClientByTitle(String title){
        Optional<Client> client = clientRepository.findByTitleIgnoreCase(title);
        if(client.isPresent())
            return client.get();
        return null;
    }

    public Page<Object[]> findAllClient(String search, Pageable pageable){
        return clientRepository.findAllClients(search,pageable);
    }

    public Client findByTitleAlreadyExist(Long id,String title){
        Optional<Client> client = clientRepository.findByTitleAlreadyExist(id,title);
        if(client.isPresent())
            return client.get();
        return null;
    }
}
