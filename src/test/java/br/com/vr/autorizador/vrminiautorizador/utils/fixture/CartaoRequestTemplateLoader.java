package br.com.vr.autorizador.vrminiautorizador.utils.fixture;

import java.math.BigDecimal;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.vr.autorizador.vrminiautorizador.request.CartaoRequest;
import br.com.vr.autorizador.vrminiautorizador.request.TransacaoRequest;

public class CartaoRequestTemplateLoader implements TemplateLoader {
	

    @Override
    public void load() {
    	
        Fixture.of(CartaoRequest.class).addTemplate("createCartao", new Rule(){{
            add("numeroCartao",  "6549873025634777");
            add("senha", "1234");
        }});
        
        Fixture.of(TransacaoRequest.class).addTemplate("transcaoCartaoSucesso", new Rule(){{
            add("numeroCartao",  "6549873025634777");
            add("senhaCartao", "1234");
            add("valor", new BigDecimal(100));
        }});
        
        Fixture.of(TransacaoRequest.class).addTemplate("transcaoCartaoSaldoInsuficiente", new Rule(){{
            add("numeroCartao",  "6549873025634777");
            add("senhaCartao", "1234");
            add("valor", new BigDecimal(600));
        }});
        
        Fixture.of(TransacaoRequest.class).addTemplate("transcaoCartaoSenhaInvalida", new Rule(){{
            add("numeroCartao",  "6549873025634777");
            add("senhaCartao", "12345");
            add("valor", new BigDecimal(100));
        }});
        
        Fixture.of(TransacaoRequest.class).addTemplate("transcaoCartaoCartaoInexistente", new Rule(){{
            add("numeroCartao",  "6549873025634888");
            add("senhaCartao", "1234");
            add("valor", new BigDecimal(100));
        }});
    	
    }

}
