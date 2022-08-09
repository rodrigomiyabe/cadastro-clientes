package com.devsuperior.backend.services;

import com.devsuperior.backend.dtos.ClientDto;
import com.devsuperior.backend.entities.Client;
import com.devsuperior.backend.repositories.ClientRepository;
import com.devsuperior.backend.services.exceptions.DatabaseException;
import com.devsuperior.backend.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository repository ;

    @Autowired
    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<ClientDto> findAllPaged(PageRequest pageRequest){
        Page<Client> clientList = repository.findAll(pageRequest);
         return clientList.map(ClientDto::new);
    }
    @Transactional(readOnly = true)
    public ClientDto findById(Long id){
        Optional<Client> obj = repository.findById(id);
        Client client = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new ClientDto(client);
    }

    @Transactional
    public ClientDto insert(ClientDto dto){
        Client client = new Client();
        copyDtoToEntity(dto,client);
        repository.save(client);
        return new ClientDto(client);
    }

    @Transactional
    public ClientDto update(Long id,ClientDto dto){
        try{
            Client client = repository.getReferenceById(id);
            copyDtoToEntity(dto,client);
            repository.save(client);
            return new ClientDto(client);
        }catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("ID not found" + id);
        }
        }

    public void delete(Long id){
        try{
            repository.deleteById(id);
        }catch (EmptyResultDataAccessException e){
            throw new ResourceNotFoundException("ID not found " + id);
        }catch (DataIntegrityViolationException e){
            throw new DatabaseException("Integrity violation");
        }
    }
    private void copyDtoToEntity(ClientDto dto, Client client) {
        client.setName(dto.getName());
        client.setCpf(dto.getCpf());
        client.setIncome(dto.getIncome());
        client.setBirthDate(dto.getBirthDate());
        client.setChildren(dto.getChildren());
    }



}
