package br.com.seap.livraria.services;

import br.com.seap.livraria.models.Livro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface LivroService {

    public Livro salvar(Livro livro);

    public Livro retornar(Long idLivro);

    public void deletar(Livro livro);

    public Livro atualizar(Livro livro);

    public Page<Livro> filtrar(Livro filtro, Pageable pageRequest);


}
