package br.com.seap.livraria.services.impl;

import br.com.seap.livraria.exceptions.EntidadeEmUsoException;
import br.com.seap.livraria.exceptions.EntidadeNaoEncontradaException;
import br.com.seap.livraria.exceptions.NegocioException;
import br.com.seap.livraria.models.Livro;
import br.com.seap.livraria.repositories.LivroRepository;
import br.com.seap.livraria.services.LivroService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

@Service
public class LivroServiceImpl implements LivroService {
    private LivroRepository livroRepository;

    public LivroServiceImpl(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public Livro salvar(Livro livro) {
        if(livroRepository.existsByIsbn(livro.getIsbn()))
            throw new NegocioException("Isbn já cadastrado.");
        return livroRepository.save(livro);
    }

    public Livro retornar(Long idLivro)  {
        return livroRepository.findById(idLivro).orElseThrow(() -> new EntidadeNaoEncontradaException("Livro buscado não existe."));
    }

    public void deletar(Livro livro) throws EntidadeEmUsoException {
        try {
            livroRepository.delete(livro);
        }catch (Exception e){
            throw new EntidadeEmUsoException("O Livro está em uso no momento e não pode ser deletado.");
        }
    }
    public Livro atualizar(Livro livro) {
        try {
            return livroRepository.save(livro);
        }catch (Exception e){
            throw new NegocioException("Não foi possivel atualizar.");
        }
    }

    public Page<Livro> filtrar(Livro filtro, Pageable pageRequest) {
        Example<Livro> exemplo = Example.of(filtro,
                ExampleMatcher.matching()
                        .withIgnoreCase()
                        .withIgnoreNullValues()
                        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
        return livroRepository.findAll(exemplo, pageRequest);
    }
}
