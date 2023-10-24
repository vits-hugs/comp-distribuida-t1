package ufsc.br.distribuida.t1;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class MuntjacController {

    @Autowired
    MuntjacRepository muntjacRepository;

    @GetMapping("/muntjacs")
    public ResponseEntity<List<Muntjac>> getAllMuntjacs() {
        try {
            List<Muntjac> muntjacs = new ArrayList<Muntjac>();

            muntjacRepository.findAll().forEach(muntjacs::add);

            if (muntjacs.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(muntjacs, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/muntjacs")
    public ResponseEntity<Muntjac> createMuntjac(@RequestBody Muntjac muntjac) {
        try {
            Muntjac _muntjac = muntjacRepository
                    .save(new Muntjac(muntjac.getNome(), muntjac.getChapeu(), muntjac.getFelicidade()));
            return new ResponseEntity<>(_muntjac, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

