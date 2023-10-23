package ufsc.br.distribuida.t1;
import jakarta.persistence.*; // for Spring Boot 3
// import javax.persistence.*; // for Spring Boot 2
@Entity
@Table(name = "muntjac")
public class Muntjac {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "chapeu")
    private String chapeu;

    @Column(name = "felicidade")
    private double felicidade;

    public Muntjac() {
    }

    public long getId(){
        return id;
    }
    public String getNome(){
        return nome;
    }
    public void setNome(String nome){
        this.nome = nome;
    }
    public String getChapeu(){
        return chapeu;
    }
    public void setChapeu(String chapeu){
        this.chapeu = chapeu;
    }
    public double getFelicidade(){
        return felicidade;
    }
    public void setFelicidade(double felicidade){
        this.felicidade = felicidade;
    }

    public Muntjac(String nome, String chapeu, double felicidade){
        this.nome = nome;
        this.felicidade = felicidade;
        this.chapeu = chapeu;
    }
}
