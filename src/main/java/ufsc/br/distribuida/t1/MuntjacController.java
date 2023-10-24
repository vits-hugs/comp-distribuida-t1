package ufsc.br.distribuida.t1;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
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

    public static void main(String[] args) {
        SpringApplication.run(T1Application.class, args);
    }

    @Autowired
    MuntjacRepository muntjacRepository;

    @GetMapping("/muntjacs")
    public ResponseEntity<List<Muntjac>> getAllMuntjacs() {

        try {
            List<Muntjac> muntjacs = new ArrayList<Muntjac>();

            muntjacRepository.findAll().forEach(muntjacs::add);

            if (muntjacs.isEmpty()) {
                return new ResponseEntity<>(muntjacs,HttpStatus.OK);
            }

            return new ResponseEntity<>(muntjacs, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/muntjacs")
    public ResponseEntity<Muntjac> createMuntjac() {
        try {
            Muntjac _muntjac = muntjacRepository
                    .save(new Muntjac("juan", "chapeu de palha", 40.0));
            return new ResponseEntity<>(_muntjac, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

