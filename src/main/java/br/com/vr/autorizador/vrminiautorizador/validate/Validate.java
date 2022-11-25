package br.com.vr.autorizador.vrminiautorizador.validate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Validate <T, U> {
	
    private final List<Rule<T,U>> rules = new ArrayList<>();
    
    public void validateRule(T objeto,  U comparavel) {
        List<Rule<T,U>> results = rules
                .stream()
                .filter(r -> r.validate(objeto, comparavel))
                .collect(Collectors.toList());
        results.forEach(Rule::error);
    }
    
    public void addRule(Rule<T,U> regra){
        this.rules.add(regra);
    }

}
