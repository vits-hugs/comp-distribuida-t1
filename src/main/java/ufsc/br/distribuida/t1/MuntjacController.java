package ufsc.br.distribuida.t1;

import java.util.*;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api")
public class MuntjacController {

    @Autowired
    MuntjacRepository muntjacRepository;

    int maxCacheSize = 3;
    LinkedList<Long> cacheOrder = new LinkedList<>();
    Map<Long, Muntjac> cache = new HashMap<>();
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
    @GetMapping("/muntjacs/save/{id}")
    public ResponseEntity<Muntjac> updateMuntjac(@PathVariable("id") long id) {

        boolean completed = saveFromCache(id);

        if (completed) {
            return new ResponseEntity<>(cache.get(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/muntjacs/{id}")
    public ResponseEntity<Muntjac> getMuntjacById(@PathVariable("id") long id) {
        Muntjac muntjac = getFromCache(id);

        if (muntjac == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(muntjac, HttpStatus.OK);
        }
    }

    @PutMapping("/muntjacs/{id}")
    public ResponseEntity<Muntjac> putMuntjacById(@PathVariable("id") long id, @RequestBody Muntjac muntjac) {
        boolean onDB = addToCache(muntjac, id);

        if (onDB) {
            return new ResponseEntity<>(cache.get(id), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private Muntjac getFromCache(long id){
        if(cache.containsKey(id)){
            return cache.get(id);
        }

        Optional<Muntjac> muntjacData = muntjacRepository.findById(id);
        if (!muntjacData.isPresent()){
            return null;
        }
        Muntjac _muntjac = muntjacData.get();

        cache.put(id, _muntjac);
        cacheOrder.add(id);

        if(cacheOrder.size() > maxCacheSize){
            long toRemoveId = cacheOrder.getFirst();
            cacheOrder.removeFirst();

            saveFromCache(toRemoveId);

            cache.remove(toRemoveId);
        }
        return _muntjac;
    }

    private boolean addToCache(Muntjac muntjac, long id){

        if(cache.containsKey(id)){
            Muntjac cachedMuntjac = cache.get(id);
            cachedMuntjac.setNome(muntjac.getNome());
            cachedMuntjac.setChapeu(muntjac.getChapeu());
            cachedMuntjac.setFelicidade(muntjac.getFelicidade());

            cache.put(id, cachedMuntjac);
            return true;
        }

        Optional<Muntjac> muntjacData = muntjacRepository.findById(id);
        if (!muntjacData.isPresent()){
            return false;
        }
        Muntjac _muntjac = muntjacData.get();
        _muntjac.setNome(muntjac.getNome());
        _muntjac.setChapeu(muntjac.getChapeu());
        _muntjac.setFelicidade(muntjac.getFelicidade());

        cache.put(id, _muntjac);
        cacheOrder.add(id);


        if(cacheOrder.size() > maxCacheSize){
            long toRemoveId = cacheOrder.getFirst();
            cacheOrder.removeFirst();

            saveFromCache(toRemoveId);

            cache.remove(toRemoveId);
        }
        return true;
    }
    private boolean saveFromCache(long id){
        Optional<Muntjac> muntjacData = muntjacRepository.findById(id);
        if (!muntjacData.isPresent() || !cache.containsKey(id)){
            return false;
        }
        Muntjac muntjacToUpdate = muntjacData.get();
        Muntjac fromCache = cache.get(id);

        muntjacToUpdate.setNome(fromCache.getNome());
        muntjacToUpdate.setChapeu(fromCache.getChapeu());
        muntjacToUpdate.setFelicidade(fromCache.getFelicidade());
        muntjacRepository.save(muntjacToUpdate);
        return true;
    }
}

