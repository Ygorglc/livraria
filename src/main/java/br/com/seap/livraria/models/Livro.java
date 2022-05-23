package br.com.seap.livraria.models;

import com.sun.istack.NotNull;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "SI435_LIVRO")
@SequenceGenerator(name = "SI435_LIVRO_SEQ", sequenceName = "SI435_LIVRO_SEQ",allocationSize = 1)
public class Livro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SI435_LIVRO_SEQ")
    @Column(name = "SI435_COD_LIVRO")
    private Long id;

    @Column(name = "SI435_TITULO")
    private String titulo;

    @Column(name = "SI435_AUTOR")
    private String autor;

    @Column(name = "SI435_ISBN")
    private String isbn;

    public Livro(Long id, String titulo, String autor, String isbn) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
    }
    public Livro( String titulo, String autor, String isbn) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
    }

    public Livro() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
