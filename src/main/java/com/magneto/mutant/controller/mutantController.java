package com.magneto.mutant.controller;

import java.util.List;
import com.magneto.mutant.model.Mutant;
import com.magneto.mutant.model.MutantAux;
import com.magneto.mutant.model.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.magneto.mutant.repository.mutantRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;


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
        JSONObject object = new JSONObject(adn);        
        JSONArray arrJson;
        Mutant mut = new Mutant();
        String arr = "";
        
        arrJson = object.getJSONArray("adn"); 
        for (int i = 0; i < arrJson.length(); i++) {
            arr = arr+arrJson.getString(i);
        }
                
        //--------------- Inicio
        int count=0;
        int resp = 0;
        int x = 0, y = 1;

        try{    
            //Para las filas (Horizontal)
            for (String adn1 : adn.getAdn()) {
                if (resp<2) {
                    while (y < adn1.length() && count<4) {
                        if (adn1.charAt(x) == adn1.charAt(y)) {
                            count++;
                            x++;y++;
                            if(count==3){
                                resp++;
                                count=0;
                                x = x - 2;
                                y = y - 2;
                            }
                        } else {
                            x++;y++;
                            count=0;
                        }
                    }
                    count=0;
                    x=0;
                    y=1;
                }
            }
            //Para las columnas (Vertical)
            for(int c = 0;c < adn.getAdn().length;c++){
                if(resp<2){
                    while(y<adn.getAdn()[c].length() && count<4){
                        if(adn.getAdn()[x].charAt(c)==adn.getAdn()[y].charAt(c)){
                            count++;
                            y++;
                            if(count==3){
                                resp++;
                                count=0;
                                y = y - 2;
                            }
                        }else{
                            y++;
                            count=0;
                        }
                    }
                    count=0;
                    x=0;
                    y=1;
                }
            }
        
            //Para las diagonales (Oblicuo)
            for (String adn1 : adn.getAdn()) {
                if (resp<2) {
                    while (y < adn1.length() && count<4) {
                        if(adn.getAdn()[x].charAt(x)==adn.getAdn()[y].charAt(y)){
                            count++;
                            x++;
                            y++;
                            if(count==3){
                                resp++;
                                count=0;
                                x = x - 2;
                                y = y - 2;
                            }
                        }else{
                            count=0;
                            x++;
                            y++;
                        }
                    }
                    count=0;
                    x=0;
                    y=1;
                }
            }
            
            if (resp>1){
                mut.setAdn(arr);
                mut.setMutante(1);
                repositorio.save(mut);
                return new ResponseEntity<>("Mutante", HttpStatus.OK);
            }else{
                mut.setAdn(arr);
                mut.setMutante(0);
                repositorio.save(mut);
                return new ResponseEntity<>("No-Mutante", HttpStatus.FORBIDDEN);
            }     
        
        }catch (Exception ex){
            return new ResponseEntity<>("Exception: "+ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } //--------------- Fin
    }
    
    @GetMapping("/stats")
    public @ResponseBody Stat stats() throws Exception {
        Stat res = new Stat();
        List<Mutant> listMut,listHuman;
        listMut = repositorio.findBymutante(1);
        listHuman = repositorio.findBymutante(0);
        res.setCount_human_dna(listHuman.size());
        res.setCount_mutant_dna(listMut.size());
        res.setRatio((double)listMut.size()/listHuman.size());
        return res;
    }

}
