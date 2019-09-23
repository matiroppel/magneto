package com.magneto.mutant.controller;

import java.util.List;
import com.magneto.mutant.model.Mutant;
import com.magneto.mutant.model.MutantAux;
import com.magneto.mutant.model.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.magneto.mutant.repository.mutantRepository;
import org.springframework.http.ResponseEntity;


@RestController
@RequestMapping("/")
public class mutantController {
    
    @Autowired
    mutantRepository repositorio;
        
    // Find
    @GetMapping("/allmutants")
    public List<Mutant> findAll() {
        return repositorio.findAll();
    }    

    // Save
    @PostMapping("/mutant")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> isMutant(@RequestBody MutantAux adn) {
        boolean isMutant = adn.isMutant();
        Mutant mut = new Mutant();
        mut.setAdn(adn.getRawDna());
        mut.setMutant(isMutant);
        repositorio.save(mut);
        String responseBody = isMutant ? "Mutant" : "No-mutant";
        return new ResponseEntity<>(responseBody, isMutant ? HttpStatus.OK : HttpStatus.FORBIDDEN);
    }
    
    @GetMapping("/stats")
    public @ResponseBody Stat stats() {
        Stat res = new Stat();
        List<Mutant> listMut;
        List<Mutant> listHuman;
        listMut = repositorio.findByMutant(true);
        listHuman = repositorio.findByMutant(false);
        res.setCount_human_dna(listHuman.size());
        res.setCount_mutant_dna(listMut.size());
        res.setRatio((double)listMut.size()/listHuman.size());
        return res;
    }

}
