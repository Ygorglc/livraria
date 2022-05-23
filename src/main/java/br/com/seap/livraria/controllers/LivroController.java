package br.com.seap.livraria.controllers;

import br.com.seap.livraria.controllers.request.LivroRequest;
import br.com.seap.livraria.controllers.response.LivroResponse;
import br.com.seap.livraria.exceptions.EntidadeEmUsoException;
import br.com.seap.livraria.models.Livro;
import br.com.seap.livraria.services.impl.LivroServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/livro")
public class LivroController {

    private final LivroServiceImpl livroService;

    private final ModelMapper modelMapper;

    public LivroController(LivroServiceImpl livroService, ModelMapper modelMapper) {
        this.livroService = livroService;
        this.modelMapper = modelMapper;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LivroResponse salvar(@RequestBody LivroRequest livroRequest) {

        Livro livro = new Livro();
        BeanUtils.copyProperties(livroRequest,livro);
        try {
            Livro livroSalvo = Optional.of(livroService.salvar(livro)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            LivroResponse livroResponse = Optional.of(new LivroResponse(livroSalvo.getId(),
                    livroSalvo.getTitulo(),
                    livroSalvo.getAutor(),
                    livroSalvo.getIsbn())).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            return livroResponse;
        }catch (NullPointerException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("{id}")
    public LivroResponse buscar(@PathVariable Long id) {
        Livro livro = livroService.retornar(id);
        return new LivroResponse(livro.getId(), livro.getTitulo(), livro.getAutor(), livro.getIsbn());
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Long id)  {

            Livro livro = livroService.retornar(id);
            try {
               livroService.deletar(livro);
            }catch (Exception e){
                throw new EntidadeEmUsoException("sdfsd");
            }
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public LivroResponse atualizar(@PathVariable Long id, @RequestBody LivroRequest livroRequest){
        Livro livro = livroService.retornar(id);
        livro.setTitulo(livroRequest.titulo());
        livro.setAutor(livroRequest.autor());
        Livro livroAtualizado = livroService.atualizar(livro);

        return new LivroResponse(livro.getId(), livro.getTitulo(), livro.getAutor(), livro.getIsbn());
    }

    @GetMapping
    public PageImpl<LivroResponse> filtrar(LivroRequest request, Pageable pageRequest){
        Livro filtro = new Livro(request.id(), request.titulo(), request.autor(), request.isbn());
        Page<Livro> resultado = livroService.filtrar(filtro, pageRequest);

        List<LivroResponse> lista = resultado.getContent()
                .stream()
                .map(entity -> new LivroResponse(entity.getId(), entity.getTitulo(), entity.getAutor(), entity.getIsbn()))//modelMapper.map(entity, Livro.class))
                .collect(Collectors.toList());
        return new PageImpl<LivroResponse>(lista,  pageRequest, resultado.getTotalElements());
    }
}
