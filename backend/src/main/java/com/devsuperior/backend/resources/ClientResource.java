package com.devsuperior.backend.resources;

import com.devsuperior.backend.dtos.ClientDto;
import com.devsuperior.backend.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/clients")
public class ClientResource {
    private final ClientService service;

    @Autowired
    public ClientResource(ClientService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<ClientDto>> findAllPaged(
            @RequestParam(name = "page",defaultValue = "0") Integer page,
            @RequestParam(name = "linesPerPage",defaultValue = "5") Integer linesPerPage,
            @RequestParam(name = "direction", defaultValue = "ASC")String direction,
            @RequestParam(name = "orderBy", defaultValue = "name") String orderBy){
        PageRequest pageRequest = PageRequest.of(page,linesPerPage, Sort.Direction.valueOf(direction),orderBy);
        return ResponseEntity.ok().body(service.findAllPaged(pageRequest));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto>findById(@PathVariable Long id){
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ClientDto>insertClient(@RequestBody ClientDto dto){
       ClientDto newClient = service.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/id").buildAndExpand(newClient.getId()).toUri();
       return ResponseEntity.created(uri).body(newClient);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDto>updateClient(@PathVariable Long id, @RequestBody ClientDto dto){
        ClientDto newClient = service.update(id,dto);
        return ResponseEntity.ok().body(newClient);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
