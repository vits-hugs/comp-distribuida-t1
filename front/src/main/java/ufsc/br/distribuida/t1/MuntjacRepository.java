package ufsc.br.distribuida.t1;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MuntjacRepository extends JpaRepository<Muntjac, Long>{
    List<Muntjac> findByNome(String nome);
}
