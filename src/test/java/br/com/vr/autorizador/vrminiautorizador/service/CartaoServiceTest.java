package br.com.vr.autorizador.vrminiautorizador.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.context.ActiveProfiles;

import br.com.vr.autorizador.vrminiautorizador.domain.Cartao;
import br.com.vr.autorizador.vrminiautorizador.helper.Helper;
import br.com.vr.autorizador.vrminiautorizador.repository.CartaoRepository;

@RunWith(MockitoJUnitRunner.class)
@ActiveProfiles("test")
public class CartaoServiceTest {
	@InjectMocks
	private CartaoService service;
	@Mock
	private CartaoRepository repository;
	
	@Test
	public void testCartaoSaveSucess() {
		Cartao dockMock = Helper.buildGenericCartao();
		when(repository.save(dockMock)).thenReturn(dockMock);
		service.save(dockMock);
		verify(repository).save(Mockito.any());
	}
	
	@Test
	public void testCartaoFindById() {
		Optional<Cartao> dockMock = Optional.of(Helper.buildGenericCartao());
		when(repository.findById(dockMock.get().getNumeroCartao())).thenReturn(dockMock);
		service.findById(dockMock.get().getNumeroCartao());
		verify(repository).findById(dockMock.get().getNumeroCartao());
	}
	
	@Test
	public void testCartaoExistsById() {
		Optional<Cartao> dockMock = Optional.of(Helper.buildGenericCartao());
		when(repository.existsById(dockMock.get().getNumeroCartao())).thenReturn(true);
		service.existsById(dockMock.get().getNumeroCartao());
		verify(repository).existsById(dockMock.get().getNumeroCartao());
	}
	
	@Test
	public void testCartaoUpdate() {
		Cartao dockMock = Helper.buildGenericCartao();
		when(repository.save(dockMock)).thenReturn(dockMock);
		service.save(dockMock);
		verify(repository).save(Mockito.any());
	}
	
	
	@Test
	public void testObterSaldo() {
		Cartao dockMock = Helper.buildGenericCartao();
		when(repository.obterSaldoDoCartao(dockMock.getNumeroCartao())).thenReturn(Optional.of(dockMock.getSaldo()));
		service.obterSaldo(dockMock.getNumeroCartao());
		verify(repository).obterSaldoDoCartao(dockMock.getNumeroCartao());
	}
	
	

}
